package com.example.demo;

import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
class SecurityFilterChainConfiguration {

    @Bean
    @Order(SecurityProperties.BASIC_AUTH_ORDER)
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((requests) -> requests.requestMatchers("/hello").permitAll());
        http.authorizeHttpRequests((requests) -> requests.anyRequest().authenticated());
/*         You can disable the below via
         http.formLogin(fcl -> fcl.disable());
         But then only basic auth will be active
         In that case browser is nice enough to offer its own built in user/password
         pop-up screen. Info will be sent in "Authorization" header as "Basic XYZ"
         where XYX is the base64 encoded form of "user:pass"
         For example if you type into the browser login user=a pass=b you will see
         Authorization header as "Basic YTpi" and you can double check
         echo -n "a:b" | base64 produces YTpi
         echo -n "YTpi" | base64 -d produces "a:b"

 */
        http.formLogin(withDefaults());
        http.httpBasic(withDefaults());
        // If you don't say anything both http and https are allowed
        // If you want to restrict to only one, pick among the lines below
        // http.requiresChannel(rcc -> rcc.anyRequest().requiresInsecure());
        // http.requiresChannel(rcc -> rcc.anyRequest().requiresSecure());
        return http.build();
    }

    @Bean
    @Profile("default")
    public UserDetailsService userDetailsService() {
        UserDetails u1 = User
                .withUsername("user")
                .password("{noop}mypass1").
                authorities("read").build();
        UserDetails u2 = User
                .withUsername("admin")
                // brypt online hashed "1234" to the string below
                .password("{bcrypt}$2y$10$B3shz6IZdvILcYhZZcNhV.UIqE/AMlkxckVMVO8k8i3J69xA/Xuue")
                .authorities("admin").build();
        return new InMemoryUserDetailsManager(u1, u2);
    }

    /*
    switch the active profile to "usedb" then user/pass/authority will be read from DB
    As long as you've done the setups below.

Note even if "usedb" is not active, it will try to connect to database some auto-configuration
feature of spring boot. That's why spring.datasource properties are still active in
application.properties

create table users(username varchar(50) not null primary key,password varchar(500) not null,enabled boolean not null);
create table authorities (username varchar(50) not null,authority varchar(50) not null,constraint fk_authorities_users foreign key(username) references users(username));
create unique index ix_auth_username on authorities (username,authority);
insert into users values('user','{noop}mypass',1);
insert into authorities('user', 'read');
 */
    @Bean
    @Profile("usedb")
    public UserDetailsService userDetailsServiceJdbc(DataSource dataSource) {
        return new JdbcUserDetailsManager(dataSource);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // click inside this class to see the possible encoders
        // bcrypt os the default
        // to user another one say it above when giving your password
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}

