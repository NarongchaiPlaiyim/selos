package com.clevel.selos.dao.master;


import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.SearchCitizenId;
import com.clevel.selos.model.db.master.SearchRegistrationId;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;
import org.hibernate.Criteria;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SearchRegistrationIdDAO extends GenericDAO<SearchRegistrationId,String>
{

    @Inject
    @SELOS
    Logger log;

    public SearchRegistrationIdDAO()
    {

    }

    public List<SearchRegistrationId> getRegistrationId(String registationid)
    {
        List<SearchRegistrationId> registrationIdList = new ArrayList<SearchRegistrationId>();

        log.info("Controller entered in to getRegistrationId method of SearchRegistrationIdDAO class");

        Criteria criteria = getSession().createCriteria(SearchRegistrationId.class);

        criteria.setProjection(Projections.projectionList().add(Projections.property("id"), "id").add(Projections.property("customerId"),"customerId"));

        criteria.add(Restrictions.eq("registrationid", registationid)).setResultTransformer(Transformers.aliasToBean(SearchRegistrationId.class));

        registrationIdList = criteria.list();

        log.info("registrationidlist size is : {}",registrationIdList.size());

        /*Iterator iterator = registrationIdList.iterator();

        int registrationidbasedworkcaseid = 0;

        while(iterator.hasNext() == true)
        {
             SearchRegistrationId searchRegistrationId = new SearchRegistrationId();

            searchRegistrationId = (SearchRegistrationId)iterator.next();

            registrationidbasedworkcaseid = searchRegistrationId.getId();

            log.info("registration id is : {}",registrationidbasedworkcaseid);
        }*/

        return registrationIdList;
    }

}
