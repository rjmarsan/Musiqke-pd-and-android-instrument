import processing.core.*; 
import processing.xml.*; 

import oscP5.*; 
import netP5.*; 

import java.applet.*; 
import java.awt.Dimension; 
import java.awt.Frame; 
import java.awt.event.MouseEvent; 
import java.awt.event.KeyEvent; 
import java.awt.event.FocusEvent; 
import java.awt.Image; 
import java.io.*; 
import java.net.*; 
import java.text.*; 
import java.util.*; 
import java.util.zip.*; 
import java.util.regex.*; 

public class osckeyboard extends PApplet {





OscP5 oscP5;
NetAddress myRemoteLocation;

int octavenum = 3;
int channel = 2;

public void setup() {
  size(100,100);
  frameRate(120);
  
  oscP5 = new OscP5(this,9123);
  /* myRemoteLocation is a NetAddress. a NetAddress takes 2 parameters,
   * an ip address and a port number. myRemoteLocation is used as parameter in
   * oscP5.send() when sending osc packets to another computer, device, 
   * application. usage see below. for testing purposes the listening port
   * and the port of the remote location address are the same, hence you will
   * send messages back to this sketch.
   */
  myRemoteLocation = new NetAddress("127.0.0.1",9123);
  
  down = new boolean[127];
}


public void draw() {
  //background(0);  
}

char[] keys = {'a','w','s','e','d','f','t','g','y','h','u','j','k','l'};
boolean[] down;

public void keyTyped() {
    if (key == 'c') {
    clearall();
    println("Clearing!");
    return;
  }
  
  if (key == 'x') {
    octavenum += 1;
    println("octave ++ "+octavenum);
    return;
  }
  if (key == 'z') {
    octavenum -= 1;
    println("octave -- "+octavenum);
    return;
  }
  if (key <= '9' && key >= '1') {
    channel = key - '0';
    println("channel: "+channel);
    return;
  }

  
  keyEvent(true);
}
public void keyReleased() {
  keyEvent(false);
}

public void keyEvent(boolean isdown) {
  /* in the following different ways of creating osc messages are shown by example */
  OscMessage myMessage = new OscMessage("/key");
  
  
  int keynum = getKeyNum(key);
  if (keynum >= 0) {
    myMessage.add(channel);
    myMessage.add(keynum);
    if (isdown && !down[keynum]) {
      down[keynum] = true;
      myMessage.add(127);
      /* send the message */
      oscP5.send(myMessage, myRemoteLocation); 
      println("Sending key: "+keynum);
    } else if (!isdown && down[keynum]) {
      myMessage.add(0);
      down[keynum] = false;
      /* send the message */
      oscP5.send(myMessage, myRemoteLocation); 
      println("Sending key: "+keynum);
    }
  }

}

public void clearall() {
  for (int i=0; i<127;i++) {
    OscMessage myMessage = new OscMessage("/key");
    myMessage.add(channel);
    myMessage.add(i);
    myMessage.add(0);
    oscP5.send(myMessage, myRemoteLocation); 
    down[i] = false;
  } 
}


public int getKeyNum(char key) {
  int num = -1;
  for (int i=0; i<keys.length;i++) {
    if (key == keys[i]) {
      num = i;
    }
  } 
  if (num >= 0) {
    int keynum = num + octavenum * 12;
    return keynum;
  }
  return num;
}
  static public void main(String args[]) {
    PApplet.main(new String[] { "--bgcolor=#FFFFFF", "osckeyboard" });
  }
}
