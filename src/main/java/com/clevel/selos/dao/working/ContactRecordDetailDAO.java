package com.clevel.selos.dao.working;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.model.db.working.ContactRecordDetail;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;

public class ContactRecordDetailDAO extends GenericDAO<ContactRecordDetail, Integer> {
    @Inject
    private Logger log;

    @Inject
    public ContactRecordDetailDAO() {
    }

    public List<ContactRecordDetail> findRecordCallingByCustomerAcceptance(long customerAcceptanceId) {

        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("customerAcceptance.id", customerAcceptanceId));
        criteria.addOrder(Order.asc("id"));
        List<ContactRecordDetail> contactRecordDetailList = criteria.list();

        log.info("findByBizInfoSummaryId. (result size: {})", contactRecordDetailList.size());


        return contactRecordDetailList;
    }


}
