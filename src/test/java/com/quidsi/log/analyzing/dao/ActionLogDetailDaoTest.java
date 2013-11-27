package com.quidsi.log.analyzing.dao;

import com.quidsi.core.util.StopWatch;
import com.quidsi.log.analyzing.SpringTest;
import com.quidsi.log.analyzing.domain.SearchDetailCondition;
import com.quidsi.log.analyzing.domain.TempLog;
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

        StopWatch stopWatch = new StopWatch();
        searchDetailCondition.setStatus("SUCCESS");
        int totalCount = actionLogDetailDao.getTotalCountByCondition(searchDetailCondition);
        searchDetailCondition.setTotalCount(totalCount);
        searchDetailCondition.setOffset(10);
        List<TempLog> tempLogs = actionLogDetailDao.findConditionLimitId(searchDetailCondition);
        for (TempLog tempLog : tempLogs) {
            System.out.println(tempLog.getId());
        }
        System.out.println(totalCount);
        System.out.println(stopWatch.elapsedTime());
    }

    @Inject
    public void setActionLogDetailDao(ActionLogDetailDao actionLogDetailDao) {
        this.actionLogDetailDao = actionLogDetailDao;
    }
}
