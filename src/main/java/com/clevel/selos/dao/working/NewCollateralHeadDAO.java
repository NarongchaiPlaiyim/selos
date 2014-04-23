package com.clevel.selos.dao.working;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.ProposeType;
import com.clevel.selos.model.RequestAppraisalValue;
import com.clevel.selos.model.db.working.NewCollateral;
import com.clevel.selos.model.db.working.NewCollateralHead;
import com.clevel.selos.util.Util;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;

public class NewCollateralHeadDAO extends GenericDAO<NewCollateralHead, Long> {
    @Inject
    @SELOS
    Logger log;
    @Inject
    public NewCollateralHeadDAO() {}

    public List<NewCollateralHead> findByNewCollateral(NewCollateral newCollateral) {
        log.info("findByNewCollateral ::: {}", newCollateral);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("newCollateral", newCollateral));
        criteria.addOrder(Order.asc("id"));
        List<NewCollateralHead> newCollateralHeadDetails = (List<NewCollateralHead>) criteria.list();
        log.info("newCollateralHeadDetails ::: size : {}", newCollateralHeadDetails.size());
        return newCollateralHeadDetails;
    }

    public List<NewCollateralHead> findByNewCollateralId(final long newCollateralId) {
        log.info("-- findByNewCollateral NewCollateral.id[{}]", newCollateralId);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("newCollateral.id", newCollateralId));
        criteria.addOrder(Order.asc("id"));
        List<NewCollateralHead> newCollateralHeadDetails = (List<NewCollateralHead>) criteria.list();
        log.debug("-- NewCollateralHeadDetailList[{}]", newCollateralHeadDetails);
        return newCollateralHeadDetails;
    }

    public List<NewCollateralHead> findByNewCollateralIdAndPurpose(final long newCollateralId) {
        log.info("---- findByNewCollateral NewCollateral.id[{}]", newCollateralId);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("newCollateral.id", newCollateralId));
        criteria.add(Restrictions.eq("proposeType", "P"));
        criteria.add(Restrictions.ne("appraisalRequest", RequestAppraisalValue.NOT_REQUEST.value()));
        criteria.addOrder(Order.asc("id"));
        List<NewCollateralHead> newCollateralHeadDetails = (List<NewCollateralHead>) criteria.list();
        return newCollateralHeadDetails;
    }

    public List<NewCollateralHead> findByCollateralProposeTypeRequestAppraisalType(long newCollateralId, ProposeType proposeType, RequestAppraisalValue requestAppraisalValue){
        log.info("---- findByCollateralAndProposeType newCollateralId : [{}], proposeType : [{}], requestAppraisal : [{}]", newCollateralId, proposeType, requestAppraisalValue);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("newCollateral.id", newCollateralId));
        criteria.add(Restrictions.eq("proposeType", proposeType.toString()));
        criteria.add(Restrictions.ne("appraisalRequest", requestAppraisalValue.value()));
        criteria.addOrder(Order.asc("id"));
        List<NewCollateralHead> newCollateralHeadDetails = (List<NewCollateralHead>) criteria.list();
        return newCollateralHeadDetails;
    }

    public boolean setAppraisalRequest(final List<NewCollateralHead> newCollateralHeadList){
        log.debug("-- setAppraisalRequest");
        boolean result = false;
        if(Util.isNull(newCollateralHeadList) || Util.isZero(newCollateralHeadList.size())){
            return result;
        }
        log.debug("-- NewCollateralHeadList.size()[{}]", newCollateralHeadList.size());
        for(NewCollateralHead newCollateralHead : newCollateralHeadList){
            log.debug("-- NewCollateralHead.id[{}]", newCollateralHead.getId());
            log.debug("-- NewCollateralHead.NewCollateral.id[{}]", newCollateralHead.getNewCollateral().getId());
            newCollateralHead.setAppraisalRequest(RequestAppraisalValue.NOT_REQUEST.value());
            log.debug("-- NewCollateralHead.AppraisalRequest[{}]", newCollateralHead.getAppraisalRequest());
        }
        persist(newCollateralHeadList);
        log.debug("-- saved");
        return !result;
    }

}
