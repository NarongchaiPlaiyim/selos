package com.clevel.selos.dao.master;


import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.SearchUserId;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.hibernate.Criteria;

public class SearchUserIdDAO extends GenericDAO<SearchUserId,Integer>
{
    @Inject
    @SELOS
    Logger log;

    public SearchUserIdDAO()
    {

    }

    public List<SearchUserId> getWorkCaseIdByUserId(String userId)
    {
        log.info("Controller comes to getWorkCaseIdByUserId method of SearchUserIdDAO class");

        List<SearchUserId> workCaseIdList = new ArrayList<SearchUserId>();

        Criteria criteria = getSession().createCriteria(SearchUserId.class);

        criteria.setProjection( Projections.projectionList().add(Projections.property("workcaseid"),"workcaseid").add(Projections.property("id"), "id"));

        criteria.add(Restrictions.eq("userid", userId)).setResultTransformer(Transformers.aliasToBean(SearchUserId.class));

        workCaseIdList = criteria.list();

        log.info("workcaseidlist size is : {}",workCaseIdList.size());

/*        Iterator iterator = workCaseIdList.iterator();

        int id;

        int workcaseid = 0 ;

        while(iterator.hasNext() == true)
        {
            SearchUserId searchUserId = new SearchUserId();

            searchUserId = (SearchUserId)iterator.next();

            id = searchUserId.getId();

            if(searchUserId.getWorkcaseid()!=null)
            {
                workcaseid = searchUserId.getWorkcaseid();
            }

            log.info("id value is : {}",id);

            log.info("workcaseid value is : {}",workcaseid);
        }*/

        return workCaseIdList;

    }
}
