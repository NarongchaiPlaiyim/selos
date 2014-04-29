package com.clevel.selos.dao.master;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.SearchFirstLastName;
import org.hibernate.criterion.Order;
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

    public String getBorrowerNameByWorkCaseID(Long workCaseID)
    {
        Criteria criteria = getSession().createCriteria(SearchFirstLastName.class);

        criteria.setProjection(Projections.projectionList().add(Projections.property("firstnametai"),"firstnametai").add(Projections.property("lastnametai"),"lastnametai").add(Projections.property("relationId"),"relationId"));

        criteria.add(Restrictions.in("relationId",relationIdList));

        criteria.add(Restrictions.eq("workCaseId",workCaseID.intValue())).addOrder(Order.asc("relationId")).setResultTransformer(Transformers.aliasToBean(SearchFirstLastName.class));

        List namesList = criteria.list();

        Iterator namesIterator = namesList.iterator();

        StringBuffer names = new StringBuffer();

        while (namesIterator.hasNext())
        {

            SearchFirstLastName searchFirstLastName = (SearchFirstLastName) namesIterator.next();

            if(searchFirstLastName.getRelationId()==1)
            {
                if(searchFirstLastName.getFirstnametai() != null && searchFirstLastName.getLastnametai() !=null)
                {
                    names.append("(B) "+searchFirstLastName.getFirstnametai()+" "+searchFirstLastName.getLastnametai()+",");
                }

                else if(searchFirstLastName.getFirstnametai()!=null)
                {
                    names.append("(B) "+searchFirstLastName.getFirstnametai());
                }

                else if(searchFirstLastName.getLastnametai()!=null)
                {
                    names.append("(B) "+searchFirstLastName.getLastnametai());
                }
                else
                {
                    if(searchFirstLastName.getFirstnameenglish()!=null && searchFirstLastName.getLastnameenglish()!=null)
                    {
                        names.append("(B) "+searchFirstLastName.getFirstnameenglish()+" "+searchFirstLastName.getLastnameenglish()+", ");
                    }
                    if(searchFirstLastName.getFirstnameenglish()!=null)
                    {
                        names.append("(B) "+searchFirstLastName.getFirstnameenglish()+", ");
                    }
                    else if(searchFirstLastName.getLastnameenglish()!=null)
                    {
                        names.append("(B) "+searchFirstLastName.getLastnameenglish()+", ");
                    }

                }

            }

            else if(searchFirstLastName.getRelationId()==2)
            {

                if(searchFirstLastName.getFirstnametai() != null && searchFirstLastName.getLastnametai() !=null)
                {
                    names.append("(G) "+searchFirstLastName.getFirstnametai()+" "+searchFirstLastName.getLastnametai()+",");
                }

                else if(searchFirstLastName.getFirstnametai()!=null)
                {
                    names.append("(G) "+searchFirstLastName.getFirstnametai());
                }

                else if(searchFirstLastName.getLastnametai()!=null)
                {
                    names.append("(G) "+searchFirstLastName.getLastnametai());
                }
                else
                {
                    if(searchFirstLastName.getFirstnameenglish()!=null && searchFirstLastName.getLastnameenglish()!=null)
                    {
                        names.append("(G) "+searchFirstLastName.getFirstnameenglish()+" "+searchFirstLastName.getLastnameenglish()+", ");
                    }
                    if(searchFirstLastName.getFirstnameenglish()!=null)
                    {
                        names.append("(G) "+searchFirstLastName.getFirstnameenglish()+", ");
                    }
                    else if(searchFirstLastName.getLastnameenglish()!=null)
                    {
                        names.append("(G) "+searchFirstLastName.getLastnameenglish()+", ");
                    }

                }

            }
        }

        String strNames = "";

        if(names.indexOf(",")>0)
        {
            strNames = names.substring(0,names.length()-1);
        }

        return strNames;
    }

    public String getBorrowerNameByWorkCasePrescreenID(Long wrokCasePreScreenId)
    {
        Criteria criteria = getSession().createCriteria(SearchFirstLastName.class);

        criteria.setProjection(Projections.projectionList().add(Projections.property("firstnameenglish"),"firstnameenglish").add(Projections.property("lastnameenglish"),"lastnameenglish").add(Projections.property("firstnametai"),"firstnametai").add(Projections.property("lastnametai"),"lastnametai").add(Projections.property("relationId"),"relationId"));

        criteria.add(Restrictions.in("relationId",relationIdList));

        criteria.add(Restrictions.eq("wrokCasePreScreenId",wrokCasePreScreenId.intValue())).addOrder(Order.asc("relationId")).setResultTransformer(Transformers.aliasToBean(SearchFirstLastName.class));

        List namesList = criteria.list();

        Iterator namesIterator = namesList.iterator();

        StringBuffer names = new StringBuffer();

        while (namesIterator.hasNext())
        {

            SearchFirstLastName searchFirstLastName = (SearchFirstLastName) namesIterator.next();

            if(searchFirstLastName.getRelationId()==1)
            {
                if(searchFirstLastName.getFirstnametai() != null && searchFirstLastName.getLastnametai() !=null)
                {
                    names.append("(B) "+searchFirstLastName.getFirstnametai()+" "+searchFirstLastName.getLastnametai()+",");
                }

                else if(searchFirstLastName.getFirstnametai()!=null)
                {
                    names.append("(B) "+searchFirstLastName.getFirstnametai());
                }

                else if(searchFirstLastName.getLastnametai()!=null)
                {
                    names.append("(B) "+searchFirstLastName.getLastnametai());
                }
                else
                {
                    if(searchFirstLastName.getFirstnameenglish()!=null && searchFirstLastName.getLastnameenglish()!=null)
                    {
                        names.append("(B) "+searchFirstLastName.getFirstnameenglish()+" "+searchFirstLastName.getLastnameenglish()+", ");
                    }
                    if(searchFirstLastName.getFirstnameenglish()!=null)
                    {
                        names.append("(B) "+searchFirstLastName.getFirstnameenglish()+", ");
                    }
                    else if(searchFirstLastName.getLastnameenglish()!=null)
                    {
                        names.append("(B) "+searchFirstLastName.getLastnameenglish()+", ");
                    }

                }

            }

            else if(searchFirstLastName.getRelationId()==2)
            {

                if(searchFirstLastName.getFirstnametai() != null && searchFirstLastName.getLastnametai() !=null)
                {
                    names.append("(G) "+searchFirstLastName.getFirstnametai()+" "+searchFirstLastName.getLastnametai()+",");
                }

                else if(searchFirstLastName.getFirstnametai()!=null)
                {
                    names.append("(G) "+searchFirstLastName.getFirstnametai());
                }

                else if(searchFirstLastName.getLastnametai()!=null)
                {
                    names.append("(G) "+searchFirstLastName.getLastnametai());
                }
                else
                {
                    if(searchFirstLastName.getFirstnameenglish()!=null && searchFirstLastName.getLastnameenglish()!=null)
                    {
                        names.append("(G) "+searchFirstLastName.getFirstnameenglish()+" "+searchFirstLastName.getLastnameenglish()+", ");
                    }
                    if(searchFirstLastName.getFirstnameenglish()!=null)
                    {
                        names.append("(G) "+searchFirstLastName.getFirstnameenglish()+", ");
                    }
                    else if(searchFirstLastName.getLastnameenglish()!=null)
                    {
                        names.append("(G) "+searchFirstLastName.getLastnameenglish()+", ");
                    }

                }


            }

        }

        String strNames = "";

        if(names.indexOf(",")>0)
        {
            strNames = names.substring(0,names.length()-1);
        }

        return strNames;
    }
}
