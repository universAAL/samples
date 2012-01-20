package org.universAAL.platform.ui.tester;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.universAAL.middleware.container.ModuleContext;
import org.universAAL.middleware.io.owl.PrivacyLevel;
import org.universAAL.middleware.io.rdf.Form;
import org.universAAL.middleware.io.rdf.Group;
import org.universAAL.middleware.io.rdf.InputField;
import org.universAAL.middleware.io.rdf.Label;
import org.universAAL.middleware.io.rdf.MediaObject;
import org.universAAL.middleware.io.rdf.Range;
import org.universAAL.middleware.io.rdf.Repeat;
import org.universAAL.middleware.io.rdf.Select;
import org.universAAL.middleware.io.rdf.Select1;
import org.universAAL.middleware.io.rdf.SimpleOutput;
import org.universAAL.middleware.io.rdf.Submit;
import org.universAAL.middleware.io.rdf.TextArea;
import org.universAAL.middleware.output.OutputEvent;
import org.universAAL.middleware.output.OutputPublisher;
import org.universAAL.middleware.owl.OrderingRestriction;
import org.universAAL.middleware.owl.Restriction;
import org.universAAL.middleware.rdf.PropertyPath;
import org.universAAL.middleware.rdf.Resource;
import org.universAAL.middleware.rdf.TypeMapper;
import org.universAAL.ontology.profile.ElderlyUser;
import org.universAAL.ontology.profile.User;

public class OPublisher extends OutputPublisher {

	private final static Logger log = LoggerFactory.getLogger(OPublisher.class);

	protected OPublisher(ModuleContext context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public void communicationChannelBroken() {
		// TODO Auto-generated method stub

	}

	public void showTestDialog(User user) {
		log.debug("Show dialog from OPub");
		Form f = getTestForm(new ElderlyUser(user.getURI()));
		OutputEvent oe = new OutputEvent(user, f, null, Locale.ENGLISH,
				PrivacyLevel.insensible);
		Activator.ninput.subscribe(f.getDialogID());
		log.debug("Publish dialog from OPub");
		publish(oe);
	}

	public void showRespDialog(User user, String content) {
		log.debug("Show result from OPub");
		Form f = getRespForm(new ElderlyUser(user.getURI()), content);
		OutputEvent oe = new OutputEvent(user, f, null, Locale.ENGLISH,
				PrivacyLevel.insensible);
		Activator.ninput.subscribe(f.getDialogID());
		log.debug("Publish dialog from OPub");
		publish(oe);
	}

	public void showAllRespDialog(User user, String[] formsNames,
			String[] formsResults) {
		log.debug("Show result from OPub");
		Form f = getAllRespForm(formsNames, formsResults);
		OutputEvent oe = new OutputEvent(user, f, null, Locale.ENGLISH,
				PrivacyLevel.insensible);
		Activator.ninput.subscribe(f.getDialogID());
		log.debug("Publish dialog from OPub");
		publish(oe);
	}

	private Form getRespForm(ElderlyUser elderlyUser, String content) {
		Form f = Form.newDialog("Input response", (String) null);
		Group controls = f.getIOControls();
		Group submits = f.getSubmits();

		new SimpleOutput(controls, new Label(content, (String) null), null,
				content);

		new Submit(submits, new Label("OK", (String) null), "testallresp");

		return f;
	}

	public Form getTestForm(ElderlyUser user) {
		Form f = Form.newDialog("Form tester", (String) null);
		Group controls = f.getIOControls();
		Group submits = f.getSubmits();

		new SimpleOutput(controls, new Label("Simple Output", (String) null),
				null, "Simple Output with a Label");
		new SimpleOutput(controls, null, null, "Simple Output without a label");
		new SimpleOutput(controls, new Label(
				"Simple Output toooooooooooooooooooooooooooooo long",
				(String) null), null,
				"Simple Output toooooooooooooooooooooooooooooo long");

		new InputField(
				controls,
				new Label("Input field", (String) null),
				new PropertyPath(
						null,
						false,
						new String[] { "http://ontology.aal-persona.org/Tests.owl#input1" }),
				null, null);
		new InputField(
				controls,
				null,
				new PropertyPath(
						null,
						false,
						new String[] { "http://ontology.aal-persona.org/Tests.owl#input2" }),
				null, "Input with initial value without label");
		// new InputField(controls,new
		// Label("Input field boolean",(String)null),new
		// PropertyPath(null,false,new
		// String[]{"http://ontology.aal-persona.org/Tests.owl#input3"}),null,new
		// Boolean(true));
		new InputField(
				controls,
				new Label("Input field boolean", (String) null),
				new PropertyPath(
						null,
						false,
						new String[] { "http://ontology.aal-persona.org/Tests.owl#input3" }),
				Restriction.getAllValuesRestrictionWithCardinality(
						"http://ontology.aal-persona.org/Tests.owl#input3",
						TypeMapper.getDatatypeURI(Boolean.class), 1, 1),
				Boolean.TRUE);

		Select1 s1 = new Select1(
				controls,
				new Label("Select1", (String) null),
				new PropertyPath(
						null,
						false,
						new String[] { "http://ontology.aal-persona.org/Tests.owl#input4" }),
				null, null);
		s1.generateChoices(new String[] { "Opt1", "Opt2", "Opt3" });

		Select1 s2 = new Select1(
				controls,
				null,
				new PropertyPath(
						null,
						false,
						new String[] { "http://ontology.aal-persona.org/Tests.owl#input5" }),
				null, "Initial no label");
		s2.generateChoices(new String[] { "Initial no label", "Other",
				"Loooooooooooooooooooooooooooooooooooooooong" });

		Select ms1 = new Select(
				controls,
				new Label("Select", (String) null),
				new PropertyPath(
						null,
						false,
						new String[] { "http://ontology.aal-persona.org/Tests.owl#input6" }),
				null, null);
		ms1.generateChoices(new String[] { "OptA", "OptB", "OptC" });

		Select ms2 = new Select(
				controls,
				null,
				new PropertyPath(
						null,
						false,
						new String[] { "http://ontology.aal-persona.org/Tests.owl#input7" }),
				null, "Initial & single & no label");
		ms2.generateChoices(new String[] { "Initial & single & no label" });

		Repeat table = new Repeat(
				controls,
				new Label("Repeat table", (String) null),
				new PropertyPath(
						null,
						false,
						new String[] { "http://ontology.aal-persona.org/Tests.owl#input8" }),
				null, null);
		Group tableGroup = new Group(table, null, null, null, (Resource) null);
		new SimpleOutput(
				tableGroup,
				new Label("Name", (String) null),
				new PropertyPath(
						null,
						true,
						new String[] { "http://ontology.aal-persona.org/Tests.owl#input9" }),
				null);
		new SimpleOutput(
				tableGroup,
				new Label("Measurement", (String) null),
				new PropertyPath(
						null,
						false,
						new String[] { "http://ontology.aal-persona.org/Tests.owl#input10" }),
				null);

		new MediaObject(controls, new Label("Media", (String) null), "IMG",
				"android.handler/button.png");

		new TextArea(
				controls,
				new Label("Text Area", (String) null),
				new PropertyPath(
						null,
						false,
						new String[] { "http://ontology.aal-persona.org/Tests.owl#input11" }),
				null, null);

		new Range(
				controls,
				new Label("Range", (String) null),
				new PropertyPath(
						null,
						false,
						new String[] { "http://ontology.aal-persona.org/Tests.owl#input12" }),
				OrderingRestriction.newOrderingRestriction(Integer.valueOf(12),
						Integer.valueOf(3), true, true,
						"http://ontology.aal-persona.org/Tests.owl#input12"),
				new Integer(5));

		new Submit(submits, new Label("OK", (String) null), "testsubmit"); //$NON-NLS-1$ //$NON-NLS-2$

		Group g1 = new Group(controls, new Label("Normal group with label",
				(String) null), null, null, (Resource) null);
		new SimpleOutput(g1, null, null, "In g1 group");

		Group g2 = new Group(controls, new Label("Other group with label",
				(String) null), null, null, (Resource) null);
		new SimpleOutput(g2, null, null, "In g2 group");

		Group g3 = new Group(g2, new Label("Nested group with label",
				(String) null), null, null, (Resource) null);
		new SimpleOutput(g3, null, null, "In g3 group");

		Group g4 = new Group(controls, null, null, null, (Resource) null);
		new SimpleOutput(g4, null, null, "In g4 group, with no label");
		// Group g2a=new Group(g1,new
		// org.persona.middleware.dialog.Label("Normal group (A) with label, LEVEL 2, inside is V",(String)null),null,null,(PResource)null);
		// Group g2b=new Group(g1,new
		// org.persona.middleware.dialog.Label("Normal group (B) with label, LEVEL 2, inside is V",(String)null),null,null,(PResource)null);
		// Group g3a=new Group(g2a,new
		// org.persona.middleware.dialog.Label("Normal group with label, LEVEL 3, inside is H",(String)null),null,null,(PResource)null);
		// Group g3b=new Group(g2a,new
		// org.persona.middleware.dialog.Label("Normal group with label, LEVEL 3, inside is H",(String)null),null,null,(PResource)null);

		// Group g4a=new Group(g2b,new
		// org.persona.middleware.dialog.Label("",(String)null),null,null,(PResource)null);
		// Group g4c=new Group(g2b,null,null,null,(PResource)null);
		//		
		// Group g4d=new Group(g3b,null,null,null,(PResource)null);
		// Group g4f=new Group(g3b,null,null,null,(PResource)null);
		//		
		// new SimpleOutput(g1,null,null,"In g1 group");
		// new SimpleOutput(g4a,null,null,"In \"\" group");
		// new SimpleOutput(g4c,null,null,"In null group");
		// new SimpleOutput(g4c,null,null,"In null group");
		//		
		// new SimpleOutput(g4d,null,null,"In null group 1");
		// new SimpleOutput(g4d,null,null,"In null group 1");
		// new SimpleOutput(g4f,null,null,"In null group 2");
		// new SimpleOutput(g4f,null,null,"In null group 2");

		//		new Submit(submits,new org.persona.middleware.dialog.Label("NutricionalGUI.Today",(String)null),"today"); //$NON-NLS-1$ //$NON-NLS-2$
		//		new Submit(submits,new org.persona.middleware.dialog.Label("NutricionalGUI.Week",(String)null),"week"); //$NON-NLS-1$ //$NON-NLS-2$
		//		new Submit(submits,new org.persona.middleware.dialog.Label("NutricionalGUI.Nutritional_Profile",(String)null),"profile"); //$NON-NLS-1$ //$NON-NLS-2$
		//		new Submit(submits,new org.persona.middleware.dialog.Label("NutricionalGUI.Home",(String)null),"home"); //$NON-NLS-1$ //$NON-NLS-2$

		return f;
	}

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

}
