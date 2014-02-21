package com.clevel.selos.transform;

import java.util.List;

import com.clevel.selos.model.db.working.Customer;
import com.clevel.selos.model.db.working.MortgageInfoCollSub;
import com.clevel.selos.model.db.working.NewCollateralSub;
import com.clevel.selos.model.db.working.NewCollateralSubOwner;
import com.clevel.selos.model.db.working.NewCollateralSubRelated;
import com.clevel.selos.model.view.MortgageInfoCollSubView;
import com.clevel.selos.util.Util;

public class MortgageInfoCollSubTransform extends Transform {
	
	private static final long serialVersionUID = -7832824672744362135L;

	public MortgageInfoCollSubView transformToView(MortgageInfoCollSub model) {
		MortgageInfoCollSubView view = new MortgageInfoCollSubView();
		if (model == null)
			return view;

		view.setId(model.getId());
		
		NewCollateralSub subModel = model.getNewCollateralSub();
		if (subModel == null)
			return view;
		view.setNewCollSubId(subModel.getId());
		view.setJobNo(subModel.getNewCollateralHead().getNewCollateral().getJobID());
		view.setHcNo(subModel.getNewCollateralHead().getCollID());
		view.setSubNo(subModel.getCollID());
		
		StringBuilder builder = new StringBuilder();
		List<NewCollateralSubOwner> owners = subModel.getNewCollateralSubOwnerList();
		if (owners != null && !owners.isEmpty()) {
			for (NewCollateralSubOwner owner : owners) {
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
		
		
		if (subModel.getCollateralTypeType() != null)
			builder.append(subModel.getCollateralTypeType().getDescription());
		if (subModel.getSubCollateralType() != null) {
			if (builder.length() > 0)
				builder.append(" - ");
			builder.append(subModel.getSubCollateralType().getDescription());
		}
		view.setCollateralType(builder.toString());
		builder.setLength(0);
		
		view.setTitleDeed(subModel.getTitleDeed());
		view.setMortgageDetail(subModel.getAddress());
		
		List<NewCollateralSubRelated> relateds = subModel.getNewCollateralSubRelatedList();
		if (relateds != null && !relateds.isEmpty()) {
			for (NewCollateralSubRelated related : relateds) {
				NewCollateralSub subRelated = related.getNewCollateralSubRelated();
				if (subRelated == null)
					continue;
				if (subRelated.getCollateralTypeType() != null)
					builder.append(subRelated.getCollateralTypeType().getDescription()).append(" ");
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
