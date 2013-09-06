package com.clevel.selos.model.db.audit;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "adt_brms_activity")
public class BRMSActivity {
    @Id
    @SequenceGenerator(name="SEQ_ADT_BRMS_ID", sequenceName="SEQ_ADT_BRMS_ID", allocationSize=1)
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="SEQ_ADT_BRMS_ID")
    private long id;
    @Column(name="requester", nullable=false)
    private String requester;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="request_time", nullable=false)
    private Date requestTime;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="response_time")
    private Date responseTime;
    @Column(name = "link_key")
    private String linkKey;
    @Column(name = "result",length = 20)
    private String result;
    @Column(name = "result_detail",length = 200)
    private String resultDetail;

    public BRMSActivity() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getRequester() {
        return requester;
    }

    public void setRequester(String requester) {
        this.requester = requester;
    }

    public Date getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(Date requestTime) {
        this.requestTime = requestTime;
    }

    public Date getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(Date responseTime) {
        this.responseTime = responseTime;
    }

    public String getLinkKey() {
        return linkKey;
    }

    public void setLinkKey(String linkKey) {
        this.linkKey = linkKey;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getResultDetail() {
        return resultDetail;
    }

    public void setResultDetail(String resultDetail) {
        this.resultDetail = resultDetail;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).
                append("id", id).
                append("requester", requester).
                append("requestTime", requestTime).
                append("responseTime", responseTime).
                append("linkKey", linkKey).
                append("result", result).
                append("resultDetail", resultDetail).
                toString();
    }
}
