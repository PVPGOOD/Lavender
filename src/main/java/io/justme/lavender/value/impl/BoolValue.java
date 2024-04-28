package io.justme.lavender.value.impl;

import io.justme.lavender.value.DefaultValue;

import java.util.function.Supplier;

public final class BoolValue extends DefaultValue<Boolean> {

    public BoolValue(final String name, final boolean value) {
        super(name, value, () -> true);
    }

    public BoolValue(final String name, final boolean value, final Supplier<Boolean> dependency) {
        super(name, value, dependency);
    }

}
