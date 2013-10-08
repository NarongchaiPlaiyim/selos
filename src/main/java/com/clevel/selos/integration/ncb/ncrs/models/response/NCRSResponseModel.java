package com.clevel.selos.integration.ncb.ncrs.models.response;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

@XStreamAlias("ncrsresponse")
public class NCRSResponseModel implements Serializable {

    @XStreamAlias("header")
    private HeaderModel header;

    @XStreamAlias("body")
    private BodyModel body;

    public HeaderModel getHeaderModel() {
        return header;
    }

    public BodyModel getBodyModel() {
        return body;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("header", header)
                .append("body", body)
                .toString();
    }
}

