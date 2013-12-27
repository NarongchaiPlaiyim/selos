package com.clevel.selos.transform;

import com.clevel.selos.dao.master.CollateralTypeDAO;
import com.clevel.selos.dao.master.SubCollateralTypeDAO;
import com.clevel.selos.dao.working.BasicInfoDAO;
import com.clevel.selos.dao.working.CustomerDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.ExistingCollateralDetail;
import com.clevel.selos.model.db.working.ExistingCreditFacility;
import com.clevel.selos.model.view.ExistingCollateralDetailView;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ExistingCollateralDetailTransform extends Transform {
    @Inject
    @SELOS
    Logger log;

    public List<ExistingCollateralDetail> transformsToModel(List<ExistingCollateralDetailView> existingCollateralDetailViewList, ExistingCreditFacility existingCreditFacility, User user) {
        List<ExistingCollateralDetail> existingCollateralDetailList = new ArrayList<ExistingCollateralDetail>();
        ExistingCollateralDetail existingCollateralDetail;

        for (ExistingCollateralDetailView existingCollateralDetailView : existingCollateralDetailViewList) {
            existingCollateralDetail = new ExistingCollateralDetail();

            if (existingCollateralDetailView.getId() != 0) {
                //existingCollateralDetail.setId(existingCollateralDetailView.getId());
                existingCollateralDetail.setCreateDate(existingCollateralDetailView.getCreateDate());
                existingCollateralDetail.setCreateBy(existingCollateralDetailView.getCreateBy());
            } else { // id = 0 create new
                existingCollateralDetail.setCreateDate(new Date());
                existingCollateralDetail.setCreateBy(user);
            }

            existingCollateralDetail.setNo(existingCollateralDetailView.getNo());
            existingCollateralDetail.setBorrowerType(existingCollateralDetailView.getBorrowerType());
            existingCollateralDetail.setCollateralType(existingCollateralDetailView.getCollateralType());
            existingCollateralDetail.setPotentialCollateral(existingCollateralDetailView.getPotentialCollateral());
            existingCollateralDetail.setRelation(existingCollateralDetailView.getRelation());
            existingCollateralDetail.setMortgageType(existingCollateralDetailView.getMortgageType());
            existingCollateralDetail.setOwner(existingCollateralDetailView.getOwner());
            existingCollateralDetail.setAccountNumber(existingCollateralDetailView.getAccountNumber());
            existingCollateralDetail.setCollateralLocation(existingCollateralDetailView.getCollateralLocation());
            existingCollateralDetail.setRemark(existingCollateralDetailView.getRemark());
            existingCollateralDetail.setAppraisalDate(existingCollateralDetailView.getAppraisalDate());
            existingCollateralDetail.setAppraisalValue(existingCollateralDetailView.getAppraisalValue());
            existingCollateralDetail.setMortgageValue(existingCollateralDetailView.getMortgageValue());
            existingCollateralDetail.setExistingCreditFacility(existingCreditFacility);
            existingCollateralDetail.setModifyDate(new Date());
            existingCollateralDetail.setModifyBy(user);

            existingCollateralDetailList.add(existingCollateralDetail);
        }
        return existingCollateralDetailList;
    }

    public List<ExistingCollateralDetailView> transformsToView(List<ExistingCollateralDetail> existingCollateralDetailList) {
        List<ExistingCollateralDetailView> existingCollateralDetailViewList = new ArrayList<ExistingCollateralDetailView>();
        ExistingCollateralDetailView existingCollateralDetailView;

        for (ExistingCollateralDetail existingCollateralDetail : existingCollateralDetailList) {
            existingCollateralDetailView = new ExistingCollateralDetailView();
            existingCollateralDetailView.setId(existingCollateralDetail.getId());
            existingCollateralDetailView.setNo(existingCollateralDetail.getNo());
            existingCollateralDetailView.setBorrowerType(existingCollateralDetail.getBorrowerType());
            existingCollateralDetailView.setCollateralType(existingCollateralDetail.getCollateralType());
            existingCollateralDetailView.setPotentialCollateral(existingCollateralDetail.getPotentialCollateral());
            existingCollateralDetailView.setRelation(existingCollateralDetail.getRelation());
            existingCollateralDetailView.setMortgageType(existingCollateralDetail.getMortgageType());
            existingCollateralDetailView.setOwner(existingCollateralDetail.getOwner());
            existingCollateralDetailView.setAccountNumber(existingCollateralDetail.getAccountNumber());
            existingCollateralDetailView.setCollateralLocation(existingCollateralDetail.getCollateralLocation());
            existingCollateralDetailView.setRemark(existingCollateralDetail.getRemark());
            existingCollateralDetailView.setAppraisalDate(existingCollateralDetail.getAppraisalDate());
            existingCollateralDetailView.setAppraisalValue(existingCollateralDetail.getAppraisalValue());
            existingCollateralDetailView.setMortgageValue(existingCollateralDetail.getMortgageValue());
            existingCollateralDetailView.setModifyDate(existingCollateralDetail.getModifyDate());
            existingCollateralDetail.setModifyBy(existingCollateralDetail.getModifyBy());
            existingCollateralDetailView.setCreateDate(existingCollateralDetail.getCreateDate());
            existingCollateralDetailView.setCreateBy(existingCollateralDetail.getCreateBy());

            existingCollateralDetailViewList.add(existingCollateralDetailView);
        }
        return existingCollateralDetailViewList;
    }

}
