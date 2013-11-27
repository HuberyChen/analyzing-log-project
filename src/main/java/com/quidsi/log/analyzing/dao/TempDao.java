package com.quidsi.log.analyzing.dao;

import com.quidsi.core.database.JPAAccess;
import com.quidsi.log.analyzing.domain.TempId;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;
import java.util.List;

/**
 * @author hubery.chen
 */
@Repository
public class TempDao {

    private JPAAccess jpaAccess;

    public void save(TempId tempId) {
        jpaAccess.save(tempId);
    }

    public void delete(TempId tempId) {
        jpaAccess.delete(tempId);
    }

    public List<TempId> findAll() {
        StringBuilder sql = new StringBuilder();
        sql.append(" from ").append(TempId.class.getName());
        return jpaAccess.find(sql.toString(), null);
    }

    @Inject
    public void setJpaAccess(JPAAccess jpaAccess) {
        this.jpaAccess = jpaAccess;
    }
}
