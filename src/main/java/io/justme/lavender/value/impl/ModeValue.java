package io.justme.lavender.value.impl;

import io.justme.lavender.value.DefaultValue;

import java.util.function.Supplier;

public class ModeValue<T extends Enum<T>> extends DefaultValue<T> {

    private final T[] values;

    public ModeValue(String name, T value, Supplier<Boolean> dependency) {
        super(name, value, dependency);
        this.values = getEnumConstants();
    }

    public ModeValue(String name, T value) {
        this(name, value, () -> true);
    }

    private T[] getEnumConstants() {
        return (T[]) value.getClass().getEnumConstants();
    }

    public boolean isSelected(T value) {
        return this.value == value;
    }

    public T[] getValues() {
        return values;
    }

}
