package com.rishab99058.linkedIn.user_service.config;

import com.cloudinary.Cloudinary;
import org.modelmapper.Condition;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.spi.MappingContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;

@Configuration
public class AppConfig {

    @Value("${cloudinary.url}")
    private String cloudinaryUrl;

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT);
        Condition<?, ?> notNull = new Condition<Object, Object>() {
            @Override
            public boolean applies(MappingContext<Object, Object> context) {
                return context.getSource() != null;
            }
        };
        modelMapper.getConfiguration().setPropertyCondition(notNull);

        return modelMapper;
    }

    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(cloudinaryUrl);
    }


}
