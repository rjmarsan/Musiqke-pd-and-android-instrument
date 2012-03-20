package com.rj.musiqke.frags;

import java.lang.reflect.Field;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.illposed.osc.OSCPortIn;
import com.illposed.osc.OSCPortOut;
import com.rj.musiqke.OSCUIElement;
import com.rj.musiqke.R;

public abstract class OSCFragment extends Fragment {
	String path = "";
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
    	String tag = this.getTag();
    	if (tag != null) {
    		int id = this.getLayoutFromString(tag);
    		if (id >= 0) return inflater.inflate(id, container, false);
    	}
        return inflater.inflate(R.layout.slidersfragment, container, false);
    }

	
	@Override
	public void onStart() {
		super.onStart();
	}
    
    public void setupOSC(String basepath, OSCPortIn oscin, OSCPortOut oscout) {
    	View v = this.getView();
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
