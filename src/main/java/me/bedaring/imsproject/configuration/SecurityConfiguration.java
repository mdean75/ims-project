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
                // allow all access to css, js, images, error pages, and the index page
                    .antMatchers("/css/**").permitAll()
                    .antMatchers("/js/**").permitAll()
                    .antMatchers("/images/**").permitAll()

                    .antMatchers("/403").permitAll()
                    .antMatchers("/404").permitAll()
                    .antMatchers("/error/**").permitAll()

                    .antMatchers("/").permitAll()

                    .antMatchers("/profile/activate").permitAll()
                    .antMatchers("/profile/reset-password").permitAll()

                // allow all authenticated users to access the following
                    .antMatchers("/profile").authenticated()
                    .antMatchers("/sms").authenticated()
                    .antMatchers("/ticket/main").authenticated()

                    .antMatchers("/ticket/list").authenticated()
                    .antMatchers("/ticket/list/1").authenticated()

                // require user or support to access these pages
                    .antMatchers("/ticket/view/**").hasAnyRole("USER", "SUPPORT")
                    .antMatchers("/ticket/create").hasAnyRole("USER", "SUPPORT")

                // require support role to have access to all tickets
                    .antMatchers("/ticket/list/2").hasAnyRole("SUPPORT")

                // require admin role to have access to any of the admin pages
                    .antMatchers("/admin/**").hasRole("ADMIN")

                .anyRequest().authenticated()
                    .and()
                .exceptionHandling().accessDeniedPage("/403")
                    .and()
                .formLogin()

                // landing page after successful login for all users
                .defaultSuccessUrl("/ticket/main")
                    .permitAll()
                    .and()
                .logout()
                    .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))

                // after successful logout redirect to index page
                    .logoutSuccessUrl("/");
    }
}