package com.universum.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordGenerator {
	public static void main(String[] args) {
		System.out.println(new BCryptPasswordEncoder().encode("admin"));
	}
}
