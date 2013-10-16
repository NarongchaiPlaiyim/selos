package com.clevel.selos.dao.working;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.model.db.working.BizInfoDetail;
import com.clevel.selos.model.db.working.BizStakeHolderDetail;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;

public class BizStakeHolderDetailDAO extends GenericDAO<BizStakeHolderDetail,Long> {
    @Inject
    private Logger log;

    @Inject
    public BizStakeHolderDetailDAO() {
    }

    public List<BizStakeHolderDetail>findByBizInfoDetail(BizInfoDetail bizInfoDetail ,String stakeHolderType ){

        log.info("findByBizInfoDetail begin ");
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("stakeHolderType", stakeHolderType));
        criteria.add(Restrictions.eq("bizInfoDetail", bizInfoDetail));
        criteria.addOrder(Order.asc("no"));
        List<BizStakeHolderDetail> bizStakeHolderDetailList = criteria.list();
        log.info("findByBizInfoSummaryId. (result size: {})", bizStakeHolderDetailList.size());

        return bizStakeHolderDetailList;

    }


}
