package com.willydupreez.prototype.structure;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;

public class TestTools {

	public static String getAppDirectory() {
		URL url = TestTools.class.getResource("/app_home");
		try {
			return Paths.get(url.toURI()).toString();
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}
	}

}
