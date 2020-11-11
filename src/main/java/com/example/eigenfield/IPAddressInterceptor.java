package com.example.eigenfield;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class IPAddressInterceptor implements HandlerInterceptor {

    public String ipaddr;

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler)
        throws Exception
    {
        String ipAddress = request.getHeader("X-Forward-For");

        if(ipAddress== null){
            ipAddress = request.getRemoteAddr();
        }

        System.out.println(ipAddress);
        ipaddr = ipAddress;
        return true;
    }
}

