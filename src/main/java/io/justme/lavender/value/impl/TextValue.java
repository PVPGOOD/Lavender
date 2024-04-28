package io.justme.lavender.value.impl;

import io.justme.lavender.value.DefaultValue;

import java.util.function.Supplier;

public class TextValue extends DefaultValue<String> {

    public TextValue(String name, String value, Supplier<Boolean> dependency) {
        super(name, value, dependency);
    }

    public TextValue(String name, String value) {
        super(name, value);
    }

}
