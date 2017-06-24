/*
	Copyright 2008-2014 ITACA-TSB, http://www.tsb.upv.es
	Instituto Tecnologico de Aplicaciones de Comunicacion
	Avanzadas - Grupo Tecnologias para la Salud y el
	Bienestar (TSB)

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
 */package org.universAAL.samples.ctxtbus;

import java.util.Random;

import org.universAAL.middleware.container.ModuleContext;
import org.universAAL.middleware.context.ContextEvent;
import org.universAAL.middleware.context.ContextEventPattern;
import org.universAAL.middleware.context.ContextPublisher;
import org.universAAL.middleware.context.owl.ContextProvider;
import org.universAAL.middleware.context.owl.ContextProviderType;
import org.universAAL.ontology.device.BlindController;
import org.universAAL.ontology.device.DimmerController;
import org.universAAL.ontology.device.LightController;
import org.universAAL.ontology.device.PanicButtonSensor;
import org.universAAL.ontology.device.StatusValue;
import org.universAAL.ontology.device.TemperatureSensor;
import org.universAAL.ontology.device.WindowController;
import org.universAAL.ontology.furniture.Furniture;
import org.universAAL.ontology.furniture.FurnitureType;
import org.universAAL.ontology.location.Location;
import org.universAAL.ontology.location.Place;
import org.universAAL.ontology.location.address.PhysicalAddress;
import org.universAAL.ontology.phThing.PhysicalThing;
import org.universAAL.ontology.profile.Profilable;
import org.universAAL.ontology.profile.User;
import org.universAAL.ontology.profile.UserProfile;

public class CPublisher extends ContextPublisher {
	protected static final int samples = 7;
	private ContextEvent[] sampleEvents;
	protected static final String URIROOT = "http://ontology.itaca.upv.es/Test.owl#";
	private Random rand = new Random();

	protected CPublisher(ModuleContext context, ContextProvider providerInfo) {
		super(context, providerInfo);
		// TODO Auto-generated constructor stub
	}

	protected CPublisher(ModuleContext context) {
		super(context, getProviderInfo());
		initSamples();
	}

	private static ContextProvider getProviderInfo() {
		ContextProvider cpinfo = new ContextProvider(URIROOT + "TestMassContextProvider");
		cpinfo.setType(ContextProviderType.gauge);
		cpinfo.setProvidedEvents(new ContextEventPattern[] { new ContextEventPattern() });
		return cpinfo;
	}

	public void communicationChannelBroken() {
		// TODO Auto-generated method stub

	}

	public long sendBurst(int size) {
		Random r = new Random();
		long t0 = System.currentTimeMillis();
		for (int i = 0; i < size; i++) {
			this.publish(sampleEvents[r.nextInt(samples)]);
		}
		long t1 = System.currentTimeMillis();
		return t1 - t0;
	}

	// to send burst of event with unique URIs (for CHe)
	// may introduce more delay
	public long sendUniqueBurst(int size) {
		Random r = new Random();
		long t0 = System.currentTimeMillis();
		for (int i = 0; i < size; i++) {
			ContextEvent ev = getSample(r.nextInt(9));
			ev.setConfidence(rand.nextInt(101));
			this.publish(ev);
		}
		long t1 = System.currentTimeMillis();
		return t1 - t0;
	}

	private void initSamples() {
		sampleEvents = new ContextEvent[samples];
		// 1 User is awake
		User u1 = new User(URIROOT + "user1");
		u1.setProperty(Profilable.PROP_HAS_PROFILE, new UserProfile(URIROOT + "user1Profile"));
		sampleEvents[0] = new ContextEvent(u1, Profilable.PROP_HAS_PROFILE);
		// 2 Blind is open
		BlindController b2 = new BlindController(URIROOT + "blind4");
		b2.setProperty(BlindController.PROP_HAS_VALUE, new Integer(100));
		sampleEvents[1] = new ContextEvent(b2, BlindController.PROP_HAS_VALUE);
		// 3 chair is in place
		Furniture f3 = new Furniture(URIROOT + "furniture5");
		f3.setFurnitureType(FurnitureType.Chair);
		f3.setLocation(new Location(URIROOT + "location5"));
		sampleEvents[2] = new ContextEvent(f3, Furniture.PROP_PHYSICAL_LOCATION);
		// 4 light is on
		LightController ls4 = new LightController(URIROOT + "light6");
		ls4.setValue(100);
		sampleEvents[3] = new ContextEvent(ls4, LightController.PROP_HAS_VALUE);
		// 5 socket at 50%
		DimmerController ss5 = new DimmerController(URIROOT + "socket7");
		ss5.setValue(50);
		sampleEvents[4] = new ContextEvent(ss5, DimmerController.PROP_HAS_VALUE);
		// 6 temperature measured
		TemperatureSensor ts6 = new TemperatureSensor(URIROOT + "tempsensor8");
		ts6.setValue(27.5f);
		sampleEvents[5] = new ContextEvent(ts6, TemperatureSensor.PROP_HAS_VALUE);
		// 7 window closed
		WindowController w7 = new WindowController(URIROOT + "window9");
		w7.setValue(StatusValue.Activated);
		sampleEvents[6] = new ContextEvent(w7, WindowController.PROP_HAS_VALUE);
	}

	// I cant have a preset collection of events because timestamp and URI are
	// set at construction
	public static ContextEvent getSample(int sample) {
		switch (sample) {
		case 0:
			// 1 User is awake
			User u1 = new User(URIROOT + "user1");
			u1.setProperty(Profilable.PROP_HAS_PROFILE, new UserProfile(URIROOT + "user1Profile"));
			return new ContextEvent(u1, Profilable.PROP_HAS_PROFILE);
		case 1:
			// 2 Blind is open
			BlindController b2 = new BlindController(URIROOT + "blind4");
			b2.setProperty(BlindController.PROP_HAS_VALUE, new Integer(100));
			return new ContextEvent(b2, BlindController.PROP_HAS_VALUE);
		case 2:
			// 3 chair is in place
			Furniture f3 = new Furniture(URIROOT + "furniture5");
			f3.setFurnitureType(FurnitureType.Chair);
			f3.setLocation(new Location(URIROOT + "location" + new Integer(6)));
			return new ContextEvent(f3, Furniture.PROP_PHYSICAL_LOCATION);
		case 3:
			// 4 light is on
			LightController ls4 = new LightController(URIROOT + "light6");
			ls4.setValue(new Integer(100));
			return new ContextEvent(ls4, LightController.PROP_HAS_VALUE);
		case 4:
			// 7 socket at 50%
			DimmerController ss5 = new DimmerController(URIROOT + "socket7");
			ss5.setValue(new Integer(100));
			return new ContextEvent(ss5, DimmerController.PROP_HAS_VALUE);
		case 5:
			// 6 temperature measured
			TemperatureSensor ts6 = new TemperatureSensor(URIROOT + "tempsensor8");
			ts6.setValue(30);
			return new ContextEvent(ts6, TemperatureSensor.PROP_HAS_VALUE);
		case 6:
			// 7 window closed
			WindowController w7 = new WindowController(URIROOT + "window9");
			w7.setValue(StatusValue.Activated);
			return new ContextEvent(w7, WindowController.PROP_HAS_VALUE);
		default:
			// 10 situation
			PanicButtonSensor p10 = new PanicButtonSensor(URIROOT + "panic10");
			p10.setProperty(PhysicalThing.PROP_CARRIED_BY, new User(URIROOT + "user" + new Integer(5)));
			p10.setProperty(PhysicalThing.PROP_IS_PORTABLE, new Boolean(true));
			p10.setLocation(new Location(URIROOT + "location" + new Integer(10)));
			return new ContextEvent(p10, PhysicalThing.PROP_PHYSICAL_LOCATION);
		}
	}

	public void sendSituation() {
		// 10 situation
		PanicButtonSensor p10 = new PanicButtonSensor(URIROOT + "panic10");
		p10.setProperty(PhysicalThing.PROP_CARRIED_BY, new User(URIROOT + "user1"));
		p10.setProperty(PhysicalThing.PROP_IS_PORTABLE, new Boolean(true));
		p10.setLocation(new Location(URIROOT + "location10"));
		this.publish(new ContextEvent(p10, PhysicalThing.PROP_PHYSICAL_LOCATION));
	}

	public void sendEventWithData() {
		// Multiple address
		PhysicalAddress address = new PhysicalAddress(URIROOT + "address1");
		int addnum = rand.nextInt(10);
		address.addAdditionalID("additionalAddress" + addnum);
		address.addAdditionalID("additionalAddress" + (addnum + 1));
		address.setBuildingID("8G");
		Place loc = new Place(URIROOT + "location4Address");
		loc.setAddress(address);
		ContextEvent ev = new ContextEvent(loc, Place.PROP_HAS_ADDRESS);
		this.publish(ev);
	}

}
