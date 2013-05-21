package com.socklabs.nemail.config;

import com.netflix.config.DynamicIntProperty;
import com.netflix.config.DynamicPropertyFactory;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.subethamail.smtp.server.SMTPServer;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

@Configuration
@EnableWebMvc
@ComponentScan("com.socklabs.nemail")
@Import({
				BeanConfig.class
		})
public class AppConfig {

	@Resource(name = "nemail-smtpServer")
	private SMTPServer smtpServer;

	@PostConstruct
	public void startSmtpServer() {
		final DynamicIntProperty smtpdPort = DynamicPropertyFactory.getInstance().getIntProperty("smtpd.port", 2500);
		smtpServer.setPort(smtpdPort.get());
		smtpServer.start();
	}

}
