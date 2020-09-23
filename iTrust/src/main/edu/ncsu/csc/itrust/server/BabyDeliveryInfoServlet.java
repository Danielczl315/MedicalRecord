package edu.ncsu.csc.itrust.server;

import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.logger.TransactionLogger;
import edu.ncsu.csc.itrust.model.old.beans.BabyDeliveryInfoBean;
import edu.ncsu.csc.itrust.model.old.beans.PatientBean;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;
import edu.ncsu.csc.itrust.model.old.dao.mysql.BabyDeliveryInfoDAO;
import edu.ncsu.csc.itrust.model.old.dao.mysql.ChildBirthVisitDAO;
import edu.ncsu.csc.itrust.model.old.dao.mysql.ObstetricsInitDAO;
import edu.ncsu.csc.itrust.model.old.dao.mysql.PatientDAO;
import edu.ncsu.csc.itrust.model.old.enums.TransactionType;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name="BabyDeliveryInfoServlet", urlPatterns = "/auth/hcp/BabyDeliveryInfoServlet")
public class BabyDeliveryInfoServlet extends HttpServlet {
    private DAOFactory prodFactory;
    private BabyDeliveryInfoDAO babyDeliveryInfoDAO;
    private PatientDAO patientDAO;
    private ChildBirthVisitDAO childBirthVisitDAO;
    private ObstetricsInitDAO obstetricsInitDAO;

    private static final String RECORD_ID = "recordID";
    private static final String OB_RECORD_ID = "obRecordID";
    private static final String VISIT_ID = "visitID";
    private static final String HCP_ID = "hcpMID";
	private static final String PID = "patientMID";
    private static final String BIRTH_DATE = "birthDate";
    private static final String DELIVERY_TYPE = "deliveryType";
    private static final String GENDER = "sexType";
    private static final String ESTIMATED = "isEstimated";

    public BabyDeliveryInfoServlet() {
        prodFactory = DAOFactory.getProductionInstance();
        babyDeliveryInfoDAO = prodFactory.getBabyDeliveryInfoDAO();
        patientDAO = prodFactory.getPatientDAO();
        childBirthVisitDAO = prodFactory.getChildBirthVisitDAO();
        obstetricsInitDAO = prodFactory.getObstetricsInitDAO();
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if(request.getParameter("delete") != null) {
            Long recordID = Long.parseLong(request.getParameter(RECORD_ID));
            try {
                    babyDeliveryInfoDAO.deleteBayDeliveryInfo(recordID);
                    return;

            } catch (DBException e) {
                e.printStackTrace();
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            }
        }
        long visitID = Long.parseLong(request.getParameter(VISIT_ID));
        long recordID = Long.parseLong(request.getParameter(RECORD_ID));
        long obRecordID = Long.parseLong(request.getParameter(OB_RECORD_ID));
        long hcpID = Long.parseLong(request.getParameter(HCP_ID));
        long pid = Long.parseLong(request.getParameter(PID));
        String birthDate = request.getParameter(BIRTH_DATE);
        String deliveryType = request.getParameter(DELIVERY_TYPE);
        String gender = request.getParameter(GENDER);
        String isEstimated = request.getParameter(ESTIMATED);

        BabyDeliveryInfoBean record = new BabyDeliveryInfoBean();
        
        if (visitID == 0)
        {
        	try
        	{
        		record.setChildBirthVisitID(childBirthVisitDAO.getNextVisitID());
        	}
        	catch (DBException e)
        	{
                e.printStackTrace();
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            }
        }
        else
        {
        	record.setChildBirthVisitID(visitID);
        }
        
        record.setDateOfBirthStr(birthDate, "yyyy-MM-dd'T'HH:mm");
        record.setDeliveryMethodStr(deliveryType);
        record.setGenderStr(gender);
        record.setIsEstimatedStr(isEstimated);

        try {
            long mid = addNewPatient(record, obRecordID);
            record.setPatientMID(mid);
            if(recordID != -1) {
                // record needs to be edited
                record.setRecordID(recordID);
                babyDeliveryInfoDAO.editBabyDeliveryInfo(record);
            } else {
                // need to create a record
                babyDeliveryInfoDAO.addBabyDeliveryInfo(record);

				if (visitID == 0)
				{
					TransactionLogger.getInstance().logTransaction(TransactionType.CREATE_BABY_RECORD, hcpID, pid , "");
				}
				else
				{
					TransactionLogger.getInstance().logTransaction(TransactionType.A_BABY_IS_BORN, hcpID, pid , "");
				}
            }
            response.setContentType("text/json");
            PrintWriter pw = response.getWriter();
            pw.print(record.toString());

        } catch (DBException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    private long addNewPatient(BabyDeliveryInfoBean baby, long obRecordID) throws DBException{
        PatientBean patientBean = new PatientBean();
        long motherMID = obstetricsInitDAO.getObstetricsInitByID(obRecordID).getPatientMID();
        patientBean.setMID(patientDAO.addEmptyPatient());
        patientBean.setMotherMID(String.valueOf(motherMID));
        patientBean.setDateOfBirthStr(baby.getBirthDatePattern("MM/dd/yyyy"));
        patientBean.setGender(baby.getGender());
        //hcp ID is never used in PatientDAO for editPatient
        patientDAO.editPatient(patientBean,0);

        return patientBean.getMID();
    }

}
