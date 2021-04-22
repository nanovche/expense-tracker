package com.pairlearning.expensetracker.filters;

import com.pairlearning.expensetracker.exceptions.EtAuthException;
import com.pairlearning.expensetracker.sessions.SessionTable;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthSessionFilter extends GenericFilterBean {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String cookieValue = null;
        for (Cookie cookie: httpRequest.getCookies()) {
            if(cookie.getName().equals("sessionId")) {
                cookieValue = cookie.getValue();
            }
        }

        Integer userId = null;
        try {
            userId = SessionTable.retrieveUserIdBySessionId(cookieValue);
        } catch (EtAuthException e) {
            e.printStackTrace();
        }
        httpRequest.setAttribute("userId", userId);
        chain.doFilter(request, response);
    }
}