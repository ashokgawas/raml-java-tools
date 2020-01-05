/*
 * Copyright 2013-2017 (c) MuleSoft, Inc.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific
 * language governing permissions and limitations under the License.
 */
package org.raml.testutils.matchers;

import com.squareup.javapoet.*;
import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;

/**
 * Created by Jean-Philippe Belanger on 3/4/17. Just potential zeroes and ones
 */
public class TypeSpecMatchers {

    public static Matcher<TypeSpec> name(Matcher<String> match) {

        return new FeatureMatcher<TypeSpec, String>(match, "type name", "type name") {

            @Override
            protected String featureValueOf(TypeSpec actual) {
                return actual.name;
            }
        };
    }

    public static Matcher<TypeSpec> superInterfaces(Matcher<Iterable<? extends TypeName>> memberMatcher) {

        return new FeatureMatcher<TypeSpec, Iterable<? extends TypeName>>(memberMatcher, "super interfaces", "super interfaces") {

            @Override
            protected Iterable<? extends TypeName> featureValueOf(TypeSpec actual) {

                return actual.superinterfaces;
            }
        };
    }

    public static Matcher<TypeSpec> methods(Matcher<Iterable<? extends MethodSpec>> memberMatcher) {

        return new FeatureMatcher<TypeSpec, Iterable<? extends MethodSpec>>(memberMatcher, "method", "method") {

            @Override
            protected Iterable<? extends MethodSpec> featureValueOf(TypeSpec actual) {

                return actual.methodSpecs;
            }
        };
    }

    public static Matcher<TypeSpec> fields(Matcher<Iterable<? extends FieldSpec>> memberMatcher) {

        return new FeatureMatcher<TypeSpec, Iterable<? extends FieldSpec>>(memberMatcher, "field", "field") {

            @Override
            protected Iterable<? extends FieldSpec> featureValueOf(TypeSpec actual) {

                return actual.fieldSpecs;
            }
        };
    }

    public static Matcher<TypeSpec> innerTypes(Matcher<Iterable<? extends TypeSpec>> typeMatcher) {

        return new FeatureMatcher<TypeSpec, Iterable<? extends TypeSpec>>(typeMatcher, "inner type", "inner type") {

            @Override
            protected Iterable<? extends TypeSpec> featureValueOf(TypeSpec actual) {

                return actual.typeSpecs;
            }
        };
    }

    public static Matcher<TypeSpec> annotations(Matcher<Iterable<? extends AnnotationSpec>> typeMatcher) {

        return new FeatureMatcher<TypeSpec, Iterable<? extends AnnotationSpec>>(typeMatcher, "annotation", "annotation") {

            @Override
            protected Iterable<? extends AnnotationSpec> featureValueOf(TypeSpec actual) {

                return actual.annotations;
            }
        };
    }

}
