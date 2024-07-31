#version 120

uniform sampler2D textureIn;
uniform vec2 texelSize, direction;
uniform float radius,depth,r,g,b;
uniform float weights[256];

#define offset texelSize * direction

void main() {
    vec2 texCoord = gl_TexCoord[0].st;
    vec3 alpha = texture2D(textureIn, texCoord).rgb * weights[0];

    for (float f = 1.0; f <= radius; f++) {
        vec2 sampleCoord1 = texCoord + f * offset;
        vec2 sampleCoord2 = texCoord - f * offset;

        vec3 sampleColor1 = texture2D(textureIn, sampleCoord1).rgb;
        vec3 sampleColor2 = texture2D(textureIn, sampleCoord2).rgb;

        float weight = weights[int(abs(f))];

        alpha += sampleColor1 * weight;
        alpha += sampleColor2 * weight;
    }

    alpha *= depth + vec3(r, g, b);

    gl_FragColor = vec4(alpha, 1.0);
}