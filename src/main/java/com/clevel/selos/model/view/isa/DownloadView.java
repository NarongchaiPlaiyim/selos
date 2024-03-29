package com.clevel.selos.model.view.isa;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.util.Date;

public class DownloadView implements Serializable{
    private Date date;
    private String fullPath;
    private String fileName;

    public DownloadView(Date date, String fullPath, String fileName) {
        this.date = date;
        this.fullPath = fullPath;
        this.fileName = fileName;
    }

    public Date getDate() {
        return date;
    }

    public String getFullPath() {
        return fullPath;
    }

    public String getFileName() {
        return fileName;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("date", date)
                .append("fullPath", fullPath)
                .append("fileName", fileName)
                .toString();
    }
}
