#version 120

uniform sampler2D inTexture, textureToCheck;
uniform vec2 texelSize, direction;
uniform float radius,r,g,b;
uniform float weights[256];

#define offset texelSize * direction

void main() {

    float totalRadius = float(radius);
    vec2 texCoord = gl_TexCoord[0].st;

    if (direction.y > 0 && texture2D(textureToCheck, texCoord).a < 0.00000000000000000000000000000000000001) {
        discard;
    }
    float alpha = texture2D(inTexture, texCoord).a * weights[0];

    for (int i = 1; i <= totalRadius; i++) {
        float total = float(i);

        float weight = weights[i];

        alpha += texture2D(inTexture, texCoord + total * offset).a * weight;
        alpha += texture2D(inTexture, texCoord - total * offset).a * weight;
    }

    gl_FragColor = vec4(r,g,b, alpha);

}
