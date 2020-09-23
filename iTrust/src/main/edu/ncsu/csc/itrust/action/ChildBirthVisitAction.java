package edu.ncsu.csc.itrust.action;

import edu.ncsu.csc.itrust.model.old.beans.PersonnelBean;
import edu.ncsu.csc.itrust.model.old.beans.ChildBirthVisitBean;
import edu.ncsu.csc.itrust.model.old.beans.PatientBean;
import edu.ncsu.csc.itrust.model.old.beans.BabyDeliveryInfoBean;

import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;
import edu.ncsu.csc.itrust.model.old.dao.mysql.PersonnelDAO;
import edu.ncsu.csc.itrust.model.old.dao.mysql.ChildBirthVisitDAO;
import edu.ncsu.csc.itrust.model.old.dao.mysql.PatientDAO;
import edu.ncsu.csc.itrust.model.old.dao.mysql.BabyDeliveryInfoDAO;
import edu.ncsu.csc.itrust.model.old.enums.BooleanType;
import edu.ncsu.csc.itrust.model.old.enums.TransactionType;
import edu.ncsu.csc.itrust.model.old.validate.BabyDeliveryInfoValidator;
import edu.ncsu.csc.itrust.model.old.validate.ChildBirthVisitValidator;

import java.util.ArrayList;
import java.util.List;

import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.exception.ITrustException;
import edu.ncsu.csc.itrust.logger.TransactionLogger;

public class ChildBirthVisitAction {
	private PersonnelDAO personnelDAO;
	private ChildBirthVisitDAO cbvDAO;
	private BabyDeliveryInfoDAO bdiDAO;
	private PatientDAO patientDAO;
	
	private long loggedInMID;
	private long patientMID;
	
	private boolean isEmergency = false;
	private boolean outsideBirth = false;
	
	/**
	 * Constructor, first checks if the user requiring the action is eligible to do so
	 *
	 * @param factory DAO factory to be used for generating the DAOs for this action
	 * @param loggedInMID Log in MID of HCP for this action
	 * @param patientMID MID of maternal patient
	 * @throws ITrustException Throw exception if patient is not eligible to view
	 */
	public ChildBirthVisitAction(DAOFactory factory, long loggedInMID, long patientMID) throws ITrustException {
		this.personnelDAO = factory.getPersonnelDAO();
		this.cbvDAO = factory.getChildBirthVisitDAO();
		this.bdiDAO = factory.getBabyDeliveryInfoDAO();
		this.patientDAO = factory.getPatientDAO();
		
		this.loggedInMID = loggedInMID;
		this.patientMID = patientMID;
	}
	
	/**
	 * Checks whether or not there is a child birth visit associated with the given obstetrics care record ID
	 * 
	 * @param long obstetricsInitID
	 * @return boolean exists
	 */
	public boolean exists(long obstetricsInitID) {
		boolean ret = false;
		try {
			ret = cbvDAO.checkChildBirthVisitExistsByInitID(obstetricsInitID);
		}
		catch (DBException e) {
			/* If a DBException occurs print a stack trace and return false
			 * (same way that EditPatientAction handles DBException)
			 */
			e.printStackTrace();
		}
		
		return ret;
	}
	
	/**
	 * Checks whether or not the baby was delivered / old record
	 * 
	 * @param visitID child birth visit ID
	 * @return boolean hasDelivered
	 */
	public boolean hasDelivered(long visitID) {
		boolean ret = false;
		try {
			ChildBirthVisitBean cbv = cbvDAO.getChildBirthVisitByVisitID(visitID);
			ret = cbv.getHasDelivered().equals(BooleanType.True);
		}
		catch (DBException e) {
			/* If a DBException occurs print a stack trace and return false */
			e.printStackTrace();
		}
		
		return ret;
	}
	
	public boolean getIsEmergency() {
		return isEmergency;
	}
	
	public void setEmergency(boolean isEmergency) {
		this.isEmergency = isEmergency;
	}
	
	public boolean getOutsideBirth() {
		return outsideBirth;
	}
	
	public void setOutsideBirth(boolean outsideBirth) {
		this.outsideBirth = outsideBirth;
	}
	
	/**
	 * Gets the child birth visit associated with the given visit ID
	 * 
	 * @param visitID
	 * @return ChildBirthVisitBean
	 */
	public ChildBirthVisitBean getChildBirthVisitRecordByVisitID(long visitID) {
		ChildBirthVisitBean cbv = null;
		try {
			cbv = cbvDAO.getChildBirthVisitByVisitID(visitID);
		}
		catch (DBException e) {
			e.printStackTrace();
		}
		
		return cbv;
	}
	
	/**
	 * Gets list of child birth visits associated with this action's obstetrics initialization record ID
	 * 
	 * @param long obstetricsInitID
	 * @return List<ChildBirthVisitBean>
	 */
	public List<ChildBirthVisitBean> getChildBirthVisitRecordByInitID(long obstetricsInitID) {
		List<ChildBirthVisitBean> cbvList = null;
		try {
			cbvList = cbvDAO.getChildBirthVisitByInitID(obstetricsInitID);
		}
		catch (DBException e) {
			e.printStackTrace();
		}
		
		return cbvList;
	}
	
	/**
	 * Gets the baby delivery info associated with the given recordID
	 * 
	 * @param recordID
	 * @return BabyDeliveryInfoBean
	 */
	public BabyDeliveryInfoBean getBabyDeliveryInfoByRecordID(long recordID) {
		BabyDeliveryInfoBean bdi = null;
		try {
			bdi = bdiDAO.getBabyDeliveryInfoByRecordID(recordID);
		}
		catch (DBException e) {
			e.printStackTrace();
		}
		
		return bdi;
	}
	
	/**
	 * Gets list of baby delivery info records associated with the given visitID
	 * 
	 * @param visitID
	 * @return List<BabyDeliveryInfoBean>
	 */
	public List<BabyDeliveryInfoBean> getBabyDeliveryInfoByVisitID(long visitID) {
		List<BabyDeliveryInfoBean> bdiList = null;
		try {
			bdiList = bdiDAO.getBabyDeliveryInfoByVisitID(visitID);
		}
		catch (DBException e) {
			e.printStackTrace();
		}
		
		return bdiList;
	}
	
	/**
     * Check if the HCP is with a specialization of “OB/GYN” or not
     *
     * @param personnelDAO
     * @param loggedInMID
     * @throws ITrustException
     */
    public boolean isOBGYN() throws ITrustException{
		try {
			String specialty = this.personnelDAO.getPersonnel(this.loggedInMID).getSpecialty();
			return specialty.equals("OB/GYN");
		} catch (DBException e) {
			e.printStackTrace();
			throw new ITrustException("Database Error");
		}
	}
	
	/**
	 * Adds a childbirth visit record to the database
	 * 
	 * @param cbv
	 * @throws FormValidationException
	 * @throws ITrustException
	 */
	public void addChildBirthVisitRecord(ChildBirthVisitBean cbv) throws FormValidationException, ITrustException {
		ChildBirthVisitValidator val = new ChildBirthVisitValidator();
		val.validate(cbv);
		cbvDAO.addChildBirthVisit(cbv);
		TransactionLogger.getInstance().logTransaction(TransactionType.CREATE_CHILDBIRTH_VISIT, loggedInMID, patientMID, "");
	}
	
	/**
	 * Adds drugs associated with an existing childbirth visit to the database
	 * 
	 * @param cbv
	 * @throws FormValidationException
	 * @throws ITrustException
	 */
	public void addChildBirthDrugs(ChildBirthVisitBean cbv) throws FormValidationException, ITrustException {
		/* Due to the way ChildBirthVisit model classes were implemented, rest of cbv fields should also
		 * be filled out. Otherwise, database will be overwritten with default values for those fields
		 */
		ChildBirthVisitValidator val = new ChildBirthVisitValidator();
		val.validate(cbv);
		
		try {
			if (cbvDAO.checkChildBirthVisitExistsByVisitID(cbv.getVisitID())) {
				cbvDAO.editChildBirthVisit(cbv);
				TransactionLogger.getInstance().logTransaction(TransactionType.ADD_CHILDBIRTH_DRUGS, loggedInMID, patientMID, "");
			}
		}
		catch (DBException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Adds baby delivery info to database and creates a patient record for the delivered baby
	 * 
	 * @param bdi
	 * @throws FormValidationException
	 * @throws ITrustException
	 */
	public void addBabyDeliveryInfo(BabyDeliveryInfoBean bdi) throws FormValidationException, ITrustException {
		if (outsideBirth) bdi.setIsEstimated(BooleanType.True);
		
		BabyDeliveryInfoValidator val = new BabyDeliveryInfoValidator();
		val.validate(bdi);
		
		bdiDAO.addBabyDeliveryInfo(bdi);          /* Add baby delivery info */
		
		long babyMID = patientDAO.addEmptyPatient();	/* Add patient file for baby */
		bdi.setPatientMID(babyMID);
		bdiDAO.editBabyDeliveryInfo(bdi);
		PatientBean p = new PatientBean();
		p.setMID(babyMID);
		p.setGender(bdi.getGender());
		p.setDateOfBirthStr(bdi.getDateOfBirthStr());
		/* not sure what other information should be set besides what is known from given bdi */
		patientDAO.editPatient(p, loggedInMID);
		
		TransactionLogger.getInstance().logTransaction(TransactionType.A_BABY_IS_BORN, loggedInMID, patientMID, "");
		TransactionLogger.getInstance().logTransaction(TransactionType.CREATE_BABY_RECORD, loggedInMID, patientMID, Long.toString(babyMID));
	}
	
	/**
	 * Edits given child birth visit and all given baby delivery info records
	 * 
	 * @param cbv
	 * @param bdiList
	 * @throws FormValidationException
	 * @throws ITrustException
	 */
	public void editChildBirthVisitRecord(ChildBirthVisitBean cbv, List<BabyDeliveryInfoBean> bdiList) throws FormValidationException, ITrustException {
		checkEligibility(personnelDAO, loggedInMID);
		
		ChildBirthVisitValidator cbvVal = new ChildBirthVisitValidator();
		BabyDeliveryInfoValidator bdiVal = new BabyDeliveryInfoValidator();
		cbvVal.validate(cbv);
		for (int i = 0; i < bdiList.size(); i++) {
			bdiVal.validate(bdiList.get(i));
		}
		
		cbvDAO.editChildBirthVisit(cbv);
		for (int i = 0; i < bdiList.size(); i++) {
			bdiDAO.editBabyDeliveryInfo(bdiList.get(i));
		}
		
		TransactionLogger.getInstance().logTransaction(TransactionType.EDIT_CHILDBIRTH_VISIT, loggedInMID, patientMID, "");
	}
	
	/**
	 * Check if the HCP is with a specialization of “OB/GYN” or not
	 * 
	 * @param personnelDAO
	 * @param loggedInMID
	 * @throws ITrustException
	 */
	private void checkEligibility(PersonnelDAO personnelDAO, long loggedInMID) throws ITrustException{
		PersonnelBean personnel = personnelDAO.getPersonnel(loggedInMID);
	    String specialty = personnel.getSpecialty(); 
	    if(!specialty.equals("OB/GYN")){
	    	throw new ITrustException("User is not with a specialization of “OB/GYN”");
	    }
	}
}
