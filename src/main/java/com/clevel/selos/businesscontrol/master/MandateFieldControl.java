package com.clevel.selos.businesscontrol.master;


import com.clevel.selos.businesscontrol.BusinessControl;
import com.clevel.selos.dao.master.*;
import com.clevel.selos.integration.ADMIN;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.MandateDependConType;
import com.clevel.selos.model.MandateDependType;
import com.clevel.selos.model.db.master.*;
import com.clevel.selos.model.db.master.*;
import com.clevel.selos.model.view.*;
import com.clevel.selos.model.view.master.*;
import com.clevel.selos.transform.ActionTransform;
import com.clevel.selos.transform.master.MandateFieldTransform;
import com.clevel.selos.transform.StepTransform;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Stateless
public class MandateFieldControl extends BusinessControl {
    private static final long serialVersionUID = -4625744349595576016L;
    @Inject
    @ADMIN
    private Logger logger;

    public static final String PACKAGE_NAME = "com.clevel.selos.model.db.working";

    @Inject
    private MandateFieldClassDAO mandateFieldClassDAO;
    @Inject
    private MandateFieldDAO mandateFieldDAO;
    @Inject
    private MandateFieldConditionDAO mandateFieldConditionDAO;
    @Inject
    private MandateFieldConditionDetailDAO mandateFieldConditionDetailDAO;
    @Inject
    private MandateFieldStepActionDAO mandateFieldStepActionDAO;
    @Inject
    private MandateFieldConStepActionDAO mandateFieldConStepActionDAO;
    @Inject
    private MandateFieldClassStepActionDAO mandateFieldClassStepActionDAO;
    @Inject
    private StepDAO stepDAO;
    @Inject
    private ActionDAO actionDAO;

    @Inject
    MandateFieldTransform mandateFieldTransform;
    @Inject
    StepTransform stepTransform;
    @Inject
    ActionTransform actionTransform;

    @Inject
    public MandateFieldControl(){}

    public List<MandateFieldClassView> getMandateFieldClass(Set<Class<?>> classSet){
        logger.debug("-- begin getMandateFieldClass class size {}", classSet == null?0:classSet.size());
        List<MandateFieldClass> mandateFieldClassList = mandateFieldClassDAO.findAll();

        logger.debug("number of mandateFieldClass: {}", mandateFieldClassList.size());
        Map<String, MandateFieldClassView> mandateFieldClassViewMap = new TreeMap<String, MandateFieldClassView>();
        for(Class clazz : classSet){
            if(!(clazz.getName().contains("Abstract")||clazz.getName().endsWith("_"))){
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
                        logger.debug("number of mandateFieldClass: {}", mandateFieldClassList.size());
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

    public MandateFieldClassView getMandateFieldClassView(String className){
        MandateFieldClass mandateFieldClass = mandateFieldClassDAO.findByClassName(className);
        return mandateFieldTransform.transformToView(mandateFieldClass);
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
        logger.debug("-- begin getMandateField: {}", mandateFieldClassView);

        List<MandateFieldView> mandateFieldViewList = new ArrayList<MandateFieldView>();
        if(mandateFieldClassView == null){
            return null;
        }

        try{
            Class clazz = Class.forName(mandateFieldClassView.getClassName());

            Field[] fields = clazz.getDeclaredFields();

            if(clazz.getSuperclass().getName().contains("AbstractWorkCase")){
                fields = combinedFields(fields, clazz.getSuperclass().getDeclaredFields());
            }

            logger.debug("fields {}", fields.length);
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
                    mandateFieldView.setFieldName(field.getName());
                }
                mandateFieldViewList.add(mandateFieldView);
            }

        } catch (ClassNotFoundException cnf){
            logger.error("Class Not Found Exception: {}", cnf);
            throw cnf;
        }
        logger.debug("-- end getMandateField return size: {}", mandateFieldViewList);
        return mandateFieldViewList;
    }

    public List<MandateFieldConditionView> getExternalDependCondition(MandateFieldClassView mandateFieldClassView){
        List<MandateFieldConditionView> _tmpList = new ArrayList<MandateFieldConditionView>();

        try{
            Class clazz = Class.forName(mandateFieldClassView.getClassName());
            Field[] fieldList = clazz.getDeclaredFields();
            for(Field field : fieldList){
                Class typeClazz = field.getType();
                logger.debug("field: {}, type: {}", field.getName(), typeClazz.getName());
                if(typeClazz.getName().contains(PACKAGE_NAME)){
                    MandateFieldClass mandateFieldClass = mandateFieldClassDAO.findByClassName(typeClazz.getName());
                    if(mandateFieldClass != null && mandateFieldClass.getId() > 0){
                        List<MandateFieldConditionView> mandateConditionList = getMandateConditionList(mandateFieldClass.getId());
                        _tmpList.addAll(mandateConditionList);
                    }
                }
            }
        } catch(ClassNotFoundException cnf){
            logger.debug("Class Not Found, external Depend List return empty", cnf);
        }
        return _tmpList;
    }

    private Field[] combinedFields(Field[] fields1, Field[] fields2){
        logger.debug("begin combinedFields {}, {}", fields1, fields2);
        List<Field> _combinedList = new ArrayList<Field>();
        for(Field field : fields1){
            _combinedList.add(field);
        }
        if(fields2 != null){
            for (Field field : fields2){
                _combinedList.add(field);
            }
        }

        return _combinedList.toArray(new Field[_combinedList.size()]);
    }

    public List<MandateFieldConditionView> getMandateConditionList(MandateFieldClassView mandateFieldClassView){
        return getMandateConditionList(mandateFieldClassView.getId());

    }

    public List<MandateFieldConditionView> getMandateConditionList(long mandateClassId){
        logger.debug("-- begin getMandateConditionList: {}", mandateClassId);
        List<MandateFieldConditionView> mandateFieldConditionViewList = new ArrayList<MandateFieldConditionView>();
        if(mandateClassId > 0){
            List<MandateFieldCondition> mandateFieldConditionList = mandateFieldConditionDAO.findByClass(mandateClassId);
            for(MandateFieldCondition mandateFieldCondition : mandateFieldConditionList){
                MandateFieldConditionView mandateFieldConditionView = mandateFieldTransform.transformToView(mandateFieldCondition);
                mandateFieldConditionViewList.add(mandateFieldConditionView);
            }
            logger.debug("Mandate FieldCondition: {}", mandateFieldConditionList);

            for(MandateFieldCondition condition : mandateFieldConditionList) {
                if(condition.getDependCondition() > 0){
                MandateFieldConditionView _toUpdate = null;
                MandateFieldConditionView _toDepend = null;
                for(MandateFieldConditionView view : mandateFieldConditionViewList){
                    if(condition.getId() == view.getId()){
                        _toUpdate = view;
                        break;
                    }
                }

                if(MandateDependConType.INTERNAL.equals(condition.getDependConType())){
                    for(MandateFieldConditionView view : mandateFieldConditionViewList){
                        if(condition.getDependCondition() == view.getId()) {
                            _toDepend = view;
                        }
                    }
                } else if(MandateDependConType.EXTERNAL.equals(condition.getDependConType())){
                    MandateFieldCondition _tmpCondition = mandateFieldConditionDAO.findById(condition.getDependCondition());
                    _toDepend = mandateFieldTransform.transformToView(_tmpCondition);
                }

                if(_toUpdate != null && _toDepend != null)
                        _toUpdate.setDependCondition(_toDepend);
                }
            }
        }
        logger.debug("-- end getMandateConditionList return {}", mandateFieldConditionViewList);
        return mandateFieldConditionViewList;
    }

    public void saveMandateField(MandateFieldClassView mandateFieldClassView, List<MandateFieldView> mandateFieldViewList) {
        logger.debug("-- begin saveMandateField");
        MandateFieldClass mandateFieldClass = saveMandateFieldClass(mandateFieldClassView);
        List<MandateField> mandateFieldList = saveMandateFieldDetail(mandateFieldClass, mandateFieldViewList);
        logger.debug("-- end saveMandateField");
    }

    public MandateFieldClass saveMandateFieldClass(MandateFieldClassView mandateFieldClassView) {
        logger.debug("-- begin saveMandateFieldClass {}", mandateFieldClassView);
        MandateFieldClass mandateFieldClass = null;

        if(mandateFieldClassView != null){
            mandateFieldClass = mandateFieldTransform.transformToModel(mandateFieldClassView);
            mandateFieldClass = mandateFieldClassDAO.persist(mandateFieldClass);
        }
        logger.debug("-- end saveMandateFieldClass");
        return mandateFieldClass;
    }

    public List<MandateField> saveMandateFieldDetail(MandateFieldClass mandateFieldClass, List<MandateFieldView> mandateFieldViewList){
        List<MandateField> mandateFieldList = new ArrayList<MandateField>();
        if(mandateFieldViewList != null){
            for(MandateFieldView mandateFieldView : mandateFieldViewList){
                MandateField mandateField = mandateFieldTransform.transformToModel(mandateFieldView, mandateFieldClass);
                if(mandateFieldView.isNeedUpdate()){
                    logger.debug("-- saveMandateFieldDetail {}", mandateFieldView);
                    mandateFieldDAO.persist(mandateField);
                }
                mandateFieldList.add(mandateField);
            }
        }
        logger.debug("-- end saveMandateField: {}", mandateFieldList);
        return mandateFieldList;
    }

    public void saveMandateCondition(MandateFieldClassView mandateFieldClassView, List<MandateFieldView> mandateFieldViewList, List<MandateFieldConditionView> mandateFieldConditionViewList){
        List<MandateFieldCondition> mandateFieldConditionList = new ArrayList<MandateFieldCondition>();
        MandateFieldClass mandateFieldClass = mandateFieldClassDAO.findById(mandateFieldClassView.getId());
        List<MandateField> mandateFieldList = new ArrayList<MandateField>();
        for(MandateFieldView mandateFieldView : mandateFieldViewList){
            mandateFieldList.add(mandateFieldDAO.findById(mandateFieldView.getId()));
        }
        for(MandateFieldConditionView mandateFieldConditionView : mandateFieldConditionViewList){

            MandateFieldCondition mandateFieldCondition = mandateFieldTransform.transformToModel(mandateFieldConditionView);
            mandateFieldCondition.setMandateFieldClass(mandateFieldClass);
            mandateFieldConditionList.add(mandateFieldCondition);
            if(mandateFieldConditionView.isNeedUpdate()){
                mandateFieldConditionDAO.persist(mandateFieldCondition);
            }

            for(MandateFieldConditionDetailView mandateFieldConditionDetailView : mandateFieldConditionView.getConditionDetailViewList()){
                MandateFieldConditionDetail mandateFieldConditionDetail = mandateFieldTransform.transformToModel(mandateFieldConditionDetailView);

                if(mandateFieldConditionDetailView.getMandateFieldView() != null){
                    for(MandateField mandateField : mandateFieldList){
                        logger.debug("mandateField {}", mandateField);
                        logger.debug("mandateFieldList {}", mandateFieldConditionDetailView.getMandateFieldView());
                        if(mandateField.getFieldName().equals(mandateFieldConditionDetailView.getMandateFieldView().getFieldName())){
                            mandateFieldConditionDetail.setMandateField(mandateField);
                            break;
                        }
                    }
                    mandateFieldConditionDetail.setBaseValue(mandateFieldConditionDetailView.getBaseValue());
                    mandateFieldConditionDetail.setMandateFieldCondition(mandateFieldCondition);
                    if(mandateFieldConditionDetailView.isNeedUpdate()){
                        mandateFieldConditionDetail.setMandateFieldCondition(mandateFieldCondition);
                        mandateFieldConditionDetailDAO.persist(mandateFieldConditionDetail);
                    }
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
                        logger.debug("to Update: {}", toUpdate);
                        break;
                    }
                }

                if(mandateFieldConditionView.getDependConType().equals(MandateDependConType.INTERNAL)){
                    for(MandateFieldCondition mandateFieldCondition : mandateFieldConditionList){
                        if(mandateFieldCondition.getName().equals(mandateFieldConditionView.getDependCondition().getName())){
                            toDepend = mandateFieldCondition;
                            logger.debug(" to Depend Internal: {}", toDepend);
                            break;
                        }
                    }
                }else if(mandateFieldConditionView.getDependConType().equals(MandateDependConType.EXTERNAL)){
                    toDepend = mandateFieldConditionDAO.findById(mandateFieldConditionView.getDependCondition().getId());
                    logger.debug(" to Depend External: {}", toDepend);
                }

                if(toUpdate != null && toDepend != null){
                    toUpdate.setDependCondition(toDepend.getId());
                    mandateFieldConditionDAO.persist(toUpdate);
                }

            }
        }
    }

    public void deleteMandateFieldCondition(List<MandateFieldConditionView> deletedMandateFieldConditionViewList, List<MandateFieldConditionDetailView> deletedMandateFieldConditionDetailView){
        if(deletedMandateFieldConditionDetailView != null){
            for(MandateFieldConditionDetailView mandateFieldConditionDetailView : deletedMandateFieldConditionDetailView){
                mandateFieldConditionDetailDAO.deleteById(mandateFieldConditionDetailView.getId());
            }
        }

        if(deletedMandateFieldConditionViewList != null){
            for (MandateFieldConditionView mandateFieldConditionView : deletedMandateFieldConditionViewList){
                for(MandateFieldConditionDetailView conditionDetailView : mandateFieldConditionView.getConditionDetailViewList()){
                    if(conditionDetailView.getId() > 0)
                        mandateFieldConditionDetailDAO.deleteById(conditionDetailView.getId());
                }
                mandateFieldConditionDAO.deleteById(mandateFieldConditionView.getId());
            }
        }
    }

    public MandateFieldStepActionView getMandateFieldStepAction(long stepId, long actionId){

        logger.debug("-- begin getMandateFieldStepAction step: {}, action:{}", stepId, actionId);
        List<MandateFieldStepAction> mandateFieldStepActionList = mandateFieldStepActionDAO.findByAction(stepId, actionId);
        List<MandateFieldConStepAction> mandateFieldConStepActionList = mandateFieldConStepActionDAO.findByAction(stepId, actionId);

        MandateFieldStepActionView mandateFieldStepActionView = mandateFieldTransform.transformToView(mandateFieldStepActionList, mandateFieldConStepActionList);

        logger.debug("-- end getMandateFieldStepAction return {}", mandateFieldStepActionView);
        return mandateFieldStepActionView;

    }

    public MandateFieldStepActionView getMandateFieldStepAction(SelectItem step, SelectItem action){
        logger.debug("-- begin getMandateFieldStepAction step: {}, action:{}", step, action);

        MandateFieldStepActionView mandateFieldStepActionView = getMandateFieldStepAction((Long)step.getValue(), (Long)action.getValue());

        if(mandateFieldStepActionView == null){
            mandateFieldStepActionView = new MandateFieldStepActionView();
            mandateFieldStepActionView.setStepView(stepTransform.transformToView(step));
            mandateFieldStepActionView.setActionView(actionTransform.transformToView(action));
        }
        logger.debug("-- end getMandateFieldStepAction return {}", mandateFieldStepActionView);
        return mandateFieldStepActionView;
    }

    public Map<String, List<MandateFieldView>> getMandateFieldByStepAction(long step, long action){
        List<MandateFieldStepAction> mandateFieldStepActionList = mandateFieldStepActionDAO.findByAction(step, action);

        Map<String, List<MandateFieldView>> _tmpClassMap = new ConcurrentHashMap<String, List<MandateFieldView>>();
        for(MandateFieldStepAction mandateFieldStepAction : mandateFieldStepActionList){
            MandateFieldView mandateFieldView = mandateFieldTransform.transformToView(mandateFieldStepAction.getMandateField());
            logger.debug("Mandate Field View: {}", mandateFieldView);

            MandateFieldClassView mandateFieldClassView = mandateFieldView.getMandateFieldClassView();
            String className = mandateFieldClassView.getClassName();
            List<MandateFieldView> _tmpFieldList = _tmpClassMap.get(className);
            if(_tmpFieldList == null){
                _tmpFieldList = new ArrayList<MandateFieldView>();
            }
            _tmpFieldList.add(mandateFieldView);
            _tmpClassMap.put(className, _tmpFieldList);
        }
        return _tmpClassMap;
    }

    public Map<Long, MandateFieldConditionView> getMandateConditionByStepAction(long step, long action){
        List<MandateFieldConStepAction> mandateFieldConStepActionList = mandateFieldConStepActionDAO.findByAction(step, action);
        Map<Long, MandateFieldConditionView> _tmpMap = new ConcurrentHashMap<Long, MandateFieldConditionView>();
        for(MandateFieldConStepAction mandateFieldConStepAction : mandateFieldConStepActionList){
            MandateFieldConditionView mandateFieldConditionView = mandateFieldTransform.transformToView(mandateFieldConStepAction.getMandateFieldCondition());
            _tmpMap.put(mandateFieldConditionView.getId(), mandateFieldConditionView);
        }

        //Set Depend Condition
        for(MandateFieldConStepAction mandateFieldConStepAction : mandateFieldConStepActionList){
            MandateFieldCondition mandateFieldCondition = mandateFieldConStepAction.getMandateFieldCondition();
            if(!mandateFieldCondition.getDependType().equals(MandateDependType.NO_DEPENDENCY)){
                if(mandateFieldCondition.getDependCondition() > 0){
                    MandateFieldConditionView _toUpdate = _tmpMap.get(mandateFieldCondition.getId());
                    MandateFieldConditionView _toDepend = _tmpMap.get(mandateFieldCondition.getDependCondition());
                    _toUpdate.setDependCondition(_toDepend);
                    _tmpMap.put(_toUpdate.getId(), _toUpdate);
                }
            }
        }
        return _tmpMap;
    }

    public Map<String, MandateFieldClassStepActionView> getMandateFieldClassStepAction(long stepId, long actionId){
        Map<String, MandateFieldClassStepActionView> _tmpMap = new ConcurrentHashMap<String, MandateFieldClassStepActionView>();
        List<MandateFieldClassStepAction> mandateFieldClassStepActionList = mandateFieldClassStepActionDAO.findByStepAction(stepId, actionId);
        for(MandateFieldClassStepAction mandateFieldClassStepAction : mandateFieldClassStepActionList){
            MandateFieldClassStepActionView mandateFieldClassStepActionView = new MandateFieldClassStepActionView();
            mandateFieldClassStepActionView.setRequired(mandateFieldClassStepAction.isRequired());
            mandateFieldClassStepActionView.setMandateFieldClassView(mandateFieldTransform.transformToView(mandateFieldClassStepAction.getMandateFieldClass()));
            _tmpMap.put(mandateFieldClassStepActionView.getMandateFieldClassView().getClassName(), mandateFieldClassStepActionView);
        }
        return _tmpMap;
    }

    public void saveMandateFieldClassSAAdmin(MandateFieldClassSAAdminView mandateFieldClassSAAdminView, StepView stepView, ActionView actionView){
        if(mandateFieldClassSAAdminView != null){
            Step step = stepDAO.findById(stepView.getId());
            Action action = actionDAO.findById(actionView.getId());
            saveMandateClassRequiredStepAction(mandateFieldClassSAAdminView, step, action);
            saveMandateFieldStepAction(mandateFieldClassSAAdminView.getMandateFieldViewList(), step, action);
            saveMandateFieldConStepAction(mandateFieldClassSAAdminView.getMandateFieldConditionViewList(), step, action);
        }
    }

    public void saveMandateClassRequiredStepAction(MandateFieldClassSAAdminView classSAAdminView, Step step, Action action){
        logger.debug("-- begin saveMandateClassRequiredStepAction: {}", classSAAdminView);
        if(classSAAdminView != null){
            MandateFieldClassStepAction mandateFieldClassStepAction = mandateFieldClassStepActionDAO.findByActionAndClass(step.getId(), action.getId(), classSAAdminView.getId());
            if(classSAAdminView.isNeedUpdate()){
                if(mandateFieldClassStepAction == null){
                    mandateFieldClassStepAction = new MandateFieldClassStepAction();
                }
                mandateFieldClassStepAction.setAction(action);
                mandateFieldClassStepAction.setStep(step);
                mandateFieldClassStepAction.setRequired(classSAAdminView.isClassRequired());
                mandateFieldClassStepAction.setMandateFieldClass(mandateFieldClassDAO.findById(classSAAdminView.getId()));
                mandateFieldClassStepActionDAO.persist(mandateFieldClassStepAction);
            }
            /*else {
                if(mandateFieldClassStepAction != null)
                    mandateFieldClassStepActionDAO.delete(mandateFieldClassStepAction);
            }*/
        }
    }

    public void saveMandateFieldStepAction(List<MandateFieldView> mandateFieldViewList, Step step, Action action){
        logger.debug("-- begin saveMandateFieldStepAction {}, {}, {}", mandateFieldViewList, step, action);
        if(mandateFieldViewList != null && mandateFieldViewList.size() > 0){

            List<MandateFieldStepAction> mandateFieldStepActionList = new ArrayList<MandateFieldStepAction>();

            for(MandateFieldView mandateFieldView : mandateFieldViewList){
                MandateFieldStepAction mandateFieldStepAction = mandateFieldStepActionDAO.findByActionAndField(step.getId(), action.getId(), mandateFieldView.getId());
                if(mandateFieldStepAction == null){
                    mandateFieldStepAction = new MandateFieldStepAction();
                }
                MandateField mandateField = mandateFieldTransform.transformToModel(mandateFieldView);
                mandateFieldStepAction.setAction(action);
                mandateFieldStepAction.setStep(step);
                mandateFieldStepAction.setMandateField(mandateField);
                mandateFieldStepActionList.add(mandateFieldStepAction);
            }
            if(mandateFieldStepActionList.size() > 0) {
                logger.debug("-- saving mandateFieldStepActionList {}", mandateFieldStepActionList);
                mandateFieldStepActionDAO.persist(mandateFieldStepActionList);

            }
        }
        logger.debug("-- end saveMandateFieldStepAction");
    }

    public void saveMandateFieldConStepAction(List<MandateFieldConditionView> mandateFieldConditionViewList, Step step, Action action){
        logger.debug("-- begin saveMandateFieldConStepAction {}, {}, {}", mandateFieldConditionViewList, step, action);
        if(mandateFieldConditionViewList != null && mandateFieldConditionViewList.size() > 0) {
        List<MandateFieldConStepAction> conStepActionList = new ArrayList<MandateFieldConStepAction>();
            for(MandateFieldConditionView conditionView : mandateFieldConditionViewList){
                MandateFieldConStepAction mandateFieldConStepAction = mandateFieldConStepActionDAO.findByActionAndCon(step.getId(), action.getId(), conditionView.getId());
                if(mandateFieldConStepAction == null){
                    mandateFieldConStepAction = new MandateFieldConStepAction();
                }
                MandateFieldCondition mandateFieldCondition = mandateFieldConditionDAO.findById(conditionView.getId());
                mandateFieldConStepAction.setStep(step);
                mandateFieldConStepAction.setAction(action);
                mandateFieldConStepAction.setMandateFieldCondition(mandateFieldCondition);
                conStepActionList.add(mandateFieldConStepAction);
            }

            if(conStepActionList.size() > 0) {
                logger.debug("-- saving conStepActionList {}", conStepActionList);
                mandateFieldConStepActionDAO.persist(conStepActionList);
            }
        }
        logger.debug("-- end saveMandateFieldConStepAction");
    }

    public void deleteMandateFieldStepAction(List<MandateFieldView> deletedMandateFieldViewList, StepView stepView, ActionView actionView){
        logger.debug("-- begin deleteMandateFieldStepAction {}", deletedMandateFieldViewList);
        if(deletedMandateFieldViewList != null && deletedMandateFieldViewList.size() > 0){
            for(MandateFieldView mandateFieldView : deletedMandateFieldViewList){
                MandateFieldStepAction mandateFieldStepAction = mandateFieldStepActionDAO.findByActionAndField(stepView.getId(), actionView.getId(), mandateFieldView.getId());
                if(mandateFieldStepAction != null)
                    mandateFieldStepActionDAO.delete(mandateFieldStepAction);
            }
        }
        logger.debug("-- end deleteMandateFieldStepAction");
    }

    public void deleteMandateFieldConStepAction(List<MandateFieldConditionView> delectedMandateConViewList, StepView stepView, ActionView actionView){
        logger.debug("-- begin deleteMandateFieldConStepAction {}, {}, {}", delectedMandateConViewList, stepView, actionView);
        if(delectedMandateConViewList != null && delectedMandateConViewList.size() > 0){
            for(MandateFieldConditionView conditionView : delectedMandateConViewList){
                MandateFieldConStepAction mandateFieldConStepAction = mandateFieldConStepActionDAO.findByActionAndCon(stepView.getId(), actionView.getId(), conditionView.getId());
                if(mandateFieldConStepAction != null)
                    mandateFieldConStepActionDAO.delete(mandateFieldConStepAction);
            }
        }
        logger.debug("-- end deleteMandateFieldConStepAction");

    }

    public void deleteMandateFieldClassSAAdmin(MandateFieldClassSAAdminView mandateFieldClassSAAdminView, StepView stepView, ActionView actionView){
        mandateFieldClassSAAdminView.getMandateFieldViewList();
        deleteMandateFieldStepAction(mandateFieldClassSAAdminView.getMandateFieldViewList(), stepView, actionView);
        deleteMandateFieldConStepAction(mandateFieldClassSAAdminView.getMandateFieldConditionViewList(), stepView, actionView);
    }
}
