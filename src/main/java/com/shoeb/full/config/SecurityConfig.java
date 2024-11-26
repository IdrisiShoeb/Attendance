package com.shoeb.full.config;

import com.shoeb.full.services.impl.SecurityCustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
public class SecurityConfig {


    @Autowired
    private SecurityCustomUserDetailsService userDetailService;

    // configuraiton of authentication provider for spring security
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        // user detail service  object:
        daoAuthenticationProvider.setUserDetailsService(userDetailService);
        // password encoder  object
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());

        return daoAuthenticationProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
//        httpSecurity.authorizeHttpRequests(authorize->{
//            authorize.requestMatchers("/user/**").authenticated();
//            authorize.anyRequest().permitAll();
//        });

                httpSecurity.authorizeRequests(authorize -> {
                    authorize
                            .requestMatchers("/admin/**").hasRole("ADMIN") // Only ADMINs can access this route
                            .requestMatchers("/user/**").hasAnyRole("USER", "ADMIN") // USER and ADMIN can access user routes
                            .anyRequest().permitAll(); // All other requests are permitted
                });

        httpSecurity.formLogin(formLogin -> {
            formLogin.loginPage("/login");
            formLogin.loginProcessingUrl("/authenticate");
            formLogin.defaultSuccessUrl("/user/profile");
            formLogin.usernameParameter("email");
            formLogin.passwordParameter("password");
        });

        httpSecurity.exceptionHandling(exceptionHandling -> {
            exceptionHandling.accessDeniedPage("/403"); // Specify the custom forbidden page
        });

        httpSecurity.csrf(AbstractHttpConfigurer::disable);
        httpSecurity.logout(logoutForm -> {
            logoutForm.logoutUrl("/do-logout");
            logoutForm.logoutSuccessUrl("/login?logout=true");
        });

        return httpSecurity.build();


    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }


}
