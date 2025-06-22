package gcp25.configurations;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import static com.gcp.agent25.common.core.constant.CommonConstant.BASE_PACKAGE;

@Configuration
@ComponentScan(basePackages = {BASE_PACKAGE, "gcp25.utils"})
public class ApplicationConfiguration {
}