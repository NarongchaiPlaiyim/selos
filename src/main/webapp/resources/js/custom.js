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
    if(readonly == true){
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

function checkAllowKeyNumber(keyCode){
    var validChar = '0123456789';
    var keyChar = String.fromCharCode(keyCode);
    validChar += String.fromCharCode(8);
    if (validChar.indexOf(keyChar) < 0) {
        return false;
    }
    return true;
}

function onKeyPressNumber(evt){
    var keyCode = evt.keyCode ? evt.keyCode : evt.which;

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

//    alert("keyPress : " + keyCode);

    if(keyCode == 33 || keyCode == 34 || keyCode == 35 || keyCode == 36 || keyCode == 37 || keyCode == 38 || keyCode == 39 || keyCode == 40 ||
        keyCode == 41 || keyCode == 42 || keyCode == 45 || keyCode == 47 || keyCode == 58 || keyCode == 59 || keyCode == 60 || keyCode == 62 ||
        keyCode == 63 || keyCode == 64 || keyCode == 91 || keyCode == 93 || keyCode == 123 || keyCode == 125){
        return false;
    }

    /** ALLOW NUMBER **/
    /*  96-105=number(0-9) */
    if (keyCode > 95 && keyCode < 106) {
        return true;
    } else {
        return checkAllowKeyNumber(keyCode);
    }
}

function onKeyDownNumber(evt){
    var keyCode = evt.keyCode ? evt.keyCode : evt.which;

//    alert("keyDown : " + keyCode);

    if(keyCode == 8 || keyCode == 9 || keyCode == 35 || keyCode == 36 || keyCode == 37 || keyCode == 38 || keyCode == 39 || keyCode == 40 || keyCode == 46){
        return true;
    }
    /** ALLOW NUMBER **/
    /*  96-105=number(0-9) */
    if (keyCode > 95 && keyCode < 106) {
        return true;
    } else {
        return checkAllowKeyNumber(keyCode);
    }
}

function checkAllowKeyMoney(keyCode){
    var validChar = '0123456789.,';
    var keyChar = String.fromCharCode(keyCode);
    validChar += String.fromCharCode(8);
    if (validChar.indexOf(keyChar) < 0) {
        return false;
    }
    return true;
}

function onKeyPressMoney(evt){
    var keyCode = evt.keyCode ? evt.keyCode : evt.which;

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

    if(keyCode == 33 || keyCode == 34 || keyCode == 35 || keyCode == 36 || keyCode == 37 || keyCode == 38 || keyCode == 39 || keyCode == 40 ||
        keyCode == 41 || keyCode == 42 || keyCode == 45 || keyCode == 47 || keyCode == 58 || keyCode == 59 || keyCode == 60 || keyCode == 62 ||
        keyCode == 63 || keyCode == 64 || keyCode == 91 || keyCode == 93 || keyCode == 123 || keyCode == 125){
        return false;
    }

    /** ALLOW NUMBER **/
    /*  96-105=number(0-9)
     *  44=comma    188=comma
     *  46=period   190=period
     */
    if ( (keyCode > 95 && keyCode < 106) || keyCode == 44 || keyCode == 188 || keyCode == 46 || keyCode == 190 ) {
        return true;
    } else {
        return checkAllowKeyMoney(keyCode);
    }
}

function onKeyDownMoney(evt){
    var keyCode = evt.keyCode ? evt.keyCode : evt.which;

    if(keyCode == 8 || keyCode == 9 || keyCode == 35 || keyCode == 36 || keyCode == 37 || keyCode == 38 || keyCode == 39 || keyCode == 40 || keyCode == 46 || keyCode == 144 || keyCode == 110){
        return true;
    }
    /** ALLOW NUMBER **/
    /*  96-105=number(0-9)
     *  44=comma    188=comma
     *  46=period   190=period
     */
    if ( (keyCode > 95 && keyCode < 106) || keyCode == 44 || keyCode == 188 || keyCode == 190 ) {
        return true;
    } else {
        return checkAllowKeyMoney(keyCode);
    }
}

function onKeyPressText(evt){
    var keyCode = evt.keyCode ? evt.keyCode : evt.which;

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

    if(keyCode == 33 || keyCode == 34 || keyCode == 35 || keyCode == 36 || keyCode == 37 || keyCode == 38 || keyCode == 39 || keyCode == 40 ||
        keyCode == 41 || keyCode == 42 || keyCode == 45 || keyCode == 47 || keyCode == 58 || keyCode == 59 || keyCode == 60 || keyCode == 62 ||
        keyCode == 63 || keyCode == 64 || keyCode == 91 || keyCode == 93 || keyCode == 94 || keyCode == 123 || keyCode == 125){
        return false;
    }

    return true;
}

function onKeyDownText(evt){
    var keyCode = evt.keyCode ? evt.keyCode : evt.which;

    if(keyCode == 33 || keyCode == 34 || keyCode == 35 || keyCode == 36 || keyCode == 37 || keyCode == 38 || keyCode == 39 || keyCode == 40 ||
        keyCode == 41 || keyCode == 42 || keyCode == 45 || keyCode == 47 || keyCode == 58 || keyCode == 59 || keyCode == 60 || keyCode == 62 ||
        keyCode == 63 || keyCode == 64 || keyCode == 91 || keyCode == 93 || keyCode == 94 || keyCode == 123 || keyCode == 125){
        return false;
    }

    return true;
}

function onKeyPressAddress(evt){
    var keyCode = evt.keyCode ? evt.keyCode : evt.which;

    /** CHECK SPECIAL CHARACTER **/
    /*
     * SPECIAL CHARACTER (KEY CODE & CHAR CODE)
     *  33=!       34="    35=#        36=$
     *  37=%       38=&    39='        40=(
     *  41=)       42=*
     *  58=:       59=;    60=<        62=<
     *  63=?       64=@    91=[        93=]
     *  94=^       95=_    123={       125=}
     */

    if(keyCode == 33 || keyCode == 34 || keyCode == 35 || keyCode == 36 || keyCode == 37 || keyCode == 38 || keyCode == 39 || keyCode == 40 ||
        keyCode == 41 || keyCode == 42 || keyCode == 58 || keyCode == 59 || keyCode == 60 || keyCode == 62 ||
        keyCode == 63 || keyCode == 64 || keyCode == 91 || keyCode == 93 || keyCode == 123 || keyCode == 125){
        return false;
    }

    return true;
}

function onKeyDownAddress(evt){
    var keyCode = evt.keyCode ? evt.keyCode : evt.which;

    if(keyCode == 8 || keyCode == 9 || keyCode == 35 || keyCode == 36 || keyCode == 37 || keyCode == 38 || keyCode == 39 || keyCode == 40 || keyCode == 46){
        return true;
    }

    return false;
}

function onKeyPressTelNo(evt){
    var keyCode = evt.keyCode ? evt.keyCode : evt.which;
    /** CHECK SPECIAL CHARACTER **/
    /*
     * SPECIAL CHARACTER (KEY CODE & CHAR CODE)
     *  33=!       34="    36=$
     *  37=%       38=&    39='
     *  40=(       41=)    42=*        47=/
     *  58=:       59=;    60=<        62=<
     *  63=?       64=@    91=[        93=]
     *  94=^       95=_    123={       125=}
     */

    if( keyCode == 33 || keyCode == 34 || keyCode == 36 || keyCode == 37 || keyCode == 38 || keyCode == 39 ||
        keyCode == 40 || keyCode == 41 || keyCode == 42 || keyCode == 47 || keyCode == 58 || keyCode == 59 ||
        keyCode == 60 || keyCode == 62 || keyCode == 63 || keyCode == 64 || keyCode == 91 || keyCode == 93 ||
        keyCode == 94 || keyCode == 95 || keyCode == 123 || keyCode == 125){
        return false;
    }

    return true;
}

function onKeyDownTelNo(evt){

    var keyCode = evt.keyCode ? evt.keyCode : evt.which;
    if(keyCode == 8 || keyCode == 9 || keyCode == 35 || keyCode == 36 || keyCode == 37 || keyCode == 38 || keyCode == 39 || keyCode == 40 || keyCode == 46 || keyCode == 144 || keyCode == 110){
        return true;
    }
    /** ALLOW NUMBER **/
    /*  96-105=number(0-9)
     *  35=# 45=-
     *  189=- 109=-
     */
    if ( (keyCode > 95 && keyCode < 106) || keyCode == 45 || keyCode == 35 || keyCode == 109 || keyCode == 189 ) {
        return true;
    } else {
        return checkAllowKeyTelNo(keyCode);
    }

    return false;
}

function checkAllowKeyTelNo(keyCode){
    var validChar = '0123456789#-';
    var keyChar = String.fromCharCode(keyCode);
    validChar += String.fromCharCode(8);
    if (validChar.indexOf(keyChar) < 0) {
        return false;
    }
    return true;
}


function onKeyPressHomeNo(evt){
    var keyCode = evt.keyCode ? evt.keyCode : evt.which;
    /** CHECK SPECIAL CHARACTER **/
    /*
     * SPECIAL CHARACTER (KEY CODE & CHAR CODE)
     *  33=!       34="    35=#        36=$
     *  37=%       38=&    39='
     *  40=(       41=)    42=*
     *  58=:       59=;    60=<        62=<
     *  63=?       64=@    91=[        93=]
     *  94=^       95=_    123={       125=}
     */

    if( keyCode == 33 || keyCode == 34 || keyCode == 36 || keyCode == 37 || keyCode == 38 || keyCode == 39 ||
        keyCode == 40 || keyCode == 41 || keyCode == 42 || keyCode == 35 || keyCode == 58 || keyCode == 59 ||
        keyCode == 60 || keyCode == 62 || keyCode == 63 || keyCode == 64 || keyCode == 91 || keyCode == 93 ||
        keyCode == 94 || keyCode == 95 || keyCode == 123 || keyCode == 125){
        return false;
    }

    return true;
}

function onKeyDownHomeNo(evt){

    var keyCode = evt.keyCode ? evt.keyCode : evt.which;
    if(keyCode == 8 || keyCode == 9 || keyCode == 36 || keyCode == 37 || keyCode == 38 || keyCode == 39 || keyCode == 40 || keyCode == 46 || keyCode == 144 || keyCode == 110){
        return true;
    }
    /** ALLOW NUMBER **/
    /*  96-105=number(0-9)
     *  45=-  47=/
     *  189=- 109=-
     *  111=/ 191=/
     */
    if ( (keyCode > 95 && keyCode < 106) || keyCode == 45 || keyCode == 47 || keyCode == 109 || keyCode == 189 || keyCode == 111 || keyCode == 191 ) {
        return true;
    } else {
        return checkAllowKeyHomeNo(keyCode);
    }

    return false;
}

function checkAllowKeyHomeNo(keyCode){
    var validChar = '0123456789/-';
    var keyChar = String.fromCharCode(keyCode);
    validChar += String.fromCharCode(8);
    if (validChar.indexOf(keyChar) < 0) {
        return false;
    }
    return true;
}




function hideWindowsScrollBar() {
    $("body").attr("style", "overflow-y: hidden");
    $('input').each(function() {
        var readonly = $(this).attr("readonly");
        if(readonly && readonly.toLowerCase()!=='false') { // this is readonly
            $(this).focus(function(){
                $(this).blur();
            });
        }
    });
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

function handleReturnMakerDialogRequest(xhr, status, args) {
    if (args.functionComplete) {
        returnMakerDlg.hide();
    }
}

function handleAssignCheckerDialogRequest(xhr, status, args) {
    if (args.functionComplete) {
        assignCheckerDlg.hide();
    }
}

function handleSubmitZMDialogRequest(xhr, status, args) {
    if (args.functionComplete) {
        submitZMDlg.hide();
    }
}

function handleAssignABDMDialogRequest(xhr, status, args) {
    if (args.functionComplete) {
        assignABDMDialog.hide();
    }
}

function handleDisbursementMcDialogRequest(xhr, status, args) {
    if (args.functionComplete) {
        mcDisbursementDialog.hide();
    }
}

function handleDisbursementDepositDialogRequest(xhr, status, args) {
    if (args.functionComplete) {
        depositDisbursementDialog.hide();
    }
}
function handleDisbursementBahtnetDialogRequest(xhr, status, args) {
    if (args.functionComplete) {
        bahtnetDisbursementDialog.hide();
    }
}

function handleBaPaAddDialogRequest(xhr, status, args) {
    if (args.functionComplete) {
        bapaInfoDialog.hide();
    }
}

function handleApplyBaDialogRequest(xhr, status, args) {
    if (args.functionComplete) {
        applyBaInfoDialog.hide();
    }
}

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

function handleAccountInfoRequest(xhr, status, args) {
    if (args.functionComplete) {
        accountInfoDlg.hide();
    }
}

function handleInsuranceInfoRequest(xhr, status, args) {
    if (args.functionComplete) {
        insuranceInfoDlg.hide();
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

function handleDialogOpen(xhr, status, args, widgetVarName) {
    if (args.functionComplete) {
        var name = widgetVarName;
        PF(name).show();
    }
}

function handleExistingCreditRequest(xhr, status, args) {
    if (args.functionComplete) {
        existingCreditDlg.hide();
    }
}

function handleExistingConditionRequest(xhr, status, args) {
    if (args.functionComplete) {
        conditionDlg.hide();
    }
}

// Credit Facility Existing Collateral Dialog
function handleExistingCollateralBorrowerRequest(xhr, status, args) {
    if (args.functionComplete) {
        existingCollateralBorrowerDlg.hide();
    }
}

// Credit Facility Existing Collateral Dialog
function handleExistingCollateralRelatedRequest(xhr, status, args) {
    if (args.functionComplete) {
        existingCollateralRelatedDlg.hide();
    }
}

function handleExistingGuarantorRequest(xhr, status, args) {
    if (args.functionComplete) {
        existingGuarantorDlg.hide();
    }
}

// Credit Facility Propose Credit Dialog
function handleDlgCreditProposeRequest(xhr, status, args) {
    if (args.functionComplete) {
        creditInfoDlg.hide();
    }
}

function handleTierDlgRequest(xhr, status, args) {
    if (args.functionComplete) {
        proposeTierDlg.hide();
    }
}

// Credit Facility Propose Collateral Dialog
function handleProposeCollateralDetailRequest(xhr, status, args) {
    if (args.functionComplete) {
        collateralInfoDlg.hide();
    }
}

// Credit Facility Propose Sub Collateral Dialog
function handleSubCollateralInfoRequest(xhr, status, args) {
    if (args.functionComplete) {
        subCollateralInfoDlg.hide();
    }
}

// Credit Facility Propose Guarantor Dialog
function handleGuarantorInfoRequest(xhr, status, args) {
    if (args.functionComplete) {
        guarantorInfoDlg.hide();
    }
}

function handleConditionInfoRequest(xhr, status, args) {
    if (args.functionComplete) {
        conditionDlg.hide();
    }
}

// Credit Facility Propose Credit Dialog
function onOneClick(buttonID, isDisable){
     document.getElementById(buttonID).disabled=isDisable;
}

function alphabetThaiEng(element){
    var alphabet = element.val();
    var alp_array = alphabet.split("");
    var isAlphabet = false;
    var pattern1 = /^[\u0E01-\u0E5B|A-Za-z|\s|-]+$/;
    var pattern2 = /^[๑-๙||฿|]+$/
    var char_at;
    if(alp_array.length > 0){
        for(var i=0;i<alp_array.length; i++){
            isAlphabet = false;
            char_at = alp_array[i];
            if(!pattern1.test(char_at)){
                isAlphabet = true;
            }else{
                if(pattern2.test(char_at)){
                    isAlphabet = true;
                }
            }
            if(isAlphabet){
                alp_array.splice(i, 1);
            }
        }
    }
    element.val(alp_array.join(""));
}

function onKeyPressAccountName(evt){
    var keyCode = evt.keyCode ? evt.keyCode : evt.which;

    /** CHECK SPECIAL CHARACTER **/
    /*
     * SPECIAL CHARACTER (KEY CODE & CHAR CODE)
     *  33=!       34="    35=#        36=$
     *  37=%       38=&    39='        40=(
     *  41=)       42=*    43=+        44=,
     *  58=:       59=;    60=<        61='='
     *  62=<       63=?    64=@        91=[
     *  93=]       92=|
     *  94=^       95=_    123={       125=}
     *  124=|      3665-3673 = 0-๙    3647=฿
     */

    if(keyCode == 33 || keyCode == 34 || keyCode == 35 || keyCode == 36 || keyCode == 37 || keyCode == 38 || keyCode == 39 || keyCode == 40 ||
        keyCode == 41 || keyCode == 42 || keyCode == 43 || keyCode == 44 || keyCode == 58 || keyCode == 59 || keyCode == 60 ||keyCode == 61 || keyCode == 62 || keyCode == 125 ||
        keyCode == 63 || keyCode == 64 || keyCode == 91 || keyCode == 92 || keyCode == 93 || keyCode == 94 || keyCode == 95 || keyCode == 123 || keyCode == 124 ||
        keyCode == 3647 ||(keyCode > 3664 && keyCode < 3674) || keyCode == 48 || (keyCode > 48 && keyCode < 58) ){
        return false;
    }

    return true;
}

function onKeyDownAccountName(evt){
    var keyCode = evt.keyCode ? evt.keyCode : evt.which;

    /** CHECK SPECIAL CHARACTER **/
    /*
     * SPECIAL CHARACTER (KEY CODE & CHAR CODE)
     {  16=Ctrl  9=Tab  35=End 36=Home 37=Left Arrow 38=Up Arrow 39=Right Arrow 40=Down Arrow
     *
     */

    if(keyCode == 16 || keyCode == 9 || keyCode == 35 || keyCode == 36 || keyCode == 37 || keyCode == 38 || keyCode == 39 || keyCode == 40){
        return false;
    }

    return true;
}