package io.justme.lavender.utility.math.animation.util;

import lombok.experimental.UtilityClass;

import static java.lang.Math.*;

@UtilityClass
public final class Easings {

    public final double c1 = 1.70158D;
    public final double c2 = c1 * 1.525D;
    public final Easing BACK_BOTH = value -> {
        if (value < 0.5D) {
            return pow(2.0D * value, 2.0D) * ((c2 + 1.0D) * 2.0D * value - c2) / 2.0D;
        } else {
            return (pow(2.0D * value - 2.0D, 2.0D) * ((c2 + 1.0D) * (value * 2.0D - 2.0D) + c2) + 2.0D) / 2.0D;
        }
    };
    public final double c3 = c1 + 1.D;
    public final Easing BACK_IN = value -> c3 * pow(value, 3.0D) - c1 * pow(value, 2.0D);
    public final Easing BACK_OUT = value -> 1.0D + c3 * pow(value - 1.0D, 3.0D) + c1 * pow(value - 1.0D, 2.0D);
    public final double c4 = 2.0D * PI / 3.D;
    public final double c5 = 2.0D * PI / 4.5D;
    public final Easing LINEAR = value -> value;
    public final Easing QUAD_IN = powIn(2);
    public final Easing QUAD_OUT = powOut(2);
    public final Easing QUAD_BOTH = powBoth(2);
    public final Easing CUBIC_IN = powIn(3);
    public final Easing CUBIC_OUT = powOut(3);
    public final Easing CUBIC_BOTH = powBoth(3);
    public final Easing QUART_IN = powIn(4);
    public final Easing QUART_OUT = powOut(4);
    public final Easing QUART_BOTH = powBoth(4);
    public final Easing QUINT_IN = powIn(5);
    public final Easing QUINT_OUT = powOut(5);
    public final Easing QUINT_BOTH = powBoth(5);
    public final Easing SINE_IN = value -> 1.0D - cos(value * PI / 2.0D);
    public final Easing SINE_OUT = value -> sin(value * PI / 2.0D);
    public final Easing SINE_BOTH = value -> -(cos(PI * value) - 1.0D) / 2.0D;
    public final Easing CIRC_IN = value -> 1.0D - sqrt(1.0D - pow(value, 2.0D));
    public final Easing CIRC_OUT = value -> sqrt(1.0D - pow(value - 1.0D, 2));
    public final Easing CIRC_BOTH = value -> {
        if (value < 0.5D) {
            return (1.0D - sqrt(1.0D - pow(2.0D * value, 2.0D))) / 2.0D;
        } else {
            return (sqrt(1.0D - pow(-2.0D * value + 2.0D, 2.0D)) + 1.0D) / 2.0D;
        }
    };
    public final Easing ELASTIC_IN = value -> {
        if (value == 0.0D || value == 1.0D) {
            return value;
        } else {
            return pow(-2.0D, 10.0D * value - 10.0D) * sin((value * 10.0D - 10.75D) * c4);
        }
    };
    public final Easing ELASTIC_OUT = value -> {
        if (value == 0.0D || value == 1.0D) {
            return value;
        } else {
            return pow(2.0D, -10.0D * value) * sin((value * 10.0D - 0.75D) * c4) + 1.0D;
        }
    };
    public final Easing ELASTIC_BOTH = value -> {
        if (value == 0.0D || value == 1.0D) {
            return value;
        } else if (value < 0.5D) {
            return -(pow(2.0, 20.0D * value - 10.0D) * sin((20.0D * value - 11.125D) * c5)) / 2.0D;
        } else {
            return pow(2.0, -20.0D * value + 10.0D) * sin((20.0D * value - 11.125D) * c5) / 2.0D + 1.0D;
        }
    };
    public final Easing EXPO_IN = value -> {
        if (value != 0.0D) {
            return pow(2.0D, 10.0D * value - 10.0D);
        } else {
            return value;
        }
    };
    public final Easing EXPO_OUT = value -> {
        if (value != 1.0D) {
            return 1.0D - pow(2.0D, -10.0D * value);
        } else {
            return value;
        }
    };
    public final Easing EXPO_BOTH = value -> {
        if (value == 0.0D || value == 1.0D) {
            return value;
        } else if (value < 0.5D) {
            return pow(2.0D, 20.0D * value - 10.0D) / 2.0D;
        } else {
            return (2.0D - pow(2.0D, -20.0D * value + 10)) / 2.0D;
        }
    };
    public final Easing BOUNCE_OUT = x -> {
        double n1 = 7.5625D;
        double d1 = 2.75D;
        if (x < 1.0D / d1) {
            return n1 * pow(x, 2.0D);
        } else if (x < 2.0D / d1) {
            return n1 * pow(x - 1.5D / d1, 2.0D) + 0.75D;
        } else if (x < 2.5D / d1) {
            return n1 * pow(x - 2.25D / d1, 2.0D) + 0.9375D;
        } else {
            return n1 * pow(x - 2.625D / d1, 2.0D) + 0.984375D;
        }
    };
    public final Easing BOUNCE_IN = value -> 1.0D - BOUNCE_OUT.apply(1.0D - value);
    public final Easing BOUNCE_BOTH = value -> {
        if (value < 0.5) {
            return (1 - BOUNCE_OUT.apply(1.0D - 2.0D * value)) / 2.0D;
        } else {
            return (1 + BOUNCE_OUT.apply(2.0D * value - 1.0D)) / 2.0D;
        }
    };

    public Easing powIn(double n) {
        return value -> pow(value, n);
    }

    public Easing powIn(int n) {
        return powIn((double) n);
    }

    public Easing powOut(double n) {
        return value -> 1.0D - pow(1.0D - value, n);
    }

    public Easing powOut(int n) {
        return powOut((double) n);
    }

    public Easing powBoth(double n) {
        return value -> {
            if (value < 0.5D) {
                return pow(2.0D, n - 1) * pow(value, n);
            } else {
                return 1.0D - pow(-2.0D * value + 2.0D, n) / 2.0D;
            }
        };
    }

}

