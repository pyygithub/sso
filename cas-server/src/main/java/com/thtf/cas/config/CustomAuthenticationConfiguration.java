package com.thtf.cas.config;

import org.apereo.cas.authentication.AuthenticationEventExecutionPlan;
import org.apereo.cas.authentication.AuthenticationEventExecutionPlanConfigurer;
import org.apereo.cas.authentication.AuthenticationHandler;
import org.apereo.cas.authentication.principal.DefaultPrincipalFactory;
import org.apereo.cas.configuration.CasConfigurationProperties;
import org.apereo.cas.services.ServicesManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Configuration("CustomAuthenticationConfiguration")
@EnableConfigurationProperties({CasConfigurationProperties.class, DataBaseProperties.class})
public class CustomAuthenticationConfiguration implements AuthenticationEventExecutionPlanConfigurer {
	
	@Autowired
	private DataBaseProperties databaseProperties;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private CasConfigurationProperties casProperties;

	@Autowired
	@Qualifier("servicesManager")
	private ServicesManager servicesManager;

	@Bean 
	public AuthenticationHandler myAuthenticationHandler() { 
		return new CustomUsernamePasswordAuthentication(CustomUsernamePasswordAuthentication.class.getName(), servicesManager, new DefaultPrincipalFactory(), 1, jdbcTemplate); 
	}
	
	@Override
	public void configureAuthenticationExecutionPlan(AuthenticationEventExecutionPlan plan) {
		plan.registerAuthenticationHandler(myAuthenticationHandler());
	}
	
	@Bean
	public JdbcTemplate jdbcTemplate(){
		// JDBC模板依赖于连接池来获得数据的连接，所以必须先要构造连接池 
		DriverManagerDataSource dataSource = new DriverManagerDataSource(); 
		dataSource.setDriverClassName(databaseProperties.getDriverClass()); 
		dataSource.setUrl(databaseProperties.getUrl()); 
		dataSource.setUsername(databaseProperties.getUser()); 
		dataSource.setPassword(databaseProperties.getPassword()); 
		// 创建JDBC模板 
		JdbcTemplate jdbcTemplate = new JdbcTemplate(); 
		jdbcTemplate.setDataSource(dataSource); 
		return jdbcTemplate;
	}
}
