package com.tirsportif.backend.configuration;

import com.tirsportif.backend.configuration.converter.OffsetDateTimeConverter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.support.DefaultConversionService;

@Configuration
public class ConversionConfiguration implements InitializingBean {

    @Override
    public void afterPropertiesSet() {
        DefaultConversionService conversionService = (DefaultConversionService) DefaultConversionService.getSharedInstance();
        conversionService.addConverter(new OffsetDateTimeConverter());
    }

}
