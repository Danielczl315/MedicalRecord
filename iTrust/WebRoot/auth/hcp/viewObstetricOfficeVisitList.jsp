<%@ page import="edu.ncsu.csc.itrust.action.ViewObstetricCareRecordsAction" %>
<%@ page import="edu.ncsu.csc.itrust.action.ViewObstetricOfficeVisitAction" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Date" %>
<%@ page import="edu.ncsu.csc.itrust.exception.ITrustException" %>
<%@ page import="edu.ncsu.csc.itrust.model.old.enums.BooleanType" %>
<%@ page import="edu.ncsu.csc.itrust.model.old.beans.*" %>
<%@ page import="java.util.Map" %>
<%@taglib uri="/WEB-INF/tags.tld" prefix="itrust"%>
<%@page errorPage="/auth/exceptionHandler.jsp"%>

<%@include file="/global.jsp" %>


<%
    pageTitle = "iTrust - View Obstetric Office Visits";
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
        response.sendRedirect("/iTrust/auth/getPatientID.jsp?forward=hcp/viewObstetricOfficeVisitList.jsp");
        return;
    }

    long hcpId = (Long)session.getAttribute("loggedInMID");
    long pid = Long.parseLong(pidString);

    try{
        ViewObstetricOfficeVisitAction action = new ViewObstetricOfficeVisitAction(prodDAO,hcpId,pid);
        boolean isOBGYN = action.isOBGYN();
        List<ObstetricsOfficeVisitBean> visits = action.getRecentVisits();

%>

<script type="text/javascript">

    function createVisit() {
        <% if (isOBGYN) { %>
        window.location.href="/iTrust/auth/hcp/editObstetricsOfficeVisit.jsp";
        <% } else { %>
        alert("Only an OB/GYN can add visits.");
        window.location.href="/iTrust/auth/hcp-uap/officeVisitInfo.xhtml";
        <% } %>
    }
    function editVisit(id) {
        <% if (isOBGYN) { %>
        window.location.href = "/iTrust/auth/hcp/editObstetricsOfficeVisit.jsp?visitID=" + id;
        <% } else { %>
        alert("Only an OB/GYN can edit visits.");
        window.location.href="/iTrust/auth/hcp-uap/officeVisitInfo.xhtml";
        <% } %>
    }
    function deleteVisit() {
        <% if (isOBGYN) { %>
        var valid = confirm("Are you sure?");
        if(valid) {
        	$.ajax({
                url:'/iTrust/auth/hcp/ObstetricOfficeVisitServlet',
                type:'post',
                data:$('#deleteVisit').serialize(),
                success: function() {
                    alert("Successfully Deleted.");
                    window.location.replace("/iTrust/auth/hcp/viewObstetricOfficeVisitList.jsp");
                }
            });
        }
        <% } else { %>
        alert("Only an OB/GYN can delete visits.");
        <% } %>
    }

</script>

<div class="row">
    <div class="col-xs-3">
        <h4>Select a visit by Visit Date</h4>

        <div class="list-group">
            <%
                Map<Long, String> labels = action.getVisitLabelsMap();
                for (ObstetricsOfficeVisitBean visit : visits) {
                    String visitDate = visit.getVisitDateStr();
                    String label = labels.get(visit.getVisitID());
                    String visitID = Long.toString(visit.getVisitID());
		            %>
		                <a href="/iTrust/auth/hcp/viewObstetricOfficeVisitList.jsp?visitID=<%=visitID%>" class="list-group-item">
		                    <%=visitDate + " " + label%>
		                </a>
		            <%
                    }
		            %>
        </div>
        <button class="btn btn-primary" name = "add_button" onclick="createVisit()">Create Visit</button>
    </div>
    <div class="col-xs-9">
        <%
            if(request.getParameter("visitID") != null) {
                long visitID = Long.parseLong(request.getParameter("visitID"));
                ObstetricsOfficeVisitBean currentRecord = action.getObstetricsOfficeVisitByID(visitID);
                List<UltrasoundRecordBean> ultrasoundRecords = action.getUltrasoundRecords(visitID);
                currentRecord.setLastMenstrualPeriodDate(action.getRecentObstetricsInit().getLastMenstrualPeriod());

              if (action.checkRHNotice(pid, currentRecord.getVisitDate()))
              {
            	  %>
            	  <div class="alert alert-danger">Patient should be given an RH immune globulin shot if they have not already.</div>
            	  <%
              }
                String link = action.getGoogleLink(currentRecord.getVisitDate());
        %>
        <%--Table for Office Visit Overview--%>
        <div class="table-wrapper">
            <div class="table-title">
                <div class="row">
                    <div class="col-sm-8"><h2>Office Visit Overview</h2></div>
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
                </table>
            </form>
        </div>

        <%--Table for Ultrasound Records--%>
        <div class="table-wrapper" id="ultrasoundForm">

            <div class="table-title">
                <div class="row">
                    <div class="col-sm-8"><h2>Ultrasound Records</h2></div>
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

                    </tbody>



                </table>
            </div>
<%
            }
%>
        </div>

        <button class="btn btn-primary" name = "edit_button" onclick="editVisit(<%=currentRecord.getVisitID()%>)">Edit Visit</button>
        <button class="btn btn-danger" name = "delete_button" onclick="deleteVisit()">Delete Visit</button>
        <%
            if (labels.get(currentRecord.getVisitID()).equals("Upcoming Visit")) {
        %>
        <a href="<%=link%>" class="btn btn-default">Add to Calendar</a>
        <%
            }
        %>
        <form id="deleteVisit">
            <input type="hidden" name="visitID" value=<%=currentRecord.getVisitID()%>>
            <input type="hidden" name="delete" value="true">
        </form>
        <%
            }
        %>

    </div>
</div>

<%
} catch(ITrustException ex){

    String error= ex.getMessage();
%>
<div class="alert alert-danger"><%=error%></div>
<%
    }
%>

<%@include file="/footer.jsp" %>