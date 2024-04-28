package io.justme.lavender.value.impl;

import io.justme.lavender.value.DefaultValue;
import lombok.Getter;
import java.util.Arrays;
import java.util.function.Supplier;

@Getter
public class ModeValue extends DefaultValue<String> {

    private final String[] modes;

    public ModeValue(String name, String[] modes, String value) {
        super(name, value);
        this.modes = modes;
        this.setValue(value);
    }

    public ModeValue(String name, String[] modes, String value, Supplier<Boolean> displayable) {
        super(name, value, displayable);
        this.modes = modes;
        this.setValue(value);
    }

    @Override
    public String getValue() {
        return super.getValue();
    }

    public void setMode(String mode) {
        Arrays.stream(modes)
                .filter(e -> e.equalsIgnoreCase(mode))
                .findFirst()
                .ifPresent(this::setValue);
    }

    public boolean isValid(String name) {
        return Arrays.stream(modes)
                .anyMatch(e -> e.equalsIgnoreCase(name));
    }
}
