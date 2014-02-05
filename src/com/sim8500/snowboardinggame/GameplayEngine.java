package com.sim8500.snowboardinggame;

import java.util.Iterator;
import java.util.Random;
import java.util.Vector;
import android.view.MotionEvent;
import com.sim8500.snowboardinggame.GameFragment.GameSensorData;
import android.os.Handler;
import android.os.Message;


public class GameplayEngine 
{
	// TO DO:
	// wsadziæ do tej klasy wszystko, co ma wiêcej wspólnego z logik¹ gry
	// ni¿ z warstw¹ okna/cyklem ¿ycia aplikacji (GameActivity) i renderowaniem obiektów sceny (MyGLRenderer)

	public class GameplayRect
	{
		protected float  topLeftX 			= 0.f;
		protected float  topLeftY 			= 0.f;
		protected float	 bottomRightX 		= 0.f;
		protected float  bottomRightY 		= 0.f;
	}
	
	protected GameplayRect   mGameplayRect = new GameplayRect();
	protected GameBackGround mGameBG = new GameBackGround();
	protected ForeGroundObject mFGObject = new ForeGroundObject();
	protected Vector<AlphaObject> mAlphaObjects = new Vector<AlphaObject>();
	protected Handler mHandler;
	protected static final int  mMaxAlphaCount = 12;
	protected static final int  mMaxAlphaAdded = 4;
	protected static final float mMinAlphaAddBreak = 3.f;
	
	protected Random mEngineRand = new Random();
	protected GameFragment.GameSensorData mGSD;
    protected boolean mObjectsChanged = false;
    protected float  mAddBreakTimer = 0.f;
    protected GameGuiFragment mGuiFragment;
    protected int  mCurrentScore = 0;
	
	// wywo³ywane z GameActivity przy zmianie wskazañ sensora
	public void updateSensorData(GameSensorData gsd)
	{		
		mGSD = gsd;
	}
	
	public void setGuiFragment(GameGuiFragment frag)
	{
		mGuiFragment = frag;
	}
	
	public void updateThick(float deltaTime)
	{
		mFGObject.update(deltaTime);
		Iterator<AlphaObject> alphaIt = mAlphaObjects.iterator();
		while(alphaIt.hasNext())
		{
			AlphaObject obj = alphaIt.next();
			
			obj.update(deltaTime);
			if(isOutsideGameplayRect(obj.getPos().x, obj.getPos().y))
			{
				alphaIt.remove();
				mObjectsChanged = true;
			}
			else if(!mFGObject.isJumping())
			{
				float dx = obj.getPos().x - mFGObject.getPos().x;
				float dy = obj.getPos().y - mFGObject.getPos().y;
				if(Math.sqrt(dx*dx + dy*dy) < 0.33f*(obj.getWidth()+mFGObject.getWidth()))
				{
					alphaIt.remove();
					mCurrentScore += obj.isBad() ? -10 : 10;
					mObjectsChanged = true;
				}
			}
		}
		if(mHandler != null && mObjectsChanged)
		{
			Message msg = new Message();
			msg.arg1 = mCurrentScore;
			mHandler.sendMessage(msg);
		}
		
		if(mAlphaObjects.size() < mMaxAlphaCount)
		{
			mAddBreakTimer -= deltaTime;
			
			if(mAddBreakTimer <= 0.f)
			{
				int currentCnt = 0;
				while(mAlphaObjects.size() < mMaxAlphaCount && currentCnt < mMaxAlphaAdded)
				{
					boolean isBad = mEngineRand.nextFloat() < 0.25f;		
					AlphaObject obj = new AlphaObject(isBad ? R.drawable.circleb : R.drawable.circle);
					obj.setIsBad(isBad);
					
					mAlphaObjects.addElement(obj);
					obj.initObject(mGameplayRect, mEngineRand);
					mObjectsChanged = true;
					++currentCnt;
				}
				mAddBreakTimer = mMinAlphaAddBreak;
			}
		}
		
		computePhysicsTick();
	}
	
	public void computePhysicsTick()
	{
		if(mGSD != null)
			mFGObject.computePhysics(mGSD, mGameplayRect);
	}
	
	public void processMotionEvent(MotionEvent event)
	{
		if(event.getAction() == MotionEvent.ACTION_DOWN)
		{
			mFGObject.performJump();
		}
	}
	
	public Vector<SpriteObject>  getObjects()
	{
		Vector<SpriteObject>  objects = new Vector<SpriteObject>();
		objects.addElement(mFGObject);
		Iterator<AlphaObject> alphaIt = mAlphaObjects.iterator();
		
		while(alphaIt.hasNext())
		{
			objects.addElement(alphaIt.next());
		}
		return objects;
	}
	
	public void setGameplayRect(float width, float height)
	{
		mGameplayRect.topLeftX = -0.5f*width;
		mGameplayRect.topLeftY = -0.5f*height;
		mGameplayRect.bottomRightX = 0.5f*width;
		mGameplayRect.bottomRightY = 0.5f*height;
		if(mAlphaObjects.size() != 0)
		{
			Iterator<AlphaObject> alphaIt = mAlphaObjects.iterator();
			
			while(alphaIt.hasNext())
			{
				alphaIt.next().initObject(mGameplayRect, mEngineRand);
			}
		}
	}
	
	public void generateAlphaObjects()
	{
		int currentCnt = 0;
		while(mAlphaObjects.size() < mMaxAlphaCount && currentCnt < mMaxAlphaAdded)
		{
			boolean isBad = mEngineRand.nextFloat() < 0.25f;
			
			AlphaObject obj = new AlphaObject(isBad ? R.drawable.circleb : R.drawable.circle);
			obj.setIsBad(isBad);
			mAlphaObjects.addElement(obj);
			mObjectsChanged = true;
			++currentCnt;
		}
		
	}
	
	public boolean areObjectsChanged()
	{
		boolean retval = mObjectsChanged;
		mObjectsChanged = false;
		return retval;
	}
	
	public boolean isOutsideGameplayRect(float pX, float pY)
	{
		if(pX < 1.2f*mGameplayRect.topLeftX || pX > 1.2f*mGameplayRect.bottomRightX)
			return true;
		if(pY < 1.2f*mGameplayRect.topLeftY || pY > 1.2f*mGameplayRect.bottomRightY)
			return true;
		
		return false;
	}
	
	public void  setHandler(Handler hndlr)
	{
		mHandler = hndlr;
	}
	
}
