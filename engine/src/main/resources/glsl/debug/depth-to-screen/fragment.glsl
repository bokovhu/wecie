#version 450

uniform float u_scale;
uniform sampler2D u_depthMap;

uniform float u_near;
uniform float u_far;
uniform int u_perspective;

layout (location = 0) in vec2 v_texCoord;

out vec4 out_finalColor;

float linearize (float depth) {
    float z = depth * 2.0 - 1.0;
    return (2.0 * u_near * u_far) / (u_near + u_far - z * (u_far - u_near));
}

void main () {
    float depth = texture (u_depthMap, v_texCoord).x * u_scale;
    depth = 1.0 - (1.0 - depth) * 100.0;
    out_finalColor = vec4 (depth, depth, depth, 1.0);
}