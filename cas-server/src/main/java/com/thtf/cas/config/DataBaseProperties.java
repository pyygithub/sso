package com.thtf.cas.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.Serializable;


@Component
@ConfigurationProperties(prefix = "sso.jdbc")
@Data
public class DataBaseProperties implements Serializable {

	private String user;
	
	private String password;
	
	private String driverClass;
	
	private String url;

}
