#version 150 core

in vec2 v_texture_cords;
uniform vec3 texture_color;
uniform sampler2D texture_2d;
out vec4 outColor;

void main()
{
    //outColor = texture(tex, Texcoord);
    //outColor = vec4(triangleColor, 1.0);
    //outColor = vec4(1.0, 0.5, 1.0, 1.0);
    outColor = texture(texture_2d, v_texture_cords) * vec4(texture_color, 1.0);
}