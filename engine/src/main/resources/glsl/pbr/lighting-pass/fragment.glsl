#version 450

precision highp float;

struct GBuffer {
    sampler2D worldPositionMap;
    sampler2D normalAndRoughnessMap;
    sampler2D albedoAndMetallicMap;
    sampler2D ambientOcclusionMap;
};

uniform GBuffer u_gBuffer;

const float GAMMA = 2.2;
const vec3 GAMMAVEC = vec3 (1.0 / GAMMA);
const float PI = 3.141592653;
const float EPSILON = 0.001;
const vec3 diaelectricF0 = vec3 (0.04);

const int LIGHT_TYPE_AMBIENT = 0;
const int LIGHT_TYPE_POINT = 1;
const int LIGHT_TYPE_DIRECTIONAL = 2;
const int LIGHT_TYPE_SPOT = 3;
const int N_LIGHTS = 8;

struct Light {
    int type;
    vec3 position;
    vec3 direction;
    float cutOffAngle;
    float outerCutOffAngle;
    float constant;
    float linear;
    float quadratic;
    vec3 energy;
    bool enabled;
    bool castsShadow;
    mat4 viewProjection;
};

struct Bloom {
    float threshold;
};

struct Camera {
    vec3 position;
};

uniform Light u_lights [N_LIGHTS];
uniform sampler2D u_lightShadowMaps [N_LIGHTS];
uniform samplerCube u_lightShadowCubes [N_LIGHTS];

uniform Camera u_camera;

uniform Bloom u_bloom;

layout(location = 0) in vec2 v_texCoord;

layout(location = 0) out vec4 out_finalColor;
layout(location = 1) out vec4 out_brightMap;

/* PBR stuff */

float ggxDistribution(vec3 N, vec3 H, float roughness) {
    float a = pow (roughness, 2.0);
    float a2 = pow(a, 2.0);
    float NdotH = max(dot(N, H), 0.0);
    float NdotH2 = NdotH * NdotH;

    float nom   = a2;
    float denom = (NdotH2 * (a2 - 1.0) + 1.0);
    denom = PI * denom * denom;

    return nom / denom;
}

float geometrySchlickGGX(float NdotV, float roughness) {
    float r = (roughness + 1.0);
    float k = pow (r, 2.0) / 8.0;

    float nom   = NdotV;
    float denom = NdotV * (1.0 - k) + k;

    return nom / denom;
}

float geometrySmith(vec3 N, vec3 V, vec3 L, float roughness) {
    float NdotV = max(dot(N, V), 0.0);
    float NdotL = max(dot(N, L), 0.0);
    float ggx2 = geometrySchlickGGX(NdotV, roughness);
    float ggx1 = geometrySchlickGGX(NdotL, roughness);

    return ggx1 * ggx2;
}

vec3 Fresnel (float cosTheta, vec3 F0) {
    return F0 - (1.0 - F0) * pow(1.0 - cosTheta, 5.0);
}

/* End of PBR stuff */

vec4 pbr (vec3 worldPosition) {

    vec4 albedoMetallicValue = texture (u_gBuffer.albedoAndMetallicMap, v_texCoord);
    vec4 normalRoughnessValue = texture (u_gBuffer.normalAndRoughnessMap, v_texCoord);
    vec4 aoValue = texture (u_gBuffer.ambientOcclusionMap, v_texCoord);

    // The albedo color of the material
    vec3 albedo = albedoMetallicValue.xyz;

    // The metallic value of the material
    float metallic = albedoMetallicValue.w;

    // The sampled normal vector of the surface
    vec3 normal = normalRoughnessValue.xyz;

    // The roughness of the material
    float roughness = normalRoughnessValue.w;

    // The ambient occlusion value
    float ao = 1.0;

    // If we have an ambient occlusion map
    if (aoValue.a >= 0.99) {
        // Sample the AO value from the map
        ao = aoValue.x;
    }

    // The total cumulated radiance of the lights
    vec3 totalRadiance = vec3 (0.0);

    // Iterate all lightsources
    for (int i = 0; i < N_LIGHTS; i++) {
        Light light = u_lights [i];

        // If this given lightsource is disabled, skip it
        if (light.enabled) {

            // The direction of the light
            vec3 lightDirection = vec3 (0.0);
            // The direction the surface is viewed from
            vec3 viewDirection = normalize (u_camera.position - worldPosition);
            // Used for lighting calculations
            vec3 F0 = diaelectricF0;
            F0 = mix (F0, albedo, metallic);

            if (light.type == LIGHT_TYPE_POINT || light.type == LIGHT_TYPE_SPOT) {
                // Spot and point lights have a given source position,
                // light rays originate from that point, and travel
                // towards the surface
                lightDirection = normalize (light.position - worldPosition);
            } else {
                // Directional light sources have parallel rays
                lightDirection = normalize (light.direction);
            }

            // The shadow factor is used to darken parts of the
            // scene that are in shadow
            float shadowFactor = 1.0;

            // If this given lightsource is a shadow caster
            if (light.castsShadow) {

                if (light.type == LIGHT_TYPE_POINT) {

                    vec3 surfaceToLight = worldPosition - light.position;
                    float distance = length (surfaceToLight);
                    float depth = texture (u_lightShadowCubes [i], surfaceToLight).x;
                    float bias = 0.0001;

                    if (depth < distance - bias) shadowFactor = 0.0;
                    else shadowFactor = 1.0;

                } else if (light.type == LIGHT_TYPE_SPOT) {

                    // Transform the world space surface coordiantes to the
                    // light's own view space
                    vec4 lightSpace = light.viewProjection * vec4 (worldPosition, 1.0);

                    // The NDC coordinates of the surface position in light space
                    vec3 projCoords = lightSpace.xyz / lightSpace.w;

                    // The texture coordinate, used to sample the shadow map
                    vec2 tc = projCoords.xy;
                    // Correct from [-1, 1] to [0, 1]
                    tc = tc * 0.5 + 0.5;

                    // If we are inside the [0, 1] range
                    if (tc.x >= 0.0 && tc.x <= 1.0 && tc.y >= 0.0 && tc.y <= 1.0) {

                        // The distance of the surface from the light is the
                        // depth value in light space
                        float z = 0.5 * projCoords.z + 0.5;

                        vec2 shadowMapPixelOffset = vec2 (1.0) / textureSize(u_lightShadowMaps [i], 0);

                        float bias = max(0.0001, 0.001 * (1.0 - dot(normal, lightDirection)));

                        // Sample the shadow map
                        float depth = 0.0;
                        for (int _y = -1; _y <= 1; _y++) {
                            for (int _x = -1; _x <= 1; _x++) {
                                vec2 sampleUv = tc + vec2 (shadowMapPixelOffset.x * _x, shadowMapPixelOffset.y * _y);
                                depth = texture(u_lightShadowMaps [i], sampleUv).x;

                                // If the shadow map contains a depth value less than
                                // the surface's distance to the light, it means that this
                                // given pixel is in shadow

                                if (depth < (z - bias)) shadowFactor += 0.0;
                                else shadowFactor += 1.0;
                            }
                        }
                        shadowFactor /= 9.0;


                    }

                }

            } else {
                shadowFactor = 1.0;
            }

            // The total contribution to the radiance of this given lightsource
            vec3 radianceContribution = vec3 (0.0, 0.0, 0.0);
            // The factor is used to scale the radiance contribution of
            // spotlights
            float factor = 1.0;

            if (light.type == LIGHT_TYPE_AMBIENT) {
                // In case of ambient lightsources, the radiance contribution
                // is the albedo color, scaled by the ambient occlusion value
                radianceContribution += light.energy * albedo * ao;
            } else {

                // Handling of spotlights
                // Basically we are scaling the radiance according to the
                // angle between the light to surface direction, and the
                // spotlight's general direction vector
                if (light.type == LIGHT_TYPE_SPOT) {
                    float theta = dot (normalize (lightDirection), normalize (-light.direction));
                    float cutOff = cos (light.cutOffAngle);
                    float outerCutoff = cos (light.outerCutOffAngle);
                    float epsilon = cutOff - outerCutoff;
                    factor = clamp ((theta - outerCutoff) / epsilon, 0.0, 1.0);
                }

                // The halfway vector is used for the specular calculation
                vec3 halfway = normalize (viewDirection + lightDirection);
                // The distance from the lightsource to the surface
                float lightDistance = length(light.position - worldPosition);
                float attenuation = 1.0;
                if (light.type == LIGHT_TYPE_POINT || light.type == LIGHT_TYPE_SPOT) {
                    // The attenuation takes into consideration the distance
                    // of the light from the surface, so that the radiance can
                    // be scaled accordingly
                    attenuation = 1.0 / (light.constant + lightDistance * light.linear + pow (lightDistance, 2.0) * light.quadratic);
                }

                // The base radiance of the light, scaled by the attenuation value
                vec3 radiance = light.energy * attenuation;

                // PBR calculations
                float NDF = ggxDistribution (normal, halfway, roughness);
                float G = geometrySmith (normal, viewDirection, lightDirection, roughness);
                vec3 F = Fresnel (max(dot(halfway, viewDirection), 0.0), F0);
                vec3 nominator = NDF * G * F;
                float denominator = 4.0 * max(dot(normal, viewDirection), 0.0) * max(dot(normal, lightDirection), 0.0) + EPSILON;
                vec3 specular = nominator / denominator;

                // The specular component
                vec3 kS = F;
                // The diffuse component
                vec3 kD = vec3(1.0) - kS;
                // The diffuse component is offset by the metallic value
                kD *= 1.0 - metallic;
                float NdotL = max(dot(normal, lightDirection), 0.0);

                // Calculation, and scaling of this light's radiance contribution
                radianceContribution += (kD * albedo / PI + specular) * radiance * NdotL;
                radianceContribution *= factor;

            }

            if (radianceContribution.x < 0.0) radianceContribution.x = 0.0;
            if (radianceContribution.y < 0.0) radianceContribution.y = 0.0;
            if (radianceContribution.z < 0.0) radianceContribution.z = 0.0;
            // The radiance contribution must also be scaled by the shadow factor

            if (!light.castsShadow) {
                totalRadiance += radianceContribution;
            } else {
                totalRadiance += radianceContribution * shadowFactor;
            }

        }

    }

    vec3 pbrColor = totalRadiance;
    if (pbrColor.x < 0.0) pbrColor.x = 0.0;
    if (pbrColor.y < 0.0) pbrColor.y = 0.0;
    if (pbrColor.z < 0.0) pbrColor.z = 0.0;

    return vec4 (pbrColor, 1.0);

}

void main () {
    // Sample the GBuffer
    vec4 worldPositionValue = texture (u_gBuffer.worldPositionMap, v_texCoord);

    if (worldPositionValue.a < 0.5) {
        // This indicates, that there is no surface at this given pixel,
        // instead, we can draw for example a skymap into this pixel,
        // and display the albedo color
        out_finalColor = vec4 (0.0);
    } else {
        // This pixel is occupied by an object, calculate lighting
        out_finalColor = pbr (worldPositionValue.xyz);
    }

    float finalColorBrightness = (out_finalColor.x + out_finalColor.y + out_finalColor.z) / 3.0;
    if (finalColorBrightness > u_bloom.threshold) {
        out_brightMap = out_finalColor;
    } else {
        out_brightMap = vec4(0.0, 0.0, 0.0, 0.0);
    }

}