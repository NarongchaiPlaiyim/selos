package com.clevel.selos.dao.master;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.SearchApplicationNoByWorkCasePrescreenId;
import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Prashanth Reddy B
 * Date: 3/25/14
 * Time: 12:54 PM
 * To change this template use File | Settings | File Templates.
 */
public class SearchApplicationNoByWorkCasePrescreenIdDAO extends GenericDAO<SearchApplicationNoByWorkCasePrescreenId,String> {

    @Inject
    @SELOS
    Logger log;

    public SearchApplicationNoByWorkCasePrescreenIdDAO()
    {

    }

    public List<SearchApplicationNoByWorkCasePrescreenId> getApplicationNoByWorkCaseId(int workcaseid, int bpmActive)
    {
        List<SearchApplicationNoByWorkCasePrescreenId> applicationNoList = new ArrayList<SearchApplicationNoByWorkCasePrescreenId>();

        log.info("controller comes to getApplicationNoByWorkCaseId method of SearchApplicationNoDAO class");

        Criteria criteria = getSession().createCriteria(SearchApplicationNoByWorkCasePrescreenId.class);

        criteria.setProjection( Projections.projectionList().add(Projections.property("applicationNo"),"applicationNo"));

        criteria.add(Restrictions.eq("bpmActive",bpmActive));

        criteria.add(Restrictions.eq("id", workcaseid)).setResultTransformer(Transformers.aliasToBean(SearchApplicationNoByWorkCasePrescreenId.class));

        applicationNoList = criteria.list();

        log.info("applicationNumber list size is : {}",applicationNoList.size());

        Iterator iterator = applicationNoList.iterator();

        String applicationNumber;

        while(iterator.hasNext() == true)
        {
            SearchApplicationNoByWorkCasePrescreenId searchApplicationNo = new SearchApplicationNoByWorkCasePrescreenId();

            searchApplicationNo = (SearchApplicationNoByWorkCasePrescreenId)iterator.next();

            applicationNumber = searchApplicationNo.getApplicationNo();

            log.info("application number is : {}",applicationNumber);
        }

        return applicationNoList;
    }

    //find number of appeals
    public Integer getAppealResubmitCount(String refAppNumber, Integer requestType)
    {

        Criteria criteria = createCriteria();

        criteria.add(Restrictions.eq("refAppNumber",refAppNumber)).add(Restrictions.eq("requestTypeId",requestType));

        log.info("AppNumber : {}, Request type : {}",refAppNumber,requestType);

        Integer caseCount = ((Number)criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();

        log.info("Case Count : {} ",caseCount);

        return caseCount;

    }
}
