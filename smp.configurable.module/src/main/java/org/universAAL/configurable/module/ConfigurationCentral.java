/*******************************************************************************
 * Copyright 2014 2011 Universidad Politécnica de Madrid
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
package org.universAAL.configurable.module;

import java.net.URL;
import java.util.Locale;

import org.universAAL.middleware.interfaces.configuration.ConfigurableModule;
import org.universAAL.middleware.interfaces.configuration.configurationDefinitionTypes.ConfigurationFile;
import org.universAAL.middleware.interfaces.configuration.configurationDefinitionTypes.ConfigurationParameter;
import org.universAAL.middleware.interfaces.configuration.configurationDefinitionTypes.DescribedEntity;
import org.universAAL.middleware.interfaces.configuration.scope.Scope;
import org.universAAL.middleware.owl.IntRestriction;
import org.universAAL.middleware.owl.MergedRestriction;
import org.universAAL.middleware.rdf.TypeMapper;

/**
 * An example of a {@link ConfigurableModule}, not all implementations should be like this.
 * @author amedrano
 *
 */
public class ConfigurationCentral implements ConfigurableModule{

	public static String APPID = "configurationSample";
	
	public static DescribedEntity[] configurations = {
		new ConfigurationParameter() {
			
			public Scope getScope() {
				return Scope.applicationScope("app.name", APPID);
			}
			
			public String getDescription(Locale loc) {
				return "the string to use as application name";
			}
			
			public MergedRestriction getType() {
				return  MergedRestriction
						.getAllValuesRestrictionWithCardinality(ConfigurationParameter.PROP_CONFIG_VALUE, 
								TypeMapper.getDatatypeURI(String.class), 1, 1);
			}
			
			public Object getDefaultValue() {
				return "Configuration Example";
			}
		},

		new ConfigurationParameter() {
		    
		    public Scope getScope() {
			return Scope.applicationScope("number.of.foos", APPID);
		    }
		    
		    public String getDescription(Locale loc) {
			if (loc.equals(Locale.FRENCH)){
			    //return description in french.
			}
			return "The number of foos to give by this module, between 0 and 10";
		    }
		    
		    public MergedRestriction getType() {
			MergedRestriction mr = MergedRestriction
				.getAllValuesRestrictionWithCardinality(ConfigurationParameter.PROP_CONFIG_VALUE, 
					TypeMapper.getDatatypeURI(Integer.class), 1, 1);
			mr.addType(new IntRestriction(0, true, 10, true));
			return mr;
		    }
		    
		    public Object getDefaultValue() {
			return 1;
		    }
		},
		new ConfigurationFile() {
			
			public Scope getScope() {
				return Scope.applicationScope("optional.file", APPID);
			}
			
			public String getDescription(Locale loc) {
				return "a file that only I could interpret, I.E: not a properties file.";
			}
			
			public String getExtensionfilter() {
				return ".xml";
			}
			
			public URL getDefaultFileRef() {
				return null;
			}
		},
	};
	
	/** {@inheritDoc} */
	public boolean configurationChanged(Scope param, Object value) {
		System.out.println(param.getId() + " changed to: " + value);
		//in this example always accepted...
		return true;
	}

}
