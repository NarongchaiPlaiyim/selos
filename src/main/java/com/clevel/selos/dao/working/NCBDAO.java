package com.clevel.selos.dao.working;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.model.db.working.NCB;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;

/**
 * Created with IntelliJ IDEA.
 * User: SUKANDA CHITSUP
 * Date: 20/9/2556
 * Time: 16:02 น.
 * To change this template use File | Settings | File Templates.
 */
public class NCBDAO extends GenericDAO<NCB, Long> {
    @Inject
    private Logger log;

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


}
