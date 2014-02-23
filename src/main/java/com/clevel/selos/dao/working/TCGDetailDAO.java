package com.clevel.selos.dao.working;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.working.TCGDetail;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;

public class TCGDetailDAO extends GenericDAO<TCGDetail, Integer> {
    @Inject
    @SELOS
    Logger log;
    @Inject
    public TCGDetailDAO() {

    }

    public List<TCGDetail> findTCGDetailByTcgId(long tcgId) {
        log.info("findTCGDetailByTcgId ::: {}", tcgId);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("tcg.id", tcgId));
        criteria.addOrder(Order.asc("id"));
        List<TCGDetail> tcgDetailList = (List<TCGDetail>) criteria.list();
        if (tcgDetailList != null)
        	log.info("tcgDetailList ::: size : {}", tcgDetailList.size());
        return tcgDetailList;
    }
}
