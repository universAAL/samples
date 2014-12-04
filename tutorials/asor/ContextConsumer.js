importPackage(java.util);
importClass(org.universAAL.ontology.lighting.LightSource);

// register context event pattern
function handleEvent(event) {
	println("Received context event:\n"+ "    Subject     = " +
		event.getSubjectURI() + "\n" + "    Subject type= " +
		event.getSubjectTypeURI() + "\n" + "    Predicate   = " +
		event.getRDFPredicate() + "\n" + "    Object      = " +
		event.getRDFObject());
}

var cep = new ContextEventPattern();
cep.addRestriction(MergedRestriction.getAllValuesRestriction(ContextEvent.PROP_RDF_SUBJECT, LightSource.MY_URI));
ctxt.register("handleEvent", cep);
