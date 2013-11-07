package com.clevel.selos.ui.component.calendar;

import org.primefaces.component.calendar.Calendar;

import javax.faces.component.FacesComponent;

@FacesComponent(BECalendar.COMPONENT_TYPE)
public class BECalendar extends Calendar {
    public static final String COMPONENT_FAMILY = "com.clevel.selos.ui";
    public static final String COMPONENT_TYPE = "com.clevel.selos.ui.component.calendar.BECalendar";

    private enum PropertyKeys {
        isBE
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Override
    public String getRendererType() {
        return BECalendarRenderer.RENDERER_TYPE;
    }

    public String getBE() {
        return (String) getStateHelper().eval(PropertyKeys.isBE, null);
    }

    public void setBE(String _isBE) {
        getStateHelper().put(PropertyKeys.isBE, _isBE);
    }

}
