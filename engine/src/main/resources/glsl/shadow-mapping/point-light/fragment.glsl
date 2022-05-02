#version 450
layout (location = 0) in vec4 in_fragPosition;

uniform vec3 u_lightPosition;

out float out_finalColor;

void main () {
    float ld = length (in_fragPosition.xyz - u_lightPosition);
    out_finalColor = ld;
}