package com.clevel.selos.dao.master;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.SearchCitizenId;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;
import org.hibernate.Criteria;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SearchCitizenIdDAO extends GenericDAO<SearchCitizenId,String>
{
    @Inject
    @SELOS
    Logger log;

    public SearchCitizenIdDAO()
    {

    }

    public List<SearchCitizenId> getCitizenId(String citizenid)
    {
        List<SearchCitizenId> searchCitizenIdList = new ArrayList<SearchCitizenId>();

        log.info("Controller entered in to getCitizenId method of SearchCitizenIdDAO class");

        Criteria criteria = getSession().createCriteria(SearchCitizenId.class);

        criteria.setProjection(Projections.projectionList().add(Projections.property("id"), "id"));

        criteria.add(Restrictions.eq("citizenid", citizenid)).setResultTransformer(Transformers.aliasToBean(SearchCitizenId.class));

        searchCitizenIdList = criteria.list();

        log.info("searchCitizenIdList size is : {}",searchCitizenIdList);

        Iterator iterator = searchCitizenIdList.iterator();

        int citijenbasedworkcaseid = 0;

        while(iterator.hasNext() == true)
        {
            SearchCitizenId searchCitizenId = new SearchCitizenId();

            searchCitizenId = (SearchCitizenId)iterator.next();

            citijenbasedworkcaseid = searchCitizenId.getId();

            log.info("citizen id obtained is : {}",citijenbasedworkcaseid);
        }



        return searchCitizenIdList;

    }
}
