package com.quidsi.log.analyzing.dao;

import javax.inject.Inject;

import org.junit.Test;

import com.quidsi.core.util.DigestUtils;
import com.quidsi.log.analyzing.SpringTest;
import com.quidsi.log.analyzing.domain.User;

public class UserDaoTest extends SpringTest {

    private UserDao userDao;

    @Test
    public void saveTest() {
        User user = new User();
        user.setName("1");
        user.setPassword(DigestUtils.sha512("1"));
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
