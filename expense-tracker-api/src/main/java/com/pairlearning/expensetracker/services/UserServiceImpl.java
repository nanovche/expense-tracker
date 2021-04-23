package com.pairlearning.expensetracker.services;

import com.pairlearning.expensetracker.domain.User;
import com.pairlearning.expensetracker.exceptions.EtAuthException;
import com.pairlearning.expensetracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

import static com.pairlearning.expensetracker.Constants.EMAIL_IN_USE;
import static com.pairlearning.expensetracker.Constants.INVALID_EMAIL_FORMAT;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    UserRepository userRepository;

    @Override
    public User validateUser(String email, String password) throws EtAuthException {
        if(email != null) {
            email = email.toLowerCase();
        }
        return userRepository.fetchByEmailAndPassword(email, password);
    }

    @Override
    public User registerUser(String firstName, String lastName, String email, String password) throws EtAuthException {
        Pattern pattern = Pattern.compile("^(.+)@(.+)$");
        if(email != null) {
            email = email.toLowerCase();
        }
        if(!pattern.matcher(email).matches()){
            throw new EtAuthException(INVALID_EMAIL_FORMAT);
        }
        Integer count = userRepository.getCountByEmail(email);
        if(count > 0) {
            throw new EtAuthException(EMAIL_IN_USE);
        }
        User user = new User.
                UserBuilder(firstName, lastName, email, password).
                build();
        Integer userId = userRepository.create(user);
        return userRepository.findById(userId);
    }
}
