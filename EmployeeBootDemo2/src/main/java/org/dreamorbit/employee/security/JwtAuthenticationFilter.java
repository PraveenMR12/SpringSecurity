package org.dreamorbit.employee.security;

import java.io.IOException;
import java.util.Enumeration;

import org.apache.commons.lang3.StringUtils;
import org.dreamorbit.employee.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private Logger logger = LoggerFactory.getLogger(OncePerRequestFilter.class);
    @Autowired
    private JwtHelper jwtHelper;


//    @Autowired
//    private UserDetailsService userDetailsService;
    
    @Autowired
    private UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        //Authorization = Bearer 2352345235sdfrsfgsdfsdf
    	
        String requestHeader = request.getHeader("Authorization");
        System.out.println("Hii");

        logger.info(" Header :  {}", requestHeader);
        System.out.println(requestHeader);
        String username = null;
        String jwtToken = null;
        
        if(StringUtils.isEmpty(requestHeader) || !StringUtils.startsWith(requestHeader, "Bearer"))
        {
        	filterChain.doFilter(request, response);
        	return;
        }

    	jwtToken = requestHeader.substring(7);
    	username = this.jwtHelper.extractUserName(jwtToken);



        //
        if (StringUtils.isNotEmpty(username) && SecurityContextHolder.getContext().getAuthentication() == null) {


            //fetch user detail from username
            UserDetails userDetails = userService.userDetailsService().loadUserByUsername(username);
            Boolean validateToken = this.jwtHelper.isTokenValid(jwtToken, userDetails);
            if (validateToken) {

            	//set the authentication
            	
            	SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
                UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            	securityContext.setAuthentication(token);
            	SecurityContextHolder.setContext(securityContext);
            	
               
//                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
//                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//                SecurityContextHolder.getContext().setAuthentication(authentication);


            }


        }

        filterChain.doFilter(request, response);


    }
}