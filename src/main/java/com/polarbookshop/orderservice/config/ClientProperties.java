package com.polarbookshop.orderservice.config;


import jakarta.validation.constraints.NotNull;
import java.net.URI;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "polar")
public record ClientProperties(
       @NotNull
       URI catalogServiceUri
) {

}
