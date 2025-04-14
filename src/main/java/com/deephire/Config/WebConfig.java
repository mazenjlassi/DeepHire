package com.deephire.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;

import java.util.Arrays;

@Configuration // Marks this class as a configuration class for Spring Boot
public class WebConfig {

    /**
     * This bean registers a CORS (Cross-Origin Resource Sharing) filter that allows
     * the Angular frontend (running on http://localhost:4200) to communicate with the Spring Boot backend.
     *
     * CORS is necessary because browsers block requests between different origins (domains/ports)
     * unless explicitly allowed.
     */
    @Bean
    public FilterRegistrationBean<CorsFilter> corsFilter() {
        CorsConfiguration config = new CorsConfiguration();

        // Allow credentials such as cookies or Authorization headers (e.g. JWT)
        config.setAllowCredentials(true);

        // Allow requests only from the Angular app on localhost:4200
        config.setAllowedOrigins(Arrays.asList("http://localhost:4200"));

        // Allow specific headers in the request
        config.setAllowedHeaders(Arrays.asList(
                HttpHeaders.AUTHORIZATION, // For sending tokens or credentials
                HttpHeaders.CONTENT_TYPE,  // To allow JSON and form data
                HttpHeaders.ACCEPT         // To specify accepted response formats
        ));

        // Allow HTTP methods used by the frontend
        config.setAllowedMethods(Arrays.asList(
                HttpMethod.GET.name(),
                HttpMethod.POST.name(),
                HttpMethod.PUT.name(),
                HttpMethod.DELETE.name()
        ));

        // Set how long the CORS response can be cached by the browser (in seconds)
        config.setMaxAge(3600L); // 1 hour

        // Register the configuration for all endpoints (/**)
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        // Register the CORS filter and give it high priority
        FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>(new CorsFilter(source));
        bean.setOrder(-102); // Lower numbers mean higher priority
        return bean;
    }
}
