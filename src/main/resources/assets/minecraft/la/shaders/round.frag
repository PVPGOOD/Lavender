#version 120

uniform vec2 size;
uniform vec4 color;
uniform float radius;

void main() {
	vec2 u_st = (gl_TexCoord[0].st * size) - size / 2;
	vec2 abs_u_st = abs(u_st);
	vec2 mid = size / 2 - radius - 1.0;

	float dist_to_edge = length(max(abs_u_st - mid, 0.0)) - radius;
	float smooths = 1.0 - smoothstep(0.0, 1.0, dist_to_edge);

	gl_FragColor = vec4(color.rgb, smooths * color.a);
}
