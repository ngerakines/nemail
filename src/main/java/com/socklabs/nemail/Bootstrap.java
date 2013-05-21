package com.socklabs.nemail;

import com.netflix.config.*;
import com.socklabs.nemail.config.AppConfig;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.SystemConfiguration;
import org.apache.commons.daemon.Daemon;
import org.apache.commons.daemon.DaemonContext;
import org.apache.commons.daemon.DaemonController;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.subethamail.smtp.server.SMTPServer;

public class Bootstrap implements Daemon {

	private ApplicationContext applicationContext;

	public static void main(String[] args) {
		final Daemon daemon = new Bootstrap();
		final DaemonContext daemonContext = new DaemonContext() {
			@Override
			public DaemonController getController() {
				return null;
			}

			@Override
			public String[] getArguments() {
				return new String[0];  //To change body of implemented methods use File | Settings | File Templates.
			}
		};
		try {
			daemon.init(daemonContext);
			daemon.start();
			while (true) {
				Thread.sleep(1000);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void init(DaemonContext daemonContext) throws Exception {
		setConfig();

		final AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
		context.register(AppConfig.class);
		context.refresh();

		this.applicationContext = context;
	}

	private void setConfig() throws ConfigurationException {
		//String fileName = "/config/config.properties";
		//final ConcurrentMapConfiguration configFromPropertiesFile = new ConcurrentMapConfiguration(new PropertiesConfiguration(fileName));
		final ConcurrentMapConfiguration configFromSystemProperties = new ConcurrentMapConfiguration(new SystemConfiguration());
		final ConcurrentCompositeConfiguration finalConfig = new ConcurrentCompositeConfiguration();
		finalConfig.addConfiguration(configFromSystemProperties, "systemConfig");
		//finalConfig.addConfiguration(configFromPropertiesFile, "fileConfig");

		ConfigurationManager.install(finalConfig);
	}

	@Override
	public void start() throws Exception {
		if (applicationContext == null) {
			throw new RuntimeException("Application context not set.");
		}

	}

	@Override
	public void stop() throws Exception {
		final SMTPServer smtpServer = applicationContext.getBean("nemail-smtpServer", SMTPServer.class);
		smtpServer.stop();
	}

	@Override
	public void destroy() {
	}

}
