package com.pairlearning.expensetracker.filters;

import com.pairlearning.expensetracker.Constants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.pairlearning.expensetracker.Constants.USER_ID;

public class AuthFilter extends GenericFilterBean {

	private static final String INVALID_OR_EXPIRED_TOKEN = "invalid/expired token";
	private static final String AUTHORIZATION_TOKEN_MUST_BE_PROVIDED = "authorization token must be provided";
	private static final String AUTHORIZATION_TOKEN_MUST_BE_BEARER = "authorization must be bearer [token]";

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		
		String authHeader = httpRequest.getHeader("Authorization");
		if(authHeader != null) {
			String[] authHeaderArr = authHeader.split("Bearer ");
			if(authHeaderArr.length > 1 && authHeaderArr[1] != null) {
				String token = authHeaderArr[1];
				try {
						Claims claims = Jwts.parser().setSigningKey(Constants.API_SECRET_KEY)
								.parseClaimsJws(token).getBody();
						httpRequest.setAttribute(USER_ID, Integer.parseInt(claims.get(USER_ID).toString()));
				} catch(Exception e) {
					httpResponse.sendError(HttpStatus.FORBIDDEN.value(), INVALID_OR_EXPIRED_TOKEN);
					return;
				}
			} else {
				httpResponse.sendError(HttpStatus.FORBIDDEN.value(), AUTHORIZATION_TOKEN_MUST_BE_BEARER);
				return;
			}
		} else {
			httpResponse.sendError(HttpStatus.FORBIDDEN.value(), AUTHORIZATION_TOKEN_MUST_BE_PROVIDED);
			return;
		}
		chain.doFilter(request, response);
	}
}
