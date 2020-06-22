package com.example.rpl.RPL.config;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

import com.example.rpl.RPL.service.EmailService;
import com.example.rpl.RPL.service.IEmailService;
import com.example.rpl.RPL.utils.MockEmailService;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paranamer.ParanamerModule;
import com.fasterxml.jackson.module.paranamer.ParanamerOnJacksonAnnotationIntrospector;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;
import org.thymeleaf.TemplateEngine;

@Configuration
public class BeanConfiguration {

    private static final String ISO_8601_24H_FULL_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";
    @Value("${client.apis.timeout}")
    private int defaultTimeout;

    @Value("${rpl.frontend.url}")
    private String frontEndUrl;

    @Profile({"producer", "prod"})
    @Bean
    public IEmailService emailService(JavaMailSender emailSender, TemplateEngine templateEngine) {
        return new EmailService(emailSender, templateEngine, frontEndUrl);
    }

    @Profile({"test-functional", "test-unit"})
    @Bean
    public IEmailService fakeEmailService() {
        return new MockEmailService();
    }


    @Bean
    public ObjectMapper objectMapper(Jackson2ObjectMapperBuilder builder) {
        ObjectMapper mapper = builder.createXmlMapper(false)
            .propertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE).build();
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        mapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        mapper.setSerializationInclusion(NON_NULL);
        mapper.registerModule(new JavaTimeModule());
        mapper.registerModule(new ParanamerModule());
        mapper.setAnnotationIntrospector(new ParanamerOnJacksonAnnotationIntrospector());
        mapper.setDateFormat(new SimpleDateFormat(ISO_8601_24H_FULL_FORMAT));
        return mapper;
    }

    @Bean
    @Primary
    public RestTemplate restTemplate(ObjectMapper objectMapper) {
        return getRestTemplate(objectMapper, defaultTimeout);
    }

    private RestTemplate getRestTemplate(ObjectMapper objectMapper, int timeout) {
        RequestConfig config = RequestConfig.custom()
            .setConnectTimeout(timeout)
            .setConnectionRequestTimeout(timeout)
            .setSocketTimeout(timeout)
            .build();
        CloseableHttpClient client = HttpClientBuilder.create()
            .setDefaultRequestConfig(config)
            .build();
        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory(
            client);

        RestTemplate restTemplate = new RestTemplate(clientHttpRequestFactory);
        restTemplate.getMessageConverters().stream()
            .filter(m -> m instanceof StringHttpMessageConverter)
            .findFirst()
            .ifPresent(messageConverter ->
                ((StringHttpMessageConverter) messageConverter).setDefaultCharset(
                    StandardCharsets.UTF_8));

        restTemplate.getMessageConverters().stream()
            .filter(m -> m instanceof MappingJackson2HttpMessageConverter)
            .findFirst()
            .ifPresent(messageConverter ->
                ((MappingJackson2HttpMessageConverter) messageConverter)
                    .setObjectMapper(objectMapper));

        restTemplate.setErrorHandler(new DefaultResponseErrorHandler() {
            @Override
            protected boolean hasError(HttpStatus statusCode) {
                return !statusCode.is2xxSuccessful();
            }
        });

        return restTemplate;
    }
}
