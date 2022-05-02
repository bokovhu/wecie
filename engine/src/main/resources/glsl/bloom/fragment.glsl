#version 450

uniform sampler2D u_image;

layout(location = 0) in vec2 v_texCoord;

out vec4 out_finalColor;

uniform bool u_horizontal;
uniform float u_weights[5] = float[] (0.227027, 0.1945946, 0.1216216, 0.054054, 0.016216);

void main () {
    vec2 offset = 1.0 / textureSize(u_image, 0);
    vec3 result = texture(u_image, v_texCoord).rgb * u_weights [0];
    if (u_horizontal) {
        for (int i = 1; i < 5; i++) {
            result += texture(u_image, v_texCoord + vec2(offset.x * i, 0.0)).rgb * u_weights[i];
            result += texture(u_image, v_texCoord - vec2(offset.x * i, 0.0)).rgb * u_weights[i];
        }
    } else {
        for (int i = 1; i < 5; i++) {
            result += texture(u_image, v_texCoord + vec2(0.0, offset.y * i)).rgb * u_weights[i];
            result += texture(u_image, v_texCoord - vec2(0.0, offset.y * i)).rgb * u_weights[i];
        }
    }
    out_finalColor = vec4(result, 1.0);
}