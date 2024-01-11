package kr.oyez.email.config;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class EmailConfig {
	
	@Value("${mail.smtp.protocol}")
	private String protocol;

	@Value("${mail.smtp.host}")
	private String host;
	
	@Value("${mail.smtp.port}")
	private int port;
	
	@Value("${mail.smtp.socketFactory.port}")
	private int socketPort;
	
	@Value("${mail.smtp.auth}")
	private boolean auth;
	
	@Value("${mail.smtp.starttls.enable}")
	private boolean starttls;
	
	@Value("${mail.smtp.starttls.required}")
	private boolean starttls_required;
	
	@Value("${mail.smtp.socketFactory.fallback}")
	private boolean fallback;
	
	@Value("${mail.smtp.default-encoding}")
	private String encoding;
	
	@Value("${mail.smtp.username}")
	private String id;
	
	@Value("${mail.smtp.password}")
	private String password;
	
	@Bean
	protected JavaMailSender javaMailSender() {
		JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
		javaMailSender.setProtocol(protocol);
		javaMailSender.setHost(host);
		javaMailSender.setUsername(id);
		javaMailSender.setPassword(password);
		javaMailSender.setPort(port);
		javaMailSender.setJavaMailProperties(getMailProperties());
		javaMailSender.setDefaultEncoding(encoding);
		
		return javaMailSender;
	}
	
	private Properties getMailProperties() {
		Properties pt = new Properties();
		pt.put("mail.smtp.socketFactory.port", socketPort);
		pt.put("mail.smtp.auth", auth);
		pt.put("mail.smtp.starttls.enable", starttls);
		pt.put("mail.smtp.starttls.required", starttls_required);
		pt.put("mail.smtp.socketFactory.fallback", fallback);
		pt.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		
		return pt;
	}
}
