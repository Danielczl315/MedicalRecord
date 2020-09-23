<%@ page import="edu.ncsu.csc.itrust.model.old.enums.DeliveryType" %>
<%@ page import="edu.ncsu.csc.itrust.model.old.enums.Gender" %>
<%@ page import="edu.ncsu.csc.itrust.action.ChildBirthVisitAction" %>
<%@ page import="edu.ncsu.csc.itrust.model.old.beans.ChildBirthVisitBean" %>
<%@ page import="java.util.List" %>
<%@ page import="edu.ncsu.csc.itrust.model.old.enums.BooleanType" %>
<%@ page import="edu.ncsu.csc.itrust.model.old.beans.BabyDeliveryInfoBean" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Date" %>
<%@page errorPage="/auth/exceptionHandler.jsp"%>

<!-- IMPORTS GO HERE -->

<%@include file="/global.jsp" %>

<%
    pageTitle = "iTrust - Child Birth Visit";
%>

<%@include file="/header.jsp" %>
<%
    long hcpId = (Long)session.getAttribute("loggedInMID");
    String pidString = (String)session.getAttribute("pid");
    long pid = Long.parseLong(pidString);

    //isScheduled true or false
    String visitType = "";
    String preferredDeliveryType = "";
    String recordID = "";
    boolean isEmergency = false;
    String currentDateInput = new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + "T" + new SimpleDateFormat("HH:mm").format(new Date());
    // used as a temp variable to set the default option for select elements
    String selected = "";
    if (request.getParameter("visitType") != null) {
        visitType = request.getParameter("visitType");
    }
    if (request.getParameter("preferredDeliveryType") != null) {
        preferredDeliveryType = request.getParameter("preferredDeliveryType");
    }
    if (request.getParameter("recordID") != null) {
        recordID = request.getParameter("recordID");
        long obstetricRecordID = Long.parseLong(recordID);
        ChildBirthVisitAction action = new ChildBirthVisitAction(prodDAO,hcpId,pid);
        List<ChildBirthVisitBean> visits = action.getChildBirthVisitRecordByInitID(obstetricRecordID);
        ChildBirthVisitBean currentRecord;
        if (visits == null || visits.size() == 0) {
            //If no visits then create one otherwise, use first one, should only be one record
            currentRecord = new ChildBirthVisitBean();
            currentRecord.setObstetricsInitRecordID(obstetricRecordID);
        } else {
            currentRecord = visits.get(0);
        }

        if (visitType.equalsIgnoreCase("false")) {
            isEmergency = true;
            if (preferredDeliveryType.equalsIgnoreCase("alreadyDelivered")) {
                currentRecord.setPreferredDeliveryMethod(DeliveryType.VaginalDelivery);
            } else {
                currentRecord.setPreferredDeliveryMethodStr(preferredDeliveryType);
            }
        }
%>
<style type="text/css">
    body {
        color: #404E67;
        background: #F5F7FA;
    }
    .table-wrapper {
        width: auto;
        margin: 30px auto;
        background: #fff;
        padding: 20px;
        box-shadow: 0 1px 1px rgba(0,0,0,.05);
    }
    .table-title {
        padding-bottom: 10px;
        margin: 0 0 10px;
    }
    .table-title h2 {
        margin: 6px 0 0;
        font-size: 22px;
    }
    .table-title .add-new {
        float: right;
        height: 30px;
        font-weight: bold;
        font-size: 12px;
        text-shadow: none;
        min-width: 100px;
        border-radius: 50px;
        line-height: 13px;
    }
    .table-title .add-new i {
        margin-right: 4px;
    }
    table.table {
        table-layout: fixed;
    }
    table.table tr th, table.table tr td {
        border-color: #e9e9e9;
    }
    table.table th i {
        font-size: 13px;
        margin: 0 5px;
        cursor: pointer;
    }
    table.table th:last-child {
        width: 100px;
    }
    table.table td a {
        cursor: pointer;
        display: inline-block;
        margin: 0 5px;
        min-width: 24px;
    }
    table.table td a.save {
        color: #27C46B;
    }
    table.table td a.edit {
        color: #FFC107;
    }
    table.table td a.delete {
        color: #E34724;
    }
    table.table td i {
        font-size: 19px;
    }
    table.table td a.save i {
        font-size: 24px;
        margin-right: -1px;
        position: relative;
        top: 3px;
    }
    table.table .form-control {
        height: 32px;
        line-height: 32px;
        box-shadow: none;
        border-radius: 2px;
    }
    table.table .form-control.error {
        border-color: #f50000;
    }
<%
        if (currentRecord.getHasDelivered() == BooleanType.True) {
%>
    table.table td .save {
        display: none;
    }
<%
        } else {
%>
    table.table td .edit {
        display: none;
    }
<%
        }
%>
</style>
<script type="text/javascript">
    var isScheduled = '<%=visitType%>';
    var isEstimated = "false";
    function setupForm() {
        if (isScheduled == 'false' && '<%=preferredDeliveryType%>' == 'alreadyDelivered') {
            $('#drugForm').addClass('hidden');
            isEsitmated = "true";
        }
    }

    $(document).ready(function(){
        $('[data-toggle="tooltip"]').tooltip();
        setupForm();
        //save action cell from PP form
        var actions = '<a class="save" title="Save" data-toggle="tooltip"><i class="fa fa-check"></i></a>\n' +
            '                        <a class="edit" title="Edit" data-toggle="tooltip"><i class="material-icons">&#xE254;</i></a>\n' +
            '                        <a class="delete" title="Delete" data-toggle="tooltip"><i class="material-icons">&#xE872;</i></a>';

        // Add Row Baby
        $(".add-new").click(function(){
            var index = $("#babyForm table tbody tr:last-child").index();
            var row = '<tr>' +
                '<td class="birthDate"><input type="datetime-local" class="form-control" name="birthDate" value="<%=currentDateInput%>"></td>' +
                '<td class="deliveryType"><select class="form-control" name="deliveryType" > ' +
                <%
                for(DeliveryType types : DeliveryType.values()){
                    String type = types.getName();
                    selected = (type.equals(currentRecord.getPreferredDeliveryMethod().getName())) ? "selected" : "";
               %>
                '<option value="<%=type%>" <%=selected%>> <%=type%> </option>' +
                <%
                }
                %>
                '</select></td>' +
                '<td class="sexType"><select class="form-control" name="sexType" > ' +
                <%
                for(Gender types : Gender.values()){
                    String type = types.getName();
                    selected = (type.equals(Gender.NotSpecified.getName())) ? "selected":"";
               %>
                '<option value="<%=type%>" <%=selected%>> <%=type%> </option>' +
                <%
                }
                %>
                '</select></td>' +
                '<input class="recordID" type="hidden" name="recordID" value="-1">' +
                '<input type="hidden" class="birthDateInput" value="<%=currentDateInput%>">' +
                '<td>' + actions + '</td>' +
                '</tr>';
            $("#babyForm table").append(row);
            <%
            if (currentRecord.getHasDelivered() == BooleanType.True) {
            %>
            $("#babyForm table tbody tr").eq(index + 1).find(".save, .edit").toggle();
            <%
            }
            %>
            $('[data-toggle="tooltip"]').tooltip();
        });

        // Save Row Baby
        $('#babyForm').on("click", ".save", function(){
            var empty = false;
            var input = $(this).parents("tr").find('input');
            input.each(function(){
                if(!$(this).val()){
                    $(this).addClass("error");
                    empty = true;
                } else {
                    $(this).removeClass("error");
                }
            });

            $(this).parents("tr").find(".error").first().focus();
            if(!empty){
                var birthDate = $(this).parents("tr").find(".birthDate").first();
                var deliveryType = $(this).parents("tr").find(".deliveryType").first();
                var sexType = $(this).parents("tr").find(".sexType").first();
                var recordID = $(this).parents("tr").find(".recordID").first();
                var birthDateInput = $(this).parents("tr").find(".birthDateInput").first();
                var record = {
                    "birthDate":birthDate.find('input').val(),
                    "deliveryType":deliveryType.find('select').val(),
                    "sexType":sexType.find('select').val(),
                    "isEstimated":isEstimated,
                    "visitID":"<%=currentRecord.getVisitID()%>",
                    "obRecordID":"<%=obstetricRecordID%>",
                    "hcpMID":"<%=hcpId%>",
                    "patientMID":"<%=pidString%>",
                    "recordID":recordID.val()
                };
                submitBabyRecord(record, function(response) {
                    recordID.val(response.recordID);
                    birthDateInput.val(response.birthDateInput);
                    birthDate.html(response.birthDate);
                    deliveryType.html(response.deliveryType);
                    sexType.html(response.sexType);
                });

                $(this).parents("tr").find(".save, .edit").toggle();
            }
        });

        // Edit Row Baby
        $('#babyForm').on("click", ".edit", function(){
            var birthDate = $(this).parents("tr").find(".birthDate").first();
            var deliveryType = $(this).parents("tr").find(".deliveryType").first();
            var sexType = $(this).parents("tr").find(".sexType").first();
            var birthDateInput =  $(this).parents("tr").find(".birthDateInput").first().val();
            birthDate.html('<input type="datetime-local" class="form-control" name="birthDate" value="' + birthDateInput + '">');


            var currentDeliveryType = deliveryType.text();
            deliveryType.html('<select class="form-control" name="deliveryType" >' +
                <%
                for(DeliveryType types : DeliveryType.values()){
                    String type = types.getName();
               %>
                '<option value="<%=type%>"> <%=type%> </option>' +
                <%
                }
                %>
                '</select>');
            var currentSexType = sexType.text();

            sexType.html('<select class="form-control" name="sexType" >' +
                <%
                for(Gender types : Gender.values()){
                    String type = types.getName();
               %>
                '<option value="<%=type%>"> <%=type%> </option>' +
                <%
                }
                %>
                '</select>');

            $(this).parents("tr").find('select option[value="' + currentSexType + '"]').attr("selected",true);
            $(this).parents("tr").find('select option[value="' + currentDeliveryType + '"]').attr("selected",true);
            $(this).parents("tr").find(".save, .edit").toggle();

        });

        // Delete Row
        $('#babyForm').on("click", ".delete", function(){
            var id = $(this).parents("tr").find('.recordID').val();
            deleteBabyRecord(id);
            $(this).parents("tr").remove();
        });
    });

    function submitBabyRecord(record, successCallback) {

        $.ajax({
            url:'/iTrust/auth/hcp/BabyDeliveryInfoServlet',
            type:'post',
            data:record,
            dataType:'json',
            success: successCallback
        });
    }

    function deleteBabyRecord(recordID) {
        $.ajax({
            url:'/iTrust/auth/hcp/BabyDeliveryInfoServlet',
            type:'post',
            data:{
                "recordID":recordID,
                "delete":"true"
            },
            success: function() {
                alert("Successfully Deleted.");
            }
        });
    }

    function submitChildBirthVisit(successCallback) {
        $.ajax({
            url:'/iTrust/auth/hcp/ChildbirthVisitServlet',
            type:'post',
            data:$('#drugForm').serialize(),
            dataType:'json',
            success: successCallback
        });
    }

    function finishBtn() {
        submitChildBirthVisit(function (response) {
            window.location.href = "/iTrust/auth/hcp/viewChildBirthVisit.jsp?recordID=<%=obstetricRecordID%>";
        });
    }


</script>

<div class="panel panel-default">
    <div class="panel-body">

    <form id="drugForm">
        <div class="row" id="preferredDeliveryType">
            <%
                // allow field to be edited if they already delivered=OB/GYN is editing or it's never been set before
                if (currentRecord.getPreferredDeliveryMethod() == DeliveryType.NotSpecified ||
                        currentRecord.getHasDelivered() == BooleanType.True) {
            %>
            <div class="col-sm-8">
                <h4>What is the preferred delivery type?</h4>
            </div>
            <div class="col-sm-4">
                <select name="prefDeliveryMethod" class="form-control">
                    <%
                        for (DeliveryType types : DeliveryType.values()) {
                            String type = types.getName();
                            selected = (type.equals(currentRecord.getPreferredDeliveryMethod().getName())) ? "selected":"";
                    %>
                    <option value="<%=type%>" <%=selected%>> <%=type%> </option>
                    <%
                        }
                    %>
                </select>
            </div>
            <%
                } else {
            %>
            <div class="col-sm-12">
                <h3>Preferred Delivery Type: <%=currentRecord.getPreferredDeliveryMethod().getName()%></h3>
                <input type="hidden" class="prefDeliveryMethod" name="prefDeliveryMethod" value="<%=currentRecord.getPreferredDeliveryMethod().getName()%>">
            </div>
            <%
                }
            %>
        </div>



        <div class="table-title">
            <h3>Drugs Administered During Delivery</h3>
        </div>

        <table class="table table-bordered">
            <thead>
            <tr>
                <th>Drug</th>
                <th>Dosage</th>
            </tr>
            </thead>
            <tbody>
            <tr>
                <td>Pitocin</td>
                <td> <input type="number" class="form-control" name="pnd" id="pnd" value="<%=currentRecord.getPitocinDosage()%>"></td>
            </tr>
            <tr>
                <td>Nitrous oxide</td>
                <td> <input type="number" class="form-control" name="nod" id="nod" value="<%=currentRecord.getNitrousOxideDosage()%>"></td>
            </tr>
            <tr>
                <td>Pethidine</td>
                <td> <input type="number" class="form-control" name="ped" id="ped" value="<%=currentRecord.getPethidineDosage()%>"></td>
            </tr>
            <tr>
                <td>Epidural anaesthesia</td>
                <td> <input type="number" class="form-control" name="ead" id="ead" value="<%=currentRecord.getEpiduralAnaesthesiaDosage()%>"></td>
            </tr>
            <tr>
                <td>Magnesium sulfate</td>
                <td> <input type="number" class="form-control" name="msd" id="msd" value="<%=currentRecord.getMagnesiumSulfateDosage()%>"></td>
            </tr>
            <tr>
                <td>RH immune globulin</td>
                <td> <input type="number" class="form-control" name="rh" id="rh" value="<%=currentRecord.getRhImmuneGlobulinDosage()%>"></td>
            </tr>
            </tbody>
        </table>
        <input type="hidden" class="recordID" name="recordID" value="<%=currentRecord.getObstetricsInitRecordID()%>">
        <input type="hidden" class="patientMID" name="patientMID" value="<%=pidString%>">
        <input type="hidden" class="hcpMID" name="hcpMID" value="<%=hcpId%>">

    </form>

    <form id="babyForm">
        <div class="table-title">
            <h3>Baby Delivery</h3>
        </div>

        <table class="table table-bordered">
            <thead>
            <tr>
                <th>Birth Date</th>
                <th>Delivery Type</th>
                <th>Sex</th>
                <th>Actions</th>
            </tr>
            </thead>
            <tbody>
            <%
                // if they did not deliver already displays empty table with one row
                if (currentRecord.getHasDelivered() != BooleanType.True) {
            %>
            <tr>
                <td class="birthDate"><input type="datetime-local" class="form-control" name="birthDate" value="<%=currentDateInput%>"></td>
                <td class="deliveryType"><select class="form-control" name="deliveryType">
                <%
                    for(DeliveryType types : DeliveryType.values()){
                        String type = types.getName();
                        selected = (type.equals(currentRecord.getPreferredDeliveryMethod().getName())) ? "selected":"";

                %>
                <option value="<%=type%>" <%=selected%>> <%=type%> </option>
                <%
                    }
                %>
                </select></td>
                <td class="sexType"><select class="form-control" name="sexType">
                    <%
                        for(Gender types : Gender.values()){
                            String type = types.getName();
                            selected = (type.equals(Gender.NotSpecified.getName())) ? "selected":"";
                    %>
                    <option value="<%=type%>" <%=selected%>> <%=type%> </option>
                    <%
                        }
                    %>
                </select></td>
                <input type="hidden" class="recordID" name="recordID" value="-1">
                <input type="hidden" class="birthDateInput" value="<%=currentDateInput%>">
                <td>
                    <a class="save" title="Save" data-toggle="tooltip"><i class="fa fa-check"></i></a>
                    <a class="edit" title="Edit" data-toggle="tooltip"><i class="material-icons">&#xE254;</i></a>
                    <a class="delete" title="Delete" data-toggle="tooltip"><i class="material-icons">&#xE872;</i></a>
                </td>
            </tr>
                <%
                // other wise display the babies that were delivered so it can be edited
                } else {

                    List<BabyDeliveryInfoBean> babies = action.getBabyDeliveryInfoByVisitID(currentRecord.getVisitID());
                    for (BabyDeliveryInfoBean baby : babies) {
                        String currentBabyBirthDateInput = baby.getBirthDatePattern("yyyy-MM-dd") + "T" + baby.getBirthDatePattern("HH:mm");
                %>
                <tr>
                    <td class="birthDate"><%=baby.getBirthDatePattern("MM/dd/yyyy HH:mm aa")%></td>
                    <td class="deliveryType"><%=baby.getDeliveryMethod().getName()%></td>
                    <td class="sexType"><%=baby.getGender().getName()%></td>
                    <input type="hidden" class="recordID" name="recordID" value="<%=baby.getRecordID()%>">
                    <input type="hidden" class="birthDateInput" value="<%=currentBabyBirthDateInput%>">
                    <td>
                        <a class="save" title="Save" data-toggle="tooltip"><i class="fa fa-check"></i></a>
                        <a class="edit" title="Edit" data-toggle="tooltip"><i class="material-icons">&#xE254;</i></a>
                        <a class="delete" title="Delete" data-toggle="tooltip"><i class="material-icons">&#xE872;</i></a>
                    </td>
                </tr>
                <%
                    }
                }
                %>
            </tbody>
        </table>
        <div class="table-title">
            <button type="button" class="btn btn-info add-new"><i class="fa fa-plus"></i>Add a Baby</button>
        </div>
    </form>
        <button onclick="finishBtn()" type="button" class="btn btn-primary"> Finish </button>
    </div>
</div>
<%
    }
%>

<%@include file="/footer.jsp" %>
