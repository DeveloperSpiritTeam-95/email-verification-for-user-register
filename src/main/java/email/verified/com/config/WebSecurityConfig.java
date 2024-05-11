package email.verified.com.config;

import email.verified.com.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {


    @Autowired
    private DataSource dataSource;

    @Bean
    public UserDetailsService userDetailsService() {
        return new CustomUserDetailsService();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

//    @Bean
//    protected SecurityFilterChain configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.authenticationProvider(authenticationProvider());
//    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new CustomAccessDeniedHandler();
    }

    @Bean
    @Deprecated
    protected SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .requestMatchers("/users").authenticated()
                .anyRequest().permitAll()
                .and()
                .formLogin()
                .usernameParameter("email")
                .defaultSuccessUrl("/users")
                .permitAll()
                .and()
                .logout().logoutSuccessUrl("/").permitAll();

//        http
//                .authorizeHttpRequests((authz) -> {
//                            try {
//                                authz
//                                        .requestMatchers("/users").authenticated()
//                                        .anyRequest().permitAll()
//                                        .and().formLogin()
//                                        .loginPage("/login")
//                                        .usernameParameter("email")
//                                        .defaultSuccessUrl("/users", true)
//                                        .permitAll()
//                                        .and()
//                                        .logout()
////                                        .logoutSuccessUrl("/").permitAll()
//                                        .invalidateHttpSession(true)
//                                        .clearAuthentication(true)
//                                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
////                                        .logoutSuccessUrl("/login?logout")
//                                        .logoutSuccessUrl("/")
//                                        .permitAll()
//                                        .and()
////                                        .exceptionHandling()
////                                        .accessDeniedHandler(accessDeniedHandler)
//                                        .exceptionHandling().accessDeniedHandler(accessDeniedHandler())
//                                ;
//                            } catch (Exception e) {
//                                throw new RuntimeException(e);
//                            }
//                        }
//                );

        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers("/images/**", "/js/**", "/webjars/**");
    }

}
