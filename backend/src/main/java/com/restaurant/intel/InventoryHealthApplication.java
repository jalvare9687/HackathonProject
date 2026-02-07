package com.restaurant.intel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class InventoryHealthApplication {
  public static void main(String[] args) {
    SpringApplication.run(InventoryHealthApplication.class, args);
  }
}
