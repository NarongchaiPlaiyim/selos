package com.clevel.selos.controller;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import com.clevel.selos.model.ApproveResult;
import com.clevel.selos.model.ApproveType;

@RequestScoped
@ManagedBean(name="enumGeneralControl")
public class EnumGeneralControl implements Serializable{
	
	private static final long serialVersionUID = -5460993971781022624L;
	public ApproveType[] getApproveTypes() {
		return ApproveType.displayList();
	}
	public ApproveResult[] getApproveResults() {
		return ApproveResult.displayList();
	}
}
