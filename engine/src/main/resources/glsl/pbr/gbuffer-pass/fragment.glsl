#version 450

struct Material {
    sampler2D albedoMap;
    sampler2D metallicMap;
    sampler2D roughnessMap;
    sampler2D aoMap;
    sampler2D normalMap;
    int hasAoMap;
    int hasNormalMap;
};

uniform Material u_material;

in VertexShaderOutput {
    vec3 normal;
    vec2 texCoord;
    vec3 worldPosition;
} shaderInput;

layout(location = 0) out vec4 out_worldPosition;
layout(location = 1) out vec4 out_normalAndRoughness;
layout(location = 2) out vec4 out_albedoAndMetallic;
layout(location = 3) out vec4 out_ao;

vec3 normalMapping() {

    if (u_material.hasNormalMap <= 0) return shaderInput.normal;

    vec3 tangentNormal = texture(u_material.normalMap, shaderInput.texCoord).xyz * 2.0 - 1.0;

    vec3 Q1  = dFdx(shaderInput.worldPosition);
    vec3 Q2  = dFdy(shaderInput.worldPosition);
    vec2 st1 = dFdx(shaderInput.texCoord);
    vec2 st2 = dFdy(shaderInput.texCoord);

    vec3 N   = normalize(shaderInput.normal);
    vec3 T  = normalize(Q1 * st2.t - Q2 * st1.t);
    vec3 B  = -normalize(cross(N, T));
    mat3 TBN = mat3(T, B, N);

    return normalize(TBN * tangentNormal);
}

void main () {

    // R G B of world position map is vertex X Y Z coordinates
    // World position map in the G-Buffer is cleared with
    // vec4 (0.0, 0.0, 0.0, 0.0), geometry is only present
    // at pixels where A value of the map is set to 1.0
    out_worldPosition = vec4 (shaderInput.worldPosition, 1.0);

    // R G B of normal and roughness map is the normal vector
    // of the geometry after normal mapping, A value is the
    // material's roughness
    out_normalAndRoughness = vec4 (
        normalMapping (),
        texture (u_material.roughnessMap, shaderInput.texCoord).r
    );

    // This map contains the albedo (diffuse) color at its
    // R G B values, and contains the material's metallic
    // property in the A value
    out_albedoAndMetallic = vec4 (
        texture (u_material.albedoMap, shaderInput.texCoord).rgb,
        texture (u_material.metallicMap, shaderInput.texCoord).r
    );

    if (u_material.hasAoMap > 0) {
        out_ao = vec4 (
            texture (u_material.aoMap, shaderInput.texCoord).r,
            0.0, 0.0, 1.0
        );
    } else {
        out_ao = vec4 (0.0);
    }

}