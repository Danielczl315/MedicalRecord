/**
 *
 */
package edu.ncsu.csc.itrust.action;

import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.exception.ITrustException;
import edu.ncsu.csc.itrust.logger.TransactionLogger;
import edu.ncsu.csc.itrust.model.old.beans.*;
import edu.ncsu.csc.itrust.model.old.beans.ObstetricsInitBean;
import edu.ncsu.csc.itrust.model.old.beans.ObstetricsOfficeVisitBean;
import edu.ncsu.csc.itrust.model.old.beans.PatientBean;
import edu.ncsu.csc.itrust.model.old.beans.PersonnelBean;
import edu.ncsu.csc.itrust.model.old.beans.UltrasoundRecordBean;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;
import edu.ncsu.csc.itrust.model.old.dao.mysql.*;
import edu.ncsu.csc.itrust.model.old.dao.mysql.ObstetricsInitDAO;
import edu.ncsu.csc.itrust.model.old.dao.mysql.ObstetricsOfficeVisitDAO;
import edu.ncsu.csc.itrust.model.old.dao.mysql.PatientDAO;
import edu.ncsu.csc.itrust.model.old.dao.mysql.PersonnelDAO;
import edu.ncsu.csc.itrust.model.old.dao.mysql.UltrasoundRecordDAO;
import edu.ncsu.csc.itrust.model.old.enums.Obstetrics;
import edu.ncsu.csc.itrust.model.old.enums.BooleanType;
import edu.ncsu.csc.itrust.model.old.enums.TransactionType;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeUnit;
import java.util.Date;
import java.util.List;

/**
 * Author: PB_SO_COOL
 */
public class ViewObstetricOfficeVisitAction {
    private PersonnelDAO personnelDAO;
    private PatientDAO patientDAO;
    private ObstetricsOfficeVisitDAO obOfficeVisitDAO;
    private UltrasoundRecordDAO ultrasoundRecordDAO;
    private ObstetricsInitDAO obstetricsInitDAO;
    private ApptDAO apptDAO;

    private DAOFactory __factory;
    private long loggedInMID;
    private long patientMID;

    /**
     * Constructor, first checks if the user requiring the action is eligible to do so
     *
     * @param factory DAO factory to be used for generating the DAOs for this action
     * @param loggedInMID Log in MID of HCP for this action
     * @param patientMID Patient MID to view for this action
     * @throws ITrustException Throw exception if patient is not eligible to view
     */
    public ViewObstetricOfficeVisitAction(DAOFactory factory, long loggedInMID, long patientMID) throws ITrustException {
    	checkEligibility(factory.getPatientDAO(), patientMID);
        this.personnelDAO = factory.getPersonnelDAO();
        this.patientDAO = factory.getPatientDAO();
        this.obOfficeVisitDAO = factory.getObstetricsOfficeVisitDAO();
        this.ultrasoundRecordDAO = factory.getUltrasoundRecordDAO();
        this.obstetricsInitDAO = factory.getObstetricsInitDAO();
        this.apptDAO = factory.getApptDAO();

        this.__factory = factory;
        this.loggedInMID = loggedInMID;
        this.patientMID = patientMID;
        checkIfRecent();

        TransactionLogger.getInstance().logTransaction(TransactionType.VIEW_OBSTETRIC_OFFICE_VISIT, loggedInMID, patientMID , "");
    }

    /**
	 * Check if the patient is eligible for obstetric care
	 *
	 * @param patientDAO
	 * @param patientMID
	 * @throws ITrustException
	 */
	private void checkEligibility(PatientDAO patientDAO, long patientMID) throws ITrustException{
		PatientBean patient = patientDAO.getPatient(patientMID);
		Obstetrics type = patient.getObstetrics();
		if(type != Obstetrics.Eligible){
			throw new ITrustException("Patient is not eligible for obstetrics care.");
		}
	}

    public ObstetricsOfficeVisitBean getObstetricsOfficeVisitByID(long visitID) throws ITrustException {
        ObstetricsOfficeVisitBean result;
        result = obOfficeVisitDAO.getObstetricsOfficeVisitByID(visitID);

        return result;
    }

    public ObstetricsInitBean getObstetricsInitByVisitID(long visitID) throws ITrustException {
    	ObstetricsOfficeVisitBean visit;
        ObstetricsInitBean result;
        visit = obOfficeVisitDAO.getObstetricsOfficeVisitByID(visitID);
        result = obstetricsInitDAO.getObstetricsInitByID(visit.getObstetricsInitRecordID());

        return result;
    }
    
    public ObstetricsInitBean getRecentObstetricsInit() throws ITrustException {
        ObstetricsInitBean result;
        ViewObstetricCareRecordsAction obAction = new ViewObstetricCareRecordsAction(__factory, loggedInMID, patientMID);
        List<ObstetricsInitBean> obRecords = obAction.getListObstetricInitRecords();
        result = obRecords.get(0);

        return result;
    }

    /**
     * Returns list of obstetrics office visits associated with the patient
     * @throws FormValidationException, ITrustException
     */
    public List<ObstetricsOfficeVisitBean> getObstetricOfficeVisitList() throws ITrustException {
        List<ObstetricsOfficeVisitBean> result;
        result = this.obOfficeVisitDAO.getObstetricsOfficeVisitByMID(patientMID);	 //Acccess from obsDAO
        result = this.obOfficeVisitDAO.sortByVisitDate(result);

        return result;
    }

    /**
     * Edits an ultrasound record
     * @param visitID the specific visit
     * @throws FormValidationException, ITrustException
     */
    public List<UltrasoundRecordBean> getUltrasoundRecords(long visitID) throws ITrustException {
        List<UltrasoundRecordBean> result;
        result = ultrasoundRecordDAO.getUltrasoundRecordByVisitID(visitID);

        return result;
    }

    /**
     *
     * Check if the HCP is with a specialization of “OB/GYN” or not
     * @throws ITrustException
     */
    public boolean isOBGYN() throws ITrustException{
		String specialty = this.personnelDAO.getPersonnel(this.loggedInMID).getSpecialty();
		return specialty.equals("OB/GYN");
	}

    public boolean checkCurrentObstetrics(long visitID) throws ITrustException{
        boolean result = false;
        ViewObstetricCareRecordsAction obAction = new ViewObstetricCareRecordsAction(__factory, loggedInMID, patientMID);
        List<ObstetricsInitBean> obRecords = obAction.getListObstetricInitRecords();
        ObstetricsInitBean recentOb = obRecords.get(0);
        Date lmp = recentOb.getLastMenstrualPeriod();
        ObstetricsOfficeVisitBean chosenVisit = obOfficeVisitDAO.getObstetricsOfficeVisitByID(visitID);
        long diff = chosenVisit.getVisitDate().getTime() - lmp.getTime();
        if (diff >= 0L && diff < 49L*7L*24L*60L*60L*1000L) {
            result = true;
        }

        return result;
    }

    public ObstetricsOfficeVisitBean getNewOfficeVisit() throws ITrustException {
        ObstetricsOfficeVisitBean visit = new ObstetricsOfficeVisitBean();
        long visitID = obOfficeVisitDAO.addObstetricsOfficeVisit(visit);
        visit.setVisitID(visitID);
        return visit;
    }


    public void scheduleNextVisit(ObstetricsOfficeVisitBean currentVisit) throws ITrustException{
        ApptBean appt = new ApptBean();
        appt.setPatient(this.patientMID);
        appt.setHcp(this.loggedInMID);
        appt.setComment("Upcoming Visit");
        appt.setApptType("Obstetrics");
        ObstetricsInitBean recentInitRecord = getRecentObstetricsInit();
        Date lmp = recentInitRecord.getLastMenstrualPeriod();
        Date today = currentVisit.getVisitDate();
        long diff = Math.abs(today.getTime() - lmp.getTime());
        long days = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
        long weeks = days / 7;

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);

        if (weeks >= 0 && weeks <= 13) {
            calendar.add(Calendar.DATE, 7 * 4);
        } else if (weeks >= 14 && weeks <= 28) {
            calendar.add(Calendar.DATE, 7 * 2);
        } else if (weeks >= 29 && weeks <= 40) {
            calendar.add(Calendar.DATE, 7 * 1);
        } else if (weeks >= 40 && weeks < 42) {
            calendar.add(Calendar.DATE, 2);
        } else {
            TransactionLogger.getInstance().logTransaction(TransactionType.SCHEDULE_CHILDBIRTH, this.loggedInMID, this.patientMID, "");
            calendar.add(Calendar.DATE, 2);
            appt.setApptType("Childbirth");
        }
        appt.setDate(new Timestamp(calendar.getTimeInMillis()));
        int apptID = (int) apptDAO.scheduleAppt(appt);

        ObstetricsOfficeVisitBean futureVisit = new ObstetricsOfficeVisitBean();
        futureVisit.setApptID(apptID);
        futureVisit.setHcpMID(loggedInMID);
        futureVisit.setPatientMID(patientMID);
        futureVisit.setVisitDateStr(new SimpleDateFormat("MM/dd/yyyy").format(calendar.getTime()));
        futureVisit.setObstetricsInitRecordID(recentInitRecord.getRecordID());
        obOfficeVisitDAO.addObstetricsOfficeVisit(futureVisit);
    }

    public String getGoogleLink(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        String year = String.valueOf(calendar.get(Calendar.YEAR));
        String month = String.valueOf(calendar.get(Calendar.MONTH) + 1);
        if(calendar.get(Calendar.MONTH) < 10){
            month = "0" + month;
        }
        String day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        if(calendar.get(Calendar.DAY_OF_MONTH) < 10){
            day = "0" + day;
        }
        String startHour = String.valueOf(calendar.get(Calendar.HOUR_OF_DAY) + 6);
        if(calendar.get(Calendar.HOUR_OF_DAY) < 10){
            startHour = "0" + startHour;
        }
        String startMin = String.valueOf(calendar.get(Calendar.MINUTE));
        if(calendar.get(Calendar.MINUTE) < 10){
            startMin = "0" + startMin;
        }
        String endHour = String.valueOf(calendar.get(Calendar.HOUR_OF_DAY) + 6);
        if(calendar.get(Calendar.HOUR_OF_DAY) < 10){
            endHour = "0" + endHour;
        }
        String endMin = String.valueOf(calendar.get(Calendar.MINUTE));
        if(calendar.get(Calendar.MINUTE) < 10){
            endMin = "0" + endMin;
        }
        String URL = "http://www.google.com/calendar/event?action=TEMPLATE&dates=" + year + month + day + "T" + startHour + startMin + "00Z%2F" + year + month + day + "T" + endHour + endMin + "00Z&text=Obstetrics+Office+Visit&location=&details=";
        return URL;
    }

    public Map<Long, String> getVisitLabelsMap() throws ITrustException{
        Map<Long, String> map = new HashMap<>();
        List<ObstetricsOfficeVisitBean> visits = getObstetricOfficeVisitList();
        for (ObstetricsOfficeVisitBean visit : visits) {
            if (visit.getApptID() != 0) {
                ApptBean appt = apptDAO.getAppt((int) visit.getApptID()).get(0);
                map.put(visit.getVisitID(), appt.getComment());
            } else {
                map.put(visit.getVisitID(), "Past Visit");
            }
        }
        return  map;
    }

    public void checkIfRecent() throws ITrustException{
        ViewObstetricCareRecordsAction obAction = new ViewObstetricCareRecordsAction(__factory, loggedInMID, patientMID);
        List<ObstetricsInitBean> obRecords = obAction.getListObstetricInitRecords();
        if (obRecords.size() == 0) {
            throw new ITrustException("Patient has no recent obstetric care records.");
        }
        Date lmp = obRecords.get(0).getLastMenstrualPeriod();
        Date today = new Date();

        long diff = today.getTime() - lmp.getTime();
        long days = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);

        long weeks = days / 7;

        if (weeks > 49) {
            throw new ITrustException("Patient has no recent obstetric care records.");
        }
    }


    public List<ObstetricsOfficeVisitBean> getRecentVisits() throws ITrustException{
        long id = getRecentObstetricsInit().getRecordID();
        List<ObstetricsOfficeVisitBean> allVisits = getObstetricOfficeVisitList();
        List<ObstetricsOfficeVisitBean> recentVisits = new ArrayList<>();
        for (ObstetricsOfficeVisitBean visit : allVisits) {
            if (visit.getObstetricsInitRecordID() == id) {
                recentVisits.add(visit);
            }
        }
        return recentVisits;
    }



    public boolean checkRHNotice(long patientMID, Date visitDate) throws ITrustException {
    	BooleanType rh = this.patientDAO.getPatient(patientMID).getRH();
    	BooleanType rhImmunization = this.patientDAO.getPatient(patientMID).getRHImmunization();
        Date lmp = getRecentObstetricsInit().getLastMenstrualPeriod();
        Date today = visitDate;
        long diff = today.getTime() - lmp.getTime();
        long days = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
        long weeks = days / 7;

        if (weeks > 28 && rh == BooleanType.True && rhImmunization == BooleanType.False) {

            return true;
        }
        return false;
    }
}
