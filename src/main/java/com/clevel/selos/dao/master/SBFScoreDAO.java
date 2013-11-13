package com.clevel.selos.dao.master;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.SBFScore;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;

public class SBFScoreDAO extends GenericDAO<SBFScore, Integer> {
    @Inject
    @SELOS
    Logger log;
    @Inject
    public SBFScoreDAO() {
    }

    @Override
    public List<SBFScore> findAll() {
        Criteria criteria = getSession().createCriteria(getEntityClass())
                .add(Restrictions.eq("active", 1));
        List<SBFScore> list = criteria.list();
        return list;
    }
}
