package edu.ncsu.csc.itrust.action;

import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.ITrustException;
import edu.ncsu.csc.itrust.logger.TransactionLogger;
import edu.ncsu.csc.itrust.model.old.beans.*;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;
import edu.ncsu.csc.itrust.model.old.dao.mysql.*;
import edu.ncsu.csc.itrust.model.old.enums.BooleanType;
import edu.ncsu.csc.itrust.model.old.enums.Obstetrics;
import edu.ncsu.csc.itrust.model.old.enums.TransactionType;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class LaborDeliveryReportAction{
	private PatientDAO patientDAO;
	private ObstetricsInitDAO obstetricsInitDAO;
	private ObstetricsOfficeVisitDAO obstetricsOfficeVisitDAO;
	private PriorPregnancyDAO priorPregnancyDAO;
	private AllergyDAO allergyDAO;
	


	private PatientBean patient;
	private ObstetricsInitBean obRecord;
    private ObstetricsOfficeVisitBean recentVisit;
	private List<ObstetricsOfficeVisitBean> visits;
	private List<PriorPregnancyBean> pregnancies;
	/**
	 * Constructor, first checks if the user requiring the action is eligible to do so
	 *
	 * @param factory DAO factory to be used for generating the DAOs for this action
	 * @param loggedInMID Log in MID of HCP for this action
	 * @param patientMID Patient MID to view for this action
	 * @throws ITrustException Throw exception if patient is not eligible to view
	 */
	public LaborDeliveryReportAction (DAOFactory factory, long loggedInMID, long patientMID) throws ITrustException {
		this.patientDAO = factory.getPatientDAO();
		this.priorPregnancyDAO = factory.getPriorPregnancyDAO();
		this.obstetricsInitDAO = factory.getObstetricsInitDAO();
		this.obstetricsOfficeVisitDAO = factory.getObstetricsOfficeVisitDAO();
        this.allergyDAO = factory.getAllergyDAO();

        loadPatient(patientMID);
        loadObRecord();
        checkEligibility(factory.getPatientDAO(), patientMID);
        loadVisits();
        loadPregnancies();

		TransactionLogger.getInstance().logTransaction(TransactionType.VIEW_LABOR_DELIVERY_REPORT, loggedInMID, patientMID , "");
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
		if (obRecord == null) {
            throw new ITrustException("Patient has no obstetric care records.");
        }
	}

	private void loadPatient(long mid) throws ITrustException{
		try {
			this.patient = patientDAO.getPatient(mid);
		} catch (DBException e) {
			e.printStackTrace();
			throw new ITrustException("Error reading patient from database");
		}
	}

	private void loadObRecord() throws ITrustException{
		try {
			this.obRecord = obstetricsInitDAO.getObstetricsInitByMostRecent(patient.getMID());
		} catch (DBException e) {
			e.printStackTrace();
			throw new ITrustException("Error reading obstetric record from database");
		}
	}

	private void loadVisits() throws ITrustException{
        try {
            List<ObstetricsOfficeVisitBean> records = this.obstetricsOfficeVisitDAO.getObstetricsOfficeVisitByInitRecord(obRecord.getRecordID());
            records = this.obstetricsOfficeVisitDAO.sortByVisitDate(records);
            if(records.size() == 0) {
                this.recentVisit = new ObstetricsOfficeVisitBean();
                this.visits = new ArrayList<>();
                return;
            }
            this.visits = records;
            this.recentVisit = visits.get(0);
        } catch(DBException e) {
            throw new ITrustException("Error reading Obstetric Office Visits from Database");
        }
    }

    private void loadPregnancies() throws ITrustException{
        List<ObstetricsInitBean> obRecords = this.obstetricsInitDAO.getObstetricsInitByMID(patient.getMID());
        Date currentLMPDate = obRecord.getLastMenstrualPeriod();
        List<PriorPregnancyBean> result = this.priorPregnancyDAO.getPriorPregnancyByObstetricRecordIDList(obRecords,currentLMPDate);
        this.pregnancies = result;
    }
	
	public List<PriorPregnancyBean> getPriorPregnancies() {
		return pregnancies;
	}
	
	public String getBloodType() {
        return patient.getBloodTypeStr();
	}
	
	public String getEstimatedDeliveryDate() {
	    return obRecord.getEstimatedDueDateMMDDYYYY();
	}
	
	public List<ObstetricsOfficeVisitBean> getAllObstetricsOfficeVisits() throws ITrustException {
		return this.visits;
	}
	
	public String hasRHFlag() {
	    return patient.getRH().getName();
	}

	public List<AllergyBean> getAllergies() throws ITrustException {
        List<AllergyBean> allergies = this.allergyDAO.getAllergies(patient.getMID());
        return allergies;
    }

    public String hasAdvancedMaternalAge() {
	    Date dateOfBirth = patient.getDateOfBirth();
	    Date edd = obRecord.getEstimatedDueDate();
	    int age = edd.getYear() - dateOfBirth.getYear();
	    // if the date isn't past their birthday, then subtract one from age
	    if (edd.getMonth() < dateOfBirth.getMonth() && edd.getDay() < dateOfBirth.getDay()) {
	        age--;
        }

        BooleanType isOld = BooleanType.False;
	    if (age >= 35) {
	        isOld = BooleanType.True;
        }
        return isOld.getName() + ", " + String.valueOf(age) + " years old";
    }

    public String hasLyingPlacenta() {
	    return this.recentVisit.getLowLyingPlacentaObserved().getName();
    }

    public String mulitples() {
	    int numBabies = this.recentVisit.getNumberOfBabies();
	    if(numBabies > 1) {
	        return "True, " + numBabies + " babies expected";
        } else {
	        return "False";
        }
    }

    public String atypicalWeightChange() {
	    float weightChange = recentVisit.getWeight();
	    if (weightChange > 35 || weightChange < 15) {
            return "True, weight change of " + weightChange + " lbs";
        } else {
	        return "False, weight change of " + weightChange + " lbs";
        }
    }

    public String abnormalFetalHR() {
	    int fetalHR = recentVisit.getFetalHeartRate();
	    if(fetalHR > 160 || fetalHR < 120) {
	        return "True, fetal heart rate is at "  + fetalHR;
        } else {
            return "False, fetal heart rate is at "  + fetalHR;
        }
    }

}
