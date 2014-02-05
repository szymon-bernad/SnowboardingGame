package com.sim8500.snowboardinggame;

import android.app.Fragment;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.DisplayMetrics;
import android.view.Surface;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnTouchListener;
import android.content.Context;



public class GameFragment extends Fragment implements SensorEventListener, OnTouchListener
{
	public class GameSensorData
	{
	    public float				mSensorX;
	    public float				mSensorY;
	    public long 				mSensorTimeStamp;
	    public long 				mCpuTimeStamp;
	    public float				mXDpi;
	    public float				mYDpi;
	    public float				mMetersToPixelsX;
	    public float				mMetersToPixelsY;

	}
	
	
    protected GameSensorData		mSensorData = new GameSensorData();
    private SensorManager 		mSensorManager;
    private WindowManager 		mWindowManager;
    private Display 			mDisplay;
    private Sensor				mAccelerometer;
    private Sensor				mGravityMeter;
    
    private GameplayEngine		mGameplayEngine;
	private GameGLSurfaceView mGameView;
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) 
    {
        // Inflate the layout for this fragment
    	View v = inflater.inflate(R.layout.game_fragment_layout, container, false);
    	mGameView = (GameGLSurfaceView)v.findViewById(R.id.game_frag_view);
    	mGameplayEngine = new GameplayEngine();
		mGameView.getRenderer().setGameplayEngine(mGameplayEngine);
		
		GameActivity actv = (GameActivity)getActivity();
		
		mGameplayEngine.setHandler(actv.getHandler());
		// Get an instance of the SensorManager
        mSensorManager = (SensorManager) actv.getSystemService(Context.SENSOR_SERVICE);
        // Get an instance of the WindowManager
        mWindowManager = (WindowManager) actv.getSystemService(Context.WINDOW_SERVICE);
        
        mDisplay = mWindowManager.getDefaultDisplay();
        
		mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

		mGravityMeter = mSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
		
        DisplayMetrics metrics = new DisplayMetrics();
        mWindowManager.getDefaultDisplay().getMetrics(metrics);
        mSensorData.mXDpi = metrics.xdpi;
        mSensorData.mYDpi = metrics.ydpi;
        mSensorData.mMetersToPixelsX = mSensorData.mXDpi / 0.0254f;
        mSensorData.mMetersToPixelsY = mSensorData.mYDpi / 0.0254f;
    	
        v.setOnTouchListener(this);
        return v;
    }

	 @Override
	 public void onResume() 
	 {
	     super.onResume();
	     mGameView.onResume();
	     
	     if(mGravityMeter != null)
	    	 mSensorManager.registerListener(this, mGravityMeter, SensorManager.SENSOR_DELAY_GAME);
	     else	if(mAccelerometer != null)
	    	 mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
	 }

	 @Override
	 public void onPause() 
	 {
		 super.onPause();
		 mGameView.onPause();
		 mSensorManager.unregisterListener(this);
	 }
	
	 
	  /* implementation of SensorEventListener */
	    @Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) 
	    {
	    	
	    }
	 
	    
	   /* implementation of SensorEventListener */
	   @Override
	public void onSensorChanged(SensorEvent event) {
	        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
	        {
		        /*
		         * record the accelerometer data, the event's timestamp as well as
		         * the current time. The latter is needed so we can calculate the
		         * "present" time during rendering. In this application, we need to
		         * take into account how the screen is rotated with respect to the
		         * sensors (which always return data in a coordinate space aligned
		         * to with the screen in its native orientation).
		         */
	
		        switch (mDisplay.getRotation()) 
		        {
		            case Surface.ROTATION_0:
		            	mSensorData.mSensorX = event.values[0];
		            	mSensorData.mSensorY = event.values[1];
		                break;
		            case Surface.ROTATION_90:
		            	mSensorData.mSensorX = -event.values[1];
		            	mSensorData.mSensorY = event.values[0];
		                break;
		            case Surface.ROTATION_180:
		            	mSensorData. mSensorX = -event.values[0];
		            	mSensorData.mSensorY = -event.values[1];
		                break;
		            case Surface.ROTATION_270:
		            	mSensorData.mSensorX = event.values[1];
		            	mSensorData.mSensorY = -event.values[0];
		                break;
		        }
		     
		        mSensorData.mSensorTimeStamp = event.timestamp;
		        mSensorData.mCpuTimeStamp = System.nanoTime();
		        mGameplayEngine.updateSensorData(mSensorData);
	        }
	        
	       if(event.sensor.getType() == Sensor.TYPE_GRAVITY)
	       {
	    	   switch (mDisplay.getRotation()) 
	    	   {
			       case Surface.ROTATION_0:
		           	mSensorData.mSensorX = event.values[0];
		           	mSensorData.mSensorY = event.values[1];
		               break;
		           case Surface.ROTATION_90:
		           	mSensorData.mSensorX = -event.values[1];
		           	mSensorData.mSensorY = event.values[0];
		               break;
		           case Surface.ROTATION_180:
		           	mSensorData. mSensorX = -event.values[0];
		           	mSensorData.mSensorY = -event.values[1];
		               break;
		           case Surface.ROTATION_270:
		           	mSensorData.mSensorX = event.values[1];
		           	mSensorData.mSensorY = -event.values[0];
		               break;
	    	   }
    
	    	   mSensorData.mSensorTimeStamp = event.timestamp;
	    	   mSensorData.mCpuTimeStamp = System.nanoTime();
	    	   mGameplayEngine.updateSensorData(mSensorData);  	
	      }
	   }
	   
	    public boolean onTouch(View v, MotionEvent event)
	    {
			mGameplayEngine.processMotionEvent(event);
			return true;
		}
		
		public GameplayEngine  getGameplayEngine()
		{
			return  mGameplayEngine;
		}

}
