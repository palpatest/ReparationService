package org.reparationservice.rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration.WebMvcAutoConfigurationAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;

import reparationservice.entities.worker.WorkerGateway;
import reparationservice.persistenceimpls.inmemory.InMemoryConfigurator;

@Configuration
@ComponentScan
@EnableAutoConfiguration
public class Application {
  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

  @Bean
  WorkerGateway getWorkerGW() {
    return InMemoryConfigurator.getWorkerGateway();
  }
  
  @Configuration
  public static class DisableStaticResourceConfiguration extends WebMvcAutoConfigurationAdapter {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {}
  }
}
