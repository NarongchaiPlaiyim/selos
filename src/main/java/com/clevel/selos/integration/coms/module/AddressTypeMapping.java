package com.clevel.selos.integration.coms.module;

import com.clevel.selos.integration.coms.model.AddressType;
import com.clevel.selos.integration.coms.model.OnlType;

import javax.inject.Inject;
import java.io.Serializable;

public class AddressTypeMapping implements Serializable {

    @Inject
    public AddressTypeMapping() {

    }

    public AddressType getAddressType(String colType, String subColType, String onlType){
        if(OnlType.getValue(onlType) == OnlType.LAND) {
            if(colType.equalsIgnoreCase("286003") && subColType.equalsIgnoreCase("01")){
                return AddressType.TYPE1;
            }
            if(colType.equalsIgnoreCase("286003") && subColType.equalsIgnoreCase("03")){
                return AddressType.TYPE2;
            }
            if(colType.equalsIgnoreCase("286003") && subColType.equalsIgnoreCase("02")){
                return AddressType.TYPE3;
            }
            if(colType.equalsIgnoreCase("286003") && subColType.equalsIgnoreCase("04")){
                return AddressType.TYPE4;
            }
            if(colType.equalsIgnoreCase("286004") && subColType.equalsIgnoreCase("10")){
                return AddressType.TYPE6;
            }
        }

        if(OnlType.getValue(onlType) == OnlType.LAND) {
            if((colType.equalsIgnoreCase("286004") || colType.equalsIgnoreCase("286010"))  && !subColType.equalsIgnoreCase("10")){
                return AddressType.TYPE5;
            }
        }

        if(OnlType.getValue(onlType) == OnlType.MACHINE) {
            return AddressType.TYPE7;
        }

        if(OnlType.getValue(onlType) == OnlType.GOODS) {
            return AddressType.TYPE8;
        }

        if(OnlType.getValue(onlType) == OnlType.CAR) {
            return AddressType.TYPE9;
        }

        if(OnlType.getValue(onlType) == OnlType.SHIP) {
            return AddressType.TYPE10;
        }

        if(OnlType.getValue(onlType) == OnlType.RENT) {
            return AddressType.TYPE11;
        }

        if(OnlType.getValue(onlType) == OnlType.OTHER) {
            return AddressType.TYPE12;
        }

        return null;
    }
}
