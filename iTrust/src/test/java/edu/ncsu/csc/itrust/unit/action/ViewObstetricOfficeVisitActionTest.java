package edu.ncsu.csc.itrust.unit.action;

import java.util.List;
import java.util.Date;

import edu.ncsu.csc.itrust.action.ViewObstetricOfficeVisitAction;
import edu.ncsu.csc.itrust.exception.ITrustException;
import edu.ncsu.csc.itrust.model.old.beans.ObstetricsInitBean;
import edu.ncsu.csc.itrust.model.old.beans.ObstetricsOfficeVisitBean;
import edu.ncsu.csc.itrust.model.old.beans.UltrasoundRecordBean;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;
import edu.ncsu.csc.itrust.model.old.dao.mysql.ObstetricsInitDAO;
import edu.ncsu.csc.itrust.model.old.dao.mysql.ObstetricsOfficeVisitDAO;
import edu.ncsu.csc.itrust.model.old.dao.mysql.PersonnelDAO;
import edu.ncsu.csc.itrust.model.old.dao.mysql.UltrasoundRecordDAO;
import edu.ncsu.csc.itrust.model.old.enums.BooleanType;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;
import edu.ncsu.csc.itrust.unit.testutils.TestDAOFactory;
import junit.framework.TestCase;

public class ViewObstetricOfficeVisitActionTest extends TestCase {
	private DAOFactory factory;
	private TestDataGenerator gen;
	
	private PersonnelDAO personnelDAO;
    private ObstetricsOfficeVisitDAO oovDAO;
    private UltrasoundRecordDAO usrDAO;
    private ObstetricsInitDAO oiDAO;
    
    private long loggedInMID = 9000000012L;	/* Kathryn Evans */
    private long patientMID = 1L;			/* Random Person */
    
    @Override
    protected void setUp() throws Exception {
    	factory = TestDAOFactory.getTestInstance();
    	personnelDAO = factory.getPersonnelDAO();
    	oovDAO = factory.getObstetricsOfficeVisitDAO();
    	usrDAO = factory.getUltrasoundRecordDAO();
    	oiDAO = factory.getObstetricsInitDAO();
    	
    	gen = new TestDataGenerator();
    	gen.clearAllTables();
    }
    
    public void testObstetricsOfficeVisitByID() throws Exception {
    	gen.patient1();
    	gen.obstetricsInit1();
    	gen.obstetricsOfficeVisit1();
    	
    	ViewObstetricOfficeVisitAction action = new ViewObstetricOfficeVisitAction(factory, loggedInMID, patientMID);
    	ObstetricsOfficeVisitBean oov1 = action.getObstetricsOfficeVisitByID(1);
    	ObstetricsOfficeVisitBean oov = oovDAO.getObstetricsOfficeVisitByID(1);
    	
    	assertEquals(oov.getVisitID(), oov1.getVisitID());
		assertEquals(oov.getObstetricsInitRecordID(), oov1.getObstetricsInitRecordID());
		assertEquals(oov.getLocationID(), oov1.getLocationID());
		assertEquals(oov.getPatientMID(), oov1.getPatientMID());
		assertEquals(oov.getHcpMID(), oov1.getHcpMID());
		assertEquals(oov.getApptID(), oov1.getApptID());
		assertEquals(oov.getVisitDate(), oov1.getVisitDate());
		assertEquals(oov.getWeight(), oov1.getWeight());
		assertEquals(oov.getBloodPressure(), oov1.getBloodPressure());
		assertEquals(oov.getFetalHeartRate(), oov1.getFetalHeartRate());
		assertEquals(oov.getLowLyingPlacentaObserved(), oov1.getLowLyingPlacentaObserved());
		assertEquals(oov.getNumberOfBabies(), oov1.getNumberOfBabies());
		
		try
		{
			action.getObstetricsOfficeVisitByID(-100000);
		}
		catch (ITrustException e)
		{
			
		}
    }
    
    public void testGetObstetricsInitByVisitID() throws Exception {
    	gen.patient1();
    	gen.obstetricsInit1();
    	gen.obstetricsOfficeVisit1();
    	
    	ViewObstetricOfficeVisitAction action = new ViewObstetricOfficeVisitAction(factory, loggedInMID, patientMID);
    	
    	ObstetricsInitBean oi = oiDAO.getObstetricsInitByID(1);
    	ObstetricsInitBean oi1 = action.getObstetricsInitByVisitID(1);
    	
    	assertEquals(oi.getRecordID(), oi1.getRecordID());
		assertEquals(oi.getPatientMID(), oi1.getPatientMID());
		assertEquals(oi.getInitDate(), oi1.getInitDate());
		assertEquals(oi.getLastMenstrualPeriod(), oi1.getLastMenstrualPeriod());
		assertEquals(oi.getEstimatedDueDate(), oi1.getEstimatedDueDate());
    }
    
    public void testGetRecentObstetricsInit() throws Exception {
    	gen.patient1();
    	gen.obstetricsInit1();
    	gen.obstetricsOfficeVisit1();
    	
    	ViewObstetricOfficeVisitAction action = new ViewObstetricOfficeVisitAction(factory, loggedInMID, patientMID);
    	
    	ObstetricsInitBean oi = oiDAO.getObstetricsInitByID(3);
    	ObstetricsInitBean oi3 = action.getRecentObstetricsInit();
    	
    	assertEquals(oi.getRecordID(), oi3.getRecordID());
		assertEquals(oi.getPatientMID(), oi3.getPatientMID());
		assertEquals(oi.getInitDate(), oi3.getInitDate());
		assertEquals(oi.getLastMenstrualPeriod(), oi3.getLastMenstrualPeriod());
		assertEquals(oi.getEstimatedDueDate(), oi3.getEstimatedDueDate());

    }
    
    public void testGetObstetricsOfficeVisitList() throws Exception {
    	gen.patient1();
    	gen.obstetricsInit1();
    	gen.obstetricsOfficeVisit1();
    	gen.appointment();
    	
    	ViewObstetricOfficeVisitAction action = new ViewObstetricOfficeVisitAction(factory, loggedInMID, patientMID);
    	List<ObstetricsOfficeVisitBean> oovList = action.getObstetricOfficeVisitList();
    	
    	action.getVisitLabelsMap();
    	
    	assertEquals(3, oovList.size());
    	
    	ObstetricsOfficeVisitBean oov = oovDAO.getObstetricsOfficeVisitByID(2);
    	ObstetricsOfficeVisitBean oov2 = oovList.get(1);
    	
    	action.scheduleNextVisit(oov);
    	action.getGoogleLink(new Date());
    	
    	assertEquals(oov.getVisitID(), oov2.getVisitID());
		assertEquals(oov.getObstetricsInitRecordID(), oov2.getObstetricsInitRecordID());
		assertEquals(oov.getLocationID(), oov2.getLocationID());
		assertEquals(oov.getPatientMID(), oov2.getPatientMID());
		assertEquals(oov.getHcpMID(), oov2.getHcpMID());
		assertEquals(oov.getApptID(), oov2.getApptID());
		assertEquals(oov.getVisitDate(), oov2.getVisitDate());
		assertEquals(oov.getWeight(), oov2.getWeight());
		assertEquals(oov.getBloodPressure(), oov2.getBloodPressure());
		assertEquals(oov.getFetalHeartRate(), oov2.getFetalHeartRate());
		assertEquals(oov.getLowLyingPlacentaObserved(), oov2.getLowLyingPlacentaObserved());
		assertEquals(oov.getNumberOfBabies(), oov2.getNumberOfBabies());
    	
		oov = oovDAO.getObstetricsOfficeVisitByID(1);
		ObstetricsOfficeVisitBean oov1 = oovList.get(2);
		
		assertEquals(oov.getVisitID(), oov1.getVisitID());
		assertEquals(oov.getObstetricsInitRecordID(), oov1.getObstetricsInitRecordID());
		assertEquals(oov.getLocationID(), oov1.getLocationID());
		assertEquals(oov.getPatientMID(), oov1.getPatientMID());
		assertEquals(oov.getHcpMID(), oov1.getHcpMID());
		assertEquals(oov.getApptID(), oov1.getApptID());
		assertEquals(oov.getVisitDate(), oov1.getVisitDate());
		assertEquals(oov.getWeight(), oov1.getWeight());
		assertEquals(oov.getBloodPressure(), oov1.getBloodPressure());
		assertEquals(oov.getFetalHeartRate(), oov1.getFetalHeartRate());
		assertEquals(oov.getLowLyingPlacentaObserved(), oov1.getLowLyingPlacentaObserved());
		assertEquals(oov.getNumberOfBabies(), oov1.getNumberOfBabies());
    }
    
    public void testGetUltrasoundRecords() throws Exception {
    	gen.patient1();
    	gen.obstetricsInit1();
    	gen.ultrasoundRecords1();
    	
    	ViewObstetricOfficeVisitAction action = new ViewObstetricOfficeVisitAction(factory, loggedInMID, patientMID);
    	List<UltrasoundRecordBean> usrList = action.getUltrasoundRecords(1);
    	
    	assertEquals(1, usrList.size());
    	
    	UltrasoundRecordBean usr = usrDAO.getUltrasoundRecordByVisitID(1).get(0);
    	UltrasoundRecordBean usr1 = usrList.get(0);
    	
    	assertEquals(usr.getRecordID(), usr1.getRecordID());
		assertEquals(usr.getObstetricsOfficeVisitID(), usr1.getObstetricsOfficeVisitID());
		assertEquals(usr.getCrownRumpLength(), usr1.getCrownRumpLength());
		assertEquals(usr.getBiparietalDiameter(), usr1.getBiparietalDiameter());
		assertEquals(usr.getHeadCircumference(), usr1.getHeadCircumference());
		assertEquals(usr.getFemurLength(), usr1.getFemurLength());
		assertEquals(usr.getOccipitofrontalDiameter(), usr1.getOccipitofrontalDiameter());
		assertEquals(usr.getAbdominalCircumference(), usr1.getAbdominalCircumference());
		assertEquals(usr.getHumerusLength(), usr1.getHumerusLength());
		assertEquals(usr.getEstimatedFetalWeight(), usr1.getEstimatedFetalWeight());
		assertEquals(usr.getFileURL(), usr1.getFileURL());
		assertEquals(true, action.checkRHNotice(patientMID, new Date()));
		
		action.getNewOfficeVisit();
		action.getRecentVisits();
    }
    
    public void testIsOBGYN() throws Exception {
    	gen.patient1();
    	gen.obstetricsInit1();
    	gen.uc63();
    	gen.hcp1();
    	
    	ViewObstetricOfficeVisitAction action1 = new ViewObstetricOfficeVisitAction(factory, loggedInMID, patientMID);
    	ViewObstetricOfficeVisitAction action2 = new ViewObstetricOfficeVisitAction(factory, 9900000000L, patientMID);
    
    	try {
    		action1.isOBGYN();
    	}
    	catch (ITrustException e) {
    		fail("Test failed");
    	}
    	
    	try {
    		action2.isOBGYN();
    	}
    	catch (ITrustException e) {
    		/* Test succeeds */
    	}
    }
    
    public void testCheckCurrentObstetrics() throws Exception {
    	gen.patient1();
    	gen.obstetricsInit1();
    	gen.obstetricsOfficeVisit1();
    	gen.uc63();
    	
    	ViewObstetricOfficeVisitAction action = new ViewObstetricOfficeVisitAction(factory, loggedInMID, patientMID);
    	
    	assertEquals(false, action.checkCurrentObstetrics(2));
    }
}
