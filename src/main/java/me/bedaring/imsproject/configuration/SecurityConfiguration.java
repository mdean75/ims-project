package me.bedaring.imsproject.configuration;

import me.bedaring.imsproject.models.data.UserDao;
import me.bedaring.imsproject.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@EnableGlobalMethodSecurity(prePostEnabled = true)
@Configuration
@EnableWebSecurity
@EnableJpaRepositories(basePackageClasses = UserDao.class)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {



    @Autowired
    private CustomUserDetailsService userDetailsService;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public SecurityConfiguration(CustomUserDetailsService userDetailsService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userDetailsService = userDetailsService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(bCryptPasswordEncoder);
    }




    private PasswordEncoder getPasswordEncoder() {
        return new PasswordEncoder() {
            @Override
            public String encode(CharSequence charSequence) {
                return charSequence.toString();
            }

            @Override
            public boolean matches(CharSequence charSequence, String s) {
                return true;
            }
        };
    }


    public PasswordEncoder passwordEncoder(){
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder;
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf().disable()
                .authorizeRequests()
                    .antMatchers("/css/**").permitAll()
                    .antMatchers("/js/**").permitAll()
                    .antMatchers("/images/**").permitAll()
                    .antMatchers("/ticket").permitAll()
                    .antMatchers("/profile").authenticated()
                .antMatchers("/403").permitAll()
                .antMatchers("/404").permitAll()
                .antMatchers("/error/**").permitAll()
                .antMatchers("/").permitAll()
                    .antMatchers("/ticket/view/**").permitAll()
                    .antMatchers("/admin/**").hasRole("ADMIN")
                    .antMatchers("/ticket/list/2").hasAnyRole("ADMIN", "SUPPORT")
                    //.antMatchers("/ticket/create").hasAnyRole("SUPPORT", "ADMIN")
                    .anyRequest().authenticated()
                .and()
                .exceptionHandling().accessDeniedPage("/403")
                    .and()
                .formLogin()


                    .defaultSuccessUrl("/ticket/main")
                    .permitAll()
                    .and()
                .logout()
                    .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                    .logoutSuccessUrl("/");
    }





}