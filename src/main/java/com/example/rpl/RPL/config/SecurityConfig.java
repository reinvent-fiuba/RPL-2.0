package com.example.rpl.RPL.config;

import com.example.rpl.RPL.security.CustomUserDetailsService;
import com.example.rpl.RPL.security.JwtAuthenticationEntryPoint;
import com.example.rpl.RPL.security.JwtAuthenticationFilter;
import com.example.rpl.RPL.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
    securedEnabled = true,
    jsr250Enabled = true,
    prePostEnabled = true
)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomUserDetailsService customUserDetailsService;

    private final JwtAuthenticationEntryPoint unauthorizedHandler;

    private JwtTokenProvider tokenProvider;


    @Autowired
    public SecurityConfig(
        CustomUserDetailsService customUserDetailsService,
        JwtAuthenticationEntryPoint unauthorizedHandler,
        JwtTokenProvider tokenProvider) {
        super();
        this.customUserDetailsService = customUserDetailsService;
        this.unauthorizedHandler = unauthorizedHandler;
        this.tokenProvider = tokenProvider;
    }

    public SecurityConfig(boolean disableDefaults,
        CustomUserDetailsService customUserDetailsService,
        JwtAuthenticationEntryPoint unauthorizedHandler) {
        super(disableDefaults);
        this.customUserDetailsService = customUserDetailsService;
        this.unauthorizedHandler = unauthorizedHandler;
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(tokenProvider, customUserDetailsService);
    }

    @Override
    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder)
        throws Exception {
        authenticationManagerBuilder
            .userDetailsService(customUserDetailsService)
            .passwordEncoder(passwordEncoder());
    }

    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .cors()
            .and()
            .csrf()
            .disable()
            .exceptionHandling()
            .authenticationEntryPoint(unauthorizedHandler)
            .and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .headers()
            .frameOptions().sameOrigin()
            .and()
            .authorizeRequests()
            .antMatchers("/",
                "/favicon.ico",
                "/**/*.png",
                "/**/*.gif",
                "/**/*.svg",
                "/**/*.jpg",
                "/**/*.html",
                "/**/*.css",
                "/**/*.js")
            .permitAll()
            .antMatchers("/api/auth/**")
            .permitAll()
            .antMatchers("/h2-console/**", "/v2/api-docs", "/api/health", "/ping")
            .permitAll()
            .antMatchers(HttpMethod.GET, "/api/polls/**", "/api/users/**")
            .permitAll()
            .antMatchers(HttpMethod.GET,
                "/api/files/**",
                "/api/getExtractedFile/**",
                "/api/getExtractedFiles/**",
                "/api/submissions/**"
            )
            .permitAll()
            .antMatchers(HttpMethod.POST,
                "/api/submissions/**"
            )
            .permitAll()
            .antMatchers(HttpMethod.PUT,
                "/api/submissions/**"
            )
            .permitAll()
            .antMatchers(HttpMethod.POST,
                "/api/uploadMultipleFiles/**"
            )
            .permitAll()
            .anyRequest()
            .authenticated();

        // Add our custom JWT security filter
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration cc = new CorsConfiguration().applyPermitDefaultValues();
        cc.addAllowedMethod(HttpMethod.DELETE);
        cc.addAllowedMethod(HttpMethod.PUT);
        cc.addAllowedMethod(HttpMethod.PATCH);
        source.registerCorsConfiguration("/**", cc);
        return source;
    }

    @Bean
    public CommonsRequestLoggingFilter logFilter() {
        CommonsRequestLoggingFilter filter
            = new CommonsRequestLoggingFilter();
        filter.setIncludeQueryString(true);
        filter.setIncludePayload(true);
        filter.setMaxPayloadLength(10000);
        filter.setIncludeHeaders(false);
        filter.setAfterMessagePrefix("REQUEST DATA : ");
        return filter;
    }
}