package com.willydupreez.prototype.structure;

import java.util.Arrays;

/**
 * The application entry point.
 *
 * @author Willy du Preez
 *
 */
// TODO Create children of data directory if not exist
// TODO Clean command
public class Launcher {

	private static Launcher launcher;

	public static void main(String[] args) throws Exception {

		if (args.length == 0) {
			throw new IllegalArgumentException("No arguments provided.");
		} else {
			System.out.println(Arrays.asList(args));
		}

		switch (args[0]) {
		case "start":
			if (launcher != null) {
				throw new IllegalStateException("Failed to start. Launcher is not null.");
			}
			launcher = new Launcher();
			launcher.init(args);
			launcher.start();
			break;

		case "stop":
			if (launcher == null) {
				throw new IllegalStateException("Failed to stop. Launcher is null.");
			}
			try {
				launcher.stop();
			} catch (Exception e) {
				launcher.destroy();
				System.out.println("ERROR: Failed to stop launcher. Destroying.");
				e.printStackTrace();
			}
			break;

		case "run":
			launcher = new Launcher();
			launcher.init(args);
			launcher.consolePostInit();
			launcher.start();
			break;

		default:
			throw new UnsupportedOperationException("Unsupported operation: " + args[0]);
		}

	}

	private DefaultLauncherContext context;
	private LauncherProperties properties;
	private LauncherThread thread;
	private Object lock;
	private boolean run;

	private Class<?> appClass;
	private Object appInstance;

	private Launcher() {
	}

	public void init(String[] args) throws Exception {
		System.out.println("init");

		// Here open configuration files, create a trace file, create ServerSockets, Threads

		if (args.length < 2) {
			throw new IllegalArgumentException("APP_HOME argument must be provided.");
		}

		// 1. Handle command-line arguments.
		String appHome = args[1];

		// 2. Create the launcher context.
		this.context = DefaultLauncherContext.builder()
				.applicationHome(appHome)
				.build();

		// 3. Load system properties.
		properties = context.getPropertiesFactory().create(LauncherProperties.class);

		// 4. Create the Launcher control thread.
		lock = new Object();
		thread = new LauncherThread("Thread-Launcher-" + appHome);
		run = true;

	}

	private void consolePostInit() {
		System.out.println("consolePostInit");
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				try {
					Launcher.this.stop();
				} catch (Exception e) {
					Launcher.this.destroy();
					System.out.println("ERROR: Failed to stop from shutdown hook.");
					e.printStackTrace();
				}
			}
		});
	}

	public void start() throws Exception {
		// Start the Thread, accept incoming connections
		System.out.println("start");

		appClass = Class.forName(properties.getApplicationClass());
		appInstance = appClass.newInstance();
		appClass.getMethod("onStart").invoke(appInstance);

		synchronized (lock) {
			thread.start();
		}

	}

	public void stop() throws Exception {
		// Inform the Thread to terminate the run(), close the ServerSockets
		System.out.println("stop");

		if (appClass == null || appInstance == null) {
			throw new IllegalStateException(
					"appClass [" + appClass + "] or appInstance [" + appInstance + "] cannot null.");
		}
		if (!run) {
			throw new IllegalStateException("Thread not running");
		}

		stopThread();
		System.out.println("Thread stopped");

		appClass.getMethod("onStop").invoke(appInstance);
	}

	private void stopThread() {

		run = false;
		synchronized (lock) {
			lock.notify();
		}

		while (thread.isAlive()) {
			try {
				thread.join();
			} catch (InterruptedException e) {
				System.out.println("WARNING: Thread interrupt while waiting for join.");
				// Ignore.
			}
		}
	}

	public void destroy() {
		// Destroy any object created in init()
		System.out.println("destroy");

		appClass = null;
		appInstance = null;
		thread = null;
		lock = null;
		run = false;
		launcher = null;
	}

	private class LauncherThread extends Thread {

		public LauncherThread(String name) {
			super(name);
		}

		@Override
		public void run() {
			System.out.println("INFO: Starting application control thread: " + this.getName());
			while (run) {
				synchronized (lock) {
					try {
						lock.wait();
					} catch (InterruptedException e) {
						// Ignore.
						System.out.println("WARNING: Thread interrupted, continuing wait state.");
					}
				}
			}
		}

	}

}
