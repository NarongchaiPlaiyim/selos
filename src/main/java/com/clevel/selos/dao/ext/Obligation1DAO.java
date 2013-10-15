package com.clevel.selos.dao.ext;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.model.db.ext.Obligation1;
import com.clevel.selos.model.db.test.csi.CSIData;
import com.clevel.selos.util.Util;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;

public class Obligation1DAO extends GenericDAO<Obligation1,Long> {
    @Inject
    private Logger log;

    @Inject
    public Obligation1DAO() {
    }

    public List<Obligation1> getListByTmbCusId(String tmbCusId) {
        log.debug("getListByTmbCusId. (tmbCusId: {})",tmbCusId);

        if(!Util.isEmpty(tmbCusId)){
            Criteria criteria = createCriteria();
            criteria.add(Restrictions.eq("tmbCusId", tmbCusId));

            List<Obligation1> obligation1List = criteria.list();

            log.debug("getListByTmbCusId. (result size: {})", obligation1List.size());

            return obligation1List;
        }

        return null;
    }
}
