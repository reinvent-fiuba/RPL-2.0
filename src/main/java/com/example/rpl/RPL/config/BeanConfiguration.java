package com.example.rpl.RPL.config;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

import com.example.rpl.RPL.service.EmailService;
import com.example.rpl.RPL.service.IEmailService;
import com.example.rpl.RPL.utils.MockEmailService;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paranamer.ParanamerModule;
import com.fasterxml.jackson.module.paranamer.ParanamerOnJacksonAnnotationIntrospector;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import org.apache.hc.client5.http.config.ConnectionConfig;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.core5.util.Timeout;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.lang.NonNull;
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

    @Profile({ "prod", "heroku", "development" })
    @Bean
    public IEmailService emailService(JavaMailSender emailSender, TemplateEngine templateEngine) {
        return new EmailService(emailSender, templateEngine, frontEndUrl);
    }

    @Profile({ "test-functional", "test-unit" })
    @Bean
    public IEmailService fakeEmailService() {
        return new MockEmailService();
    }

    @Bean
    public ObjectMapper objectMapper(Jackson2ObjectMapperBuilder builder) {
        ObjectMapper mapper = builder.createXmlMapper(false)
                .propertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE).build();
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        mapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
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
                .setConnectionRequestTimeout(Timeout.ofSeconds(timeout))
                .build();
        ConnectionConfig connectionConfig = ConnectionConfig.custom()
                .setConnectTimeout(Timeout.ofSeconds(timeout))
                .setSocketTimeout(Timeout.ofSeconds(timeout))
                .build();

        PoolingHttpClientConnectionManager connManager = PoolingHttpClientConnectionManagerBuilder.create()
                .setDefaultConnectionConfig(connectionConfig).build();

        CloseableHttpClient client = HttpClientBuilder.create()
                .setDefaultRequestConfig(config)
                .setConnectionManager(connManager)
                .build();

        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory(
                client);

        RestTemplate restTemplate = new RestTemplate(clientHttpRequestFactory);
        restTemplate.getMessageConverters().stream()
                .filter(StringHttpMessageConverter.class::isInstance)
                .findFirst()
                .ifPresent(messageConverter -> ((StringHttpMessageConverter) messageConverter).setDefaultCharset(
                        StandardCharsets.UTF_8));

        restTemplate.getMessageConverters().stream()
                .filter(MappingJackson2HttpMessageConverter.class::isInstance)
                .findFirst()
                .ifPresent(messageConverter -> ((MappingJackson2HttpMessageConverter) messageConverter)
                        .setObjectMapper(objectMapper));

        restTemplate.setErrorHandler(new DefaultResponseErrorHandler() {
            @Override
            protected boolean hasError(@NonNull HttpStatusCode statusCode) {
                return !statusCode.is2xxSuccessful();
            }
        });

        return restTemplate;
    }
}
