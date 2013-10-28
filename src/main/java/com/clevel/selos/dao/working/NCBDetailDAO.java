package com.clevel.selos.dao.working;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.model.db.working.NCBDetail;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;


public class NCBDetailDAO extends GenericDAO<NCBDetail, Long> {
    @Inject
    private Logger log;

    @Inject
    public NCBDetailDAO() {
    }


    public List<NCBDetail> findNCBDetailByNcbId(long ncbId) {
        log.info("findNCBDetailByTcgId ::: {}", ncbId);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("ncb.id", ncbId));
        criteria.addOrder(Order.asc("id"));
        List<NCBDetail> ncbDetailList = (List<NCBDetail>) criteria.list();
        log.info("ncbDetailList ::: size : {}", ncbDetailList.size());
        return ncbDetailList;
    }
}