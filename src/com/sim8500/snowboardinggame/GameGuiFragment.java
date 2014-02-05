package com.sim8500.snowboardinggame;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class GameGuiFragment extends Fragment 
{
	protected TextView	mTxtView;
	protected View		mView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) 
    {
        // Inflate the layout for this fragment
    	mView = inflater.inflate(R.layout.game_gui_fragment_layout, container, false);
    	
    	mTxtView = (TextView)mView.findViewById(R.id.score_txt_view);
    	return mView;
    }
    
    public void  setCurrentScore(int score)
    {
    	if(mTxtView != null)
    	{
	    	mTxtView.setText(Integer.toString(score));
	    	mTxtView.invalidate();
    	}
    
    }
}
