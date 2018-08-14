package me.bedaring.imsproject.configuration;

import me.bedaring.imsproject.models.data.UserDao;
import me.bedaring.imsproject.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@EnableGlobalMethodSecurity(prePostEnabled = true)
@Configuration
@EnableWebSecurity
@EnableJpaRepositories(basePackageClasses = UserDao.class)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public SecurityConfiguration(CustomUserDetailsService userDetailsService,
                                 BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userDetailsService = userDetailsService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(bCryptPasswordEncoder);
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf().disable()
                .authorizeRequests()
                    .antMatchers("/css/**").permitAll()
                    .antMatchers("/js/**").permitAll()
                    .antMatchers("/images/**").permitAll()

                    .antMatchers("/403").permitAll()
                    .antMatchers("/404").permitAll()
                    .antMatchers("/error/**").permitAll()

                    .antMatchers("/").permitAll()

                    .antMatchers("/profile").authenticated()

                    .antMatchers("/ticket/main").authenticated()

                    .antMatchers("/ticket/view/**").hasAnyRole("USER", "SUPPORT")
                    .antMatchers("/ticket/create").hasAnyRole("USER", "SUPPORT")

                    .antMatchers("/ticket/list").authenticated()
                    .antMatchers("/ticket/list/1").authenticated()
                    .antMatchers("/ticket/list/2").hasAnyRole("SUPPORT")

                    .antMatchers("/admin/**").hasRole("ADMIN")

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