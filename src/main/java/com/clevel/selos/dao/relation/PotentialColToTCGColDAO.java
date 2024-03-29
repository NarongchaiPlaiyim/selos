package com.clevel.selos.dao.relation;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
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
    @SELOS
    Logger log;
    @Inject
    PotentialColToTCGColDAO() {

    }

    @SuppressWarnings("unchecked")
    public List<PotentialColToTCGCol> getListPotentialColToTCGCol(PotentialCollateral potentialCollateral) {
        log.info("getListPotentialColToTCGCol. (PotentialCollateral: {})", potentialCollateral);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("potentialCollateral", potentialCollateral));
        criteria.addOrder(Order.asc("tcgCollateralType.id"));
        List<PotentialColToTCGCol> list = criteria.list();
        log.info("getListPotentialColToTCGCol. (result size: {})", list.size());
        return list;
    }

    public List<PotentialColToTCGCol> getListPotentialColToTCGCol(int id) {
        log.info("getListPotentialColToTCGCol. (id: {})", id);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("potentialCollateral.id", id));
        criteria.addOrder(Order.asc("tcgCollateralType.id"));
        List<PotentialColToTCGCol> list = criteria.list();
        log.info("getListPotentialColToTCGCol. (result size: {})", list.size());
        return list;
    }

    public PotentialColToTCGCol getPotentialColToTCGCol(PotentialCollateral potentialCollateral ,TCGCollateralType tcgCollateralType) {
        log.info("getListPotentialColToTCGCol. (PotentialCollateral: {})", potentialCollateral);
        log.info("getPotentialColToTCGCol. (TCGCollateralType: {})", tcgCollateralType);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("potentialCollateral", potentialCollateral));
        criteria.add(Restrictions.eq("tcgCollateralType", tcgCollateralType));
        PotentialColToTCGCol potentialColToTCGCol = (PotentialColToTCGCol) criteria.uniqueResult();
        return potentialColToTCGCol;
    }
}
