package com.pairlearning.expensetracker.repository;

import com.pairlearning.expensetracker.domain.User;
import com.pairlearning.expensetracker.exceptions.EtAuthException;

public interface UserRepository {

    Integer create(User user) throws EtAuthException;

    User fetchByEmailAndPassword(String email, String password) throws EtAuthException;

    Integer getCountByEmail(String email);

    User findById(Integer userId);
}
