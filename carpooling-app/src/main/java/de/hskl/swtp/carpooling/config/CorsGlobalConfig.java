package de.hskl.swtp.carpooling.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsGlobalConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // apply to all paths
                        .allowedOrigins("*","null") // allow all origins
                        .allowedMethods("*") // allow all HTTP methods
                        .allowedHeaders("*") // allow all headers
                        .exposedHeaders("Authorization");
            }
        };
    }
}
