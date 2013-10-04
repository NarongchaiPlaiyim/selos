$(document).ready(function(){
    $(".numberonly").bind("keypress", function (e) {
        alert('5');
        return !(e.which != 8 && e.which != 0 &&
            (e.which < 48 || e.which > 57) && e.which != 46);
    });
    /*$(".numeric").numeric();
    $(".integer").numeric(false, function() { alert("Integers only"); this.value = ""; this.focus(); });
    $(".positive").numeric({ negative: false }, function() { alert("No negative values"); this.value = ""; this.focus(); });*/
});

$(document).ready(function() {
    $(".readonly").bind("onclick", function(e) {
        $(".readonly").blur();
    })
});

function numberOnly(e){
    return !(e.which != 8 && e.which != 0 &&
        (e.which < 48 || e.which > 57) && e.which != 46);
}

function removeWindowsScrollbar(){
    $("body").attr("style","overflow-y: hidden");
}
function showWindowsScrollbar(){
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