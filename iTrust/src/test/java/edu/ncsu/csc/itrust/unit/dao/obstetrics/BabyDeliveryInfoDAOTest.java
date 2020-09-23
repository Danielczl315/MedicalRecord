package edu.ncsu.csc.itrust.unit.dao.obstetrics;

import junit.framework.TestCase;

import java.util.List;
import java.text.SimpleDateFormat;
import edu.ncsu.csc.itrust.model.old.beans.BabyDeliveryInfoBean;
import edu.ncsu.csc.itrust.model.old.dao.mysql.BabyDeliveryInfoDAO;
import edu.ncsu.csc.itrust.model.old.enums.BooleanType;
import edu.ncsu.csc.itrust.model.old.enums.DeliveryType;
import edu.ncsu.csc.itrust.model.old.enums.Gender;
import edu.ncsu.csc.itrust.unit.testutils.TestDAOFactory;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;

public class BabyDeliveryInfoDAOTest extends TestCase {
	private TestDataGenerator gen = new TestDataGenerator();
	private BabyDeliveryInfoDAO bdiDAO = TestDAOFactory.getTestInstance().getBabyDeliveryInfoDAO();
	private SimpleDateFormat formatter;
	
	@Override
	protected void setUp() throws Exception {
		gen.clearAllTables();
		formatter = new SimpleDateFormat("MM/dd/yyyy");
	}
	
	public void testAddEmptyBabyDeliveryInfo() throws Exception {
		long recordID = bdiDAO.addEmptyBabyDeliveryInfo();
		assertEquals(true, bdiDAO.checkBabyDeliveryInfoExistsByRecordID(recordID));
		assertEquals(true, bdiDAO.checkBabyDeliveryInfExistsByVisitID(0));
	}
	
	public void testAddBabyDeliveryInfo() throws Exception {
		BabyDeliveryInfoBean bdi = new BabyDeliveryInfoBean();
		
		bdi.setRecordID(1);
		bdi.setChildBirthVisitID(1);
		bdi.setGenderStr("Male");
		bdi.setDateOfBirthStr("11/03/2001");
		bdi.setDeliveryMethodStr("Vaginal Delivery Vacuum Assist");
		bdi.setIsEstimatedStr("False");
		
		bdiDAO.addBabyDeliveryInfo(bdi);
		bdi = bdiDAO.getBabyDeliveryInfoByRecordID(1);
		
		assertEquals(1, bdi.getRecordID());
		assertEquals(1, bdi.getChildBirthVisitID());
		assertEquals(Gender.Male, bdi.getGender());
		assertEquals(formatter.parse("11/03/2001"), bdi.getDateOfBirth());
		assertEquals(DeliveryType.VaginalDeliveryVacuumAssist, bdi.getDeliveryMethod());
		assertEquals(BooleanType.False, bdi.getIsEstimated());
	}
	
	public void testGetBabyDeliveryInfoByRecordID() throws Exception {
		gen.babyDeliveryInfo1();
		
		BabyDeliveryInfoBean bdi = bdiDAO.getBabyDeliveryInfoByRecordID(1);
		
		assertEquals(1, bdi.getRecordID());
		assertEquals(1, bdi.getChildBirthVisitID());
		assertEquals(Gender.Male, bdi.getGender());
		assertEquals(formatter.parse("11/03/2001"), bdi.getDateOfBirth());
		assertEquals(DeliveryType.VaginalDeliveryVacuumAssist, bdi.getDeliveryMethod());
		assertEquals(BooleanType.False, bdi.getIsEstimated());
	}
	
	public void testGetBabyDeliveryInfoByVisitID() throws Exception {
		gen.babyDeliveryInfo1();
		
		List<BabyDeliveryInfoBean> bdiList = bdiDAO.getBabyDeliveryInfoByVisitID(1);
		
		assertEquals(3, bdiList.size());
		
		assertEquals(1, bdiList.get(0).getRecordID());
		assertEquals(1, bdiList.get(0).getChildBirthVisitID());
		assertEquals(Gender.Male, bdiList.get(0).getGender());
		assertEquals(formatter.parse("11/03/2001"), bdiList.get(0).getDateOfBirth());
		assertEquals(DeliveryType.VaginalDeliveryVacuumAssist, bdiList.get(0).getDeliveryMethod());
		assertEquals(BooleanType.False, bdiList.get(0).getIsEstimated());
		
		assertEquals(2, bdiList.get(1).getRecordID());
		assertEquals(1, bdiList.get(1).getChildBirthVisitID());
		assertEquals(Gender.Male, bdiList.get(1).getGender());
		assertEquals(formatter.parse("11/03/2001"), bdiList.get(1).getDateOfBirth());
		assertEquals(DeliveryType.VaginalDeliveryVacuumAssist, bdiList.get(1).getDeliveryMethod());
		assertEquals(BooleanType.False, bdiList.get(1).getIsEstimated());
		
		assertEquals(3, bdiList.get(2).getRecordID());
		assertEquals(1, bdiList.get(2).getChildBirthVisitID());
		assertEquals(Gender.Female, bdiList.get(2).getGender());
		assertEquals(formatter.parse("11/03/2001"), bdiList.get(2).getDateOfBirth());
		assertEquals(DeliveryType.VaginalDeliveryVacuumAssist, bdiList.get(2).getDeliveryMethod());
		assertEquals(BooleanType.False, bdiList.get(2).getIsEstimated());
	}
	
	public void testDelete() throws Exception {
		gen.babyDeliveryInfo1();
		
		assertEquals(true, bdiDAO.deleteBayDeliveryInfo(1));
	}
}
