package edu.ncsu.csc.itrust.server;

import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.old.beans.PriorPregnancyBean;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;
import edu.ncsu.csc.itrust.model.old.dao.mysql.PriorPregnancyDAO;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name="PriorPregnancyServlet", urlPatterns = "/auth/hcp/PriorPregnancyServlet")
public class PriorPregnancyServlet extends HttpServlet {
    private static final String RECORD_ID = "recordID";
    private static final String OBSTETRIC_RECORD_ID = "obstetricRecordID";
    private static final String YEAR_OF_CONCEPTION = "yearOfConception";
    private static final String DAYS_PREGNANT = "daysPregnant";
    private static final String HOURS_IN_LABOR = "hoursInLabor";
    private static final String WEIGHT_GAIN = "weightGain";
    private static final String DELIVERY_TYPE = "deliveryType";
    private static final String MULTIPLICITY = "multiplicity";

    private DAOFactory prodFactory;
    private PriorPregnancyDAO priorPregnancyDAO;

    public PriorPregnancyServlet() {
        prodFactory = DAOFactory.getProductionInstance();
        priorPregnancyDAO = prodFactory.getPriorPregnancyDAO();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if(request.getParameter("delete") != null) {
            long recordID = Long.parseLong(request.getParameter(RECORD_ID));
            try {
                priorPregnancyDAO.deletePriorPregnancy(recordID);
                return;

            } catch (DBException e) {
                e.printStackTrace();
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            }
        }
        long recordID = Long.parseLong(request.getParameter(RECORD_ID));
        long obstetricRecordID = Long.parseLong(request.getParameter(OBSTETRIC_RECORD_ID));
        int yearOfConception = Integer.parseInt(request.getParameter(YEAR_OF_CONCEPTION));
        int daysPregnant = Integer.parseInt(request.getParameter(DAYS_PREGNANT));
        float hoursInLabor = Float.parseFloat(request.getParameter(HOURS_IN_LABOR));
        float weightGain = Float.parseFloat(request.getParameter(WEIGHT_GAIN));
        String type = request.getParameter(DELIVERY_TYPE);
        int multiplicity = Integer.parseInt(request.getParameter(MULTIPLICITY));

        PriorPregnancyBean record = new PriorPregnancyBean();
        record.setObstetricRecordID(obstetricRecordID);
        record.setYearOfConception(yearOfConception);
        record.setDaysPregnant(daysPregnant);
        record.setHoursInLabor(hoursInLabor);
        record.setWeightGain(weightGain);
        record.setDeliveryTypeStr(type);
        record.setMultiplicity(multiplicity);

        try {
            if(recordID >= 0) {
                record.setPriorPregnancyID(recordID);
                priorPregnancyDAO.editPriorPregnancy(record);
            } else {
                priorPregnancyDAO.addPriorPregnancy(record);
            }
            response.setContentType("text/json");
            PrintWriter pw = response.getWriter();
            pw.print(record.toString());
        } catch (DBException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}
