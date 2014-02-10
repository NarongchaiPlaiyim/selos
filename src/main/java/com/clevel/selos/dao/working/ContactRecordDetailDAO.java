package com.clevel.selos.dao.working;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.working.Appraisal;
import com.clevel.selos.model.db.working.ContactRecordDetail;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;

public class ContactRecordDetailDAO extends GenericDAO<ContactRecordDetail, Long> {
    @Inject
    @SELOS
    Logger log;
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

    public List<ContactRecordDetail> findRecordCallingByWorkCase(long workCaseId) {

        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("workCase.id", workCaseId));
        criteria.addOrder(Order.asc("id"));
        List<ContactRecordDetail> contactRecordDetailList = criteria.list();

        log.info("findByBizInfoSummaryId. (result size: {})", contactRecordDetailList.size());

        return contactRecordDetailList;
    }


    public List<ContactRecordDetail> findByAppraisal(Appraisal appraisal ){

        log.info("findByAppraisal begin ");
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("appraisal", appraisal));
        criteria.addOrder(Order.asc("no"));
        List<ContactRecordDetail> appraisalContactDetailList = criteria.list();
        log.info("findByAppraisal. (result size: {})", appraisalContactDetailList.size());

        return appraisalContactDetailList;
    }

    public List<ContactRecordDetail> findByAppraisalId(final long appraisalId){
        log.info("-- findByAppraisalId : {}", appraisalId);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("appraisal.id", appraisalId));
        criteria.addOrder(Order.asc("id"));
        List<ContactRecordDetail> appraisalContactDetailList = criteria.list();
        return appraisalContactDetailList;
    }

    public List<ContactRecordDetail> findByWorkCaseId(final long workCaseId){
        log.info("-- findContactRecordDetailListByAppraisalId : {}", workCaseId);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("workCase.id", workCaseId));
        criteria.addOrder(Order.asc("id"));
        List<ContactRecordDetail> contactRecordDetailList = (List<ContactRecordDetail>) criteria.list();
        return contactRecordDetailList;
    }


}
