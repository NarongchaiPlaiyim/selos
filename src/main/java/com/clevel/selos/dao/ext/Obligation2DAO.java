package com.clevel.selos.dao.ext;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.model.db.ext.dwh.Obligation2;
import com.clevel.selos.util.Util;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;

public class Obligation2DAO extends GenericDAO<Obligation2,Long> {
    @Inject
    private Logger log;

    @Inject
    public Obligation2DAO() {
    }

    public List<Obligation2> getListByTmbCusId(String tmbCusId) {
        log.debug("getListByTmbCusId. (tmbCusId: {})",tmbCusId);

        if(!Util.isEmpty(tmbCusId)){
            Criteria criteria = createCriteria();
            criteria.add(Restrictions.eq("tmbCusId", tmbCusId));

            List<Obligation2> obligation2List = criteria.list();

            log.debug("getListByTmbCusId. (result size: {})", obligation2List.size());
            return obligation2List;
        }

        return null;
    }
}
