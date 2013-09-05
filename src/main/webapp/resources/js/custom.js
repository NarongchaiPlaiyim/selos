/**
 * Created with IntelliJ IDEA.
 * User: l2ashies
 * Date: 8/27/13
 * Time: 6:12 PM
 * To change this template use File | Settings | File Templates.
 */
$(document).ready(function(){
    $(".numberonly").bind("keypress", function (e) {
        return !(e.which != 8 && e.which != 0 &&
            (e.which < 48 || e.which > 57) && e.which != 46);
    });
    $(".numeric").numeric();
    $(".integer").numeric(false, function() { alert("Integers only"); this.value = ""; this.focus(); });
    $(".positive").numeric({ negative: false }, function() { alert("No negative values"); this.value = ""; this.focus(); });
});

$(document).ready(function() {
    $(".readonly").bind("onclick", function(e) {
        $(".readonly").blur();
    })
});

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