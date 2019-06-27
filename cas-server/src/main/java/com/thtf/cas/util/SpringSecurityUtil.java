package com.thtf.cas.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


public class SpringSecurityUtil {

	/**
	 * 加密
	 * @param password
	 * @return
	 */
	public static String encoderPassword(String password){
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		return encoder.encode(password);
	}


	/**
	 * 校验
	 * @param password
	 * @param secret
	 * @return
	 */
	public static boolean checkpassword(String password, String secret){
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		return encoder.matches(password, secret);
	}
	
	public static void main(String[] args) {
		System.out.println(encoderPassword("123456"));
	}
	
}
