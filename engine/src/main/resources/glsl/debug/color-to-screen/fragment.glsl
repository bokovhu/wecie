#version 450

uniform sampler2D u_map;

layout (location = 0) in vec2 v_texCoord;

out vec4 out_finalColor;

void main () {
    out_finalColor = texture (u_map, v_texCoord);
}