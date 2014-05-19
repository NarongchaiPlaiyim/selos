package com.clevel.selos.businesscontrol;

import com.clevel.selos.dao.master.PreDisbursementDataDAO;
import com.clevel.selos.dao.working.PreDisbursementDAO;
import com.clevel.selos.dao.working.PreDisbursementDetailDAO;
import com.clevel.selos.dao.working.WorkCaseDAO;
import com.clevel.selos.integration.BPMInterface;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.PreDisbursementData;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.PreDisbursement;
import com.clevel.selos.model.db.working.PreDisbursementDetail;
import com.clevel.selos.model.view.PreDisbursementDetailView;
import com.clevel.selos.model.view.PreDisbursementView;
import com.clevel.selos.transform.PreDisbursementTransform;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.*;

@Stateless
public class PreDisbursementControl extends BusinessControl {
	@Inject
	@SELOS
	private Logger log;

	@Inject
	BPMInterface bpmInterface;

	@Inject
	WorkCaseDAO workCaseDAO;

	@Inject
	PreDisbursementDataDAO preDisbursementDatatDAO;

	@Inject
	PreDisbursementDAO preDisbursementDAO;

	@Inject
	PreDisbursementTransform preDisbursementTransform;
	
	@Inject
	PreDisbursementDetailDAO preDisbursementDetailDAO;

	@Inject
	public PreDisbursementControl() {

	}

	public PreDisbursementView getPreDisbursementView(long workcase_id) {
		// PreDisBursementDetailView preDisBursementDetailView
		Set<String> groupNameSet = preDisbursementDatatDAO.findGropuName();
		PreDisbursementView preDisbursementView = new PreDisbursementView();
		PreDisbursement preDisbursement = preDisbursementDAO.findPreDisbursementByWorkcaseId(workcase_id);
		if (preDisbursement != null) {
			preDisbursementView = preDisbursementTransform.transformToPreDisbursementView(preDisbursement);

			// Get Detail
			List<PreDisbursementDetail> preDisbursementDetailList = preDisbursement.getPreDisbursementDetailList();
			if (preDisbursementDetailList != null) {
				Iterator<String> it = groupNameSet.iterator();
				HashMap<String, List<PreDisbursementDetailView>> preDisBursementDetailViewMap = new HashMap<String, List<PreDisbursementDetailView>>();
				while (it.hasNext()) {
					String groupName = it.next();
					List<PreDisbursementDetailView> preDisbursementDetailViewList = preDisbursementTransform
							.transformPreDisbursementDetailList(groupName, preDisbursementDetailList);
					
					preDisBursementDetailViewMap.put(groupName, preDisbursementDetailViewList);
				}
				preDisbursementView.setPreDisBursementDetailViewMap(preDisBursementDetailViewMap);
			}
		} else {
			HashMap<String, List<PreDisbursementDetailView>> preDisBursementDetailViewMap = new HashMap<String, List<PreDisbursementDetailView>>();
			Iterator<String> it = groupNameSet.iterator();
			while (it.hasNext()) {
				String groupName = it.next();
				List<PreDisbursementData> preDisbursementDataList = preDisbursementDatatDAO.findPreDisbursementDataByGropuName(groupName);
				List<PreDisbursementDetailView> preDisbursementDetailViewList = preDisbursementTransform.transformPreDisbursementDataList(
						groupName, preDisbursementDataList);
				preDisBursementDetailViewMap.put(groupName, preDisbursementDetailViewList);
			}
			preDisbursementView.setPreDisBursementDetailViewMap(preDisBursementDetailViewMap);
		}

		return preDisbursementView;
	}

	public void savePreDisbursement(PreDisbursementView preDisbursementView, long workcase_id, User user) {
		PreDisbursement preDisbursement = preDisbursementDAO.findPreDisbursementByWorkcaseId(workcase_id);
		if (preDisbursement != null) {
			preDisbursement.setRemark(preDisbursementView.getRemark());
			preDisbursement.setModifyBy(user);
			preDisbursement.setModifyDate(new Date());
			preDisbursementDAO.persist(preDisbursement);
		
		}else{
			preDisbursement = new PreDisbursement();
			preDisbursement.setRemark(preDisbursementView.getRemark());
			preDisbursement.setCreateBy(user);
			preDisbursement.setCreateDate(new Date());
			preDisbursement.setWorkCase(workCaseDAO.findById(workcase_id));
			preDisbursementDAO.persist(preDisbursement);
		}
		for (String key : preDisbursementView.getPreDisBursementDetailViewMap().keySet()){
			this.savePerDisbursementDetail(preDisbursement, preDisbursementView.getPreDisBursementDetailViewMap().get(key));
		}
	}
	
	public void savePerDisbursementDetail(PreDisbursement preDisbursement, List<PreDisbursementDetailView> preDisbursementDetailViewList){
		for (PreDisbursementDetailView preDisbursementDetailView : preDisbursementDetailViewList){
			if (preDisbursementDetailView.getId() > 0){
				PreDisbursementDetail preDisbursementDetail = preDisbursementDetailDAO.findById(preDisbursementDetailView.getId());
				if (preDisbursementDetailView.getValue() != preDisbursementDetail.getValue()){
					preDisbursementDetail.setValue(preDisbursementDetailView.getValue());
					preDisbursementDetail.setSubmission_date(new Date());
				}
				preDisbursementDetailDAO.persist(preDisbursementDetail);
			}else{
				PreDisbursementDetail preDisbursementDetail = new PreDisbursementDetail();
				preDisbursementDetail.setValue(preDisbursementDetailView.getValue());
				if (preDisbursementDetailView.getValue() != 0){
					preDisbursementDetail.setSubmission_date(new Date());
				}
				preDisbursementDetail.setPreDisbursementData(preDisbursementDatatDAO.findById(preDisbursementDetailView.getPreDisbursementData_id()));
				preDisbursementDetail.setPreDisbursement(preDisbursement);
				preDisbursementDetailDAO.persist(preDisbursementDetail);
			}
		}
	}

}
