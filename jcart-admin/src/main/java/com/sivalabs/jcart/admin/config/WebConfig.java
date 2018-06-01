package com.sivalabs.jcart.admin.config;

import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.thymeleaf.extras.springsecurity4.dialect.SpringSecurityDialect;

import com.sivalabs.jcart.admin.security.PostAuthorizationFilter;

/**
 * @author Siva
 *
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

	@Value("${server.port:9443}")
	private int serverPort;

	private PostAuthorizationFilter postAuthorizationFilter;

	private MessageSource messageSource;

	/**
	 * Spring {@link Autowired}
	 * @param postAuthorizationFilter
	 * @param messageSource
	 */
	public WebConfig(PostAuthorizationFilter postAuthorizationFilter,
			MessageSource messageSource) {
		this.postAuthorizationFilter = postAuthorizationFilter;
		this.messageSource = messageSource;
	}

	@Bean
	public Validator jsrValidator() {
		LocalValidatorFactoryBean factory = new LocalValidatorFactoryBean();
		factory.setValidationMessageSource(messageSource);
		return factory;
	}

	@Bean
	public FilterRegistrationBean<PostAuthorizationFilter> postAuthorizationFilterRegistrationBean() {
		FilterRegistrationBean<PostAuthorizationFilter> registrationBean = new FilterRegistrationBean<>();
		registrationBean.setFilter(postAuthorizationFilter);
		registrationBean.setOrder(Integer.MAX_VALUE);
		return registrationBean;
	}

	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/login").setViewName("public/login");
		registry.addRedirectViewController("/", "/home");

	}

	@Bean
	public SpringSecurityDialect securityDialect() {
		return new SpringSecurityDialect();
	}

	@Bean
	public TomcatServletWebServerFactory servletContainer() {
		TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory() {
			@Override
			protected void postProcessContext(Context context) {
				SecurityConstraint securityConstraint = new SecurityConstraint();
				securityConstraint.setUserConstraint("CONFIDENTIAL");
				SecurityCollection collection = new SecurityCollection();
				collection.addPattern("/*");
				securityConstraint.addCollection(collection);
				context.addConstraint(securityConstraint);
			}
		};

		tomcat.addAdditionalTomcatConnectors(initiateHttpConnector());
		return tomcat;
	}

	private Connector initiateHttpConnector() {
		Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
		connector.setScheme("http");
		connector.setPort(9090);
		connector.setSecure(false);
		connector.setRedirectPort(serverPort);

		return connector;
	}

}
