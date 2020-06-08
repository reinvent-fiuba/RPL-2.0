package com.example.rpl.RPL.security;

import com.example.rpl.RPL.model.CourseUser;
import com.example.rpl.RPL.model.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Value
public class UserPrincipal implements UserDetails {

    private Long id;

    private String name;

    private String username;

    @JsonIgnore
    private String email;

    @JsonIgnore
    private String password;

    private Collection<? extends GrantedAuthority> authorities;

    @JsonIgnore
    private User user;

    private UserPrincipal(Long id, String name, String username, String email, String password,
        Collection<? extends GrantedAuthority> authorities, User user) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
        this.user = user;
    }

    public static UserPrincipal create(User user) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        if (user.getIsAdmin()) {
            authorities.add(new SimpleGrantedAuthority("superadmin"));
        }
        return new UserPrincipal(
            user.getId(),
            user.getName(),
            user.getUsername(),
            user.getEmail(),
            user.getPassword(),
            authorities,
            user
        );
    }

    public static UserPrincipal create(CourseUser courseUser) {
        List<GrantedAuthority> authorities = courseUser.getRole().getPermissions().stream().map(
            SimpleGrantedAuthority::new
        ).collect(Collectors.toList());
        if (courseUser.getUser().getIsAdmin()) {
            authorities.add(new SimpleGrantedAuthority("superadmin"));
        }
        return new UserPrincipal(
            courseUser.getUser().getId(),
            courseUser.getUser().getName(),
            courseUser.getUser().getUsername(),
            courseUser.getUser().getEmail(),
            courseUser.getUser().getPassword(),
            authorities,
            courseUser.getUser()
        );
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
