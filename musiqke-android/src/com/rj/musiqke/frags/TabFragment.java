package com.rj.musiqke.frags;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rj.musiqke.Musiqke;
import com.rj.musiqke.UIBundler;

public class TabFragment extends OSCFragment {
	int tabnum;
	Bundle saved;
	
	public TabFragment(int path) {
		this(path, path);
	}
	
	public TabFragment(int path, int number) {
		this.tabnum = number;
		this.path = ""+path;
	}

	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
    	View v = inflater.inflate(getLayoutFromString("tab"+tabnum), container, false);
		Log.d("TabFragment", "Inflating");
    	if (savedInstanceState != null) {
    		Log.d("TabFragment", "Restoring from incoming");
    		recursivelyRestore(v, savedInstanceState.getBundle("us"));
    	} else if (saved != null) {
    		Log.d("TabFragment", "Restoring from selfsaved");
    		recursivelyRestore(v, saved.getBundle("us"));    	
    		saved = null;
    	}
    	return v;
    }
    
    @Override
    public void onStart() {
    	super.onStart();
    	Musiqke parent = (Musiqke)this.getActivity();
    	this.setupOSC("", parent.getOSCIn(), parent.getOSCOut());
    }
    
    
    
    @Override
    public void onDestroyView() {
		Log.d("TabFragment", "Storing by death");
    	Bundle b = new Bundle();
    	b.putBundle("us", recursivelySave(getView()));
    	saved = b;
    	super.onDestroyView();
    }
    
    @Override
    public void onSaveInstanceState(Bundle outState) {
    	super.onSaveInstanceState(outState);
		Log.d("TabFragment", "Storing");
    	outState.putBundle("us", recursivelySave(getView()));
    }
    
    public Bundle recursivelySave(View v) {
		if (v instanceof ViewGroup) {
			ViewGroup group = (ViewGroup)v;
			Bundle children = new Bundle();
			for (int i=0; i<group.getChildCount(); i++) {
				children.putBundle(
						i+"", 
						recursivelySave(group.getChildAt(i)));
			}
			Log.d("TABFRAG", children+"");
			return children;
		}
		if (v instanceof UIBundler) {
			return ((UIBundler)v).toBundle();
		}
		return new Bundle();
    }

    
    public void recursivelyRestore(View v, Bundle b) {
		if (v instanceof ViewGroup && b != null) {
			ViewGroup group = (ViewGroup)v;
			for (int i=0; i<group.getChildCount(); i++) {
				Bundle childb = b.getBundle(i+"");
				Log.d("TABFRAG", "child at "+i+" with tag:"+group.getChildAt(i).getTag()+""+" has bundle: "+childb);
				recursivelyRestore(group.getChildAt(i), childb);
			}
		}
		if (v instanceof UIBundler) {
			((UIBundler)v).fromBundle(b);
		}
    }



}
