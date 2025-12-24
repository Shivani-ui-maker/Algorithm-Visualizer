    package com.algo.backend.security;

    import java.util.Collection;
    import java.util.Collections;
    import java.util.Objects;

    import org.springframework.security.core.GrantedAuthority;
    import org.springframework.security.core.authority.SimpleGrantedAuthority;
    import org.springframework.security.core.userdetails.UserDetails;

    import com.algo.backend.entity.User;
    import com.fasterxml.jackson.annotation.JsonIgnore;

    public class UserPrincipal implements UserDetails {
        private static final long serialVersionUID = 1L;
        
        private final Long id;
        private final String username;
        private final String uuid;
        private final String email;
        
        @JsonIgnore
        private final String password;
        
        private final Collection<? extends GrantedAuthority> authorities;

        public UserPrincipal(Long id, String username, String uuid, String email, String password, 
                            Collection<? extends GrantedAuthority> authorities) {
            this.id = id;
            this.username = username;
            this.uuid = uuid;
            this.email = email;
            this.password = password;
            this.authorities = authorities;
        }

        public static UserPrincipal create(User user) {
            return new UserPrincipal(
                user.getId(),
                user.getDisplayName(),
                user.getEmail(), // Using email as identifier since uuid is removed
                user.getEmail(),
                user.getPasswordHash(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()))
            );
        }

        public Long getId() {
            return id;
        }

        public String getUuid() {
            return uuid;
        }
        
        public String getEmail() {
            return email;
        }

        @Override
        public String getUsername() {
            return username;
        }

        @JsonIgnore
        @Override
        public String getPassword() {
            return password;
        }

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return authorities;
        }

        @JsonIgnore
        @Override
        public boolean isAccountNonExpired() {
            return true;
        }

        @JsonIgnore
        @Override
        public boolean isAccountNonLocked() {
            return true;
        }

        @JsonIgnore
        @Override
        public boolean isCredentialsNonExpired() {
            return true;
        }

        @JsonIgnore
        @Override
        public boolean isEnabled() {
            return true;
        }
        
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            UserPrincipal that = (UserPrincipal) o;
            return Objects.equals(id, that.id);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id);
        }
    }
