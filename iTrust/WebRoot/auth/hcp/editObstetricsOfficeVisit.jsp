<%@page import="edu.ncsu.csc.itrust.model.old.beans.ObstetricsInitBean"%>
<%@taglib uri="/WEB-INF/tags.tld" prefix="itrust"%>
<%@page errorPage="/auth/exceptionHandler.jsp"%>

<%@page import="java.util.List"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.PatientBean"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.ObstetricsInitBean" %>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.ObstetricsOfficeVisitBean"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.RemoteMonitoringDataBean"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.UltrasoundRecordBean" %>
<%@page import="edu.ncsu.csc.itrust.action.ViewMyRemoteMonitoringListAction"%>
<%@page import="edu.ncsu.csc.itrust.model.old.dao.mysql.PatientDAO" %>
<%@page import="edu.ncsu.csc.itrust.model.old.dao.mysql.UltrasoundRecordDAO" %>
<%@page import="edu.ncsu.csc.itrust.action.ViewObstetricOfficeVisitAction" %>
<%@page import="edu.ncsu.csc.itrust.model.old.enums.DeliveryType"%>
<%@ page import="edu.ncsu.csc.itrust.model.old.enums.BooleanType" %>
<%@ page import="java.util.ArrayList" %>
<%@include file="/global.jsp" %>

 
<%
pageTitle = "iTrust - Obstetric Office Visit";
%>

<%@include file="/header.jsp" %>

<%

     long hcpId = (Long)session.getAttribute("loggedInMID");
     String pidString = (String)session.getAttribute("pid");
     long pid = Long.parseLong(pidString);
     ViewObstetricOfficeVisitAction viewAction = new ViewObstetricOfficeVisitAction(prodDAO, hcpId, pid);
     ObstetricsOfficeVisitBean currentRecord;
     List<UltrasoundRecordBean> ultrasoundRecords;
     if (request.getParameter("visitID") != null) {
        long visitID = Long.parseLong(request.getParameter("visitID"));
        currentRecord = viewAction.getObstetricsOfficeVisitByID(visitID);
        ObstetricsInitBean obstetricsInitBean = viewAction.getObstetricsInitByVisitID(visitID);
        currentRecord.setLastMenstrualPeriodDate(obstetricsInitBean.getLastMenstrualPeriod());
        ultrasoundRecords = viewAction.getUltrasoundRecords(visitID);
     } else {
        currentRecord = viewAction.getNewOfficeVisit();
        currentRecord.setHcpMID(hcpId);
        currentRecord.setPatientMID(pid);
        ObstetricsInitBean obstetricsInitBean = viewAction.getRecentObstetricsInit();
        long obstetricRecordID = obstetricsInitBean.getRecordID();
        Date lmpDate = obstetricsInitBean.getLastMenstrualPeriod();
        currentRecord.setLastMenstrualPeriodDate(lmpDate);
        currentRecord.setObstetricsInitRecordID(obstetricRecordID);
        ultrasoundRecords = new ArrayList<>();
     }
%>

<%--Code below derived from Dyanmic Tables, see header.jsp for more information--%>
<style type="text/css">
     body {
          color: #404E67;
          background: #F5F7FA;
          font-family: 'Open Sans', sans-serif;
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
     .table-title .add-new  {
          float: right;
          height: 30px;
          font-weight: bold;
          font-size: 12px;
          text-shadow: none;
          min-width: 100px;
          border-radius: 50px;
          line-height: 13px;
     }
     #saveVisit {
         float: right;
         height: 30px;
         font-weight: bold;
         font-size: 12px;
         text-shadow: none;
         min-width: 100px;
         border-radius: 50px;
         line-height: 13px;
         display: none;
     }
     #editVisit{
         float: right;
         height: 30px;
         font-weight: bold;
         font-size: 12px;
         text-shadow: none;
         min-width: 100px;
         border-radius: 50px;
         line-height: 13px;
     }

     .ultrasound-edit, .ultrasound-save, .ultrasound-delete, .ultrasound-upload{
         height: 30px;
         font-weight: bold;
         font-size: 12px;
         text-shadow: none;
         min-width: 100px;
         border-radius: 50px;
         line-height: 13px;
     }
     .ultrasound-save {
         display: none;
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
     table.table td .save {
          display: none;
     }
</style>

<script type="text/javascript">
    $(document).ready(function(){

        // Add Row UR
        $(".add-new").click(function(){
            var newUltrasoundRecord =' <div class="ultrasound-record">\n' +
                '\n' +
                '\n' +
                '     <table class="table table-bordered">\n' +
                '\n' +
                '        <tbody>\n' +
                '            <tr>\n' +
                '                <th>Crown Rump Length (CRL)</th>\n' +
                '                <td class="crownRumpLength ultrasound-input">0</td>\n' +
                '                <td class="ultrasound-image" rowspan="7"></td>\n' +
                '            </tr>\n' +
                '            <tr>\n' +
                '                <th>Biparietal Diameter (BPD)</th>\n' +
                '                <td class="bpDiameter ultrasound-input">0</td>\n' +
                '            </tr>\n' +
                '            <tr>\n' +
                '                <th>Head Circumference (HC)</th>\n' +
                '                <td class="headCircumference ultrasound-input">0</td>\n' +
                '            </tr>\n' +
                '            <tr>\n' +
                '                <th>Femur Length (FL)</th>\n' +
                '                <td class="femurLength ultrasound-input">0</td>\n' +
                '            </tr>\n' +
                '            <tr>\n' +
                '                <th>Occipitofrontal Diameter (OFD)</th>\n' +
                '                <td class="ofDiameter ultrasound-input">0</td>\n' +
                '            </tr>\n' +
                '            <tr>\n' +
                '                <th>Abdominal Circumference (AC)</th>\n' +
                '                <td class="abCircumference ultrasound-input">0</td>\n' +
                '            </tr>\n' +
                '            <tr>\n' +
                '                <th>Humerus Length (HL)</th>\n' +
                '                <td class="humerusLength ultrasound-input">0</td>\n' +
                '            </tr>\n' +
                '            <tr>\n' +
                '                <th>Estimated Fetal Weight (EFW)</th>\n' +
                '                <td class="estFetalWeight ultrasound-input">0</td>\n' +
                '                <td class="ultrasound-upload-container"> Upload Image' +
                '                </td>\n' +
                '            </tr>\n' +
                '\n' +
                '            <tr>\n' +
                '                <td colspan="3">\n' +
                '                    <button class="btn btn-primary ultrasound-save">Save</button>\n' +
                '                    <button class="btn btn-primary ultrasound-edit">Edit</button>\n' +
                '                    <button class="btn btn-danger ultrasound-delete">Delete</button>\n' +
                '                </td>\n' +
                '\n' +
                '            </tr>\n' +
                '            <input type="hidden" class="recordID" value="-1">\n' +
                '            <input type="hidden" class="fileURL" value="-1">' +
                '            <input type="hidden" class="obOfficeVisitID" value="<%=currentRecord.getVisitID()%>">\n' +
                '            <input type="hidden" class="patientMID" value="<%=pidString%>">\n' +
                '            <input type="hidden" class="hcpMID" value="<%=hcpId%>">\n' +
                '        </tbody>\n' +
                '\n' +
                '\n' +
                '     </table>\n' +
                '    </div>';

            $("#ultrasoundForm").append(newUltrasoundRecord);
            var record = $("#ultrasoundForm").find(".ultrasound-record").last();
            record.find(".ultrasound-edit").click();
        });

        //Save Row UR
        $('#ultrasoundForm').on("click", ".ultrasound-save", function(){
            var empty = false;
            var input = $(this).parents("tbody").find('input').not('.fileInputControl');
            input.each(function(){
                if(!$(this).val()){
                    $(this).addClass("error");
                    empty = true;
                } else {
                    $(this).removeClass("error");
                }
            });

            $(this).parents("tbody").find(".error").first().focus();

            if(!empty) {
                var crownRumpLength = $(this).parents("tbody").find(".crownRumpLength").first();
                var bpDiameter = $(this).parents("tbody").find(".bpDiameter").first();
                var headCircumference = $(this).parents("tbody").find(".headCircumference").first();
                var femurLength = $(this).parents("tbody").find(".femurLength").first();
                var ofDiameter = $(this).parents("tbody").find(".ofDiameter").first();
                var abCircumference = $(this).parents("tbody").find(".abCircumference").first();
                var humerusLength = $(this).parents("tbody").find(".humerusLength").first();
                var estFetalWeight = $(this).parents("tbody").find(".estFetalWeight").first();
                var obOfficeVisitID = $(this).parents("tbody").find(".obOfficeVisitID").first();
                var recordID = $(this).parents("tbody").find(".recordID").first();
                var fileURL = $(this).parents("tbody").find(".fileURL").first();
                var patientMID = $(this).parents("tbody").find(".patientMID").first();
                var hcpMID = $(this).parents("tbody").find(".hcpMID").first();
                var recordToPost = {
                    "crownRumpLength":$(crownRumpLength).find('input').val(),
                    "bpDiameter":$(bpDiameter).find('input').val(),
                    "headCircumference":$(headCircumference).find('input').val(),
                    "femurLength":$(femurLength).find('input').val(),
                    "ofDiameter":$(ofDiameter).find('input').val(),
                    "abCircumference":$(abCircumference).find('input').val(),
                    "humerusLength":$(humerusLength).find('input').val(),
                    "estFetalWeight":$(estFetalWeight).find('input').val(),
                    "recordID":recordID.val(),
                    "obOfficeVisitID":obOfficeVisitID.val(),
                    "patientMID":patientMID.val(),
                    "hcpMID":hcpMID.val(),
                    "fileURL": fileURL.val()
                };
                submitUltrasoundRecord(recordToPost, function(data) {
                    var response = data;
                    crownRumpLength.html(response.crownRumpLength);
                    bpDiameter.html(response.bpDiameter);
                    headCircumference.html(response.headCircumference);
                    femurLength.html(response.femurLength);
                    ofDiameter.html(response.ofDiameter);
                    abCircumference.html(response.abCircumference);
                    humerusLength.html(response.humerusLength);
                    estFetalWeight.html(response.estFetalWeight);
                    //changes because the recordID should be -1 in case of new and should be changed for future edits
                    recordID.val(response.recordID);
                });

                $(this).parents("tbody").find(".ultrasound-save, .ultrasound-edit").toggle();
            }
        });


        // Edit Row UR
        $('#ultrasoundForm').on("click", ".ultrasound-edit", function(){
            //HTML cells
            var crownRumpLength = $(this).parents("tbody").find(".crownRumpLength").first();
            var bpDiameter = $(this).parents("tbody").find(".bpDiameter").first();
            var headCircumference = $(this).parents("tbody").find(".headCircumference").first();
            var femurLength = $(this).parents("tbody").find(".femurLength").first();
            var ofDiameter = $(this).parents("tbody").find(".ofDiameter").first();
            var abCircumference = $(this).parents("tbody").find(".abCircumference").first();
            var humerusLength = $(this).parents("tbody").find(".humerusLength").first();
            var estFetalWeight = $(this).parents("tbody").find(".estFetalWeight").first();
            var fileURL = $(this).parents("tbody").find(".ultrasound-upload-container").first();

            //set cells to inputs with current value
            crownRumpLength.html('<input type="number" class="form-control" name="crownRumpLength" value="' + crownRumpLength.text() + '">');
            bpDiameter.html('<input type="number" class="form-control" name="bpDiameter" value="' + bpDiameter.text() + '">');
            headCircumference.html('<input type="number" class="form-control" name="headCircumference" value="' + headCircumference.text() + '">');
            femurLength.html('<input type="number" class="form-control" name="femurLength" value="' + femurLength.text() + '">');
            ofDiameter.html('<input type="number" class="form-control" name="ofDiameter" value="' + ofDiameter.text() + '">');
            abCircumference.html('<input type="number" class="form-control" name="abCircumference" value="' + abCircumference.text() + '">');
            humerusLength.html('<input type="number" class="form-control" name="humerusLength" value="' + humerusLength.text() + '">');
            estFetalWeight.html('<input type="number" class="form-control" name="estFetalWeight" value="' + estFetalWeight.text() + '">');
            fileURL.html('<form class="us-img" action="/iTrust/auth/hcp/UploadImageServlet" method="post" enctype="multipart/form-data">\n' +
                '                <input class="fileInputControl" type="file" name="file" />\n' +
                '         </form>\n' +
                '                <button class="btn btn-default ultrasound-upload">Upload</button>');
            $(this).parents("tbody").find(".ultrasound-save, .ultrasound-edit").toggle();

        });

        // Delete Row UR
        $('#ultrasoundForm').on("click", ".ultrasound-delete", function(){
            var id = $(this).parents("tbody").find('.recordID').val();
            deleteUltrasoundRecord(id);
            $(this).parents(".ultrasound-record").remove();
        });


        //upload file
        $('#ultrasoundForm').on("click", ".ultrasound-upload", function(){
            var form = $('.us-img')[0];
            var imgElem = $(this).parents(".ultrasound-record").find('.ultrasound-image').first();
            var fileURLElem = $(this).parents(".ultrasound-record").find('.fileURL').first();

            uploadImage(form, function(response) {
                var name = response.name;
                imgElem.html('<img src="' + name + '" class="img-responsive">');
                fileURLElem.val(name);
            });
        });

        $("#editVisit").click();
    });


    function submitObstetricOfficeVisit(successCallback) {

        $.ajax({
            url:'/iTrust/auth/hcp/ObstetricOfficeVisitServlet',
            type:'post',
            data:$('#obstetricOfficeVisitForm').serialize(),
            dataType:'json',
            success: successCallback
        });
    }

    function submitUltrasoundRecord(record, successCallback) {

         $.ajax({
             url:'/iTrust/auth/hcp/UltrasoundServlet',
             type:'post',
             data:record,
             dataType:'json',
             success: successCallback
         });
     }

    function deleteUltrasoundRecord(recordID) {
         $.ajax({
             url:'/iTrust/auth/hcp/UltrasoundServlet',
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

    function editVisit() {
         var officeVisitDate = $('#officeVisitDate');
         var weight = $('#weight');
         var bloodPressure = $('#bloodPressure');
         var fetalHeartRate = $('#fetalHeartRate');
         var numBabies = $('#numBabies');
         var lowLyingPlacenta = $('#lowLyingPlacenta');
         var officeVisitDateInput = $('#visitDateInput');

         officeVisitDate.html('<input type="date" name="officeVisitDate" class="form-control" value="'+ officeVisitDateInput.val() + '">');
         weight.html('<input type="number" name="weight" class="form-control" step="0.1" value="'+ weight.text() + '">');
         bloodPressure.html('<input type="number" name="bloodPressure" class="form-control" value="'+ bloodPressure.text() + '">');
         fetalHeartRate.html('<input type="number" name="fetalHeartRate" class="form-control" value="'+ fetalHeartRate.text() + '">');
         numBabies.html('<input type="number" name="numBabies" class="form-control" value="'+ numBabies.text() + '">');

         var currentlySelected = lowLyingPlacenta.text();
         lowLyingPlacenta.html('<select class="form-control" name="lowLyingPlacenta">' +
             <%
             for(BooleanType types : BooleanType.values()){
                 String type = types.getName();
            %>
             '<option value=<%=type%>> <%=type%> </option>' +
             <%
             }
             %>
             '</select');

         $('#lowLyingPlacenta').find('select option[value="' + currentlySelected + '"]').attr("selected",true);

         $(document).find("#saveVisit, #editVisit").toggle();
     }

    function saveVisit() {
         var empty = false;

         var officeVisitDate = $('#officeVisitDate').find('input').first();
         var weight = $('#weight').find('input').first();
         var bloodPressure = $('#bloodPressure').find('input').first();
         var fetalHeartRate = $('#fetalHeartRate').find('input').first();
         var numBabies = $('#numBabies').find('input').first();
         var lowLyingPlacenta = $('#lowLyingPlacenta');
         var input = [officeVisitDate, weight, bloodPressure, fetalHeartRate, numBabies];
         var i;
         for(i = 0; i < input.length; i++) {
             currentInput = input[i];
             if(!currentInput.val()){
                 currentInput.addClass("error");
                 currentInput.focus();
                 empty = true;
             } else {
                 currentInput.removeClass("error");
             }
         }

         if(!empty){
             submitObstetricOfficeVisit(function(response) {
                 $('#visitID').val(response.visitID);
                 $('#officeVisitDate').html(response.officeVisitDate);
                 $('#visitDateInput').val(response.officeVisitDateInput);
                 $('#weeksDaysPregnant').html(response.weeksDaysPregnant);
                 $('#weight').html(response.weight);
                 $('#bloodPressure').html(response.bloodPressure);
                 $('#fetalHeartRate').html(response.fetalHeartRate);
                 $('#numBabies').html(response.numBabies);
                 $('#lowLyingPlacenta').html(response.lowLyingPlacenta);
             });

             $(document).find("#saveVisit, #editVisit").toggle();
         }
     }

     function uploadImage(form, successCallback) {
         var filedata = new FormData(form);
         $.ajax({
             url:'/iTrust/auth/hcp/UploadImageServlet',
             type:'post',
             enctype: 'multipart/form-data',
             data:filedata,
             processData: false,
             contentType: false,
             cache: false,
             timeout: 600000,
             dataType:'json',
             success: successCallback
         });
     }

    function checkIfCompleted() {
        var completed = true;
        //clicks all avaialble save buttons
        if($('#saveVisit').is(':visible')) {
            $('#saveVisit').click();
        }

        var input = $('#ultrasoundForm').find('.ultrasound-save');

        input.each(function(){
            if($(this).is(':visible')) {
                $(this).click();
            }
        });
        //if there are still clicks then some fields are empty and must be completed
        if($('#saveVisit').is(':visible')) {
            completed = false;
        }

        var input = $('#ultrasoundForm').find('.ultrasound-save');

        input.each(function(){
            if($(this).is(':visible')) {
                completed = false;
            }
        });

        if(completed) {
            window.location.href ="/iTrust/auth/hcp/viewObstetricOfficeVisitList.jsp?visitID=<%=currentRecord.getVisitID()%>";
        } else {
            alert("Please save your changes.");
        }

    }


</script>

<%--Table for Office Visit Overview--%>
<div class="table-wrapper">
     <div class="table-title">
         <div class="row">
            <div class="col-sm-8"> <h2>Office Visit Overview</h2></div>
            <div class="col-sm-4">
                <button id="saveVisit" class="btn btn-primary" onclick="saveVisit()">Save</button>
                <button id="editVisit" class="btn btn-primary" onclick="editVisit()">Edit</button>
            </div>
        </div>
     </div>

     <form id="obstetricOfficeVisitForm">
     <table class="table table-bordered">
          <tbody>
          <tr>
               <th>Office Visit Date</th>
               <td id="officeVisitDate"><%=currentRecord.getVisitDateStr("MM/dd/yyyy")%></td>
          </tr>
          <tr>
               <th>Time Pregnant</th>
               <td id="weeksDaysPregnant"><%=currentRecord.getWeeksDaysPregnant()%></td>
          </tr>
          <tr>
               <th>Weight Gain</th>
               <td id="weight"><%=currentRecord.getWeight()%></td>
          </tr>
          <tr>
               <th>Blood Pressure</th>
               <td id="bloodPressure"><%=currentRecord.getBloodPressure()%></td>
          </tr>
          <tr>
               <th>Fetal Heart Rate</th>
               <td id="fetalHeartRate"><%=currentRecord.getFetalHeartRate()%></td>
          </tr>
          <tr>
               <th>Number of Babies</th>
               <td id="numBabies"><%=currentRecord.getNumberOfBabies()%></td>
          </tr>
          <tr>
               <th>Low Lying Placenta Observed</th>
               <td id="lowLyingPlacenta"><%=currentRecord.getLowLyingPlacentaObserved().getName()%></td>
          </tr>

          </tbody>
          <input type="hidden" id="visitID" name="visitID" value="<%=currentRecord.getVisitID()%>">
          <input type="hidden" id="patientMID" name="patientMID" value="<%=pidString%>">
          <input type="hidden" id="hcpID" name="hcpID" value="<%=hcpId%>">
          <input type="hidden" id="obstetricRecordID" name="obstetricRecordID" value="<%=currentRecord.getObstetricsInitRecordID()%>">
          <input type="hidden" id="visitDateInput" value="<%=currentRecord.getVisitDateStr("yyyy-MM-dd")%>">
     </table>
     </form>
</div>

<%--Table for Ultrasound Records--%>
<div class="table-wrapper" id="ultrasoundForm">

     <div class="table-title">
          <div class="row">
               <div class="col-sm-8"><h2>Ultrasound Records</h2></div>
               <div class="col-sm-4">
                    <button type="button" class="btn btn-info add-new"><i class="fa fa-plus"></i> Add New</button>
               </div>
          </div>
     </div>

    <%
        for(UltrasoundRecordBean record: ultrasoundRecords) {
    %>
    <div class="ultrasound-record">

     <table class="table table-bordered">

        <tbody>
            <tr>
                <th>Crown Rump Length (CRL)</th>
                <td class="crownRumpLength ultrasound-input"><%=record.getCrownRumpLength()%></td>
                <td class="ultrasound-image" rowspan="7"><img src="<%= record.getFileURL()%>" alt="Ultrasound Image" class="img-responsive"></td>
            </tr>
            <tr>
                <th>Biparietal Diameter (BPD)</th>
                <td class="bpDiameter ultrasound-input"><%=record.getBiparietalDiameter()%></td>
            </tr>
            <tr>
                <th>Head Circumference (HC)</th>
                <td class="headCircumference ultrasound-input"><%=record.getHeadCircumference()%></td>
            </tr>
            <tr>
                <th>Femur Length (FL)</th>
                <td class="femurLength ultrasound-input"><%=record.getFemurLength()%></td>
            </tr>
            <tr>
                <th>Occipitofrontal Diameter (OFD)</th>
                <td class="ofDiameter ultrasound-input"><%=record.getOccipitofrontalDiameter()%></td>
            </tr>
            <tr>
                <th>Abdominal Circumference (AC)</th>
                <td class="abCircumference ultrasound-input"><%=record.getAbdominalCircumference()%></td>
            </tr>
            <tr>
                <th>Humerus Length (HL)</th>
                <td class="humerusLength ultrasound-input"><%=record.getHumerusLength()%></td>
            </tr>
            <tr>
                <th>Estimated Fetal Weight (EFW)</th>
                <td class="estFetalWeight ultrasound-input"><%=record.getEstimatedFetalWeight()%></td>
                <td class="ultrasound-upload-container">
                    Ultrasound Image
                </td>
            </tr>

            <tr>
                <td colspan="3">
                    <button class="btn btn-primary ultrasound-save">Save</button>
                    <button class="btn btn-primary ultrasound-edit">Edit</button>
                    <button class="btn btn-danger ultrasound-delete">Delete</button>
                </td>
            </tr>
            <input type="hidden" class="fileURL" value="<%=record.getFileURL()%>">
            <input type="hidden" class="recordID" value="<%=record.getRecordID()%>">
            <input type="hidden" class="obOfficeVisitID" value="<%=record.getObstetricsOfficeVisitID()%>">
            <input type="hidden" class="patientMID" value="<%=pidString%>">
            <input type="hidden" class="hcpMID" value="<%=hcpId%>">
        </tbody>



     </table>
    </div>
    <%
        }
    %>
</div>

<button class="btn btn-primary" onclick="checkIfCompleted()">Finish</button>

<%@include file="/footer.jsp" %>
