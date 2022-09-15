package com.microservices.flash.bloop.client.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.microservices.flash.bloop.client.security.MemberUserDetailsService;
import com.microservices.flash.bloop.client.security.oauth.DatabaseLoginSuccessHandler;
import com.microservices.flash.bloop.client.security.oauth.MemberOAuth2UserService;
import com.microservices.flash.bloop.client.security.oauth.OAuth2LoginSuccessHandler;

/**
 *  Create a Spring Security Class by extending WebSecurityConfigurerAdapter and override configure() methods
 *  Authentication is required by default
 *  Default login page is good for quick test
 *  Show Spring-generated error message in custom login page using attrubute SPRING_SECURITY_LAST_EXCEPTION is session
 *  Every POST request must include _crsf toke -> prevent Cross Site Request Forgery (CSRF) attacks
 *  Ignore authentication for static resources (images, CSS, JS, e.t.c)
 *  Logout request must be sent in HTTP POST
 *  Create a class of type UserDetaks ti represent the currently logged-in user
 *  Use @AuthenticationPrincipal to get the UserDetails object representing the authenticated user: @AuthenticationPrincipal StoreUserDetails loggedUser
 *  Default cookie: JSESSIONID (expires when session ends)
 *  "Remember Me" implementation: hash-based toek (cookies only) and persistent token (cookies and database)
 *  remember-me cookie: expires after 14 days (default)
 *  Spring Security Filter intercepts requests to process authentication 
 */
@EnableWebSecurity
@Configuration
public class SecurityConfig {

    /**
     * Reference a MemberOAuth2UserService 
     *     We need to tell Spring Security to use MemberOAuth2UserService
     */
    @Autowired
    private MemberOAuth2UserService memberOAuth2UserService;

    /**
     * Reference OAuth2LoginSuccessHandler 
     */
    @Autowired
    private OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;

    /**
     * Reference DatabaseLoginSuccessHandler 
     */
    @Autowired
    private DatabaseLoginSuccessHandler databaseLoginSuccessHandler;

    /**
     * Return an instance of the PasswordEncoder
     *     Use "static" to get rid of circular reference error
     */
    @Bean
    static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Return an instance of the MemberUserDetails 
     */
    @Bean
    UserDetailsService userDetailsService() {
        return new MemberUserDetailsService();
    }

    /**
     * Configuring HTTP Security
     */
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.csrf()
                /**
                 * Disable CSRF 
                 */
                .disable()
                .authorizeRequests()
                /**
                 * Only these URIs require authentication 
                 * Spring Security requires authentication for only these URLs
                 */
                .antMatchers("/messages/**", "/swagger-ui/**")
                /**
                 * Access to the application will require Authentication 
                 */
                .authenticated()
                .anyRequest()
                .permitAll()
                .and()
                .formLogin()
                    /**
                     * Define custom login page 
                     */
                    .loginPage("/login")
                    /**
                     * Specify username parameter defined in login page 
                     */
                    .usernameParameter("email")
                    /**
                     * Reference DatabaseLoginSuccessHandler 
                     */
                    .successHandler(databaseLoginSuccessHandler)
                    .permitAll()
                .and()
                /**
                 * Call the OAuth2 Login 
                 */
                .oauth2Login()
                    .loginPage("/login")
                    .userInfoEndpoint()
                    /**
                     * Pass in the reference to the MemberOAuth2UserService
                     */
                    .userService(memberOAuth2UserService)
                    .and()
                    /**
                     * Configure the OAuth2 Login Success Handler 
                     *    Pass in a reference to OAuth2LoginSuccessHandler
                     */
                    .successHandler(oAuth2LoginSuccessHandler)
                .and()
                /**
                 * Permit Spring Security to logout  
                 */
                .logout()
                .permitAll()
                .and()
                /**
                 * With the default option a random private key is generated to encode the login information in the cookie 
                 */
                .rememberMe()
                    /*
                     * The default option destroys cookies when the application is restarted
                     * To allow the cookies to survive when the application is restrated we need to provide a key
                     */
                    .key("YWRtaW5pLnN0cmF0b3IlNDBkb21haW4uY29tOjE2NjAzNzk1MTY1NzQ6YzY4ZjVlY2M1NmI3MWNmZWNhNDcwNzVhNGJiYzVmODA")
                    /**
                     * Lifetime of the cookie
                     * We can also set the expiration time for the cookie. The default expiration time is 2 weeks
                     * Reset to one week
                     */
                    .tokenValiditySeconds(7 * 24 * 60 * 60);

        return http.build();
    }

    /**
     * Exempting static resources from being authenticated
     */
    @Bean
    WebSecurityCustomizer webSecurityCustomizer() {
        
        /**
         * Ignore authentication for static resources 
         */
        return (web) -> web.ignoring()
                            .antMatchers(
                                "/images/**", 
                                "/js/**", 
                                "/webjars/**", 
                                "/css/**", 
                                "/fontawesome/**", 
                                "/richtext/**"
                            );
    }


    /**
     * Configure the authentication provider 
     */
    @Bean
    AuthenticationManager defaultAuthenticationManager(ObjectPostProcessor<Object> objectPostProcessor) throws Exception {
        return new AuthenticationManagerBuilder(objectPostProcessor).authenticationProvider(authenticationProvider())
                .build();
    } 

    /**
     * Configure Spring Security for Authentication with the database
     * 
     * Tell Spring Security that we will use DaoAuthenticationProvider
     *     The Authentication will be based on the Database looking up the Member details stored in the database
     */
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService());
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());

        return daoAuthenticationProvider;
    }


}
