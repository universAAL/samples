/*******************************************************************************
 * Copyright 2011 Universidad Politécnica de Madrid
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package org.universAAL.ui.full.test;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Locale;
import java.util.TreeMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.universAAL.middleware.container.ModuleContext;
import org.universAAL.middleware.owl.supply.LevelRating;
import org.universAAL.middleware.rdf.Resource;
import org.universAAL.middleware.ui.UICaller;
import org.universAAL.middleware.ui.UIRequest;
import org.universAAL.middleware.ui.UIResponse;
import org.universAAL.middleware.ui.owl.PrivacyLevel;
import org.universAAL.middleware.ui.rdf.Form;

public class MyUICaller extends UICaller {

    private Resource user = null;
    private LevelRating priority = LevelRating.low;
    private Locale locale = Locale.ENGLISH;
    private PrivacyLevel privacy = PrivacyLevel.insensible;

    static TreeMap<String, AbstractForm> inputMapper;

    static private String pName = "org.universAAL.ui.full.test.forms";

    public MyUICaller(ModuleContext mcontext) {
	super(mcontext);
    }

    public static AbstractForm getAbstractForm(String cName) {
	AbstractForm af = null;
	try {
	    af = (AbstractForm) Class.forName(pName + "." + cName)
		    .getConstructor((Class<?>) null).newInstance((Object) null);
	} catch (Exception e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	return af;
    }

    public static String[] getClassNamesFromPackage() throws IOException {

	ClassLoader classLoader = Thread.currentThread()
		.getContextClassLoader();
	URL packageURL;
	ArrayList<String> names = new ArrayList<String>();

	String packageName = pName.replace(".", "/");
	packageURL = classLoader.getResource(packageName);

	if (packageURL.getProtocol().equals("jar")) {
	    String jarFileName;
	    JarFile jf;
	    Enumeration<JarEntry> jarEntries;
	    String entryName;

	    // build jar file name, then loop through zipped entries
	    jarFileName = URLDecoder.decode(packageURL.getFile(), "UTF-8");
	    jarFileName = jarFileName.substring(5, jarFileName.indexOf("!"));
	    System.out.println(">" + jarFileName);
	    jf = new JarFile(jarFileName);
	    jarEntries = jf.entries();
	    while (jarEntries.hasMoreElements()) {
		entryName = jarEntries.nextElement().getName();
		if (entryName.startsWith(packageName)
			&& entryName.length() > packageName.length() + 5) {
		    entryName = entryName.substring(packageName.length(),
			    entryName.lastIndexOf('.'));
		    names.add(entryName);
		}
	    }

	    // loop through files in classpath
	} else {
	    File folder = new File(packageURL.getFile());
	    File[] contenuti = folder.listFiles();
	    String entryName;
	    for (File actual : contenuti) {
		entryName = actual.getName();
		entryName = entryName.substring(0, entryName.lastIndexOf('.'));
		names.add(entryName);
	    }
	}
	return names.toArray(new String[] {});
    }

    @Override
    public void handleUIResponse(UIResponse uiResponse) {
	inputMapper.get(uiResponse.getDialogID()).handleUIResponse(uiResponse);
	inputMapper.remove(uiResponse.getDialogID());

    }

    public void mapAndSendUIRequest(AbstractForm af) {
	Form f = af.getDialog();
	MyUICaller.inputMapper.put(f.getDialogID(), af);
	sendUIRequest(new UIRequest(user, f, priority, locale, privacy));
    }

    /**
     * @return the priority
     */
    public LevelRating getPriority() {
	return priority;
    }

    /**
     * @param priority
     *            the priority to set
     */
    public void setPriority(LevelRating priority) {
	this.priority = priority;
    }

    /**
     * @return the locale
     */
    public Locale getLocale() {
	return locale;
    }

    /**
     * @param locale
     *            the locale to set
     */
    public void setLocale(Locale locale) {
	this.locale = locale;
    }

    /**
     * @return the privacy
     */
    public PrivacyLevel getPrivacy() {
	return privacy;
    }

    /**
     * @param privacy
     *            the privacy to set
     */
    public void setPrivacy(PrivacyLevel privacy) {
	this.privacy = privacy;
    }

    /**
     * @return the user
     */
    public Resource getUser() {
	return user;
    }

    public void setUser(Resource u) {
	user = u;
    }

    public void communicationChannelBroken() {
	// TODO Auto-generated method stub

    }

    public void dialogAborted(String dialogID) {
	// TODO Auto-generated method stub
    }
    /*
     * public void registerUI(String formID, AbstractForm listener) {
     * inputMapper.put(formID, listener); addNewRegParams(formID); }
     * 
     * public void unresgisterUI(String formID) { inputMapper.remove(formID);
     * removeMatchingRegParams(formID); }
     */
}
