package com.clevel.selos.dao.relation;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.CreditType;
import com.clevel.selos.model.db.master.ProductProgram;
import com.clevel.selos.model.db.relation.PrdProgramToCreditType;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;

public class PrdProgramToCreditTypeDAO extends GenericDAO<PrdProgramToCreditType, Integer> {
    @Inject
    @SELOS
    Logger log;
    @Inject
    public PrdProgramToCreditTypeDAO() {
    }

    @SuppressWarnings("unchecked")
    public List<PrdProgramToCreditType> getListPrdProByPrdprogram(ProductProgram productProgram) {
        log.info("getListPrdProByPrdprogram. (ProductProgram: {})", productProgram);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("productProgram", productProgram));
        criteria.addOrder(Order.asc("creditType.id"));
        List<PrdProgramToCreditType> list = criteria.list();

        log.info("getList. (result size: {})", list.size());

        return list;

    }

    @SuppressWarnings("unchecked")
    public List<PrdProgramToCreditType> getListCreditProposeByPrdprogram(ProductProgram productProgram) {
        log.info("getListCreditProposeByPrdprogram. (ProductProgram: {})", productProgram);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("productProgram", productProgram));
        criteria.add(Restrictions.eq("addProposeCredit", 1));
        criteria.addOrder(Order.asc("creditType.id"));
        List<PrdProgramToCreditType> list = criteria.list();

        log.info("getList. (result size: {})", list.size());

        return list;

    }

    public List<PrdProgramToCreditType> getListCreditProposeByPrdprogram(int productProgramId) {
        log.info("getListCreditProposeByPrdprogram. (ProductProgramId: {})", productProgramId);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("productProgram.id", productProgramId));
        criteria.add(Restrictions.eq("addProposeCredit", 1));
        criteria.addOrder(Order.asc("creditType.id"));
        List<PrdProgramToCreditType> list = criteria.list();

        log.info("getList. (result size: {})", list.size());

        return list;

    }

    @SuppressWarnings("unchecked")
    public PrdProgramToCreditType getPrdProgramToCreditType(CreditType creditType , ProductProgram productProgram) {
        log.info("getPrdProgramToCreditType. (productProgram: {})",productProgram );
        log.info("getPrdProgramToCreditType. (creditType: {})", creditType);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("creditType", creditType));
        criteria.add(Restrictions.eq("productProgram", productProgram));
        PrdProgramToCreditType  prdProgramToCreditType = (PrdProgramToCreditType)criteria.uniqueResult();

        return prdProgramToCreditType;

    }

    @SuppressWarnings("unchecked")
    public PrdProgramToCreditType getPrdProgramToCreditType(int creditTypeId , int productProgramId) {
        log.info("getPrdProgramToCreditType. (productProgram.id: {})",productProgramId );
        log.info("getPrdProgramToCreditType. (creditType.id: {})", creditTypeId);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("creditType.id", creditTypeId));
        criteria.add(Restrictions.eq("productProgram.id", productProgramId));
        PrdProgramToCreditType  prdProgramToCreditType = (PrdProgramToCreditType)criteria.uniqueResult();
        return prdProgramToCreditType;

    }
}
