package com.quidsi.log.analyzing.service;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.quidsi.core.util.DigestUtils;
import com.quidsi.log.analyzing.dao.UserDao;
import com.quidsi.log.analyzing.domain.User;

@Service
public class UserService {

    private UserDao userDao;

    @Transactional
    public int saveUser(User user) {
        return userDao.saveUser(user);
    }

    public User getUserByName(String name) {
        return userDao.getUserByName(name);
    }

    public boolean verifyPassword(String plainText, String cipherText) {
        return cipherText.equals(DigestUtils.sha512(plainText));
    }

    @Inject
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

}
