#version 450

uniform samplerCube u_skymap;

layout(location = 0) in vec3 v_texCoord;

layout (location = 2) out vec4 o_finalColor;

void main () {
    o_finalColor = vec4 (texture(u_skymap, v_texCoord).rgb, 0.0);
}