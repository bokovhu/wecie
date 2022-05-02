#version 450

layout(location = 0) in vec4 a_position;
layout(location = 2) in vec2 a_texCoord;

layout (location = 0) out vec2 v_texCoord;

void main () {
    gl_Position = a_position;
    v_texCoord = vec2(a_texCoord.x, 1.0 - a_texCoord.y);
}