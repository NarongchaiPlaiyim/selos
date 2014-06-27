package com.clevel.selos.businesscontrol.isa.csv.validation;

import com.clevel.selos.businesscontrol.isa.csv.command.CommandType;
import com.clevel.selos.businesscontrol.isa.csv.model.CSVModel;
import com.clevel.selos.model.UserStatus;
import com.clevel.selos.util.Util;

public class ValidationImp{
    public String valid(final CSVModel csvModel, final CommandType commandType) {
        String result = "";
        if(CommandType.INSERT.equals(commandType.toString())){
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
            if(!"0".equalsIgnoreCase(csvModel.getActive()) && !"1".equalsIgnoreCase(csvModel.getActive())){
                return "active is invalid";
            }
        }
        if(Util.isNull(csvModel.getRole()) || Util.isZero(csvModel.getRole().length())){
            return "role is required";
        }
        if(Util.isNull(csvModel.getDepartment()) || Util.isZero(csvModel.getDepartment().length())){
            return "department is required";
        }
        if(Util.isNull(csvModel.getDivision()) || Util.isZero(csvModel.getDivision().length())){
            return "division is required";
        }
        if(Util.isNull(csvModel.getRegion()) || Util.isZero(csvModel.getRegion().length())){
            return "region is required";
        }
        if(Util.isNull(csvModel.getTeam()) || Util.isZero(csvModel.getTeam().length())){
            return "team is required";
        }
        if(Util.isNull(csvModel.getTitle()) || Util.isZero(csvModel.getTitle().length())){
            return "title is required";
        }
        if(Util.isNull(csvModel.getStatus()) || Util.isZero(csvModel.getStatus().length())){
            return "status is required";
        } else {
            if(UserStatus.NORMAL.equals(csvModel.getStatus()) && UserStatus.DISABLED.equals(csvModel.getStatus()) && UserStatus.MARK_AS_DELETED.equals(csvModel.getStatus())){
                return "status is invalid";
            }
        }
        return "";
    }

    private String checkUpdate(final CSVModel csvModel){
        if(Util.isNull(csvModel.getUserId()) || Util.isZero(csvModel.getUserId().length())){
            return "userId is required";
        }
        if(!Util.isNull(csvModel.getActive()) && !Util.isZero(csvModel.getActive().length())){
            if(!"0".equalsIgnoreCase(csvModel.getActive()) && !"1".equalsIgnoreCase(csvModel.getActive())){
                return "active is invalid";
            }
        }
        if(!Util.isNull(csvModel.getStatus()) && !Util.isZero(csvModel.getStatus().length())){
            if(UserStatus.NORMAL.equals(csvModel.getStatus()) && UserStatus.DISABLED.equals(csvModel.getStatus()) && UserStatus.MARK_AS_DELETED.equals(csvModel.getStatus())){
                return "status is invalid";
            }
        }
        return "";
    }

    private String checkDelete(final CSVModel csvModel){
        if(Util.isNull(csvModel.getUserId()) || Util.isZero(csvModel.getUserId().length())){
            return "userId is required";
        }
        return "";
    }
}
