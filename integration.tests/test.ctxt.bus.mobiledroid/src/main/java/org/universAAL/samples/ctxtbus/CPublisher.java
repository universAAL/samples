package org.universAAL.samples.ctxtbus;

import java.util.Random;

import org.universAAL.middleware.container.ModuleContext;
import org.universAAL.middleware.context.ContextEvent;
import org.universAAL.middleware.context.ContextPublisher;
import org.universAAL.middleware.context.owl.ContextProvider;
import org.universAAL.middleware.context.owl.ContextProviderType;
import org.universAAL.ontology.furniture.Furniture;
import org.universAAL.ontology.furniture.FurnitureType;
import org.universAAL.ontology.lighting.LightSource;
import org.universAAL.ontology.location.Location;
import org.universAAL.ontology.phThing.PhysicalThing;
import org.universAAL.ontology.powersocket.Powersocket;
import org.universAAL.ontology.profile.Profilable;
import org.universAAL.ontology.profile.User;
import org.universAAL.ontology.profile.UserProfile;
import org.universAAL.ontology.risk.PanicButton;
import org.universAAL.ontology.weather.TempSensor;
import org.universAAL.ontology.window.BlindActuator;
import org.universAAL.ontology.window.WindowActuator;

public class CPublisher extends ContextPublisher {
    protected static final int samples=7;
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
	ContextProvider cpinfo = new ContextProvider(URIROOT
		+ "TestMassContextProvider");
	cpinfo.setType(ContextProviderType.gauge);
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
	    this.publish(getSample(r.nextInt(9)));
	}
	long t1 = System.currentTimeMillis();
	return t1 - t0;
    }

    private void initSamples() {
	sampleEvents = new ContextEvent[samples];
	// 1 User is awake
	User u1 = new User(URIROOT + "user1");
	u1.setProperty(Profilable.PROP_HAS_PROFILE, new UserProfile(URIROOT
		+ "user1Profile"));
	sampleEvents[0] = new ContextEvent(u1, Profilable.PROP_HAS_PROFILE);
	// 2 Blind is open
	BlindActuator b2 = new BlindActuator(URIROOT + "blind4");
	b2.setProperty(BlindActuator.PROP_DIMMABLE_STATUS, new Integer(100));
	sampleEvents[1] = new ContextEvent(b2,
		BlindActuator.PROP_DIMMABLE_STATUS);
	// 3 chair is in place
	Furniture f3 = new Furniture(URIROOT + "furniture5");
	f3.setFurnitureType(FurnitureType.Chair);
	f3.setLocation(new Location(URIROOT + "location5"));
	sampleEvents[2] = new ContextEvent(f3, Furniture.PROP_PHYSICAL_LOCATION);
	// 4 light is on
	LightSource ls4 = new LightSource(URIROOT + "light6");
	ls4.setBrightness(100);
	sampleEvents[3] = new ContextEvent(ls4,
		LightSource.PROP_SOURCE_BRIGHTNESS);
	// 5 socket at 50%
	Powersocket ss5 = new Powersocket(URIROOT + "socket7");
	ss5.setValue(50);
	sampleEvents[4] = new ContextEvent(ss5, Powersocket.PROP_SOCKET_VALUE);
	// 6 temperature measured
	TempSensor ts6 = new TempSensor(URIROOT + "tempsensor8");
	ts6.setMeasuredValue(27.5f);
	sampleEvents[5] = new ContextEvent(ts6, TempSensor.PROP_MEASURED_VALUE);
	// 7 window closed
	WindowActuator w7 = new WindowActuator(URIROOT + "window9");
	w7.setProperty(WindowActuator.PROP_WINDOW_STATUS, new Integer(0));
	sampleEvents[6] = new ContextEvent(w7,
		WindowActuator.PROP_WINDOW_STATUS);
    }

    // I cant have a preset collection of events because timestamp and URI are
    // set at construction
    private ContextEvent getSample(int sample) {
	switch (sample) {
	case 0:
	    // 1 User is awake
	    User u1 = new User(URIROOT + "user1");
	    u1.setProperty(Profilable.PROP_HAS_PROFILE, new UserProfile(URIROOT
		    + "user1Profile"));
	    return new ContextEvent(u1, Profilable.PROP_HAS_PROFILE);
	case 1:
	    // 2 Blind is open
	    BlindActuator b2 = new BlindActuator(URIROOT + "blind4");
	    b2.setProperty(BlindActuator.PROP_DIMMABLE_STATUS, new Integer(100));
	    return new ContextEvent(b2, BlindActuator.PROP_DIMMABLE_STATUS);
	case 2:
	    // 3 chair is in place
	    Furniture f3 = new Furniture(URIROOT + "furniture5");
	    f3.setFurnitureType(FurnitureType.Chair);
	    f3.setLocation(new Location(URIROOT + "location" + rand.nextInt(6)));
	    return new ContextEvent(f3, Furniture.PROP_PHYSICAL_LOCATION);
	case 3:
	    // 4 light is on
	    LightSource ls4 = new LightSource(URIROOT + "light6");
	    ls4.setBrightness(rand.nextInt(101));
	    return new ContextEvent(ls4, LightSource.PROP_SOURCE_BRIGHTNESS);
	case 4:
	    // 7 socket at 50%
	    Powersocket ss5 = new Powersocket(URIROOT + "socket7");
	    ss5.setValue(rand.nextInt(101));
	    return new ContextEvent(ss5, Powersocket.PROP_SOCKET_VALUE);
	case 5:
	    // 6 temperature measured
	    TempSensor ts6 = new TempSensor(URIROOT + "tempsensor8");
	    ts6.setMeasuredValue(30 * rand.nextFloat());
	    return new ContextEvent(ts6, TempSensor.PROP_MEASURED_VALUE);
	case 6:
	    // 7 window closed
	    WindowActuator w7 = new WindowActuator(URIROOT + "window9");
	    w7.setProperty(WindowActuator.PROP_WINDOW_STATUS,
		    new Integer(rand.nextInt(101)));
	    return new ContextEvent(w7, WindowActuator.PROP_WINDOW_STATUS);
	default:
	    // 10 situation
	    PanicButton p10 = new PanicButton(URIROOT + "panic10");
	    p10.setProperty(PhysicalThing.PROP_CARRIED_BY, new User(URIROOT
		    + "user" + rand.nextInt(5)));
	    p10.setProperty(PhysicalThing.PROP_IS_PORTABLE,
		    new Boolean(rand.nextBoolean()));
	    p10.setLocation(new Location(URIROOT + "location"
		    + rand.nextInt(11)));
	    return new ContextEvent(p10, PhysicalThing.PROP_PHYSICAL_LOCATION);
	}
    }

}
