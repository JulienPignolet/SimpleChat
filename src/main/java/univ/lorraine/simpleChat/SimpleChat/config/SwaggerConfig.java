package univ.lorraine.simpleChat.SimpleChat.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import com.google.common.collect.Lists;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger.web.SecurityConfigurationBuilder;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import static springfox.documentation.builders.PathSelectors.regex;
import springfox.documentation.swagger.web.ApiKeyVehicle;

import java.util.List;

@Configuration
@EnableSwagger2
public class SwaggerConfig extends WebMvcConfigurationSupport{
	@Bean
    public Docket SimpleChatApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("univ.lorraine.simpleChat.SimpleChat.controller"))
                .paths(regex("/api.*"))
                .build()
                .securityContexts(Lists.newArrayList(securityContext()))
    			.securitySchemes(Lists.newArrayList(apiKey()))
                .apiInfo(metaData());
             
    }
	
	 private SecurityContext securityContext() {
	    return SecurityContext.builder()
	        .securityReferences(defaultAuth())
	        .forPaths(regex("/api.*"))
	        .build();
	 }
	 
	 List<SecurityReference> defaultAuth() {
		    AuthorizationScope authorizationScope
		        = new AuthorizationScope("global", "accessEverything");
		    AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
		    authorizationScopes[0] = authorizationScope;
		    return Lists.newArrayList(
		        new SecurityReference("AUTHORIZATION", authorizationScopes));
		  }

	private ApiKey apiKey() {
    	return new ApiKey("AUTHORIZATION", "user_key", "header");
    }
	
	
	@Bean
	  SecurityConfiguration security() {
	    return SecurityConfigurationBuilder.builder()
	        .clientId(null)
	        .clientSecret(null)
	        .realm(null)
	        .appName("simple chat")
	        .scopeSeparator(",")
	        .additionalQueryStringParams(null)
	        .useBasicAuthenticationWithAccessCodeGrant(false)
	        .build();
	  }
    
    private ApiInfo metaData() {
        return new ApiInfoBuilder()
                .title("Spring Boot REST API - Simple chat")
                .description("\"Spring Boot REST API - Simple chat\"")
                .version("1.0.0")
                .license("Apache License Version 2.0")
                .licenseUrl("https://www.apache.org/licenses")
                .build();
    }

    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }
  
}
