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
    SimpleDateFormat formatInput = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat formatOutput = new SimpleDateFormat("MM/dd/yyyy");
    String initDate = formatOutput.format(new Date());
    String lmpDate = "";
    String edDate = "";
    String weeksPregnant = "";

    //Used for the Date Picker HTML as current values
    String initDateInput = formatInput.format(new Date());;
    String lmpDateInput = "";
    String recordID = "-1";
    
    long hcpId = (Long)session.getAttribute("loggedInMID");
    String pidString = (String)session.getAttribute("pid");
    String hcpIdString = Long.toString(hcpId);
    long pid = Long.parseLong(pidString);

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

    $(document).ready(function() {
        $('[data-toggle="tooltip"]').tooltip();

        // Save Row OB
        $('#obstetricInitForm').on("click", ".save", function () {
            var empty = false;
            var input = $(this).parents("tr").find('input');
            input.each(function () {
                if (!$(this).val()) {
                    $(this).addClass("error");
                    empty = true;
                } else {
                    $(this).removeClass("error");
                }
            });

            $(this).parents("tr").find(".error").first().focus();
            if (!empty) {
                submitObstetricRecord(function (data) {
                    var response = data;
                    var recordID = response.recordID;
                    window.location.replace('/iTrust/auth/hcp/editObstetricCareRecord.jsp?recordID=' + recordID);
                });
            }
        });


        // Edit Row OB
        $('#obstetricInitForm').on("click", ".edit", function () {
            var creationDate = $(this).parents("tr").find(".creationDate").first();
            var lmpDate = $(this).parents("tr").find(".lmpDate").first();

            creationDate.html('<input type="date" name="creationDate" class="form-control" value="<%=initDateInput%>">');
            lmpDate.html('<input type="date" name="lmpDate" class="form-control" value="<%=lmpDateInput%>">');


            $(this).parents("tr").find(".save, .edit").toggle();

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
            <input type="hidden" name="hcpMID" value=<%=hcpIdString%>>
        </table>
    </form>
</div>

<a href="/iTrust/auth/hcp/viewObstetricCareRecordsList.jsp" class="btn btn-danger">
    Cancel
</a>

<%@include file="/footer.jsp" %>
