package com.clevel.selos.dao.master;


import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.BPMActive;
import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BPMActiveDAO  extends GenericDAO<BPMActive,Integer>
{
    @Inject
    @SELOS
    Logger log;

    public BPMActiveDAO()
    {

    }

    public List<BPMActive> getBPMActiveAppNumbers(int bpmactive)
    {
        log.info("controller entered into getBPMActiveAppNumbers method of BPMActive class");

        List<BPMActive> bpmActiveAppNumList = new ArrayList<BPMActive>();

        try
        {
             Criteria criteria = getSession().createCriteria(BPMActive.class);

            criteria.setProjection(Projections.projectionList().add(Projections.property("applicationnumber"),"applicationnumber")) ;

            criteria.add(Restrictions.eq("bpmactive", bpmactive)).setResultTransformer(Transformers.aliasToBean(BPMActive.class));

            bpmActiveAppNumList = criteria.list();

            Iterator iterator = bpmActiveAppNumList.iterator();

            while(iterator.hasNext() == true)
            {
                BPMActive bpmActive = new BPMActive();

                bpmActive = (BPMActive)iterator.next();

                String applicationno = bpmActive.getApplicationnumber();

                log.info("application no is : {}",applicationno);
            }



        }
        catch (Exception ex)
        {

        }
        finally {

        }

        return bpmActiveAppNumList;

    }

}
