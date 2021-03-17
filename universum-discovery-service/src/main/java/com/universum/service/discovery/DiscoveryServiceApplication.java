package com.universum.service.discovery;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.core.env.Environment;

import com.universum.common.util.AppProfiles;

import de.codecentric.boot.admin.server.config.EnableAdminServer;


@SpringBootApplication
@EnableEurekaServer
@EnableAdminServer
public class DiscoveryServiceApplication {
	private static final Logger log = LoggerFactory.getLogger(DiscoveryServiceApplication.class);
	private static final String SPRING_PROFILE_DEFAULT = "spring.profiles.default";
	
	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(DiscoveryServiceApplication.class);
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
}
