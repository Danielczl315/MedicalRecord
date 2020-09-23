package edu.ncsu.csc.itrust.unit.dao.obstetrics;

import junit.framework.TestCase;

import java.util.List;
import edu.ncsu.csc.itrust.model.old.beans.ChildBirthVisitBean;
import edu.ncsu.csc.itrust.model.old.dao.mysql.ChildBirthVisitDAO;
import edu.ncsu.csc.itrust.model.old.enums.BooleanType;
import edu.ncsu.csc.itrust.model.old.enums.DeliveryType;
import edu.ncsu.csc.itrust.unit.testutils.TestDAOFactory;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;

public class ChildBirthVisitDAOTest extends TestCase {
	private TestDataGenerator gen = new TestDataGenerator();
	private ChildBirthVisitDAO cbvDAO = TestDAOFactory.getTestInstance().getChildBirthVisitDAO();
	
	@Override
	protected void setUp() throws Exception {
		gen.clearAllTables();
	}
	
	public void testAddEmptyCBV() throws Exception {
		long visitID = cbvDAO.addEmptyChildBirthVisit();
		assertEquals(true, cbvDAO.checkChildBirthVisitExistsByVisitID(visitID));
		assertEquals(true, cbvDAO.checkChildBirthVisitExistsByInitID(0));
	}
	
	public void testAddCBV() throws Exception {
		ChildBirthVisitBean cbv = new ChildBirthVisitBean();
		
		cbv.setVisitID(1);
		cbv.setObstetricsInitRecordID(1);
		cbv.setApptID(8);
		cbv.setPreferredDeliveryMethod(DeliveryType.VaginalDeliveryVacuumAssist);
		cbv.setHasDelivered(BooleanType.True);
		cbv.setPitocinDosage(10);
		cbv.setNitrousOxideDosage(10);
		cbv.setEpiduralAnaesthesiaDosage(5);
		cbv.setMagnesiumSulfateDosage(8);
		cbv.setRhImmuneGlobulinDosage(5);
		
		cbvDAO.addChildBirthVisit(cbv);
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
	
	public void testGetChildBirthVisitByVisitID() throws Exception {
		gen.childBirthVisit1();
		
		ChildBirthVisitBean cbv = cbvDAO.getChildBirthVisitByVisitID(2);
		
		assertEquals(2, cbv.getVisitID());
		assertEquals(2, cbv.getObstetricsInitRecordID());
		assertEquals(8, cbv.getApptID());
		assertEquals(DeliveryType.VaginalDelivery, cbv.getPreferredDeliveryMethod());
		assertEquals(BooleanType.True, cbv.getHasDelivered());
		assertEquals(5, cbv.getPitocinDosage());
		assertEquals(5, cbv.getNitrousOxideDosage());
		assertEquals(2, cbv.getEpiduralAnaesthesiaDosage());
		assertEquals(6, cbv.getMagnesiumSulfateDosage());
		assertEquals(3, cbv.getRhImmuneGlobulinDosage());
	}
	
	public void testGetChildBirthVisitByInitID() throws Exception {
		gen.childBirthVisit1();
		
		List<ChildBirthVisitBean> cbvList = cbvDAO.getChildBirthVisitByInitID(2);
		
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
	
	public void testGetNextVisitID() throws Exception
	{
		assertEquals(1, cbvDAO.getNextVisitID());
	}
}
