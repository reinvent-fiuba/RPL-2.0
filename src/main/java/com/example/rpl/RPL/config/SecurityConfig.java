package com.example.rpl.RPL.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

import com.example.rpl.RPL.security.CustomUserDetailsService;
import com.example.rpl.RPL.security.JwtAuthenticationEntryPoint;
import com.example.rpl.RPL.security.JwtAuthenticationFilter;
import com.example.rpl.RPL.security.JwtTokenProvider;

import jakarta.servlet.http.HttpServletRequest;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true, prePostEnabled = true)
public class SecurityConfig {

        private JwtTokenProvider tokenProvider;
        private final CustomUserDetailsService customUserDetailsService;
        private final JwtAuthenticationEntryPoint unauthorizedHandler;
        @Value("${rpl.logging.requests}")
        private boolean logRequests;

        @Autowired
        public SecurityConfig(
                        CustomUserDetailsService customUserDetailsService,
                        JwtAuthenticationEntryPoint unauthorizedHandler,
                        JwtTokenProvider tokenProvider) {
                this.customUserDetailsService = customUserDetailsService;
                this.unauthorizedHandler = unauthorizedHandler;
                this.tokenProvider = tokenProvider;
        }

        @Bean
        public AuthenticationManager authenticationManagerBean(HttpSecurity http) throws Exception {
                AuthenticationManagerBuilder authenticationManagerBuilder = http
                                .getSharedObject(AuthenticationManagerBuilder.class);
                authenticationManagerBuilder.userDetailsService(customUserDetailsService)
                                .passwordEncoder(passwordEncoder());
                return authenticationManagerBuilder.build();
        }

        @Bean
        public JwtAuthenticationFilter jwtAuthenticationFilter() {
                return new JwtAuthenticationFilter(tokenProvider, customUserDetailsService);
        }

        // TODO: descomentar
        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
                http
                                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                                .csrf(csrf -> csrf.disable())
                                .exceptionHandling(exceptionHandling -> exceptionHandling
                                                .authenticationEntryPoint(unauthorizedHandler))
                                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                                .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.sameOrigin()))
                                .authorizeHttpRequests(
                                                authorizeRequests -> authorizeRequests
                                                                .requestMatchers( // TODO: sacar esto
                                                                                AntPathRequestMatcher.antMatcher("/**"))
                                                                .permitAll()
                                // .requestMatchers(AntPathRequestMatcher.antMatcher("/"),
                                // AntPathRequestMatcher.antMatcher("/favicon.ico"),
                                // AntPathRequestMatcher.antMatcher("/**/*.png"),
                                // AntPathRequestMatcher.antMatcher("/**/*.gif"),
                                // AntPathRequestMatcher.antMatcher("/**/*.svg"),
                                // AntPathRequestMatcher.antMatcher("/**/*.jpg"),
                                // AntPathRequestMatcher.antMatcher("/**/*.html"),
                                // AntPathRequestMatcher.antMatcher("/**/*.css"),
                                // AntPathRequestMatcher.antMatcher("/**/*.js"))
                                // .permitAll()
                                // .requestMatchers(AntPathRequestMatcher.antMatcher("/api/auth/**"))
                                // .permitAll()
                                // .requestMatchers(AntPathRequestMatcher.antMatcher("/h2-console/**"),
                                // AntPathRequestMatcher.antMatcher("/v2/api-docs"),
                                // AntPathRequestMatcher.antMatcher("/api/health"),
                                // AntPathRequestMatcher.antMatcher("/ping"))
                                // .permitAll()
                                // .requestMatchers(
                                // AntPathRequestMatcher.antMatcher(HttpMethod.GET,
                                // "/api/polls/**"),
                                // AntPathRequestMatcher.antMatcher(HttpMethod.GET,
                                // "/api/users/**"))
                                // .permitAll()
                                // .requestMatchers(
                                // AntPathRequestMatcher.antMatcher(HttpMethod.GET,
                                // "/api/files/**"),
                                // AntPathRequestMatcher
                                // .antMatcher(HttpMethod.GET,
                                // "/api/getExtractedFile/**"),
                                // AntPathRequestMatcher.antMatcher(HttpMethod.GET,
                                // "/api/getFileForStudent/**"),
                                // AntPathRequestMatcher.antMatcher(HttpMethod.GET,
                                // "/api/getExtractedFiles/**"),
                                // AntPathRequestMatcher.antMatcher(HttpMethod.GET,
                                // "/api/submissions/**"))
                                // .permitAll()
                                // .requestMatchers(
                                // AntPathRequestMatcher.antMatcher(HttpMethod.POST,
                                // "/api/submissions/**"))
                                // .permitAll()
                                // .requestMatchers(
                                // AntPathRequestMatcher.antMatcher(HttpMethod.PUT,
                                // "/api/submissions/**"))
                                // .permitAll()
                                // .requestMatchers(
                                // AntPathRequestMatcher.antMatcher(
                                // HttpMethod.POST,
                                // "/api/uploadMultipleFiles/**"))
                                // .permitAll()
                                // .anyRequest()
                                // .authenticated()
                                );

                // Add our custom JWT security filter
                // http.addFilterBefore(jwtAuthenticationFilter(),
                // UsernamePasswordAuthenticationFilter.class);
                return http.build();
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
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
                // Don't log the polled endpoints (/health || /api/submissions/123/result)
                CommonsRequestLoggingFilter filter = new CommonsRequestLoggingFilter() {
                        // Don't log passwords
                        @Override
                        protected boolean shouldLog(HttpServletRequest request) {
                                return !request.getRequestURI().contains("login") && !request.getRequestURI()
                                                .contains("Password") && !request.getRequestURI().contains("signup")
                                // && !request.getRequestURI().contains("health") TODO: descomentar
                                                &&
                                                !(request.getRequestURI()
                                                                .matches("/api/submissions/.*/result")
                                                                && request.getMethod().equals("GET"))
                                                && logRequests;
                        }
                };

                filter.setIncludeQueryString(true);
                filter.setIncludePayload(true);

                filter.setMaxPayloadLength(10000);
                filter.setIncludeHeaders(false);
                filter.setAfterMessagePrefix("REQUEST DATA : ");
                return filter;
        }

}
