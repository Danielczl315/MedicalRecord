package edu.ncsu.csc.itrust.unit.action;

import java.util.List;

import edu.ncsu.csc.itrust.action.LaborDeliveryReportAction;
import edu.ncsu.csc.itrust.exception.ITrustException;
import edu.ncsu.csc.itrust.model.old.beans.AllergyBean;
import edu.ncsu.csc.itrust.model.old.beans.PriorPregnancyBean;
import edu.ncsu.csc.itrust.model.old.beans.ObstetricsOfficeVisitBean;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;
import edu.ncsu.csc.itrust.model.old.enums.DeliveryType;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;
import edu.ncsu.csc.itrust.unit.testutils.TestDAOFactory;
import junit.framework.TestCase;

public class LaborDeliveryReportActionTest extends TestCase {
	private DAOFactory factory;
	private TestDataGenerator gen;
	
	private long loggedInMID = 9000000012L;	/* Kathryn Evans */
    private long patientMID = 1L;			/* Random Person */
    
    @Override
    protected void setUp() throws Exception {
    	factory = TestDAOFactory.getTestInstance();
    	gen = new TestDataGenerator();
    	gen.clearAllTables();
    }
    
    /* Mainly tests checkEligibility */
    public void testActionConstructor() throws Exception {
    	gen.patient1();	/* Generate patient 1 */
    	gen.obstetricsInit1();
    	gen.obstetricsOfficeVisit1();
    	LaborDeliveryReportAction action = new LaborDeliveryReportAction(factory, loggedInMID, patientMID);
    	
    	/* If throws exception, failed */
    }
    
    public void testActionConstructorIneligible() throws Exception {
    	try {
    		gen.patient2();	/* Generate patient 2 */
    		gen.obstetricsInit2();
    		LaborDeliveryReportAction action = new LaborDeliveryReportAction(factory, loggedInMID, 2);
    	}
	    catch (ITrustException e) {
	    	assertEquals("Patient is not eligible for obstetrics care.", e.getMessage());
	    }
    }
    
    public void testActionGetPriorPregnancies() throws Exception {
    	gen.patient1();	/* Generate patient 1 */
    	gen.obstetricsInit1();
    	gen.obstetricsOfficeVisit1();
    	gen.priorPregnancy1();
    	LaborDeliveryReportAction action = new LaborDeliveryReportAction(factory, loggedInMID, patientMID);
    	
    	List<PriorPregnancyBean> list = action.getPriorPregnancies();
    	assertEquals(2, list.size());
    }

    public void testActionGetBloodType() throws Exception {
    	gen.patient1();	/* Generate patient 1 */
    	gen.obstetricsInit1();
    	gen.obstetricsOfficeVisit1();
    	gen.priorPregnancy1();
    	LaborDeliveryReportAction action = new LaborDeliveryReportAction(factory, loggedInMID, patientMID);
    	
    	assertEquals("AB+", action.getBloodType());
    }
    
    public void testActionGetEstimatedDeliveryDate() throws Exception {
    	gen.patient1();	/* Generate patient 1 */
    	gen.obstetricsInit1();
    	gen.obstetricsOfficeVisit1();
    	gen.priorPregnancy1();
    	LaborDeliveryReportAction action = new LaborDeliveryReportAction(factory, loggedInMID, patientMID);
    	
    	assertEquals("10/20/2018", action.getEstimatedDeliveryDate());
    }
    
    public void testActionGetAllObstetricsOfficeVisits() throws Exception {
    	gen.patient1();	/* Generate patient 1 */
    	gen.obstetricsInit1();
    	gen.obstetricsOfficeVisit1();
    	gen.priorPregnancy1();
    	LaborDeliveryReportAction action = new LaborDeliveryReportAction(factory, loggedInMID, patientMID);
    	
    	List<ObstetricsOfficeVisitBean> list = action.getAllObstetricsOfficeVisits();
    	assertEquals(1, list.size());
    }
    
    public void testActionHasRHFlag() throws Exception {
    	gen.patient1();	/* Generate patient 1 */
    	gen.obstetricsInit1();
    	gen.obstetricsOfficeVisit1();
    	gen.priorPregnancy1();
    	LaborDeliveryReportAction action = new LaborDeliveryReportAction(factory, loggedInMID, patientMID);
    	
    	assertEquals("True", action.hasRHFlag());
    }
    
    public void testActionGetAllergies() throws Exception {
    	gen.patient1();	/* Generate patient 1 */
    	gen.obstetricsInit1();
    	gen.obstetricsOfficeVisit1();
    	gen.priorPregnancy1();
    	LaborDeliveryReportAction action = new LaborDeliveryReportAction(factory, loggedInMID, patientMID);
    	
    	List<AllergyBean> list = action.getAllergies();
    	assertEquals(0, list.size());
    }
    
    public void testActionHasAdvancedMaternalAge() throws Exception {
    	gen.patient1();	/* Generate patient 1 */
    	gen.obstetricsInit1();
    	gen.obstetricsOfficeVisit1();
    	gen.priorPregnancy1();
    	LaborDeliveryReportAction action = new LaborDeliveryReportAction(factory, loggedInMID, patientMID);
    	
    	assertEquals("True, 68 years old", action.hasAdvancedMaternalAge());
    }
    
    public void testActionHasLyingPlacenta() throws Exception {
    	gen.patient1();	/* Generate patient 1 */
    	gen.obstetricsInit1();
    	gen.obstetricsOfficeVisit1();
    	gen.priorPregnancy1();
    	LaborDeliveryReportAction action = new LaborDeliveryReportAction(factory, loggedInMID, patientMID);
    	
    	assertEquals("True", action.hasLyingPlacenta());
    }
    
    public void testActionMultiples() throws Exception {
    	gen.patient1();	/* Generate patient 1 */
    	gen.obstetricsInit1();
    	gen.obstetricsOfficeVisit1();
    	gen.priorPregnancy1();
    	LaborDeliveryReportAction action = new LaborDeliveryReportAction(factory, loggedInMID, patientMID);
    	
    	assertEquals("True, 2 babies expected", action.mulitples());
    }
    
    public void testActionAtypicalWeightChange() throws Exception {
    	gen.patient1();	/* Generate patient 1 */
    	gen.obstetricsInit1();
    	gen.obstetricsOfficeVisit1();
    	gen.priorPregnancy1();
    	LaborDeliveryReportAction action = new LaborDeliveryReportAction(factory, loggedInMID, patientMID);
    	
    	assertEquals("True, weight change of 10.6 lbs", action.atypicalWeightChange());
    }
    
    public void testActionAbnormalFetalHR() throws Exception {
    	gen.patient1();	/* Generate patient 1 */
    	gen.obstetricsInit1();
    	gen.obstetricsOfficeVisit1();
    	gen.priorPregnancy1();
    	LaborDeliveryReportAction action = new LaborDeliveryReportAction(factory, loggedInMID, patientMID);
    	
    	assertEquals("True, fetal heart rate is at 50", action.abnormalFetalHR());
    }
}
