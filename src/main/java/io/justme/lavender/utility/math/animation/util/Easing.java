package io.justme.lavender.utility.math.animation.util;

@FunctionalInterface
public interface Easing {
    double apply(double value);
}