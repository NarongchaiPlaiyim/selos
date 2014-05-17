package com.clevel.selos.controller;

import java.io.Serializable;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import com.clevel.selos.model.*;
import com.clevel.selos.model.db.master.MandateFieldCondition;

@ApplicationScoped
@ManagedBean(name="enumGeneralControl")
public class EnumGeneralControl implements Serializable{
	
	private static final long serialVersionUID = -5460993971781022624L;
	public ApproveType[] getApproveTypes() {
		return ApproveType.displayList();
	}
	public ApproveResult[] getApproveResults() {
		return ApproveResult.displayList();
	}
	public Gender[] getGenders() {
		return Gender.displayList();
	}
	public MortgageSignLocationType[] getMortgageSignLocations() {
		return MortgageSignLocationType.displayList();
	}
	public AttorneyRelationType[] getAttorneyRelationTypes() {
		return AttorneyRelationType.displayList();
	}
	public BAPAType[] getBAPATypes() {
		return BAPAType.displayList();
	}
	public RadioValue[] getYesNoEnum() {
		return RadioValue.displayListYesNo();
	}
	public ConfirmAccountType[] getConfirmAccountTypes() {
		return ConfirmAccountType.displayList();
	}
	public RequestAccountType[] getRequestAccountTypes() {
		return RequestAccountType.displayList();
	}
	public MortgageConfirmedType[] getMortgageConfirmedTypes() {
		return MortgageConfirmedType.displayList();
	}
    public MandateConditionType[] getMandateConditionType(){
        return MandateConditionType.values();
    }
    public MandateDependType[] getMandateDependType(){
        return MandateDependType.values();
    }
}
