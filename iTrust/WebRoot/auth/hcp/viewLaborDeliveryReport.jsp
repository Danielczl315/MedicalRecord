<%@ page import="edu.ncsu.csc.itrust.action.ViewObstetricCareRecordsAction" %>
<%@ page import="edu.ncsu.csc.itrust.model.old.beans.ObstetricsInitBean" %>
<%@ page import="java.util.List" %>
<%@ page import="edu.ncsu.csc.itrust.exception.ITrustException" %>
<%@ page import="edu.ncsu.csc.itrust.action.LaborDeliveryReportAction" %>
<%@ page import="edu.ncsu.csc.itrust.model.old.beans.PriorPregnancyBean" %>
<%@ page import="edu.ncsu.csc.itrust.model.old.beans.ObstetricsOfficeVisitBean" %>
<%@ page import="edu.ncsu.csc.itrust.model.old.beans.AllergyBean" %>
<%@page errorPage="/auth/exceptionHandler.jsp"%>

<!-- IMPORTS GO HERE -->

<%@include file="/global.jsp" %>

<%
    pageTitle = "iTrust - Labor and Delivery Report";
%>

<%@include file="/header.jsp" %>
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
        response.sendRedirect("/iTrust/auth/getPatientID.jsp?forward=hcp/viewLaborDeliveryReport.jsp");
        return;
    }

    long hcpId = (Long)session.getAttribute("loggedInMID");
    long pid = Long.parseLong(pidString);

    try {
        LaborDeliveryReportAction action = new LaborDeliveryReportAction(prodDAO, hcpId, pid);
        List<PriorPregnancyBean> pregnancies = action.getPriorPregnancies();
%>
        <%--Prior Pregnancies Table--%>
        <div class="table-wrapper">
            <div class="table-title">
                <h2>Prior Pregnancy Data</h2>
            </div>

            <table class="table table-bordered">
                <thead>
                <tr>
                    <th>Year of Conception</th>
                    <th>Weeks Pregnant</th>
                    <th>Delivery Type</th>
                </tr>
                </thead>
                <tbody>
                <%
                    for(PriorPregnancyBean record: pregnancies) {
                %>
                <tr>
                    <td><%=record.getYearOfConception()%></td>
                    <td><%=record.getWeeksDaysPregnant()%></td>
                    <td><%=record.getDeliveryType().getName()%></td>
                </tr>
                <%
                    }
                %>
                </tbody>
            </table>
        </div>
<%
        String edd = action.getEstimatedDeliveryDate();
%>
        <div class="table-wrapper">
            <div class="table-title">
                <h2>Estimated Due Date: <%=edd%></h2>
            </div>
        </div>
<%
        String bloodType = action.getBloodType();
%>
        <div class="table-wrapper">
            <div class="table-title">
                <h2>Blood Type: <%=bloodType%></h2>
            </div>
        </div>
<%
        List<ObstetricsOfficeVisitBean> visits = action.getAllObstetricsOfficeVisits();
%>
        <div class="table-wrapper">
            <div class="table-title">
                <h2>Office Visit Overview</h2>
            </div>

                <table class="table table-bordered">
<%
            for (ObstetricsOfficeVisitBean visit: visits) {
%>
                    <tbody>
                    <tr>
                        <th>Office Visit Date</th>
                        <td id="officeVisitDate"><%=visit.getVisitDateStr("MM/dd/yyyy")%></td>
                    </tr>
                    <tr>
                        <th>Time Pregnant</th>
                        <td id="weeksDaysPregnant"><%=visit.getWeeksDaysPregnant()%></td>
                    </tr>
                    <tr>
                        <th>Weight Gain</th>
                        <td id="weight"><%=visit.getWeight()%></td>
                    </tr>
                    <tr>
                        <th>Blood Pressure</th>
                        <td id="bloodPressure"><%=visit.getBloodPressure()%></td>
                    </tr>
                    <tr>
                        <th>Fetal Heart Rate</th>
                        <td id="fetalHeartRate"><%=visit.getFetalHeartRate()%></td>
                    </tr>
                    <tr>
                        <th>Number of Babies</th>
                        <td id="numBabies"><%=visit.getNumberOfBabies()%></td>
                    </tr>
                    <tr>
                        <th>Low Lying Placenta Observed</th>
                        <td id="lowLyingPlacenta"><%=visit.getLowLyingPlacentaObserved().getName()%></td>
                    </tr>

                    </tbody>
<%
            }
%>
                </table>
        </div>
<%--Pregnancy complication warning flags--%>
<%
        String rhFlag = action.hasRHFlag();
        String advancedMaternalAge = action.hasAdvancedMaternalAge();
        String lowLyingPlacenta = action.hasLyingPlacenta();
        String fetalHR = action.abnormalFetalHR();
        String multiples = action.mulitples();
        String weight = action.atypicalWeightChange();

%>
        <div class="table-wrapper">
            <div class="table-title">
                <h2>Pregnancy Complications</h2>
            </div>
            <table class="table table-bordered">
                <tbody>
                    <tr>
                        <th>RH-flag</th>
                        <td><%=rhFlag%></td>
                    </tr>
                    <tr>
                        <th>Advanced Maternal Age</th>
                        <td><%=advancedMaternalAge%></td>
                    </tr>
                    <tr>
                        <th>Low Lying Placenta Observed</th>
                        <td><%=lowLyingPlacenta%></td>
                    </tr>
                    <tr>
                        <th>Abnormal Fetal Heart Rate</th>
                        <td><%=fetalHR%></td>
                    </tr>
                    <tr>
                        <th>Multiples</th>
                        <td><%=multiples%></td>
                    </tr>
                    <tr>
                        <th>Atypical Weight Change</th>
                        <td><%=weight%></td>
                    </tr>
                </tbody>
            </table>
        </div>

<%--Allergies Table Data--%>
<%
        List<AllergyBean> allergies = action.getAllergies();
        if (allergies.size() == 0) {
%>
        <div class="table-wrapper">
            <div class="table-title">
                <h2>No Allergies Documented</h2>
            </div>
        </div>
<%
        } else {
%>
        <div class="table-wrapper">
            <div class="table-title">
                <h2>Allergies</h2>
            </div>
            <table class="table table-bordered">
                <thead>
                <tr>
                    <th>ND Code</th>
                    <th>Description</th>
                </tr>
                </thead>
                <tbody>
<%
                for (AllergyBean allergy: allergies) {
%>
                <tr>
                    <td><%=allergy.getNDCode()%></td>
                    <td><%=allergy.getDescription()%></td>
                </tr>
<%
                }
%>
                </tbody>
            </table>
        </div>
<%
    }
%>
<%
    } catch (ITrustException e){
        String err = e.getMessage();
%>
    <div class="alert alert-danger"> <%=err%> </div>
<%
    }
%>

<%@include file="/footer.jsp" %>