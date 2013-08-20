package com.clevel.selos.dao.testdao;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.model.db.testrm.CardType;
import org.slf4j.Logger;

import javax.inject.Inject;

/**
 * Created with IntelliJ IDEA.
 * User: sahawat
 * Date: 16/8/2556
 * Time: 17:44 น.
 * To change this template use File | Settings | File Templates.
 */
public class CardTypeDao extends GenericDAO<CardType,String> {
    @Inject
    private Logger log;

    @Inject
    public CardTypeDao(){

    }


}
