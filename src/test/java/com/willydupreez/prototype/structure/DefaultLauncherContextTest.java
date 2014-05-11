package com.willydupreez.prototype.structure;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.io.File;

import org.junit.Test;

public class DefaultLauncherContextTest {

	@Test
	public void testBuilder() {

		String appHome = TestTools.getAppDirectory();
		String ps = File.separator;

		DefaultLauncherContext context = DefaultLauncherContext.builder()
				.applicationHome(appHome)
				.build();

		assertThat(context.getApplicationHomeDirectory(), is(appHome));
		assertThat(context.getBinDirectory(), is(appHome + ps + "bin"));
		assertThat(context.getConfigDirectory(), is(appHome + ps + "etc"));
		assertThat(context.getDataDirectory(), is(appHome + ps + "data"));
		assertThat(context.getLibDirectory(), is(appHome + ps + "lib"));
		assertThat(context.getLogDirectory(), is(appHome + ps + "data" + ps + "log"));
		assertThat(context.getResourceDirectory(), is(appHome + ps + "res"));
		assertThat(context.getResourcePublicDirectory(), is(appHome + ps + "res-public"));
		assertThat(context.getSystemDataDirectory(), is(appHome + ps + "data" + ps + "system"));
		assertThat(context.getSystemDirectory(), is(appHome + ps + "system"));
		assertThat(context.getTempDirectory(), is(appHome + ps + "data" + ps + "tmp"));

		assertThat(context.getPropertiesFactory(), is(notNullValue()));
	}

}
