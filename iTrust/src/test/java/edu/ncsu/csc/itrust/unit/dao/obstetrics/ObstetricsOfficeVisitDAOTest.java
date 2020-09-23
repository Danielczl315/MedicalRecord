package edu.ncsu.csc.itrust.unit.dao.obstetrics;

import junit.framework.TestCase;

import edu.ncsu.csc.itrust.model.old.beans.ObstetricsOfficeVisitBean;
import edu.ncsu.csc.itrust.model.old.dao.mysql.ObstetricsOfficeVisitDAO;
import edu.ncsu.csc.itrust.model.old.enums.BooleanType;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;
import edu.ncsu.csc.itrust.unit.testutils.TestDAOFactory;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.ArrayList;

public class ObstetricsOfficeVisitDAOTest extends TestCase {
	private TestDataGenerator gen = new TestDataGenerator();
	private ObstetricsOfficeVisitDAO oovDAO = TestDAOFactory.getTestInstance().getObstetricsOfficeVisitDAO();
	private SimpleDateFormat formatter;
	
	@Override
	protected void setUp() throws Exception {
		gen.clearAllTables();
		formatter = new SimpleDateFormat("MM/dd/yyy");
	}
	
	public void testAddOOV() throws Exception {
		ObstetricsOfficeVisitBean oov = new ObstetricsOfficeVisitBean();
		
		oov.setVisitID(1);
		oov.setObstetricsInitRecordID(1);
		oov.setLocationID(4);
		oov.setPatientMID(1);
		oov.setHcpMID(9000000012L);
		oov.setApptID(7);
		oov.setVisitDateStr("02/15/2001");
		oov.setWeight(6.2f);
		oov.setBloodPressure(1);
		oov.setFetalHeartRate(40);
		oov.setLowLyingPlacentaObservedStr("False");
		oov.setNumberOfBabies(1);
		
		oovDAO.addObstetricsOfficeVisit(oov);
		oov = oovDAO.getObstetricsOfficeVisitByID(1);
		
		assertEquals(1, oov.getVisitID());
		assertEquals(1, oov.getObstetricsInitRecordID());
		assertEquals(4, oov.getLocationID());
		assertEquals(1, oov.getPatientMID());
		assertEquals(9000000012L, oov.getHcpMID());
		assertEquals(7, oov.getApptID());
		assertEquals(formatter.parse("02/15/2001"), oov.getVisitDate());
		assertEquals(6.2f, oov.getWeight());
		assertEquals(1, oov.getBloodPressure());
		assertEquals(40, oov.getFetalHeartRate());
		assertEquals(BooleanType.False, oov.getLowLyingPlacentaObserved());
		assertEquals(1, oov.getNumberOfBabies());
	}
	
	public void testGetOOVByInitRecord() throws Exception {
		ObstetricsOfficeVisitBean oov;
		
		gen.obstetricsOfficeVisit1();
		
		oov = oovDAO.getObstetricsOfficeVisitByInitRecord(1).get(0);
		
		assertEquals(1, oov.getVisitID());
		assertEquals(1, oov.getObstetricsInitRecordID());
		assertEquals(4, oov.getLocationID());
		assertEquals(1, oov.getPatientMID());
		assertEquals(9000000012L, oov.getHcpMID());
		assertEquals(7, oov.getApptID());
		assertEquals(formatter.parse("02/15/2001"), oov.getVisitDate());
		assertEquals(6.2f, oov.getWeight());
		assertEquals(1, oov.getBloodPressure());
		assertEquals(40, oov.getFetalHeartRate());
		assertEquals(BooleanType.False, oov.getLowLyingPlacentaObserved());
		assertEquals(1, oov.getNumberOfBabies());
	}
	
	public void testEditOOV() throws Exception {
		ObstetricsOfficeVisitBean oov = new ObstetricsOfficeVisitBean();
		
		oov.setObstetricsInitRecordID(2);
		oov.setLocationID(4);
		oov.setPatientMID(1);
		oov.setHcpMID(9000000012L);
		oov.setApptID(7);
		oov.setVisitDateStr("04/20/2004");
		oov.setWeight(10.6f);
		oov.setBloodPressure(2);
		oov.setFetalHeartRate(50);
		oov.setLowLyingPlacentaObservedStr("True");
		oov.setNumberOfBabies(2);
		
		oovDAO.editObstetricsOfficeVisit(oov);
		oovDAO.getObstetricsOfficeVisitByID(1);
		
		assertEquals(2, oov.getObstetricsInitRecordID());
		assertEquals(4, oov.getLocationID());
		assertEquals(1, oov.getPatientMID());
		assertEquals(9000000012L, oov.getHcpMID());
		assertEquals(7, oov.getApptID());
		assertEquals(formatter.parse("04/20/2004"), oov.getVisitDate());
		assertEquals(10.6f, oov.getWeight());
		assertEquals(2, oov.getBloodPressure());
		assertEquals(50, oov.getFetalHeartRate());
		assertEquals(BooleanType.True, oov.getLowLyingPlacentaObserved());
		assertEquals(2, oov.getNumberOfBabies());
	}
	
	public void testSortOOV() throws Exception {
		List<ObstetricsOfficeVisitBean> oovList = new ArrayList<ObstetricsOfficeVisitBean>();
		ObstetricsOfficeVisitBean oov1 = new ObstetricsOfficeVisitBean();
		ObstetricsOfficeVisitBean oov2 = new ObstetricsOfficeVisitBean();
		
		oov1.setVisitDateStr("11/11/2018");
		oov2.setVisitDateStr("11/12/2018");
		oovList.add(oov1);
		oovList.add(oov2);
		
		oovList = oovDAO.sortByVisitDate(oovList);
		
		assertEquals(formatter.parse("11/12/2018"), oovList.get(0).getVisitDate());
		assertEquals(formatter.parse("11/11/2018"), oovList.get(1).getVisitDate());
	}
	
	public void testGetByMID() throws Exception {
		ObstetricsOfficeVisitBean oov;
		
		gen.obstetricsOfficeVisit1();
		
		oov = oovDAO.getObstetricsOfficeVisitByMID(1).get(0);
		
		assertEquals(1, oov.getVisitID());
		assertEquals(1, oov.getObstetricsInitRecordID());
		assertEquals(4, oov.getLocationID());
		assertEquals(1, oov.getPatientMID());
		assertEquals(9000000012L, oov.getHcpMID());
		assertEquals(7, oov.getApptID());
		assertEquals(formatter.parse("02/15/2001"), oov.getVisitDate());
		assertEquals(6.2f, oov.getWeight());
		assertEquals(1, oov.getBloodPressure());
		assertEquals(40, oov.getFetalHeartRate());
		assertEquals(BooleanType.False, oov.getLowLyingPlacentaObserved());
		assertEquals(1, oov.getNumberOfBabies());
	}
	
	public void testDelete() throws Exception {
		gen.obstetricsOfficeVisit1();
		
		assertEquals(true, oovDAO.deleteObstetricsOfficeVisit(1));
	}
}
