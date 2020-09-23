package edu.ncsu.csc.itrust.server;

import edu.ncsu.csc.itrust.action.ViewObstetricOfficeVisitAction;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.ITrustException;
import edu.ncsu.csc.itrust.logger.TransactionLogger;
import edu.ncsu.csc.itrust.model.old.beans.ApptBean;
import edu.ncsu.csc.itrust.model.old.beans.ObstetricsOfficeVisitBean;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;
import edu.ncsu.csc.itrust.model.old.dao.mysql.ApptDAO;
import edu.ncsu.csc.itrust.model.old.dao.mysql.ObstetricsInitDAO;
import edu.ncsu.csc.itrust.model.old.dao.mysql.ObstetricsOfficeVisitDAO;
import edu.ncsu.csc.itrust.model.old.enums.TransactionType;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@WebServlet(name="ObstetricOfficeVisitServlet", urlPatterns = "/auth/hcp/ObstetricOfficeVisitServlet")
public class ObstetricOfficeVisitServlet extends HttpServlet {
	private static final String VISIT_ID = "visitID";
	private static final String OBSTETRIC_RECORD_ID = "obstetricRecordID";
	private static final String HCP_ID = "hcpID";
	private static final String PID = "patientMID";
    private static final String OFFICE_VISIT_DATE = "officeVisitDate";
    private static final String WEIGHT = "weight";
    private static final String BLOOD_PRESSURE = "bloodPressure";
    private static final String FETAL_HEART_RATE = "fetalHeartRate";
    private static final String NUMBER_OF_BABIES = "numBabies";
    private static final String LOW_LYING_PLACENTA = "lowLyingPlacenta";

    private DAOFactory prodFactory;
    private ObstetricsOfficeVisitDAO oovDAO;
    private ObstetricsInitDAO obstetricsInitDAO;
    private ApptDAO apptDAO;

    public ObstetricOfficeVisitServlet() {
        prodFactory = DAOFactory.getProductionInstance();
        oovDAO = prodFactory.getObstetricsOfficeVisitDAO();
        obstetricsInitDAO = prodFactory.getObstetricsInitDAO();
        apptDAO = prodFactory.getApptDAO();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        long visitID = Long.parseLong(request.getParameter(VISIT_ID));
        if(request.getParameter("delete") != null) {
            String delete = request.getParameter("delete");
            try {
                if(delete.equalsIgnoreCase("true")) {
                    oovDAO.deleteObstetricsOfficeVisit(visitID);
                    return;
                }
            } catch (DBException e) {
                e.printStackTrace();
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            }
        }

        long hcpID = Long.parseLong(request.getParameter(HCP_ID));
        long pid = Long.parseLong(request.getParameter(PID));
        long obstetricRecordID = Long.parseLong(request.getParameter(OBSTETRIC_RECORD_ID));
        String officeVisitDate = request.getParameter(OFFICE_VISIT_DATE);
        float weight = Float.parseFloat(request.getParameter(WEIGHT));
        int bloodPressure = Integer.parseInt(request.getParameter(BLOOD_PRESSURE));
        int fetalHeartRate = Integer.parseInt(request.getParameter(FETAL_HEART_RATE));
        int numBabies = Integer.parseInt(request.getParameter(NUMBER_OF_BABIES));
        String lowLyingPlacenta = request.getParameter(LOW_LYING_PLACENTA);

        ObstetricsOfficeVisitBean visit = new ObstetricsOfficeVisitBean();
        visit.setVisitID(visitID);


        try {
            if(visit.getVisitID() <= 0) {
                long newVisitID = oovDAO.addObstetricsOfficeVisit(visit);
                TransactionLogger.getInstance().logTransaction(TransactionType.CREATE_OBSTETRIC_OFFICE_VISIT, hcpID, pid , "");
                visit.setVisitID(newVisitID);
            } else {
                visit = oovDAO.getObstetricsOfficeVisitByID(visitID);
            }
            Date lmpDate = obstetricsInitDAO.getObstetricsInitByID(obstetricRecordID).getLastMenstrualPeriod();
            visit.setLastMenstrualPeriodDate(lmpDate);
            visit.setHcpMID(hcpID);
            visit.setPatientMID(pid);
            visit.setObstetricsInitRecordID(obstetricRecordID);
            visit.setVisitDateStr(officeVisitDate, "yyyy-MM-dd");
            visit.setWeight(weight);
            visit.setBloodPressure(bloodPressure);
            visit.setFetalHeartRate(fetalHeartRate);
            visit.setNumberOfBabies(numBabies);
            visit.setLowLyingPlacentaObservedStr(lowLyingPlacenta);


            List<ApptBean> apptBeans = apptDAO.getAppt((int) visit.getApptID());
            ApptBean apptBean = null;
            if (apptBeans != null && apptBeans.size() != 0) apptBean = apptBeans.get(0);
            if (apptBean == null) {
                ApptBean appt = new ApptBean();
                appt.setPatient(pid);
                appt.setHcp(hcpID);
                appt.setApptType("Obstetrics");
                appt.setDate(new Timestamp(new Date().getTime()));
                appt.setComment("Past Visit");
                long apptID = apptDAO.scheduleAppt(appt);
                visit.setApptID(apptID);
                apptBean = appt;
                TransactionLogger.getInstance().logTransaction(TransactionType.SCHEDULE_NEXT_OFFICE_VISIT, hcpID, pid, "");
                ViewObstetricOfficeVisitAction scheduleNextVisitAction = new ViewObstetricOfficeVisitAction(prodFactory, hcpID, pid);
                scheduleNextVisitAction.scheduleNextVisit(visit);
            }
            if (apptBean.getComment().equals("Upcoming Visit")) {
                apptBean.setComment("Past Visit");
                apptDAO.editAppt(apptBean);
                TransactionLogger.getInstance().logTransaction(TransactionType.SCHEDULE_NEXT_OFFICE_VISIT, hcpID, pid, "");
                ViewObstetricOfficeVisitAction scheduleNextVisitAction = new ViewObstetricOfficeVisitAction(prodFactory, hcpID, pid);
                scheduleNextVisitAction.scheduleNextVisit(visit);
            }


            oovDAO.editObstetricsOfficeVisit(visit);


            TransactionLogger.getInstance().logTransaction(TransactionType.EDIT_OBSTETRIC_OFFICE_VISIT, hcpID, pid , "");
            response.setContentType("text/json");
            PrintWriter pw = response.getWriter();
            pw.print(visit.toString());
        } catch (ITrustException | SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }

    }
}
