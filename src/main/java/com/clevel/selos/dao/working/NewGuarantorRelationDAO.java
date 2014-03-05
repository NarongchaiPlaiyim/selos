package com.clevel.selos.dao.working;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.working.NewCreditFacility;
import com.clevel.selos.model.db.working.NewGuarantorCredit;
import com.clevel.selos.model.db.working.NewGuarantorDetail;
import com.clevel.selos.model.db.working.WorkCase;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
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

        return criteria.list();

    }

    public List<NewGuarantorCredit> getListByNewCreditFacility(WorkCase workCase) {
        Criteria criteria = createCriteria();
        List<NewGuarantorCredit> newGuarantorCreditList = new ArrayList<NewGuarantorCredit>();
        NewCreditFacility newCreditFacility = newCreditFacilityDAO.findByWorkCase(workCase);
        if (newCreditFacility != null && newCreditFacility.getNewGuarantorDetailList() != null && newCreditFacility.getNewGuarantorDetailList().size() > 0) {
            for (NewGuarantorDetail newGuarantorDetail : newCreditFacility.getNewGuarantorDetailList()) {
//              criteria.add(Restrictions.eq("newGuarantorDetail", newGuarantorDetail));
                String query = "SELECT newGuarantorCredit FROM NewGuarantorCredit newGuarantorCredit WHERE newGuarantorDetail.id  = " + newGuarantorDetail.getId();
                newGuarantorCreditList = getSession().createQuery(query).list();
            }
//            criteria.setFetchMode("newGuarantorDetail", FetchMode.LAZY);
//            newGuarantorCreditList = criteria.list();
        }

        return newGuarantorCreditList;
    }

}
