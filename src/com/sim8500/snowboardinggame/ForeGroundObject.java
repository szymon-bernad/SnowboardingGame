package com.sim8500.snowboardinggame;

import com.sim8500.snowboardinggame.GameFragment.GameSensorData;;


public class ForeGroundObject extends SpriteObject
{
       private float 	mAccelX;
       private float 	mAccelY;
       private float 	mLastPosX;
       private float 	mLastPosY;
       private float 	mOneMinusFriction;
       private long 	mLastT = 0;
       private float 	mLastDeltaT = 0f;
       private float	mJumpTime = 0.f;
       private boolean	mIsJumping = false;
       private float	mBoundingRadius = 0.f;
       
       public static final float sFriction = 0.12f;
	   public static final float sJumpDuration = 0.35f;
	   public static final float sJumpMaxScale = 1.5f;
	   
		protected float customUVs[] = {          
                0.f, 0.f,
                0.2f, 0.f,
                0.2f, 1.f,
                0.f, 1.f, };
	   
	   public ForeGroundObject()
	   {  
		   super();
		   final float r = ((float) Math.random() - 0.5f) * 0.2f;
           mOneMinusFriction = 1.0f - sFriction + r;
           textureId = R.drawable.fg_ex;
           mSize.set(150.f, 150.f);
           mBoundingRadius = 60.f;
           texture = customUVs;
           mPosition.setZ(0.75f);
           allocBuffers();
	   }
	   
	   @Override
	public void update(float deltaTime)
	   {
		   if(mIsJumping)
		   {
			   mJumpTime += deltaTime;
			   float halfDuration = 0.5f*sJumpDuration;
			   
			   if(mJumpTime >= sJumpDuration)
			   {
				   mIsJumping = false;
				   currentScale = 1.f;
			   }
			   else if(mJumpTime >= halfDuration)
			   {
				   float phase = (mJumpTime - halfDuration)/halfDuration;
				   phase *= phase;
				   phase = 1.f - phase;
				   currentScale = 1.f + phase*(sJumpMaxScale - 1.f);
			   }
			   else
			   {
				   float phase = (mJumpTime - halfDuration)/halfDuration;
				   phase *= phase;
				   phase = 1.f - phase;
				   currentScale = 1.f + phase*(sJumpMaxScale - 1.f);
			   }
		   }
	   }
	   
	   public void performJump()
	   {
		   if(!mIsJumping)
		   {
			   mIsJumping = true;
			   mJumpTime = 0.f;
		   }
		   
	   }
       public void computePhysics(GameSensorData gsd, GameplayEngine.GameplayRect rect) 
       {
    	   // TO DO:
    	   // obs³uga kolizji z krawêdziami planszy
    	   
           final long now = gsd.mSensorTimeStamp + (System.nanoTime() - gsd.mCpuTimeStamp);
           final float sx = gsd.mSensorX;
           final float sy = gsd.mSensorY;
    	   // Force of gravity applied to our virtual object
           final float m = 1000.0f; // mass of our virtual object
           final float gx = -sx * m;
           final float gy = -sy * m;

           /*
            * ·F = mA <=> A = ·F / m We could simplify the code by
            * completely eliminating "m" (the mass) from all the equations,
            * but it would hide the concepts from this sample code.
            */
           final float invm = 1.0f / m;
           final float ax = gx * invm;
           final float ay = gy * invm;

           if (mLastT != 0) 
           {
	           final float dT = (now - mLastT) * (1.0f / 1000000000.0f);
	           if (mLastDeltaT != 0) 
	           {
	               final float dTC = dT / mLastDeltaT;
		           /*
		            * Time-corrected Verlet integration 
		            */
		           final float dTdT = dT * dT;
		           final float x = mPosition.x + mOneMinusFriction * dTC * (mPosition.x - mLastPosX) + mAccelX * dTdT;
		           final float y = mPosition.y + mOneMinusFriction * dTC * (mPosition.y - mLastPosY) + mAccelY * dTdT;
		           
		           float rectDim = (rect.bottomRightX > rect.bottomRightY ? rect.bottomRightX : rect.bottomRightY);
		           mAccelX = ax*rectDim;
		           mAccelY = ay*rectDim;
		           
		           mLastPosX = mPosition.x;
		           mLastPosY = mPosition.y;
		           mPosition.x = x;
		           mPosition.y = y;
		           
		           if(mPosition.x < rect.topLeftX + mBoundingRadius || mPosition.x  > rect.bottomRightX - mBoundingRadius)
		           {
		        	   if(mPosition.x < rect.topLeftX + mBoundingRadius )
		        	   {
		        		   mPosition.x = rect.topLeftX + mBoundingRadius ;
		        	   }
		        	   else mPosition.x = rect.bottomRightX - mBoundingRadius;
		           }
		           
		           if(mPosition.y < rect.topLeftY + mBoundingRadius || mPosition.y > rect.bottomRightY - mBoundingRadius)
		           {
		        	   if(mPosition.y < rect.topLeftY + mBoundingRadius)
		        	   {
		        		   mPosition.y = rect.topLeftY + mBoundingRadius;
		        	   }
		        	   else mPosition.y = rect.bottomRightY - mBoundingRadius;
		           }
		          
	           }
	           mLastDeltaT = dT;
           }
           mLastT = now;
           float[] tempBuff = texture.clone();
           float xoffset = 0.0f;
           
           if(Math.abs(mAccelX) > Math.abs(mAccelY))
           {
        	   if(mAccelX > 0.1f)
        	   {
        		   xoffset = 0.2f;
        	   }
        	   else if(mAccelX < -0.1f)
        	   {
        		   xoffset = 0.4f;
        	   }
           }
           else if(Math.abs(mAccelX) < Math.abs(mAccelY))
           {
        	   if(mAccelY > 0.1f)
        	   {
        		   xoffset = 0.6f;
        	   }
        	   else if(mAccelY < -0.1f)
        	   {
        		   xoffset = 0.8f;
        	   }
           }
           tempBuff[0] += xoffset;
           tempBuff[2] += xoffset;
           tempBuff[4] += xoffset;
           tempBuff[6] += xoffset;
           textureBuffer.put(tempBuff);
		   textureBuffer.position(0);
       }
       
       public boolean isJumping()
       {
    	   return mIsJumping;
       }
       
       
}
