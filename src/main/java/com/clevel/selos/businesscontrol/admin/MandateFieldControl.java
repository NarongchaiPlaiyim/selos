package com.clevel.selos.businesscontrol.admin;


import com.clevel.selos.businesscontrol.BusinessControl;
import com.clevel.selos.dao.master.*;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.MandateFieldType;
import com.clevel.selos.model.db.master.*;
import com.clevel.selos.model.view.*;
import com.clevel.selos.transform.ActionTransform;
import com.clevel.selos.transform.MandateFieldTransform;
import com.clevel.selos.transform.StepTransform;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.faces.model.SelectItem;
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
    MandateFieldConditionDAO mandateFieldConditionDAO;
    @Inject
    MandateFieldConditionDetailDAO mandateFieldConditionDetailDAO;
    @Inject
    MandateFieldStepActionDAO mandateFieldStepActionDAO;
    @Inject
    MandateFieldConStepActionDAO mandateFieldConStepActionDAO;
    @Inject
    StepDAO stepDAO;
    @Inject
    ActionDAO actionDAO;

    @Inject
    MandateFieldTransform mandateFieldTransform;
    @Inject
    StepTransform stepTransform;
    @Inject
    ActionTransform actionTransform;

    @Inject
    public MandateFieldControl(){}

    public List<MandateFieldClassView> getMandateFieldClass(Set<Class<?>> classSet){
        logger.debug("-- begin getMandateFieldClass class size{} + ", classSet == null?0:classSet.size());
        List<MandateFieldClass> mandateFieldClassList = mandateFieldClassDAO.findAll();

        logger.info("number of mandateFieldClass: {}", mandateFieldClassList.size());
        Map<String, MandateFieldClassView> mandateFieldClassViewMap = new TreeMap<String, MandateFieldClassView>();
        for(Class clazz : classSet){
            if(!clazz.getName().contains("Abstract"))
            {
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
        }

        List<MandateFieldClassView> mandateFieldClassViewList = new ArrayList<MandateFieldClassView>();
        for(MandateFieldClassView mandateFieldClassView : mandateFieldClassViewMap.values()){
            mandateFieldClassViewList.add(mandateFieldClassView);
        }

        return mandateFieldClassViewList;
    }

    public List<MandateFieldClassView> getMandateFieldClass(){
        List<MandateFieldClass> mandateFieldClassList = mandateFieldClassDAO.findAll();
        List<MandateFieldClassView> mandateFieldClassViewList = new ArrayList<MandateFieldClassView>();
        for(MandateFieldClass mandateFieldClass : mandateFieldClassList){
            MandateFieldClassView mandateFieldClassView = mandateFieldTransform.transformToView(mandateFieldClass);
            mandateFieldClassViewList.add(mandateFieldClassView);
        }
        return mandateFieldClassViewList;
    }

    public List<MandateFieldView> getMandateFieldDB(MandateFieldClassView mandateFieldClassView){
        List<MandateField> mandateFieldList = mandateFieldDAO.findByClass(mandateFieldClassView.getId());
        List<MandateFieldView> mandateFieldViewList = new ArrayList<MandateFieldView>();
        for(MandateField mandateField :mandateFieldList){
            MandateFieldView mandateFieldView = mandateFieldTransform.transformToView(mandateField);
            mandateFieldViewList.add(mandateFieldView);
        }
        return mandateFieldViewList;
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

    public List<MandateFieldConditionView> getMandateConditionList(MandateFieldClassView mandateFieldClassView){
        List<MandateFieldConditionView> mandateFieldConditionViewList = new ArrayList<MandateFieldConditionView>();
        if(mandateFieldClassView.getId() > 0){
            List<MandateFieldCondition> mandateFieldConditionList = mandateFieldConditionDAO.findByClass(mandateFieldClassView.getId());
            for(MandateFieldCondition mandateFieldCondition : mandateFieldConditionList){
                MandateFieldConditionView mandateFieldConditionView = mandateFieldTransform.transformToView(mandateFieldCondition);
                mandateFieldConditionViewList.add(mandateFieldConditionView);
            }

            for(MandateFieldCondition mandateFieldCondition : mandateFieldConditionList){
                if(mandateFieldCondition.getDependCondition() != null){
                    MandateFieldConditionView _toUpdate = null;
                    MandateFieldConditionView _toDepend = null;
                    for(MandateFieldConditionView conditionView : mandateFieldConditionViewList){
                        if(conditionView.getId() == mandateFieldCondition.getId()){
                            _toUpdate = conditionView;
                        }
                        if(conditionView.getId() == mandateFieldCondition.getDependCondition().getId()){
                            _toDepend = conditionView;
                        }
                    }
                    if(_toUpdate != null && _toDepend != null)
                        _toUpdate.setDependCondition(_toDepend);

                }
            }

        }
        return mandateFieldConditionViewList;
    }

    public void saveAllMandateField(MandateFieldClassView mandateFieldClassView, List<MandateFieldView> mandateFieldViewList, List<MandateFieldConditionView> mandateFieldConditionViewList){
        logger.debug("-- begin saveAllMandateField");
        MandateFieldClass mandateFieldClass = saveMandateField(mandateFieldClassView, mandateFieldViewList);
        logger.debug("save succeed mandateFieldClass: {}", mandateFieldClass);

        saveMandateCondition(mandateFieldClass, mandateFieldConditionViewList);

        logger.debug("-- end saveAllMandateField");

    }

    private MandateFieldClass saveMandateField(MandateFieldClassView mandateFieldClassView, List<MandateFieldView> mandateFieldViewList){
        logger.debug("-- begin saveMandateFieldClass {}", mandateFieldClassView);
        MandateFieldClass mandateFieldClass = null;
        if(mandateFieldClassView != null){
            mandateFieldClass = mandateFieldTransform.transformToModel(mandateFieldClassView);
            mandateFieldClass = mandateFieldClassDAO.persist(mandateFieldClass);
            for(MandateFieldView mandateFieldView : mandateFieldViewList){
                if(mandateFieldView.isNeedUpdate()){
                    logger.debug("-- saveMandateFieldDetail {}", mandateFieldView);
                    MandateField mandateField = mandateFieldTransform.transformToModel(mandateFieldView, mandateFieldClass);
                    mandateFieldDAO.persist(mandateField);
                }
            }
        }
        logger.debug("-- end saveMandateFieldClass");
        return mandateFieldClass;
    }

    private void saveMandateCondition(MandateFieldClass mandateFieldClass, List<MandateFieldConditionView> mandateFieldConditionViewList){
        List<MandateFieldCondition> mandateFieldConditionList = new ArrayList<MandateFieldCondition>();
        for(MandateFieldConditionView mandateFieldConditionView : mandateFieldConditionViewList){

            MandateFieldCondition mandateFieldCondition = mandateFieldTransform.transformToModel(mandateFieldConditionView);
            mandateFieldCondition.setMandateFieldClass(mandateFieldClass);
            mandateFieldConditionList.add(mandateFieldCondition);
            if(mandateFieldConditionView.isNeedUpdate()){
                mandateFieldConditionDAO.persist(mandateFieldCondition);
            }

            for(MandateFieldConditionDetailView mandateFieldConditionDetailView : mandateFieldConditionView.getConditionDetailViewList()){
                MandateFieldConditionDetail mandateFieldConditionDetail = mandateFieldTransform.transformToModel(mandateFieldConditionDetailView);
                mandateFieldConditionDetail.setMandateFieldCondition(mandateFieldCondition);
                if(mandateFieldConditionDetailView.isNeedUpdate()){
                    mandateFieldConditionDetail.setMandateFieldCondition(mandateFieldCondition);
                    mandateFieldConditionDetailDAO.persist(mandateFieldConditionDetail);
                }
            }
        }

        for(MandateFieldConditionView mandateFieldConditionView : mandateFieldConditionViewList){
            if(mandateFieldConditionView.getDependCondition() != null){
                MandateFieldCondition toUpdate = null;
                MandateFieldCondition toDepend = null;
                for(MandateFieldCondition mandateFieldCondition : mandateFieldConditionList){
                    if(mandateFieldCondition.getName().equals(mandateFieldConditionView.getName())){
                        toUpdate = mandateFieldCondition;
                    }
                    if(mandateFieldCondition.getName().equals(mandateFieldConditionView.getDependCondition().getName())){
                        toDepend = mandateFieldCondition;
                    }
                }
                if(toUpdate != null && toDepend != null){
                    toUpdate.setDependCondition(toDepend);
                    mandateFieldConditionDAO.persist(toUpdate);
                }
            }
        }
    }

    public void deleteAllMandateField(List<MandateFieldConditionView> deletedMandateFieldConditionViewList, List<MandateFieldConditionDetailView> deletedMandateFieldConditionDetailView){
        if(deletedMandateFieldConditionDetailView != null){
            for(MandateFieldConditionDetailView mandateFieldConditionDetailView : deletedMandateFieldConditionDetailView){
                mandateFieldConditionDetailDAO.deleteById(mandateFieldConditionDetailView.getId());
            }
        }

        if(deletedMandateFieldConditionViewList != null){
            for (MandateFieldConditionView mandateFieldConditionView : deletedMandateFieldConditionViewList){
                mandateFieldConditionDAO.deleteById(mandateFieldConditionView.getId());
            }
        }
    }

    public MandateFieldStepActionView getMandateFieldStepAction(SelectItem step, SelectItem action){
        logger.info("-- begin getMandateFieldStepAction step: {}, action:{}", step, action);
        List<MandateFieldStepAction> mandateFieldStepActionList = mandateFieldStepActionDAO.findByAction((Long)step.getValue(), (Long)action.getValue());
        List<MandateFieldConStepAction> mandateFieldConStepActionList = mandateFieldConStepActionDAO.findByAction((Long)step.getValue(), (Long)action.getValue());

        MandateFieldStepActionView mandateFieldStepActionView = mandateFieldTransform.transformToView(mandateFieldStepActionList, mandateFieldConStepActionList);

        if(mandateFieldStepActionView == null){
            mandateFieldStepActionView = new MandateFieldStepActionView();
            mandateFieldStepActionView.setStepView(stepTransform.transformToView(step));
            mandateFieldStepActionView.setActionView(actionTransform.transformToView(action));
        }
        logger.info("-- end getMandateFieldStepAction return {}", mandateFieldStepActionView);
        return mandateFieldStepActionView;
    }


    public void saveMandateFieldClassStepAction(MandateFieldClassStepActionView mandateFieldClassStepActionView, StepView stepView, ActionView actionView){
        if(mandateFieldClassStepActionView != null){
            Step step = stepDAO.findById(stepView.getId());
            Action action = actionDAO.findById(actionView.getId());
            saveMandateFieldStepAction(mandateFieldClassStepActionView.getMandateFieldViewList(), step, action);
            saveMandateFieldConStepAction(mandateFieldClassStepActionView.getMandateFieldConditionViewList(), step, action);
        }
    }

    public void saveMandateFieldStepAction(List<MandateFieldView> mandateFieldViewList, Step step, Action action){
        logger.info("-- begin saveMandateFieldStepAction {}, {}, {}", mandateFieldViewList, step, action);
        if(mandateFieldViewList != null && mandateFieldViewList.size() > 0){

            List<MandateFieldStepAction> mandateFieldStepActionList = new ArrayList<MandateFieldStepAction>();

            for(MandateFieldView mandateFieldView : mandateFieldViewList){
                MandateFieldStepAction mandateFieldStepAction = mandateFieldStepActionDAO.findByActionAndField(step.getId(), action.getId(), mandateFieldView.getId());
                if(mandateFieldStepAction == null){
                    MandateField mandateField = mandateFieldTransform.transformToModel(mandateFieldView);
                    mandateFieldStepAction = new MandateFieldStepAction();
                    mandateFieldStepAction.setAction(action);
                    mandateFieldStepAction.setStep(step);
                    mandateFieldStepAction.setMandateField(mandateField);
                }
                mandateFieldStepActionList.add(mandateFieldStepAction);
            }
            if(mandateFieldStepActionList.size() > 0) {
                logger.info("-- saving mandateFieldStepActionList {}", mandateFieldStepActionList);
                mandateFieldStepActionDAO.persist(mandateFieldStepActionList);

            }
        }
        logger.info("-- end saveMandateFieldStepAction");
    }

    public void saveMandateFieldConStepAction(List<MandateFieldConditionView> mandateFieldConditionViewList, Step step, Action action){
        logger.info("-- begin saveMandateFieldConStepAction {}, {}, {}", mandateFieldConditionViewList, step, action);
        if(mandateFieldConditionViewList != null && mandateFieldConditionViewList.size() > 0) {
        List<MandateFieldConStepAction> conStepActionList = new ArrayList<MandateFieldConStepAction>();
            for(MandateFieldConditionView conditionView : mandateFieldConditionViewList){
                MandateFieldConStepAction mandateFieldConStepAction = mandateFieldConStepActionDAO.findByActionAndCon(step.getId(), action.getId(), conditionView.getId());
                if(mandateFieldConStepAction == null){
                    MandateFieldCondition mandateFieldCondition = mandateFieldConditionDAO.findById(conditionView.getId());
                    mandateFieldConStepAction = new MandateFieldConStepAction();
                    mandateFieldConStepAction.setStep(step);
                    mandateFieldConStepAction.setAction(action);
                    mandateFieldConStepAction.setMandateFieldCondition(mandateFieldCondition);
                }
                conStepActionList.add(mandateFieldConStepAction);
            }

            if(conStepActionList.size() > 0) {
                logger.info("-- saving conStepActionList {}", conStepActionList);
                mandateFieldConStepActionDAO.persist(conStepActionList);
            }
        }
        logger.info("-- end saveMandateFieldConStepAction");
    }

    public void deleteMandateFieldStepAction(List<MandateFieldView> deletedMandateFieldViewList, StepView stepView, ActionView actionView){
        logger.info("-- begin deleteMandateFieldStepAction {}", deletedMandateFieldViewList);
        if(deletedMandateFieldViewList != null && deletedMandateFieldViewList.size() > 0){
            for(MandateFieldView mandateFieldView : deletedMandateFieldViewList){
                MandateFieldStepAction mandateFieldStepAction = mandateFieldStepActionDAO.findByActionAndField(stepView.getId(), actionView.getId(), mandateFieldView.getId());
                if(mandateFieldStepAction != null)
                    mandateFieldStepActionDAO.delete(mandateFieldStepAction);
            }
        }
        logger.info("-- end deleteMandateFieldStepAction");
    }

    public void deleteMandateFieldConStepAction(List<MandateFieldConditionView> delectedMandateConViewList, StepView stepView, ActionView actionView){
        logger.info("-- begin deleteMandateFieldConStepAction {}, {}, {}", delectedMandateConViewList, stepView, actionView);
        if(delectedMandateConViewList != null & delectedMandateConViewList.size() > 0){
            for(MandateFieldConditionView conditionView : delectedMandateConViewList){
                MandateFieldConStepAction mandateFieldConStepAction = mandateFieldConStepActionDAO.findByActionAndCon(stepView.getId(), actionView.getId(), conditionView.getId());
                if(mandateFieldConStepAction != null)
                    mandateFieldConStepActionDAO.delete(mandateFieldConStepAction);
            }
        }
        logger.info("-- end deleteMandateFieldConStepAction");

    }

    public void deleteMandateFieldClassStepActionView(MandateFieldClassStepActionView mandateFieldClassStepActionView, StepView stepView, ActionView actionView){
        mandateFieldClassStepActionView.getMandateFieldViewList();
        deleteMandateFieldStepAction(mandateFieldClassStepActionView.getMandateFieldViewList(), stepView, actionView);
        deleteMandateFieldConStepAction(mandateFieldClassStepActionView.getMandateFieldConditionViewList(), stepView, actionView);
    }
}
