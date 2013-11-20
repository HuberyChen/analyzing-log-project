package com.quidsi.log.analyzing.web.request;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * @author hubery.chen
 */
@XmlRootElement(name = "condition-detail-show-request")
@XmlAccessorType(XmlAccessType.FIELD)
public class ConditionDetailShowRequest {

    @XmlElement(name = "status")
    private String status;

    @XmlElement(name = "interface")
    private String interfaceName;

    @XmlElement(name = "errorCode")
    private String errorCode;

    @XmlElement(name = "offset")
    private int offset;

    @XmlElement(name = "logIdList")
    private final List<Integer> logIdList = new ArrayList<>();

    @XmlElement(name = "totalCount")
    private int totalCount;

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public List<Integer> getLogIdList() {
        return logIdList;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

}
