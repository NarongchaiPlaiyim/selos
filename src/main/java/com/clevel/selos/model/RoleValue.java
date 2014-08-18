package com.clevel.selos.model;

public enum RoleValue {
    SYSTEM(1), ISA(11), ADMIN(21), ABDM(101), BDM(102),
    ZM(103), RGM(104), GH(105), CSSO(106), UW(107),
    AAD_ADMIN(108), AAD_COMITTEE(109), SSO(110), CONTACT_CENTER(201), INSURANCE_CENTER(202),
    DOC_CHECK(203), DATA_ENTRY_MAKER(204), DATA_ENTRY_CHECK(205), TCG(206), CDM(207),
    LAR_BC(208), CO1(209), CO2(210), LS(211), LS_MAKER(212),
    LD(213), TH_LAR(214), TH_BC(215), TH_AAD(216), TH_CO(217),
    GH_CLO(218), TH_BO(219), GH_BO(220), TL_DATA_ENTRY(221), TL_CDM(222),
    VIEWER(301);

    int id;

    RoleValue(int id) {
        this.id = id;
    }

    public int id() {
        return this.id;
    }
}
