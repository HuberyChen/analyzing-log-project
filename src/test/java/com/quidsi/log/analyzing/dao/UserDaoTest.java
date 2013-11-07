package com.quidsi.log.analyzing.dao;

import com.quidsi.core.util.DigestUtils;
import com.quidsi.log.analyzing.SpringTest;
import com.quidsi.log.analyzing.domain.User;

import org.junit.Test;

import javax.inject.Inject;

public class UserDaoTest extends SpringTest {

    private UserDao userDao;

    @Test
    public void saveTest() {
        User user = new User();
        user.setName("2");
        user.setPassword(DigestUtils.sha512("2"));
        userDao.saveUser(user);
    }

    @Test
    public void getTest() {
        userDao.getUserByName("1");
    }

    @Inject
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

}
