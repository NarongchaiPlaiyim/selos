package com.clevel.selos.transform;

import com.clevel.selos.model.db.working.AgreementInfo;
import com.clevel.selos.model.view.ApproveDetailInformationView;

import java.util.Calendar;

public class ApproveDetailInformationTransform extends Transform {
	
	private static final long serialVersionUID = -2268338691808292928L;

	public ApproveDetailInformationView transformToView(AgreementInfo agreementInfo) {
		ApproveDetailInformationView approveDetailInformationView = new ApproveDetailInformationView();
    	if (agreementInfo != null){
    		approveDetailInformationView.setSigningDate(agreementInfo.getLoanContractDate());
    		Calendar calendar = Calendar.getInstance();
    		if (agreementInfo.getFirstPaymentDate() != null){
    			approveDetailInformationView.setFirstPaymentDate(agreementInfo.getFirstPaymentDate());
    		}
    		if (agreementInfo.getLoanContractDate() != null){
	    		calendar.setTime(agreementInfo.getLoanContractDate());
	    		if ( calendar.get(Calendar.DAY_OF_MONTH) < 16 ){
	    			calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DAY_OF_MONTH) ); 
	    			approveDetailInformationView.setFirstPaymentDate(calendar.getTime());
	    		}else{
	    			calendar.add(Calendar.MONTH, 1);
	    			calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DAY_OF_MONTH) ); 
	    			approveDetailInformationView.setFirstPaymentDate(calendar.getTime());
	    		}
    		}
    		if (agreementInfo.getPayDate() > 0){
    			approveDetailInformationView.setPayDate(agreementInfo.getPayDate());
    		}else{
    			approveDetailInformationView.setPayDate(31);
    		}
    		
    	}
		return approveDetailInformationView;
	}
}
