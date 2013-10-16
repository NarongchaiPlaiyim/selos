$(document).ready(function(){
    $(".numberonly").bind("keypress", function (e) {
        alert('5');
        return !(e.which != 8 && e.which != 0 &&
            (e.which < 48 || e.which > 57) && e.which != 46);
    });
});

$(document).ready(function() {
    $(".readonly").bind("onclick", function(e) {
        $(".readonly").blur();
    })
});

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
    return !(e.which != 8 && e.which != 0 &&
        (e.which < 48 || e.which > 57) && e.which != 46);
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

    if(args.functionCalSum){
        alert("ผลรวมผิดพลาด ระบบจะคืนค่าเดิม");
        stakeholderViewDlg.hide();
    }
}

function handletcgInfoRequest(xhr, status, args) {
    if(args.functionComplete){
        tcgDlg.hide();
    }
}