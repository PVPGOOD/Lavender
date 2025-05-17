#version 120

uniform vec2 u_resolution;
uniform float u_time;

uniform float u_nscaleTL;
uniform float u_namountTL;
uniform float u_nscaleBR;
uniform float u_namountBR;
uniform float u_nscaleBL;
uniform float u_namountBL;
uniform float u_blend_zone_uv;

uniform vec4 u_shapeColorTL;
uniform vec4 u_shapeColorBR;
uniform vec4 u_shapeColorBL;
uniform vec3 u_bgColor;

vec2 hash2(vec2 p) {
    p = fract(p * vec2(123.456, 789.123));
    p += dot(p, p + 56.78);
    return fract(p);
}

float noise(vec2 p) {
    vec2 ip = floor(p);
    vec2 fp = fract(p);
    vec2 u = fp * fp * (3.0 - 2.0 * fp);
    float res = mix(
    mix(hash2(ip).x, hash2(ip + vec2(1.0, 0.0)).x, u.x),
    mix(hash2(ip + vec2(0.0, 1.0)).x, hash2(ip + vec2(1.0, 1.0)).x, u.x), u.y);
    return res * 2.0 - 1.0;
}

vec2 noise_vec(vec2 p) {
    return vec2(noise(p), noise(p + vec2(99.1, 33.7)));
}

float sdfEllipse(vec2 p, vec2 center, vec2 radii) {
    vec2 d = (p - center) / radii;
    return (length(d) - 1.0) * min(radii.x, radii.y);
}

void main() {
    vec2 fragCoord = gl_FragCoord.xy;
    vec2 uv = fragCoord / u_resolution;
    float aspect = u_resolution.x / u_resolution.y;
    uv.x *= aspect;
    float t = u_time * 0.5;

    vec2 radiiTL = vec2(0.65, 0.25);
    vec2 centerTL = vec2(radiiTL.x - 0.1, 1.0 - radiiTL.y + 0.1);

    vec2 radiiBR = vec2(0.4, 0.2);
    vec2 centerBR = vec2(aspect - radiiBR.x + 0.1, radiiBR.y - 0.1);

    vec2 radiiBL = vec2(0.5, 0.3);
    vec2 centerBL = vec2(radiiBL.x - 0.4, radiiBL.y - 0.3);

    vec2 nTL_raw = noise_vec(uv * u_nscaleTL + vec2(t, -t));
    vec2 nBR_raw = noise_vec(uv * u_nscaleBR + vec2(-t, t));
    vec2 nBL_raw = noise_vec(uv * u_nscaleBL + vec2(t * 0.7, t * 0.3));

    float bTL = smoothstep(0.0, u_blend_zone_uv, min(uv.x, 1.0 - uv.y));
    float bBR = smoothstep(0.0, u_blend_zone_uv, min(aspect - uv.x, uv.y));
    float bBL = smoothstep(0.0, u_blend_zone_uv, min(uv.x, uv.y));

    vec2 dTL = vec2(max(0.0, nTL_raw.x), min(0.0, nTL_raw.y));
    vec2 dBR = vec2(min(0.0, nBR_raw.x), max(0.0, nBR_raw.y));
    vec2 dBL = vec2(max(0.0, nBL_raw.x), max(0.0, nBL_raw.y));

    vec2 pTL = mix(dTL, nTL_raw, bTL) * u_namountTL;
    vec2 pBR = mix(dBR, nBR_raw, bBR) * u_namountBR;
    vec2 pBL = mix(dBL, nBL_raw, bBL) * u_namountBL;

    float dTLv = sdfEllipse(uv + pTL, centerTL, radiiTL);
    float dBRv = sdfEllipse(uv + pBR, centerBR, radiiBR);
    float dBLv = sdfEllipse(uv + pBL, centerBL, radiiBL);

    float d_out = max(max(-uv.x, uv.x - aspect), max(-uv.y, uv.y - 1.0));

    float minD = dTLv;
    int idx = 0;
    if (dBRv < minD) { minD = dBRv; idx = 1; }
    if (dBLv < minD) { minD = dBLv; idx = 2; }
    float dist = max(minD, d_out);

    float edge = 0.005;
    float a = smoothstep(edge, -edge, dist);

    vec4 sc;
    if (idx == 0) sc = u_shapeColorTL;
    else if (idx == 1) sc = u_shapeColorBR;
    else sc = u_shapeColorBL;

    vec3 finalC = mix(u_bgColor, sc.rgb, a * sc.a);
    gl_FragColor = vec4(finalC, 1.0);
}
