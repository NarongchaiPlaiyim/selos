package com.clevel.selos.transform;

import com.clevel.selos.dao.working.ExistingCreditFacilityDAO;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.ExistingCreditFacility;
import com.clevel.selos.model.db.working.WorkCase;
import com.clevel.selos.model.view.ExistingCreditFacilityView;

import javax.inject.Inject;
import java.util.Date;

public class ExistingCreditFacilityTransform extends Transform {
    @Inject
    ExistingCreditFacilityDAO existingCreditFacilityDAO;

    @Inject
    public ExistingCreditFacilityTransform() {}


    public ExistingCreditFacility transformsToModelDB(ExistingCreditFacilityView existingCreditFacilityView, WorkCase workCase, User user) {

        ExistingCreditFacility existingCreditFacility = new ExistingCreditFacility();

        existingCreditFacility.setWorkCase(workCase);

        if (existingCreditFacilityView.getId() != 0) {
            existingCreditFacility = existingCreditFacilityDAO.findById(existingCreditFacilityView.getId());
        } else { // id = 0 create new
            existingCreditFacility.setCreateDate(new Date());
            existingCreditFacility.setCreateBy(user);
        }

        existingCreditFacility.setModifyDate(new Date());
        existingCreditFacility.setModifyBy(user);

        existingCreditFacility.setTotalBorrowerComLimit(existingCreditFacilityView.getTotalBorrowerComLimit());
        existingCreditFacility.setTotalBorrowerRetailLimit(existingCreditFacilityView.getTotalBorrowerRetailLimit());
        existingCreditFacility.setTotalBorrowerAppInRLOSLimit(existingCreditFacilityView.getTotalBorrowerAppInRLOSLimit());

        existingCreditFacility.setTotalRelatedComLimit(existingCreditFacilityView.getTotalRelatedComLimit());
        existingCreditFacility.setTotalRelatedRetailLimit(existingCreditFacilityView.getTotalRelatedRetailLimit());
        existingCreditFacility.setTotalRelatedAppInRLOSLimit(existingCreditFacilityView.getTotalRelatedAppInRLOSLimit());

        existingCreditFacility.setTotalBorrowerCom(existingCreditFacilityView.getTotalBorrowerCom());
        existingCreditFacility.setTotalBorrowerComOBOD(existingCreditFacilityView.getTotalBorrowerComOBOD());
        existingCreditFacility.setTotalBorrowerExposure(existingCreditFacilityView.getTotalBorrowerExposure());

        existingCreditFacility.setTotalRelatedCom(existingCreditFacilityView.getTotalRelatedCom());
        existingCreditFacility.setTotalRelatedComOBOD(existingCreditFacilityView.getTotalRelatedComOBOD());
        existingCreditFacility.setTotalRelatedExposure(existingCreditFacilityView.getTotalRelatedExposure());

        existingCreditFacility.setTotalGroupCom(existingCreditFacilityView.getTotalGroupCom());
        existingCreditFacility.setTotalGroupComOBOD(existingCreditFacilityView.getTotalGroupComOBOD());
        existingCreditFacility.setTotalGroupExposure(existingCreditFacilityView.getTotalGroupExposure());

        existingCreditFacility.setTotalBorrowerAppraisalValue(existingCreditFacilityView.getTotalBorrowerAppraisalValue());
        existingCreditFacility.setTotalBorrowerMortgageValue(existingCreditFacilityView.getTotalBorrowerMortgageValue());

        existingCreditFacility.setTotalRelatedAppraisalValue(existingCreditFacilityView.getTotalRelatedAppraisalValue());
        existingCreditFacility.setTotalRelatedMortgageValue(existingCreditFacilityView.getTotalRelatedMortgageValue());

        existingCreditFacility.setTotalGuaranteeAmount(existingCreditFacilityView.getTotalGuaranteeAmount());

        existingCreditFacility.setTotalBorrowerNumberOfExistingOD(existingCreditFacilityView.getTotalBorrowerNumberOfExistingOD());
        existingCreditFacility.setTotalBorrowerODLimit(existingCreditFacilityView.getTotalBorrowerExistingODLimit());
        
        return existingCreditFacility;
    }

    public ExistingCreditFacilityView transformsToView(ExistingCreditFacility existingCreditFacility) {
        ExistingCreditFacilityView existingCreditFacilityView = new ExistingCreditFacilityView();
        if (existingCreditFacility == null) {
            return existingCreditFacilityView;
        }

        existingCreditFacilityView.setId(existingCreditFacility.getId());
        existingCreditFacilityView.setCreateDate(existingCreditFacility.getCreateDate());
        existingCreditFacilityView.setCreateBy(existingCreditFacility.getCreateBy());
        existingCreditFacilityView.setModifyDate(existingCreditFacility.getModifyDate());
        existingCreditFacilityView.setModifyBy(existingCreditFacility.getModifyBy());
        existingCreditFacilityView.setTotalBorrowerComLimit(existingCreditFacility.getTotalBorrowerComLimit());
        existingCreditFacilityView.setTotalBorrowerRetailLimit(existingCreditFacility.getTotalBorrowerRetailLimit());
        existingCreditFacilityView.setTotalBorrowerAppInRLOSLimit(existingCreditFacility.getTotalBorrowerAppInRLOSLimit());

        existingCreditFacilityView.setTotalRelatedComLimit(existingCreditFacility.getTotalRelatedComLimit());
        existingCreditFacilityView.setTotalRelatedRetailLimit(existingCreditFacility.getTotalRelatedRetailLimit());
        existingCreditFacilityView.setTotalRelatedAppInRLOSLimit(existingCreditFacility.getTotalRelatedAppInRLOSLimit());

        existingCreditFacilityView.setTotalBorrowerCom(existingCreditFacility.getTotalBorrowerCom());
        existingCreditFacilityView.setTotalBorrowerComOBOD(existingCreditFacility.getTotalBorrowerComOBOD());
        existingCreditFacilityView.setTotalBorrowerExposure(existingCreditFacility.getTotalBorrowerExposure());

        existingCreditFacilityView.setTotalRelatedCom(existingCreditFacility.getTotalRelatedCom());
        existingCreditFacilityView.setTotalRelatedComOBOD(existingCreditFacility.getTotalRelatedComOBOD());
        existingCreditFacilityView.setTotalRelatedExposure(existingCreditFacility.getTotalRelatedExposure());

        existingCreditFacilityView.setTotalGroupCom(existingCreditFacility.getTotalGroupCom());
        existingCreditFacilityView.setTotalGroupComOBOD(existingCreditFacility.getTotalGroupComOBOD());
        existingCreditFacilityView.setTotalGroupExposure(existingCreditFacility.getTotalGroupExposure());

        existingCreditFacilityView.setTotalBorrowerAppraisalValue(existingCreditFacility.getTotalBorrowerAppraisalValue());
        existingCreditFacilityView.setTotalBorrowerMortgageValue(existingCreditFacility.getTotalBorrowerMortgageValue());

        existingCreditFacilityView.setTotalRelatedAppraisalValue(existingCreditFacility.getTotalRelatedAppraisalValue());
        existingCreditFacilityView.setTotalRelatedMortgageValue(existingCreditFacility.getTotalRelatedMortgageValue());

        existingCreditFacilityView.setTotalGuaranteeAmount(existingCreditFacility.getTotalGuaranteeAmount());

        existingCreditFacilityView.setTotalBorrowerNumberOfExistingOD(existingCreditFacility.getTotalBorrowerNumberOfExistingOD());
        existingCreditFacilityView.setTotalBorrowerExistingODLimit(existingCreditFacility.getTotalBorrowerODLimit());

        return existingCreditFacilityView;
    }

}
