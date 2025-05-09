package io.justme.lavender.value.impl;

import io.justme.lavender.value.DefaultValue;
import lombok.Getter;
import lombok.Setter;

import java.util.function.Supplier;

@Getter
@Setter
public class NumberRangeValue extends DefaultValue<Float[]> {

    // 下界和上界的当前值
    private Float lowerValue;
    private Float upperValue;

    // 允许的最小和值和最大值
    private final float absoluteMin;
    private final float absoluteMax;
    private final float increment;

    public NumberRangeValue(String name, double lower, double upper, double absoluteMin, double absoluteMax, double increment, Supplier<Boolean> dependency) {
        super(name, new Float[]{(float) lower, (float) upper}, dependency);
        this.absoluteMin = (float) absoluteMin;
        this.absoluteMax = (float) absoluteMax;
        this.increment = (float) increment;
        this.lowerValue = (float) lower;
        this.upperValue = (float) upper;
    }

    public NumberRangeValue(String name, double lower, double upper, double absoluteMin, double absoluteMax, double increment) {
        this(name, lower, upper, absoluteMin, absoluteMax, increment, () -> true);
    }

    public void setLowerValue(Float value) {
        if (value < absoluteMin) {
            value = absoluteMin;
        } else if (value > upperValue) {
            value = upperValue;
        }
        // 根据 increment 取整
        value = Math.round(value / increment) * increment;
        this.lowerValue = value;
        super.setValue(new Float[]{this.lowerValue, this.upperValue});
    }

    public void setUpperValue(Float value) {
        if (value > absoluteMax) {
            value = absoluteMax;
        } else if (value < lowerValue) {
            value = lowerValue;
        }

        value = Math.round(value / increment) * increment;
        this.upperValue = value;
        super.setValue(new Float[]{this.lowerValue, this.upperValue});
    }

    @Override
    public void setValue(Float[] value) {
        if(value == null || value.length != 2) return;
        Float lower = value[0];
        Float upper = value[1];
        if (lower < absoluteMin) lower = absoluteMin;
        if (upper > absoluteMax) upper = absoluteMax;
        if (lower > upper) lower = upper;
        lower = Math.round(lower / increment) * increment;
        upper = Math.round(upper / increment) * increment;
        this.lowerValue = lower;
        this.upperValue = upper;
        super.setValue(new Float[]{this.lowerValue, this.upperValue});
    }

    public String getSuffix() {
        return "";
    }
}