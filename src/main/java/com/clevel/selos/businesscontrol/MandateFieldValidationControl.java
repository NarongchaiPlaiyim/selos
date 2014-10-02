package com.clevel.selos.businesscontrol;

import com.clevel.selos.businesscontrol.master.MandateFieldControl;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.*;
import com.clevel.selos.model.view.MandateFieldMessageView;
import com.clevel.selos.model.view.MandateFieldValidationResult;
import com.clevel.selos.model.view.master.*;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.system.message.NormalMessage;
import com.clevel.selos.system.message.ValidationMessage;
import com.clevel.selos.util.Util;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RequestScoped
public class MandateFieldValidationControl extends BusinessControl{

    @Inject
    @SELOS
    private Logger logger;

    @Inject
    @ValidationMessage
    private Message validationMsg;

    @Inject
    @NormalMessage
    private Message normalMsg;

    private final static String ACTION_DATA_REQUIRED = "1151";
    private final static String ACTION_DATA_INCORRECT = "1152";
    private final static String ACTION_DATA_MIN_INCORRECT = "1153";
    private final static String ACTION_DATA_MAX_INCORRECT = "1154";
    private final static String ACTION_DATA_MIN_MAX_INCORRECT = "1155";
    private final static String ACTION_DATA_MATCHED_INCORRECT = "1156";
    private final static String ACTION_DATA_NOT_MATCHED_INCORRECT = "1157";
    private final static String CONDITION_BASE = "1162";
    private final static String ACTION_VERIFY_FIELD_COND = "1163";

    private Map<String, List<ClassResult>> mandateClassResultMap = null;
    private MandateFieldValidationResult mandateFieldValidationResult = null;

    //Store List of mandateFieldView which configure for this step;
    private Map<String, List<MandateFieldView>> mandateFieldViewMap = null;
    private Map<String, MandateFieldClassStepActionView> mandateFieldClassStepActionViewMap = null;
    private Map<Long, MandateFieldConditionView> mandateFieldConditionMap = null;

    @Inject
    private MandateFieldControl mandateFieldControl;

    @Inject
    public MandateFieldValidationControl(){}

    public void loadMandateField(long stepId, long actionId){
        logger.debug("-- begin loadMandateField stepId:{}, actionId:{}", stepId, actionId);
        mandateFieldViewMap = mandateFieldControl.getMandateFieldByStepAction(stepId, actionId);
        mandateFieldConditionMap = mandateFieldControl.getMandateConditionByStepAction(stepId, actionId);
        mandateFieldClassStepActionViewMap = mandateFieldControl.getMandateFieldClassStepAction(stepId, actionId);
        logger.debug("Class", mandateFieldClassStepActionViewMap);


        mandateClassResultMap = new ConcurrentHashMap<String, List<ClassResult>>();
        logger.debug("-- end loadMandateField stepId:{}, actionId:{}", stepId, actionId);
    }

    public void validate(Object object, String className){
        validate(object, className, null);
    }

    public void validate(Object object, String className, ClassResult refClassResult){
        logger.debug("-- begin validate: className{}, refClassResult: {}", className, refClassResult);
        if(mandateFieldClassStepActionViewMap.get(className) == null){
            logger.debug("The {} is not mandatory in this step", className);
            return;
        }

        MandateFieldClassStepActionView mandateFieldClassStepActionView = mandateFieldClassStepActionViewMap.get(className);
        if(object == null){
            ClassResult classResult = new ClassResult(mandateFieldClassStepActionView.getMandateFieldClassView());
            if(mandateFieldClassStepActionView.isRequired()){
                logger.debug("Class {} is required", mandateFieldClassStepActionView.getMandateFieldClassView().getClassName());
                classResult.isPassRequired = false;

            } else {
                classResult.isPassRequired = true;
                logger.debug("Class {} is NOT required", mandateFieldClassStepActionView.getMandateFieldClassView().getClassName());
            }

            logger.debug("Object NULL, Class Result: {} is required", classResult);
            addClassResult(classResult);
            return;
        }

        String type = object.getClass().getName();
        logger.debug("validate type: {}", type);
        if(type.equals(ArrayList.class.getName())) {
            logger.debug("object is Array, -- begin Validate list of class");
            ArrayList objectList = (ArrayList)object;

            if(mandateFieldClassStepActionView.isRequired()){
                if(objectList.size() == 0){
                    ClassResult classResult = new ClassResult(mandateFieldClassStepActionView.getMandateFieldClassView());
                    classResult.isPassRequired = false;
                    logger.debug("Object size is 0, Class Result: {}", classResult);
                    addClassResult(classResult);
                    return;
                }
            }

            for(int i=0; i<objectList.size(); i++){
                Object eachClassObj = objectList.get(i);
                ClassResult classResult = validateClass(eachClassObj, refClassResult);
                classResult.isPassRequired = true;
                logger.debug("Class Result: {}", classResult);
                addClassResult(classResult);
            }
        } else {
            ClassResult classResult = validateClass(object, refClassResult);
            classResult.isPassRequired = true;
            logger.debug("Class Result: {}", classResult);
            addClassResult(classResult);
        }
    }

    private ClassResult validateClass(Object classObj, ClassResult refClassResult){
        logger.debug("-- begin validateClass {}", classObj);
        Class clazz = classObj.getClass();
        logger.debug("-- className: {}" + clazz.getName());
        List<MandateFieldView> mandateFieldValidationList = mandateFieldViewMap.get(clazz.getName());

        //Prepare Class Result;
        MandateFieldClassStepActionView mandateFieldClassStepActionView = mandateFieldClassStepActionViewMap.get(clazz.getName());
        ClassResult classResult = new ClassResult(mandateFieldClassStepActionView.getMandateFieldClassView());
        classResult.id = getClassIdentity(classObj);

        List<MandateFieldView> _toCheckFieldDetailList = new ArrayList<MandateFieldView>();

        if(mandateFieldValidationList != null){
            for(MandateFieldView mandateFieldView : mandateFieldValidationList){
                try{
                    Field field = clazz.getDeclaredField(mandateFieldView.getFieldName());
                    field.setAccessible(true);

                    Class fieldType = field.getType();
                    logger.debug("Field Type: {}", fieldType.getName());

                    Object fieldObj = field.get(classObj);
                    FieldResult fieldResult = null;
                    if(fieldType.getName().equals("int") || fieldType.getName().equals(Integer.class.getName()) ||
                            fieldType.getName().equals("long") || fieldType.getName().equals(Long.class.getName())){
                        fieldResult = validateFieldValue((Number) fieldObj, mandateFieldView);
                    } else if(fieldType.getName().equals(String.class.getName())){
                        fieldResult = validateFieldValue((String) fieldObj, mandateFieldView);
                    } else if(fieldType.getName().equals(BigDecimal.class.getName())){
                        fieldResult = validateFieldValue((BigDecimal) fieldObj, mandateFieldView);
                    } else if(fieldType.getName().equals("boolean") || fieldType.getName().equals(Boolean.class.getName())){
                        fieldResult = validateFieldValue((Boolean) fieldObj, mandateFieldView);
                    } else if(fieldType.getName().equals(Date.class.getName())){
                        fieldResult = validateFieldValue((Date)fieldObj, mandateFieldView);
                    } else if(fieldType.getName().contains(".db.master.")){
                        fieldResult = validateFieldValue(fieldObj, mandateFieldView);
                    } else if(fieldType.getName().contains(".db.working.")){
                        fieldResult = validateFieldValue(fieldObj, mandateFieldView);
                        if(mandateFieldView.isCheckFieldDetail()){
                            //_toCheckFieldDetailList.add(mandateFieldView);
                            validate(fieldObj, fieldType.getName(), classResult);
                        }
                    }
                    classResult.addFieldResult(fieldResult);
                    logger.debug("Field Result: {}", fieldResult);

                } catch(Exception ex){
                    ex.printStackTrace();
                }
            }
        }

        //Validate Condition Result for each class before combine Condition
        for(MandateFieldConditionView mandateFieldConditionView : mandateFieldConditionMap.values()){
            MandateFieldClassView mandateFieldClassView = mandateFieldConditionView.getMandateFieldClassView();
            if(mandateFieldClassView != null && mandateFieldClassView.getClassName().equals(clazz.getName())){
                ConditionResult conditionResult = validateCondition(classObj, mandateFieldConditionView, classResult);
                if(refClassResult != null){
                    conditionResult.refClassName = refClassResult.mandateFieldClassView.getClassName();
                    conditionResult.refClassId = refClassResult.id;
                }
                classResult.addConditionResult(conditionResult);
            }
        }

        logger.debug("-- end validateClass return {}", classResult);

        return classResult;
    }

    public MandateFieldValidationResult getMandateFieldValidationResult(){
        logger.debug("-- begin getMandateFieldValidationResult --");
        //if(mandateFieldValidationResult == null){
            logger.debug("-- mandateFieldValidationResult is NULL, start to combine a message");
            mandateFieldValidationResult = new MandateFieldValidationResult();
            List<MandateFieldMessageView> _tmpMessageViewList = new ArrayList<MandateFieldMessageView>();
            for(List<ClassResult> classResultList : mandateClassResultMap.values()){
                for(ClassResult classResult : classResultList) {
                    _tmpMessageViewList.addAll(combineMandateResult(classResult));
                }
            }
            if(_tmpMessageViewList.size() == 0){
                mandateFieldValidationResult.setActionResult(ActionResult.SUCCESS);
                mandateFieldValidationResult.setMandateFieldMessageViewList(new ArrayList<MandateFieldMessageView>());
            } else {
                mandateFieldValidationResult.setActionResult(ActionResult.FAILED);
                mandateFieldValidationResult.setMandateFieldMessageViewList(_tmpMessageViewList);
            }
        //}
        logger.debug("-- end getMandateFieldValidationResult return: {}", mandateFieldValidationResult);

        return mandateFieldValidationResult;
    }

    private List<MandateFieldMessageView> combineMandateResult(ClassResult classResult){
        logger.debug("-- begin combineMandateResult, ClassResult: {}", classResult);

        List<MandateFieldMessageView> mandateFieldMessageViewList = new ArrayList<MandateFieldMessageView>();
        if(!classResult.isPassRequired){
            mandateFieldMessageViewList.add(classResult.getMessage());
            logger.debug("combineMandateResult, Mandate Class {} ", classResult.getMessage());
            return mandateFieldMessageViewList;
        }

        for (FieldResult fieldResult : classResult.fieldResultMap.values()){
            if(fieldResult.isDepended != null && !fieldResult.isDepended){
                if(!fieldResult.getFinalResult()){
                    mandateFieldMessageViewList.add(fieldResult.getMessage());
                    logger.debug("combineMandateResult, Field Result: {}", fieldResult.getMessage());
                }
            }

        }

        for(ConditionResult conditionResult : classResult.conditionResultMap.values()){
            combineConditionResult(conditionResult);
            logger.debug("combineMandateResult, conditionResult: {}", conditionResult);
        }

        for(ConditionResult conditionResult : classResult.conditionResultMap.values()){
            if(conditionResult.conditionView.getMandateConditionType().equals(MandateConditionType.CHECK_RESULT)){
                if(conditionResult.isPassCombine != null){
                    if(conditionResult.isDepended != null){
                        if(!conditionResult.isDepended && !conditionResult.isPassCombine){
                            mandateFieldMessageViewList.addAll(conditionResult.getMessage());
                            logger.debug("combine message: {}", conditionResult.getMessage());
                        }
                    } else {
                        if(!conditionResult.isPassCombine){
                            mandateFieldMessageViewList.addAll(conditionResult.getMessage());
                            logger.debug("combine message: {}", conditionResult.getMessage());
                        }
                    }
                }
            }
        }
        logger.debug("-- end combineMandateResult return: message size {}", mandateFieldMessageViewList.size());
        return mandateFieldMessageViewList;
    }

    private Boolean combineConditionResult(ConditionResult conditionResult){
        logger.debug("-- begin combineConditionResult :: {}", conditionResult);

        MandateFieldConditionView conditionView = conditionResult.conditionView;

        if(conditionView.getDependType().equals(MandateDependType.NO_DEPENDENCY)){
            return conditionResult.isPass;
        } else {
            Boolean combineResult = null;
            if(MandateDependConType.EXTERNAL.equals(conditionView.getDependConType())){
                logger.debug("Depend Class is external");
                ClassResult refClassResult = null;
                List<ClassResult> classResultList = mandateClassResultMap.get(conditionResult.refClassName);
                for(ClassResult classResult : classResultList){
                    if(classResult.id == conditionResult.refClassId){
                        refClassResult = classResult;
                    }
                }

                ConditionResult dependConditionResult = refClassResult.conditionResultMap.get(conditionView.getDependCondition().getId());
                combineResult = combineConditionResult(dependConditionResult);
                dependConditionResult.isPassCombine = combineResult;
                dependConditionResult.isDepended = true;
                logger.debug("-- DependConditionResult: {}", dependConditionResult);
                refClassResult.conditionResultMap.put(dependConditionResult.conditionView.getId(), dependConditionResult);
            }
            else {
                logger.debug("Depend Class is internal");
                ConditionResult dependConditionResult = conditionResult.parentClassResult.conditionResultMap.get(conditionView.getDependCondition().getId());
                combineResult = combineConditionResult(dependConditionResult);
                dependConditionResult.isPassCombine = combineResult;
                dependConditionResult.isDepended = true;
                logger.debug("-- DependConditionResult: {}", dependConditionResult);
                conditionResult.parentClassResult.conditionResultMap.put(dependConditionResult.conditionView.getId(), dependConditionResult);
            }
            if(combineResult == null)
                return null;

            if(conditionView.getDependType().equals(MandateDependType.DEPEND_TRUE) && combineResult){
                logger.debug("-- MandateDependType.DEPEND_TRUE: return {}", conditionResult.isPass);
                return conditionResult.isPass;
            } else if(conditionView.getDependType().equals(MandateDependType.DEPEND_FALSE) && !combineResult){
                logger.debug("-- MandateDependType.DEPEND_FALSE: return {}", conditionResult.isPass);
                return conditionResult.isPass;
            } else{
                return null;
            }
        }
    }

    private ConditionResult validateCondition(Object classObj, MandateFieldConditionView mandateFieldConditionView, ClassResult classResult){
        logger.debug("-- begin validateCondition object: {}", classObj);
        logger.debug("condition: " + mandateFieldConditionView);

        Class clazz = classObj.getClass();
        ConditionResult conditionResult = new ConditionResult(mandateFieldConditionView);

        for(MandateFieldConditionDetailView conditionDetailView : mandateFieldConditionView.getConditionDetailViewList()){
            MandateFieldView _fieldView = conditionDetailView.getMandateFieldView();
            classResult.fieldResultMap.get(_fieldView.getId());
            FieldResult fieldResult = new FieldResult(conditionDetailView.getMandateFieldView());
            if(conditionDetailView.getBaseValue() != null && !"".equals(conditionDetailView.getBaseValue())){
                logger.debug("validate BASE condition");
                try{
                    Field field = clazz.getDeclaredField(_fieldView.getFieldName());
                    field.setAccessible(true);
                    Object fieldObj = field.get(classObj);

                    if(fieldObj == null)
                        fieldResult.isPassMatched = false;

                    Class fieldType = field.getType();
                    if(fieldType.getName().equals("int") || fieldType.getName().equals(Integer.class.getName()) ||
                            fieldType.getName().equals("long") || fieldType.getName().equals(Long.class.getName())){
                        fieldResult.isPassMatched = (((Number)fieldObj).longValue() == parseLong(conditionDetailView.getBaseValue()).longValue());
                    } else if(fieldType.getName().equals(String.class.getName())){
                        fieldResult.isPassMatched = fieldObj.equals(conditionDetailView.getBaseValue());
                    } else if(fieldType.getName().equals(BigDecimal.class.getName())){
                        fieldResult.isPassMatched = ((BigDecimal)fieldObj).equals(new BigDecimal(conditionDetailView.getBaseValue()));
                    } else if(fieldType.getName().equals("boolean") || fieldType.getName().equals(Boolean.class.getName())){
                        fieldResult.isPassMatched = fieldObj.equals(Boolean.parseBoolean(conditionDetailView.getBaseValue()));
                    } else if(fieldType.getName().contains(".db.master.")){
                        fieldResult.isPassMatched = checkMasterValue(fieldObj, conditionDetailView.getBaseValue());
                    }
                } catch (NoSuchFieldException nfex){
                    nfex.printStackTrace();
                } catch (IllegalAccessException ilex){
                    ilex.printStackTrace();
                }
                logger.debug("check BaseValue, Field Result: {}", fieldResult.getFinalResult());
            } else {
                fieldResult = classResult.fieldResultMap.get(_fieldView.getId());
                logger.debug("Use Original Result from field: {}", fieldResult.getFinalResult());
                fieldResult.isDepended = Boolean.TRUE;
            }

            conditionResult.addFieldResult(fieldResult);
            if(conditionResult.isPass == null){
                conditionResult.isPass = fieldResult.getFinalResult();
            } else {
                if(mandateFieldConditionView.getMandateConDetailType().equals(MandateConDetailType.AND)){
                    conditionResult.isPass = conditionResult.isPass && fieldResult.getFinalResult();
                } else {
                    conditionResult.isPass = conditionResult.isPass || fieldResult.getFinalResult();
                }
            }
        }
        logger.debug("-- end validateCondition, return: {}", conditionResult);
        return conditionResult;
    }

    private Long getClassIdentity(Object classObj){
        logger.debug("-- begin getClassIdentity --");
        Class clazz = classObj.getClass();
        Field fieldId = null;
        try{
            fieldId = clazz.getDeclaredField("id");
        } catch (Exception ex){
            try {
                fieldId = clazz.getDeclaredField("code");
            }catch (Exception inex){
                inex.printStackTrace();
                return -1L;
            }
        }
        fieldId.setAccessible(true);
        try{
            Number fieldObj = (Number)fieldId.get(classObj);
            logger.debug("-- end getClassIdentity, return: {}", fieldObj.longValue());
            return fieldObj.longValue();
        }catch (IllegalAccessException iaex){
            iaex.printStackTrace();
            logger.debug("-- end getClassIdentity, return: {}", -1L);
            return -1L;
        }
    }

    private FieldResult validateFieldValue(Number numberObj, MandateFieldView mandateFieldView){
        logger.debug("-- begin validateFieldValue, Number: {}, MandateFieldView: {}", numberObj, mandateFieldView);

        FieldResult fieldResult = new FieldResult(mandateFieldView);
        if(!Util.isEmpty(mandateFieldView.getMinValue())){
            if(numberObj != null)
                fieldResult.isPassMin = (numberObj.longValue() >= parseLong(mandateFieldView.getMinValue()));
        }
        if(!Util.isEmpty(mandateFieldView.getMaxValue())){
            if(numberObj != null)
                fieldResult.isPassMax = (numberObj.longValue() <= parseLong(mandateFieldView.getMaxValue()));
        }
        if(!Util.isEmpty(mandateFieldView.getMatchedValue())){
            if(mandateFieldView.getMatchedValue().equals(UserSysParameterKey.STATIC_EMPTY.key())){
                fieldResult.isPassMatched = (numberObj == null);
            } else {
                if(numberObj == null)
                    fieldResult.isPassMatched = false;
                else
                    fieldResult.isPassMatched = (numberObj.longValue() == parseLong(mandateFieldView.getMatchedValue()));
            }
        }
        if(!Util.isEmpty(mandateFieldView.getNotMatchedValue())){
            if(mandateFieldView.getNotMatchedValue().equals(UserSysParameterKey.STATIC_EMPTY.key())){
                fieldResult.isPassNotMatched = !(numberObj == null);
            } else {
                if(numberObj == null)
                    fieldResult.isPassNotMatched = true;
                else
                    fieldResult.isPassNotMatched = !(numberObj.longValue() == parseLong(mandateFieldView.getNotMatchedValue()));
            }
        }
        logger.debug("-- end ValidateFieldResult: return {}", fieldResult);
        return fieldResult;
    }

    private FieldResult validateFieldValue(String strObj, MandateFieldView mandateFieldView) {
        logger.debug("-- begin validateFieldValue, String: {}, MandateFieldView: {}", strObj, mandateFieldView);

        FieldResult fieldResult = new FieldResult(mandateFieldView);
        if(!Util.isEmpty(mandateFieldView.getMinValue())){
            fieldResult.isPassMin = (strObj.compareTo(mandateFieldView.getMinValue()) >= 0);
        }
        if(!Util.isEmpty(mandateFieldView.getMaxValue())){
            fieldResult.isPassMax = (strObj.compareTo(mandateFieldView.getMinValue()) <= 0);
        }
        if(!Util.isEmpty(mandateFieldView.getMatchedValue())){
            if(mandateFieldView.getMatchedValue().equals(UserSysParameterKey.STATIC_EMPTY.key())){
                fieldResult.isPassMatched = (strObj == null || "".equals(strObj));
            } else {
                fieldResult.isPassMatched = mandateFieldView.getMatchedValue().equals(strObj);
            }
        }
        if(!Util.isEmpty(mandateFieldView.getNotMatchedValue())){
            if(mandateFieldView.getNotMatchedValue().equals(UserSysParameterKey.STATIC_EMPTY.key())){
                fieldResult.isPassNotMatched = !(strObj == null || "".equals(strObj));
            } else {
                fieldResult.isPassNotMatched = !(mandateFieldView.getNotMatchedValue().equals(strObj));
            }
        }
        logger.debug("-- end ValidateFieldResult: return {}", fieldResult);
        return fieldResult;
    }

    private FieldResult validateFieldValue(BigDecimal bigDecimalObj, MandateFieldView mandateFieldView) {
        logger.debug("-- begin validateFieldValue, BigDecimal: {}, MandateFieldView: {}", bigDecimalObj, mandateFieldView);

        FieldResult fieldResult = new FieldResult(mandateFieldView);
        if(!Util.isEmpty(mandateFieldView.getMinValue())){
            BigDecimal _compareDecimal = new BigDecimal(mandateFieldView.getMinValue());
            if(bigDecimalObj != null)
                fieldResult.isPassMin = (_compareDecimal.compareTo(bigDecimalObj) <= 0);
        }
        if(!Util.isEmpty(mandateFieldView.getMaxValue())){
            BigDecimal _compareDecimal = new BigDecimal(mandateFieldView.getMaxValue());
            if(bigDecimalObj != null)
                fieldResult.isPassMax = (_compareDecimal.compareTo(bigDecimalObj) >= 0);
        }
        if(!Util.isEmpty(mandateFieldView.getMatchedValue())){
            if(mandateFieldView.getMatchedValue().equals(UserSysParameterKey.STATIC_EMPTY.key())){
                fieldResult.isPassMatched = (bigDecimalObj == null);
            } else {
                BigDecimal _compareDecimal = new BigDecimal(mandateFieldView.getMatchedValue());
                if(bigDecimalObj == null)
                    fieldResult.isPassMatched = false;
                else
                    fieldResult.isPassMatched = (_compareDecimal.compareTo(bigDecimalObj) == 0);
            }
        }
        if(!Util.isEmpty(mandateFieldView.getNotMatchedValue())){
            if(mandateFieldView.getNotMatchedValue().equals(UserSysParameterKey.STATIC_EMPTY.key())){
                fieldResult.isPassNotMatched = !(bigDecimalObj == null);
            } else {
                BigDecimal bigDecimal = new BigDecimal(mandateFieldView.getNotMatchedValue());
                if(bigDecimalObj == null)
                    fieldResult.isPassNotMatched = true;
                else
                    fieldResult.isPassNotMatched = !(bigDecimal.compareTo(bigDecimalObj) == 0);
            }
        }
        logger.debug("-- end ValidateFieldResult: return {}", fieldResult);
        return fieldResult;
    }

    private FieldResult validateFieldValue(Boolean booleanObj, MandateFieldView mandateFieldView){
        logger.debug("-- begin validateFieldValue, Boolean: {}, MandateFieldView: {}", booleanObj, mandateFieldView);

        FieldResult fieldResult = new FieldResult(mandateFieldView);
        if(!Util.isEmpty(mandateFieldView.getMatchedValue())){
            if(mandateFieldView.getMatchedValue().equals(UserSysParameterKey.STATIC_EMPTY.key())){
                fieldResult.isPassMatched = (booleanObj == null);
            } else {
                fieldResult.isPassMatched = (booleanObj == Boolean.parseBoolean(mandateFieldView.getMatchedValue()));
            }
        }
        if(!Util.isEmpty(mandateFieldView.getNotMatchedValue())){
            if(mandateFieldView.getNotMatchedValue().equals(UserSysParameterKey.STATIC_EMPTY.key())){
                fieldResult.isPassNotMatched = (booleanObj != null);
            } else {
                fieldResult.isPassNotMatched = (booleanObj != Boolean.parseBoolean(mandateFieldView.getNotMatchedValue()));
            }
        }
        logger.debug("-- end ValidateFieldResult: return {}", fieldResult);
        return fieldResult;
    }
    private FieldResult validateFieldValue(Date dateObj, MandateFieldView mandateFieldView){
        logger.debug("-- begin validateFieldValue, Boolean: {}, MandateFieldView: {}", dateObj, mandateFieldView);

        SimpleDateFormat simpleDate = new SimpleDateFormat("dd/MM/yyyy");
        String dateString = "";
        if(dateObj != null){
            dateString = simpleDate.format(dateObj);
        }

        FieldResult fieldResult = new FieldResult(mandateFieldView);
        if(!Util.isEmpty(mandateFieldView.getMinValue())){
            if(dateObj != null)
                fieldResult.isPassMin = mandateFieldView.getMinValue().compareTo(dateString) <= 0;
        }
        if(!Util.isEmpty(mandateFieldView.getMaxValue())){
            if(dateObj != null)
                fieldResult.isPassMax = mandateFieldView.getMaxValue().compareTo(dateString) >= 0;
        }
        if(!Util.isEmpty(mandateFieldView.getMatchedValue())){
            if(mandateFieldView.getMatchedValue().equals(UserSysParameterKey.STATIC_EMPTY.key())){
                fieldResult.isPassMatched = (dateObj == null);
            } else {
                fieldResult.isPassMatched = (mandateFieldView.getMatchedValue().equals(dateString));
            }
        }
        if(!Util.isEmpty(mandateFieldView.getNotMatchedValue())){
            if(mandateFieldView.getNotMatchedValue().equals(UserSysParameterKey.STATIC_EMPTY.key())){
                fieldResult.isPassNotMatched = (dateObj != null);
            } else {
                fieldResult.isPassNotMatched = !(mandateFieldView.getNotMatchedValue().equals(dateString));
            }
        }
        logger.debug("-- end ValidateFieldResult: return {}", fieldResult);
        return fieldResult;
    }

    /**
     * To validate masterObj, with the configuration in MandateFieldView
     * @param masterObj
     * @param mandateFieldView
     * @return
     */
    private FieldResult validateFieldValue(Object masterObj, MandateFieldView mandateFieldView){
        logger.debug("-- begin validateFieldValue, Object: {}, MandateFieldView: {}", masterObj, mandateFieldView);

        FieldResult fieldResult = new FieldResult(mandateFieldView);
        if(!Util.isEmpty(mandateFieldView.getMatchedValue())){
            if(mandateFieldView.getMatchedValue().equals(UserSysParameterKey.STATIC_EMPTY.key())){
                fieldResult.isPassMatched = (masterObj == null);
            } else {
                if(masterObj == null)
                    fieldResult.isPassMatched = false;
                else {
                    fieldResult.isPassMatched = checkMasterValue(masterObj, mandateFieldView.getMatchedValue());
                }
            }
        }
        if(!Util.isEmpty(mandateFieldView.getNotMatchedValue())){
            if(mandateFieldView.getNotMatchedValue().equals(UserSysParameterKey.STATIC_EMPTY.key())){
                fieldResult.isPassNotMatched = (masterObj != null);
            } else {
                fieldResult.isPassNotMatched = !checkMasterValue(masterObj, mandateFieldView.getNotMatchedValue());
            }
        }
        return fieldResult;
    }

    /**
     * To check value of field in Master Obj, for example validate id:1 (id = 1 or not) of Reference Object.
     * @param masterObj
     * @param value
     * @return
     */
    private boolean checkMasterValue(Object masterObj, String value){
        logger.debug("-- begin checkMasterValue, Object: {}, String: {}", masterObj, value);
        boolean valueIsNull = (value == null || "".equals(value));
        boolean masterObjIsNull = masterObj == null;

        if(valueIsNull && masterObjIsNull)
            return true;
        else if(valueIsNull || masterObjIsNull)
            return false;

        String fieldName = "id";
        String fieldValue = value;
        if(value != null){
            if (value.contains(":")){
                fieldName = value.substring(0, value.indexOf(":"));
                fieldValue = value.substring(value.indexOf(":") + 1);
            }
        }
        logger.debug("verify master: {}, value: {}", fieldName, fieldValue);

        try{
            Field field = masterObj.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);

            Object object = field.get(masterObj);
            Class fieldType = field.getType();
            Boolean result = Boolean.FALSE;
            if(fieldType.getName().equals("int") || fieldType.getName().equals(Integer.class.getName()) ||
                    fieldType.getName().equals("long") || fieldType.getName().equals(Long.class.getName())){
                result = parseLong(fieldValue).equals(((Number)object).longValue());
            } else if(fieldType.getName().equals(String.class.getName())){
                result = value.equals((String)object);
            } else {
                result = object.hashCode() == value.hashCode();
            }
            logger.debug("-- end checkMasterValue, return: {}", result);
            return result;
        }catch (Exception ex){
            logger.error("Error when parse master: {}", ex);
            return false;
        }

    }

    private Long parseLong(String value){
        try{
            return Long.parseLong(value);
        } catch (Exception ex){
            ex.printStackTrace();
            return -1L;
        }
    }

    private void addClassResult(ClassResult classResult){
        String className = classResult.mandateFieldClassView.getClassName();

        List<ClassResult> mandateClassList = mandateClassResultMap.get(className);
        if(mandateClassList == null)
            mandateClassList = new ArrayList<ClassResult>();
        mandateClassList.add(classResult);
        mandateClassResultMap.put(className, mandateClassList);
    }

    //To Store Class Result
    private class ClassResult {
        Long id = -1L;
        MandateFieldClassView mandateFieldClassView;
        Map<Long, FieldResult> fieldResultMap;
        Map<Long, ConditionResult> conditionResultMap;
        Boolean isPassRequired = null;

        public ClassResult(MandateFieldClassView in) {
            mandateFieldClassView = in;
            fieldResultMap = new ConcurrentHashMap<Long, FieldResult>();
            conditionResultMap = new ConcurrentHashMap<Long, ConditionResult>();
        }

        public void addFieldResult(FieldResult fieldResult){
            fieldResult.parentClassResult = this;
            fieldResultMap.put(fieldResult.mandateFieldView.getId(), fieldResult);
        }

        public void addConditionResult(ConditionResult conditionResult){
            conditionResult.parentClassResult = this;
            conditionResultMap.put(conditionResult.conditionView.getId(), conditionResult);
        }

        public MandateFieldMessageView getMessage(){
            if(!isPassRequired){
                MandateFieldMessageView mandateFieldMessageView = new MandateFieldMessageView();
                mandateFieldMessageView.setPageName(mandateFieldClassView.getPageName());
                mandateFieldMessageView.setFieldDesc(mandateFieldClassView.getClassDescription());
                mandateFieldMessageView.setFieldName(mandateFieldClassView.getClassName());

                mandateFieldMessageView.setMessage(validationMsg.get(ACTION_DATA_REQUIRED, mandateFieldClassView.getClassDescription()));
                return mandateFieldMessageView;
            }
            return null;
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                    .append("isPassRequired", isPassRequired)
                    .append("id", id)
                    .append("mandateFieldClassView", mandateFieldClassView)
                    .append("fieldResultMap", fieldResultMap == null? 0: fieldResultMap.size())
                    .append("conditionResultMap", conditionResultMap == null? 0: conditionResultMap.size())
                    .toString();
        }
    }

    //To Store the Field Result
    private class FieldResult {
        MandateFieldView mandateFieldView = null;
        Boolean isPassMin = null;
        Boolean isPassMax = null;
        Boolean isPassMatched = null;
        Boolean isPassNotMatched = null;

        ClassResult parentClassResult = null;
        Boolean isDepended = Boolean.FALSE;

        public FieldResult(MandateFieldView in){
            mandateFieldView = in;
        }

        public boolean getFinalResult(){
            if(isPassMin != null && !isPassMin)
                return Boolean.FALSE;
            if(isPassMax != null && !isPassMax)
                return Boolean.FALSE;
            if(isPassMatched != null && !isPassMatched)
                return Boolean.FALSE;
            if(isPassNotMatched != null && !isPassNotMatched)
                return Boolean.FALSE;
            return Boolean.TRUE;
        }

        public MandateFieldMessageView getMessage(){

            StringBuilder stringBuilder = new StringBuilder();
            if(isPassMin != null && isPassMax != null){
                if(!isPassMin || !isPassMax)
                    stringBuilder.append(validationMsg.get(ACTION_DATA_MIN_MAX_INCORRECT, mandateFieldView.getFieldDesc(), mandateFieldView.getMinValue(), mandateFieldView.getMaxValue()));
            }
            else if(isPassMin != null && !isPassMin) {
                stringBuilder.append(validationMsg.get(ACTION_DATA_MIN_INCORRECT, mandateFieldView.getFieldDesc(), mandateFieldView.getMinValue()));
            } else if(isPassMax != null && !isPassMax){
                stringBuilder.append(validationMsg.get(ACTION_DATA_MAX_INCORRECT, mandateFieldView.getFieldDesc(), mandateFieldView.getMaxValue()));
            }

            if(isPassMatched != null && !isPassMatched){
                stringBuilder.append(stringBuilder.length() > 0?",":"");
                stringBuilder.append(validationMsg.get(ACTION_DATA_MATCHED_INCORRECT, mandateFieldView.getFieldDesc(), mandateFieldView.getMatchedValue()));
            }

            if(isPassNotMatched != null && !isPassNotMatched){
                stringBuilder.append(stringBuilder.length() > 0?",":"");
                stringBuilder.append(validationMsg.get(ACTION_DATA_NOT_MATCHED_INCORRECT, mandateFieldView.getFieldDesc(), mandateFieldView.getNotMatchedValue()));
            }

            if(stringBuilder.length() > 0){
                MandateFieldMessageView mandateFieldMessageView = new MandateFieldMessageView();
                mandateFieldMessageView.setPageName(mandateFieldView.getPage());
                mandateFieldMessageView.setFieldDesc(mandateFieldView.getFieldDesc());
                mandateFieldMessageView.setFieldName(mandateFieldView.getFieldName());
                mandateFieldMessageView.setMessage(stringBuilder.toString());
                return mandateFieldMessageView;
            }
            return null;
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                    .append("mandateFieldView", mandateFieldView.getFieldName())
                    .append("isPassMin", isPassMin)
                    .append("isPassMax", isPassMax)
                    .append("isPassMatched", isPassMatched)
                    .append("isPassNotMatched", isPassNotMatched)
                    .toString();
        }
    }

    //To Store the ConditionResult
    private class ConditionResult{
        MandateFieldConditionView conditionView = null;
        Boolean isPass = null;
        Boolean isPassCombine = null;
        ClassResult parentClassResult = null;
        List<FieldResult> fieldResultList = null;
        Boolean isDepended = Boolean.FALSE;
        String refClassName = null;
        Long refClassId = null;

        public ConditionResult(MandateFieldConditionView in){
            conditionView = in;
            fieldResultList = new ArrayList<FieldResult>();
        }

        public void addFieldResult(FieldResult fieldResult){
            fieldResultList.add(fieldResult);
        }

        public List<MandateFieldMessageView> getMessage(){
            List<MandateFieldMessageView> mandateFieldMessageViewList = new ArrayList<MandateFieldMessageView>();
            if(!isPass){
                MandateFieldClassView mandateFieldClassView = conditionView.getMandateFieldClassView();

                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(validationMsg.get(ACTION_VERIFY_FIELD_COND));
                for(FieldResult fieldResult : fieldResultList){
                    if(!fieldResult.getFinalResult()){
                        MandateFieldView fieldView = fieldResult.mandateFieldView;

                        //Create Message View for each field result in condition
                        MandateFieldMessageView mandateFieldMessageView = new MandateFieldMessageView();
                        mandateFieldMessageView.setPageName(mandateFieldClassView.getPageName());
                        mandateFieldMessageView.setFieldDesc(conditionView.getConditionDesc() == null ? conditionView.getName() : conditionView.getConditionDesc());
                        mandateFieldMessageView.setFieldName(conditionView.getName() + ": " + fieldView.getFieldName());
                        mandateFieldMessageView.setMessage(fieldResult.getMessage().getMessage());
                        mandateFieldMessageViewList.add(mandateFieldMessageView);
                    }
                }
            }
            return mandateFieldMessageViewList;
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                    .append("isPass", isPass)
                    .append("isPassCombine", isPassCombine)
                    .append("conditionView", conditionView)
                    .toString();
        }
    }

    public Map<String, List<MandateFieldView>> getMandateFieldViewMap() {
        return mandateFieldViewMap;
    }

    public void setMandateFieldViewMap(Map<String, List<MandateFieldView>> mandateFieldViewMap) {
        this.mandateFieldViewMap = mandateFieldViewMap;
    }

    public Map<String, MandateFieldClassStepActionView> getMandateFieldClassStepActionViewMap() {
        return mandateFieldClassStepActionViewMap;
    }

    public void setMandateFieldClassStepActionViewMap(Map<String, MandateFieldClassStepActionView> mandateFieldClassStepActionViewMap) {
        this.mandateFieldClassStepActionViewMap = mandateFieldClassStepActionViewMap;
    }

    public Map<Long, MandateFieldConditionView> getMandateFieldConditionMap() {
        return mandateFieldConditionMap;
    }

    public void setMandateFieldConditionMap(Map<Long, MandateFieldConditionView> mandateFieldConditionMap) {
        this.mandateFieldConditionMap = mandateFieldConditionMap;
    }
}
