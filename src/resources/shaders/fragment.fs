#version 330

const int MAX_POINT_LIGHTS = 5;
const int MAX_SPOT_LIGHTS = 5;

in vec2 outTexCoord;
in vec3 mvVertexNormal;
in vec3 mvVertexPos;

out vec4 fragColor;

uniform sampler2D texture_sampler;
uniform vec3 color;
uniform int useColor;

struct Attenuation
{
    float constant;
    float linear;
    float exponent;
};

struct PointLight
{
    vec3 color;
    // Light position is assumed to be in view coordinates
    vec3 position;
    float intensity;
    Attenuation att;
};

struct DirectionalLight
{
	vec3 color;
	vec3 direction;
	float intensity;
};

struct SpotLight
{
	PointLight pointLight;
	vec3 coneDirection;
	float cutOff;
};

struct Material
{
    vec3 color;
    int useColor;
    float reflectance;
};

uniform vec3 ambientLight;
uniform float specularPower;
uniform Material material;
uniform PointLight pointLights[MAX_POINT_LIGHTS];
uniform SpotLight spotLights[MAX_SPOT_LIGHTS];
uniform DirectionalLight directionalLight;
uniform vec3 camera_pos;

vec4 calcLightColor(vec3 light_color, float light_intensity, vec3 position, vec3 to_light_dir, vec3 normal)
{
    vec4 diffuseColor = vec4(0, 0, 0, 0);
    vec4 specColor = vec4(0, 0, 0, 0);

    // Diffuse Light
    float diffuseFactor = max(dot(normal, to_light_dir), 0.0);
    diffuseColor = vec4(light_color, 1.0) * light_intensity * diffuseFactor;

    // Specular Light
    vec3 camera_direction = normalize(camera_pos - position);
    vec3 from_light_dir = -to_light_dir;
    vec3 reflected_light = normalize(reflect(from_light_dir , normal));
    float specularFactor = max( dot(camera_direction, reflected_light), 0.0);
    specularFactor = pow(specularFactor, specularPower);
    specColor = light_intensity  * specularFactor * material.reflectance * vec4(light_color, 1.0);

    return (diffuseColor + specColor);
}

vec4 calcPointLight(PointLight light, vec3 position, vec3 normal)
{
    vec3 light_direction = light.position - position;
    vec3 to_light_dir  = normalize(light_direction);
    vec4 light_color = calcLightColor(light.color, light.intensity, position, to_light_dir, normal);

    // Apply Attenuation
    float distance = length(light_direction);
    float attenuationInv = light.att.constant + light.att.linear * distance + light.att.exponent * distance * distance;
    return light_color / attenuationInv;
}

vec4 calcSpotLight(SpotLight light, vec3 position, vec3 normal)
{
    vec3 light_direction = light.pointLight.position - position;
    vec3 to_light_dir  = normalize(light_direction);
    vec3 from_light_dir  = -to_light_dir;
    float spot_alfa = dot(from_light_dir, normalize(light.coneDirection));
    
    vec4 color = vec4(0, 0, 0, 0);
    
    if ( spot_alfa > light.cutOff ) 
    {
        color = calcPointLight(light.pointLight, position, normal);
        color *= (1.0 - (1.0 - spot_alfa)/(1.0 - light.cutOff));
    }
    return color;    
}

vec4 calcDirectionalLight(DirectionalLight light, vec3 position, vec3 normal)
{
    return calcLightColor(light.color, light.intensity, position, normalize(light.direction), normal);
}

void main()
{
    vec4 baseColor; 
    if ( material.useColor == 1 )
    {
        baseColor = vec4(material.color, 1);
    }
    else
    {
        baseColor = texture(texture_sampler, outTexCoord);
    }
    vec4 totalLight = vec4(ambientLight, 1.0);
    totalLight += calcDirectionalLight(directionalLight, mvVertexPos, mvVertexNormal);

    for (int i=0; i<MAX_POINT_LIGHTS; i++)
    {
        if ( pointLights[i].intensity > 0 )
        {
            totalLight += calcPointLight(pointLights[i], mvVertexPos, mvVertexNormal); 
        }
    }

    for (int i=0; i<MAX_SPOT_LIGHTS; i++)
    {
        if ( spotLights[i].pointLight.intensity > 0 )
        {
            totalLight += calcSpotLight(spotLights[i], mvVertexPos, mvVertexNormal);
        }
    }
    
    fragColor = baseColor * totalLight;
}