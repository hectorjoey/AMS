package fhi360.it.assetverify.config;

import fhi360.it.assetverify.auth.RestAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfiguration{
    private final RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication().withUser("asset")
                .password(this.passwordEncoder().encode("admin"))
                .roles("ADMIN").and().withUser("user").password
                        (this.passwordEncoder().encode("user")).roles("USER");
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().exceptionHandling().authenticationEntryPoint(this.restAuthenticationEntryPoint)
                .and().authorizeRequests().antMatchers("/**").permitAll()
                .antMatchers("/login").permitAll()
                .antMatchers(HttpMethod.POST, "/users").hasRole("ADMIN")
                .antMatchers("/delete/**").hasRole("ADMIN")
                .antMatchers("/updateUser").hasAnyRole("USER").anyRequest()
                .authenticated().and().formLogin().permitAll()
                .and().logout().permitAll().and().exceptionHandling().accessDeniedPage("/403");

        http.csrf().disable();

        return http.build();
    }

}