package com.universum.service.apigateway;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.universum.common.util.AppProfiles;
import com.universum.service.apigateway.filter.pre.JWTTokenRelayFilter;

@EnableDiscoveryClient
@SpringBootApplication
@EnableFeignClients
@EnableZuulProxy
@ComponentScan(basePackages = {"com.universum.common", "com.universum.service.apigateway"})
public class APIGatewayApplication {
	private static final Logger log = LoggerFactory.getLogger(APIGatewayApplication.class);
	private static final String SPRING_PROFILE_DEFAULT = "spring.profiles.default";
	
	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(APIGatewayApplication.class);
		addDefaultProfile(app);
		Environment env = app.run(args).getEnvironment();
		logApplicationStartup(env);
    }
	
	/**
	 * Log application statistics on startup
	 * 
	 * @param env - Interface representing the environment in which the current
	 *            application is running.
	 */
	private static void logApplicationStartup(final Environment env) {
		String protocol = getServerProtocol(env);
		String contextPath = getServerContextPath(env);
		String serverPort = env.getProperty("server.port");
		String hostAddress = "localhost";
		try {
			hostAddress = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			log.warn("The host name could not be determined, using `localhost` as fallback");
		}
		log.info("\n----------------------------------------------------------\n" + "\t Application '{}' is running!\n"
				+ "\t Access URLs:\n" + "\t\t Local: \t{}://localhost:{}{}\n" + "\t\t External: \t{}://{}:{}{}\n"
				+ "----------------------------------------------------------",
				env.getProperty("spring.application.name"), protocol, serverPort, contextPath, protocol, hostAddress,
				serverPort, contextPath);
	}

	private static String getServerProtocol(final Environment env) {
		String protocol = "http";
		if (env.getProperty("server.ssl.key-store") != null) {
			protocol = "https";
		}
		return protocol;
	}

	private static String getServerContextPath(final Environment env) {
		String contextPath = env.getProperty("server.servlet.context-path");
		if (StringUtils.isBlank(contextPath)) {
			contextPath = "/";
		}
		return contextPath;
	}

	/**
	 * The default profile to use when no other profiles are defined.
	 * 
	 * @param app
	 */
	public static void addDefaultProfile(SpringApplication app) {
		Map<String, Object> defProperties = new HashMap<>();
		defProperties.put(SPRING_PROFILE_DEFAULT, AppProfiles.SPRING_PROFILE_DEVELOPMENT.getName());
		app.setDefaultProperties(defProperties);
	}
	
	@Bean
	public JWTTokenRelayFilter authorizationFilter() {
        return new JWTTokenRelayFilter();
    }
	
	@Bean
	public CorsFilter corsFilter() {
	    final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	    final CorsConfiguration config = new CorsConfiguration();
	    config.setAllowCredentials(true);
	    config.addAllowedOrigin("*");
	    config.addAllowedHeader("*");
	    config.addAllowedMethod("OPTIONS");
	    config.addAllowedMethod("HEAD");
	    config.addAllowedMethod("GET");
	    config.addAllowedMethod("PUT");
	    config.addAllowedMethod("POST");
	    config.addAllowedMethod("DELETE");
	    config.addAllowedMethod("PATCH");
	    source.registerCorsConfiguration("/**", config);
	    return new CorsFilter(source);
	}
}
