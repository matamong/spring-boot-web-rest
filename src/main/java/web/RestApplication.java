package web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class RestApplication implements WebMvcConfigurer {
    public static void main(String[] args) {
        SpringApplication.run(RestApplication.class, args);
    }

    @Configuration
    @EnableGlobalMethodSecurity(prePostEnabled = true)
    @EnableWebSecurity
    static class SecurityConfiguration extends WebSecurityConfigurerAdapter {

        @Bean
        InMemoryUserDetailsManager userDetailsManager() {
            User.UserBuilder commonUser = User.withUsername("commonUser");
            User.UserBuilder matamong = User.withUsername("matamong");

            List<UserDetails> userDetailsList = new ArrayList<>();
            userDetailsList.add(commonUser.password("{noop}common").roles("USER").build());
            userDetailsList.add(matamong.password("{noop}test").roles("USER", "ADMIN").build());

            return new InMemoryUserDetailsManager(userDetailsList);
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            CorsConfiguration configuration = new CorsConfiguration();
            configuration.addAllowedOrigin("*");                            //요청을 보내는 페이지의 출처 (*, 도메인)
            configuration.addAllowedMethod("*");                            //요청을 허용하는 메소드 (Default : GET, POST, HEAD)
            configuration.addAllowedHeader("*");                            // 요청을 허용하는 헤더
            UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
            source.registerCorsConfiguration("/**", configuration);

            http.httpBasic()
                    .and().authorizeRequests()
                    //.antMatchers(HttpMethod.POST, "/Boards/**").hasRole("ADMIN")
                    .anyRequest().permitAll()
                    .and().cors().configurationSource(source)
                    .and().csrf().disable();
        }
    }
}
