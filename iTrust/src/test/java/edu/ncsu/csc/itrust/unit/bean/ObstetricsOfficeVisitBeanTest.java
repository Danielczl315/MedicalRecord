package edu.ncsu.csc.itrust.unit.bean;

import junit.framework.TestCase;

import java.text.SimpleDateFormat;
import edu.ncsu.csc.itrust.model.old.beans.ObstetricsOfficeVisitBean;
import edu.ncsu.csc.itrust.model.old.enums.BooleanType;

public class ObstetricsOfficeVisitBeanTest extends TestCase {
	private SimpleDateFormat formatter;

	@Override
	public void setUp() {
		formatter = new SimpleDateFormat("MM/dd/yyy");
	}
	
	public void testBean() throws Exception {
		ObstetricsOfficeVisitBean oov = new ObstetricsOfficeVisitBean();
		oov.setLastMenstrualPeriodDate(formatter.parse("12/01/2018"));
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
		assertEquals(1, oov.getVisitID());
		assertEquals(1, oov.getObstetricsInitRecordID());
		assertEquals(4, oov.getLocationID());
		assertEquals(1, oov.getPatientMID());
		assertEquals(9000000012L, oov.getHcpMID());
		assertEquals(7, oov.getApptID());
		assertEquals(formatter.parse("02/15/2001"), oov.getVisitDate());
		assertEquals("02/15/2001", oov.getVisitDateStr());
		assertEquals("2001/02/15", oov.getVisitDateStr("yyyy/MM/dd"));
		assertEquals(6.2f, oov.getWeight());
		assertEquals(1, oov.getBloodPressure());
		assertEquals(40, oov.getFetalHeartRate());
		assertEquals(BooleanType.False, oov.getLowLyingPlacentaObserved());
		assertEquals(1, oov.getNumberOfBabies());
		assertEquals(0, oov.compareTo(oov));
		assertEquals(0, oov.equals(oov));

		oov.setVisitDateStr("02/aa/2001");
		assertEquals(null, oov.getVisitDate());
		
		assertEquals(0, oov.getVisitDate_yyyyMMdd());
		oov.setVisitDateStr("02/15/2001");
		assertEquals(20010215, oov.getVisitDate_yyyyMMdd());
		
		oov.setVisitDateStr("2001/02/15", "yyyy/MM/dd");
		assertEquals("02/15/2001", oov.getVisitDateStr());
		
		oov.setVisitDateStr("");
		oov.setVisitDateStr("2001/aa/15", "yyyy/MM/dd");
		assertEquals("", oov.getVisitDateStr());
		
		oov.setLastMenstrualPeriodDate(null);
		assertEquals("0", oov.getWeeksDaysPregnant());
		
		oov.setLastMenstrualPeriodDate(formatter.parse("12/01/2018"));
		oov.setVisitDateStr("12/01/2018");
		assertEquals("0 days", oov.getWeeksDaysPregnant());
		
		oov.setVisitDateStr("12/09/2018");
		assertEquals("1 week, 1 day", oov.getWeeksDaysPregnant());

		oov.setVisitDateStr("12/17/2018");
		assertEquals("2 weeks, 2 days", oov.getWeeksDaysPregnant());
		
		oov.setLowLyingPlacentaObserved(BooleanType.True);
		assertEquals(BooleanType.True, oov.getLowLyingPlacentaObserved());
	}
}
