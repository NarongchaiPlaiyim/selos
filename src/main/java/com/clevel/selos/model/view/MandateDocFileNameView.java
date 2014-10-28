package com.clevel.selos.model.view;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

public class MandateDocFileNameView implements Serializable {
    private long id;
    private String ecmDocId;
    private String fnDocId;
    private String fileName;
    private String url;

    public MandateDocFileNameView() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEcmDocId() {
        return ecmDocId;
    }

    public void setEcmDocId(String ecmDocId) {
        this.ecmDocId = ecmDocId;
    }

    public String getFnDocId() {
        return fnDocId;
    }

    public void setFnDocId(String fnDocId) {
        this.fnDocId = fnDocId;
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

    public void updateValues(MandateDocFileNameView view){
        id = view.id;
        ecmDocId = view.ecmDocId;
        fileName = view.fileName;
        url = view.url;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("ecmDocId", ecmDocId)
                .append("fnDocId", fnDocId)
                .append("fileName", fileName)
                .append("url", url)
                .toString();
    }
}
