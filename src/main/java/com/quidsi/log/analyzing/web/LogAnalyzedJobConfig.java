package com.quidsi.log.analyzing.web;

import com.quidsi.core.platform.scheduler.info.JobInfo;
import com.quidsi.core.platform.scheduler.info.JobStatistic;
import com.quidsi.core.xml.XMLBuilder;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author Echo
 * @version createTime：Jul 4, 2013 10:10:41 AM
 */
@Component
public class LogAnalyzedJobConfig implements InitializingBean {

    private static final LogAnalyzedJobStatus DEFAULTJOBSTATUS = LogAnalyzedJobStatus.STOP;

    private static Map<String, LogAnalyzedJobStatus> logAnalyzedJobStatusConfig = new HashMap<>();

    private JobStatistic jobStatistic;

    public Map<String, LogAnalyzedJobStatus> getLogAnalyzedJobStatusConfig() {
        return logAnalyzedJobStatusConfig;
    }

    public String toXML() {
        XMLBuilder builder = XMLBuilder.indentedXMLBuilder();
        builder.startElement("job_config");
        for (Entry<String, LogAnalyzedJobStatus> entry : logAnalyzedJobStatusConfig.entrySet()) {
            builder.startElement("job");
            builder.attribute("id", entry.getKey());
            builder.attribute("status", entry.getValue().name());
            builder.endElement();
        }
        builder.endElement();
        return builder.toXML();
    }

    @Inject
    public void setJobStatistic(JobStatistic jobStatistic) {
        this.jobStatistic = jobStatistic;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (logAnalyzedJobStatusConfig.isEmpty()) {
            Map<String, JobInfo> jobs = jobStatistic.getJobs();
            for (JobInfo info : jobs.values()) {
                logAnalyzedJobStatusConfig.put(info.getJobId(), DEFAULTJOBSTATUS);
            }

        }
    }
}
