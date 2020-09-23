package edu.ncsu.csc.itrust.unit.action;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;

import edu.ncsu.csc.itrust.action.ChildBirthVisitAction;

import edu.ncsu.csc.itrust.model.old.dao.mysql.ChildBirthVisitDAO;
import edu.ncsu.csc.itrust.model.old.dao.mysql.PatientDAO;
import edu.ncsu.csc.itrust.model.old.enums.BooleanType;
import edu.ncsu.csc.itrust.model.old.enums.DeliveryType;
import edu.ncsu.csc.itrust.model.old.enums.Gender;
import edu.ncsu.csc.itrust.model.old.beans.BabyDeliveryInfoBean;
import edu.ncsu.csc.itrust.model.old.beans.ChildBirthVisitBean;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;
import edu.ncsu.csc.itrust.model.old.dao.mysql.BabyDeliveryInfoDAO;

import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;
import edu.ncsu.csc.itrust.unit.testutils.TestDAOFactory;

public class ChildBirthActionTest extends TestCase {
	private DAOFactory factory;
	
	ChildBirthVisitDAO cbvDAO;
	BabyDeliveryInfoDAO bdiDAO;
	PatientDAO patientDAO;
	
	private long loggedInMID = 9000000012L;	/* Kathryn Evans */
    private long patientMID = 1L;			/* Random Person */
    
    private TestDataGenerator gen;
    
    @Override
    protected void setUp() throws Exception {
    	factory = TestDAOFactory.getTestInstance();
    	cbvDAO = factory.getChildBirthVisitDAO();
    	bdiDAO = factory.getBabyDeliveryInfoDAO();
    	patientDAO = factory.getPatientDAO();
    	
    	gen = new TestDataGenerator();
    	gen.clearAllTables();
    }
    
    public void testGetChildBirthVisitByVisitID() throws Exception {
    	/* Generate ChildBirthVisit records */
    	gen.childBirthVisit1();
    	
    	/* Get record by visit ID and check results */
    	ChildBirthVisitAction action = new ChildBirthVisitAction(factory, loggedInMID, patientMID);
    	ChildBirthVisitBean cbv = action.getChildBirthVisitRecordByVisitID(1);
    	
    	action.setEmergency(false);
    	action.setOutsideBirth(false);
    	assertEquals(false, action.getIsEmergency());
    	assertEquals(false, action.getOutsideBirth());
    	
    	assertEquals(1, cbv.getVisitID());
		assertEquals(1, cbv.getObstetricsInitRecordID());
		assertEquals(8, cbv.getApptID());
		assertEquals(DeliveryType.VaginalDeliveryVacuumAssist, cbv.getPreferredDeliveryMethod());
		assertEquals(BooleanType.True, cbv.getHasDelivered());
		assertEquals(10, cbv.getPitocinDosage());
		assertEquals(10, cbv.getNitrousOxideDosage());
		assertEquals(5, cbv.getEpiduralAnaesthesiaDosage());
		assertEquals(8, cbv.getMagnesiumSulfateDosage());
		assertEquals(5, cbv.getRhImmuneGlobulinDosage());
    }
    
    public void testGetChildBirthVisitByInitID() throws Exception {
    	/* Generate ChildBirthVisit records */
    	gen.childBirthVisit1();
    	
    	/* Get record by obstetrics initialization record ID and check results */
    	ChildBirthVisitAction action = new ChildBirthVisitAction(factory, loggedInMID, patientMID);
    	List<ChildBirthVisitBean> cbvList = action.getChildBirthVisitRecordByInitID(2);
    	
    	assertEquals(1, cbvList.size());
		assertEquals(2, cbvList.get(0).getVisitID());
		assertEquals(2, cbvList.get(0).getObstetricsInitRecordID());
		assertEquals(8, cbvList.get(0).getApptID());
		assertEquals(DeliveryType.VaginalDelivery, cbvList.get(0).getPreferredDeliveryMethod());
		assertEquals(BooleanType.True, cbvList.get(0).getHasDelivered());
		assertEquals(5, cbvList.get(0).getPitocinDosage());
		assertEquals(5, cbvList.get(0).getNitrousOxideDosage());
		assertEquals(2, cbvList.get(0).getEpiduralAnaesthesiaDosage());
		assertEquals(6, cbvList.get(0).getMagnesiumSulfateDosage());
		assertEquals(3, cbvList.get(0).getRhImmuneGlobulinDosage());
    }
    
    public void testGetBabyDeliveryInfoByRecordID() throws Exception {
    	/* Generate BabyDeliveryInfo records */
    	gen.babyDeliveryInfo1();
    	
    	/* Get record by record ID and check correctness */
    	ChildBirthVisitAction action = new ChildBirthVisitAction(factory, loggedInMID, patientMID);
    	BabyDeliveryInfoBean bdi = action.getBabyDeliveryInfoByRecordID(1);
    	
    	assertEquals(1, bdi.getRecordID());
		assertEquals(1, bdi.getChildBirthVisitID());
		assertEquals(Gender.Male, bdi.getGender());
		assertEquals("11/03/2001", bdi.getDateOfBirthStr());
		assertEquals(DeliveryType.VaginalDeliveryVacuumAssist, bdi.getDeliveryMethod());
		assertEquals(BooleanType.False, bdi.getIsEstimated());
    }
    
    public void testGetBabyDeliveryInfoByVisitID() throws Exception {
    	/* Generate BabyDeliveryInfo records (3 records associated with visitID = 1) */
    	gen.babyDeliveryInfo1();
    	
    	/* Get records by visit ID and check correctness */
    	ChildBirthVisitAction action = new ChildBirthVisitAction(factory, loggedInMID, patientMID);
    	List<BabyDeliveryInfoBean> bdiList = action.getBabyDeliveryInfoByVisitID(1);
    	
    	assertEquals(3, bdiList.size());
		
		assertEquals(1, bdiList.get(0).getRecordID());
		assertEquals(1, bdiList.get(0).getChildBirthVisitID());
		assertEquals(Gender.Male, bdiList.get(0).getGender());
		assertEquals("11/03/2001", bdiList.get(0).getDateOfBirthStr());
		assertEquals(DeliveryType.VaginalDeliveryVacuumAssist, bdiList.get(0).getDeliveryMethod());
		assertEquals(BooleanType.False, bdiList.get(0).getIsEstimated());
		
		assertEquals(2, bdiList.get(1).getRecordID());
		assertEquals(1, bdiList.get(1).getChildBirthVisitID());
		assertEquals(Gender.Male, bdiList.get(1).getGender());
		assertEquals("11/03/2001", bdiList.get(1).getDateOfBirthStr());
		assertEquals(DeliveryType.VaginalDeliveryVacuumAssist, bdiList.get(1).getDeliveryMethod());
		assertEquals(BooleanType.False, bdiList.get(1).getIsEstimated());
		
		assertEquals(3, bdiList.get(2).getRecordID());
		assertEquals(1, bdiList.get(2).getChildBirthVisitID());
		assertEquals(Gender.Female, bdiList.get(2).getGender());
		assertEquals("11/03/2001", bdiList.get(2).getDateOfBirthStr());
		assertEquals(DeliveryType.VaginalDeliveryVacuumAssist, bdiList.get(2).getDeliveryMethod());
		assertEquals(BooleanType.False, bdiList.get(2).getIsEstimated());
    }

    public void testAddChildBirthVisitRecord() throws Exception {
    	gen.clearAllTables();

    	/* Add new ChildBirthVisit record to database */
    	ChildBirthVisitBean cbv = new ChildBirthVisitBean();
    	cbv.setVisitID(1);
		cbv.setObstetricsInitRecordID(1);
		cbv.setApptID(8);
		cbv.setPreferredDeliveryMethod(DeliveryType.VaginalDeliveryVacuumAssist);
		cbv.setHasDelivered(BooleanType.True);

		ChildBirthVisitAction action = new ChildBirthVisitAction(factory, loggedInMID, patientMID);
		action.addChildBirthVisitRecord(cbv);
		action.exists(1);
		action.hasDelivered(1);

		/* Get record from database using DAO and check results */
		cbv = cbvDAO.getChildBirthVisitByVisitID(1);
		assertEquals(1, cbv.getVisitID());
		assertEquals(1, cbv.getObstetricsInitRecordID());
		assertEquals(8, cbv.getApptID());
		assertEquals(DeliveryType.VaginalDeliveryVacuumAssist, cbv.getPreferredDeliveryMethod());
		assertEquals(BooleanType.True, cbv.getHasDelivered());
    }

    public void testAddChildBirthDrugs() throws Exception {
    	gen.clearAllTables();

    	/* Add new ChildBirthVisit record to database */
    	ChildBirthVisitBean cbv = new ChildBirthVisitBean();
    	cbv.setVisitID(1);
		cbv.setObstetricsInitRecordID(1);
		cbv.setApptID(8);
		cbv.setPreferredDeliveryMethod(DeliveryType.VaginalDeliveryVacuumAssist);
		cbv.setHasDelivered(BooleanType.True);

		ChildBirthVisitAction action = new ChildBirthVisitAction(factory, loggedInMID, patientMID);
		action.addChildBirthVisitRecord(cbv);

		/* Add drugs info to new record */
		cbv.setPitocinDosage(10);
		cbv.setNitrousOxideDosage(10);
		cbv.setEpiduralAnaesthesiaDosage(5);
		cbv.setMagnesiumSulfateDosage(8);
		cbv.setRhImmuneGlobulinDosage(5);

		action.addChildBirthDrugs(cbv);

		/* Get record from database using DAO and check results */
		cbv = cbvDAO.getChildBirthVisitByVisitID(1);
		assertEquals(1, cbv.getVisitID());
		assertEquals(1, cbv.getObstetricsInitRecordID());
		assertEquals(8, cbv.getApptID());
		assertEquals(DeliveryType.VaginalDeliveryVacuumAssist, cbv.getPreferredDeliveryMethod());
		assertEquals(BooleanType.True, cbv.getHasDelivered());
		assertEquals(10, cbv.getPitocinDosage());
		assertEquals(10, cbv.getNitrousOxideDosage());
		assertEquals(5, cbv.getEpiduralAnaesthesiaDosage());
		assertEquals(8, cbv.getMagnesiumSulfateDosage());
		assertEquals(5, cbv.getRhImmuneGlobulinDosage());
    }

    public void testAddBabyDeliveryInfo() throws Exception {
    	gen.clearAllTables();

    	/* Add new BabyDeliveryInfo record to database */
    	BabyDeliveryInfoBean bdi = new BabyDeliveryInfoBean();
		bdi.setRecordID(1);
		bdi.setChildBirthVisitID(1);
		bdi.setGenderStr("Male");
		bdi.setDateOfBirthStr("11/03/2001");
		bdi.setDeliveryMethodStr("Vaginal Delivery Vacuum Assist");
		bdi.setIsEstimatedStr("False");

		ChildBirthVisitAction action = new ChildBirthVisitAction(factory, loggedInMID, patientMID);
		action.addBabyDeliveryInfo(bdi);

		/* Get record from database using DAO and check results */
		bdi = bdiDAO.getBabyDeliveryInfoByRecordID(1);
		assertEquals(1, bdi.getRecordID());
		assertEquals(1, bdi.getChildBirthVisitID());
		assertEquals(Gender.Male, bdi.getGender());
		assertEquals("11/03/2001", bdi.getDateOfBirthStr());
		assertEquals(DeliveryType.VaginalDeliveryVacuumAssist, bdi.getDeliveryMethod());
		assertEquals(BooleanType.False, bdi.getIsEstimated());
    }

    /* Also tests checkEligibility */
    public void testEditChildBirthVisit() throws Exception {
    	/* Generate ChildBirthVisit and BabyDeliveryInfo records, as well as OB/GYN HCP */
    	gen.childBirthVisit1();
    	gen.babyDeliveryInfo1();
    	gen.uc63();

    	/* Edit records */
    	ChildBirthVisitBean cbv = cbvDAO.getChildBirthVisitByVisitID(1);
    	List<BabyDeliveryInfoBean> bdiList = new ArrayList<BabyDeliveryInfoBean>();
    	for (int i = 1; i <= 3; i++) {
    		bdiList.add(bdiDAO.getBabyDeliveryInfoByRecordID(i));
    	}

    	cbv.setVisitID(1);
		cbv.setObstetricsInitRecordID(1);
		cbv.setApptID(8);
		cbv.setPreferredDeliveryMethod(DeliveryType.VaginalDelivery);
		cbv.setHasDelivered(BooleanType.False);
		cbv.setPitocinDosage(11);
		cbv.setNitrousOxideDosage(11);
		cbv.setEpiduralAnaesthesiaDosage(6);
		cbv.setMagnesiumSulfateDosage(9);
		cbv.setRhImmuneGlobulinDosage(6);

		bdiList.get(0).setRecordID(1);
		bdiList.get(0).setChildBirthVisitID(1);
		bdiList.get(0).setGenderStr("Female");
		bdiList.get(0).setDateOfBirthStr("11/04/2001");
		bdiList.get(0).setDeliveryMethodStr("Vaginal Delivery");
		bdiList.get(0).setIsEstimatedStr("True");

		bdiList.get(1).setRecordID(2);
		bdiList.get(1).setChildBirthVisitID(1);
		bdiList.get(1).setGenderStr("Female");
		bdiList.get(1).setDateOfBirthStr("11/04/2001");
		bdiList.get(1).setDeliveryMethodStr("Vaginal Delivery");
		bdiList.get(1).setIsEstimatedStr("True");

		bdiList.get(2).setRecordID(3);
		bdiList.get(2).setChildBirthVisitID(1);
		bdiList.get(2).setGenderStr("Female");
		bdiList.get(2).setDateOfBirthStr("11/04/2001");
		bdiList.get(2).setDeliveryMethodStr("Vaginal Delivery");
		bdiList.get(2).setIsEstimatedStr("True");

		ChildBirthVisitAction action = new ChildBirthVisitAction(factory, loggedInMID, patientMID);
		action.editChildBirthVisitRecord(cbv, bdiList);

		/* Get record from database using DAOs and check results */
		cbv = cbvDAO.getChildBirthVisitByVisitID(1);
		bdiList = bdiDAO.getBabyDeliveryInfoByVisitID(1);

		assertEquals(1, cbv.getVisitID());
		assertEquals(1, cbv.getObstetricsInitRecordID());
		assertEquals(8, cbv.getApptID());
		assertEquals(DeliveryType.VaginalDelivery, cbv.getPreferredDeliveryMethod());
		assertEquals(BooleanType.False, cbv.getHasDelivered());
		assertEquals(11, cbv.getPitocinDosage());
		assertEquals(11, cbv.getNitrousOxideDosage());
		assertEquals(6, cbv.getEpiduralAnaesthesiaDosage());
		assertEquals(9, cbv.getMagnesiumSulfateDosage());
		assertEquals(6, cbv.getRhImmuneGlobulinDosage());
    }
}
