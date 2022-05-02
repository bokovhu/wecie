#version 450

layout(location = 0) in vec4 a_position;
layout(location = 1) in vec3 a_normal;
layout(location = 2) in vec2 a_texCoord;
layout(location = 3) in vec4 a_color;

uniform mat4 u_MVP;
uniform mat4 u_model;

out VertexShaderOutput {
    vec3 normal;
    vec2 texCoord;
    vec3 worldPosition;
} shaderOutput;

void main () {
    gl_Position = u_MVP * a_position;
    shaderOutput.normal = normalize (mat3(u_model) * a_normal);
    shaderOutput.texCoord = a_texCoord;
    shaderOutput.worldPosition = vec3 (u_model * a_position);
}