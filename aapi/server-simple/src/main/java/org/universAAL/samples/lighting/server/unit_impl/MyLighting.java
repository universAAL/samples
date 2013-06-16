/*
Copyright 2011-2014 AGH-UST, http://www.agh.edu.pl
Faculty of Computer Science, Electronics and Telecommunications
Department of Computer Science 

See the NOTICE file distributed with this work for additional
information regarding copyright ownership

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
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
