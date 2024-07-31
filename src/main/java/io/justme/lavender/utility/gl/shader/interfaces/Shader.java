package io.justme.lavender.utility.gl.shader.interfaces;

import io.justme.lavender.utility.gl.shader.impl.Bloom;
import io.justme.lavender.utility.gl.shader.impl.GaussianBlur;
import io.justme.lavender.utility.gl.shader.impl.rect.RoundCustomRadiusRect;
import io.justme.lavender.utility.gl.shader.impl.rect.RoundGradientRect;
import io.justme.lavender.utility.gl.shader.impl.rect.RoundOutlineRect;
import io.justme.lavender.utility.gl.shader.impl.rect.RoundRect;

/**
 * @author JustMe.
 * @since 2024/3/9
 **/
public interface Shader {

    RoundRect roundRect = new RoundRect();
    RoundCustomRadiusRect roundCustomRadiusRect = new RoundCustomRadiusRect();
    RoundGradientRect roundGradientRect = new RoundGradientRect();
    RoundOutlineRect roundOutlineRect = new RoundOutlineRect();

    Bloom bloom = new Bloom();
    GaussianBlur gaussian = new GaussianBlur();

}
