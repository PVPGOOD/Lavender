#version 120

uniform vec2 location, size;
uniform vec4 color, outlineColor;
uniform float radius, outlineThickness;

float roundSDF(vec2 p, vec2 b, float r) {
    return length(max(abs(p) - b, 0.0)) - r;
}

void main() {
    vec2 rectHalf = size * 0.5;
    float distance = roundSDF(rectHalf - (gl_TexCoord[0].st * size), rectHalf - radius - outlineThickness, radius);

    float blendAmount = smoothstep(0.0, 1.0, abs(distance) - outlineThickness);

    vec4 insideColor = (distance < 0.0) ? color : vec4(outlineColor.rgb, 0.0);

    vec4 finalColor = mix(outlineColor, insideColor, blendAmount);
    gl_FragColor = finalColor;
}
