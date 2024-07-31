#version 120

uniform vec2 u_size;
uniform vec4 color;
uniform float radiusTopLeft,radiusTopRight,radiusBottomLeft,radiusBottomRight;

void main() {
	vec2 u_st = (gl_TexCoord[0].st * u_size) - u_size / 2.0;

	float radiusX = 0;
	float radiusY = 0;

	if (u_st.x < 0.0) {
		if (u_st.y < 0.0) {
			radiusX = radiusBottomLeft;
			radiusY = radiusBottomLeft;
		} else {
			radiusX = radiusTopLeft;
			radiusY = radiusTopLeft;
		}
	} else {
		if (u_st.y < 0.0) {
			radiusX = radiusBottomRight;
			radiusY = radiusBottomRight;
		} else {
			radiusX = radiusTopRight;
			radiusY = radiusTopRight;
		}
	}

	vec2 mid = u_size / 2.0 - vec2(radiusX, radiusY) - 1.0;
	float distance = length(max(abs(u_st) - mid, 0.0)) - min(radiusX, radiusY);
	float smooths = 1.0 - smoothstep(0.0, 1.0, distance);

	gl_FragColor = vec4(color.rgb, smooths * color.a);
}
