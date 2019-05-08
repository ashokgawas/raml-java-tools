/*
 * Copyright 2013-2017 (c) MuleSoft, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific
 * language governing permissions and limitations under the License.
 */
package org.raml.pojotoraml.plugins;

//import com.google.common.base.Function;

import com.google.common.collect.Streams;
import org.raml.pojotoraml.RamlAdjuster;
import org.raml.ramltopojo.plugin.PluginManager;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created. There, you have it.
 */
public class PojoToRamlExtensionFactory {

    private static PluginManager pluginManager = PluginManager.createPluginManager("META-INF/pojotoraml-plugin.properties");

    private final Package topPackage;

    public PojoToRamlExtensionFactory(Package topPackage) {
        this.topPackage = topPackage;
    }

    public RamlAdjuster createAdjusters(final Class<?> clazz, final RamlAdjuster... ramlAdjusters) {

        RamlGenerator generator = clazz.getAnnotation(RamlGenerator.class);
        if (generator != null) {
            return new RamlAdjuster.Composite(
                    Streams.concat(Arrays.stream(generator.plugins())
                            .map(ramlGeneratorPlugin -> new RamlAdjuster.Composite(pluginManager.getClassesForName(
                                    ramlGeneratorPlugin.plugin(),
                                    Arrays.asList(ramlGeneratorPlugin.parameters()), RamlAdjuster.class))), Arrays.stream(ramlAdjusters)).collect(Collectors.toList()));
        } else {

            if (topPackage != null) {
                RamlGenerators generators = topPackage.getAnnotation(RamlGenerators.class);

                // get the generator for the class.
                java.util.Optional<RamlGenerator> ramlAdjusterOptional =
                        Arrays.stream(generators.value()).filter(ramlGeneratorForClass -> ramlGeneratorForClass.forClass().equals(clazz))
                                .findFirst()
                                .map(RamlGeneratorForClass::generator);

                java.util.Optional<RamlAdjuster> finalAdjuster = ramlAdjusterOptional.map(new Function<RamlGenerator, RamlAdjuster>() {

                    @Nullable
                    @Override
                    public RamlAdjuster apply(RamlGenerator ramlGenerator) {
                        return new RamlAdjuster.Composite(Streams.concat(
                                Arrays.stream(ramlGenerator.plugins())
                                        .map(ramlGeneratorPlugin -> new RamlAdjuster.Composite(pluginManager.getClassesForName(ramlGeneratorPlugin.plugin(),
                                                Arrays.asList(ramlGeneratorPlugin.parameters()), RamlAdjuster.class))), Arrays.stream(ramlAdjusters)).collect(Collectors.toList()));
                    }
                });

                return finalAdjuster.orElseGet(() -> new RamlAdjuster.Composite(Arrays.asList(ramlAdjusters)));
            } else {

                return new RamlAdjuster.Composite(Arrays.asList(ramlAdjusters));
            }
        }

    }
}
