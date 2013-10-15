/*$(document).ready(function(){
    $(".number-only").keypress(function(event) {
        var validNums = '0123456789';

        if (!event.charCode) k = String.fromCharCode(event.which);
        else k = String.fromCharCode(event.charCode);

        if (validNums.indexOf(k) == -1) event.preventDefault();
    });
});

$(document).ready(function() {
    $(".readonly").bind("onclick", function(e) {
        $(".readonly").blur();
    })
});*/

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

function numberOnly(e){
    var validNums = '0123456789.,';

    if (!event.charCode) k = String.fromCharCode(event.which);
    else k = String.fromCharCode(event.charCode);

    if (validNums.indexOf(k) == -1) event.preventDefault();
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

function handletcgInfoRequest(xhr, status, args) {
    if(args.functionComplete){
        tcgDlg.hide();
    }
}