package rs.ftn.FitpassCopyCat.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Properties;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    public void configureAuthentication(
            AuthenticationManagerBuilder authenticationManagerBuilder)
            throws Exception {

        authenticationManagerBuilder
                .userDetailsService(this.userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);

        mailSender.setUsername("fpclone2024@gmail.com");
        mailSender.setPassword("Y1i*M9d91*kdKD*7");

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");

        return mailSender;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public AuthenticationTokenFilter authenticationTokenFilterBean()
            throws Exception {
        AuthenticationTokenFilter authenticationTokenFilter = new AuthenticationTokenFilter();
        authenticationTokenFilter
                .setAuthenticationManager(authenticationManagerBean());
        return authenticationTokenFilter;
    }

//    todo antMatchers URL change to match Controllers' URLs
    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception{
        // disable auth check on Preflight requests
//        TODO* uncomment
        httpSecurity.cors().and();
        // A note to browser not to cache data received from headers
        httpSecurity.headers().cacheControl().disable();

        // h2 console to app communication configuration
        httpSecurity.headers().frameOptions().disable();
        httpSecurity.csrf().disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, "/reddit/users/login").permitAll()
                .antMatchers(HttpMethod.POST, "/reddit/users/").permitAll()
/*                .antMatchers(HttpMethod.GET, "/reddit/users/all").permitAll()*/
                .antMatchers(HttpMethod.PUT, "/reddit/users/ban").access("@webSecurity.moderatesCommunity(authentication, request, #communityId)")
                .antMatchers(HttpMethod.PUT, "/reddit/users/unban").access("@webSecurity.moderatesCommunity(authentication, request, #communityId)")
/*                .antMatchers(HttpMethod.POST, "reddit/users/logout").access("@webSecurity.isUserLogged(authentication, request)")*/
                .antMatchers(HttpMethod.GET, "/reddit/communities").permitAll()
                .antMatchers(HttpMethod.GET, "/reddit/communities/{id}").permitAll()
//                .antMatchers(HttpMethod.PUT, "/reddit/communities/{communityId}").access("@webSecurity.moderatesCommunity(authentication, request, #communityId)")
//  TODO* uncomment               .antMatchers(HttpMethod.DELETE, "/reddit/communities/{communityId}").access("@webSecurity.moderatesCommunity(authentication, request, #communityId)")
                .antMatchers(HttpMethod.GET, "/reddit/communities/{communityId}/posts").permitAll()
                .antMatchers(HttpMethod.GET, "/reddit/communities/{communityId}/posts/{postId}").permitAll()
                .antMatchers(HttpMethod.GET, "/reddit/posts/{postId}").permitAll()
//                .antMatchers(HttpMethod.POST, "/reddit/communities/{communityId}/posts").access("@webSecurity.canUserParttake(authentication, request, #postId)")
                .antMatchers(HttpMethod.PUT, "/reddit/communities/{communityId}/posts/{postId}").access("@webSecurity.canChangePost(authentication, request, #postId)")
//                .antMatchers(HttpMethod.DELETE, "/reddit/communities/{communityId}/posts/{postId}").access("@webSecurity.canChangePost(authentication, request, #postId)")
                .antMatchers(HttpMethod.GET, "/reddit/communities/{communityId}/flairs").permitAll()
                .antMatchers(HttpMethod.GET, "/reddit/communities/{communityId}/posts/{postId}/flair").permitAll()
//                .antMatchers(HttpMethod.PUT, "/reddit/communities/{communityId}/posts/{postId}/flair").access("@webSecurity.canChangePost(authentication, request, #postId)")
                .antMatchers(HttpMethod.GET, "/reddit/communities/{communityId}/flairs/{flair}").permitAll()
//                .antMatchers(HttpMethod.DELETE, "/reddit/communities/{communityId}/flairs/{flair}").access("@webSecurity.moderatesCommunity(authentication, request, #communityId)")
                .antMatchers(HttpMethod.GET, "/reddit/posts/{postId}/comments").permitAll()
                .antMatchers(HttpMethod.GET, "/reddit/posts/{postId}/comments/{commentId}").permitAll()
//                .antMatchers(HttpMethod.POST, "/reddit/posts/{postId}/comments/{parentId}").access("@webSecurity.canUserParttake(authentication, request, #postId)")
                .antMatchers(HttpMethod.PUT, "/reddit/posts/{postId}/comments/{commentId}").access("@webSecurity.canChangeComment(authentication, request, #postId, #commentId)")
                .antMatchers(HttpMethod.DELETE, "/reddit/posts/{postId}/comments/{commentId}").access("@webSecurity.canChangeComment(authentication, request, #postId, #commentId)")
//                .antMatchers(HttpMethod.POST, "/reddit/posts/{postId}/reactions").access("@webSecurity.canReactToPost(authentication, request, #postId)")
//                .antMatchers(HttpMethod.POST, "/reddit/comments/{commentId}/reactions").access("@webSecurity.canReactToComment(authentication, request, #commentId)")
                .antMatchers(HttpMethod.POST, "/reddit/communities/{communityId}/rules").access("@webSecurity.moderatesCommunity(authentication, request, #communityId)")
                .antMatchers(HttpMethod.DELETE, "/reddit/communities/{communityId}/rules/{ruleId}").access("@webSecurity.moderatesCommunity(authentication, request, #communityId)")
                .antMatchers(HttpMethod.POST, "/reddit/posts/{postId}/reports").access("@webSecurity.canReportPost(authentication, request, #postId)")
                .antMatchers(HttpMethod.POST, "/reddit/comments/{commentId}/reports").access("@webSecurity.canReportComment(authentication, request, #commentId)")
                .antMatchers(HttpMethod.PUT, "/reddit/reports/{reportId}").access("@webSecurity.canChangeReport(authentication, request, #reportId)")
                .antMatchers(HttpMethod.PUT, "/reddit/reports/{reportId}/accept").access("@webSecurity.canChangeReport(authentication, request, #reportId)")
                .antMatchers(HttpMethod.GET, "/reddit/comments/{commentId}/karma").permitAll()
                .antMatchers(HttpMethod.GET, "/reddit/posts/{postId}/karma").permitAll()

                .anyRequest().authenticated();

        httpSecurity.addFilterBefore(authenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter.class);
    }
}
