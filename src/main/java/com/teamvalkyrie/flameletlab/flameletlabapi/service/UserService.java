package com.teamvalkyrie.flameletlab.flameletlabapi.service;

import com.teamvalkyrie.flameletlab.flameletlabapi.model.Role;
import com.teamvalkyrie.flameletlab.flameletlabapi.model.User;
import com.teamvalkyrie.flameletlab.flameletlabapi.repository.UserRepository;
import com.teamvalkyrie.flameletlab.flameletlabapi.util.SecurityUtil;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     *  Find a user by their email
     *
     * @param email the user's email
     * @return Optional with user inside or empty if none found
     */
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * Fina a user by their id
     *
     * @param id the user's id
     * @return Optional with user inside or empty if none found
     */
    public Optional<User> findOneById(Long id) {
        return userRepository.findById(id);
    }

    public String getCurrentLoggedInUserFullName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails user = (UserDetails) authentication.getPrincipal();
            Optional<User> ireviewMoviesUser= userRepository.findByEmail(user.getUsername());
            if(ireviewMoviesUser.isPresent()) {
                return ireviewMoviesUser.get().getFullName();
            }
        }

        return "";

    }


    public User getCurrentLoggedInUser() {
        Authentication authentication = SecurityUtil.getAuthentication();

        if(authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails user = (UserDetails) authentication.getPrincipal();
            Optional<User> currellentLoggedInUser = userRepository.findByEmail(user.getUsername());
            if(currellentLoggedInUser .isPresent()) {
                return currellentLoggedInUser .get();
            }
        }

        return null;
    }

    @Transactional
    public User save(User user) {

        // Only encode password if user is new
        if (user.getId() == null) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        return userRepository.save(user);
    }


    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = this.findByEmail(s)
                .orElseThrow(() -> new UsernameNotFoundException("User Not found"));
        org.springframework.security.core.userdetails.User userDetails =
                new org.springframework.security.core.userdetails.User(user.getEmail(),
                        user.getPassword(), this.mapRolesToAuthorities(user.getRoles()));

        return userDetails;
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection <Role> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getRole().name()))
                .collect(Collectors.toList());
    }
}