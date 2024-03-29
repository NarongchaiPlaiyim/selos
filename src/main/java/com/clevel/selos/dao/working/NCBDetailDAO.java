package com.clevel.selos.dao.working;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.working.NCBDetail;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;


public class NCBDetailDAO extends GenericDAO<NCBDetail, Long> {
    @Inject
    @SELOS
    Logger log;
    @Inject
    public NCBDetailDAO() {
    }


    public List<NCBDetail> findNCBDetailByNcbId(long ncbId) {
        log.info("findNCBDetailByNcbId ::: {}", ncbId);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("ncb.id", ncbId));
        criteria.addOrder(Order.asc("id"));
        List<NCBDetail> ncbDetailList = (List<NCBDetail>) criteria.list();
        log.info("ncbDetailList ::: size : {}", ncbDetailList.size());
        return ncbDetailList;
    }

    public List<NCBDetail> getNCBForDBRList(long workCaseId){
        log.debug("getNCBForDBRList : workCaseId : {}", workCaseId);
        List<NCBDetail> ncbDetailList;

        String query = "SELECT ncbDetail FROM NCBDetail ncbDetail WHERE ncbDetail.ncb in ( SELECT ncb FROM NCB ncb WHERE ncb.customer in ( SELECT customer FROM Customer customer WHERE customer.workCase.id = " + workCaseId + ")) order by ncbDetail.id asc";
        ncbDetailList = (List<NCBDetail>) getSession().createQuery(query).list();

        return ncbDetailList;
    }
}