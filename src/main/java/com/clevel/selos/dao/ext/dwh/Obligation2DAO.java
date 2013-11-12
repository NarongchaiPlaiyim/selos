package com.clevel.selos.dao.ext.dwh;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.ext.dwh.Obligation2;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;

public class Obligation2DAO extends GenericDAO<Obligation2, Long> {
    @Inject
    @SELOS
    Logger log;
    @Inject
    public Obligation2DAO() {
    }

    public List<Obligation2> getListByTmbCusIdList(List<String> tmbCusIdList) {
        log.debug("getListByTmbCusId. (tmbCusIdList: {})", tmbCusIdList);

        if (tmbCusIdList != null && tmbCusIdList.size() > 0) {
            Criteria criteria = createCriteria();
            criteria.add(Restrictions.in("tmbCusId", tmbCusIdList));

            List<Obligation2> obligation2List = criteria.list();

            log.debug("getListByTmbCusId. (result size: {})", obligation2List.size());

            return obligation2List;
        }

        return null;
    }
}
