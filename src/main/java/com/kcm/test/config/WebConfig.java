package com.kcm.test.config;

import com.kcm.test.converter.UserPasswordRequestToCredentialRepresentationConverter;
import com.kcm.test.converter.UserRequestToUserRepresentationConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionService;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

  @Override
  public void addFormatters(FormatterRegistry registry) {
    registry.addConverter(new UserPasswordRequestToCredentialRepresentationConverter());
    registry.addConverter(
        new UserRequestToUserRepresentationConverter((ConversionService) registry));
  }
}
