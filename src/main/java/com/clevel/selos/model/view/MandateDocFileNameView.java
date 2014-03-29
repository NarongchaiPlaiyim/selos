package com.clevel.selos.model.view;

import com.clevel.selos.model.db.working.MandateDoc;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

public class MandateDocFileNameView implements Serializable {
    private long id;
    private String fileName;
    private String url;
    private MandateDoc mandateDoc;

    public MandateDocFileNameView() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public MandateDoc getMandateDoc() {
        return mandateDoc;
    }

    public void setMandateDoc(MandateDoc mandateDoc) {
        this.mandateDoc = mandateDoc;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("fileName", fileName)
                .append("url", url)
                .toString();
    }
}
