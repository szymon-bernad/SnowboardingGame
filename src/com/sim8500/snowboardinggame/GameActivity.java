package com.sim8500.snowboardinggame;

import android.os.Bundle;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Handler;
import android.os.Message;

import com.sim8500.snowboardinggame.GameFragment;
import com.sim8500.snowboardinggame.GameGuiFragment;

public class GameActivity extends Activity implements Handler.Callback
{
	
	protected GameFragment 			mGameFrag;
	protected GameGuiFragment 		mGameGuiFrag;
	protected Handler				mHandler;
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);

		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		
		mHandler = new Handler(this);
		mGameFrag = new GameFragment();
		mGameGuiFrag = new GameGuiFragment();
		fragmentTransaction.add(R.id.game_canvas, mGameFrag);
		fragmentTransaction.commit();
		
		fragmentTransaction = fragmentManager.beginTransaction();
		fragmentTransaction.add(R.id.game_gui, mGameGuiFrag);
		fragmentTransaction.commit();
		setContentView(R.layout.activity_game);
        
	}

	 @Override
	 protected void onResume() 
	 {
	     super.onResume();
	     mGameFrag.onResume();
	     mGameGuiFrag.onResume();
	 }

	 @Override
	 protected void onPause() 
	 {
		 super.onPause();
		 mGameFrag.onPause();
		 mGameGuiFrag.onPause();
	 }

	@Override
	public boolean handleMessage(Message msg) 
	{
		mGameGuiFrag.setCurrentScore(msg.arg1);
		return false;
	}
	
	public Handler  getHandler()  {  return mHandler; }
}
