package com.clevel.selos.businesscontrol;

import com.clevel.selos.dao.master.MandateFieldDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.ActionResult;
import com.clevel.selos.model.db.master.CreditType;
import com.clevel.selos.model.db.master.MandateField;
import com.clevel.selos.model.db.working.NewCreditDetail;
import com.clevel.selos.model.view.ActionValidationResult;
import com.clevel.selos.model.view.MandateFieldMessageView;
import com.clevel.selos.model.view.MandateFieldView;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.system.message.ValidationMessage;
import com.clevel.selos.transform.MandateFieldTransform;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Stateless
public class ActionValidationControl extends BusinessControl{

    @Inject
    @SELOS
    Logger logger;

    @Inject
    MandateFieldDAO mandateFieldDAO;

    @Inject
    @ValidationMessage
    Message validationMsg;

    @Inject
    MandateFieldTransform mandateFieldTransform;

    private final static String ACTION_DATA_REQUIRED = "1151";
    private final static String ACTION_DATA_INCORRECT = "1152";

    private Map<String, List<MandateFieldView>> mandateFieldViewMap;
    private ActionValidationResult actionValidationResult;

    public int loadActionValidation(String step, String action){
        mandateFieldViewMap = new HashMap<String, List<MandateFieldView>>();
        List<MandateField> mandateFieldList = mandateFieldDAO.findActiveAll();

        for(MandateField mandateField : mandateFieldList){
            MandateFieldView mandateFieldView = mandateFieldTransform.transformToView(mandateField);
            List<MandateFieldView> mandateFieldViewList = mandateFieldViewMap.get(mandateFieldView.getClassName());
            if(mandateFieldList == null)
                mandateFieldViewList = new ArrayList<MandateFieldView>();
            mandateFieldViewList.add(mandateFieldView);

            logger.debug("mandateFieldView {}", mandateFieldView);
            mandateFieldViewMap.put(mandateFieldView.getClassName(), mandateFieldViewList);
        }
        actionValidationResult = new ActionValidationResult();
        List<MandateFieldMessageView> mandateFieldMessageViewList = new ArrayList<MandateFieldMessageView>();
        actionValidationResult.setMandateFieldMessageViewList(mandateFieldMessageViewList);
        return mandateFieldViewMap.size();
    }

    public boolean validate(List<Object> objectList){
        for(Object object : objectList){
            validate(object);
        }
        return true;
    }

    public boolean validate(Object object){
        if(object != null){
            Class objectClass = object.getClass();

            List<MandateFieldView> mandateFieldViewList = mandateFieldViewMap.get(objectClass.getName());
            Field[] fields = objectClass.getDeclaredFields();
            for(Field field : fields){
                logger.debug("check for field name {}", field.getName());
                field.setAccessible(true);
                for(MandateFieldView mandateFieldView : mandateFieldViewList){
                    logger.debug("loop for mandateFieldView name {}", mandateFieldView);
                    if(field.getName().equals(mandateFieldView.getFieldName())){
                        logger.debug("found name matched field: {}, mandateFieldView: {}", field.getName(), mandateFieldView.getFieldName());
                        mandateFieldView.setChecked(Boolean.TRUE);
                        boolean failed = false;

                        try{
                            Object _fieldObj = field.get(object);
                            if(_fieldObj == null){
                                failed = true;
                            }
                            Class _fieldObjClass = _fieldObj.getClass();
                            if(_fieldObjClass.equals(Integer.class.getName())){
                                if((Integer)_fieldObj == 0){
                                    failed = true;
                                }
                            } else if(_fieldObjClass.equals(Long.class.getName())){
                                if((Long)_fieldObj == 0){
                                    failed = true;
                                }
                            } else if(_fieldObjClass.equals(BigDecimal.class.getName())){
                                if(BigDecimal.ZERO.equals((BigDecimal)_fieldObj)){
                                    failed = true;
                                }
                            } else if(_fieldObjClass.equals(List.class.getClass())){
                                if(((List)_fieldObj).size() == 0){
                                    failed = true;
                                }
                            }
                        } catch (IllegalAccessException iae){
                            logger.debug("Cannot validate Field to validate Field {}", field.getName());
                            failed = true;
                        }

                        if(failed){
                            addMandateFieldMessageView(mandateFieldView.getFieldDesc(), mandateFieldView.getFieldDesc(),validationMsg.get(ACTION_DATA_REQUIRED, field.getName()), mandateFieldView.getPage());
                        }
                        break;
                    }
                }
            }
        }
        return true;
    }

    private void addMandateFieldMessageView(String fieldName, String description, String message, String pageName){
        MandateFieldMessageView mandateFieldMessageView = new MandateFieldMessageView();
        mandateFieldMessageView.setFieldName(fieldName);
        mandateFieldMessageView.setMessage(message);
        mandateFieldMessageView.setPageName(pageName);
        actionValidationResult.getMandateFieldMessageViewList().add(mandateFieldMessageView);
    }

    public List<MandateField> getMandateFieldConfigureList(){
        List<MandateField> mandateFieldConfigureList = new ArrayList<MandateField>();
        MandateField mandateFieldConfigure = new MandateField();
        mandateFieldConfigure.setId(1);
        mandateFieldConfigure.setClassName("com.clevel.selos.model.db.working.NewCreditDetail");
        mandateFieldConfigure.setFieldName("creditType");
        return mandateFieldConfigureList;
    }

    public ActionValidationResult getFinalValidationResult(){

        if(actionValidationResult.getMandateFieldMessageViewList().size() > 0){
            actionValidationResult.setActionResult(ActionResult.FAILED);

        } else {
            actionValidationResult.setActionResult(ActionResult.SUCCESS);
        }
        return actionValidationResult;
    }
}
