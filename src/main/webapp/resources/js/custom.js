PrimeFaces.locales ['th_TH'] = {
    closeText: 'ปิด',
    prevText: 'ย้อนกลับ',
    nextText: 'ถัดไป',
    monthNames: ['มกราคม', 'กุมภาพันธ์', 'มีนาคม', 'เมษายน', 'พฤษภาคม', 'มิถุนายน', 'กรกฎาคม', 'สิงหาคม', 'กันยายน', 'ตุลาคม', 'พฤศจิกายน', 'ธันวาคม' ],
    //monthNamesShort: ['ม.ค.', 'ก.พ.', 'มี.ค.', 'เม.ย.', 'พ.ค.', 'มิ.ย.', 'ก.ค.', 'ส.ค.', 'ก.ย.', 'ต.ค.', 'พ.ย.', 'ธ.ค.' ],
    monthNamesShort: ['มกราคม', 'กุมภาพันธ์', 'มีนาคม', 'เมษายน', 'พฤษภาคม', 'มิถุนายน', 'กรกฎาคม', 'สิงหาคม', 'กันยายน', 'ตุลาคม', 'พฤศจิกายน', 'ธันวาคม' ],
    dayNames: ['Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday'],
    dayNamesShort: ['Sun', 'Mon', 'Tue', 'Wed', 'Tue', 'Fri', 'Sat'],
    dayNamesMin: ['อา', 'จ', 'อ', 'พ ', 'พฤ', 'ศ ', 'ส'],
    weekHeader: 'สัปดาห์',
    firstDay: 0,
    isRTL: false,
    showMonthAfterYear: false,
    yearSuffix:'',
    timeOnlyTitle: 'เวลา เท่านั้น',
    timeText: 'Time',
    hourText: 'Time',
    minuteText: 'Minute',
    secondText: 'Second',
    currentText: 'ปัจจุบัน',
    ampm: false,
    month: 'เดือน',
    week: 'สัปดาห์',
    day: 'วัน',
    allDayText: 'All Day'
};

function gotoInbox(contextUrl) {
    window.location = contextUrl;
}

function checkThaiID(id) {
    if(id=='') return true;
    if(id.length != 13) return false;
    var sum = 0;
    for(i=0; i < 12; i++) {	sum += (13-i)*parseFloat(id.charAt(i));	}
    var x = sum%11;
    if(x <= 1 && (1-x != parseFloat(id.charAt(12)))) return false;
    else if(x > 1 && (11-x != parseFloat(id.charAt(12)))) return false;
    return true;
}

function checkSearchThaiID(obj){
    if(obj != undefined){
        var id = obj.value;
        if(!checkThaiID(id)){
            PF('msgBoxInvalidCitizenSearchDlg').show();
        }
    }
}

function checkJuristicThaiID(obj){
    if(obj != undefined){
        var id = obj.value;
        if(!checkThaiID(id)){
            PF('msgBoxInvalidCitizenJrDlg').show();
        }
    }
}

function checkBorrowerThaiID(obj){
    if(obj != undefined){
        var id = obj.value;
        if(!checkThaiID(id)){
            PF('msgBoxInvalidCitizenBrDlg').show();
        }
    }
}

function checkSpouseThaiID(obj){
    if(obj != undefined){
        var id = obj.value;
        if(!checkThaiID(id)){
            PF('msgBoxInvalidCitizenSpDlg').show();
        }
    }
}

function closeMessageCitizenDialog(msgDlgName, inputId){
    PF(msgDlgName).hide();
    $("#"+inputId).focus();
}

function removeComma(obj) {
    var val = obj.value;
    val = val.replace(/,/g, '');
    obj.value = val;
}

function onClickReadonlyField(obj, readonly){
    if(readonly != 1){
        if(obj != undefined){
            obj.blur();
        }
    }
}

function formatNumber(obj) {
    var val = obj.value;
    val = $.trim(val);
    if (val != '') {
        val += '';
        x = val.split('.');
        x1 = x[0];
        x2 = x.length > 1 ? '.' + x[1] : '.00';
        var rgx = /(\d+)(\d{3})/;
        while (rgx.test(x1)) {
            x1 = x1.replace(rgx, '$1' + ',' + '$2');
        }
        obj.value = x1 + x2;
    }
}


/*** KEY PRESS AND KEY DOWN FUNCTION ***/
/* ALLOWED SOME KEY (KEY CODE & CHAR CODE)
 *  8=backspace             9=tab               46=delete
 *  35(0)=end               36(0)=home          37(0)=left arrow
 *  38(0)=up arrow          39(0)=right arrow   40(0)=down arrow
 *  111=fwd slash(num pad)  191=fwd slash       109=dash(num pad)
 */
/*
 * SPECIAL CHARACTER (KEY CODE & CHAR CODE)
 *  33=!       34="    35=#        36=$
 *  37=%       38=&    39='        40=(
 *  41=)       42=*    45=-        47=/
 *  58=:       59=;    60=<        62=<
 *  63=?       64=@    91=[        93=]
 *  94=^       95=_    123={       125=}
 */

function checkSpecialCharacter(evt){
    if( (evt.keyCode == 33 && evt.charCode == 33) || (evt.keyCode == 34 && evt.charCode == 34) || (evt.keyCode == 35 && evt.charCode == 35)
        || (evt.keyCode == 36 && evt.charCode == 36) || (evt.keyCode == 37 && evt.charCode == 37) || (evt.keyCode == 38 && evt.charCode == 38)
        || (evt.keyCode == 39 && evt.charCode == 39) || (evt.keyCode == 40 && evt.charCode == 40) || (evt.keyCode == 41 && evt.charCode == 41)
        || (evt.keyCode == 42 && evt.charCode == 42) || (evt.keyCode == 45 && evt.charCode == 45) || (evt.keyCode == 47 && evt.charCode == 47)
        || (evt.keyCode == 58 && evt.charCode == 58) || (evt.keyCode == 59 && evt.charCode == 59) || (evt.keyCode == 60 && evt.charCode == 60)
        || (evt.keyCode == 62 && evt.charCode == 62) || (evt.keyCode == 63 && evt.charCode == 63) || (evt.keyCode == 64 && evt.charCode == 64)
        || (evt.keyCode == 91 && evt.charCode == 93) || (evt.keyCode == 95 && evt.charCode == 95) || (evt.keyCode == 123 && evt.charCode == 123)
        || (evt.keyCode == 125 && evt.charCode == 125) ){
        return false;
    }
}

function onKeyNumber(evt) {
    var validNums = '0123456789';
    var nbr = evt.keyCode ? evt.keyCode : evt.which;

    /** CHECK SPECIAL CHARACTER **/
    /*
     * SPECIAL CHARACTER (KEY CODE & CHAR CODE)
     *  33=!       34="    35=#        36=$
     *  37=%       38=&    39='        40=(
     *  41=)       42=*    45=-        47=/
     *  58=:       59=;    60=<        62=<
     *  63=?       64=@    91=[        93=]
     *  94=^       95=_    123={       125=}
     */
    if( (evt.keyCode == 33 && evt.charCode == 33) || (evt.keyCode == 34 && evt.charCode == 34) || (evt.keyCode == 35 && evt.charCode == 35)
        || (evt.keyCode == 36 && evt.charCode == 36) || (evt.keyCode == 37 && evt.charCode == 37) || (evt.keyCode == 38 && evt.charCode == 38)
        || (evt.keyCode == 39 && evt.charCode == 39) || (evt.keyCode == 40 && evt.charCode == 40) || (evt.keyCode == 41 && evt.charCode == 41)
        || (evt.keyCode == 42 && evt.charCode == 42) || (evt.keyCode == 45 && evt.charCode == 45) || (evt.keyCode == 47 && evt.charCode == 47)
        || (evt.keyCode == 58 && evt.charCode == 58) || (evt.keyCode == 59 && evt.charCode == 59) || (evt.keyCode == 60 && evt.charCode == 60)
        || (evt.keyCode == 62 && evt.charCode == 62) || (evt.keyCode == 63 && evt.charCode == 63) || (evt.keyCode == 64 && evt.charCode == 64)
        || (evt.keyCode == 91 && evt.charCode == 93) || (evt.keyCode == 95 && evt.charCode == 95) || (evt.keyCode == 123 && evt.charCode == 123)
        || (evt.keyCode == 125 && evt.charCode == 125) ){
        return false;
    }

    /** ALLOW NUMBER **/
    /*  96-105=number(0-9) */
    /* ALLOWED SOME KEY (KEY CODE & CHAR CODE)
     *  8=backspace             9=tab               46=delete
     *  35(0)=end               36(0)=home          37(0)=left arrow
     *  38(0)=up arrow          39(0)=right arrow   40(0)=down arrow
     */
    if ( (evt.keyCode == 8 && evt.charCode == 0) || (evt.keyCode == 9 && evt.charCode == 0) || (evt.keyCode == 46 && evt.charCode ==0)
        || (evt.keyCode == 35 && evt.charCode == 0) || (evt.keyCode == 36 && evt.charCode == 0) || (evt.keyCode == 37 && evt.charCode == 0)
        || (evt.keyCode == 38 && evt.charCode == 0) || (evt.keyCode == 39 && evt.charCode == 0) || (evt.keyCode == 40 && evt.charCode == 0) ) {
        return true;
    }

    if (evt.keyCode > 95 && evt.keyCode < 106) {
        return true;
    } else {
        keychar = String.fromCharCode(nbr);
        validNums += String.fromCharCode(8);
        if (validNums.indexOf(keychar) < 0) {
            return false;
        }
        return true;
    }
}

function onKeyMoney(evt) {
    var validNums = '0123456789.,';
    var nbr = evt.keyCode ? evt.keyCode : evt.which;

    /** CHECK SPECIAL CHARACTER **/
    /*
     * SPECIAL CHARACTER (KEY CODE & CHAR CODE)
     *  33=!       34="    35=#        36=$
     *  37=%       38=&    39='        40=(
     *  41=)       42=*    45=-        47=/
     *  58=:       59=;    60=<        62=<
     *  63=?       64=@    91=[        93=]
     *  94=^       95=_    123={       125=}
     */
    if( (evt.keyCode == 33 && evt.charCode == 33) || (evt.keyCode == 34 && evt.charCode == 34) || (evt.keyCode == 35 && evt.charCode == 35)
        || (evt.keyCode == 36 && evt.charCode == 36) || (evt.keyCode == 37 && evt.charCode == 37) || (evt.keyCode == 38 && evt.charCode == 38)
        || (evt.keyCode == 39 && evt.charCode == 39) || (evt.keyCode == 40 && evt.charCode == 40) || (evt.keyCode == 41 && evt.charCode == 41)
        || (evt.keyCode == 42 && evt.charCode == 42) || (evt.keyCode == 45 && evt.charCode == 45) || (evt.keyCode == 47 && evt.charCode == 47)
        || (evt.keyCode == 58 && evt.charCode == 58) || (evt.keyCode == 59 && evt.charCode == 59) || (evt.keyCode == 60 && evt.charCode == 60)
        || (evt.keyCode == 62 && evt.charCode == 62) || (evt.keyCode == 63 && evt.charCode == 63) || (evt.keyCode == 64 && evt.charCode == 64)
        || (evt.keyCode == 91 && evt.charCode == 93) || (evt.keyCode == 95 && evt.charCode == 95) || (evt.keyCode == 123 && evt.charCode == 123)
        || (evt.keyCode == 125 && evt.charCode == 125) ){
        return false;
    }

    /** ALLOW NUMBER **/
    /*  96-105=number(0-9)
     *  44=comma    188=comma
     *  46=period   190=period
     */
    /* ALLOWED SOME KEY (KEY CODE & CHAR CODE)
     *  8=backspace             9=tab               46=delete
     *  35(0)=end               36(0)=home          37(0)=left arrow
     *  38(0)=up arrow          39(0)=right arrow   40(0)=down arrow
     */
    if ( (evt.keyCode == 8 && evt.charCode == 0) || (evt.keyCode == 9 && evt.charCode == 0) || (evt.keyCode == 46 && evt.charCode ==0)
        || (evt.keyCode == 35 && evt.charCode == 0) || (evt.keyCode == 36 && evt.charCode == 0) || (evt.keyCode == 37 && evt.charCode == 0)
        || (evt.keyCode == 38 && evt.charCode == 0) || (evt.keyCode == 39 && evt.charCode == 0) || (evt.keyCode == 40 && evt.charCode == 0) ) {
        return true;
    }

    if ( (evt.keyCode > 95 && evt.keyCode < 106) || evt.keyCode == 44 || evt.keyCode == 188 || evt.keyCode == 46 || evt.keyCode == 190) {
        return true;
    } else {
        keychar = String.fromCharCode(nbr);
        validNums += String.fromCharCode(8);
        if (validNums.indexOf(keychar) < 0) {
            return false;
        }
        return true;
    }
}

function onKeyAddress(evt) {
    var validNums = '0123456789/';
    var nbr = evt.keyCode ? evt.keyCode : evt.which;

    /*home and end || evt.keyCode == '35' || evt.keyCode == '36' */
    /*  96-105 = numkey 0-9
     8 = backspace
     9 = tab
     46 = delete
     191 = fwd slash
     */
    if ((evt.keyCode > 95 && evt.keyCode < 106) || evt.keyCode == '8' || evt.keyCode == '9' || evt.keyCode == '46' || evt.keyCode == 191) {
        return true;
    } else {
        keychar = String.fromCharCode(nbr);
        validNums += String.fromCharCode(8);
        if (validNums.indexOf(keychar) < 0) {
            return false;
        }
        return true;
    }
}

function onKeyAddressMoo(evt) {
    var validNums = '0123456789/-';
    var nbr = evt.keyCode ? evt.keyCode : evt.which;

    /* TO REMOVE SPECIAL CHARACTER
     33=!       34="    35=#        36=$
     37=%       38=&    39='        40=(
     41=)       42=*    45=-        47=/
     58=:       59=;    60=<        62=<
     63=?       64=@    91=[        93=]
     94=^       95=_    123={       125=}
    */

    if( (evt.keyCode > 32 && evt.keyCode < 43)
            || evt.keyCode == 45 || evt.keyCode == 47 ||
        (evt.keyCode > 57 && evt.keyCode < 61) || (evt.keyCode > 61 && evt.keyCode < 65) ||
        evt.keyCode == 91 || evt.keyCode == 93 || evt.keyCode == 94 || evt.keyCode == 95 || evt.keyCode == 123 || evt.keyCode == 125 ){
        return false;
    }

    /*
     96-105 = number 0-9
     8 = backspace
     9 = tab
     46 = delete
     191,111 = fwd slash
     189,109 = dash
     35(0) = end
     36(0) = home
     37(0) = left arrow
     38(0) = up arrow
     39(0) = right arrow
     40(0) = down arrow
    */
    if ((evt.keyCode > 95 && evt.keyCode < 106)
            || evt.keyCode == 8 || evt.keyCode == 9 || evt.keyCode == 46
            || evt.keyCode == 191 || evt.keyCode == 111 || evt.keyCode == 189 || evt.keyCode == 109
            || (evt.keyCode == 35 && evt.charCode == 0)
            || (evt.keyCode == 36 && evt.charCode == 0)
            || (evt.keyCode == 37 && evt.charCode == 0)
            || (evt.keyCode == 38 && evt.charCode == 0)
            || (evt.keyCode == 39 && evt.charCode == 0)
            || (evt.keyCode == 40 && evt.charCode == 0)) {
        return true;
    } else {
        keychar = String.fromCharCode(nbr);
        validNums += String.fromCharCode(8);
        if (validNums.indexOf(keychar) < 0) {
            return false;
        }
        return true;
    }
}

function onKeyName(evt){
    var nbr = evt.keyCode ? evt.keyCode : evt.which;

    /*
     33 = !, 34=", 35 = #, 36 = $, 37=%, 38=&, 39=', 40=(, 41=), 42=*, 45=-, 47=/,
     58=:, 59=;, 60=<, 62=<, 63=?, 64 = @,
     91=[, 93=], 94=^, 95=_,
     123={,  125=}
     */

    if( (evt.keyCode > 32 && evt.keyCode < 43) || evt.keyCode == 45 || evt.keyCode == 47 ||
        (evt.keyCode > 57 && evt.keyCode < 61) || (evt.keyCode > 61 && evt.keyCode < 65) ||
        evt.keyCode == 91 || evt.keyCode == 93 || evt.keyCode == 94 || evt.keyCode == 95 || evt.keyCode == 123 || evt.keyCode == 125 ){
        return false;
    }

    return true;
}

function onKeyText(evt){
    var nbr = evt.keyCode ? evt.keyCode : evt.which;

    /*
     33 = !, 34=", 35 = #, 36 = $, 37=%, 38=&, 39=', 40=(, 41=), 42=*, 45=-, 47=/,
     58=:, 59=;, 60=<, 62=<, 63=?, 64 = @,
     91=[, 93=], 94=^, 95=_,
     123={,  125=}
     */

    if( (evt.keyCode > 32 && evt.keyCode < 43) || evt.keyCode == 45 || evt.keyCode == 47 ||
            (evt.keyCode > 57 && evt.keyCode < 61) || (evt.keyCode > 61 && evt.keyCode < 65) ||
                evt.keyCode == 91 || evt.keyCode == 93 || evt.keyCode == 94 || evt.keyCode == 95 || evt.keyCode == 123 || evt.keyCode == 125 ){
        return false;
    }

    return true;
}

function hideWindowsScrollBar() {
    $("body").attr("style", "overflow-y: hidden");
}
function showWindowsScrollBar() {
    $("body").attr("style", "overflow-y: scroll");
}

function onCheckRightClick(event){
    if(event.button==2)
    {
        return false;
    }
}

/*function onCheckEnter(obj, event){
    if(event.keyCode == 13){
        $(obj).attr("disabled","disabled");
        return true;
    }
}*/

function handleManageUserDialogRequest(xhr, status, args) {
    if (args.functionComplete) {
        manageUserDlg.hide();
    }
}

function handlePrescreenCustomerInfoRequest(xhr, status, args) {
    if (args.functionComplete) {
        customerDlg.hide();
    }
}

function handlePrescreenFacilityRequest(xhr, status, args) {
    if (args.functionComplete) {
        facilityDlg.hide();
    }
}

function handlePrescreenBusinessInfoRequest(xhr, status, args) {
    if (args.functionComplete) {
        businessDlg.hide();
    }
}

function handlePrescreenProposeCreditRequest(xhr, status, args) {
    if (args.functionComplete) {
        proposeCollateralDlg.hide();
    }
}

function handlencbInfoRequest(xhr, status, args) {
    if (args.functionComplete) {
        ncbDlg.hide();
    }
}

function handleFullappBizProductRequest(xhr, status, args) {
    if (args.functionComplete) {
        bizProductViewDlg.hide();
    }
}

function handleFullappBizStakeHolderRequest(xhr, status, args) {

    if (args.functionComplete) {
        stakeholderViewDlg.hide();
    }
}

function handletcgInfoRequest(xhr, status, args) {
    if (args.functionComplete) {
        tcgDlg.hide();
    }
}

function handleBasicInfoAccountRequest(xhr, status, args) {
    if (args.functionComplete) {
        basicInfoAccountDlg.hide();
    }
}

function handleContactRecordRequest(xhr, status, args) {
    if (args.functionComplete) {
        contactRecordDetailViewDlg.hide();
    }
}

function handleAppraisalDetailRequest(xhr, status, args) {
    if(args.functionComplete){
        appraisalDetailViewDlg.hide();
    }
}

function handleAppraisalContactDetailRequest(xhr, status, args) {
    if(args.functionComplete){
        appraisalContactDetailViewDlg.hide();
    }
}

function handleCollateralDetailRequest(xhr, status, args) {
    if(args.functionComplete){
        appraisalComsViewDlg.hide();
    }
}

function testHandle(){
    return true;
}

function handleDialogRequest(xhr, status, args, widgetVarName) {
    if (args.functionComplete) {
        var name = widgetVarName;
        PF(name).hide();
    }
}

function handleDlgCreditProposeRequest(xhr, status, args) {
    if (args.functionComplete) {
        creditInfoDlg.hide();
    }
}


function handleConditionInfoRequest(xhr, status, args) {
    if (args.functionComplete) {
        conditionDlg.hide();
    }
}
