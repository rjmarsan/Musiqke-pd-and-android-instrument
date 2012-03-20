package com.rj.musiqke;

import java.lang.reflect.Field;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.illposed.osc.OSCPortIn;
import com.illposed.osc.OSCPortOut;

public abstract class OSCViewGroup extends ViewGroup {
	String path = "";
	
	
    public OSCViewGroup(Context context, AttributeSet attrs, int style) {
    	super(context, attrs, style);
    	onCreateView((LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE), this, null);
    }
    
    
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
    	String tag = (String)this.getTag();
    	
    	if (tag != null) {
    		int id = this.getLayoutFromString(tag);
    		if (id >= 0) return inflater.inflate(id, container, false);
    	}
        return inflater.inflate(R.layout.slidersfragment, container, false);
    }

    
    public void setupOSC(String basepath, OSCPortIn oscin, OSCPortOut oscout) {
    	View v = this;
    	basepath += "/"+path;
    	if (v instanceof ViewGroup) {
    		setupOSC((ViewGroup)v, basepath, oscin, oscout);
    	}
    }
    
    public void setupOSC(ViewGroup group, String basepath, OSCPortIn oscin, OSCPortOut oscout) {
		for (int i=0; i<group.getChildCount(); i++) {
			View child = group.getChildAt(i);
			if (child instanceof OSCUIElement) {
				OSCUIElement oscchild = (OSCUIElement) child;
				oscchild.setOSC(basepath, oscin, oscout);
			} else if ( child instanceof ViewGroup ) {
				String newbasepath = basepath;
				if (child.getTag() != null && child.getTag() instanceof String) {
					String newpath = (String)child.getTag();
					if (!newpath.trim().equalsIgnoreCase("null")) newbasepath += "/"+newpath;
				}
				setupOSC((ViewGroup)child, newbasepath, oscin, oscout);
			} 
		}
	}
    
    
    protected int getLayoutFromString(String s) {
    	Field[] fields = R.layout.class.getFields();
    	for (Field f : fields) {
    		System.out.println("Searching for "+s+" getting:"+f.getName());
    		if (f.getName().equalsIgnoreCase(s)) {
        		System.out.println("Found a match!");
    			try {
					return f.getInt(null);
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
    		}
    	}
    	return -1;

    }
    


}
