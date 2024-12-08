package io.justme.lavender.value.impl;


import io.justme.lavender.value.DefaultValue;
import lombok.Getter;

import java.util.function.Supplier;

@Getter
public class NumberValue extends DefaultValue<Float> {


    private final float min,max,increment;

    public NumberValue(String name, double value, double min, double max, double increment, Supplier<Boolean> dependency) {
        super(name, (float) value, dependency);
        this.min = (float) min;
        this.max = (float) max;
        this.increment = (float) increment;
    }

    public NumberValue(String name, double value, double min, double max, double increment) {
        this(name, value, min, max, increment, () -> true);
    }



    @Override
    public void setValue(Float value) {
        if (this.value != null && this.value.doubleValue() != value.doubleValue()) {
            if (value < min)
                value = min;
            else if (value > max)
                value = max;
        }

        super.setValue(value);
    }

    public String getSuffix(){
        return "";
    }

}
