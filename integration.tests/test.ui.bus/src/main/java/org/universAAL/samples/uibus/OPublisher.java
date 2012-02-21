package org.universAAL.samples.uibus;

import java.util.Locale;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.universAAL.middleware.container.ModuleContext;
import org.universAAL.middleware.io.owl.PrivacyLevel;
import org.universAAL.middleware.io.rdf.*;
import org.universAAL.middleware.output.OutputEvent;
import org.universAAL.middleware.output.OutputPublisher;
import org.universAAL.middleware.owl.OrderingRestriction;
import org.universAAL.middleware.rdf.PropertyPath;
import org.universAAL.middleware.rdf.Resource;
import org.universAAL.ontology.profile.User;

public class OPublisher extends OutputPublisher {

    private final static Logger log = LoggerFactory.getLogger(OPublisher.class);
    private static Form[] presetForms = new Form[] { getPresetForm(1),
	    getPresetForm(2), getPresetForm(3), getPresetForm(4),
	    getPresetForm(5), getPresetForm(6), getPresetForm(7),
	    getPresetForm(8), getPresetForm(9), getPresetForm(10) };

    protected OPublisher(ModuleContext context) {
	super(context);
	// TODO Auto-generated constructor stub
    }

    public void communicationChannelBroken() {
	// TODO Auto-generated method stub

    }

    // SHOWS_____________________________________________
    public void showRandomDialog(User user) {
	log.debug("Show dialog from OPub");
	Random rand = new Random();
	Form f = presetForms[rand.nextInt(10)];
	OutputEvent oe = new OutputEvent(user, f, null, Locale.ENGLISH,
		PrivacyLevel.insensible);
	Activator.uinput.subscribe(f.getDialogID());
	log.debug("Publish dialog from OPub");
	publish(oe);
    }

    public long showRandomBurst(User user, int size) {
	Random r = new Random();
	long t0 = System.currentTimeMillis();
	for (int i = 0; i < size; i++) {
	    Form f = presetForms[r.nextInt(10)];
	    OutputEvent oe = new OutputEvent(user, f, null, Locale.ENGLISH,
		    PrivacyLevel.insensible);
	    Activator.uinput.subscribe(f.getDialogID());
	    publish(oe);
	}
	long t1 = System.currentTimeMillis();
	return t1 - t0;
    }

    public long showDynamicDialog(User user, int size) {
	long t0 = System.currentTimeMillis();
	Form f = getDynamicForm(size);
	OutputEvent oe = new OutputEvent(user, f, null, Locale.ENGLISH,
		PrivacyLevel.insensible);
	Activator.uinput.subscribe(f.getDialogID());
	publish(oe);
	long t1 = System.currentTimeMillis();
	return t1 - t0;
    }

    public void showAllRespDialog(User user, String[] formsNames,
	    String[] formsResults) {
	log.debug("Show result from OPub");
	Form f = getAllRespForm(formsNames, formsResults);
	OutputEvent oe = new OutputEvent(user, f, null, Locale.ENGLISH,
		PrivacyLevel.insensible);
	Activator.uinput.subscribe(f.getDialogID());
	log.debug("Publish dialog from OPub");
	publish(oe);
    }

    // FORMS_______________________________________________________
    public Form getAllRespForm(String[] formsNames, String[] formsResults) {
	Form f = Form.newDialog("Input response", (String) null);
	Group controls = f.getIOControls();
	Group submits = f.getSubmits();

	for (int i = 0; i < formsResults.length; i++) {
	    new SimpleOutput(controls, new Label(formsNames[i], (String) null),
		    null, formsResults[i]);
	}
	new Submit(submits, new Label("OK", (String) null), "testresp");

	return f;

    }

    private static Form getPresetForm(int size) {
	Form f = Form.newDialog("Form tester", (String) null);
	Group controls = f.getIOControls();
	Group submits = f.getSubmits();
	int i = 0;
	if (i++ <= size) {
	    new SimpleOutput(controls,
		    new Label("Simple Output", (String) null), null,
		    "Simple Output with a Label");
	}
	if (i++ <= size) {
	    new InputField(
		    controls,
		    new Label("Input field", (String) null),
		    new PropertyPath(
			    null,
			    false,
			    new String[] { "http://ontology.aal-persona.org/Tests.owl#input1" }),
		    null, null);
	}
	if (i++ <= size) {
	    Select1 s1 = new Select1(
		    controls,
		    new Label("Select1", (String) null),
		    new PropertyPath(
			    null,
			    false,
			    new String[] { "http://ontology.aal-persona.org/Tests.owl#input4" }),
		    null, null);
	    s1.generateChoices(new String[] { "Opt1", "Opt2", "Opt3" });
	}
	if (i++ <= size) {
	    Select ms1 = new Select(
		    controls,
		    new Label("Select", (String) null),
		    new PropertyPath(
			    null,
			    false,
			    new String[] { "http://ontology.aal-persona.org/Tests.owl#input6" }),
		    null, null);
	    ms1.generateChoices(new String[] { "OptA", "OptB", "OptC" });
	}
	if (i++ <= size) {
	    // Repeat table = new Repeat(controls,new
	    // Label("Repeat table",(String)null),new
	    // PropertyPath(null,false,new
	    // String[]{"http://ontology.aal-persona.org/Tests.owl#input8"}),null,
	    // null);
	    // Group tableGroup = new Group(table, null, null, null,
	    // (Resource)null);
	    // new SimpleOutput(tableGroup,new Label("Name",(String)null),new
	    // PropertyPath(null, true, new
	    // String[]{"http://ontology.aal-persona.org/Tests.owl#input9"}),null);
	    // new SimpleOutput(tableGroup,new
	    // Label("Measurement",(String)null),new PropertyPath(null, false,
	    // new
	    // String[]{"http://ontology.aal-persona.org/Tests.owl#input10"}),null);
	}
	if (i++ <= size) {
	    new MediaObject(controls, new Label("Media", (String) null), "IMG",
		    "android.handler/button.png");
	}
	if (i++ <= size) {
	    new TextArea(
		    controls,
		    new Label("Text Area", (String) null),
		    new PropertyPath(
			    null,
			    false,
			    new String[] { "http://ontology.aal-persona.org/Tests.owl#input11" }),
		    null, null);
	}
	if (i++ <= size) {
	    new Range(
		    controls,
		    new Label("Range", (String) null),
		    new PropertyPath(
			    null,
			    false,
			    new String[] { "http://ontology.aal-persona.org/Tests.owl#input12" }),
		    OrderingRestriction.newOrderingRestriction(
			    Integer.valueOf(12), Integer.valueOf(3), true,
			    true,
			    "http://ontology.aal-persona.org/Tests.owl#input12"),
		    new Integer(5));
	}
	if (i++ <= size) {
	    Group g1 = new Group(controls, new Label("Normal group with label",
		    (String) null), null, null, (Resource) null);
	    new SimpleOutput(g1, null, null, "In g1 group");
	}
	new Submit(submits, new Label("OK", (String) null), "testsubmit"); //$NON-NLS-1$ //$NON-NLS-2$
	return f;
    }

    private static Form getDynamicForm(int size) {
	Form f = Form.newDialog("Form tester", (String) null);
	Group controls = f.getIOControls();
	Group submits = f.getSubmits();
	Random r = new Random();
	for (int i = 0; i < size; i++) {
	    switch (r.nextInt(10)) {
	    case 1:
		new SimpleOutput(controls, new Label("Simple Output",
			(String) null), null, "Simple Output with a Label");
		break;
	    case 2:
		new InputField(
			controls,
			new Label("Input field", (String) null),
			new PropertyPath(
				null,
				false,
				new String[] { "http://ontology.aal-persona.org/Tests.owl#input1" }),
			null, null);
		break;
	    case 3:
		Select1 s1 = new Select1(
			controls,
			new Label("Select1", (String) null),
			new PropertyPath(
				null,
				false,
				new String[] { "http://ontology.aal-persona.org/Tests.owl#input4" }),
			null, null);
		s1.generateChoices(new String[] { "Opt1", "Opt2", "Opt3" });
		break;
	    case 4:
		Select ms1 = new Select(
			controls,
			new Label("Select", (String) null),
			new PropertyPath(
				null,
				false,
				new String[] { "http://ontology.aal-persona.org/Tests.owl#input6" }),
			null, null);
		ms1.generateChoices(new String[] { "OptA", "OptB", "OptC" });
		break;
	    case 5:
		// Repeat table = new Repeat(controls,new
		// Label("Repeat table",(String)null),new
		// PropertyPath(null,false,new
		// String[]{"http://ontology.aal-persona.org/Tests.owl#input8"}),null,
		// null);
		// Group tableGroup = new Group(table, null, null, null,
		// (Resource)null);
		// new SimpleOutput(tableGroup,new
		// Label("Name",(String)null),new PropertyPath(null, true, new
		// String[]{"http://ontology.aal-persona.org/Tests.owl#input9"}),null);
		// new SimpleOutput(tableGroup,new
		// Label("Measurement",(String)null),new PropertyPath(null,
		// false, new
		// String[]{"http://ontology.aal-persona.org/Tests.owl#input10"}),null);
		break;
	    case 6:
		new MediaObject(controls, new Label("Media", (String) null),
			"IMG", "android.handler/button.png");
		break;
	    case 7:
		new TextArea(
			controls,
			new Label("Text Area", (String) null),
			new PropertyPath(
				null,
				false,
				new String[] { "http://ontology.aal-persona.org/Tests.owl#input11" }),
			null, null);
		break;
	    case 8:
		new Range(
			controls,
			new Label("Range", (String) null),
			new PropertyPath(
				null,
				false,
				new String[] { "http://ontology.aal-persona.org/Tests.owl#input12" }),
			OrderingRestriction.newOrderingRestriction(
				Integer.valueOf(12), Integer.valueOf(3), true,
				true,
				"http://ontology.aal-persona.org/Tests.owl#input12"),
			new Integer(5));
		break;
	    case 9:
		Group g1 = new Group(controls, new Label(
			"Normal group with label", (String) null), null, null,
			(Resource) null);
		new SimpleOutput(g1, null, null, "In g1 group");
		break;
	    default:
		break;
	    }
	}
	new Submit(submits, new Label("OK", (String) null), "testsubmit"); //$NON-NLS-1$ //$NON-NLS-2$
	return f;
    }

}
