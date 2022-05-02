#version 450

layout (location = 0) in vec3 v_texCoord;

uniform samplerCube u_map;

out vec4 out_finalColor;

void main () {
    float depth = texture (u_map, v_texCoord).x;
    depth = depth * 0.2;

    float x = v_texCoord.x * 0.5 + 0.5;
    float y = v_texCoord.y * 0.5 + 0.5;
    float z = v_texCoord.z * 0.5 + 0.5;

    out_finalColor = vec4 (depth * x, depth * y, depth * z, 1.0);
}