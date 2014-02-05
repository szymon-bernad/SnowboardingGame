package com.sim8500.snowboardinggame;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

public class GameGLSurfaceView extends GLSurfaceView {

	
	private GameGLRenderer myRenderer;
	
	public GameGLSurfaceView(Context context) 
	{
		super(context);
		GameGLRenderer.gContext = context;
		myRenderer = new GameGLRenderer();

		  // Create an OpenGL ES 2.0 context.
        setEGLContextClientVersion(1);
        
        // Set the Renderer for drawing on the GLSurfaceView
        setRenderer(myRenderer);
        
        // Render the view only when there is a change in the drawing data
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
	}
	
	public GameGLSurfaceView(Context context, AttributeSet attr)
	{
		super(context, attr);
		myRenderer = new GameGLRenderer();
		GameGLRenderer.gContext = context;
		  // Create an OpenGL ES 2.0 context.
        setEGLContextClientVersion(1);
        
        // Set the Renderer for drawing on the GLSurfaceView
        setRenderer(myRenderer);
        
        // Render the view only when there is a change in the drawing data
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
        
	 }
	
	public GameGLRenderer getRenderer()  {  return  myRenderer; }
	
	@Override
	public void onResume()
	{
		super.onResume();
	}
	
	@Override
	public void onPause()
	{
		super.onPause();
	}

}
