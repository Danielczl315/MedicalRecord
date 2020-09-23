package edu.ncsu.csc.itrust.unit.dao.obstetrics;

import edu.ncsu.csc.itrust.model.old.beans.ObstetricsInitBean;
import edu.ncsu.csc.itrust.model.old.beans.PriorPregnancyBean;
import edu.ncsu.csc.itrust.model.old.dao.mysql.ObstetricsInitDAO;
import edu.ncsu.csc.itrust.model.old.dao.mysql.PriorPregnancyDAO;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;
import edu.ncsu.csc.itrust.unit.testutils.TestDAOFactory;
import junit.framework.TestCase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class GetPriorPregnanciesTest extends TestCase {
	private TestDataGenerator gen = new TestDataGenerator();
	private PriorPregnancyDAO ppDAO = TestDAOFactory.getTestInstance().getPriorPregnancyDAO();
	
	@Override
	protected void setUp() throws Exception {
		gen.clearAllTables();
	}
	
	public void testGetPriorPregnancyByObstetricRecordID() throws Exception {
		PriorPregnancyBean pp1;

		gen.priorPregnancy1();
		List<PriorPregnancyBean> ppList = ppDAO.getPriorPregnancyByObstetricRecordID(1L);
		pp1 = ppList.get(0);

		assertEquals(1, pp1.getPriorPregnancyID());
		assertEquals(2001, pp1.getYearOfConception());
		assertEquals(200, pp1.getDaysPregnant());
		assertEquals(12.25f, pp1.getHoursInLabor());
		assertEquals(5.6f, pp1.getWeightGain());
		assertEquals("Vaginal Delivery Vacuum Assist", pp1.getDeliveryType().getName());
		assertEquals(3, pp1.getMultiplicity());
	}

	public void testGetPriorPregnancyByObstetricRecordIDList() throws Exception {
		gen.obstetricsInit1();
		gen.priorPregnancy1();
		
		ObstetricsInitDAO obDAO = TestDAOFactory.getTestInstance().getObstetricsInitDAO();

		Date obInitDate = new SimpleDateFormat("MM/dd/yyyy").parse("01/31/2001");
		List<ObstetricsInitBean> obstetricsInitRecords = obDAO.getObstetricsInitByMID(1L);

		List<PriorPregnancyBean> ppList = ppDAO.getPriorPregnancyByObstetricRecordIDList(obstetricsInitRecords, obInitDate);
		assertEquals(1, ppList.size());
		PriorPregnancyBean pp1 = ppList.get(0);

		assertEquals(1, pp1.getPriorPregnancyID());
		assertEquals(2001, pp1.getYearOfConception());
		assertEquals(200, pp1.getDaysPregnant());
		assertEquals(12.25f, pp1.getHoursInLabor());
		assertEquals(5.6f, pp1.getWeightGain());
		assertEquals("Vaginal Delivery Vacuum Assist", pp1.getDeliveryType().getName());
		assertEquals(3, pp1.getMultiplicity());
	}
	
	public void testGetPriorPregnancyByID() throws Exception {
		gen.priorPregnancy1();
		
		PriorPregnancyBean pp = ppDAO.getPriorPregnancyByID(1L);
		assertEquals(1, pp.getPriorPregnancyID());
		assertEquals(2001, pp.getYearOfConception());
		assertEquals(200, pp.getDaysPregnant());
		assertEquals(12.25f, pp.getHoursInLabor());
		assertEquals(5.6f, pp.getWeightGain());
		assertEquals("Vaginal Delivery Vacuum Assist", pp.getDeliveryType().getName());
		assertEquals(3, pp.getMultiplicity());
	}
	
	public void testDeletePriorPregnancy() throws Exception {
		gen.priorPregnancy1();
		
		PriorPregnancyBean pp = ppDAO.getPriorPregnancyByID(1L);
		ppDAO.deletePriorPregnancy(1);
		pp = ppDAO.getPriorPregnancyByID(1);
		
		assertEquals(null, pp);
	}
}
