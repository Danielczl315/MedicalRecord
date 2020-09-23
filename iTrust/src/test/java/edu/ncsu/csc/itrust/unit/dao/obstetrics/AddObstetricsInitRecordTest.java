package edu.ncsu.csc.itrust.unit.dao.obstetrics;

import edu.ncsu.csc.itrust.model.old.beans.ObstetricsInitBean;
import edu.ncsu.csc.itrust.model.old.dao.mysql.ObstetricsInitDAO;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;
import edu.ncsu.csc.itrust.unit.testutils.TestDAOFactory;
import junit.framework.TestCase;

public class AddObstetricsInitRecordTest extends TestCase {
	private TestDataGenerator gen = new TestDataGenerator();
	private ObstetricsInitDAO obInitDAO = TestDAOFactory.getTestInstance().getObstetricsInitDAO();
	
	@Override
	protected void setUp() throws Exception {
		gen.clearAllTables();
	}
	
	public void testAddEmptyObstetricsInit() throws Exception {
		long recordID = obInitDAO.addEmptyObstetricsInit();
		assertEquals(true, obInitDAO.checkObstetricsInitExistsByID(recordID));
		assertEquals(true, obInitDAO.checkObstetricsInitExistsByMID(0L));
	}
	
	public void testAddObstetricsInit() throws Exception {
		ObstetricsInitBean obInit;

		obInit = new ObstetricsInitBean();
		obInit.setPatientMID(1L);
		obInit.setInitDate("01/30/2001", "MM/dd/yyyy");
		obInit.setLastMenstrualPeriod("12/25/2000", "MM/dd/yyyy");
		obInitDAO.addObstetricsInit(obInit);
		obInit = obInitDAO.getObstetricsInitByMID(1L).get(0);
		assertEquals(1L, obInit.getPatientMID());
		assertEquals("01/30/2001", obInit.getInitDateMMDDYYYY());
		assertEquals("12/25/2000", obInit.getLastMenstrualPeriodMMDDYYYY());
	}
}
