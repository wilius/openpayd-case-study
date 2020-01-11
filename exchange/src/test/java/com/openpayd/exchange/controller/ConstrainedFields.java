package com.openpayd.exchange.controller;

import org.springframework.restdocs.constraints.ConstraintDescriptions;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.util.StringUtils;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.snippet.Attributes.key;

public class ConstrainedFields {

    private final ConstraintDescriptions descriptions;

    ConstrainedFields(Class<?> input) {
        this.descriptions = new ConstraintDescriptions(input);
    }

    public FieldDescriptor withPath(String path) {
        String constraints = StringUtils.collectionToDelimitedString(descriptions.descriptionsForProperty(path), "\n");
        return fieldWithPath(path).attributes(key("constraints").value(constraints));
    }
}