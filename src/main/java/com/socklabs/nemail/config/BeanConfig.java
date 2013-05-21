package com.socklabs.nemail.config;

import com.google.common.eventbus.EventBus;
import com.socklabs.nemail.DefaultSimpleMessageListener;
import com.socklabs.nemail.EmailDao;
import com.socklabs.nemail.LuceneEmailDao;
import com.socklabs.nemail.SimpleEmailDao;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.BeanNameViewResolver;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;
import org.subethamail.smtp.helper.SimpleMessageListener;
import org.subethamail.smtp.helper.SimpleMessageListenerAdapter;
import org.subethamail.smtp.server.SMTPServer;

@Configuration
public class BeanConfig extends WebMvcConfigurerAdapter {

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		super.addResourceHandlers(registry);

		registry.addResourceHandler("/static/**").addResourceLocations("/static/**");
	}

	@Bean
	public FreeMarkerConfigurer freeMarkerConfigurer() {
		final FreeMarkerConfigurer freeMarkerConfigurer = new FreeMarkerConfigurer();
		freeMarkerConfigurer.setTemplateLoaderPath("/WEB-INF/ftl/");
		return freeMarkerConfigurer;
	}

	@Bean
	public FreeMarkerViewResolver freeMarkerViewResolver() {
		final FreeMarkerViewResolver freeMarkerViewResolver = new FreeMarkerViewResolver();
		freeMarkerViewResolver.setCache(false);
		freeMarkerViewResolver.setPrefix("");
		freeMarkerViewResolver.setSuffix(".ftl");
		return freeMarkerViewResolver;
	}

	@Bean
	public BeanNameViewResolver getBeanNameViewResolver() {
		BeanNameViewResolver vr = new BeanNameViewResolver();
		vr.setOrder(-1);
		return vr;
	}

	@Bean(name = "RandomReviews-eventBus")
	public EventBus eventBus() {
		return new EventBus();
	}

	/* @Bean(name = "nemail-simpleEmailDao")
	public EmailDao simpleEmailDao() {
		return new SimpleEmailDao();
	} */

	@Bean(name = "nemail-luceneEmailDao")
	public EmailDao luceneEmailDao() {
		return new LuceneEmailDao();
	}

	@Bean(name = "nemail-smtpServer")
	public SMTPServer smtpServer() {
		return new SMTPServer(simpleMessageListenerAdapter());
	}

	@Bean(name = "nemail-simpleMessageListenerAdapter")
	public SimpleMessageListenerAdapter simpleMessageListenerAdapter() {
		return new SimpleMessageListenerAdapter(simpleMessageListener());
	}

	@Bean(name = "nemail-simpleMessageListener")
	public SimpleMessageListener simpleMessageListener() {
		return new DefaultSimpleMessageListener(luceneEmailDao());
	}

}
