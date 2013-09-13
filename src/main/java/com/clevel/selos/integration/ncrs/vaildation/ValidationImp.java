package com.clevel.selos.integration.ncrs.vaildation;


import com.clevel.selos.exception.ValidationException;
import com.clevel.selos.integration.ncrs.models.request.TUEFEnquiryIdModel;
import com.clevel.selos.integration.ncrs.models.request.TUEFEnquiryNameModel;
import com.clevel.selos.integration.ncrs.service.NCRSModel;
import com.clevel.selos.integration.ncrs.vaildation.Validation;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.system.message.ValidationMessage;
import com.clevel.selos.util.ValidationUtil;

import javax.inject.Inject;
import java.io.Serializable;
import java.util.ArrayList;

public class ValidationImp implements Validation, Serializable {
    @Inject
    @ValidationMessage
    Message message;

    @Inject
    public ValidationImp() {
    }

    @Override
    public void validation(NCRSModel model) throws Exception {
        if (null==model){
            throw new ValidationException(message.get("101"));
        }
        if(ValidationUtil.isNull(model.getMemberref()) && ValidationUtil.isGreaterThan(25, model.getMemberref())){
            throw new ValidationException("Length of memberref is more than 25");
        }
        if(!"01".equals(model.getEnqpurpose())||!"02".equals(model.getEnqpurpose())){
            throw new ValidationException(message.get("105"));
        }

        //if(!ValidationUtil.isNull(enqamount) && ValidationUtil.isGreaterThan(9, enqamount))throw new ValidationException("Length of enqamount is more than 9");

        if(!"Y".equals(model.getEnqpurpose())){
            throw new ValidationException(message.get("107"));
        }

        if(!ValidationUtil.isValueInRange(1, 2, model.getNameList())){
            //Name can't be more than 2 names
        } else {
            ArrayList<TUEFEnquiryNameModel> arrayList = model.getNameList();
            for(TUEFEnquiryNameModel nameModel : arrayList){
                validation(nameModel);
            }
        }

        if(!ValidationUtil.isValueInRange(1, 4, model.getIdList())){
            //ID can't be more than 4 ids
        } else {
            ArrayList<TUEFEnquiryIdModel> arrayList = model.getIdList();
            for(TUEFEnquiryIdModel idModel : arrayList){
                validation(idModel);
            }
        }



    }

    @Override
    public void validation(TUEFEnquiryNameModel model) throws Exception {

        if(!ValidationUtil.isValueInRange(1,50,model.getFamilyname())){
            //Two character minimum
        }
        if(!ValidationUtil.isValueInRange(1,30,model.getFirstname())){
            //Two character minimum
        }
        if(!ValidationUtil.isNull(model.getMiddlename()) && ValidationUtil.isGreaterThan(26, model.getMiddlename())){
            // can null but if not null length of middlename <= 26
        }
        if(!ValidationUtil.isValueInRange(8,8,model.getDateofbirth())||!ValidationUtil.isInteger(model.getDateofbirth())){
            //
        }

    }

    @Override
    public void validation(TUEFEnquiryIdModel model) throws Exception {
        String idType = null;
        if(!ValidationUtil.isValueInRange(2,2,model.getIdtype())){
            //Two character minimum
        } else {
            idType = model.getIdtype();
            boolean flag = true;
            for (int i=0;i<=9;i++) {
                if (!idType.equals("0" + i)) {
                    continue;
                } else {
                    flag = false;
                    return;
                }
            }
            if(flag){
                //Values are 01-09
            }
        }
        if("01".equals(idType)){
            model.setIssuecountry("TH");
        } else {
            model.setIssuecountry("");
        }

        if(!ValidationUtil.isNull(model.getIdnumber()) && ValidationUtil.isGreaterThan(20, model.getIdnumber())){
            //not null and 20 character
        }
        if("01".equals(idType)&&!ValidationUtil.isValueInRange(13,13,model.getIdnumber())){
            //The length of citizen id is 13
        }

    }
}
