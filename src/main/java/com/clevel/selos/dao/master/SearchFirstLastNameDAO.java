package com.clevel.selos.dao.master;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.SearchFirstLastName;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.hibernate.Criteria;

public class SearchFirstLastNameDAO extends GenericDAO<SearchFirstLastName,String>
{
    @Inject
    @SELOS
    Logger log;

    List<Integer> relationIdList;

    public SearchFirstLastNameDAO()
    {
        relationIdList = new ArrayList<Integer>();

        relationIdList.add(1);

        relationIdList.add(2);
    }

    public List<SearchFirstLastName>  getFirstName(String firstname)
    {
        List<SearchFirstLastName> firstLastNameList = new ArrayList<SearchFirstLastName>();

        log.info("Controller comes to getFirstLastName method of SearchFirstLastNameDAO class");

        Criteria criteria = getSession().createCriteria(SearchFirstLastName.class);

        criteria.setProjection( Projections.projectionList().add(Projections.property("id"), "id").add(Projections.property("workCaseId"), "workCaseId").add(Projections.property("wrokCasePreScreenId"), "wrokCasePreScreenId"));

        criteria.add(Restrictions.disjunction().add(Restrictions.like("firstnameenglish", firstname+"%")).add(Restrictions.like("firstnametai", firstname+"%")));

        criteria.add(Restrictions.in("relationId",relationIdList)).setResultTransformer(Transformers.aliasToBean(SearchFirstLastName.class));

        //criteria.add(Restrictions.disjunction().add(Restrictions.like("lastnameenglish", "lastname%")).add(Restrictions.like("lastnametai", "lastname%"))).setResultTransformer(Transformers.aliasToBean(SearchFirstLastName.class));

        firstLastNameList = criteria.list();

        log.info("firstlastnamelist size is : {}",firstLastNameList.size());

        Iterator iterator = firstLastNameList.iterator();

        int firstnamelastnamebasedworkcaseid = 0;

        while(iterator.hasNext() == true)
        {
            SearchFirstLastName searchFirstLastName = new SearchFirstLastName();

            searchFirstLastName = (SearchFirstLastName)iterator.next();

            firstnamelastnamebasedworkcaseid = searchFirstLastName.getId();

        }

        return firstLastNameList;
    }

    public List<SearchFirstLastName>  getLastName(String lastname)
    {
        List<SearchFirstLastName> lastNameList = new ArrayList<SearchFirstLastName>();

        log.info("Controller comes to getLastName method of SearchFirstLastNameDAO class");

        Criteria criteria = getSession().createCriteria(SearchFirstLastName.class);

        criteria.setProjection( Projections.projectionList().add(Projections.property("id"), "id").add(Projections.property("workCaseId"), "workCaseId").add(Projections.property("wrokCasePreScreenId"), "wrokCasePreScreenId"));

        //criteria.add(Restrictions.disjunction().add(Restrictions.like("firstnameenglish", "firstname%")).add(Restrictions.like("firstnametai", "firstname%")));

        criteria.add(Restrictions.in("relationId",relationIdList));

        criteria.add(Restrictions.disjunction().add(Restrictions.like("lastnameenglish", lastname+"%")).add(Restrictions.like("lastnametai", lastname+"%"))).setResultTransformer(Transformers.aliasToBean(SearchFirstLastName.class));

        lastNameList = criteria.list();

        log.info("firstlastnamelist size is : {}",lastNameList.size());

        Iterator iterator = lastNameList.iterator();

        int firstnamelastnamebasedworkcaseid = 0;

        while(iterator.hasNext() == true)
        {
            SearchFirstLastName searchFirstLastName = new SearchFirstLastName();

            searchFirstLastName = (SearchFirstLastName)iterator.next();

            firstnamelastnamebasedworkcaseid = searchFirstLastName.getId();

        }

        return lastNameList;
    }
}
