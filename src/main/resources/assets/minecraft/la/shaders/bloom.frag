#version 120

uniform sampler2D inTexture, textureToCheck;
uniform sampler1D weightTex;

uniform vec2 texelSize, direction;
uniform float radius, r, g, b;

#define offset texelSize * direction

void main() {
    float totalRadius = radius;
    vec2 texCoord = gl_TexCoord[0].st;

    if (!(direction.y <= 0 || texture2D(textureToCheck, texCoord).a == 0.0)) discard;

    float alpha = texture2D(inTexture, texCoord).a * texture1D(weightTex, 0.0).r;

    for (int i = 1; i <= int(totalRadius); i++) {
        float fIdx = float(i) / 255.0;
        float weight = texture1D(weightTex, fIdx).r;

        alpha += texture2D(inTexture, texCoord + float(i) * offset).a * weight;
        alpha += texture2D(inTexture, texCoord - float(i) * offset).a * weight;
    }

    gl_FragColor = vec4(r, g, b, alpha);
}
