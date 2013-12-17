package com.clevel.selos.ui.component.calendar;

import org.primefaces.component.calendar.Calendar;
import org.primefaces.component.calendar.CalendarRenderer;
import org.primefaces.component.calendar.CalendarUtils;
import org.primefaces.util.WidgetBuilder;

import javax.faces.context.FacesContext;
import javax.faces.render.FacesRenderer;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.Locale;

@FacesRenderer(
        componentFamily = BECalendar.COMPONENT_FAMILY,
        rendererType = BECalendarRenderer.RENDERER_TYPE
)
public class BECalendarRenderer extends CalendarRenderer implements Serializable {
    public static final String RENDERER_TYPE = "com.clevel.selos.ui.component.calendar.BECalendarRenderer";

    @Override
    protected void encodeScript(FacesContext context, Calendar calendar, String value) throws IOException {
        String clientId = calendar.getClientId(context);
        Locale locale = calendar.calculateLocale(context);
        String pattern = calendar.isTimeOnly() ? calendar.calculateTimeOnlyPattern() : calendar.calculatePattern();
        WidgetBuilder wb = getWidgetBuilder(context);
        wb.initWithDomReady("Calendar", calendar.resolveWidgetVar(), clientId);

        wb.attr("popup", calendar.isPopup())
                .attr("locale", locale.toString())
                .attr("dateFormat", CalendarUtils.convertPattern(pattern))
                .attr("isBE", true);

        //default date
        Object pagedate = calendar.getPagedate();
        String defaultDate = null;

        if (calendar.isConversionFailed()) {
            defaultDate = CalendarUtils.getValueAsString(context, calendar, new Date());
        } else if (!isValueBlank(value)) {
            defaultDate = value;
        } else if (pagedate != null) {
            defaultDate = CalendarUtils.getValueAsString(context, calendar, pagedate);
        }

        wb.attr("defaultDate", defaultDate, null)
                .attr("numberOfMonths", calendar.getPages(), 1)
                .attr("minDate", CalendarUtils.getValueAsString(context, calendar, calendar.getMindate()), null)
                .attr("maxDate", CalendarUtils.getValueAsString(context, calendar, calendar.getMaxdate()), null)
                .attr("showButtonPanel", calendar.isShowButtonPanel(), false)
                .attr("showWeek", calendar.isShowWeek(), false)
                .attr("disabledWeekends", calendar.isDisabledWeekends(), false)
                .attr("disabled", calendar.isDisabled(), false)
                .attr("yearRange", calendar.getYearRange(), null);

        if (calendar.isNavigator()) {
            wb.attr("changeMonth", true).attr("changeYear", true);
        }

        if (calendar.getEffect() != null) {
            wb.attr("showAnim", calendar.getEffect()).attr("duration", calendar.getEffectDuration());
        }

        String beforeShowDay = calendar.getBeforeShowDay();
        if (beforeShowDay != null) {
            wb.nativeAttr("preShowDay", beforeShowDay);
        }

        String showOn = calendar.getShowOn();
        if (!showOn.equalsIgnoreCase("focus")) {
            wb.attr("showOn", showOn);
        }

        if (calendar.isShowOtherMonths()) {
            wb.attr("showOtherMonths", true).attr("selectOtherMonths", true);
        }

        if (calendar.hasTime()) {
            wb.attr("timeOnly", calendar.isTimeOnly())
                    .attr("stepHour", calendar.getStepHour())
                    .attr("stepMinute", calendar.getStepMinute())
                    .attr("stepSecond", calendar.getStepSecond())
                    .attr("hourMin", calendar.getMinHour())
                    .attr("hourMax", calendar.getMaxHour())
                    .attr("minuteMin", calendar.getMinMinute())
                    .attr("minuteMax", calendar.getMaxMinute())
                    .attr("secondMin", calendar.getMinSecond())
                    .attr("secondMax", calendar.getMaxSecond());
        }

        encodeClientBehaviors(context, calendar);

        wb.finish();
    }
}