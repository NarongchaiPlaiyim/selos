package com.clevel.selos.dao.master;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.model.db.master.CustomerEntity;
import com.clevel.selos.model.db.master.SettlementStatus;
import com.clevel.selos.util.Util;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;

public class SettlementStatusDAO extends GenericDAO<SettlementStatus,Integer> {
    @Inject
    private Logger log;

    @Inject
    public SettlementStatusDAO() {
    }

    public SettlementStatus getIndividualByCode(String code){
        log.debug("getIndividualByCode. (code: {}",code);
        if(!Util.isEmpty(code)){
            //set for individual
            Criteria criteria = createCriteria();
            criteria.add(Restrictions.eq("customerEntity.id", 1));
            criteria.add(Restrictions.eq("code", code));
            SettlementStatus settlementStatus = (SettlementStatus) criteria.uniqueResult();

            log.debug("getIndividualByCode. (settlementStatus: {})",settlementStatus);
            return settlementStatus;
        }
        return null;
    }

    public SettlementStatus getJuristicByCode(String code){
        log.debug("getJuristicByCode. (code: {}",code);
        if(!Util.isEmpty(code)){
            //set for juristic
            Criteria criteria = createCriteria();
            criteria.add(Restrictions.eq("customerEntity.id", 2));
            criteria.add(Restrictions.eq("code", code));
            SettlementStatus settlementStatus = (SettlementStatus) criteria.uniqueResult();

            log.debug("getJuristicByCode. (settlementStatus: {})",settlementStatus);
            return settlementStatus;
        }
        return null;
    }
}
