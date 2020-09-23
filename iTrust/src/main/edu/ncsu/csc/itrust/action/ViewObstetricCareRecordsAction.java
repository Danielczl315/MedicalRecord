/**
 * 
 */
package edu.ncsu.csc.itrust.action;

import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.ITrustException;
import edu.ncsu.csc.itrust.logger.TransactionLogger;
import edu.ncsu.csc.itrust.model.old.beans.ObstetricsInitBean;
import edu.ncsu.csc.itrust.model.old.beans.PatientBean;
import edu.ncsu.csc.itrust.model.old.beans.PriorPregnancyBean;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;
import edu.ncsu.csc.itrust.model.old.dao.mysql.ObstetricsInitDAO;
import edu.ncsu.csc.itrust.model.old.dao.mysql.PatientDAO;
import edu.ncsu.csc.itrust.model.old.dao.mysql.PersonnelDAO;
import edu.ncsu.csc.itrust.model.old.dao.mysql.PriorPregnancyDAO;
import edu.ncsu.csc.itrust.model.old.enums.Obstetrics;
import edu.ncsu.csc.itrust.model.old.enums.TransactionType;

import java.util.*;

/**
 * @author zchen142
 */
public class ViewObstetricCareRecordsAction{
	private PatientDAO patientDAO;
	private PriorPregnancyDAO priorPregnancyDAO;
	private ObstetricsInitDAO obstetricsInitDAO;
	private PersonnelDAO personnelDAO;

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
	public ViewObstetricCareRecordsAction (DAOFactory factory, long loggedInMID, long patientMID) throws ITrustException {
		checkEligibility(factory.getPatientDAO(), patientMID);
		this.patientDAO = factory.getPatientDAO();
		this.priorPregnancyDAO = factory.getPriorPregnancyDAO();
		this.obstetricsInitDAO = factory.getObstetricsInitDAO();
		this.loggedInMID = loggedInMID;
		this.patientMID = patientMID;
		this.personnelDAO = factory.getPersonnelDAO();

		TransactionLogger.getInstance().logTransaction(TransactionType.INITIAL_OBSTETRIC_RECORD_VIEW, loggedInMID, patientMID , "");
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

	public boolean isOBGYN() throws ITrustException{
		String specialty = this.personnelDAO.getPersonnel(this.loggedInMID).getSpecialty();
		return specialty.equals("OB/GYN");
	}

	/**
	 * 
	 * @return A list of ObstetricInit beans
	 * @throws ITrustException if 
	 */

	public List<ObstetricsInitBean> getListObstetricInitRecords() throws ITrustException{
		List<ObstetricsInitBean> result;
		try {
			result = this.obstetricsInitDAO.getObstetricsInitByMID(patientMID);	 //Acccess from obsDAO
		} catch (DBException e) {
			throw new ITrustException("Invalid Patient MID");
		}

		Collections.sort(result, new Comparator<ObstetricsInitBean>() {
			@Override
			public int compare(ObstetricsInitBean o1, ObstetricsInitBean o2) {
				Date first = o1.getInitDate();
				Date second = o2.getInitDate();
				if (first == null && second == null) {
					return 0;
				} else if (first == null) {
					return 1;
				} else if (second == null){
					return -1;
				}
				return second.compareTo(first);
			}
		});

		return result;
	}

	public ObstetricsInitBean getObstetricInitRecord(long recordId) throws ITrustException {
		ObstetricsInitBean result;
		result = this.obstetricsInitDAO.getObstetricsInitByID(recordId);	 //Acccess from obsDAO

		return result;
	}

	/**
	 * Returns a list of prior pregnancies before the given date.
	 *
	 * @param obstetricsInitDate obstetrics initialization date
	 * @return A list of PriorPregnancy Bean
	 * @throws ITrustException 
	 */
	public List<PriorPregnancyBean> getListPriorPregnancies(Date obstetricsInitDate) throws ITrustException {
        List<ObstetricsInitBean> obstetricsInitRecords =  this.obstetricsInitDAO.getObstetricsInitByMID(this.patientMID);
        List<PriorPregnancyBean> result = this.priorPregnancyDAO.getPriorPregnancyByObstetricRecordIDList(obstetricsInitRecords, obstetricsInitDate);
        return result;
	}
}
