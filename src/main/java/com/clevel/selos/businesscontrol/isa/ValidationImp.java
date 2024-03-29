package com.clevel.selos.businesscontrol.isa;

import com.clevel.selos.model.CommandType;
import com.clevel.selos.model.UserStatus;
import com.clevel.selos.model.view.isa.CSVModel;
import com.clevel.selos.util.Util;

public class ValidationImp{
    protected String valid(final CSVModel csvModel, final CommandType commandType) {
        String result = "";
        if(CommandType.CREATE.equals(commandType.toString())){
            result = checkInsert(csvModel);
        } else if(CommandType.UPDATE.equals(commandType.toString())){
            result = checkUpdate(csvModel);
        } else if(CommandType.DELETE.equals(commandType.toString())){
            result = checkDelete(csvModel);
        }
        return result;
    }

    private String checkInsert(final CSVModel csvModel){
        if(Util.isNull(csvModel.getUserId()) || Util.isZero(csvModel.getUserId().length())){
            return "userId is required";
        }
        if(Util.isNull(csvModel.getUserName()) || Util.isZero(csvModel.getUserName().length())){
            return "userName is required";
        }
        if(Util.isNull(csvModel.getActive()) || Util.isZero(csvModel.getActive().length())){
            return "active is required";
        } else {
            if(!"INACTIVE".equalsIgnoreCase(csvModel.getActive()) && !"ACTIVE".equalsIgnoreCase(csvModel.getActive())){
                return "active is invalid";
            }
        }
        if(Util.isNull(csvModel.getRole()) || Util.isZero(csvModel.getRole().length())){
            return "role is required";
        }
        if(Util.isNull(csvModel.getTeam()) || Util.isZero(csvModel.getTeam().length())){
            return "team is required";
        }
//        if(Util.isNull(csvModel.getDepartment()) || Util.isZero(csvModel.getDepartment().length())){
//            return "department is required";
//        }
//        if(Util.isNull(csvModel.getDivision()) || Util.isZero(csvModel.getDivision().length())){
//            return "division is required";
//        }
//        if(Util.isNull(csvModel.getRegion()) || Util.isZero(csvModel.getRegion().length())){
//            return "region is required";
//        }
//        if(Util.isNull(csvModel.getTitle()) || Util.isZero(csvModel.getTitle().length())){
//            return "title is required";
//        }
        return "";
    }

    private String checkUpdate(final CSVModel csvModel){
        if(Util.isNull(csvModel.getUserId()) || Util.isZero(csvModel.getUserId().length())){
            return "userId is required";
        }
        if(Util.isNull(csvModel.getUserName()) || Util.isZero(csvModel.getUserName().length())){
            return "userName is required";
        }
        if(Util.isNull(csvModel.getRole()) || Util.isZero(csvModel.getRole().length())){
            return "role is required";
        }
        if(Util.isNull(csvModel.getTeam()) || Util.isZero(csvModel.getTeam().length())){
            return "team is required";
        }
        if(!Util.isNull(csvModel.getActive()) && !Util.isZero(csvModel.getActive().length())){
            if(!"INACTIVE".equalsIgnoreCase(csvModel.getActive()) && !"ACTIVE".equalsIgnoreCase(csvModel.getActive())){
                return "active is invalid";
            }
        }
//        if(!Util.isNull(csvModel.getStatus()) && !Util.isZero(csvModel.getStatus().length())){
//            if(UserStatus.NORMAL.equals(csvModel.getStatus()) && UserStatus.DISABLED.equals(csvModel.getStatus()) && UserStatus.MARK_AS_DELETED.equals(csvModel.getStatus())){
//                return "status is invalid";
//            }
//        }
        return "";
    }

    private String checkDelete(final CSVModel csvModel){
        if(Util.isNull(csvModel.getUserId()) || Util.isZero(csvModel.getUserId().length())){
            return "userId is required";
        }
        return "";
    }
}
