package com.quidsi.log.analyzing.utils;

import com.quidsi.core.util.StopWatch;
import com.quidsi.log.analyzing.SpringTest;
import com.quidsi.log.analyzing.dao.ActionLogDetailDao;
import com.quidsi.log.analyzing.domain.ActionLogDetail;
import com.quidsi.log.analyzing.domain.SearchDetailCondition;
import org.junit.Assert;
import org.junit.Test;

import javax.inject.Inject;
import java.util.List;

/**
 * @author hubery.chen
 */
public class ActionLogDetailDaoTest extends SpringTest {

    private ActionLogDetailDao actionLogDetailDao;

    @Test
    public void actionLogDetailDaoTest() {
        SearchDetailCondition searchDetailCondition = new SearchDetailCondition();
        for (int i = 892; i < 914; i++) {
            searchDetailCondition.getLogIdList().add(i);
        }

        StopWatch stopWatch = new StopWatch();
        searchDetailCondition.setStatus("SUCCESS");
        int totalCount = actionLogDetailDao.getTotalCountByCondition(searchDetailCondition);
        searchDetailCondition.setTotalCount(totalCount);
        searchDetailCondition.setOffset(10);
        List<ActionLogDetail> details = actionLogDetailDao.findConditionLimit(searchDetailCondition);
        for (ActionLogDetail detail : details) {
            System.out.println(detail.getRecordTime());
        }
        System.out.println(totalCount);
        System.out.println(stopWatch.elapsedTime());
    }

    @Test
    public void findConditionLimitIdTest() {
        SearchDetailCondition searchDetailCondition = new SearchDetailCondition();
        for (int i = 892; i < 914; i++) {
            searchDetailCondition.getLogIdList().add(i);
        }

        searchDetailCondition.setStatus("SUCCESS");
        int totalCount = actionLogDetailDao.getTotalCountByCondition(searchDetailCondition);
        searchDetailCondition.setTotalCount(totalCount);
        searchDetailCondition.setOffset(10);
        List<Integer> ids = actionLogDetailDao.findConditionLimitId(searchDetailCondition);
        Assert.assertNotNull(ids);
    }

    @Inject
    public void setActionLogDetailDao(ActionLogDetailDao actionLogDetailDao) {
        this.actionLogDetailDao = actionLogDetailDao;
    }
}
