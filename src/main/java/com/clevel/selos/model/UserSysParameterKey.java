package com.clevel.selos.model;

public enum UserSysParameterKey {

    BRMS_ACCOUNT_ACTIVE_FLAG_CARDLINK_CODE("brms.tmb.account.active.flag.cardlink.code"),
    BRMS_ACCOUNT_ACTIVE_FLAG_CARDLINK_BLOCKCODE("brms.tmb.account.active.flag.ds.cardlink.blockcode"),
    BRMS_ACCOUNT_ACTIVE_FLAG_ALS_CODE("brms.tmb.account.active.flag.als.code"),
    BRMS_ACCOUNT_ACTIVE_FLAG_ALS_STATUS_CON1("brms.tmb.account.active.flag.als.status.con1"),
    BRMS_ACCOUNT_ACTIVE_FLAG_ALS_STATUS_CON2("brms.tmb.account.active.flag.als.status.con2"),
    BRMS_ACCOUNT_ACTIVE_FLAG_IM_CODE("brms.tmb.account.active.flag.im.code"),
    BRMS_ACCOUNT_ACTIVE_FLAG_IM_STATUS("brms.tmb.account.active.flag.im.status"),
    BRMS_ACCOUNT_ACTIVE_FLAG_LOANBILL_CODE("brms.tmb.account.active.flag.loanbill.code"),
    BRMS_ACCOUNT_ACTIVE_FLAG_LOANBILL_STATUS("brms.tmb.account.active.flag.loanbill.status"),
    BRMS_ACCOUNT_ACTIVE_FLAG_EXIMBILL_CODE("brms.tmb.account.active.flag.eximbill.code"),
    BRMS_ACCOUNT_ACTIVE_FLAG_EXIMBILL_STATUS("brms.tmb.account.active.flag.eximbill.status"),
    BRMS_ACCOUNT_ACTIVE_FLAG_MUREX_CODE("brms.tmb.account.active.flag.murex.code"),
    BRMS_ACCOUNT_ACTIVE_FLAG_MUREX_STATUS("brms.tmb.account.active.flag.murex.status"),
    BRMS_ACCOUNT_ACTIVE_FLAG_OPIC_CODE("brms.tmb.account.active.flag.opic.code"),
    BRMS_ACCOUNT_ACTIVE_FLAG_OPIC_STATUS("brms.tmb.account.active.flag.opic.status");

    private String key;
    private UserSysParameterKey(String key){
        this.key = key;
    }
}
