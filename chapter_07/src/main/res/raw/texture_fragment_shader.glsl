precision mediump float;

uniform sampler2D u_TextureUnit_0;
uniform sampler2D u_TextureUnit_1;
varying vec2 v_TextureCoordinates;

void main()
{
    gl_FragColor = mix(texture2D(u_TextureUnit_0, v_TextureCoordinates), texture2D(u_TextureUnit_1, v_TextureCoordinates), 0.8);
}