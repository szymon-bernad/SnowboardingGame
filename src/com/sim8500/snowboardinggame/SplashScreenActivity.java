package com.sim8500.snowboardinggame;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Intent;
import java.lang.Runnable;
import java.lang.Thread;

public class SplashScreenActivity extends Activity 
{

	public class SplashDelayRunnable implements Runnable
	{
		@Override
		public void run()
		{
			Intent splashTrans = new Intent(SplashScreenActivity.this, MainMenuActivity.class);
			SplashScreenActivity.this.startActivity(splashTrans);
			SplashScreenActivity.this.overridePendingTransition(R.anim.card_flip_out, R.anim.card_flip_anim);
			SplashScreenActivity.this.finish();
			
			
			
		}
		
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash_screen);
		new Handler().postDelayed(new Thread(new SplashDelayRunnable()), 2500);
		
	}

}
