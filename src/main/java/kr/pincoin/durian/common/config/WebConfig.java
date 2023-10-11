package kr.pincoin.durian.common.config;

import kr.pincoin.durian.shop.domain.conveter.*;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addFormatters(FormatterRegistry registry) {
        // Using converters with @RequestParam
        registry.addConverter(new ProductStatusRequestConverter());
        registry.addConverter(new ProductStockStatusRequestConverter());
        registry.addConverter(new VoucherStatusRequestConverter());
        registry.addConverter(new PaymentMethodRequestConverter());
        registry.addConverter(new OrderStatusRequestConverter());
    }
}
