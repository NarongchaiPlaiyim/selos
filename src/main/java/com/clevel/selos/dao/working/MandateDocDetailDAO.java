package com.clevel.selos.dao.working;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.DocMandateType;
import com.clevel.selos.model.RadioValue;
import com.clevel.selos.model.db.working.MandateDocDetail;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;

public class MandateDocDetailDAO extends GenericDAO<MandateDocDetail, Long>{
    @Inject
    @SELOS
    Logger log;

    @Inject
    public MandateDocDetailDAO() {

    }



    public List<MandateDocDetail> findByWorkCaseIdAndRoleForReturn(long workCaseId, int roleId) {
        log.info("--findByWorkCaseId : {}", workCaseId);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("workCase.id", workCaseId));
        criteria.add(Restrictions.eq("role.id", roleId));
        //criteria.add(Restrictions.eq("mandateType", DocMandateType.MANDATE.value()));
        criteria.add(Restrictions.eq("isCompleted", RadioValue.NO));
        criteria.addOrder(Order.asc("ecmDocType"));
        List<MandateDocDetail> mandateDocList = criteria.list();
        return mandateDocList;

    }

    public List<MandateDocDetail> findByWorkCasePrescreenIdAndRoleForReturn(long workCasePrescreenId, int roleId, long stepId) {
        log.info("--findByWorkCasePrescreenId : {}", workCasePrescreenId);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("workCasePrescreen.id", workCasePrescreenId));
        criteria.add(Restrictions.eq("role.id", roleId));
        //criteria.add(Restrictions.eq("mandateType", DocMandateType.MANDATE.value()));
        criteria.add(Restrictions.eq("isCompleted", RadioValue.NO));
        criteria.addOrder(Order.asc("ecmDocType"));
        List<MandateDocDetail> mandateDocList = criteria.list();
        return mandateDocList;

    }

}
