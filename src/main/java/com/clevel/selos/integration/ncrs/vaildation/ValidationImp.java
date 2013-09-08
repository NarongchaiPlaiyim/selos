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
        if(!ValidationUtil.isNull(model.getMemberref()) && ValidationUtil.isGreaterThan(25, model.getMemberref())){
            throw new ValidationException("Length of memberref is more than 25");
        }
        if(!"01".equals(model.getEnqpurpose())||!"02".equals(model.getEnqpurpose())){
            throw new ValidationException(message.get("105"));
        }

        //if(!ValidationUtil.isNull(enqamount) && ValidationUtil.isGreaterThan(9, enqamount))throw new ValidationException("Length of enqamount is more than 9");

        if(!"Y".equals(model.getEnqpurpose())){
            throw new ValidationException(message.get("107"));
        }

        /*if(ValidationUtil.isLessThan(1, nameList))throw new ValidationException("Size of nameList is less than 1");
        for (TUEFEnquiryNameModel nameModel : nameList)nameModel.validation();

        if(ValidationUtil.isLessThan(1, idList))throw new ValidationException("Size of idList is less than 1");
        for (TUEFEnquiryIdModel idModel : idList) idModel.validation(); */
    }

    @Override
    public void validation(TUEFEnquiryNameModel model) throws Exception {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void validation(TUEFEnquiryIdModel model) throws Exception {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
