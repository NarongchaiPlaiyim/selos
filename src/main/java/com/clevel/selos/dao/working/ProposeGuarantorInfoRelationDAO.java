package com.clevel.selos.dao.working;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.ProposeType;
import com.clevel.selos.model.db.working.ProposeGuarantorInfoRelation;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;

public class ProposeGuarantorInfoRelationDAO extends GenericDAO<ProposeGuarantorInfoRelation, Long> {
    @Inject
    @SELOS
    Logger log;
    @Inject
    public ProposeGuarantorInfoRelationDAO() {
    }

    public List<ProposeGuarantorInfoRelation> findByProposeLine(long proposeLineId, ProposeType proposeType) {
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("proposeLine.id", proposeLineId));
        criteria.add(Restrictions.eq("proposeType", proposeType));
        criteria.setFetchMode("proposeGuarantorInfo", FetchMode.LAZY);
        List<ProposeGuarantorInfoRelation> proposeGuarantorInfoRelationList = (List<ProposeGuarantorInfoRelation>)criteria.list();
        return proposeGuarantorInfoRelationList;
    }

    public List<ProposeGuarantorInfoRelation> findByGuarantorId(long guarantorId, ProposeType proposeType) {
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("proposeGuarantorInfo.id", guarantorId));
        criteria.add(Restrictions.eq("proposeType", proposeType));
        criteria.setFetchMode("proposeGuarantorInfo", FetchMode.LAZY);
        List<ProposeGuarantorInfoRelation> proposeGuarantorInfoRelationList = (List<ProposeGuarantorInfoRelation>)criteria.list();
        return proposeGuarantorInfoRelationList;
    }

    public List<ProposeGuarantorInfoRelation> getListByExistingCreditDetailId(long existingCreditDetailId) {
        log.info("getListByExistingCreditDetailId. (existingCreditDetailId: {})", existingCreditDetailId);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("existingCreditDetail.id", existingCreditDetailId));
        criteria.setFetchMode("newGuarantorDetail", FetchMode.LAZY);
        return  criteria.list();
    }

    public List<ProposeGuarantorInfoRelation> getListByNewCreditFacilityId(long newCreditFacilityId , ProposeType proposeType) {
        log.info("getListByNewCreditFacilityId. (newCreditFacilityId: {})", newCreditFacilityId);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("proposeCreditInfo.id", newCreditFacilityId));
        criteria.add(Restrictions.eq("proposeType", proposeType));
        criteria.setFetchMode("newGuarantorDetail", FetchMode.SELECT);
        return  criteria.list();

    }
}
