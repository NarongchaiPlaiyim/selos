package com.clevel.selos.dao.relation;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.Relation;
import com.clevel.selos.model.db.relation.RelationCustomer;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class RelationCustomerDAO extends GenericDAO<RelationCustomer, Integer> {
    @Inject
    @SELOS
    Logger log;
    @Inject
    RelationCustomerDAO() {
    }

    @SuppressWarnings("unchecked")
    public List<Relation> getListRelation(int customerEntityId, int borrowerTypeId, int spouse) {
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("customerEntity.id", customerEntityId));
        criteria.add(Restrictions.eq("borrowerType.id", borrowerTypeId));
        criteria.add(Restrictions.eq("spouse", spouse));
        criteria.addOrder(Order.asc("id"));

        List<RelationCustomer> list = criteria.list();
        List<Relation> listRelation = new ArrayList<Relation>();
        for(RelationCustomer rc : list){
            listRelation.add(rc.getRelation());
        }
        log.info("getList. (result size: {})", listRelation.size());

        return listRelation;
    }

    public List<Relation> getListRelationWithOutBorrower(int customerEntityId, int borrowerTypeId, int spouse) {
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("customerEntity.id", customerEntityId));
        criteria.add(Restrictions.eq("borrowerType.id", borrowerTypeId));
        criteria.add(Restrictions.eq("spouse", spouse));
        criteria.addOrder(Order.asc("id"));

        List<RelationCustomer> list = criteria.list();
        List<Relation> listRelation = new ArrayList<Relation>();
        for(RelationCustomer rc : list){
            if(rc.getRelation().getId() != 1){
                listRelation.add(rc.getRelation());
            }
        }
        log.info("getList. (result size: {})", listRelation.size());

        return listRelation;
    }
}
