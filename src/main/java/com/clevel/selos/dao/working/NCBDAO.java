package com.clevel.selos.dao.working;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.working.NCB;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;

public class NCBDAO extends GenericDAO<NCB, Long> {
    @Inject
    @SELOS
    Logger log;
    @Inject
    public NCBDAO() {
    }

    public NCB findNcbByCustomer(long customerId) {
        log.info("findNcbByCustomer : {}", customerId);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("customer.id", customerId));
        NCB ncb = (NCB) criteria.uniqueResult();

        return ncb;
    }

    public NCB checkNCBDate(List<Long> customerIdList)
    {

        NCB ncb = (NCB)createCriteria().add(Restrictions.in("customer.id",customerIdList)).addOrder(Order.desc("checkingDate")).setMaxResults(1).uniqueResult();

        return ncb;

    }


}
