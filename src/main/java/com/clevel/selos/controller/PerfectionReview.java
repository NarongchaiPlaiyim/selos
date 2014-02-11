package com.clevel.selos.controller;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;

import com.clevel.selos.businesscontrol.BasicInfoControl;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.view.BasicInfoView;
import com.clevel.selos.model.view.PerfectionReviewView;
import com.clevel.selos.util.FacesUtil;
import com.clevel.selos.util.Util;

@ViewScoped
@ManagedBean(name="perfectionReview")
public class PerfectionReview implements Serializable {
	private static final long serialVersionUID = 1312705393960592543L;

	@Inject @SELOS
	private Logger log;
	
	@Inject
	private BasicInfoControl basicInfoControl;
	
	//Private variable
	private boolean preRenderCheck = false;
	private long workCaseId = -1;
	private long stepId = -1;
	
	private BasicInfoView basicInfoView;
	
	//Property
	private List<PerfectionReviewView> perfectionReviewList;
	
	public PerfectionReview() {
	}
	
	public List<PerfectionReviewView> getPerfectionReviewList() {
		return perfectionReviewList;
	}
	
	/*
	 * Action
	 */
	@PostConstruct
	private void init() {
		log.info("Construct");
		HttpSession session = FacesUtil.getSession(false);
		if (session != null) {
			workCaseId = Util.parseLong(session.getAttribute("workCaseId"), -1);
			stepId = Util.parseLong(session.getAttribute("stepId"), -1);
		}
		_loadInitData();
	}
	
	public void preRender() {
		if (preRenderCheck)
			return;
		preRenderCheck = true;
		
		String redirectPage = null;
		log.info("preRender workCase Id = "+workCaseId);
		if (workCaseId > 0) {
			//TODO Validate step 
			if (stepId <= 0) {
				redirectPage = "/site/inbox.jsf";
			} else {
				return;
			}
		}
		try {
			log.info("preRender "+redirectPage);
			if (redirectPage == null) {
				redirectPage = "/site/inbox.jsf";
			}
			ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
			ec.redirect(ec.getRequestContextPath()+redirectPage);
		} catch (IOException e) {
			log.error("Fail to redirect screen to "+redirectPage,e);
		}
	}
	
	/*
	 * Private method
	 */
	private void _loadInitData() {
		preRenderCheck = false;
		if (workCaseId > 0) {
			basicInfoView = basicInfoControl.getBasicInfo(workCaseId);
		}
		//Calculate result for each type
		perfectionReviewList = new ArrayList<PerfectionReviewView>();
		PerfectionReviewView contractReview = new PerfectionReviewView();
		contractReview.setType("Contract");
		contractReview.setDate(basicInfoView.getCreateDate());
		contractReview.setCompleteDate(null);
		contractReview.setStatus("On Hand");
		contractReview.setRemark("Remark");
		perfectionReviewList.add(contractReview);
		
		PerfectionReviewView pledgeReview = new PerfectionReviewView();
		pledgeReview.setType("Pledge");
		pledgeReview.setDate(basicInfoView.getCreateDate());
		pledgeReview.setCompleteDate(null);
		pledgeReview.setStatus("Completed");
		pledgeReview.setRemark("Remark");
		perfectionReviewList.add(pledgeReview);
		
		PerfectionReviewView mortgageReview = new PerfectionReviewView();
		mortgageReview.setType("Mortgage");
		mortgageReview.setDate(basicInfoView.getCreateDate());
		mortgageReview.setCompleteDate(null);
		mortgageReview.setStatus("On Hand");
		mortgageReview.setRemark("Remark");
		perfectionReviewList.add(mortgageReview);
		
		PerfectionReviewView tcgReview = new PerfectionReviewView();
		tcgReview.setType("TCG");
		tcgReview.setDate(basicInfoView.getCreateDate());
		tcgReview.setCompleteDate(null);
		tcgReview.setStatus("On Hand");
		tcgReview.setRemark("Remark");
		perfectionReviewList.add(tcgReview);
		
		PerfectionReviewView feeReview = new PerfectionReviewView();
		feeReview.setType("Fee Collection");
		feeReview.setDate(basicInfoView.getCreateDate());
		feeReview.setCompleteDate(null);
		feeReview.setStatus("On Hand");
		feeReview.setRemark("Remark");
		perfectionReviewList.add(feeReview);
	}
	
}
