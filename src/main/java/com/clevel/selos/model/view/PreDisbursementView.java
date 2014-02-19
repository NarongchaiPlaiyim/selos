package com.clevel.selos.model.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.sun.xml.internal.xsom.impl.scd.Iterators.Map;

public class PreDisbursementView implements Serializable {
    private int approvedType = 0;
    
    private boolean selectedTest = false;
    private static String[] GROUP_NAMES = new String[] { "1", "2a", "2b" };
    
    private String remark;
    
    private Date submission_date;
    private Date return_date;
   
    private HashMap<String, List<PreDisbursementDetailView>> preDisBursementDetailViewMap;

    public PreDisbursementView() {
        init();
    }

    public void reset(){
        init();
    }

    private void init(){

    }

    public int getApprovedType() {
        return approvedType;
    }

    public void setApprovedType(int approvedType) {
        this.approvedType = approvedType;
    }
    
    public Date getSubmission_date() {
		return submission_date;
	}

    public void setSubmission_date(Date submission_date) {
		this.submission_date = submission_date;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Date getReturn_date() {
		return return_date;
	}

	public void setReturn_date(Date return_date) {
		this.return_date = return_date;
	}

	public HashMap<String, List<PreDisbursementDetailView>> getPreDisBursementDetailViewMap() {
		return preDisBursementDetailViewMap;
	}

	public void setPreDisBursementDetailViewMap(HashMap<String, List<PreDisbursementDetailView>> preDisBursementDetailViewMap) {
		this.preDisBursementDetailViewMap = preDisBursementDetailViewMap;
	}

	public boolean isSelectedTest() {
		return selectedTest;
	}

	public void setSelectedTest(boolean selectedTest) {
		this.selectedTest = selectedTest;
	}
}
