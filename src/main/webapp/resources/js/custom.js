function gotoInbox(contextUrl){
    window.location = contextUrl;
}

function removeComma(obj){
    var val = obj.value;
    val = val.replace(/,/g,'');
    obj.value = val;
}

function formatNumber(obj){
    var val = obj.value;
    val = $.trim(val);
    if(val != ''){
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

function onKeyNumber(evt){
    var validNums = '0123456789';
    var nbr = evt.keyCode ? evt.keyCode : evt.which;

    /*home and end || evt.keyCode == '35' || evt.keyCode == '36' */
    /*  96-105 = numkey 0-9
        8 = backspace
        9 = tab
        46 = delete
     */
    if( (evt.keyCode > 95 && evt.keyCode < 106) || evt.keyCode == '8' || evt.keyCode == '9' || evt.keyCode == '46' ){
        return true;
    } else {
        keychar = String.fromCharCode(nbr);
        validNums  += String.fromCharCode(8);
        if (validNums.indexOf(keychar) < 0){
            return false;
        }
        return true;
    }
}

function onKeyMoney(evt){
    var validNums = '0123456789.,';
    var nbr = evt.keyCode ? evt.keyCode : evt.which;

    /*home and end || evt.keyCode == '35' || evt.keyCode == '36' */
    /*  96-105 = numkey 0-9
     8 = backspace
     9 = tab
     46 = delete
     188 = comma
     190 = period
     */
    if( (evt.keyCode > 95 && evt.keyCode < 106) || evt.keyCode == '8' || evt.keyCode == '9' || evt.keyCode == '46' || evt.keyCode == 188 || evt.keyCode == 190 ){
        return true;
    } else {
        keychar = String.fromCharCode(nbr);
        validNums  += String.fromCharCode(8);
        if (validNums.indexOf(keychar) < 0){
            return false;
        }
        return true;
    }
}

function onKeyAddress(evt){
    /*string = string.replace(/[&\/\\#,+()$~%.'":*?<>{}]/g,'_');*/
    var validNums = '0123456789/';
    var nbr = evt.keyCode ? evt.keyCode : evt.which;

    /*home and end || evt.keyCode == '35' || evt.keyCode == '36' */
    /*  96-105 = numkey 0-9
     8 = backspace
     9 = tab
     46 = delete
     191 = fwd slash
     */
    if( (evt.keyCode > 95 && evt.keyCode < 106) || evt.keyCode == '8' || evt.keyCode == '9' || evt.keyCode == '46' || evt.keyCode == 191 ){
        return true;
    } else {
        keychar = String.fromCharCode(nbr);
        validNums  += String.fromCharCode(8);
        if (validNums.indexOf(keychar) < 0){
            return false;
        }
        return true;
    }
}

function hideWindowsScrollBar(){
    $("body").attr("style","overflow-y: hidden");
}
function showWindowsScrollBar(){
    $("body").attr("style","overflow-y: scroll");
}

function handlePrescreenCustomerInfoRequest(xhr, status, args) {
    if(args.functionComplete){
        customerDlg.hide();
    }
}

function handlePrescreenFacilityRequest(xhr, status, args) {
    if(args.functionComplete){
        facilityDlg.hide();
    }
}

function handlePrescreenBusinessInfoRequest(xhr, status, args) {
    if(args.functionComplete){
        businessDlg.hide();
    }
}

function handlePrescreenProposeCreditRequest(xhr, status, args) {
    if(args.functionComplete){
        proposeCollateralDlg.hide();
    }
}

function handlencbInfoRequest(xhr, status, args) {
    if(args.functionComplete){
        ncbDlg.hide();
    }
}

function handleFullappBizProductRequest(xhr, status, args) {
    if(args.functionComplete){
        bizProductViewDlg.hide();
    }
}

function handleFullappBizStakeHolderRequest(xhr, status, args) {

    if(args.functionComplete){
        stakeholderViewDlg.hide();
    }
}

function handletcgInfoRequest(xhr, status, args) {
    if(args.functionComplete){
        tcgDlg.hide();
    }
}

function handleBasicInfoAccountRequest(xhr, status, args) {
    if(args.functionComplete){
        basicInfoAccountDlg.hide();
    }
}

function testHandle(){
    return true;
}