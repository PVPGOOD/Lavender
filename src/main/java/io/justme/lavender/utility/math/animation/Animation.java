package io.justme.lavender.utility.math.animation;

import io.justme.lavender.utility.math.MathUtility;
import io.justme.lavender.utility.math.animation.util.Easing;
import io.justme.lavender.utility.math.animation.util.Easings;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.val;

@Data
@NoArgsConstructor
public class Animation {

    private long start;
    private float duration;

    private float fromValue;

    private float toValue;

    private float value;

    private Easing easing;

    public Animation(float initValue) {
        this.fromValue = initValue;
        this.toValue = initValue;
        this.value = initValue;
    }

    public void setValue(float value) {
        this.fromValue = value;
        this.toValue = value;
        this.value = value;
    }

    public void animate(float valueTo, float duration) {
        animate(valueTo, duration, Easings.LINEAR);
    }

    public void animate(float valueTo, float duration, Easing easing) {
        if (this.isAlive() && (valueTo == this.getToValue() || valueTo == this.getValue())) {
            return;
        }

        this.easing = easing;
        this.duration = duration * 1000;
        this.start = System.currentTimeMillis();
        this.fromValue = this.value;
        this.toValue = valueTo;
    }

    public boolean update() {
        val alive = isAlive();

        if (alive) {
            value = MathUtility.interpolate(fromValue, toValue, easing.apply(calculatePart())).floatValue();
        } else {
            start = 0;
            value = toValue;
        }

        return alive;
    }

    public boolean isAlive() {
        return !isDone();
    }

    public boolean isDone() {
        return calculatePart() >= 1F;
    }

    public float calculatePart() {
        return (float) (System.currentTimeMillis() - start) / duration;
    }

}
