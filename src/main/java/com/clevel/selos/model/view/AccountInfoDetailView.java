package com.clevel.selos.model.view;



import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.ArrayList;
import java.util.List;

public class AccountInfoDetailView {
    private int reqAccountType;

    private List<AccountNameView> accountNameViewList;
    private List<AccountInfoCreditTypeView> accountInfoCreditTypeViews;
    private List<AccountInfoPurposeView> accountInfoPurposeViews;

    public AccountInfoDetailView(){
        reset();
    }

    public void reset(){

    }

}
