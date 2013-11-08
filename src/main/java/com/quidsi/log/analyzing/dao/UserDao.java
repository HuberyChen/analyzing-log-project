package com.quidsi.log.analyzing.dao;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.quidsi.core.database.JPAAccess;
import com.quidsi.log.analyzing.domain.User;

@Repository
public class UserDao {

    private JPAAccess jpaAccess;

    @Transactional
    public int saveUser(User user) {
        jpaAccess.save(user);
        return user.getId();
    }

    public User getUserByName(String name) {
        Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        return jpaAccess.findUniqueResult("from " + User.class.getName() + " where name=:name", params);
    }

    @Inject
    public void setJpaAccess(JPAAccess jpaAccess) {
        this.jpaAccess = jpaAccess;
    }
}
