package me.bedaring.imsproject.service;

import me.bedaring.imsproject.models.CustomUserDetails;
import me.bedaring.imsproject.models.User;
import me.bedaring.imsproject.models.data.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserDao userDao;

    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public CustomUserDetailsService(UserDao userDao, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userDao = userDao;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> optionalUser = userDao.findByUsername(username);

        optionalUser
                .orElseThrow(() -> new UsernameNotFoundException("Username not found"));

        return optionalUser
                .map(CustomUserDetails::new).get();

    }
}
