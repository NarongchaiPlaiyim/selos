package com.clevel.selos.businesscontrol;

import com.clevel.selos.dao.master.DistrictDAO;
import com.clevel.selos.dao.master.ProvinceDAO;
import com.clevel.selos.dao.master.SubDistrictDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.District;
import com.clevel.selos.model.db.master.Province;
import com.clevel.selos.model.db.master.SubDistrict;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.io.Serializable;
import java.util.List;

public class AddressFilterControl implements Serializable {
    @Inject
    @SELOS
    Logger log;
    @Inject
    ProvinceDAO provinceDAO;
    @Inject
    DistrictDAO districtDAO;
    @Inject
    SubDistrictDAO subDistrictDAO;

    public List<Province> onGetListOrderByParameter(String parameter) {

        List<Province> provinceList;
        try {
            log.info("onFilterByProvince begin");

            provinceList = provinceDAO.getListOrderByParameter(parameter);

            return provinceList;

        } catch (Exception e) {
            log.error("onFilterByProvince error" + e);
            return null;
        } finally {

            log.info("onFilterByProvince end");

        }
    }

    public List<District> onFilterByProvince(Province province) {
        List<District> districtList;
        try {
            log.info("onFilterByProvince begin");

            districtList = districtDAO.getListByProvince(province);

            return districtList;
        } catch (Exception e) {
            log.error("onFilterByProvince error" + e);
            return null;
        } finally {

            log.info("onFilterByProvince end");

        }
    }

    public List<SubDistrict> onFilterByDistrict(District district) {
        List<SubDistrict> subDistrictList;
        try {
            log.info("onFilterByDistrict begin");

            subDistrictList = subDistrictDAO.getListByDistrict(district);

            return subDistrictList;

        } catch (Exception e) {
            log.error("onFilterByDistrict error" + e);
            return null;

        } finally {

            log.info("onFilterByDistrict end");

        }
    }


}
