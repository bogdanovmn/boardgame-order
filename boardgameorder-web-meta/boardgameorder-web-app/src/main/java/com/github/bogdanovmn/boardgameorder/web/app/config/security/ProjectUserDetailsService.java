package com.github.bogdanovmn.boardgameorder.web.app.config.security;

import com.github.bogdanovmn.boardgameorder.web.orm.entity.User;
import com.github.bogdanovmn.boardgameorder.web.orm.entity.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class ProjectUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Autowired
    public ProjectUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String name)
        throws UsernameNotFoundException {
        User user = userRepository.findFirstByName(name);

        if (user == null) {
            throw new UsernameNotFoundException(
                String.format("User '%s' not found", name)
            );
        }

        return new ProjectUserDetails(user);
    }
}
