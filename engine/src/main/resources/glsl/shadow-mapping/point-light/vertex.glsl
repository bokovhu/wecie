#version 450

layout(location = 0) in vec4 a_position;

uniform mat4 u_model;
uniform mat4 u_MVP;

layout (location = 0) out vec4 out_fragPosition;

void main () {
    gl_Position = u_MVP * a_position;
    out_fragPosition = u_model * a_position;
}