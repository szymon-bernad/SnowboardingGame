package com.sim8500.snowboardinggame;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.view.View;
//import android.view.View.OnTouchListener;
import android.view.View.OnClickListener;

public class MainMenuActivity extends Activity implements OnClickListener
{
	private Button mPlayButton;
	private Button mExitButton;
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_menu_layout);
		mPlayButton = (Button)findViewById(R.id.play_button);
		mExitButton = (Button)findViewById(R.id.exit_button);
		mPlayButton.setOnClickListener(this);
		mExitButton.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v)
	{
		if(v == mPlayButton)
		{
			Intent splashTrans = new Intent(MainMenuActivity.this, GameActivity.class);
			this.startActivity(splashTrans);
			this.finish();
		}
		else if(v == mExitButton)
		{
			this.finish();
		}
	}
}
