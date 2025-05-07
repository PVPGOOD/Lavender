#version 120

uniform vec2 size;
uniform vec4 color;
uniform float radius;

void main() {
    vec2 u_st = (gl_TexCoord[0].st * size) - size / 2.0;
    vec2 abs_u_st = abs(u_st);
    vec2 inner = size / 2.0 - radius;

    vec2 delta = max(abs_u_st - inner, 0.0);
    float dist_to_edge = length(delta);

    float antialias = 1.3;
    float alpha = 1.0 - smoothstep(radius - antialias, radius + antialias, dist_to_edge);

    gl_FragColor = vec4(color.rgb, alpha * color.a);
}
