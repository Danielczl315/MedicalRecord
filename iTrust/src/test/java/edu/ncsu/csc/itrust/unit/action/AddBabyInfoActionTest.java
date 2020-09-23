package edu.ncsu.csc.itrust.unit.action;

import java.util.List;

import edu.ncsu.csc.itrust.action.AddBabyInfoAction;
import edu.ncsu.csc.itrust.model.old.beans.PatientBean;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;
import edu.ncsu.csc.itrust.model.old.dao.mysql.PatientDAO;
import edu.ncsu.csc.itrust.model.old.dao.mysql.PersonnelDAO;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;
import edu.ncsu.csc.itrust.unit.testutils.TestDAOFactory;
import junit.framework.TestCase;

public class AddBabyInfoActionTest extends TestCase
{
	private DAOFactory factory;
	private TestDataGenerator gen;
	
	private PatientDAO patientDAO;
	private PersonnelDAO personnelDAO;
	
	private long loggedInMID = 9000000012L;	/* Kathryn Evans */
    private long patientMID = 1L;			/* Random Person */
    
    @Override
    protected void setUp() throws Exception
    {
    	factory = TestDAOFactory.getTestInstance();
    	patientDAO = factory.getPatientDAO();
    	personnelDAO = factory.getPersonnelDAO();
    	
    	gen = new TestDataGenerator();
    	gen.clearAllTables();
    }
    
    public void testActionConstructor() throws Exception
    {
    	gen.uc63();		/* Generate OB/GYN HCP */
    	gen.patient1();	/* Generate patient 1 */
    	AddBabyInfoAction action = new AddBabyInfoAction(factory, loggedInMID);
    	
    	/* If throws exception, failed */
    }

    public void testGetBabiesBymotherMID() throws Exception
    {
    	gen.uc63();		/* Generate OB/GYN HCP */
    	gen.patient1();
    	gen.obstetricsInit1();
    	gen.childBirthVisit1();
    	gen.childBirthVisit3();
    	gen.babyDeliveryInfo1();
    	AddBabyInfoAction action = new AddBabyInfoAction(factory, loggedInMID);
    	
    	List<PatientBean> list = action.getBabiesBymotherMID(patientMID);
    	assertEquals(0, list.size());
    	
    	List<PatientBean> list2 = action.getBabiesBymotherMID(-1);
    	assertEquals(0, list2.size());
    }
    
    public void testUpdateBabyInfo() throws Exception
    {
    	gen.uc63();		/* Generate OB/GYN HCP */
    	gen.patient1();
    	gen.obstetricsInit1();
    	gen.babyDeliveryInfo1();
    	long babyMID = patientDAO.addEmptyPatient();
    	AddBabyInfoAction action = new AddBabyInfoAction(factory, loggedInMID);
    	action.updateBabyInfo(babyMID, patientMID, "ABC", "DEF", "12345");
    }
}
