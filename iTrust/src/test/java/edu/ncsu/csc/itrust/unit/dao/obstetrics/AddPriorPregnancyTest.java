package edu.ncsu.csc.itrust.unit.dao.obstetrics;

import junit.framework.TestCase;

import edu.ncsu.csc.itrust.model.old.beans.PriorPregnancyBean;
import edu.ncsu.csc.itrust.model.old.dao.mysql.PriorPregnancyDAO;
import edu.ncsu.csc.itrust.model.old.enums.DeliveryType;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;
import edu.ncsu.csc.itrust.unit.testutils.TestDAOFactory;

public class AddPriorPregnancyTest extends TestCase {
	private TestDataGenerator gen = new TestDataGenerator();
	private PriorPregnancyDAO ppDAO = TestDAOFactory.getTestInstance().getPriorPregnancyDAO();
	
	@Override
	protected void setUp() throws Exception {
		gen.clearAllTables();
	}
	
	public void testAddEmptyPriorPregnancy() throws Exception {
		long ppID = ppDAO.addEmptyPriorPregnancy();
		assertEquals(true, ppDAO.checkPriorPregnancyExistsByID(ppID));
		assertEquals(true, ppDAO.checkPriorPregnancyExistsByObstetricRecordID(0L));
	}
	
	public void testAddPriorPregnancy() throws Exception {
		PriorPregnancyBean pp;
		
		pp = new PriorPregnancyBean();
		pp.setYearOfConception(2001);
		pp.setDaysPregnant(200);
		pp.setHoursInLabor(12.25f);
		pp.setWeightGain(5.6f);
		pp.setDeliveryTypeStr(DeliveryType.VaginalDeliveryVacuumAssist.getName());
		pp.setMultiplicity(3);
		ppDAO.addPriorPregnancy(pp);
		
		pp = ppDAO.getPriorPregnancyByID(1L);
		assertEquals(1, pp.getPriorPregnancyID());
		assertEquals(2001, pp.getYearOfConception());
		assertEquals(200, pp.getDaysPregnant());
		assertEquals(12.25f, pp.getHoursInLabor());
		assertEquals(5.6f, pp.getWeightGain());
		assertEquals("Vaginal Delivery Vacuum Assist", pp.getDeliveryType().getName());
		assertEquals(3, pp.getMultiplicity());
	}
}
