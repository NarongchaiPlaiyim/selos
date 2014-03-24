package com.clevel.selos.businesscontrol;

import com.clevel.selos.dao.master.MandateFieldDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.ActionResult;
import com.clevel.selos.model.db.master.CreditType;
import com.clevel.selos.model.db.master.MandateField;
import com.clevel.selos.model.db.working.NewCreditDetail;
import com.clevel.selos.model.view.ActionValidationResult;
import com.clevel.selos.model.view.MandateFieldMessageView;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.system.message.ValidationMessage;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.lang.reflect.Field;
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
    MandateFieldDAO mandateFieldConfigureDAO;

    @Inject
    @ValidationMessage
    Message validationMsg;

    private final static String ACTION_DATA_REQUIRED = "1151";
    private final static String ACTION_DATA_INCORRECT = "1152";

    public ActionValidationResult ActionValidationControl(String step, String status, String action){
        ActionValidationResult actionValidationResult = new ActionValidationResult();
        actionValidationResult.setActionResult(ActionResult.SUCCESS);

        Map<String, String> reasonMap = new HashMap<String, String>();

        //List<MandateFieldConfigure> mandateFieldConfigureList = mandateFieldConfigureDAO.findByAction(step, status, action);
        List<MandateField> mandateFieldConfigureList = getMandateFieldConfigureList();
        for(MandateField mandateFieldConfigure : mandateFieldConfigureList){

        }

        return actionValidationResult;
    }

    public ActionValidationResult validateClass(Object obj){
        System.out.println("class Name : " + obj.getClass().getName());

        List<MandateFieldMessageView> mandateFieldMessageViewList = new ArrayList<MandateFieldMessageView>();

        Class objClass = obj.getClass();

        Field[] fields = objClass.getDeclaredFields();
        System.out.println("fields length: " + fields.length);
        String compareField = "creditType";
        for(Field field : fields){
            System.out.println(field.getName());
            try{
                field.setAccessible(true);
                if(field.getName().equals(compareField)){
                    Object value = field.get(obj);
                    if(value.getClass().isPrimitive()){
                        Class valueClass = value.getClass();
                        if(valueClass.equals(String.class.getName())){
                            if(value == null){
                                mandateFieldMessageViewList.add(getMandateFieldMessageView(field.getName(), validationMsg.get(ACTION_DATA_REQUIRED, field.getName()), ""));
                            }
                        } else if(valueClass.equals(Integer.class.getName())){
                            if((Integer)value == 0){
                                mandateFieldMessageViewList.add(getMandateFieldMessageView(field.getName(), validationMsg.get(ACTION_DATA_REQUIRED, field.getName()), ""));
                            }
                        } else if(valueClass.equals(Long.class.getName())){

                        }
                    } else {
                        //reasonMap.put("NewCreditDetail", field.getName());
                    }
                }
            } catch (IllegalAccessException iae){
                System.out.println("Cannot Convert Fields");
                //reasonMap.put(field.getName(), "Cannot Convert Field");
            }
        }

        ActionValidationResult actionValidationResult = new ActionValidationResult();
        actionValidationResult.setActionResult(ActionResult.SUCCESS);
        if(mandateFieldMessageViewList.size() == 0){
            actionValidationResult.setActionResult(ActionResult.SUCCESS);
        } else {
            actionValidationResult.setActionResult(ActionResult.FAILED);
            actionValidationResult.setMandateFieldMessageViewList(mandateFieldMessageViewList);
        }
        return actionValidationResult;
    }

    private MandateFieldMessageView getMandateFieldMessageView(String fieldName, String message, String pageName){
        MandateFieldMessageView mandateFieldMessageView = new MandateFieldMessageView();
        mandateFieldMessageView.setFieldName(fieldName);
        mandateFieldMessageView.setMessage(message);
        mandateFieldMessageView.setPageName(pageName);
        return mandateFieldMessageView;
    }

    public List<MandateField> getMandateFieldConfigureList(){
        List<MandateField> mandateFieldConfigureList = new ArrayList<MandateField>();
        MandateField mandateFieldConfigure = new MandateField();
        mandateFieldConfigure.setId(1);
        mandateFieldConfigure.setClassName("com.clevel.selos.model.db.working.NewCreditDetail");
        mandateFieldConfigure.setFieldName("creditType");
        return mandateFieldConfigureList;
    }

    public static void main(String args[]){

        ActionValidationControl actionValidationControl = new ActionValidationControl();
        NewCreditDetail newCreditDetail = new NewCreditDetail();
        newCreditDetail.setCreditType(new CreditType());
        ActionValidationResult actionValidationResult = actionValidationControl.validateClass(new NewCreditDetail());
        System.out.print("result action Validation " + actionValidationResult);

    }


}
