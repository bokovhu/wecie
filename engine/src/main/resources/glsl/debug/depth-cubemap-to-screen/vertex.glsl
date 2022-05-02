#version 450

layout (location = 0) in vec4 a_position;

uniform mat4 u_projection;

layout (location = 0) out vec3 v_texCoord;

void main () {
    gl_Position = u_projection * a_position;
    v_texCoord = a_position.xyz;
}