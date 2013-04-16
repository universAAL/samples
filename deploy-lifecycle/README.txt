---------------
* FIRST STEPS *
---------------
Please rename each example from Example-<#> to the proper name of application identified by Gema, please use 
the command svn mv <oldname> <newname> ( or Move from TortoiseSVN context menu ) so that we keep track of 
everything

------------------
* CONTENT LAYOUT *
------------------
Each example folder contains the following subfolder:
 - packager: which contains the output produced by the Packaging Tool (Manlio Bacco) which will be used for deploying services to the uStore
 - uStore: which contains the output produced by the uStore (Nicole Merkle, Tom Zentek) when a user select to install a particular application by delegating the installation to the uCC
 - uCC: which should contains the folder created by the uCC (Shanshan Jiang) that is sent to the Deploy Manager (Stefano Lenzi) when a the user what to a uSrv

Moreover there is the schemas folder which should contains the official XSD ( Venelin Arnaudov ) file that will be used across all the lifecycle scenario, that is that all software should be compatibile with those schemas.

For more information please ask to ( Gema Ibaez Sanchez ) which is coordinating the integration effort

-----------------------
* CONTACT INFORMATION *
-----------------------
Stefano Lenzi		stefano.lenzi@isti.cnr.it		kismet-sl
Manlio Bacco		manlio.bacco@isti.cnr.it		manlio.bacco
Gema Ibaez Sanchez	geibsan@itaca.upv.es			geibsan
Shanshan Jiang		shanshan.jiang@sintef.no		shanshantrd2008
Nicole Merkle		merkle@fzi.de
Tom Zentek		zentek@fzi.de				tom.zentek
Venelin Arnaudov	v.arnaudov@prosyst.com			venstar1

