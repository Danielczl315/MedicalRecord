<%@ page import="edu.ncsu.csc.itrust.action.ViewObstetricCareRecordsAction" %>
<%@ page import="edu.ncsu.csc.itrust.action.ChildBirthVisitAction" %>
<%@ page import="edu.ncsu.csc.itrust.model.old.beans.ObstetricsInitBean" %>
<%@ page import="edu.ncsu.csc.itrust.model.old.beans.ChildBirthVisitBean" %>
<%@ page import="edu.ncsu.csc.itrust.model.old.beans.BabyDeliveryInfoBean" %>
<%@ page import="java.util.List" %>
<%@ page import="edu.ncsu.csc.itrust.exception.ITrustException" %>
<%@ page import="edu.ncsu.csc.itrust.model.old.beans.PriorPregnancyBean" %>
<%@ page import ="edu.ncsu.csc.itrust.model.old.enums.DeliveryType" %>
<%@ page import ="edu.ncsu.csc.itrust.model.old.enums.BooleanType" %>
<%@ page import ="edu.ncsu.csc.itrust.model.old.enums.Gender" %>
<%@taglib uri="/WEB-INF/tags.tld" prefix="itrust"%>
<%@page errorPage="/auth/exceptionHandler.jsp"%>

<%@include file="/global.jsp" %>


<%
    pageTitle = "iTrust - Childbirth";
%>
<%@include file="/header.jsp" %>

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
        response.sendRedirect("/iTrust/auth/getPatientID.jsp?forward=hcp/viewChildBirthVisit.jsp");
        return;
    }

    long hcpId = (Long)session.getAttribute("loggedInMID");
    long pid = Long.parseLong(pidString);


    try{

        ViewObstetricCareRecordsAction action = new ViewObstetricCareRecordsAction(prodDAO,hcpId,pid);
        boolean isOBGYN = action.isOBGYN();
        List<ObstetricsInitBean> records = action.getListObstetricInitRecords();
        ChildBirthVisitAction cb_action = new ChildBirthVisitAction(prodDAO,hcpId,pid);
%>

<script type="text/javascript">

    function editVisit(id) {
        <% if (isOBGYN) { %>
        window.location.href = "/iTrust/auth/hcp/addChildBirthVisit.jsp?recordID=" + id;
        <% } else { %>
        alert("Only an OB/GYN can edit records.");
        <% } %>
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

                    <a href="/iTrust/auth/hcp/viewChildBirthVisit.jsp?recordID=<%=recordID%>" name = "<%=initDate%>" class="list-group-item">
                        <%=initDate%>
                    </a>
<%
                }
%>
                    
                </div>
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
                List<ChildBirthVisitBean> visits = cb_action.getChildBirthVisitRecordByInitID(recordId);
                ChildBirthVisitBean currentVisitRecord = new ChildBirthVisitBean();
                if (visits != null && visits.size() > 0) {
                    currentVisitRecord = visits.get(0);
                }

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
            <%
                if (currentVisitRecord.getHasDelivered() != BooleanType.True) {
            %>
            <!-- Button trigger modal -->
            <button class="btn btn-primary" name = "add_button" data-toggle="modal" data-target="#createChildBirthVisit">
                Create Visit
            </button>
            <%
                } else {
            %>
            <div class="panel panel-default">
                <div class="panel-body">

                    <div class="row">
                        <div class="col-sm-12">
                            <h3>Preferred Delivery Type: <%=currentVisitRecord.getPreferredDeliveryMethod().getName()%></h3>
                        </div>
                    </div>


                    <form id="drugForm">
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
                                <td> <%=currentVisitRecord.getPitocinDosage()%></td>
                            </tr>
                            <tr>
                                <td>Nitrous oxide</td>
                                <td> <%=currentVisitRecord.getNitrousOxideDosage()%></td>
                            </tr>
                            <tr>
                                <td>Pethidine</td>
                                <td> <%=currentVisitRecord.getPethidineDosage()%></td>
                            </tr>
                            <tr>
                                <td>Epidural anaesthesia</td>
                                <td> <%=currentVisitRecord.getEpiduralAnaesthesiaDosage()%></td>
                            </tr>
                            <tr>
                                <td>Magnesium sulfate</td>
                                <td> <%=currentVisitRecord.getMagnesiumSulfateDosage()%></td>
                            </tr>
                            <tr>
                                <td>RH immune globulin</td>
                                <td> <%=currentVisitRecord.getRhImmuneGlobulinDosage()%></td>
                            </tr>
                            </tbody>
                        </table>
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
                                <th>Gender</th>
                            </tr>
                            </thead>
                            <tbody>
                    <%
                            List<BabyDeliveryInfoBean> babies = cb_action.getBabyDeliveryInfoByVisitID(currentVisitRecord.getVisitID());
                            for (BabyDeliveryInfoBean baby : babies) {

                    %>
                            <tr>
                                <td><%=baby.getBirthDatePattern("MM/dd/yyyy HH:mm aa")%></td>
                                <td><%=baby.getDeliveryMethod().getName()%></td>
                                <td><%=baby.getGender().getName()%></td>
                            </tr>
                    <%
                            }
                    %>
                            </tbody>
                        </table>
                    </form>

                    <button class="btn btn-primary" name = "edit_button" onclick="editVisit(<%=id%>)">Edit Visit</button>
                </div>
            </div>


            <%
                }
            %>
            </div>
        </div>



<script type="text/javascript">
    $(document).ready(function(){
        $('#visitType').change(function(){
            var response = $(this).val();
            if (response == "false") { //is an emergency
                $('#preferredDeliveryType').removeClass('hidden');
            } else {
                $('#preferredDeliveryType').addClass('hidden');
            }
        });
    });
</script>

        <!-- Modal -->
        <div class="modal fade" id="createChildBirthVisit" tabindex="-1">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                        <h4 class="modal-title">Creating a Child Birth Visit</h4>
                    </div>
                    <form id="initForm" class="form-horizontal" action="/iTrust/auth/hcp/addChildBirthVisit.jsp">
                    <div class="modal-body">
                        <div class="table-title">
                            <div class="form-group">
                                <div class="col-sm-8">
                                    <h4>Is this child birth visit pre-scheduled?</h4>
                                </div>
                                <div class="col-sm-4">
                                    <select id="visitType" name="visitType" class="form-control">
                                        <option selected></option>
                                        <option value="true">Pre-scheduled</option>
                                        <option value="false">Emergency</option>
                                    </select>
                                </div>
                            </div>


                            <div class="form-group hidden" id="preferredDeliveryType">
                                <div class="col-sm-8">
                                    <h4>What is the preferred delivery type?</h4>
                                </div>
                                <div class="col-sm-4">
                                    <select name="preferredDeliveryType" class="form-control">
                                        <%
                                            for(DeliveryType types : DeliveryType.values()){
                                                String type = types.getName();
                                        %>
                                        <option value="<%=type%>"> <%=type%> </option>
                                        <%
                                            }
                                        %>
                                        <option value="alreadyDelivered">Already Delivered</option>
                                    </select>
                                </div>
                            </div>

                        </div>
                        <input type="hidden" name="recordID" value="<%=id%>">

                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
                        <button type="submit" class="btn btn-primary">Next</button>
                    </div>
                    </form>
                </div>
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


