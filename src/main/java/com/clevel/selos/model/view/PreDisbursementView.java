package com.clevel.selos.model.view;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


public class PreDisbursementView implements Serializable {
    private int approvedType = 0;
    
    private boolean selectedTest2_1 = false;
    private boolean selectedTest2_2 = false;
    
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

	public boolean isSelectedTest2_1() {
		List<PreDisbursementDetailView> preDisbursementDetailViewList = this.preDisBursementDetailViewMap.get("2.1");
		for (PreDisbursementDetailView preDisbursementDetailView : preDisbursementDetailViewList){
			if (preDisbursementDetailView.getValue() > 0)
				return true;
		}
		return selectedTest2_1;
	}

	public void setSelectedTest2_1(boolean selectedTest2_1) {
		List<PreDisbursementDetailView> preDisbursementDetailViewList = this.preDisBursementDetailViewMap.get("2.1");
		for (PreDisbursementDetailView preDisbursementDetailView : preDisbursementDetailViewList){
			preDisbursementDetailView.setSubmission_date(null);
			preDisbursementDetailView.setValue(0);
		}
		this.selectedTest2_1 = selectedTest2_1;
	}

	public boolean isSelectedTest2_2() {
		List<PreDisbursementDetailView> preDisbursementDetailViewList = this.preDisBursementDetailViewMap.get("2.2");
		for (PreDisbursementDetailView preDisbursementDetailView : preDisbursementDetailViewList){
			if (preDisbursementDetailView.getValue() > 0)
				return true;
		}
		return selectedTest2_2;
	}

	public void setSelectedTest2_2(boolean selectedTest2_2) {
		List<PreDisbursementDetailView> preDisbursementDetailViewList = this.preDisBursementDetailViewMap.get("2.2");
		for (PreDisbursementDetailView preDisbursementDetailView : preDisbursementDetailViewList){
			preDisbursementDetailView.setSubmission_date(null);
			preDisbursementDetailView.setValue(0);
		}
		this.selectedTest2_2 = selectedTest2_2;
	}
}
