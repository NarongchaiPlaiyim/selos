package com.clevel.selos.dao.ext.dwh;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.model.db.ext.dwh.Obligation1;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;

public class Obligation1DAO extends GenericDAO<Obligation1, Long> {
    @Inject
    private Logger log;

    @Inject
    public Obligation1DAO() {
    }

    public List<Obligation1> getListByTmbCusIdList(List<String> tmbCusIdList) {
        log.debug("getListByTmbCusId. (tmbCusIdList: {})", tmbCusIdList);

        if (tmbCusIdList != null && tmbCusIdList.size() > 0) {
            Criteria criteria = createCriteria();
            criteria.add(Restrictions.in("tmbCusId", tmbCusIdList));

            List<Obligation1> obligation1List = criteria.list();

            log.debug("getListByTmbCusId. (result size: {})", obligation1List.size());

            return obligation1List;
        }

        return null;
    }
}
