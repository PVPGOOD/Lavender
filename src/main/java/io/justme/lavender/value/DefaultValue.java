package io.justme.lavender.value;

import lombok.Getter;

import java.util.function.Supplier;

@Getter
public class DefaultValue<T> {

    protected final String name;

    protected final Supplier<Boolean> dependency;

    @Getter
    protected T value;

    public DefaultValue(String name, T value, Supplier<Boolean> dependency) {
        this.name = name;
        this.value = value;
        this.dependency = dependency;
    }

    public DefaultValue(String name, T value) {
        this(name, value, () -> true);
    }

    public boolean isAvailable() {
        return dependency.get();
    }

    public void setValue(T value) {
        T oldValue = this.value;
        this.value = value;
    }

    public Class<?> getType() {
        return value.getClass();
    }

}
