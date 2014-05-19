package com.clevel.selos.transform;

import com.clevel.selos.dao.master.MandateFieldClassDAO;
import com.clevel.selos.dao.master.MandateFieldConditionDAO;
import com.clevel.selos.dao.master.MandateFieldConditionDetailDAO;
import com.clevel.selos.dao.master.MandateFieldDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.RadioValue;
import com.clevel.selos.model.UserSysParameterKey;
import com.clevel.selos.model.db.master.*;
import com.clevel.selos.model.view.*;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MandateFieldTransform extends Transform{

    @Inject
    @SELOS
    Logger logger;

    @Inject
    private ActionTransform actionTransform;
    @Inject
    private StepTransform stepTransform;
    @Inject
    private MandateFieldDAO mandateFieldDAO;
    @Inject
    private MandateFieldClassDAO mandateFieldClassDAO;
    @Inject
    private MandateFieldConditionDAO mandateFieldConditionDAO;
    @Inject
    private MandateFieldConditionDetailDAO mandateFieldConditionDetailDAO;

    @Inject
    public MandateFieldTransform(){

    }

    public MandateFieldView transformToView(MandateField mandateField){
        logger.debug("-- begin transformToView mandateField{}", mandateField);
        if(mandateField == null)
            return null;

        MandateFieldView mandateFieldView = new MandateFieldView();
        mandateFieldView.setId(mandateField.getId());
        mandateFieldView.setMandateFieldClassView(transformToView(mandateField.getMandateFieldClass()));
        mandateFieldView.setFieldName(mandateField.getFieldName());
        mandateFieldView.setFieldDesc(mandateField.getFieldDescription());
        mandateFieldView.setPage(mandateField.getPage());
        mandateFieldView.setMinValue(mandateField.getMinValue());
        mandateFieldView.setMaxValue(mandateField.getMaxValue());
        mandateFieldView.setMatchedValue(mandateField.getMatchedValue());
        if(UserSysParameterKey.STATIC_EMPTY.key().equals(mandateField.getMatchedValue())){
            mandateFieldView.setMatchedEmpty(RadioValue.YES.value());
        } else {
            if(mandateField.getMatchedValue() == null || "".equals(mandateField.getMatchedValue())){
                mandateFieldView.setMatchedEmpty(RadioValue.NOT_SELECTED.value());
            } else {
                mandateFieldView.setMatchedEmpty(RadioValue.NO.value());
            }
        }
        mandateFieldView.setNotMatchedValue(mandateField.getNotMatchedValue());
        if(UserSysParameterKey.STATIC_EMPTY.key().equals(mandateField.getNotMatchedValue())){
            mandateFieldView.setNotMatchedEmpty(RadioValue.YES.value());
        } else {
            if(mandateField.getNotMatchedValue() == null || "".equals(mandateField.getNotMatchedValue()))
                mandateFieldView.setNotMatchedEmpty(RadioValue.NOT_SELECTED.value());
            else
                mandateFieldView.setNotMatchedEmpty(RadioValue.NO.value());
        }
        mandateFieldView.setMandateFieldType(mandateField.getMandateFieldType());
        logger.debug("-- end transformToView return mandateFieldView: {}", mandateFieldView);
        return mandateFieldView;
    }

    public MandateFieldClassView transformToView(MandateFieldClass mandateFieldClass){
        logger.debug("-- being transformToView(MandateFieldClass : {})", mandateFieldClass);
        if(mandateFieldClass == null)
            return null;

        MandateFieldClassView mandateFieldClassView = new MandateFieldClassView();
        mandateFieldClassView.setId(mandateFieldClass.getId());
        mandateFieldClassView.setClassName(mandateFieldClass.getClassName());
        mandateFieldClassView.setPageName(mandateFieldClass.getPageName());
        mandateFieldClassView.setClassDescription(mandateFieldClass.getClassDescription());

        logger.debug("-- end transformToView return MandateFieldClassView: {}", mandateFieldClassView);
        return mandateFieldClassView;
    }

    public MandateFieldConditionView transformToView(MandateFieldCondition mandateFieldCondition){
        logger.debug("-- begin transformToView(MandateFieldCondition : {})", mandateFieldCondition);

        if(mandateFieldCondition == null)
            return null;

        MandateFieldConditionView mandateFieldConditionView = new MandateFieldConditionView();
        mandateFieldConditionView.setId(mandateFieldCondition.getId());
        mandateFieldConditionView.setName(mandateFieldCondition.getName());
        mandateFieldConditionView.setMandateFieldClassView(transformToView(mandateFieldCondition.getMandateFieldClass()));
        mandateFieldConditionView.setConditionDesc(mandateFieldCondition.getConditionDesc());

        mandateFieldConditionView.setDependType(mandateFieldCondition.getDependType());
        mandateFieldConditionView.setMandateConditionType(mandateFieldCondition.getMandateConditionType());

        List<MandateFieldConditionDetailView> conditionDetailViewList = new ArrayList<MandateFieldConditionDetailView>();
        for(MandateFieldConditionDetail conditionDetail : mandateFieldCondition.getMandateFieldConditionDetailList()){
            if(conditionDetail != null)
                conditionDetailViewList.add(transformToView(conditionDetail));
        }

        mandateFieldConditionView.setConditionDetailViewList(conditionDetailViewList);

        logger.debug("-- end transformToView()");
        return mandateFieldConditionView;
    }

    public MandateFieldConditionDetailView transformToView(MandateFieldConditionDetail conditionDetail){
        logger.debug("-- begin transformToView(MandateFieldConditionDetail: {})", conditionDetail);

        if(conditionDetail == null)
            return null;
        MandateFieldConditionDetailView mandateFieldConditionDetailView = new MandateFieldConditionDetailView();
        mandateFieldConditionDetailView.setId(conditionDetail.getId());
        mandateFieldConditionDetailView.setMandateFieldView(transformToView(conditionDetail.getMandateField()));
        mandateFieldConditionDetailView.setBaseValue(conditionDetail.getBaseValue());

        logger.debug("-- end transformToView return ", mandateFieldConditionDetailView);
        return mandateFieldConditionDetailView;
    }

    public MandateFieldStepActionView transformToView(List<MandateFieldStepAction> fieldStepActionList, List<MandateFieldConStepAction> conStepActionList){

        List<MandateFieldClassSAAdminView> mandateFieldClassStepActionViewList = new ArrayList<MandateFieldClassSAAdminView>();
        Map<String, MandateFieldClassSAAdminView> fieldClassSAAdminViewMap = new HashMap<String, MandateFieldClassSAAdminView>();
        if(fieldStepActionList != null){
            for(MandateFieldStepAction mandateFieldStepAction : fieldStepActionList){
                if(mandateFieldStepAction.getMandateField() != null){
                    MandateField mandateField = mandateFieldStepAction.getMandateField();
                    MandateFieldClass mandateFieldClass = mandateField.getMandateFieldClass();

                    if(mandateFieldClass != null){
                        MandateFieldClassSAAdminView fieldClassSAAdminView = fieldClassSAAdminViewMap.get(mandateFieldClass.getClassName());
                        if(fieldClassSAAdminView == null){
                            fieldClassSAAdminView = new MandateFieldClassSAAdminView();
                            fieldClassSAAdminView.setId(mandateFieldClass.getId());
                            fieldClassSAAdminView.setPageName(mandateFieldClass.getPageName());
                            fieldClassSAAdminView.setClassDescription(mandateFieldClass.getClassDescription());
                            fieldClassSAAdminView.setClassName(mandateFieldClass.getClassName());
                            fieldClassSAAdminView.setActive(mandateFieldClass.isActive());
                        }
                        List<MandateFieldView> mandateFieldViewList = fieldClassSAAdminView.getMandateFieldViewList();
                        if(mandateFieldViewList == null)
                            mandateFieldViewList = new ArrayList<MandateFieldView>();

                        mandateFieldViewList.add(transformToView(mandateFieldStepAction.getMandateField()));
                        fieldClassSAAdminView.setMandateFieldViewList(mandateFieldViewList);

                        fieldClassSAAdminViewMap.put(fieldClassSAAdminView.getClassName(), fieldClassSAAdminView);
                    }
                }
            }

            mandateFieldClassStepActionViewList.addAll(fieldClassSAAdminViewMap.values());
        }

        if(conStepActionList != null){
            for(MandateFieldConStepAction conStepAction : conStepActionList){
                if(conStepAction.getMandateFieldCondition() != null){
                    MandateFieldCondition mandateFieldCondition = conStepAction.getMandateFieldCondition();
                    MandateFieldClass mandateFieldClass = mandateFieldCondition.getMandateFieldClass();

                    if(mandateFieldClass != null){
                        MandateFieldClassSAAdminView fieldClassSAAdminView = fieldClassSAAdminViewMap.get(mandateFieldClass.getClassName());
                        if(fieldClassSAAdminView == null){
                            fieldClassSAAdminView = new MandateFieldClassSAAdminView();
                            fieldClassSAAdminView.setId(mandateFieldClass.getId());
                            fieldClassSAAdminView.setPageName(mandateFieldClass.getPageName());
                            fieldClassSAAdminView.setClassDescription(mandateFieldClass.getClassDescription());
                            fieldClassSAAdminView.setClassName(mandateFieldClass.getClassName());
                            fieldClassSAAdminView.setActive(mandateFieldClass.isActive());
                        }
                        List<MandateFieldConditionView> mandateFieldConditionViewList = fieldClassSAAdminView.getMandateFieldConditionViewList();
                        if(mandateFieldConditionViewList == null)
                            mandateFieldConditionViewList = new ArrayList<MandateFieldConditionView>();

                        mandateFieldConditionViewList.add(transformToView(mandateFieldCondition));
                        fieldClassSAAdminView.setMandateFieldConditionViewList(mandateFieldConditionViewList);

                        fieldClassSAAdminViewMap.put(fieldClassSAAdminView.getClassName(), fieldClassSAAdminView);
                    }
                }
            }

        }

        MandateFieldStepActionView mandateFieldStepActionView = null;
        if(fieldStepActionList != null && fieldStepActionList.size() > 0){
            MandateFieldStepAction mandateFieldStepAction = fieldStepActionList.get(0);
            mandateFieldStepActionView = new MandateFieldStepActionView();
            mandateFieldStepActionView.setStepView(stepTransform.transformToView(mandateFieldStepAction.getStep()));
            mandateFieldStepActionView.setActionView(actionTransform.transformToView(mandateFieldStepAction.getAction()));
            mandateFieldStepActionView.setClassSAAdminViewList(mandateFieldClassStepActionViewList);
        }
        return mandateFieldStepActionView;
    }

    public MandateFieldClass transformToModel(MandateFieldClassView mandateFieldClassView){
        if(mandateFieldClassView == null)
            return null;

        MandateFieldClass mandateFieldClass = new MandateFieldClass();
        if(mandateFieldClassView.getId() > 0)
            mandateFieldClass = mandateFieldClassDAO.findById(mandateFieldClassView.getId());
        mandateFieldClass.setClassName(mandateFieldClassView.getClassName());
        mandateFieldClass.setClassDescription(mandateFieldClassView.getClassDescription());
        mandateFieldClass.setPageName(mandateFieldClassView.getPageName());
        mandateFieldClass.setActive(mandateFieldClassView.isActive());
        return mandateFieldClass;
    }

    public MandateField transformToModel(MandateFieldView mandateFieldView, MandateFieldClass mandateFieldClass){
        if(mandateFieldView == null)
            return null;

        MandateField mandateField = new MandateField();
        if(mandateFieldView.getId() > 0)
            mandateField = mandateFieldDAO.findById(mandateFieldView.getId());

        mandateField.setMandateFieldType(mandateFieldView.getMandateFieldType());
        mandateField.setPage(mandateFieldView.getPage());
        mandateField.setFieldDescription(mandateFieldView.getFieldDesc());
        mandateField.setFieldName(mandateFieldView.getFieldName());
        mandateField.setMandateFieldClass(mandateFieldClass);
        mandateField.setMinValue(mandateFieldView.getMinValue());
        mandateField.setMaxValue(mandateFieldView.getMaxValue());

        if(mandateFieldView.getMatchedEmpty() == RadioValue.YES.value()){
            mandateField.setMatchedValue(UserSysParameterKey.STATIC_EMPTY.key());
        } else if(mandateFieldView.getMatchedEmpty() == RadioValue.NOT_SELECTED.value()){
            mandateField.setMatchedValue(null);
        } else {
            mandateField.setMatchedValue(mandateFieldView.getMatchedValue());
        }

        if(mandateFieldView.getNotMatchedEmpty() == RadioValue.YES.value()){
            mandateField.setNotMatchedValue(UserSysParameterKey.STATIC_EMPTY.key());
        } else if(mandateFieldView.getNotMatchedEmpty() == RadioValue.NOT_SELECTED.value()){
            mandateField.setNotMatchedValue(null);
        } else {
            mandateField.setNotMatchedValue(mandateFieldView.getNotMatchedValue());
        }

        return mandateField;

    }

    public MandateField transformToModel(MandateFieldView mandateFieldView){
        MandateField mandateField = new MandateField();
        if(mandateFieldView.getId() > 0)
            mandateField = mandateFieldDAO.findById(mandateFieldView.getId());
        return mandateField;
    }

    public MandateFieldCondition transformToModel(MandateFieldConditionView mandateFieldConditionView){
        if(mandateFieldConditionView == null)
            return null;

        MandateFieldCondition mandateFieldCondition = new MandateFieldCondition();

        if(mandateFieldConditionView.getId() > 0){
            mandateFieldCondition = mandateFieldConditionDAO.findById(mandateFieldConditionView.getId());
        }

        mandateFieldCondition.setName(mandateFieldConditionView.getName());
        mandateFieldCondition.setMandateConditionType(mandateFieldConditionView.getMandateConditionType());
        mandateFieldCondition.setConditionDesc(mandateFieldConditionView.getConditionDesc());
        mandateFieldCondition.setDependType(mandateFieldConditionView.getDependType());
        return mandateFieldCondition;
    }

    public MandateFieldConditionDetail transformToModel(MandateFieldConditionDetailView mandateFieldConditionDetailView){
        if(mandateFieldConditionDetailView == null)
            return null;

        MandateFieldConditionDetail mandateFieldConditionDetail = new MandateFieldConditionDetail();
        if(mandateFieldConditionDetailView.getId() > 0)
            mandateFieldConditionDetail = mandateFieldConditionDetailDAO.findById(mandateFieldConditionDetailView.getId());

        return mandateFieldConditionDetail;
    }
}
