package com.rj.musiqke;

import java.io.IOException;
import java.util.Date;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioButton;

import com.illposed.osc.OSCListener;
import com.illposed.osc.OSCMessage;
import com.illposed.osc.OSCPortIn;
import com.illposed.osc.OSCPortOut;

public interface OSCUIElement extends OSCListener {
	public abstract OSCUIElement setOSC(String basepath, OSCPortIn in, OSCPortOut out);
	
}
