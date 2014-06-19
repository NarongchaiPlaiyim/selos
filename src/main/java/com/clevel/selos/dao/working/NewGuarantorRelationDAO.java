package com.clevel.selos.dao.working;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.ProposeType;
import com.clevel.selos.model.db.working.NewCreditDetail;
import com.clevel.selos.model.db.working.NewCreditFacility;
import com.clevel.selos.model.db.working.NewGuarantorCredit;
import com.clevel.selos.model.db.working.NewGuarantorDetail;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;

public class NewGuarantorRelationDAO extends GenericDAO<NewGuarantorCredit, Long> {
    @Inject
    @SELOS
    Logger log;

    @Inject
    public NewGuarantorRelationDAO() {
    }

    @Inject
    NewCreditFacilityDAO newCreditFacilityDAO;

    public List<NewGuarantorCredit> getListGuarantorRelationByNewGuarantor(NewGuarantorDetail newGuarantorDetail) {
        log.info("getListGuarantorRelationByNewGuarantor. (NewGuarantorDetail: {})", newGuarantorDetail.getId());
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("newGuarantorDetail", newGuarantorDetail));
        criteria.setFetchMode("newGuarantorDetail", FetchMode.LAZY);
        List<NewGuarantorCredit> newGuarantorCreditList = (List<NewGuarantorCredit>) criteria.list();
        log.debug("getList. (result size: {})", newGuarantorCreditList.size());

        return newGuarantorCreditList;

    }

    public List<NewGuarantorCredit> getListByNewCreditFacility(NewCreditFacility newCreditFacility,ProposeType proposeType){
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("newCreditFacility", newCreditFacility));
        criteria.add(Restrictions.eq("proposeType", proposeType));
        criteria.setFetchMode("newGuarantorDetail", FetchMode.LAZY);
        List<NewGuarantorCredit> newGuarantorCreditList = (List<NewGuarantorCredit>)criteria.list();
        return newGuarantorCreditList;
    }

    public List<NewGuarantorCredit> getListByNewCreditDetail(NewCreditDetail newCreditDetail,ProposeType proposeType){
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("newCreditDetail", newCreditDetail));
        criteria.add(Restrictions.eq("proposeType", proposeType));
        criteria.setFetchMode("newGuarantorDetail", FetchMode.LAZY);
        List<NewGuarantorCredit> newGuarantorCreditList = (List<NewGuarantorCredit>)criteria.list();
        log.info("getList. (result size: {})", newGuarantorCreditList.size());

        return newGuarantorCreditList;
    }

    public List<NewGuarantorCredit> getListByNewCreditFacilityId(long newCreditFacilityId , ProposeType proposeType) {
        log.info("getListByNewCreditFacilityId. (newCreditFacilityId: {})", newCreditFacilityId);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("newCreditFacility.id", newCreditFacilityId));
        criteria.add(Restrictions.eq("proposeType", proposeType));
        criteria.setFetchMode("newGuarantorDetail", FetchMode.SELECT);
        return  criteria.list();

    }

    public List<NewGuarantorCredit> getListByExistingCreditDetailId(long existingCreditDetailId) {
        log.info("getListByExistingCreditDetailId. (existingCreditDetailId: {})", existingCreditDetailId);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("existingCreditDetail.id", existingCreditDetailId));
        criteria.setFetchMode("newGuarantorDetail", FetchMode.LAZY);
        return  criteria.list();

    }
}
