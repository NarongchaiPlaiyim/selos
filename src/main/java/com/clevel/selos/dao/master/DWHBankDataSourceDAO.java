package com.clevel.selos.dao.master;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.DWHBankDataSource;
import com.clevel.selos.util.Util;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;

public class DWHBankDataSourceDAO extends GenericDAO<DWHBankDataSource, Integer> {

    @Inject
    @SELOS
    Logger log;
    @Inject
    public DWHBankDataSourceDAO() {

    }

    public DWHBankDataSource findByDataSource(String dataSource) {
        if(!Util.isEmpty(dataSource)){
            Criteria criteria = getSession().createCriteria(getEntityClass())
                    .add(Restrictions.eq("dataSource", dataSource.trim()));
            return (DWHBankDataSource) criteria.uniqueResult();
        }
        return null;
    }

}
