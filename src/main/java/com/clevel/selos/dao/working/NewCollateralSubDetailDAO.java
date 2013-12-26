package com.clevel.selos.dao.working;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.working.*;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;

public class NewCollateralSubDetailDAO extends GenericDAO<NewCollateralSubDetail, Long> {
    @Inject
    @SELOS
    Logger log;

    @Inject
    public NewCollateralSubDetailDAO() {}

    @Inject
    WorkCaseDAO workCaseDAO;

    @Inject
    NewCreditFacilityDAO newCreditFacilityDAO;

    public List<NewCollateralSubDetail> getAllSubCollateralThisWorkCase(long workCaseId) {
        log.info("findAllSubCollThisWorkCase :: start :: {}", workCaseId);
        WorkCase workCase = workCaseDAO.findById(workCaseId);
        List<NewCollateralSubDetail> newCollateralSubDetailList = null;
        if (workCase != null) {
            NewCreditFacility newCreditFacility = newCreditFacilityDAO.findByWorkCase(workCase);
            if (newCreditFacility != null) {
                if (newCreditFacility.getNewCollateralDetailList() != null) {
                    log.info("newCreditFacility.getNewCollateralDetailList() :: {}", newCreditFacility.getNewCollateralDetailList().size());
                    for (NewCollateralDetail newCollateralDetail : newCreditFacility.getNewCollateralDetailList()) {
                        log.info("newCollateralDetail :: id :: {}", newCollateralDetail.getId());
                        if (newCollateralDetail.getNewCollateralHeadDetailList() != null) {
                            log.info("newCollateralDetail.getNewCollateralHeadDetailList() :: {}", newCollateralDetail.getNewCollateralHeadDetailList().size());
                            for (NewCollateralHeadDetail newCollateralHeadDetail : newCollateralDetail.getNewCollateralHeadDetailList()) {
                                log.info("newCollateralHeadDetail .id:: {}", newCollateralHeadDetail.getId());
                                newCollateralSubDetailList = getAllNewSubCollateralDetail(newCollateralHeadDetail);
                            }
                        }
                    }
                }
            }
        }

        log.info("newCollateralSubDetailList end :::");
        return newCollateralSubDetailList;
    }


    public List<NewCollateralSubDetail> getAllNewSubCollateralDetail(NewCollateralHeadDetail newCollateralHeadDetail) {
        log.info("getAllNewCollateralSubDetailByNewCollHeadDetail. (newCollateralDetail: {})", newCollateralHeadDetail);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("newCollateralHeadDetail", newCollateralHeadDetail));
        criteria.addOrder(Order.asc("newCollateralHeadDetail.id"));
        List<NewCollateralSubDetail> newCollateralSubDetails = (List<NewCollateralSubDetail>) criteria.list();
        log.info("getList. (result size: {})", newCollateralSubDetails.size());

        return newCollateralSubDetails;

    }

}
