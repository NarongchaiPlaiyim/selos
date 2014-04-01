package com.clevel.selos.dao.master;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.SearchApplicationNo;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.hibernate.Criteria;

public class SearchApplicationNoDAO extends GenericDAO<SearchApplicationNo,String>
{
    @Inject
    @SELOS
    Logger log;

    public SearchApplicationNoDAO()
    {

    }

    public List<SearchApplicationNo> getApplicationNoByWorkCaseId(int workcaseid, int bpmActive)
    {
        List<SearchApplicationNo> applicationNoList = new ArrayList<SearchApplicationNo>();

        log.info("controller comes to getApplicationNoByWorkCaseId method of SearchApplicationNoDAO class");

        Criteria criteria = getSession().createCriteria(SearchApplicationNo.class);

        criteria.setProjection( Projections.projectionList().add(Projections.property("applicationNo"),"applicationNo"));

        criteria.add(Restrictions.eq("bpmActive",bpmActive));

        criteria.add(Restrictions.eq("id", workcaseid)).setResultTransformer(Transformers.aliasToBean(SearchApplicationNo.class));

        applicationNoList = criteria.list();

        log.info("applicationNumber list size is : {}",applicationNoList.size());

        Iterator iterator = applicationNoList.iterator();

        String applicationNumber;

        while(iterator.hasNext() == true)
        {
            SearchApplicationNo searchApplicationNo = new SearchApplicationNo();

            searchApplicationNo = (SearchApplicationNo)iterator.next();

            applicationNumber = searchApplicationNo.getApplicationNo();

            log.info("application number is : {}",applicationNumber);
        }

        return applicationNoList;
    }
}
