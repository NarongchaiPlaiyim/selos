package com.clevel.selos.transform;

import com.clevel.selos.dao.working.CustomerDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.Customer;
import com.clevel.selos.model.db.working.ExistingCreditFacility;
import com.clevel.selos.model.db.working.ExistingGuarantorDetail;
import com.clevel.selos.model.view.CustomerInfoView;
import com.clevel.selos.model.view.ExistingGuarantorDetailView;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.system.message.NormalMessage;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ExistingGuarantorDetailTransform extends Transform {
    @SELOS
    @Inject
    private Logger log;
    @Inject
    @NormalMessage
    Message msg;

    @Inject
    CustomerDAO customerDAO;

    @Inject
    CustomerTransform customerTransform;


    @Inject
    public ExistingGuarantorDetailTransform() {
    }

    public List<ExistingGuarantorDetail> transformsToModel(List<ExistingGuarantorDetailView> existingGuarantorDetailViewList, ExistingCreditFacility existingCreditFacility, User user) {

        List<ExistingGuarantorDetail> existingGuarantorDetailList = new ArrayList<ExistingGuarantorDetail>();
        ExistingGuarantorDetail existingGuarantorDetail;

        for (ExistingGuarantorDetailView existingGuarantorDetailView : existingGuarantorDetailViewList) {
            existingGuarantorDetail = new ExistingGuarantorDetail();
            if (existingGuarantorDetailView.getId() != 0) {
                existingGuarantorDetail.setCreateDate(existingGuarantorDetailView.getCreateDate());
                existingGuarantorDetail.setCreateBy(existingGuarantorDetailView.getCreateBy());
            } else { // id = 0 create new
                existingGuarantorDetail.setCreateDate(new Date());
                existingGuarantorDetail.setCreateBy(user);
            }
            existingGuarantorDetail.setModifyDate(new Date());
            existingGuarantorDetail.setModifyBy(user);
            if(existingGuarantorDetailView.getGuarantorName() != null){
                if(existingGuarantorDetailView.getGuarantorName().getId() == -1){
                    existingGuarantorDetail.setGuarantorName(null);
                } else {
                    Customer guarantor = customerDAO.findById(existingGuarantorDetailView.getGuarantorName().getId());
                    existingGuarantorDetail.setGuarantorName(guarantor);
                }
            } else {
                existingGuarantorDetail.setGuarantorName(null);
            }
            existingGuarantorDetail.setTcglgNo(existingGuarantorDetailView.getTcgLgNo());
            existingGuarantorDetail.setExistingCreditFacility(existingCreditFacility);
            existingGuarantorDetail.setTotalLimitGuaranteeAmount(existingGuarantorDetailView.getTotalLimitGuaranteeAmount());
            existingGuarantorDetail.setGuarantorCategory(existingGuarantorDetailView.getGuarantorCategory());
            existingGuarantorDetailList.add(existingGuarantorDetail);
        }

        return existingGuarantorDetailList;
    }

    public List<ExistingGuarantorDetailView> transformsToView(List<ExistingGuarantorDetail> existingGuarantorDetailList) {
        List<ExistingGuarantorDetailView> existingGuarantorDetailViews = new ArrayList<ExistingGuarantorDetailView>();
        ExistingGuarantorDetailView existingGuarantorDetailView;

        for (ExistingGuarantorDetail existingGuarantorDetail : existingGuarantorDetailList) {
            existingGuarantorDetailView = new ExistingGuarantorDetailView();
            existingGuarantorDetailView.setId(existingGuarantorDetail.getId());
            existingGuarantorDetailView.setNo(existingGuarantorDetail.getNo());
            existingGuarantorDetailView.setCreateDate(existingGuarantorDetail.getCreateDate());
            existingGuarantorDetailView.setCreateBy(existingGuarantorDetail.getCreateBy());
            existingGuarantorDetailView.setModifyDate(existingGuarantorDetail.getModifyDate());
            existingGuarantorDetailView.setModifyBy(existingGuarantorDetail.getModifyBy());
            if(existingGuarantorDetail.getGuarantorName() != null){
                CustomerInfoView guarantorView = customerTransform.transformToView(existingGuarantorDetail.getGuarantorName());
                existingGuarantorDetailView.setGuarantorName(guarantorView);
            } else {
                CustomerInfoView customerInfoView = new CustomerInfoView();
                customerInfoView.setId(-1);
                customerInfoView.setFirstNameTh(msg.get("app.select.tcg"));
                existingGuarantorDetailView.setGuarantorName(customerInfoView);
            }
            existingGuarantorDetailView.setGuarantorCategory(existingGuarantorDetail.getGuarantorCategory());
            existingGuarantorDetailView.setTcgLgNo(existingGuarantorDetail.getTcglgNo());
            existingGuarantorDetailView.setTotalLimitGuaranteeAmount(existingGuarantorDetail.getTotalLimitGuaranteeAmount());
            existingGuarantorDetailViews.add(existingGuarantorDetailView);
        }

        return existingGuarantorDetailViews;
    }
}
