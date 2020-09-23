package edu.ncsu.csc.itrust.server;

import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.logger.TransactionLogger;
import edu.ncsu.csc.itrust.model.old.beans.ChildBirthVisitBean;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;
import edu.ncsu.csc.itrust.model.old.dao.mysql.ChildBirthVisitDAO;
import edu.ncsu.csc.itrust.model.old.enums.BooleanType;
import edu.ncsu.csc.itrust.model.old.enums.TransactionType;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name="ChildbirthVisitServlet", urlPatterns = "/auth/hcp/ChildbirthVisitServlet")
public class ChildbirthVisitServlet extends HttpServlet {
	/**
	<td class="prefDeliveryMethod"><%=preferredDeliveryMethod%></td>
    <td class="hasDelivered"><%=hasDelivered%></td>
    <td class="pd"><%=pitocinDosage%></td>
    <td class="nod"><%=nitrousOxideDosage %></td>
    <td class="ead"><%=epiduralAnaesthesiaDosage %></td>
    <td class="msd"><%=magnesiumSulfateDosage %></td>
    <td class="rgd"><%=rhImmuneGlobulinDosage %></td>
    */
    private static final String RECORD_ID = "recordID";
    private static final String HCP_ID = "hcpMID";
	private static final String PID = "patientMID";
    private static final String PREF_DELIVERY_METHOD = "prefDeliveryMethod";
    private static final String PND = "pnd";
    private static final String NOD = "nod";
    private static final String PED = "ped";
    private static final String EAD = "ead";
    private static final String MSD = "msd";
    private static final String RH = "rh";

    private DAOFactory prodFactory;
    private ChildBirthVisitDAO childBirthVisitDAO;

    public ChildbirthVisitServlet() {
        prodFactory = DAOFactory.getProductionInstance();
        childBirthVisitDAO = prodFactory.getChildBirthVisitDAO();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        long recordID = Long.parseLong(request.getParameter(RECORD_ID));
        long hcpID = Long.parseLong(request.getParameter(HCP_ID));
        long pid = Long.parseLong(request.getParameter(PID));
        long pnd = Long.parseLong(request.getParameter(PND));
        long nod = Long.parseLong(request.getParameter(NOD));
        long ped = Long.parseLong(request.getParameter(PED));
        long ead = Long.parseLong(request.getParameter(EAD));
        long msd = Long.parseLong(request.getParameter(MSD));
        long rh = Long.parseLong(request.getParameter(RH));

        ChildBirthVisitBean record = new ChildBirthVisitBean();

        try {

            List<ChildBirthVisitBean> visits = childBirthVisitDAO.getChildBirthVisitByInitID(recordID);
            if (visits != null && visits.size() > 0) {
                record = visits.get(0);
            } else {
                //set to -1 to determine if the operation is add or edit
                record.setVisitID(-1);
            }

            // set preferred delivery type if present
            if (request.getParameter(PREF_DELIVERY_METHOD) != null) {
                String prefDeliveryMethod = request.getParameter(PREF_DELIVERY_METHOD);
                record.setPreferredDeliveryMethodStr(prefDeliveryMethod);
            }
            record.setObstetricsInitRecordID(recordID);
            // if submitted, then they have delivered
            record.setHasDelivered(BooleanType.True);
            record.setPitocinDosage(pnd);
            record.setNitrousOxideDosage(nod);
            record.setPethidineDosage(ped);
            record.setEpiduralAnaesthesiaDosage(ead);
            record.setMagnesiumSulfateDosage(msd);
            record.setRhImmuneGlobulinDosage(rh);

            if(record.getVisitID() == -1){
                childBirthVisitDAO.addChildBirthVisit(record);
                TransactionLogger.getInstance().logTransaction(TransactionType.CREATE_CHILDBIRTH_VISIT, hcpID, pid , "");
            } else {
                childBirthVisitDAO.editChildBirthVisit(record);
                TransactionLogger.getInstance().logTransaction(TransactionType.EDIT_CHILDBIRTH_VISIT, hcpID, pid , "");
                TransactionLogger.getInstance().logTransaction(TransactionType.ADD_CHILDBIRTH_DRUGS, hcpID, pid , "");
            }


            response.setContentType("text/json");
            PrintWriter pw = response.getWriter();
            pw.print(record.toString());
        } catch(DBException e) {
            e.printStackTrace();
        }
    }
}
