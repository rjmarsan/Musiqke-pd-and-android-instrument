package com.rj.musiqke;

import java.io.IOException;
import java.util.Date;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioButton;

import com.illposed.osc.OSCMessage;
import com.illposed.osc.OSCPortIn;
import com.illposed.osc.OSCPortOut;

public class OSCRadioButton extends RadioButton implements OnClickListener, OSCUIElement, UIBundler {
	OSCPortOut out;
	String message;
	String path = "";
		
	public OSCRadioButton(Context context, AttributeSet set) {
		super(context, set);
	}
	
	@Override
	public Bundle toBundle() {
		Bundle bundle = new Bundle();
		bundle.putBoolean("checked", this.isChecked());
		return bundle;
	}
	@Override
	public void fromBundle(Bundle b) {
		this.setChecked(b.getBoolean("checked"));
	}
	
	private String getOSCTag() {
		Object path = this.getTag();
		String stringpath = null;
		if (path != null && path instanceof String) {
			stringpath = (String)path;
		}
		return stringpath;

	}
	
	public OSCUIElement setOSC(String basepath, OSCPortIn in, OSCPortOut out) {
		this.out = out;
		this.message = basepath + "/" + getOSCTag();
		this.setOnClickListener(this);
		in.addListener(message, this);
		System.out.println(this.message);
		return this;
	}
	
	public String getPath() {
		return path;
	}
	
	
	@Override
	public void acceptMessage(Date time, OSCMessage message) {
		System.out.println("Message "+message); 
	}
	
	private void sendMessage(OSCMessage message) {
		try {
			out.send(message);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onClick(View arg0) {
		if (out != null) {
			sendMessage(new OSCMessage(message, new Object[] {1}));
		}
	}
	
	
}
