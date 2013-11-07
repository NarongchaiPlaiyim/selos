package com.clevel.selos.dao.working;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.model.db.working.Appraisal;
import com.clevel.selos.model.db.working.CollateralDetail;
import com.clevel.selos.model.db.working.WorkCase;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;

public class CollateralDetailDAO extends GenericDAO<CollateralDetail, Long> {
    @Inject
    private Logger log;

    @Inject
    public CollateralDetailDAO(){

    }
    
    public List<CollateralDetail> findByAppraisal(Appraisal appraisal ){
        log.info("findByAppraisal begin ");
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("appraisal", appraisal));
        criteria.addOrder(Order.asc("no"));
        List<CollateralDetail> collateralDetailList = criteria.list();
        log.info("findByAppraisal. (result size: {})", collateralDetailList.size());

        return collateralDetailList;
    }
}
