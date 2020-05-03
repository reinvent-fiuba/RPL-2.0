package com.example.rpl.RPL.security;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider tokenProvider;

    private final CustomUserDetailsService customUserDetailsService;


    @Autowired
    public JwtAuthenticationFilter(JwtTokenProvider tokenProvider,
        CustomUserDetailsService customUserDetailsService) {
        super();
        this.tokenProvider = tokenProvider;
        this.customUserDetailsService = customUserDetailsService;
    }

    /**
     * Filters every request annotated with @CurrentUser (which annotates AuthenticationPrincipal).
     * Retrieves JWT from Header and assigns security context authentication.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwt = getJwtFromRequest(request);
            Long courseId = getCourseIdFromRequest(request);

            if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
                Long userId = tokenProvider.getUserIdFromJWT(jwt);

                /*
                    Note that you could also encode the user's username and roles inside JWT claims
                    and create the UserDetails object by parsing those claims from the JWT.
                    That would avoid the following database hit. It's completely up to you.
                 */
                UserDetails userDetails;
                if (courseId != null) {
                    userDetails = customUserDetailsService
                        .loadUserByIdAndCourseId(userId, courseId);
                } else {
                    userDetails = customUserDetailsService.loadUserById(userId);
                }
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities());
                authentication
                    .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception ex) {
            log.error("Could not set user authentication in security context", ex);
        }

        filterChain.doFilter(request, response);
    }

    /**
     * @return Returns the courseId if it is in the URI of the request
     */
    private Long getCourseIdFromRequest(HttpServletRequest request) {
        String url = request.getRequestURI();
        String courses = "courses";
        if (StringUtils.hasText(url) && url.contains(courses) && !url.endsWith(courses)) {
            String[] partsOfUri = url.substring(url.indexOf(courses)).split("/");
            return partsOfUri.length > 0 ? Long.parseLong(partsOfUri[1]) : null;
        }
        return null;
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
