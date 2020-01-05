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
package org.raml.ramltopojo.extensions.jsr303;

import amf.client.model.domain.AnyShape;
import amf.client.model.domain.PropertyShape;
import amf.client.model.domain.UnionShape;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import org.raml.ramltopojo.EventType;
import org.raml.ramltopojo.extensions.AllTypesPluginHelper;
import org.raml.ramltopojo.extensions.ObjectPluginContext;
import org.raml.ramltopojo.extensions.UnionPluginContext;

import javax.validation.constraints.NotNull;
import java.util.Objects;

import static org.raml.ramltopojo.extensions.jsr303.FacetValidation.addAnnotations;

/**
 * Created by Jean-Philippe Belanger on 12/12/16. Just potential zeroes and ones
 */
public class Jsr303Extension extends AllTypesPluginHelper {

    @Override
  public FieldSpec.Builder fieldBuilt(ObjectPluginContext objectPluginContext, PropertyShape typeDeclaration, FieldSpec.Builder fieldSpec, EventType eventType) {
        AnnotationAdder adder = new AnnotationAdder() {

            @Override
            public TypeName typeName() {
                return fieldSpec.build().type;
            }

            @Override
            public void addAnnotation(AnnotationSpec spec) {
                fieldSpec.addAnnotation(spec);
            }
        };

        addAnnotations((AnyShape) typeDeclaration.range(), adder);
        return fieldSpec;
    }

    @Override
    public FieldSpec.Builder fieldBuilt(UnionPluginContext unionPluginContext, AnyShape ramlType, FieldSpec.Builder fieldSpec, EventType eventType) {
        AnnotationAdder adder = new AnnotationAdder() {

            @Override
            public TypeName typeName() {
                return fieldSpec.build().type;
            }

            @Override
            public void addAnnotation(AnnotationSpec spec) {
                // ignore not null (we are in a union, of course they can be null)
                if (!Objects.equals(spec.type, TypeName.get(NotNull.class))) {
                    fieldSpec.addAnnotation(spec);
                }
            }
        };

        addAnnotations(ramlType, adder);
        return fieldSpec;
    }

    @Override
    public FieldSpec.Builder anyFieldCreated( UnionPluginContext context, UnionShape union, TypeSpec.Builder typeSpec, FieldSpec.Builder anyType, EventType eventType) {
        FacetValidation.addFacetsForBuilt(new AnnotationAdder() {

            @Override
            public TypeName typeName() {
                return anyType.build().type;
            }

            @Override
            public void addAnnotation(AnnotationSpec spec) {

                anyType.addAnnotation(spec);
            }
        });

        return anyType;
    }
}
