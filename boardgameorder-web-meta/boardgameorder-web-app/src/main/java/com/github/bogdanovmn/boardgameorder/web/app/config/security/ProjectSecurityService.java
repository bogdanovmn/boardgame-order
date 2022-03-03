package com.github.bogdanovmn.boardgameorder.web.app.config.security;


import com.github.bogdanovmn.boardgameorder.web.orm.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProjectSecurityService {
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;

    public boolean isLogged() {
        return this.getLoggedInUser() != null;
    }

    public User getLoggedInUser() {
        Object userDetails = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (userDetails instanceof ProjectUserDetails) {
            return ((ProjectUserDetails) userDetails).getUser();
        }

        return null;
    }

    public void login(String username, String password) {
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
            = new UsernamePasswordAuthenticationToken(
            userDetails,
            password,
            userDetails.getAuthorities()
        );

        this.authenticationManager.authenticate(usernamePasswordAuthenticationToken);

        if (usernamePasswordAuthenticationToken.isAuthenticated()) {
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            LOG.info(String.format("Auto login %s successfully!", username));
        }
    }
}
