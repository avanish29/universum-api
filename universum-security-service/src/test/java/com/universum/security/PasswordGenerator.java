package com.universum.security;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class PasswordGenerator {
	public static void main(String[] args) {
		//System.out.println(new BCryptPasswordEncoder().encode("admin"));
		Locale arabicLang = new Locale("en", "GB");
		System.out.println("ISO Country  ==> " + arabicLang.getISO3Country());
		System.out.println("ISO Language ==> " + arabicLang.getISO3Language());
		System.out.println("Country ==> " + arabicLang.getCountry());
		System.out.println("Display Country ==> " + arabicLang.getDisplayCountry());
		System.out.println("Display Language ==> " + arabicLang.getDisplayLanguage());
		System.out.println("Display Name ==> " + arabicLang.getDisplayName());
		
		//returns array of all locales
        Locale locales[] = SimpleDateFormat.getAvailableLocales();
		
		//iterate through each locale and print locale code, display name and country
        for (int i = 0; i < locales.length; i++) {
            System.out.printf("%10s - %s, %s \n" , locales[i].toString(), locales[i].getDisplayName(), locales[i].getDisplayCountry());
		}
	}
}
