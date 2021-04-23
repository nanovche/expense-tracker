package com.pairlearning.expensetracker.controllers;

import com.pairlearning.expensetracker.Constants;
import com.pairlearning.expensetracker.domain.User;
import com.pairlearning.expensetracker.services.UserService;
import com.pairlearning.expensetracker.sessions.SessionTable;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.pairlearning.expensetracker.Constants.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody Map<String, Object> userMap, HttpServletResponse response) {
        String email = (String) userMap.get(EMAIL);
        String password = (String) userMap.get(PASSWORD);
        User user = userService.validateUser(email, password);
        response.addCookie(generateSessionId(user));
        return new ResponseEntity<>(/*generateJWTToken(user),*/HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> registerUser(@RequestBody Map<String, Object> userMap){
        String firstName = (String) userMap.get(FIRST_NAME);
        String lastName = (String) userMap.get(LAST_NAME);
        String email = (String) userMap.get(EMAIL);
        String password = (String) userMap.get(PASSWORD);
        User user = userService.registerUser(firstName, lastName, email, password);
        return new ResponseEntity<>(generateJWTToken(user), HttpStatus.OK);
    }

    private Cookie generateSessionId(User user) {
        Integer userId = user.getUserId();
        String value = UUID.randomUUID().toString();
        SessionTable.setSessionIdForUser(value, userId);
        return new Cookie(NAME_OF_SESSION_COOKIE, value);
    }

    private Map<String, String> generateJWTToken (User user) {
       long timestamp = System.currentTimeMillis();
        String token = Jwts.builder().signWith(SignatureAlgorithm.HS256, Constants.API_SECRET_KEY)
                .setIssuedAt(new Date(timestamp))
                .setExpiration(new Date(timestamp + Constants.TOKEN_VALIDITY))
                .claim(USER_ID, user.getUserId())
                .claim(EMAIL, user.getEmail())
                .claim(FIRST_NAME, user.getFirstName())
                .claim(LAST_NAME, user.getLastName())
                .compact();
        Map<String, String> map = new HashMap<>();
        map.put(TOKEN, token);
        return map;
    }

}
