package com.quidsi.log.analyzing.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hubery.chen
 */
public class SearchDetailCondition {

    private String status;

    private String interfaceName;

    private String errorCode;

    private int offset;

    private final List<Integer> logIdList = new ArrayList<>();

    private int totalCount;

    public List<Integer> getLogIdList() {
        return logIdList;
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

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }
}
