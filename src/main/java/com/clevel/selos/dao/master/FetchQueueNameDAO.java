package com.clevel.selos.dao.master;

import com.clevel.selos.integration.SELOS;
import com.sun.org.apache.xml.internal.security.encryption.Transforms;
import org.hibernate.Criteria;
import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.model.db.master.FetchQueueName;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

public class FetchQueueNameDAO extends GenericDAO<FetchQueueName,Integer>
{
    @Inject
    @SELOS
    Logger log;

    List<FetchQueueName> fetchQueueNameList = new ArrayList<FetchQueueName>();

    public String getQueueTableName(int queueid)
    {
       String queuename = null;

       Criteria criteria =  getSession().createCriteria(FetchQueueName.class);

        criteria.setProjection(Projections.projectionList().add(Projections.property("queuename"),"queuename"))
                .add(Restrictions.eq("id",queueid)).setResultTransformer(Transformers.aliasToBean(FetchQueueName.class));

         fetchQueueNameList = criteria.list();

        Iterator iterator = fetchQueueNameList.iterator();

        while(iterator.hasNext() == true)
        {
            FetchQueueName fetchQueueName = new FetchQueueName();

            fetchQueueName = (FetchQueueName)iterator.next();

            queuename = fetchQueueName.getQueuename();

            log.info("queuename obtained is : {}",queuename);
        }


        return queuename;
    }
}
