<%@ page import="edu.ncsu.csc.itrust.action.ViewObstetricCareRecordsAction" %>
<%@ page import="edu.ncsu.csc.itrust.model.old.beans.ObstetricsInitBean" %>
<%@ page import="java.util.List" %>
<%@ page import="edu.ncsu.csc.itrust.exception.ITrustException" %>
<%@ page import="edu.ncsu.csc.itrust.model.old.beans.PriorPregnancyBean" %>
<%@taglib uri="/WEB-INF/tags.tld" prefix="itrust"%>
<%@page errorPage="/auth/exceptionHandler.jsp"%>

<%@include file="/global.jsp" %>



<%
    pageTitle = "iTrust - View Obstetric Initialization Records";
%>
<%@include file="/header.jsp" %>
<style>
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
<%
    /* Require a Patient ID first */
    String pidString = (String) session.getAttribute("pid");
    if (pidString == null || pidString.equals("") || 1 > pidString.length()) {
        out.println("pidstring is null");
        response.sendRedirect("/iTrust/auth/getPatientID.jsp?forward=hcp/viewObstetricCareRecordsList.jsp");
        return;
    }

    long hcpId = (Long)session.getAttribute("loggedInMID");
    long pid = Long.parseLong(pidString);

    try{
        ViewObstetricCareRecordsAction action = new ViewObstetricCareRecordsAction(prodDAO,hcpId,pid);
        boolean isOBGYN = action.isOBGYN();
        List<ObstetricsInitBean> records = action.getListObstetricInitRecords();

    %>

<script type="text/javascript">

    function createRecord() {
        <% if (isOBGYN) { %>
        window.location.href = "/iTrust/auth/hcp/addObstetricCareRecord.jsp";
        <% } else { %>
        alert("Only an OB/GYN can add records.");
        <% } %>
    }
    function editRecord(id) {
        <% if (isOBGYN) { %>
        window.location.href = "/iTrust/auth/hcp/editObstetricCareRecord.jsp?recordID=" + id;
        <% } else { %>
        alert("Only an OB/GYN can edit records.");
        <% } %>
    }
    function deleteRecord() {
        <% if (isOBGYN) { %>
        var valid = confirm("Are you sure?");
        if(valid) {
            deleteObstetricInitRecord();
        }
        <% } else { %>
        alert("Only an OB/GYN can delete records.");
        <% } %>
    }

    function deleteObstetricInitRecord() {

        $.ajax({
            url:'/iTrust/auth/hcp/ObstetricRecordServlet',
            type:'post',
            data:$('#deleteObstetricInitRecord').serialize(),
            success: function() {
                alert("Successfully Deleted.");
                window.location.replace("/iTrust/auth/hcp/viewObstetricCareRecordsList.jsp");
            }
        });
    }
</script>

<div class="row">
    <div class="col-xs-3">
        <h4>Select by Creation Date</h4>

        <div class="list-group">
            <%
                for (ObstetricsInitBean record : records) {
                    String initDate = record.getInitDateMMDDYYYY();
                    String recordID = Long.toString(record.getRecordID());
            %>

            <a href="/iTrust/auth/hcp/viewObstetricCareRecordsList.jsp?recordID=<%=recordID%>" class="list-group-item">
                <%=initDate%>
            </a>
            <%
                }
            %>

        </div>
        <button class="btn btn-primary" name = "add_button" onclick="createRecord()">Create Record</button>
    </div>
    <div class="col-xs-9">
        <%
            if(request.getParameter("recordID") != null) {
                String id = request.getParameter("recordID");
                Long recordId = Long.parseLong(id);
                ObstetricsInitBean currentRecord = action.getObstetricInitRecord(recordId);
                String initDate = currentRecord.getInitDateMMDDYYYY();
                String lmpDate = currentRecord.getLastMenstrualPeriodMMDDYYYY();
                String estimatedDate = currentRecord.getEstimatedDueDateMMDDYYYY();
                String weeksPregnant = currentRecord.getWeeksDaysPregnant();

        %>
        <%--Start Obstetric Init Table--%>
        <div class="table-wrapper">
            <div class="table-title">
                <h2>Obstetric Initialization Data</h2>
            </div>
            <table class="table table-bordered">
                <thead>
                <tr>
                    <th>Creation Date</th>
                    <th>Last Menstrual Period</th>
                    <th>Estimated Due Date</th>
                    <th>Weeks Pregnant</th>
                </tr>
                </thead>
                <tbody>

                <tr>
                    <td><%=initDate%></td>
                    <td><%=lmpDate%></td>
                    <td><%=estimatedDate%></td>
                    <td><%=weeksPregnant%></td>
                </tr>

                </tbody>
            </table>

            <%
                List<PriorPregnancyBean> priorPregnancyRecords = action.getListPriorPregnancies(currentRecord.getLastMenstrualPeriod());
                if(priorPregnancyRecords.size() > 0) {
            %>
            <%--Start Prior Pregnancy Table--%>

            <div class="table-title">
                <h2>Prior Pregnancy Data</h2>
            </div>

            <table class="table table-bordered">
                <thead>
                <tr>
                    <th>Year of Conception</th>
                    <th>Weeks Pregnant</th>
                    <th>Hours In Labor</th>
                    <th>Weight Gain</th>
                    <th>Delivery Type</th>
                    <th>Multiplicity</th>
                </tr>
                </thead>
                <tbody>
                <%
                    for(PriorPregnancyBean record: priorPregnancyRecords) {
                        int year = record.getYearOfConception();
                        String weekDays = record.getWeeksDaysPregnant();
                        float hours = record.getHoursInLabor();
                        float weight = record.getWeightGain();
                        String delivery = record.getDeliveryType().toString();
                        int num = record.getMultiplicity();
                %>
                <tr>
                    <td><%=year%></td>
                    <td><%=weekDays%></td>
                    <td><%=hours%></td>
                    <td><%=weight%></td>
                    <td><%=delivery%></td>
                    <td><%=num%></td>
                </tr>
                <%
                    }
                %>
                </tbody>
            </table>

            <%
                }
            %>          </div>

        <button class="btn btn-primary" name = "edit_button" onclick="editRecord(<%=id%>)">Edit Record</button>
        <button class="btn btn-danger" name = "delete_button" onclick="deleteRecord()">Delete Record</button>

        <form id="deleteObstetricInitRecord">
            <input type="hidden" name="recordID" value=<%=id%>>
            <input type="hidden" name="delete" value="true">
        </form>

    </div>
</div>

    <%
        }
    } catch(ITrustException ex) {

        String error= ex.getMessage();
        %>
        <div class="alert alert-danger"><%=error%></div>
        <%
    }

    %>





<%@include file="/footer.jsp" %>


