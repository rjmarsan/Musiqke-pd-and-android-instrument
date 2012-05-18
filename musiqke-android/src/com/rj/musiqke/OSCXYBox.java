package com.rj.musiqke;

import java.io.IOException;
import java.util.Date;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;

import com.illposed.osc.OSCMessage;
import com.illposed.osc.OSCPortIn;
import com.illposed.osc.OSCPortOut;

public class OSCXYBox extends SurfaceView implements OSCUIElement, OnTouchListener, SurfaceHolder.Callback, UIBundler {
	
	public OSCXYBox(Context context, AttributeSet set) {
		super(context, set);
		if (set.getAttributeValue(null, "subtag") != null) {
			subtag = set.getAttributeValue(null, "subtag");
		} else {
			subtag = "slider";
		}
		this.getHolder().addCallback(this);
	}
	
	@Override
	public Bundle toBundle() {
		Bundle bundle = new Bundle();
		bundle.putFloat("x", x);
		bundle.putFloat("y", y);
		bundle.putFloat("origx", origx);
		bundle.putFloat("origx", origy);
		return bundle;
	}
	@Override
	public void fromBundle(Bundle bundle) {
		x = bundle.getFloat("x");
		y = bundle.getFloat("y");
		origx = bundle.getFloat("origx");
		origy = bundle.getFloat("origx");
	}

	
	OSCPortOut out;
	String message;
	String subtag;
	AnimationThread animthread;
	
	float x = -100000;
	float y = -100000;
	float origx = -100000;
	float origy = -100000; 
	int width;
	int height;
	long lastsend = 0L;
	public final static long DONT_FLOOD_WAIT = 30L;
	
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
		this.setOnTouchListener(this);
		in.addListener(makePath(1), this);
		in.addListener(makePath(2), this);
		System.out.println(this.message);
		return this;
	}
	
	
	
	@Override
	public void acceptMessage(Date time, OSCMessage message) {
		System.out.println("Message "+message);
		try {
			float value = (Float)message.getArguments()[0];
			if (message.getAddress().endsWith("1")) {
				origx = value * this.getWidth();
				x = origx;
			} else if (message.getAddress().endsWith("2")) {
				origy = value * this.getHeight();
				y = origy;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void sendMessage(OSCMessage message) {
			out.sendAsync(message);
	}
	
	private String makePath(int num) {
		return message+"/" + subtag + num;
	}


	private class AnimationThread extends Thread {
		SurfaceHolder holder;
		boolean shouldrun = true;
		
		Paint current;
		Paint past;
		Paint notouchyet;
		Paint outline;
		
		public AnimationThread(SurfaceHolder h) {
			holder = h;
			current = new Paint();
			current.setColor(Color.rgb(200, 0, 0));
			current.setAntiAlias(true);
			past = new Paint();
			past.setColor(Color.rgb(100, 20, 20));
			past.setAntiAlias(true);
			outline = new Paint();
			outline.setColor(Color.rgb(30,30,30));
			outline.setStyle(Style.STROKE);
			outline.setStrokeWidth(3);
			notouchyet = new Paint();
			notouchyet.setColor(Color.rgb(100, 100, 100));
			notouchyet.setTypeface(Typeface.SANS_SERIF);
			notouchyet.setTextSize(30);
			notouchyet.setTextAlign(Align.CENTER);
			notouchyet.setAntiAlias(true);
			
		}
		
		public void stopDoingStuff() {
			shouldrun = false;
		}
		public void run() {
			while (shouldrun) {
				Canvas c = holder.lockCanvas();
				c.drawColor(Color.rgb(0, 0, 0));
				
				if (x > -10000 && y > -10000) {
					//c.drawText("touch here!",width/2 , height/2, notouchyet);
					c.drawCircle(origx, origy, 30, past);
					c.drawLine(0, origy, width, origy, past);
					c.drawLine(origx, 0, origx, height, past);
					c.drawCircle(x, y, 30, current);
					c.drawLine(0, y, width, y, current);
					c.drawLine(x, 0, x, height, current);
				} else {	
					c.drawText("touch here!",width/2 , height/2, notouchyet);
				}

				
				c.drawRect(1, 1, width-2, height-2, outline);

				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				holder.unlockCanvasAndPost(c);
			}
		}
	}

	@Override
	public boolean onTouch(View arg0, MotionEvent arg1) {
		if (SystemClock.uptimeMillis() - lastsend > DONT_FLOOD_WAIT) {
			x = arg1.getX();
			y = arg1.getY();
			float oscx = (float)arg1.getX() / (float)this.getWidth();
			float oscy = (float)arg1.getY() / (float)this.getHeight();
			sendMessage(new OSCMessage(makePath(1), new Object[] {oscx}));
			sendMessage(new OSCMessage(makePath(2), new Object[] {oscy}));
			System.out.println("Sending to: "+makePath(1)+ " value:"+oscx);
			System.out.println("Sending to: "+makePath(2)+ " value:"+oscy);
			lastsend = SystemClock.uptimeMillis();
		}
		return true;
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		this.width = width;
		this.height = height;
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		animthread = new AnimationThread(holder);
		animthread.start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		animthread.stopDoingStuff();
		animthread = null;
	}
	
	

}
