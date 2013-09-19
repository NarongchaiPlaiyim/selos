package com.clevel.selos.dao.master;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.model.db.master.PotentialCollateral;
import com.clevel.selos.model.db.master.TCGCollateralType;
import com.clevel.selos.model.db.relation.PotentialColToTCGCol;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;

public class PotentialColToTCGColDAO extends GenericDAO<PotentialColToTCGCol, Integer> {
    @Inject
    private Logger log;

    @Inject
    PotentialColToTCGColDAO(){

    }

    @SuppressWarnings("unchecked")
    public List<PotentialColToTCGCol> getListPotentialColToTCGCol(PotentialCollateral potentialCollateral ){
        log.info("getListPotentialColToTCGCol. (PotentialCollateral: {})", potentialCollateral);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("potentialCollateral", potentialCollateral));
        criteria.addOrder(Order.asc("potentialCollateral.id"));

        List<PotentialColToTCGCol> list = criteria.list();

        log.info("getList. (result size: {})", list.size());

        return list;

    }
}
