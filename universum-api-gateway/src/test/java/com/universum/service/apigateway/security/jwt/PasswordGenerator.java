package com.universum.service.apigateway.security.jwt;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordGenerator {
	public static void main(String[] args) {
		BCryptPasswordEncoder passwordEnconder = new BCryptPasswordEncoder();
		System.out.println(passwordEnconder.encode("admin"));
	}
}
