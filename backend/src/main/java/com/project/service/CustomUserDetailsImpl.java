package com.project.service;

import com.project.dao.UserDao;
import com.project.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CustomUserDetailsImpl implements UserDetailsService {

    private final UserDao userDao;

    public CustomUserDetailsImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> optUser = userDao.findByEmail(username);
        if (optUser.isEmpty()){
            throw new UsernameNotFoundException("User not found with email: " + username);
        } else {
            User user = optUser.get();

            List<GrantedAuthority> authorities = new ArrayList<>();
            return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), authorities);
        }
    }

}