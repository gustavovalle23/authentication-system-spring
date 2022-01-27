package com.gustavovalle.customauthentication.service.security;

import com.gustavovalle.customauthentication.exception.GustavoValleException;
import com.gustavovalle.customauthentication.repository.user.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class AuthenticationService implements UserDetailsService {

    private final UserRepository userRepository;

    public AuthenticationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found with provided credentials"));
    }

    public Authentication authenticate(AuthenticationManager authenticationManager,
                                       UsernamePasswordAuthenticationToken login) throws Exception {
        try {
            return authenticationManager.authenticate(login);
        } catch (DisabledException e) {
            throw new GustavoValleException("User Disabled");
        } catch (BadCredentialsException e) {
            throw new GustavoValleException("Invalid Credentials");
        }
    }

}
