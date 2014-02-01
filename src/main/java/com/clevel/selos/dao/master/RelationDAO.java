package com.clevel.selos.dao.master;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.RelationValue;
import com.clevel.selos.model.db.master.Relation;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;

public class RelationDAO extends GenericDAO<Relation, Integer> {
    @Inject
    @SELOS
    Logger log;
    @Inject
    public RelationDAO() {
    }

    public List<Relation> getOtherRelationList() {
        Criteria criteria = getSession().createCriteria(getEntityClass())
                .add(Restrictions.ne("id", 1));
        List<Relation> relationList = criteria.list();
        log.info("getOtherRelationList. (result size: {})", relationList.size());
        return relationList;
    }

    public List<Relation> getRelationOnlyBorrower(){
        Criteria criteria = getSession().createCriteria(getEntityClass())
                .add(Restrictions.eq("id", RelationValue.BORROWER.value()));
        List<Relation> relationList = criteria.list();
        log.info("getRelationOnlyBorrower. (result size: {})", relationList.size());
        return relationList;
    }
}
