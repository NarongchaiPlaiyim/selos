package com.clevel.selos.model.db.audit;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "adt_ext_rm")
public class RMAudit implements Serializable {
    @Id
    @SequenceGenerator(name="SEQ_ADT_EXT_RM_ID", sequenceName="SEQ_ADT_EXT_RM_ID", allocationSize=1)
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="SEQ_ADT_EXT_RM_ID")
    private long id;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="request_time", nullable=false)
    private Date requestTime;
    @Column(name = "request_msg",length = 2000)
    private String requestMsg;
    @Column(name = "response_msg",length = 2000)
    private String responseMsg;
    @Column(name = "result",length = 20)
    private String result;
    @Column(name = "result_datail",length = 2000)
    private String resultDetail;

    public RMAudit() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(Date requestTime) {
        this.requestTime = requestTime;
    }

    public String getRequestMsg() {
        return requestMsg;
    }

    public void setRequestMsg(String requestMsg) {
        this.requestMsg = requestMsg;
    }

    public String getResponseMsg() {
        return responseMsg;
    }

    public void setResponseMsg(String responseMsg) {
        this.responseMsg = responseMsg;
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
                append("requestTime", requestTime).
                append("requestMsg", requestMsg).
                append("responseMsg", responseMsg).
                append("result", result).
                append("resultDetail", resultDetail).
                toString();
    }
}
