package edu.ncsu.csc.itrust.unit.action;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import edu.ncsu.csc.itrust.action.ViewObstetricCareRecordsAction;
import edu.ncsu.csc.itrust.exception.ITrustException;
import edu.ncsu.csc.itrust.model.old.beans.ObstetricsInitBean;
import edu.ncsu.csc.itrust.model.old.beans.PriorPregnancyBean;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;
import edu.ncsu.csc.itrust.model.old.dao.mysql.ObstetricsInitDAO;
import edu.ncsu.csc.itrust.model.old.dao.mysql.PatientDAO;
import edu.ncsu.csc.itrust.model.old.dao.mysql.PersonnelDAO;
import edu.ncsu.csc.itrust.model.old.dao.mysql.PriorPregnancyDAO;
import edu.ncsu.csc.itrust.model.old.enums.DeliveryType;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;
import edu.ncsu.csc.itrust.unit.testutils.TestDAOFactory;
import junit.framework.TestCase;

public class ViewObstetricCareRecordsActionTest extends TestCase {
	private DAOFactory factory;
	private TestDataGenerator gen;
	
	private PatientDAO patientDAO;
	private PriorPregnancyDAO ppDAO;
	private ObstetricsInitDAO oiDAO;
	private PersonnelDAO personnelDAO;
	
	private long loggedInMID = 9000000012L;	/* Kathryn Evans */
    private long patientMID = 1L;			/* Random Person */
    
    @Override
    protected void setUp() throws Exception {
    	factory = TestDAOFactory.getTestInstance();
    	patientDAO = factory.getPatientDAO();
    	ppDAO = factory.getPriorPregnancyDAO();
    	oiDAO = factory.getObstetricsInitDAO();
    	personnelDAO = factory.getPersonnelDAO();
    	
    	gen = new TestDataGenerator();
    	gen.clearAllTables();
    }
    
    /* Mainly tests checkEligibility */
    public void testActionConstructor() throws Exception {
    	gen.patient1();	/* Generate patient 1 */
    	ViewObstetricCareRecordsAction action = new ViewObstetricCareRecordsAction(factory, loggedInMID, patientMID);
    	
    	/* If throws exception, failed */
    }
    
    public void testActionConstructorIneligible() throws Exception {
    	try {
    		gen.patient2();	/* Generate patient 2 */
	    	ViewObstetricCareRecordsAction action = new ViewObstetricCareRecordsAction(factory, loggedInMID, 2);
    	}
	    catch (ITrustException e) {
	    	assertEquals("Patient is not eligible for obstetrics care.", e.getMessage());
	    }
    }
    
    public void testIsOBGYN() throws Exception {
    	gen.uc63();		/* Generate OB/GYN HCP */
    	gen.patient1();	/* Generate patient 1 */
    	ViewObstetricCareRecordsAction action = new ViewObstetricCareRecordsAction(factory, loggedInMID, patientMID);
    	
    	assertEquals(true, action.isOBGYN());
    }
    
    public void testGetObstetricInitRecords() throws Exception {
    	gen.patient1();
    	gen.obstetricsInit1();
    	ViewObstetricCareRecordsAction action = new ViewObstetricCareRecordsAction(factory, loggedInMID, patientMID);
    	
    	ObstetricsInitBean oi = oiDAO.getObstetricsInitByID(1);
    	ObstetricsInitBean oi1 = action.getObstetricInitRecord(1);
    	
    	assertEquals(oi.getRecordID(), oi1.getRecordID());
		assertEquals(oi.getPatientMID(), oi1.getPatientMID());
		assertEquals(oi.getInitDate(), oi1.getInitDate());
		assertEquals(oi.getLastMenstrualPeriod(), oi1.getLastMenstrualPeriod());
		assertEquals(oi.getEstimatedDueDate(), oi1.getEstimatedDueDate());
    }
    
    public void testGetListObstetricInitRecords() throws Exception {
    	gen.patient1();
    	gen.obstetricsInit1();
    	ViewObstetricCareRecordsAction action = new ViewObstetricCareRecordsAction(factory, loggedInMID, patientMID);
    	
    	List<ObstetricsInitBean> oiList = action.getListObstetricInitRecords();
    	assertEquals(3, oiList.size());

		ObstetricsInitBean oi = oiDAO.getObstetricsInitByID(3);
		ObstetricsInitBean oi3 = oiList.get(0);
		assertEquals(oi.getRecordID(), oi3.getRecordID());
		assertEquals(oi.getPatientMID(), oi3.getPatientMID());
		assertEquals(oi.getInitDate(), oi3.getInitDate());
		assertEquals(oi.getLastMenstrualPeriod(), oi3.getLastMenstrualPeriod());
		assertEquals(oi.getEstimatedDueDate(), oi3.getEstimatedDueDate());
		
		oi = oiDAO.getObstetricsInitByID(2);
		ObstetricsInitBean oi2 = oiList.get(1);
		assertEquals(oi.getRecordID(), oi2.getRecordID());
		assertEquals(oi.getPatientMID(), oi2.getPatientMID());
		assertEquals(oi.getInitDate(), oi2.getInitDate());
		assertEquals(oi.getLastMenstrualPeriod(), oi2.getLastMenstrualPeriod());
		assertEquals(oi.getEstimatedDueDate(), oi2.getEstimatedDueDate());
		
		oi = oiDAO.getObstetricsInitByID(1);
    	ObstetricsInitBean oi1 = oiList.get(2);
		assertEquals(oi.getRecordID(), oi1.getRecordID());
		assertEquals(oi.getPatientMID(), oi1.getPatientMID());
		assertEquals(oi.getInitDate(), oi1.getInitDate());
		assertEquals(oi.getLastMenstrualPeriod(), oi1.getLastMenstrualPeriod());
		assertEquals(oi.getEstimatedDueDate(), oi1.getEstimatedDueDate());
    }
    
    public void testGetListPriorPregnancies() throws Exception {
    	gen.patient1();
    	gen.obstetricsInit1();
    	gen.priorPregnancy1();
    	ViewObstetricCareRecordsAction action = new ViewObstetricCareRecordsAction(factory, loggedInMID, patientMID);
    	
    	Date obInitDate = new SimpleDateFormat("MM/dd/yyyy").parse("01/01/2002");
    	
    	List<PriorPregnancyBean> ppList = action.getListPriorPregnancies(obInitDate);
    	assertEquals(1, ppList.size());
    	
    	PriorPregnancyBean pp1 = ppList.get(0);
    	PriorPregnancyBean pp = ppDAO.getPriorPregnancyByID(1);
    	
    	assertEquals(pp.getPriorPregnancyID(), pp1.getPriorPregnancyID());
		assertEquals(pp.getObstetricRecordID(), pp1.getObstetricRecordID());
		assertEquals(pp.getYearOfConception(), pp1.getYearOfConception());
		assertEquals(pp.getDaysPregnant(), pp1.getDaysPregnant());
		assertEquals(pp.getWeeksDaysPregnant(), pp1.getWeeksDaysPregnant());
		assertEquals(pp.getHoursInLabor(), pp1.getHoursInLabor());
		assertEquals(pp.getWeightGain(), pp1.getWeightGain());
		assertEquals(pp.getDeliveryType(), pp1.getDeliveryType());
		assertEquals(pp.getMultiplicity(), pp1.getMultiplicity());
    }
}
