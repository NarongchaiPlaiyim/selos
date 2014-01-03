package com.clevel.selos.integration.bpm.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class BPMInboxRecord {
    private int myboxRecord;
    private int returnBoxRecord;
    private int bdmUwBoxRecord;

    public int getMyboxRecord() {
        return myboxRecord;
    }

    public void setMyboxRecord(int myboxRecord) {
        this.myboxRecord = myboxRecord;
    }

    public int getReturnBoxRecord() {
        return returnBoxRecord;
    }

    public void setReturnBoxRecord(int returnBoxRecord) {
        this.returnBoxRecord = returnBoxRecord;
    }

    public int getBdmUwBoxRecord() {
        return bdmUwBoxRecord;
    }

    public void setBdmUwBoxRecord(int bdmUwBoxRecord) {
        this.bdmUwBoxRecord = bdmUwBoxRecord;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("myboxRecord", myboxRecord)
                .append("returnBoxRecord", returnBoxRecord)
                .append("bdmUwBoxRecord", bdmUwBoxRecord)
                .toString();
    }
}
