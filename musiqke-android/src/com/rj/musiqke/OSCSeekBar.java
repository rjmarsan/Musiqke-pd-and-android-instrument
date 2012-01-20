package com.rj.musiqke;

import java.io.IOException;
import java.util.Date;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

import com.illposed.osc.OSCMessage;
import com.illposed.osc.OSCPortIn;
import com.illposed.osc.OSCPortOut;

public class OSCSeekBar extends SeekBar implements OSCUIElement, OnSeekBarChangeListener, UIBundler {

	public OSCSeekBar(Context context, AttributeSet set) {
		super(context, set);
	}
	
	OSCPortOut out;
	String message;

	
	private String getOSCTag() {
		Object path = this.getTag();
		String stringpath = null;
		if (path != null && path instanceof String) {
			stringpath = (String)path;
		}
		return stringpath;

	}
	
	@Override
	public Bundle toBundle() {
		Bundle bundle = new Bundle();
		bundle.putInt("progress", this.getProgress());
		return bundle;
	}
	@Override
	public void fromBundle(Bundle b) {
		this.setProgress(b.getInt("progress"));
	}

	
	public OSCUIElement setOSC(String basepath, OSCPortIn in, OSCPortOut out) {
		this.out = out;
		this.message = basepath + "/" + getOSCTag();
		this.setOnSeekBarChangeListener(this);
		in.addListener(message, this);
		System.out.println(this.message);
		return this;
	}
	
	
	
	@Override
	public void acceptMessage(Date time, OSCMessage message) {
		System.out.println("Message "+message);
		try {
			float value = (Float)message.getArguments()[0];
			this.setProgress((int) (value * this.getMax()));
			this.setSecondaryProgress((int) (value * this.getMax()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void sendMessage(OSCMessage message) {
		try {
			out.send(message);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		if (fromUser && out != null) {
			float value = (float)progress / (float)this.getMax();
			sendMessage(new OSCMessage(message, new Object[] {value}));
		}		
	}



	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		
	}
	
	

}
