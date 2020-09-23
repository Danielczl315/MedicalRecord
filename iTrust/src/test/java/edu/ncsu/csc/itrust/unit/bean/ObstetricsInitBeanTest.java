package edu.ncsu.csc.itrust.unit.bean;

import edu.ncsu.csc.itrust.model.old.beans.ObstetricsInitBean;
import junit.framework.TestCase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ObstetricsInitBeanTest extends TestCase {
	private SimpleDateFormat formatter;
	
	@Override
	protected void setUp() throws Exception {
		formatter = new SimpleDateFormat("MM/dd/yyy");
	}

	public void testBean() throws Exception {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(formatter.parse("12/25/2000"));
		calendar.add(Calendar.DATE, 280);
		
		ObstetricsInitBean obInit = new ObstetricsInitBean();
		obInit.setRecordID(1);
		obInit.setPatientMID(1);
		obInit.setInitDate("01/30/2001", "MM/dd/yyyy");
		obInit.setLastMenstrualPeriod("12/25/2000", "MM/dd/yyyy");
		assertEquals(1, obInit.getRecordID());
		assertEquals(1, obInit.getPatientMID());
		assertEquals(formatter.parse("01/30/2001"), obInit.getInitDate());
		assertEquals(formatter.parse("12/25/2000"), obInit.getLastMenstrualPeriod());
		assertEquals(calendar.getTime(), obInit.getEstimatedDueDate());
		assertEquals(0, obInit.compareTo(obInit));
	}
	
	public void testGetDates() throws Exception {
		Date initDate = formatter.parse("01/30/2001");
		Date lmd = formatter.parse("12/25/2000");
		
		ObstetricsInitBean oi = new ObstetricsInitBean();
		oi.setInitDate(initDate);
		oi.setLastMenstrualPeriod(lmd);
		
		assertEquals("01/30/2001", oi.getInitDateMMDDYYYY());
		assertEquals("12/25/2000", oi.getLastMenstrualPeriodMMDDYYYY());
		assertEquals("2001-01-30", oi.getInitDateYYYYMMDD());
		assertEquals("2000-12-25", oi.getLastMenstrualPeriodYYYYMMDD());
	}
	
	public void testToString() {
		ObstetricsInitBean oi = new ObstetricsInitBean();
		oi.setRecordID(1);
		oi.setPatientMID(1);
		oi.setInitDate("01/30/2001", "MM/dd/yyyy");
		oi.setLastMenstrualPeriod("12/25/2000", "MM/dd/yyyy");
		
		String fs = String.format("{\"recordID\":\"%d\", \"initDate\":\"%s\", \"lmpDate\":\"%s\", \"edDate\": \"%s\", \"weeksDaysPregnant\":\"%s\", \"initDateInput\": \"%s\", \"lmpDateInput\":\"%s\"}",
			oi.getRecordID(),
            oi.getInitDateMMDDYYYY(),
			oi.getLastMenstrualPeriodMMDDYYYY(),
			oi.getEstimatedDueDateMMDDYYYY(),
			oi.getWeeksDaysPregnant(),
			oi.getInitDateYYYYMMDD(),
			oi.getLastMenstrualPeriodYYYYMMDD());
		
		assertEquals(fs, oi.toString());
	}
}
