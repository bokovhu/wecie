#version 450

layout(location = 0) in vec2 v_texCoord;

uniform sampler2D u_input;

out vec4 out_finalColor;

const float GAMMA = 2.2;
const vec3 GAMMAVEC = vec3 (1.0 / GAMMA);
const float PI = 3.141592653;

vec3 toneMap (vec3 color) {
    return pow (color, GAMMAVEC);
}

void main () {
    vec4 before = texture (u_input, v_texCoord);
    out_finalColor = vec4 (toneMap (before.rgb), before.a);
}