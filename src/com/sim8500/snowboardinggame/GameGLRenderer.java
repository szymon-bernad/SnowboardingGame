package com.sim8500.snowboardinggame;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;
import android.opengl.GLSurfaceView.Renderer;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class GameGLRenderer implements Renderer
{
	public static float SCROLL_BACKGROUND  = .002f;
	public static final int GAME_THREAD_FPS_SLEEP = (1000/60);	
	public static Context gContext;

	private long mLoopRunTime = 0;
	private long mLoopStart = 0;
	private long mLoopEnd = 0;
	private float mScreenWidth = 0.f;
	private float mScreenHeight = 0.f;
	private float mScreenAspect;
	private GameplayEngine  mGameplayEngine;
	private HashMap<Integer, Integer>  mGLTexMap = new HashMap<Integer, Integer>();
	
	// wektor obiektów do narysowania - 
	private Vector<SpriteObject>  mSpritesVec = new Vector<SpriteObject>();
	
	private float		mScrollBg = 0.0f;
	
	@Override
	public void onDrawFrame(GL10 gl) 
	{
		// TODO Auto-generated method stub
		mLoopStart = System.currentTimeMillis();
		mGameplayEngine.updateThick(mLoopRunTime/1000.f);
		
		try {
			if (mLoopRunTime < GAME_THREAD_FPS_SLEEP){
				Thread.sleep(GAME_THREAD_FPS_SLEEP - mLoopRunTime);
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		gl.glClearColor(0.5f, 0.5f, 0.0f, 1.0f);
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		gl.glEnable(GL10.GL_BLEND); 
	    gl.glBlendFunc(GL10.GL_ONE, GL10.GL_ONE_MINUS_SRC_ALPHA); 
	    
		if(mGameplayEngine != null)
		{
			if(mGameplayEngine.areObjectsChanged())
			{
				mSpritesVec = mGameplayEngine.getObjects();
				Iterator<SpriteObject> spritesIt = mSpritesVec.iterator();
				while(spritesIt.hasNext())
				{
					SpriteObject spObject = spritesIt.next();
					spObject.setTexture(loadTexture(gl, spObject.getTexId()));
				}
			}		
		}
		
		ListIterator<SpriteObject> spritesIt = mSpritesVec.listIterator(mSpritesVec.size());
		
		while(spritesIt.hasPrevious())
		{
			SpriteObject spObject = spritesIt.previous();
			gl.glMatrixMode(GL10.GL_MODELVIEW);
			gl.glLoadIdentity();
			gl.glPushMatrix();

			gl.glTranslatef(getScaleFor(spObject.getPos().x), getScaleFor(spObject.getPos().y), spObject.getPos().z);
			float scaleW = getScaleFor(spObject.getWidth());
			float scaleH = getScaleFor(spObject.getHeight());
			
			gl.glScalef(scaleW, scaleH, 1.f);
			spObject.draw(gl);
			gl.glPopMatrix();
		}
	    mLoopEnd = System.currentTimeMillis();
	    mLoopRunTime = ((mLoopEnd - mLoopStart));
	}
	
	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) 
	{
		mScreenWidth = width;
		mScreenHeight = height;
		mGameplayEngine.setGameplayRect(width, height);

			
		gl.glViewport(0, 0, width, height);
		
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		if(width > height)
		{
			mScreenAspect = height/(float)width;
			gl.glOrthof(-0.5f, 0.5f, -mScreenAspect/2f, mScreenAspect/2f, -1f, 1f);
		}
		else
		{
			mScreenAspect = width/(float)height;
			gl.glOrthof(-mScreenAspect/2f, mScreenAspect/2f, -0.5f, 0.5f, -1f, 1f);
		}
		
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) 
	{	
		gl.glEnable(GL10.GL_TEXTURE_2D);
		gl.glClearDepthf(1.0f);
		gl.glEnable(GL10.GL_DEPTH_TEST);
		gl.glDepthFunc(GL10.GL_LEQUAL);
		
		mGLTexMap.clear();
		
		mGameplayEngine.generateAlphaObjects();

		mSpritesVec = mGameplayEngine.getObjects();
		Iterator<SpriteObject> spritesIt = mSpritesVec.iterator();
		while(spritesIt.hasNext())
		{
			SpriteObject spObject = spritesIt.next();
			spObject.setTexture(loadTexture(gl, spObject.getTexId()));
		}	
		
	}
	
	public void setGameplayEngine(GameplayEngine gpe)
	{
		mGameplayEngine = gpe;
	}
	
	private float getScaleFor(float value)
	{
		if(mScreenWidth == 0.f || mScreenHeight == 0.f)
			return value;
		
		float scale = (mScreenWidth > mScreenHeight) ? value/mScreenWidth : value/mScreenHeight;
		
		return scale;	
	}
	
	public int loadTexture(GL10 gl, int textureId)
	{
		Integer tex = mGLTexMap.get(textureId);
		if(tex != null)
		{
			return tex;
		}
		
		InputStream imagestream = gContext.getResources().openRawResource(textureId);
		Bitmap bitmap = null;
		try 
		{
			bitmap = BitmapFactory.decodeStream(imagestream);
		} 
		catch (Exception e) 
		{ } 
		finally 
		{
			// Always clear and close
			try 
			{
				imagestream.close();
				imagestream = null;
			} 
			catch (IOException e) 
			{  }

		}
	
		if(bitmap != null)
		{
			int[] texes = new int[1];
			
			gl.glGenTextures(1, texes, 0);
			gl.glBindTexture(GL10.GL_TEXTURE_2D, texes[0]);
	
			gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
			gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
	
			gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_CLAMP_TO_EDGE);
			gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_CLAMP_TO_EDGE);
	
			GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
			mGLTexMap.put(textureId, texes[0]);
			return texes[0];
		}
		return 0;
	}
	
}
