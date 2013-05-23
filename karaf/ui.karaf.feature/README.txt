This file should be removed in the future:

1)install this bundle
2)run Karaf
3)add and install the MW2.0 feature: 

--> features:addurl mvn:org.universAAL.middleware/mw.karaf.feature.universAAL.osgi/1.3.2-SNAPSHOT/xml/features
--> features:install uAAL-MW

4)Add the UI feature:

--> features:addurl mvn:org.universAAL.ui/ui.karaf.feature/1.3.2-SNAPSHOT/xml/features
--> features:install uAAL-UI

note: install uAAL-UI will install uAAL-MW also


Soon an official guide. Now you can check http://forge.universaal.org/wiki/middleware:MW2.0_Building_Block#Run_the_MW2.0

