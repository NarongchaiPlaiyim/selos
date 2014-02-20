package com.clevel.selos.dao.working;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.working.NewCreditFacility;
import com.clevel.selos.model.db.working.NewGuarantorCredit;
import com.clevel.selos.model.db.working.WorkCase;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class NewGuarantorRelationDAO extends GenericDAO<NewGuarantorCredit, Long> {
    @Inject
    @SELOS
    Logger log;
    @Inject
    NewCreditFacilityDAO newCreditFacilityDAO;
    @Inject
    public NewGuarantorRelationDAO() {}

    @SuppressWarnings("unchecked")
    public List<NewGuarantorCredit> getListGuarantorRelationByNewGuarantor(WorkCase workCase){
        List<NewGuarantorCredit> newGuarantorCreditList = new ArrayList<NewGuarantorCredit>();

        NewCreditFacility newCreditFacility = newCreditFacilityDAO.findByWorkCase(workCase);
        Criteria criteria = createCriteria();
        log.info("newCreditFacility.getNewGuarantorDetailList() :: {}",newCreditFacility.getNewGuarantorDetailList().size());
        if (newCreditFacility != null && newCreditFacility.getNewGuarantorDetailList() != null && newCreditFacility.getNewGuarantorDetailList().size() > 0) {
            criteria.add(Restrictions.in("newGuarantorDetail", newCreditFacility.getNewGuarantorDetailList()));
            log.info("getList. (result size: {})", criteria.list().size());
            newGuarantorCreditList = criteria.list();
        }
        return newGuarantorCreditList;


    }


}
