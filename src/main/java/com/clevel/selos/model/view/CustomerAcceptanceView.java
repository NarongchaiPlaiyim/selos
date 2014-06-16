package com.clevel.selos.model.view;

import com.clevel.selos.model.db.master.User;

import java.io.Serializable;
import java.util.Date;

public class CustomerAcceptanceView implements Serializable {

    private static final long serialVersionUID = 6141918378692217237L;
	private long id;
	private int approveResult;
    private Date createDate;
    private Date modifyDate;
    private User createBy;
    private User modifyBy;
    
    //Zone manager
    private String zoneMgrName;
    private String zoneMgrTel;
    private String zoneMgrEmail;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getApproveResult() {
        return approveResult;
    }

    public void setApproveResult(int approveResult) {
        this.approveResult = approveResult;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }

    public User getCreateBy() {
        return createBy;
    }

    public void setCreateBy(User createBy) {
        this.createBy = createBy;
    }

    public User getModifyBy() {
        return modifyBy;
    }

    public void setModifyBy(User modifyBy) {
        this.modifyBy = modifyBy;
    }
    
    public String getZoneMgrEmail() {
		return zoneMgrEmail;
	}
    public String getZoneMgrName() {
		return zoneMgrName;
	}
    public String getZoneMgrTel() {
		return zoneMgrTel;
	}
    public void setZoneMgrEmail(String zoneMgrEmail) {
		this.zoneMgrEmail = zoneMgrEmail;
	}
    public void setZoneMgrName(String zoneMgrName) {
		this.zoneMgrName = zoneMgrName;
	}
    public void setZoneMgrTel(String zoneMgrTel) {
		this.zoneMgrTel = zoneMgrTel;
	}

}
