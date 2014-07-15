package com.clevel.selos.dao.working;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.ProposeType;
import com.clevel.selos.model.RequestAppraisalValue;
import com.clevel.selos.model.db.working.ProposeCollateralInfoHead;
import com.clevel.selos.util.Util;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;

public class ProposeCollateralInfoHeadDAO extends GenericDAO<ProposeCollateralInfoHead, Long> {
    @Inject
    @SELOS
    Logger log;
    @Inject
    public ProposeCollateralInfoHeadDAO() {}

    public List<ProposeCollateralInfoHead> findByCollateralProposeTypeRequestAppraisalType(long newCollateralId, RequestAppraisalValue requestAppraisalValue){
        log.info("---- findByCollateralAndProposeType newCollateralId : [{}], requestAppraisal : [{}]", newCollateralId, requestAppraisalValue);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("proposeLine.id", newCollateralId));
        criteria.add(Restrictions.eq("proposeType", ProposeType.P));
        criteria.add(Restrictions.ne("appraisalRequest", requestAppraisalValue.value()));
        criteria.addOrder(Order.asc("id"));
        List<ProposeCollateralInfoHead> newCollateralHeadDetails = (List<ProposeCollateralInfoHead>) criteria.list();
        return newCollateralHeadDetails;
    }

    public List<ProposeCollateralInfoHead> findByNewCollateralId(final long newCollateralId) {
        log.info("-- findByNewCollateral NewCollateral.id[{}]", newCollateralId);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("proposeCollateral.id", newCollateralId));
        criteria.addOrder(Order.asc("id"));
        List<ProposeCollateralInfoHead> newCollateralHeadDetails = (List<ProposeCollateralInfoHead>) criteria.list();
        log.debug("-- NewCollateralHeadDetailList[{}]", newCollateralHeadDetails);
        return newCollateralHeadDetails;
    }

    public boolean setAppraisalRequest(final List<ProposeCollateralInfoHead> newCollateralHeadList){
        log.debug("-- setAppraisalRequest");
        boolean result = false;
        if(Util.isNull(newCollateralHeadList) || Util.isZero(newCollateralHeadList.size())){
            return result;
        }
        log.debug("-- NewCollateralHeadList.size()[{}]", newCollateralHeadList.size());
        for(ProposeCollateralInfoHead newCollateralHead : newCollateralHeadList){
            log.debug("-- NewCollateralHead.id[{}]", newCollateralHead.getId());
            log.debug("-- NewCollateralHead.NewCollateral.id[{}]", newCollateralHead.getProposeCollateral().getId());
            newCollateralHead.setAppraisalRequest(RequestAppraisalValue.NOT_REQUEST.value());
            log.debug("-- NewCollateralHead.AppraisalRequest[{}]", newCollateralHead.getAppraisalRequest());
        }
        persist(newCollateralHeadList);
        log.debug("-- saved");
        return !result;
    }

    public List<ProposeCollateralInfoHead> findByNewCollateralIdAndPurpose(final long newCollateralId) {
        log.info("---- findByNewCollateral NewCollateral.id[{}]", newCollateralId);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("proposeCollateral.id", newCollateralId));
        criteria.add(Restrictions.eq("proposeType", "P"));
        criteria.add(Restrictions.ne("appraisalRequest", RequestAppraisalValue.NOT_REQUEST.value()));
        criteria.addOrder(Order.asc("id"));
        List<ProposeCollateralInfoHead> newCollateralHeadDetails = (List<ProposeCollateralInfoHead>) criteria.list();
        return newCollateralHeadDetails;
    }

    /*public List<NewCollateralHead> findByNewCollateral(NewCollateral newCollateral) {
        log.info("findByNewCollateral ::: {}", newCollateral);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("newCollateral", newCollateral));
        criteria.addOrder(Order.asc("id"));
        List<NewCollateralHead> newCollateralHeadDetails = (List<NewCollateralHead>) criteria.list();
        log.info("newCollateralHeadDetails ::: size : {}", newCollateralHeadDetails.size());
        return newCollateralHeadDetails;
    }*/
}
