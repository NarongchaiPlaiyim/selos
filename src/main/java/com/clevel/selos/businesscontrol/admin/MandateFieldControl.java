package com.clevel.selos.businesscontrol.admin;


import com.clevel.selos.businesscontrol.BusinessControl;
import com.clevel.selos.dao.master.MandateFieldClassDAO;
import com.clevel.selos.dao.master.MandateFieldDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.MandateFieldType;
import com.clevel.selos.model.db.master.MandateField;
import com.clevel.selos.model.db.master.MandateFieldClass;
import com.clevel.selos.model.view.MandateFieldClassView;
import com.clevel.selos.model.view.MandateFieldView;
import com.clevel.selos.transform.MandateFieldTransform;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.lang.reflect.Field;
import java.util.*;

@Stateless
public class MandateFieldControl extends BusinessControl {
    private static final long serialVersionUID = -4625744349595576016L;
    @Inject
    @SELOS
    Logger logger;

    @Inject
    MandateFieldClassDAO mandateFieldClassDAO;
    @Inject
    MandateFieldDAO mandateFieldDAO;

    @Inject
    MandateFieldTransform mandateFieldTransform;

    @Inject
    public MandateFieldControl(){}

    public List<MandateFieldClassView> getMandateFieldClass(Set<Class<?>> classSet){
        logger.debug("-- begin getMandateFieldClass class size{} + ", classSet == null?0:classSet.size());
        List<MandateFieldClass> mandateFieldClassList = mandateFieldClassDAO.findAll();

        logger.info("number of mandateFieldClass: {}", mandateFieldClassList.size());
        Map<String, MandateFieldClassView> mandateFieldClassViewMap = new TreeMap<String, MandateFieldClassView>();
        for(Class clazz : classSet){
            MandateFieldClassView mandateFieldClassView = null;
            MandateFieldClass matchedClass = null;
            for(MandateFieldClass mandateFieldClass : mandateFieldClassList){
                if(mandateFieldClass.getClassName().equals(clazz.getName())){
                    mandateFieldClassView = mandateFieldTransform.transformToView(mandateFieldClass);
                    matchedClass = mandateFieldClass;
                    break;
                }
            }
            if(mandateFieldClassView != null){
                if(matchedClass != null){
                    mandateFieldClassList.remove(matchedClass);
                    logger.info("number of mandateFieldClass: {}", mandateFieldClassList.size());
                }
            } else {
                mandateFieldClassView = new MandateFieldClassView();
                mandateFieldClassView.setClassName(clazz.getName());
                mandateFieldClassView.setPageName("");
                mandateFieldClassView.setClassDescription("");
                mandateFieldClassView.setActive(true);
            }
            mandateFieldClassViewMap.put(mandateFieldClassView.getClassName(), mandateFieldClassView);
        }

        List<MandateFieldClassView> mandateFieldClassViewList = new ArrayList<MandateFieldClassView>();
        for(MandateFieldClassView mandateFieldClassView : mandateFieldClassViewMap.values()){
            mandateFieldClassViewList.add(mandateFieldClassView);
        }

        return mandateFieldClassViewList;
    }

    public List<MandateFieldView> getMandateField(MandateFieldClassView mandateFieldClassView) throws Exception{
        logger.info("-- begin getMandateField: {}", mandateFieldClassView);

        List<MandateFieldView> mandateFieldViewList = new ArrayList<MandateFieldView>();
        if(mandateFieldClassView == null){
            return null;
        }

        try{
            Class clazz = Class.forName(mandateFieldClassView.getClassName());
            Field[] fields = clazz.getDeclaredFields();
            logger.info("fields {}", fields.length);
            List<MandateField> mandateFieldList = new ArrayList<MandateField>();
            if(mandateFieldClassView.getId() != 0)
                mandateFieldList = mandateFieldDAO.findByClass(mandateFieldClassView.getId());

            for(Field field : fields){
                MandateFieldView mandateFieldView = null;
                MandateField matchedField = null;
                for(MandateField mandateField : mandateFieldList){
                    if(mandateField.getFieldName().equals(field.getName())){
                        mandateFieldView = mandateFieldTransform.transformToView(mandateField);
                        matchedField = mandateField;
                        break;
                    }
                }
                if(mandateFieldView != null){
                    mandateFieldList.remove(matchedField);
                } else {
                    mandateFieldView = new MandateFieldView();
                    mandateFieldView.setMandateFieldClassView(mandateFieldClassView);
                    mandateFieldView.setMandateFieldType(MandateFieldType.FIELD_TYPE);
                    mandateFieldView.setFieldName(field.getName());
                }
                mandateFieldViewList.add(mandateFieldView);
            }

        } catch (ClassNotFoundException cnf){
            logger.error("Class Not Found Exception: {}", cnf);
            throw cnf;
        }
        logger.info("-- end getMandateField return size: {}", mandateFieldViewList);
        return mandateFieldViewList;
    }
}
