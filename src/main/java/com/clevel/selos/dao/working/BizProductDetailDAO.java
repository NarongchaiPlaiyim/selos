package com.clevel.selos.dao.working;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.working.BizInfoDetail;
import com.clevel.selos.model.db.working.BizProductDetail;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;

public class BizProductDetailDAO extends GenericDAO<BizProductDetail, Long> {
    @Inject
    @SELOS
    Logger log;
    @Inject
    public BizProductDetailDAO() {
    }

    public List<BizProductDetail> findByBizInfoDetail(BizInfoDetail bizInfoDetail) {

        log.info("findByBizInfoDetail begin ");
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("bizInfoDetail", bizInfoDetail));
        criteria.addOrder(Order.asc("no"));
        List<BizProductDetail> bizProductDetailList = criteria.list();
        log.info("findByBizInfoSummaryId. (result size: {})", bizProductDetailList.size());

        return bizProductDetailList;

    }
}
