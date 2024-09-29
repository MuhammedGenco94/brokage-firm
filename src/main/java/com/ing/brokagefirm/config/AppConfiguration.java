package com.ing.brokagefirm.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "user.config")
@Data
public class AppConfiguration {

    private String adminPassword;

}
