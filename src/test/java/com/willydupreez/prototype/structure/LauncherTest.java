package com.willydupreez.prototype.structure;

import java.lang.reflect.Field;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class LauncherTest {

	private String appHome;

	@Before
	public void before() {
		this.appHome = TestTools.getAppDirectory();
	}

	@After
	public void after() {
		Launcher launcher = null;
		Field launcherField = null;
		try {
			launcherField = Launcher.class.getDeclaredField("launcher");
			launcherField.setAccessible(true);
			launcher = (Launcher) launcherField.get(null);
			launcher.stop();
		} catch (Exception e) {
			// Ignore.
		} finally {
			if (launcher != null) {
				launcher.destroy();
			}
			if (launcherField != null) {
				try {
					launcherField.set(null, null);
				} catch (Exception e1) {
				}
			}
		}
	}

	@Test
	public void testMain_Start_Stop() throws Exception {
		System.out.println("testMain_Start_Stop");
		Launcher.main(new String[] { "start", appHome });
		Launcher.main(new String[] { "stop" });
	}

	@Test(expected = IllegalArgumentException.class)
	public void testMain_Start_NoAppHome() throws Exception {
		System.out.println("testMain_Start_NoAppHome");
		Launcher.main(new String[] { "start" });
	}

	@Test(expected = IllegalArgumentException.class)
	public void testMain_NoArguments() throws Exception {
		System.out.println("testMain_NoArguments");
		Launcher.main(new String[] { });
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testMain_UnsupportedOperation() throws Exception {
		System.out.println("testMain_UnsupportedOperation");
		Launcher.main(new String[] { "do" });
	}

	@Test
	public void testMain_Console() throws Exception {
		System.out.println("testMain_Console");
		Launcher.main(new String[] { "run", appHome });
	}

	@Test(expected = IllegalArgumentException.class)
	public void testMain_Console_NoAppHome() throws Exception {
		System.out.println("testMain_Console_NoAppHome");
		Launcher.main(new String[] { "run" });
	}

}
