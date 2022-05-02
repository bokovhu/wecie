#version 450

uniform sampler2D u_lightingResult;
uniform sampler2D u_bloomMap;
uniform samplerCube u_skymap;
uniform vec2 u_resolution;
uniform int u_useSkymap;

layout(location = 0) in vec3 v_texCoord;

out vec4 out_finalColor;

void main () {

    vec2 uv = gl_FragCoord.xy / u_resolution;
    vec2 betterUv = vec2(uv.x, 1.0 - uv.y);
    vec4 litColor = texture(u_lightingResult, betterUv);
    if (litColor.a > 0.5 || u_useSkymap < 1) {
        out_finalColor = litColor;
    } else {
        out_finalColor = texture(u_skymap, v_texCoord);
    }

    vec4 bloomColor = texture(u_bloomMap, betterUv);
    out_finalColor += bloomColor;

}