package com.sim8500.snowboardinggame;

import java.util.Random;
import java.lang.Math;
import com.sim8500.snowboardinggame.GameplayEngine.GameplayRect;
import javax.vecmath.*;

public class AlphaObject extends SpriteObject
{  
	protected Vector3f		mSpeed;
	protected Vector3f		mPerpSpeed;
	protected boolean		mInited = false;
	protected boolean		mIsBad  = false;
	protected float			mCounter = 0.f;
	protected float			mPrevSin = 0.f;
    public AlphaObject(int texId)
    {
    	super();
    	mSpeed = new Vector3f(0.f, 0.f, 0.f);
    	mPerpSpeed = new Vector3f(0.f, 0.f, 0.f);
    	textureId = texId;
    	allocBuffers();
    	mSize.set(36.f, 36.f);
    	
    }
   
   @Override
public void update(float deltaTime)
   {
	   Vector3f dspeed = (Vector3f)mSpeed.clone();
	   dspeed.scale(deltaTime);
	   mPosition.add(dspeed);
	   dspeed.set(mPerpSpeed);
	   mCounter += deltaTime*Math.PI;
	   //mCounter = mCounter % 2.f*(float)Math.PI;
	   float csin = (float)Math.sin(mCounter);
	   if(mPrevSin != 0.f)
		   dspeed.scale(csin - mPrevSin);
	   else dspeed.scale(csin);
	   
	   mPrevSin = csin;
	   mPosition.add(dspeed);
   }

   public void setSpeed(float sX, float sY)
   {
	   mSpeed.set(sX, sY, 0.f);
   }
   
   public void initObject(GameplayRect gr, Random enRand)
   {
	   if(!mInited)
	   {
		   Vector2f rndDir = new Vector2f(enRand.nextFloat()-0.5f, enRand.nextFloat()-0.5f);
			
			if(enRand.nextFloat() < 0.5f)
			{
				rndDir.x = Math.signum(rndDir.x)*gr.bottomRightX;
				
				if(enRand.nextFloat() > 0.5f)
				{
					rndDir.y = Math.signum(rndDir.y)*gr.bottomRightY;
				}
				else rndDir.y = rndDir.y*gr.bottomRightY;
			}
			else
			{
				rndDir.x = rndDir.x*gr.bottomRightX;
				rndDir.y = Math.signum(rndDir.y)*gr.bottomRightY;
			}
			
			setPos(rndDir.x, rndDir.y, mPosition.z);
			
			rndDir.normalize();
			setSpeed(-rndDir.x*0.25f*gr.bottomRightX, -rndDir.y*0.25f*gr.bottomRightX);
			
			mPerpSpeed.set(rndDir.y, -rndDir.x, 0.f);
			mPerpSpeed.scale((enRand.nextFloat()+1.0f)*mSize.x);
			mInited = true;
	   }

   }
   
   public boolean isBad()  { return mIsBad; }
   
   public void setIsBad(boolean bad)  { mIsBad = bad; }
   
}
