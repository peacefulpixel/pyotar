#version 150 core

in vec2 bounds;
in vec2 texture_cords;

uniform vec2  position;
uniform vec2  ortho;
uniform vec2  camera_position;
uniform mat4  transformation;
uniform float depth;

out vec2 v_texture_cords;

void main()
{
    vec2 ortho_point = 1 / ortho;
    v_texture_cords = texture_cords;
    gl_Position = vec4(transformation * vec4(bounds, 0.0, 1.0)) +
                  vec4((position - camera_position) * ortho_point, depth, 0.0);
}