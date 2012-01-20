package com.rj.musiqke;

import java.net.InetAddress;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.os.Bundle;

import com.illposed.osc.OSCPortIn;
import com.illposed.osc.OSCPortOut;
import com.rj.musiqke.frags.OSCFragment;
import com.rj.musiqke.frags.TabFragment;

public class Musiqke extends Activity {
	OSCPortIn oscin;
	OSCPortOut oscout;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        setupOSC();
        setupFragments();
//        setupPresets();
//        setupSliders();
//        setupXY();
    }
    
    private void setupOSC() {
    	try {
    		oscin = new OSCPortIn(9000);
    		oscin.startListening();
       		InetAddress out = InetAddress.getByAddress(new byte[] {(byte) 255,(byte) 255,(byte) 255,(byte) 255});
    		//InetAddress out = InetAddress.getByAddress(new byte[] {(byte) 192,(byte) 17,(byte) 96,(byte) 105}); //if broadcasting doesn't work, hardcode it here.
    		oscout = new OSCPortOut(out, 8000);
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }
    
    public OSCPortIn getOSCIn() {
    	return oscin;
    }
    public OSCPortOut getOSCOut() {
    	return oscout;
    }
    
    private class MTabListener implements ActionBar.TabListener {
        private TabFragment mFragment;

        // Called to create an instance of the listener when adding a new tab
        public MTabListener(TabFragment fragment) {
            mFragment = fragment;
        }

        public void onTabSelected(Tab tab, FragmentTransaction ft) {
            ft.replace(R.id.fragtabinsert, mFragment, null);
        }

        public void onTabUnselected(Tab tab, FragmentTransaction ft) {
            ft.remove(mFragment);
        }

        public void onTabReselected(Tab tab, FragmentTransaction ft) {
            // do nothing
        }

    }

    private void setupFragments() {
    	
        // setup Action Bar for tabs
        final ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        // remove the activity title to make space for tabs
        //actionBar.setDisplayShowTitleEnabled(false);

        // instantiate fragment for the tab
        TabFragment frag1 = new TabFragment(2, 2);
        // add a new tab and set its title text and tab listener
        actionBar.addTab(actionBar.newTab().setText("Channel 2")
                .setTabListener(new MTabListener(frag1)));
        actionBar.selectTab(actionBar.getTabAt(0));
        // instantiate fragment for the tab
        TabFragment frag2 = new TabFragment(3, 2);
        // add a new tab and set its title text and tab listener
        actionBar.addTab(actionBar.newTab().setText("Channel 3")
                .setTabListener(new MTabListener(frag2)));
        
        
//        //LEGACY
//        // instantiate fragment for the tab
//        TabFragment frag1old = new TabFragment(2, 1);
//        // add a new tab and set its title text and tab listener
//        actionBar.addTab(actionBar.newTab().setText("2old")
//                .setTabListener(new MTabListener(frag1old)));
//        actionBar.selectTab(actionBar.getTabAt(0));
//        // instantiate fragment for the tab
//        TabFragment frag2old = new TabFragment(3, 1);
//        // add a new tab and set its title text and tab listener
//        actionBar.addTab(actionBar.newTab().setText("3old")
//                .setTabListener(new MTabListener(frag2old)));
//
    }
//    private void setupPresets() {
//    	((OSCUIElement)findViewById(R.id.presetbutton1)).setOSCOut(oscout, "/1/push1");
//    	((OSCUIElement)findViewById(R.id.presetbutton2)).setOSCOut(oscout, "/1/push2");
//    	((OSCUIElement)findViewById(R.id.presetbutton3)).setOSCOut(oscout, "/1/push3");
//    	((OSCUIElement)findViewById(R.id.presetbutton4)).setOSCOut(oscout, "/1/push4");
//    	((OSCUIElement)findViewById(R.id.presetbutton5)).setOSCOut(oscout, "/1/push5");
//    	((OSCUIElement)findViewById(R.id.presetbutton6)).setOSCOut(oscout, "/1/push6");
//    	((OSCUIElement)findViewById(R.id.presetbutton7)).setOSCOut(oscout, "/1/push7");
//    	((OSCUIElement)findViewById(R.id.presetbutton8)).setOSCOut(oscout, "/1/push8");
//    }
//    
//    private void setupSliders() {
//    	((OSCUIElement)findViewById(R.id.slidertoggle1)).setOSCOut(oscout, "/1/faderselect1");
//    	((OSCUIElement)findViewById(R.id.slidertoggle2)).setOSCOut(oscout, "/1/faderselect2");
//    	((OSCUIElement)findViewById(R.id.slidertoggle3)).setOSCOut(oscout, "/1/faderselect3");
//    	((OSCUIElement)findViewById(R.id.slidertoggle4)).setOSCOut(oscout, "/1/faderselect4");
//    	((OSCUIElement)findViewById(R.id.slidertoggle5)).setOSCOut(oscout, "/1/faderselect5");
//    	((OSCUIElement)findViewById(R.id.slidertoggle6)).setOSCOut(oscout, "/1/faderselect6");
//    	((OSCUIElement)findViewById(R.id.slidertoggle7)).setOSCOut(oscout, "/1/faderselect7");
//    	((OSCUIElement)findViewById(R.id.slidertoggle8)).setOSCOut(oscout, "/1/faderselect8");
//    	
//    	oscin.addListener("/1/fader1", ((OSCUIElement)findViewById(R.id.slider1)).setOSCOut(oscout, "/1/fader1"));
//    	oscin.addListener("/1/fader2", ((OSCUIElement)findViewById(R.id.slider2)).setOSCOut(oscout, "/1/fader2"));
//    	oscin.addListener("/1/fader3", ((OSCUIElement)findViewById(R.id.slider3)).setOSCOut(oscout, "/1/fader3"));
//    	oscin.addListener("/1/fader4", ((OSCUIElement)findViewById(R.id.slider4)).setOSCOut(oscout, "/1/fader4"));
//    	oscin.addListener("/1/fader5", ((OSCUIElement)findViewById(R.id.slider5)).setOSCOut(oscout, "/1/fader5"));
//    	oscin.addListener("/1/fader6", ((OSCUIElement)findViewById(R.id.slider6)).setOSCOut(oscout, "/1/fader6"));
//    	oscin.addListener("/1/fader7", ((OSCUIElement)findViewById(R.id.slider7)).setOSCOut(oscout, "/1/fader7"));
//    	oscin.addListener("/1/fader8", ((OSCUIElement)findViewById(R.id.slider8)).setOSCOut(oscout, "/1/fader8"));
//    }
//    
//    private void setupXY() {
//    	
//    }

}