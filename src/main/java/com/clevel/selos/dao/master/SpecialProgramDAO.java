package com.clevel.selos.dao.master;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.model.db.master.SpecialProgram;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;

public class SpecialProgramDAO extends GenericDAO<SpecialProgram,Integer> {
    @Inject
    private Logger log;

    @Inject
    public SpecialProgramDAO() {
    }

    @Override
    public List<SpecialProgram> findAll() {
        Criteria criteria = getSession().createCriteria(getEntityClass())
                .add(Restrictions.eq("active",1));
        List<SpecialProgram> list = criteria.list();
        return list;
    }
}
