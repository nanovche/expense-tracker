package com.pairlearning.expensetracker;

import com.pairlearning.expensetracker.filters.AuthSessionFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ExpenseTrackerApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExpenseTrackerApiApplication.class, args);
		
	}
	
/*	@Bean
	public FilterRegistrationBean<AuthFilter> filterRegistrationBean(){
		FilterRegistrationBean<AuthFilter> registrationBean = new FilterRegistrationBean<AuthFilter>();
		AuthFilter authFilter = new AuthFilter();
		registrationBean.setFilter(authFilter);
		registrationBean.addUrlPatterns("/api/categories/*");
		return registrationBean;
	}*/

	@Bean
	public FilterRegistrationBean<AuthSessionFilter> filterRegistrationBean(){
		FilterRegistrationBean<AuthSessionFilter> registrationBean = new FilterRegistrationBean<AuthSessionFilter>();
		AuthSessionFilter AuthSessionFilter = new AuthSessionFilter();
		registrationBean.setFilter(AuthSessionFilter);
		registrationBean.addUrlPatterns("/api/categories/*");
		return registrationBean;
	}

}
