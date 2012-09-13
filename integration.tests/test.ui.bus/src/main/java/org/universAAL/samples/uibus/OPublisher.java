package org.universAAL.samples.uibus;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.universAAL.middleware.container.ModuleContext;
import org.universAAL.middleware.owl.IntRestriction;
import org.universAAL.middleware.owl.MergedRestriction;
import org.universAAL.middleware.rdf.PropertyPath;
import org.universAAL.middleware.rdf.Resource;
import org.universAAL.middleware.ui.UICaller;
import org.universAAL.middleware.ui.UIRequest;
import org.universAAL.middleware.ui.UIResponse;
import org.universAAL.middleware.ui.owl.PrivacyLevel;
import org.universAAL.middleware.ui.rdf.Form;
import org.universAAL.middleware.ui.rdf.Group;
import org.universAAL.middleware.ui.rdf.InputField;
import org.universAAL.middleware.ui.rdf.Label;
import org.universAAL.middleware.ui.rdf.MediaObject;
import org.universAAL.middleware.ui.rdf.Range;
import org.universAAL.middleware.ui.rdf.Repeat;
import org.universAAL.middleware.ui.rdf.Select;
import org.universAAL.middleware.ui.rdf.Select1;
import org.universAAL.middleware.ui.rdf.SimpleOutput;
//import org.universAAL.middleware.ui.rdf.SubdialogTrigger;
import org.universAAL.middleware.ui.rdf.Submit;
import org.universAAL.middleware.ui.rdf.TextArea;
//import org.universAAL.ontology.location.Location;
import org.universAAL.ontology.profile.User;
//import org.universAAL.samples.ui.utils.Forms;
//import org.universAAL.samples.ui.utils.SimpleDataTable;
//import org.universAAL.samples.ui.utils.SimpleGroup;
//import org.universAAL.samples.ui.utils.SimpleOut;
//import org.universAAL.samples.ui.utils.SimpleSubmit;
//import org.universAAL.samples.ui.utils.SimpleText;
//import org.universAAL.samples.ui.utils.SimpleTrigger;
//import org.universAAL.samples.ui.utils.low.SimpleDialog;

public class OPublisher extends UICaller {

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
	UIRequest oe = new UIRequest(user, f, null, Locale.ENGLISH,
		PrivacyLevel.insensible);
	log.debug("Publish dialog from OPub");
	sendUIRequest(oe);
    }

    public long showRandomBurst(User user, int size) {
	Random r = new Random();
	long t0 = System.currentTimeMillis();
	for (int i = 0; i < size; i++) {
	    Form f = presetForms[r.nextInt(10)];
	    UIRequest oe = new UIRequest(user, f, null, Locale.ENGLISH,
		    PrivacyLevel.insensible);
	    sendUIRequest(oe);
	}
	long t1 = System.currentTimeMillis();
	return t1 - t0;
    }

    public long showDynamicDialog(User user, int size) {
	long t0 = System.currentTimeMillis();
	Form f = getDynamicForm(size);
	UIRequest oe = new UIRequest(user, f, null, Locale.ENGLISH,
	PrivacyLevel.insensible);
//	SimpleDialog d=new SimpleDialog(user, "Dialog");
//	SimpleGroup go=new SimpleGroup(null,"Outputs");
//	d.add(go);
//	go.add(Forms.out("Out", "Simple Out"));
//	go.add(Forms.media("Media", "icons/services/Health_button.png"));
//	SimpleGroup gi=new SimpleGroup(null,"Inputs");
//	d.add(gi);
//	gi.add(Forms.text("http://ontology.aal-persona.org/Tests.owl#input1", "Text"));
//	gi.add(Forms.check("http://ontology.aal-persona.org/Tests.owl#input2", "Check"));
//	gi.add(Forms.area("http://ontology.aal-persona.org/Tests.owl#input3", "Area"));
//	gi.add(Forms.one("http://ontology.aal-persona.org/Tests.owl#input4", "Select 1", new String[]{"one", "two", "three"}));
//	gi.add(Forms.multi("http://ontology.aal-persona.org/Tests.owl#input5", "Select N", new String[]{"a","b","c"}));
//	gi.add(Forms.range("http://ontology.aal-persona.org/Tests.owl#input6", "Range", 5, 10));
//	SimpleGroup gs=new SimpleGroup(null,"Submits");
//	d.add(gs);
//	gs.add(Forms.submit("http://ontology.aal-persona.org/Tests.owl#submit1", "Submit"));
//	gs.add(Forms.trigger("http://ontology.aal-persona.org/Tests.owl#submit2", "Trigger"));
//	SimpleGroup gg=new SimpleGroup(null,"Groups");
//	d.add(gg);
//	SimpleGroup g1=new SimpleGroup(null,"Group 1");
//	SimpleGroup g2=new SimpleGroup(null,"Group 2");
//	SimpleGroup g3=new SimpleGroup(null,"");
//	gg.add(g1);
//	g1.add(g2);
//	g2.add(g3);
//	SimpleDataTable r = new SimpleDataTable("Table",
//		new Location[] {
//			new Location(
//				"http://ontology.itaca.es/Client.owl#loc1",
//				"Kitchen"),
//			new Location(
//				"http://ontology.itaca.es/Client.owl#loc2",
//				"Hall") });
//	gg.add(r);
//	SimpleOut col1=new SimpleOut(Location.PROP_HAS_NAME,"Name",null);
//	SimpleTrigger col2=new SimpleTrigger(Location.PROP_HAS_NAME,"Button");
//	r.add(col1);
//	r.add(col2);
//	d.addSubmit(Forms.submit(null, "Submit"));
	sendUIRequest(oe);
	long t1 = System.currentTimeMillis();
	return t1 - t0;
    }

    public void showAllRespDialog(User user, String[] formsNames,
	    String[] formsResults) {
	log.debug("Show result from OPub");
	Form f = getAllRespForm(formsNames, formsResults);
	UIRequest oe = new UIRequest(user, f, null, Locale.ENGLISH,
		PrivacyLevel.insensible);
	log.debug("Publish dialog from OPub");
	sendUIRequest(oe);
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
	    User u1 = new User(
		    "http://ontology.aal-persona.org/Tests.owl#userentry1");
	    User u2 = new User(
		    "http://ontology.aal-persona.org/Tests.owl#userentry2");
	    u1.setResourceLabel("userentry1");
	    u2.setResourceLabel("userentry2");
	    u1.setResourceComment("Comment 1");
	    u2.setResourceComment("Comment 2");
	    ArrayList l = new ArrayList();
	    l.add(u1);
	    l.add(u2);
	    Repeat r = new Repeat(controls,new Label("Repeat table", (String) null),
		    new PropertyPath(null,false,
			    new String[] { "http://ontology.aal-persona.org/Tests.owl#input8" }),
		    null, l);
	    Group g = new Group(r, null, null, null, null);
	    new SimpleOutput(g, new Label("Label", (String) null),
		    new PropertyPath(null, true,
			    new String[] { User.PROP_RDFS_LABEL }), null);
	    new SimpleOutput(g, new Label("Comment", (String) null),
		    new PropertyPath(null, false,
			    new String[] { User.PROP_RDFS_COMMENT }), null);
	    
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
	    new MediaObject(controls, new Label("Media", (String) null), "image",
		    "http://127.0.0.1:8080/resources/test.ui.bus/sample.png");
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
		    MergedRestriction
			    .getAllValuesRestriction(
				    "http://ontology.aal-persona.org/Tests.owl#input12",
				    new IntRestriction(3, true, 12, true)),
		    new Integer(5));
//	    new Range(
//		    controls,
//		    new Label("Range", (String) null),
//		    new PropertyPath(
//			    null,
//			    false,
//			    new String[] { "http://ontology.aal-persona.org/Tests.owl#input12" }),
//		    MergedRestriction.getAllValuesRestrictionWithCardinality(
//			    Range.PROP_VALUE_RESTRICTION, new IntRestriction(3,
//				    true, 12, true), 1, 1), new Integer(5));
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
			"image", "http://127.0.0.1:8080/resources/test.ui.bus/sample.png");
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
//		new Range(
//			controls,
//			new Label("Range", (String) null),
//			new PropertyPath(
//				null,
//				false,
//				new String[] { "http://ontology.aal-persona.org/Tests.owl#input12" }),
//			MergedRestriction
//				.getAllValuesRestrictionWithCardinality(
//					Range.PROP_VALUE_RESTRICTION,
//					new IntRestriction(3, true, 12, true),
//					1, 1), new Integer(5));
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

    @Override
    public void dialogAborted(String dialogID) {
	// TODO Auto-generated method stub
	
    }

    @Override
    public void handleUIResponse(UIResponse event) {
	User user = (User) event.getUser();
	log.info("Received an Input Event from user {}", user.getURI());
	String submit = event.getSubmissionID();
	try {
	     if(submit.contains("submit")){
	     String[] formsNames=new
	     String[]{"1","2","3","4","5","6"};
	     String[] formsResults=new String[6];
		formsResults[0] = event
			.getUserInput(new String[] { ("http://ontology.aal-persona.org/Tests.owl#input1") }) != null ? event
			.getUserInput(
				new String[] { ("http://ontology.aal-persona.org/Tests.owl#input1") })
			.toString()
			: "-";
		formsResults[1] = event
			.getUserInput(new String[] { ("http://ontology.aal-persona.org/Tests.owl#input2") }) != null ? event
			.getUserInput(
				new String[] { ("http://ontology.aal-persona.org/Tests.owl#input2") })
			.toString()
			: "-";
		formsResults[2] = event
			.getUserInput(new String[] { ("http://ontology.aal-persona.org/Tests.owl#input3") }) != null ? event
			.getUserInput(
				new String[] { ("http://ontology.aal-persona.org/Tests.owl#input3") })
			.toString()
			: "-";
		formsResults[3] = event
			.getUserInput(new String[] { ("http://ontology.aal-persona.org/Tests.owl#input4") }) != null ? event
			.getUserInput(
				new String[] { ("http://ontology.aal-persona.org/Tests.owl#input4") })
			.toString()
			: "-";
		formsResults[4] = event
			.getUserInput(new String[] { ("http://ontology.aal-persona.org/Tests.owl#input5") }) != null ? event
			.getUserInput(
				new String[] { ("http://ontology.aal-persona.org/Tests.owl#input5") })
			.toString()
			: "-";
		formsResults[5] = event
			.getUserInput(new String[] { ("http://ontology.aal-persona.org/Tests.owl#input6") }) != null ? event
			.getUserInput(
				new String[] { ("http://ontology.aal-persona.org/Tests.owl#input6") })
			.toString()
			: "-";
//	     formsResults[6]=event.getUserInput(new
//	     String[]{("http://ontology.aal-persona.org/Tests.owl#input7")}).toString();
//	     formsResults[7]=event.getUserInput(new
//	     String[]{("http://ontology.aal-persona.org/Tests.owl#input8")}).toString();
//	     formsResults[8]=event.getUserInput(new
//	     String[]{("http://ontology.aal-persona.org/Tests.owl#input9")}).toString();
	     Activator.uoutput.showAllRespDialog(user,formsNames,formsResults);
	     }
	} catch (Exception e) {
	    log.error("Error while processing the user input: {}", e);
	}
    }

}
