<%@ page import="edu.ncsu.csc.itrust.action.ViewObstetricCareRecordsAction" %>
<%@ page import="edu.ncsu.csc.itrust.model.old.beans.ObstetricsInitBean" %>
<%@ page import="edu.ncsu.csc.itrust.model.old.beans.PatientBean" %>
<%@ page import="java.util.List" %>
<%@ page import="edu.ncsu.csc.itrust.exception.ITrustException" %>
<%@ page import="edu.ncsu.csc.itrust.model.old.beans.PriorPregnancyBean" %>
<%@ page import="edu.ncsu.csc.itrust.action.AddBabyInfoAction" %>
<%@taglib uri="/WEB-INF/tags.tld" prefix="itrust"%>
<%@page errorPage="/auth/exceptionHandler.jsp"%>

<%@include file="/global.jsp" %>



<%
    pageTitle = "iTrust - Baby Initialization";
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
        response.sendRedirect("/iTrust/auth/getPatientID.jsp?forward=hcp/editBabyInitialization.jsp");
        return;
    }

    long hcpId = (Long)session.getAttribute("loggedInMID");
    long pid = Long.parseLong(pidString);

    String fatherMID = request.getParameter("fatherMID");
    String lastName = request.getParameter("lastName");

    try{
	    AddBabyInfoAction action = new AddBabyInfoAction(prodDAO, hcpId);
	    List<PatientBean> babies = action.getBabiesBymotherMID(Long.parseLong(pidString));
	
	    if (lastName != null) {
	        for (int i=0; i<babies.size(); i++) {
	            String firstName = request.getParameter(Integer.toString(i));
	            action.updateBabyInfo(babies.get(i).getMID(), Long.parseLong(pidString), firstName, lastName, fatherMID);
	        }
	    }
	
	    if (!babies.isEmpty()) {
%>

<div class="table-wrapper">
	<div class="table-title">
	    <h2>Baby Initialization Data</h2>
	</div>
	
	<form  id="babyInfo" method="post" action="editBabyInitialization.jsp">
	    <table class="table table-bordered">
	        <thead>
	        <tr>
	            <th>Father MID</th>
	            <th>Baby Last Name</th>
	            <th>Baby First Name</th>
	        </tr>
	        </thead>
	        <tbody>
	
	        <tr>
	            <td><input class="form-control" type="text" name="fatherMID"></td>
	            <td><input class="form-control" type="text" name="lastName"></td>
	            <%
	                for(int i=0; i<babies.size(); i++) {
	            %>
	            <td><input class="form-control" type="text" name=<%=Integer.toString(i) %>></td>
	            <%
	                }
	            %>
	        </tr>
	
	        </tbody>
	    </table>
	
	</form>
</div>

<script>
    //open the popup
    function popup() {

        $.ajax({
            url:'editBabyInitialization.jsp',
            type:'post',
            data:$('#babyInfo').serialize(),
            success: function() {
            	alert("Successfully initialized baby.");
                window.location.href = "/iTrust/auth/hcp/viewChildBirthVisit.jsp";
            }
        });
    }

</script>
<button class="btn btn-primary" name = "submit" onclick="popup()">Finish</button>

<% }
    else {
%>
    <div class="alert alert-danger">Please choose a patient with baby delivery information.</div>
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