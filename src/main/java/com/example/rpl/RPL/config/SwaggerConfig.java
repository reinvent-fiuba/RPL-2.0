// package com.example.rpl.RPL.config;

// import static com.google.common.collect.Lists.newArrayList;

// import java.util.Collections;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.web.bind.annotation.RequestMethod;
// import springfox.documentation.builders.PathSelectors;
// import springfox.documentation.builders.RequestHandlerSelectors;
// import springfox.documentation.builders.ResponseMessageBuilder;
// import springfox.documentation.schema.ModelRef;
// import springfox.documentation.service.ApiInfo;
// import springfox.documentation.service.Contact;
// import springfox.documentation.spi.DocumentationType;
// import springfox.documentation.spring.web.plugins.Docket;
// import springfox.documentation.swagger2.annotations.EnableSwagger2;

// @Configuration
// @EnableSwagger2
// public class SwaggerConfig {

//     @Bean
//     public Docket api() {
//         return new Docket(DocumentationType.SWAGGER_2).select()
//             .apis(RequestHandlerSelectors.basePackage("com.example.rpl.RPL.controller"))
//             .paths(PathSelectors.any())
//             .build()
//             .apiInfo(apiInfo())
//             .useDefaultResponseMessages(false)
//             .globalResponseMessage(RequestMethod.GET,
//                 newArrayList(new ResponseMessageBuilder().code(500)
//                         .message("500 message")
//                         .responseModel(new ModelRef("Error"))
//                         .build(),
//                     new ResponseMessageBuilder().code(403)
//                         .message("Forbidden!!!!!")
//                         .build()));
//     }

//     private ApiInfo apiInfo() {
//         return new ApiInfo("RPL 2.0", "Esta es la API de RPL 2.0 para todos y todas", "0.1",
//             "Terms of service",
//             new Contact("Alejandro Levinas o Matías Cano", "http://example.com/support",
//                 "levinasale@gmail.com;matiasjosecc@gmail.com"), "License of API",
//             "http://example.com/support", Collections.emptyList());
//     }
// }
