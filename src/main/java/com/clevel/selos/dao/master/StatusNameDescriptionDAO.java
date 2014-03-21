package com.clevel.selos.dao.master;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.SearchCitizenId;
import com.clevel.selos.model.db.master.StatusNameDescription;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.hibernate.Criteria;

public class StatusNameDescriptionDAO extends GenericDAO<StatusNameDescription,String>
{
    @Inject
    @SELOS
    Logger log;

    public StatusNameDescriptionDAO()
    {

    }


    public String getStatusNameDescriptions(int statusid)
    {
        List<StatusNameDescription> statusnamedescriptions = new ArrayList<StatusNameDescription>();

        String statusdescription = null;

        log.info("controller comes to getStatusNameDescriptions method of StatusNameDescriptionDAO");

        Criteria criteria = getSession().createCriteria(StatusNameDescription.class);

        criteria.setProjection( Projections.projectionList().add(Projections.property("name"),"name")) ;

        criteria.add(Restrictions.eq("id", statusid)).setResultTransformer(Transformers.aliasToBean(StatusNameDescription.class));


        statusnamedescriptions = criteria.list();

        log.info("stepNameIdsList size is ::::::::::: {}",statusnamedescriptions.size());

        Iterator iterator = statusnamedescriptions.iterator();

        log.info("iterator hasNext method return type : {}",iterator.hasNext());

        List<String> statusNameDescriptionlist = new ArrayList<String>();

        while(iterator.hasNext() == true)
        {
            log.info("hasNext method is true that's y controller comes to while loop");

            StatusNameDescription statusNameDescription = new StatusNameDescription();

            statusNameDescription = (StatusNameDescription)iterator.next();

            log.info("statusName Description is : {}",statusNameDescription.getName());

            statusNameDescriptionlist.add(statusNameDescription.getName()) ;

             statusdescription =  statusNameDescription.getName();
        }

        log.info("stepnameidlit string values : {}",statusNameDescriptionlist.size());

        return statusdescription;
    }
}
