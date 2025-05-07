#version 120

uniform vec2 location, size;
uniform vec4 color, outlineColor;
uniform float radius, outlineThickness;

float roundSDF(vec2 p, vec2 b, float r) {
    return length(max(abs(p) - b, 0.0)) - r;
}

void main() {
    vec2 uv = gl_TexCoord[0].st;
    vec2 p = uv * size - size * 0.5;

       float dist = roundSDF(p, size * 0.5 - radius - outlineThickness, radius);

    float aa = 1.5;

    float outer = smoothstep(outlineThickness + aa, outlineThickness - aa, abs(dist));
    float inner = smoothstep(-aa, aa, -dist);

    vec4 mixedColor = mix(outlineColor, color, inner);
    mixedColor.a *= max(outer, inner);

    gl_FragColor = mixedColor;
}
