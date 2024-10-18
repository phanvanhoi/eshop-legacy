package com.samsungsds.eshop;

import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Api(value = "Greeting Controller", tags = { "Greeting" })
@RestController
public class Swagger2SpringBoot {
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.samsungsds.eshop")) // 해당 경로 내에 있는 API들을 대상으로 한다.
                .paths(PathSelectors.ant("/api/**"))
                .build()
                .apiInfo(apiInfo()); // Add ApiInfo
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("E-SHOP API Document")
                .description("E-SHOP API에 대한 설명 페이지")
                .version("v1") // Set your API version
                .contact(new springfox.documentation.service.Contact("PV.hoi", "http://localhost",
                        "phanvanhoi.dtu@gmail.com")) // Optional: Contact information
                .license("")
                .licenseUrl("http://unlicense.org")
                .build();
    }

    @ApiOperation(value = "Get a greeting message")
    @GetMapping("/greet/{name}")
    public String greet(@ApiParam(value = "Name to greet", required = true) @PathVariable String name) {
        return "Hello, " + name + "!";
    }

}