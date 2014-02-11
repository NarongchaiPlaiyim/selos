package com.clevel.selos.transform;

import com.clevel.selos.model.db.working.TCGInfo;
import com.clevel.selos.model.db.working.WorkCase;
import com.clevel.selos.model.view.TCGInfoView;

public class TCGInfoTransform extends Transform {
	
	private static final long serialVersionUID = -2268338691808292928L;

	public TCGInfoView transformToView(TCGInfo tcgInfo) {
		TCGInfoView view = new TCGInfoView();
		if (tcgInfo != null) {
//			view.setId(tcgInfo.getId());
//			view.setApproveDate(tcgInfo.getApproveDate());
//			view.setApprovedResult(tcgInfo.getApprovedResult());
//			view.setPayinSlipSendDate(tcgInfo.getPayinSlipSendDate());
//			view.setReceiveTCGSlip(tcgInfo.getReceiveTCGSlip());
//			view.setTcgSubmitDate(tcgInfo.getTcgSubmitDate());
		}
		return view;
	}
	public TCGInfo transformToNewModel(TCGInfoView view,WorkCase workCase) {
		TCGInfo model = new TCGInfo();
//		model.setApproveDate(view.getApproveDate());
//		model.setApprovedResult(view.getApprovedResult());
//		model.setPayinSlipSendDate(view.getPayinSlipSendDate());
//		model.setReceiveTCGSlip(view.getReceiveTCGSlip());
//		model.setTcgSubmitDate(view.getTcgSubmitDate());
//		model.setWorkCase(workCase);
		return model;
	}
}
