<%@taglib uri="/WEB-INF/tags.tld" prefix="itrust"%>
<%@page errorPage="/auth/exceptionHandler.jsp"%>

<%@page import="java.util.List"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.RemoteMonitoringDataBean"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.PatientBean"%>
<%@page import="edu.ncsu.csc.itrust.action.ViewMyRemoteMonitoringListAction"%>
<%@page import="edu.ncsu.csc.itrust.model.old.dao.mysql.PatientDAO" %>
<%@page import="edu.ncsu.csc.itrust.model.old.dao.mysql.PriorPregnancyDAO" %>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.PriorPregnancyBean" %>
<%@page import="edu.ncsu.csc.itrust.action.ViewObstetricCareRecordsAction" %>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.ObstetricsInitBean"%>
<%@page import="edu.ncsu.csc.itrust.model.old.enums.DeliveryType"%>
<%@ page import="edu.ncsu.csc.itrust.model.old.enums.Obstetrics" %>
<%@include file="/global.jsp" %>

 
<%
pageTitle = "iTrust - Obstetric Record";
%>

<%@include file="/header.jsp" %>

<%
     String initDate = "";
     String lmpDate = "";
     String edDate = "";
     String weeksPregnant = "";

     //Used for the Date Picker HTML as current values
     String initDateInput = "";
     String lmpDateInput = "";
     String recordID = "-1";


     long hcpId = (Long)session.getAttribute("loggedInMID");
     String pidString = (String)session.getAttribute("pid");

     long pid = Long.parseLong(pidString);
     ViewObstetricCareRecordsAction viewAction;
     List<PriorPregnancyBean> priorPregnancyRecords;

     if (request.getParameter("recordID") != null) {
          recordID = request.getParameter("recordID");
          long id = Long.parseLong(recordID);
          viewAction = new ViewObstetricCareRecordsAction(prodDAO, hcpId, pid);
          ObstetricsInitBean currentRecord = viewAction.getObstetricInitRecord(id);

          initDate = currentRecord.getInitDateMMDDYYYY();
          lmpDate = currentRecord.getLastMenstrualPeriodMMDDYYYY();
          edDate = currentRecord.getEstimatedDueDateMMDDYYYY();
          weeksPregnant = currentRecord.getWeeksDaysPregnant();

          initDateInput = currentRecord.getInitDateYYYYMMDD();
          lmpDateInput = currentRecord.getLastMenstrualPeriodYYYYMMDD();

          priorPregnancyRecords = viewAction.getListPriorPregnancies(currentRecord.getLastMenstrualPeriod());
%>

<%--Code below derived from Dyanmic Tables, see header.jsp for more information--%>
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
     table.table td .save {
          display: none;
     }
</style>

<script type="text/javascript">
     var currentRecord = null;

    $(document).ready(function(){
        $('[data-toggle="tooltip"]').tooltip();

        //save action cell from PP form
        var actions = $("#priorPregnancyForm table td:last-child").html();

        // Add Row PP
        $(".add-new").click(function(){
            var index = $("#priorPregnancyForm table tbody tr:last-child").index();
            var row = '<tr>' +
                '<td class="yearOfConception"><input type="text" class="form-control" name="yearOfConception"></td>' +
                '<td class="daysPregnant"><input type="text" class="form-control" name="daysPregnant"></td>' +
                '<td class="hoursInLabor"><input type="text" class="form-control" name="hoursInLabor"></td>' +
                '<td class="weightGain"><input type="text" class="form-control" name="weightGain"></td>' +
                '<td class="deliveryType"><select class="form-control" name="deliveryType" > ' +
                    <%
                    for(DeliveryType types : DeliveryType.values()){
                        String type = types.getName();
                   %>
                    '<option value=<%=type%>> <%=type%> </option>' +
                    <%
                    }
                    %>
                '</select></td>' +
                '<td class="multiplicity"><input type="number" class="form-control" name="multiplicity"></td>' +
                '<input type="hidden" class="priorPregnancyID" value="-1">' +
                '<input type="hidden" class="obstetricRecordID" value="<%=id%>">' +
                '<td>' + actions + '</td>' +
                '</tr>';
            $("#priorPregnancyForm table").append(row);
            $("#priorPregnancyForm table tbody tr").eq(index + 1).find(".save, .edit").toggle();
            $('[data-toggle="tooltip"]').tooltip();
        });

        // Save Row OB
        $('#obstetricInitForm').on("click", ".save", function(){
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
                submitObstetricRecord(function(data) {
                    var response = data;
                    currentRecord = response;

                    var creationDate = $("#obstetricInitForm").find(".creationDate").first();
                    creationDate.html(response.initDate);

                    var lmpDate = $("#obstetricInitForm").find(".lmpDate").first();
                    lmpDate.html(response.lmpDate);

                    var edDate = $("#obstetricInitForm").find(".edDate").first();
                    edDate.html(response.edDate);

                    var weeksDaysPregnant = $("#obstetricInitForm").find(".weeksDaysPregnant").first();
                    weeksDaysPregnant.html(response.weeksDaysPregnant);
                });

                $(this).parents("tr").find(".save, .edit").toggle();
            }
        });

        //Save Row PP
        $('#priorPregnancyForm').on("click", ".save", function(){
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

            if(!empty) {
                var yearOfConception = $(this).parents("tr").find(".yearOfConception").first();
                var daysPregnant = $(this).parents("tr").find(".daysPregnant").first();
                var hoursInLabor = $(this).parents("tr").find(".hoursInLabor").first();
                var weightGain = $(this).parents("tr").find(".weightGain").first();
                var deliveryType = $(this).parents("tr").find(".deliveryType").first();
                var multiplicity = $(this).parents("tr").find(".multiplicity").first();
                var obstetricRecordID = $(this).parents("tr").find(".obstetricRecordID").first();
                var priorPregnancyID = $(this).parents("tr").find(".priorPregnancyID").first();
                var recordToPost = {
                    "yearOfConception":$(yearOfConception).find('input').val(),
                    "daysPregnant":$(daysPregnant).find('input').val(),
                    "hoursInLabor":$(hoursInLabor).find('input').val(),
                    "weightGain":$(weightGain).find('input').val(),
                    "deliveryType":$(deliveryType).find('select').val(),
                    "multiplicity":$(multiplicity).find('input').val(),
                    "recordID":priorPregnancyID.val(),
                    "obstetricRecordID":obstetricRecordID.val()
                };
                submitPriorPregnancyRecord(recordToPost, function(data) {
                    var response = data;
                    yearOfConception.html(response.yearOfConception);
                    daysPregnant.html(response.daysPregnant);
                    hoursInLabor.html(response.hoursInLabor);
                    weightGain.html(response.weightGain);
                    deliveryType.html(response.deliveryType);
                    multiplicity.html(response.multiplicity);
                    //changes because the recordID should be -1 in case of new and should be changed for future edits
                    priorPregnancyID.val(response.recordID);

                });

                $(this).parents("tr").find(".save, .edit").toggle();
            }
        });

        // Edit Row OB
        $('#obstetricInitForm').on("click", ".edit", function(){
            var creationDate = $(this).parents("tr").find(".creationDate").first();
            var lmpDate = $(this).parents("tr").find(".lmpDate").first();
            if (currentRecord == null) {
                creationDate.html('<input type="date" name="creationDate" class="form-control" value="<%=initDateInput%>">');
                lmpDate.html('<input type="date" name="lmpDate" class="form-control" value="<%=lmpDateInput%>">');
            } else {
                creationDate.html('<input type="date" name="creationDate" class="form-control" value="'+ currentRecord.initDateInput + '">');
                lmpDate.html('<input type="date" name="lmpDate" class="form-control" value="' + currentRecord.lmpDateInput + '">');
            }

            $(this).parents("tr").find(".save, .edit").toggle();

        });

        // Edit Row PP
        $('#priorPregnancyForm').on("click", ".edit", function(){
            //HTML cells
            var yearOfConception = $(this).parents("tr").find(".yearOfConception").first();
            var daysPregnant = $(this).parents("tr").find(".daysPregnant").first();
            var hoursInLabor = $(this).parents("tr").find(".hoursInLabor").first();
            var weightGain = $(this).parents("tr").find(".weightGain").first();
            var deliveryType = $(this).parents("tr").find(".deliveryType").first();
            var multiplicity = $(this).parents("tr").find(".multiplicity").first();

            //set cells to inputs with current value
            yearOfConception.html('<input type="number" class="form-control" name="yearOfConception" value="' + yearOfConception.text() + '">');
            daysPregnant.html('<input type="number" class="form-control" name="daysPregnant" value="' + daysPregnant.text() + '">');
            hoursInLabor.html('<input type="number" class="form-control" name="hoursInLabor" value="' + hoursInLabor.text() + '">');
            weightGain.html('<input type="number" class="form-control" name="weightGain" value="' + weightGain.text() + '">');
            multiplicity.html('<input type="number" class="form-control" name="multiplicity" value="' + multiplicity.text() + '">');

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

            $(this).parents("tr").find('select option[value="' + currentDeliveryType + '"]').attr("selected",true);
            $(this).parents("tr").find(".save, .edit").toggle();

        });

        // Delete Row
        $('#priorPregnancyForm').on("click", ".delete", function(){
            var id = $(this).parents("tr").find('.priorPregnancyID').val();
            deletePriorPregnancyRecord(id);
            $(this).parents("tr").remove();
        });
    });

    function submitObstetricRecord(successCallback) {

        $.ajax({
            url:'/iTrust/auth/hcp/ObstetricRecordServlet',
            type:'post',
            data:$('#obstetricInitForm').serialize(),
            dataType:'json',
            success: successCallback
        });
    }

     function submitPriorPregnancyRecord(record, successCallback) {

         $.ajax({
             url:'/iTrust/auth/hcp/PriorPregnancyServlet',
             type:'post',
             data:record,
             dataType:'json',
             success: successCallback
         });
     }

     function deletePriorPregnancyRecord(recordID) {
         $.ajax({
             url:'/iTrust/auth/hcp/PriorPregnancyServlet',
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

</script>



<div class="table-wrapper">
     <div class="table-title">
         <h2>Obstetric Initialization Data</h2>
     </div>

     <form id="obstetricInitForm">
     <table class="table table-bordered">
          <thead>
          <tr>
               <th>Creation Date</th>
               <th>Last Menstrual Period</th>
               <th>Estimated Due Date</th>
               <th>Weeks Pregnant</th>
               <th>Actions</th>
          </tr>
          </thead>
          <tbody>

          <tr>
               <td class="creationDate"><%=initDate%></td>
               <td class="lmpDate"><%=lmpDate%></td>
               <td class="edDate"><%=edDate%></td>
               <td class="weeksDaysPregnant"><%=weeksPregnant%></td>
               <td>
                    <a class="save" title="Save" data-toggle="tooltip"><i class="fa fa-check"></i></a>
                    <a class="edit" title="Edit" data-toggle="tooltip"><i class="material-icons">&#xE254;</i></a>
               </td>
          </tr>

          </tbody>
          <input type="hidden" name="recordID" value=<%=recordID%>>
          <input type="hidden" name="patientMID" value=<%=pidString%>>
     </table>
     </form>
</div>

<div class="table-wrapper">
     <div class="table-title">
          <div class="row">
               <div class="col-sm-8"><h2>Prior Pregnancy Data</h2></div>
               <div class="col-sm-4">
                    <button type="button" class="btn btn-info add-new"><i class="fa fa-plus"></i> Add New</button>
               </div>
          </div>
     </div>


     <form id="priorPregnancyForm">
     <table class="table table-bordered">
           <thead>
           <tr>
                <th>Year of Conception</th>
                <th>Days Pregnant</th>
                <th>Hours In Labor</th>
                <th>Weight Gain</th>
                <th>Delivery Type</th>
                <th>Multiplicity</th>
                <th>Actions</th>
           </tr>
           </thead>
          <tbody>

          <%
          if(priorPregnancyRecords.size() > 0) {
               for(PriorPregnancyBean record: priorPregnancyRecords) {
                    int year = record.getYearOfConception();
                    int days = record.getDaysPregnant();
                    float hours = record.getHoursInLabor();
                    float weight = record.getWeightGain();
                    String delivery = record.getDeliveryType().toString();
                    int num = record.getMultiplicity();
                    long obstetricRecordID = record.getObstetricRecordID();
                    long priorPregnancyID = record.getPriorPregnancyID();
          %>
          <tr>
               <td class="yearOfConception"><%=year%></td>
               <td class="daysPregnant"><%=days%></td>
               <td class="hoursInLabor"><%=hours%></td>
               <td class="weightGain"><%=weight%></td>
               <td class="deliveryType"><%=delivery%></td>
               <td class="multiplicity"><%=num%></td>
               <input type="hidden" class="priorPregnancyID" value="<%=priorPregnancyID%>">
               <input type="hidden" class="obstetricRecordID" value="<%=obstetricRecordID%>">
               <td>
                    <a class="save" title="Save" data-toggle="tooltip"><i class="fa fa-check"></i></a>
                    <a class="edit" title="Edit" data-toggle="tooltip"><i class="material-icons">&#xE254;</i></a>
                    <a class="delete" title="Delete" data-toggle="tooltip"><i class="material-icons">&#xE872;</i></a>
               </td>
          </tr>
<%
               }
          } else {
               %>
          <tr>
               <td class="yearOfConception"></td>
               <td class="daysPregnant"></td>
               <td class="hoursInLabor"></td>
               <td class="weightGain"></td>
               <td class="deliveryType"></td>
               <td class="multiplicity"></td>
               <input type="hidden" class="priorPregnancyID" value="-1">
               <input type="hidden" class="obstetricRecordID" value="<%=id%>">
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
     </form>
</div>

<a href="/iTrust/auth/hcp/viewObstetricCareRecordsList.jsp" class="btn btn-primary">
     Finish
</a>

<%@include file="/footer.jsp" %>
