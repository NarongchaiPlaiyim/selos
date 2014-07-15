package com.clevel.selos.transform;

import com.clevel.selos.model.db.working.*;
import com.clevel.selos.model.view.MortgageInfoCollSubView;
import com.clevel.selos.util.Util;

import java.util.List;

public class MortgageInfoCollSubTransform extends Transform {
	
	private static final long serialVersionUID = -7832824672744362135L;

	public MortgageInfoCollSubView transformToView(MortgageInfoCollSub model) {
		MortgageInfoCollSubView view = new MortgageInfoCollSubView();
		if (model == null)
			return view;

		view.setId(model.getId());
		
		ProposeCollateralInfoSub subModel = model.getNewCollateralSub();
		if (subModel == null)
			return view;
		view.setNewCollSubId(subModel.getId());
		view.setJobNo(subModel.getProposeCollateralHead().getProposeCollateral().getJobID());
        //todo:this
//		view.setHcNo(subModel.getProposeCollateralHead().getCollID());
//		view.setSubNo(subModel.getCollID());
		
		StringBuilder builder = new StringBuilder();
		List<ProposeCollateralSubOwner> owners = subModel.getProposeCollateralSubOwnerList();
		if (owners != null && !owners.isEmpty()) {
			for (ProposeCollateralSubOwner owner : owners) {
				Customer customer = owner.getCustomer();
				if (customer == null)
					continue;
				if (customer.getTitle() != null)
					builder.append(customer.getTitle().getTitleTh()).append(" ");
				builder.append(customer.getNameTh());
				if (!Util.isEmpty(customer.getLastNameTh()))
					builder.append(" ").append(customer.getLastNameTh());
				
				builder.append("<br/>");
			}
			builder.setLength(builder.length() - 5);
			view.setOwner(builder.toString());
			builder.setLength(0);
		} else {
			view.setOwner("-");
		}
		
		if (subModel.getSubCollateralType() != null)
			builder.append(subModel.getSubCollateralType().getDescription());
		if (subModel.getSubCollateralType() != null) {
			if (builder.length() > 0)
				builder.append(" - ");
			builder.append(subModel.getSubCollateralType().getDescription());
		}
		view.setCollateralType(builder.toString());
		builder.setLength(0);
		
		view.setTitleDeed(subModel.getTitleDeed());
		view.setMortgageDetail(subModel.getAddress());
		
		List<ProposeCollateralSubRelated> relateds = subModel.getProposeCollateralSubRelatedList();
		if (relateds != null && !relateds.isEmpty()) {
			for (ProposeCollateralSubRelated related : relateds) {
				ProposeCollateralInfoSub subRelated = related.getProposeCollateralSubRelated();
				if (subRelated == null)
					continue;
				if (subRelated.getSubCollateralType() != null)
					builder.append(subRelated.getSubCollateralType().getDescription()).append(" ");
				builder.append(subRelated.getTitleDeed());
				builder.append("<br/>");
			}
			if (builder.length() > 0)
				builder.setLength(builder.length()-5);
			view.setRelatedWith(builder.toString());
		}
		
		view.setAppraisalValue(subModel.getAppraisalValue());
		return view;
	}

}
