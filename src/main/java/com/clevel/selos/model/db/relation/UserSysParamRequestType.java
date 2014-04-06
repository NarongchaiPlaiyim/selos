package com.clevel.selos.model.db.relation;

import com.clevel.selos.model.db.master.RequestType;
import com.clevel.selos.model.db.master.UserSysParameter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "rel_user_sys_param_req_type")
public class UserSysParamRequestType implements Serializable{
    @Id
    @Column(name = "id")
    private long id;

    @ManyToOne
    @JoinColumn(name = "user_sys_parameter_id")
    private UserSysParameter userSysParameter;

    @ManyToOne
    @JoinColumn(name = "request_type_id")
    private RequestType requestType;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public UserSysParameter getUserSysParameter() {
        return userSysParameter;
    }

    public void setUserSysParameter(UserSysParameter userSysParameter) {
        this.userSysParameter = userSysParameter;
    }

    public RequestType getRequestType() {
        return requestType;
    }

    public void setRequestType(RequestType requestType) {
        this.requestType = requestType;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("userSysParameter", userSysParameter)
                .append("requestType", requestType)
                .toString();
    }
}
