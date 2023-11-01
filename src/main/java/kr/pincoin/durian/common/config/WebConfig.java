package kr.pincoin.durian.common.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import kr.pincoin.durian.auth.domain.converter.ProfileDomesticRequestConverter;
import kr.pincoin.durian.auth.domain.converter.ProfileGenderRequestConverter;
import kr.pincoin.durian.auth.domain.converter.VerificationStatusRequestConverter;
import kr.pincoin.durian.shop.domain.conveter.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.IOException;
import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addFormatters(FormatterRegistry registry) {
        // Using converters with @RequestParam

        // auth converters
        registry.addConverter(new VerificationStatusRequestConverter());
        registry.addConverter(new ProfileDomesticRequestConverter());
        registry.addConverter(new ProfileGenderRequestConverter());

        // shop converters
        registry.addConverter(new ProductStatusRequestConverter());
        registry.addConverter(new ProductStockStatusRequestConverter());
        registry.addConverter(new VoucherStatusRequestConverter());
        registry.addConverter(new PaymentMethodRequestConverter());
        registry.addConverter(new OrderStatusRequestConverter());
        registry.addConverter(new PaymentAccountRequestConverter());
        registry.addConverter(new CategoryStatusRequestConverter());
    }

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(mappingJackson2HttpMessageConverter());
    }

    @Bean
    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
        SimpleModule simpleModule = new SimpleModule()
                .addDeserializer(String.class, new StdScalarDeserializer<>(String.class) {
                    @Override
                    public String deserialize(JsonParser p, DeserializationContext ctx) throws IOException {
                        return p.getText() != null ? p.getText().trim() : null;
                    }
                });

        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setObjectMapper(new ObjectMapper().registerModule(simpleModule)
                                          .registerModule(new JavaTimeModule())); // Java 8 LocalDateTime support
        return converter;
    }
}
