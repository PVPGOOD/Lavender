package io.justme.lavender.value.impl;

import io.justme.lavender.value.DefaultValue;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

public class MultiBoolValue extends DefaultValue<List<BoolValue>> {

    public MultiBoolValue(String name, Supplier<Boolean> dependency, BoolValue... value) {
        super(name,  Arrays.asList(value) ,dependency);
    }

    public MultiBoolValue(String name, BoolValue... value) {
        super(name,  Arrays.asList(value) ,() -> true);
    }

    public BoolValue find(String name) {
        return value.stream()
                .filter(value -> value.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

}
