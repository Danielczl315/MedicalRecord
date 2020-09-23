package edu.ncsu.csc.itrust.server;

import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.logger.TransactionLogger;
import edu.ncsu.csc.itrust.model.old.beans.ObstetricsInitBean;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;
import edu.ncsu.csc.itrust.model.old.dao.mysql.ObstetricsInitDAO;
import edu.ncsu.csc.itrust.model.old.enums.TransactionType;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;


public class ObstetricRecordServlet extends HttpServlet {
    private static final String CREATION_DATE= "creationDate";
    private static final String LAST_MENSTRUAL_PERIOD_DATE = "lmpDate";
    private static final String PATIENT_MID = "patientMID";
    private static final String HCP_MID = "hcpMID";
    private static final String RECORD_ID = "recordID";
    private static final long serialVersionUID = 1L;

    private DAOFactory prodFactory;
    private ObstetricsInitDAO obstetricsInitDAO;

    public ObstetricRecordServlet() {
        prodFactory = DAOFactory.getProductionInstance();
        obstetricsInitDAO = prodFactory.getObstetricsInitDAO();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if(request.getParameter("delete") != null) {
            String delete = request.getParameter("delete");
            Long recordID = Long.parseLong(request.getParameter(RECORD_ID));
            try {
                if(delete.equalsIgnoreCase("true")) {
                    obstetricsInitDAO.deleteObstetricInit(recordID);
                    return;
                }
            } catch (DBException e) {
                e.printStackTrace();
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            }
        }

        String creationDate = request.getParameter(CREATION_DATE);
        String lmpDate = request.getParameter(LAST_MENSTRUAL_PERIOD_DATE);
        Long patientMID = Long.parseLong(request.getParameter(PATIENT_MID));
        Long hcpMID = Long.parseLong(request.getParameter(HCP_MID));

        ObstetricsInitBean record = new ObstetricsInitBean();
        record.setPatientMID(patientMID);
        record.setInitDate(creationDate, "yyyy-MM-dd");
        record.setLastMenstrualPeriod(lmpDate, "yyyy-MM-dd");

        Long recordID = Long.parseLong(request.getParameter(RECORD_ID));


        try {
            if(recordID >= 0) {
                record.setRecordID(recordID);
                obstetricsInitDAO.editObstetricsInit(record);
            } else {
                obstetricsInitDAO.addObstetricsInit(record);
                TransactionLogger.getInstance().logTransaction(TransactionType.INITIAL_OBSTETRIC_RECORD_CREATE, hcpMID, patientMID , "");
            }
            response.setContentType("text/json");
            PrintWriter pw = response.getWriter();
            pw.print(record.toString());
        } catch (DBException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Long recordID = Long.parseLong(request.getParameter(RECORD_ID));
        try {
            if(recordID >= 0) {
                ObstetricsInitBean record = obstetricsInitDAO.getObstetricsInitByID(recordID);
                response.setContentType("text/json");
                PrintWriter pw = response.getWriter();
                pw.print(record.toString());
            }
        } catch (DBException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}
