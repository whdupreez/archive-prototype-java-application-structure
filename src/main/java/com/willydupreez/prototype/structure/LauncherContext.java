package com.willydupreez.prototype.structure;

import com.willydupreez.prototype.config.PropertiesFactory;

public interface LauncherContext  {

	String getApplicationHomeDirectory();
	String getDataDirectory();
	String getTempDirectory();
	String getConfigDirectory();
	String getLibDirectory();
	String getResourceDirectory();
	String getResourcePublicDirectory();

	PropertiesFactory getPropertiesFactory();

}
