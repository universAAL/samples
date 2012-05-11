README.

Before you run:
	1. make sure you've installed (mvn install) simple.ui project.
	2. remember to make sure these files exist:
		runner/configurations/ui.dm.mobile/main_menu_saied_<XX>.txt
		runner/configurations/ui.dm.mobile/main_menu_<XX>.txt
	 Where <XX> is the 2 letter locale on your computer.
	 If not then just copy any of the existing files.
	3. select a handler leave only one of the following uncommented
		<provision>scan-composite:mvn:org.universAAL.ui/ui.handler.gui/1.1.0/composite</provision>
		<provision>scan-composite:mvn:org.universAAL.ui/ui.handler.gui.swing/1.1.0/composite</provision>
	(the selected one by default is the second handler)

to run:
1) from eclipse
	1. import the project in eclipse
	2. right click on it and run as -> maven build (Without the dots)
	3. the first time a form will appear, just input "pax:run" (without quotes) in the goal field and hit run

2) from a console
	 	execute Run.bat (with capital R) under this directory.
	OR	execute "mvn pax:run" (without the quotes) under this directory.

universAAL environment:
	when prompted user, password; leave blank and click login (only if using ui.handler.gui).
	