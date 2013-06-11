package org.universAAL.samples.lighting.server.unit_impl;

public class MyLighting {
    private class Lamp {
	String loc;
	boolean isOn;

	Lamp(String loc, boolean isOn) {
	    this.loc = loc;
	    this.isOn = isOn;
	}
    }

    private Lamp[] myLampDB = new Lamp[] { new Lamp("loc1", false),
	    new Lamp("loc2", false), new Lamp("loc3", false),
	    new Lamp("loc4", false) };

    public String getLampLocation(int lampID) {
	return myLampDB[lampID].loc;
    }

    public int getState(int lampID) {
	return myLampDB[lampID].isOn ? 100 : 0;
    }

    public Integer[] getControlledLamps() {
	Integer[] ids = new Integer[myLampDB.length];
	for (int i = 0; i < myLampDB.length; i++)
	    ids[i] = i;
	return ids;
    }

    public Object[] getLampInfo(int lampID) {
	String loc = getLampLocation(lampID);
	int state = getState(lampID);
	return new Object[] { state, loc };
    }

    public void turnOff(int lampID) {
	if (myLampDB[lampID].isOn) {
	    myLampDB[lampID].isOn = false;
	}
    }

    public void turnOn(int lampID) {
	if (!myLampDB[lampID].isOn) {
	    myLampDB[lampID].isOn = true;
	}
    }
}
