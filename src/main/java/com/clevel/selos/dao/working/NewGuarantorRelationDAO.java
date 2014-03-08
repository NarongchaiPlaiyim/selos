package com.clevel.selos.dao.working;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
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
        log.info("getList. (result size: {})", newGuarantorCreditList.size());

        return newGuarantorCreditList;

    }


    public List<NewGuarantorCredit> getListByNewCreditFacility(NewCreditFacility newCreditFacility) {
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("newCreditFacility", newCreditFacility));
        criteria.setFetchMode("newGuarantorDetail", FetchMode.LAZY);
        List<NewGuarantorCredit> newGuarantorCreditList = (List<NewGuarantorCredit>) criteria.list();
        log.info("getList. (result size: {})", newGuarantorCreditList.size());

        return newGuarantorCreditList;
    }


}
