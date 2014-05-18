package com.clevel.selos.controller;

import com.clevel.selos.businesscontrol.PerfectionReviewControl;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.view.PerfectionReviewView;
import com.clevel.selos.util.FacesUtil;
import com.clevel.selos.util.Util;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@ViewScoped
@ManagedBean(name="perfectionReview")
public class PerfectionReview implements Serializable {
	private static final long serialVersionUID = 1312705393960592543L;

	@Inject @SELOS
	private Logger log;

	@Inject
	private PerfectionReviewControl perfectionReviewControl;
	//Private variable
	private boolean preRenderCheck = false;
	private long workCaseId = -1;
	private long stepId = -1;
	private Date lastUpdateDateTime;
	private String lastUpdateBy;
	//Property
	private List<PerfectionReviewView> perfectionReviews;
	
	public PerfectionReview() {
	}
	public String getLastUpdateBy() {
		return lastUpdateBy;
	}
	public Date getLastUpdateDateTime() {
		return lastUpdateDateTime;
	}
	public List<PerfectionReviewView> getPerfectionReviews() {
		return perfectionReviews;
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
		if (workCaseId > 0) {
			//TODO Validate step 
			if (stepId <= 0) {
				redirectPage = "/site/inbox.jsf";
			} else {
				return;
			}
		}
		try {
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
			
		}
		
		perfectionReviews = perfectionReviewControl.getPerfectionReviews(workCaseId);
		lastUpdateDateTime = new Date(0);
		lastUpdateBy = null;
		for (PerfectionReviewView view : perfectionReviews) {
			if (view.getModifyDate() == null)
				continue;
			if (lastUpdateDateTime.after(view.getModifyDate())) {
				lastUpdateDateTime = view.getModifyDate();
				lastUpdateBy = view.getModifyUser();
			}
		}
		if (lastUpdateDateTime.getTime() == 0)
			lastUpdateDateTime = null;
	}
	
}
