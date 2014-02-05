package com.sim8500.snowboardinggame;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import javax.vecmath.*;

import javax.microedition.khronos.opengles.GL10;


public class SpriteObject 
{
	protected FloatBuffer 	vertexBuffer;
	protected FloatBuffer 	textureBuffer;
	protected ByteBuffer 	indexBuffer;
	protected Vector2f		mSize;
	protected float			currentScale = 1.f;
	protected Vector3f		mPosition;
    protected int       	textureId;
	protected boolean hasLoadedTex  = false;
	protected int[] textures = new int[1];

	protected float vertices[] = {
        		-0.5f, -0.5f, 0.0f, 
        		0.5f, -0.5f, 0.0f,  
        		0.5f, 0.5f,	0.0f,  
        		-0.5f, 0.5f, 0.0f, };
   
	protected float texture[] = {          
            0.f, 0.f,
            1.f, 0.f,
            1.f, 1.f,
            0.f, 1.f, };
        
	protected byte indices[] = {
                   				0,1,2, 
                   				0,2,3, };
	
	public SpriteObject()
	{
		mSize = new Vector2f(100.f, 100.f);
		mPosition = new Vector3f(0.f, 0.f, 0.f);
	}
	
	public void allocBuffers()
	{
		   ByteBuffer byteBuf = ByteBuffer.allocateDirect(vertices.length * 4);
		   byteBuf.order(ByteOrder.nativeOrder());
		   vertexBuffer = byteBuf.asFloatBuffer();
		   vertexBuffer.put(vertices);
		   vertexBuffer.position(0);

		   byteBuf = ByteBuffer.allocateDirect(texture.length * 4);
		   byteBuf.order(ByteOrder.nativeOrder());
		   textureBuffer = byteBuf.asFloatBuffer();
		   textureBuffer.put(texture);
		   textureBuffer.position(0);

		   indexBuffer = ByteBuffer.allocateDirect(indices.length);
		   indexBuffer.put(indices);
		   indexBuffer.position(0);	
	}
	public float getWidth() {  return currentScale*mSize.x; }
	
	public float getHeight() {  return currentScale*mSize.y; }
	
	public Vector3f  getPos() {  return mPosition; }
	
	public int  getTexId()  { return textureId; }
	
	public void draw(GL10 gl)
	{
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[0]);
      
		gl.glFrontFace(GL10.GL_CCW);
		gl.glEnable(GL10.GL_CULL_FACE);
		gl.glCullFace(GL10.GL_BACK);
           
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);

		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBuffer);
      
		gl.glDrawElements(GL10.GL_TRIANGLES, indices.length, GL10.GL_UNSIGNED_BYTE, indexBuffer);      
      
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glDisable(GL10.GL_CULL_FACE);
	}
	
	public void setTexture(int tex) 
	{
		textures[0] = tex;
	}
	
	public void update(float deltaTime)
	{
			
	}
	
	public void setPos(float px, float py, float pz)
	{
		mPosition.set(px, py, pz);
	}
	
}
