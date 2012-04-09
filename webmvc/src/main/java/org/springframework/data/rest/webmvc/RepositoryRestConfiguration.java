package org.springframework.data.rest.webmvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.data.rest.repository.JpaRepositoryMetadata;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.orm.jpa.support.PersistenceAnnotationBeanPostProcessor;

/**
 * @author Jon Brisbin <jon@jbrisbin.com>
 */
@Configuration
@ImportResource("classpath*:META-INF/spring-data-rest/**/*-export.xml")
public class RepositoryRestConfiguration {

  @Autowired
  EntityManagerFactory entityManagerFactory;
  @Autowired(required = false)
  JpaRepositoryMetadata jpaRepositoryMetadata;
  @Autowired(required = false)
  ConversionService customConversionService;
  ConversionService defaultConversionService = new DefaultConversionService();
  @Autowired(required = false)
  List<HttpMessageConverter<?>> httpMessageConverters = new ArrayList<HttpMessageConverter<?>>();

  @Bean ConversionService conversionService() {
    if (null != customConversionService) {
      return customConversionService;
    } else {
      return defaultConversionService;
    }
  }

  @Bean List<HttpMessageConverter<?>> httpMessageConverters() {
    if (httpMessageConverters.isEmpty()) {
      MappingJacksonHttpMessageConverter json = new MappingJacksonHttpMessageConverter();
      json.setSupportedMediaTypes(
          Arrays.asList(MediaType.APPLICATION_JSON, MediaType.valueOf("application/x-spring-data+json"))
      );
      httpMessageConverters.add(json);
    }
    return httpMessageConverters;
  }

  @Bean JpaRepositoryMetadata jpaRepositoryMetadata() throws Exception {
    if (null == jpaRepositoryMetadata) {
      jpaRepositoryMetadata = new JpaRepositoryMetadata();
    }
    return jpaRepositoryMetadata;
  }

  @Bean PersistenceAnnotationBeanPostProcessor persistenceAnnotationBeanPostProcessor() {
    return new PersistenceAnnotationBeanPostProcessor();
  }

}
