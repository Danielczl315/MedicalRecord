package edu.ncsu.csc.itrust.server;

import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.logger.TransactionLogger;
import edu.ncsu.csc.itrust.model.old.beans.UltrasoundRecordBean;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;
import edu.ncsu.csc.itrust.model.old.dao.mysql.UltrasoundRecordDAO;
import edu.ncsu.csc.itrust.model.old.enums.TransactionType;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name="UltrasoundServlet", urlPatterns = "/auth/hcp/UltrasoundServlet")
public class UltrasoundServlet extends HttpServlet {
	private static final String RECORD_ID = "recordID";
    private static final String VISIT_ID = "obOfficeVisitID";
    private static final String PATIENT_MID = "patientMID";
    private static final String HCP_MID = "hcpMID";
    private static final String CROWN_RUMP_LENGTH = "crownRumpLength";
    private static final String BIPARIETAL_DIAMETER = "bpDiameter";
    private static final String HEAD_CIRCUMFERENCE = "headCircumference";
    private static final String FEMUR_LENGTH = "femurLength";
    private static final String OCCIPITOFRONTAL_DIAMETER = "ofDiameter";
    private static final String ABDOMINAL_CIRCUMFERENCE = "abCircumference";
    private static final String HUMERUS_LENGTH = "humerusLength";
    private static final String ESTIMATED_FETAL_WEIGHT = "estFetalWeight";
    private static final String FILE_URL = "fileURL";
    
    private DAOFactory prodFactory;
    private UltrasoundRecordDAO usrDAO;
    
    public UltrasoundServlet() {
    	prodFactory = DAOFactory.getProductionInstance();
    	usrDAO = prodFactory.getUltrasoundRecordDAO();
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    	if (request.getParameter("delete") != null) {
    		long recordID = Long.parseLong(request.getParameter(RECORD_ID));
    		try {
    			usrDAO.deleteUltrasoundRecord(recordID);
    			return;
    		}
    		catch (DBException e) {
    			e.printStackTrace();
    			response.sendError(HttpServletResponse.SC_BAD_REQUEST);
    		}
    	}
    	long recordID = Long.parseLong(request.getParameter(RECORD_ID));
    	long obOfficeVisitID = Long.parseLong(request.getParameter(VISIT_ID));
        long patientMID = Long.parseLong(request.getParameter(PATIENT_MID));
        long hcpMID = Long.parseLong(request.getParameter(HCP_MID));
    	float crownRumpLength = Float.parseFloat(request.getParameter(CROWN_RUMP_LENGTH));
    	float bpDiameter = Float.parseFloat(request.getParameter(BIPARIETAL_DIAMETER));
    	float headCircumference = Float.parseFloat(request.getParameter(HEAD_CIRCUMFERENCE));
    	float femurLength = Float.parseFloat(request.getParameter(FEMUR_LENGTH));
    	float ofDiameter = Float.parseFloat(request.getParameter(OCCIPITOFRONTAL_DIAMETER));
    	float abCircumference = Float.parseFloat(request.getParameter(ABDOMINAL_CIRCUMFERENCE));
    	float humerusLength = Float.parseFloat(request.getParameter(HUMERUS_LENGTH));
    	float estFetalWeight = Float.parseFloat(request.getParameter(ESTIMATED_FETAL_WEIGHT));
    	String fileURL = request.getParameter(FILE_URL);

    	UltrasoundRecordBean usr = new UltrasoundRecordBean();
    	usr.setObstetricsOfficeVisitID(obOfficeVisitID);
    	usr.setCrownRumpLength(crownRumpLength);
    	usr.setBiparietalDiameter(bpDiameter);
    	usr.setHeadCircumference(headCircumference);
    	usr.setFemurLength(femurLength);
    	usr.setOccipitofrontalDiameter(ofDiameter);
    	usr.setAbdominalCircumference(abCircumference);
    	usr.setHumerusLength(humerusLength);
    	usr.setEstimatedFetalWeight(estFetalWeight);
    	usr.setFileURL(fileURL);

    	try {
    		if (recordID >= 0) {
    			usr.setRecordID(recordID);
    			usrDAO.editUltrasoundRecord(usr);
                TransactionLogger.getInstance().logTransaction(TransactionType.EDIT_ULTRASOUND_RECORD, hcpMID, patientMID , "");
    		}
    		else {
    			usrDAO.addUltrasoundRecord(usr);
    		}
    		response.setContentType("text/json");
    		PrintWriter pw = response.getWriter();
    		pw.print(usr.toString());
    	}
    	catch (DBException e) {
    		e.printStackTrace();
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
    	}
    }
}
