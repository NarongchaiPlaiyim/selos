package com.clevel.selos.dao.working;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.NCB;
import com.clevel.selos.model.DocMandateType;
import com.clevel.selos.model.db.working.MandateDoc;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;

public class MandateDocDAO extends GenericDAO<MandateDoc, Long>{
    @Inject
    @NCB
    Logger log;
    @Inject
    public MandateDocDAO() {

    }

    public MandateDoc findByWorkCaseId(long workCaseId) {
    log.info("--findByWorkCaseId : {}", workCaseId);
    Criteria criteria = createCriteria();
    criteria.add(Restrictions.eq("workCase.id", workCaseId));
    MandateDoc mandateDoc = (MandateDoc) criteria.uniqueResult();
    return mandateDoc;

    }

    public List<MandateDoc> findByWorkCaseIdAndRoleForReturn(long workCaseId, int roleId) {
        log.info("--findByWorkCaseId : {}", workCaseId);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("workCase.id", workCaseId));
        criteria.add(Restrictions.eq("role.id", roleId));
        criteria.add(Restrictions.eq("mandateType", DocMandateType.MANDATE.value()));
        criteria.add(Restrictions.eq("isCompleted", 0));
        List<MandateDoc> mandateDocList = criteria.list();
        return mandateDocList;

    }

    public List<MandateDoc> findByWorkCaseIdAndRole(long workCaseId, int roleId) {
        log.info("--findByWorkCaseId : {}, roleId : {}", workCaseId, roleId);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("workCase.id", workCaseId));
        criteria.add(Restrictions.eq("role.id", roleId));
        List<MandateDoc> mandateDocList = criteria.list();
        return mandateDocList;

    }

    public List<MandateDoc> findByWorkCasePreScreenIdAndRole(long workCasePreScreenId, int roleId) {
        log.info("--findByWorkCasePreScreenIdAndRole : {}, roleId : {}", workCasePreScreenId, roleId);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("workCasePrescreen.id", workCasePreScreenId));
        criteria.add(Restrictions.eq("role.id", roleId));
        List<MandateDoc> mandateDocList = criteria.list();
        return mandateDocList;

    }
}
