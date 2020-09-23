package edu.ncsu.csc.itrust.selenium;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import edu.ncsu.csc.itrust.model.old.enums.TransactionType;


import java.util.ArrayList;
import java.util.List;



public class viewObstetricCareRecordsList extends iTrustSeleniumTest {

	private HtmlUnitDriver driver;
	private StringBuffer verificationErrors = new StringBuffer();

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		gen.clearAllTables();
		gen.standardData();
	}
	@Test
	public void testViewObstetricCareRecordsList() throws Exception {

		driver = (HtmlUnitDriver) login("9000000000", "pw");
		assertEquals("iTrust - HCP Home", driver.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 9000000000L, 0L, "");
		driver.findElement(By.cssSelector("div.panel-heading")).click();
		driver.findElement(By.linkText("Obstetrics Records")).click();
		driver.findElement(By.name("UID_PATIENTID")).sendKeys("1");

		// the button to click should have the text of the MID
		driver.findElement(By.cssSelector("input[value='1']")).submit();
		
		try {
			driver.findElement(By.className("list-group-item"));
		}catch (Error e) {
				verificationErrors.append(e.toString());
				fail();
		}
	}
	
	@Test
	public void testCheckEditObstetricCareRecord() throws Exception {

		driver = (HtmlUnitDriver) login("9000000012", "pw");
		 							 
		assertEquals("iTrust - HCP Home", driver.getTitle());
		//assertLogged(TransactionType.HOME_VIEW, 9000000000L, 0L, "");
		driver.findElement(By.cssSelector("div.panel-heading")).click();
		driver.findElement(By.linkText("Obstetrics Records")).click();
		driver.findElement(By.name("UID_PATIENTID")).sendKeys("1");

		// the button to click should have the text of the MID
		driver.findElement(By.cssSelector("input[value='1']")).submit();
		//assertFalse(driver.findElement(By.linkText("ADD")).click());
		try {
			driver.findElement(By.className("list-group-item")).click();
			driver.findElement(By.className("table-title"));
			driver.findElement(By.name("edit_button")).click();;
			
		}catch (Error e) {
			verificationErrors.append(e.toString());
			fail();
	}
		
		
	}
	
	@Test
	public void testCheckDeleteObstetricCareRecord() throws Exception {

		driver = (HtmlUnitDriver) login("9000000012", "pw");
		 							 
		assertEquals("iTrust - HCP Home", driver.getTitle());
		//assertLogged(TransactionType.HOME_VIEW, 9000000000L, 0L, "");
		driver.findElement(By.cssSelector("div.panel-heading")).click();
		driver.findElement(By.linkText("Obstetrics Records")).click();
		driver.findElement(By.name("UID_PATIENTID")).sendKeys("1");

		// the button to click should have the text of the MID
		driver.findElement(By.cssSelector("input[value='1']")).submit();
		//assertFalse(driver.findElement(By.linkText("ADD")).click());
		try {
			driver.findElement(By.className("list-group-item")).click();
			driver.findElement(By.className("table-title"));
			driver.findElement(By.name("delete_button")).click();;
			
		}catch (Error e) {
			verificationErrors.append(e.toString());
			fail();
	}
		
		
	}
	
	@Test
	public void testAddObstetricCareRecord() throws Exception {

		driver = (HtmlUnitDriver) login("9000000012", "pw");
		 							 
		assertEquals("iTrust - HCP Home", driver.getTitle());
		//assertLogged(TransactionType.HOME_VIEW, 9000000000L, 0L, "");
		driver.findElement(By.cssSelector("div.panel-heading")).click();
		driver.findElement(By.linkText("Obstetrics Records")).click();
		driver.findElement(By.name("UID_PATIENTID")).sendKeys("1");

		// the button to click should have the text of the MID
		driver.findElement(By.cssSelector("input[value='1']")).submit();
		
		try {
			driver.findElement(By.name("add_button")).click();
		}catch (Error e) {
				verificationErrors.append(e.toString());
				fail();
		}
		
	}
	
	@Test
	public void testAddEditObstetricCareRecordElgible() throws Exception {

		driver = (HtmlUnitDriver) login("9000000000", "pw");
		 							 
		assertEquals("iTrust - HCP Home", driver.getTitle());
		//assertLogged(TransactionType.HOME_VIEW, 9000000000L, 0L, "");
		driver.findElement(By.cssSelector("div.panel-heading")).click();
		driver.findElement(By.linkText("Obstetrics Records")).click();
		driver.findElement(By.name("UID_PATIENTID")).sendKeys("1");

		// the button to click should have the text of the MID
		driver.findElement(By.cssSelector("input[value='1']")).submit();
		
		
		driver.findElement(By.name("add_button")).click();
		
		if(driver.getPageSource().contains("Only an OB/GYN can add records")) {
			assertTrue(true);
		}
		else {
			
			fail();
		}
		
		
	}
	
	/**
	@Test
	public void testViewObstetricCareRecord() throws Exception {

		driver = (HtmlUnitDriver) login("9000000000", "pw");
		assertEquals("iTrust - HCP Home", driver.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 9000000000L, 0L, "");
		driver.findElement(By.cssSelector("div.panel-heading")).click();
		driver.findElement(By.linkText("Obstetrics Records")).click();
		driver.findElement(By.name("UID_PATIENTID")).sendKeys("1");

		// the button to click should have the text of the MID
		driver.findElement(By.cssSelector("input[value='1']")).submit();
		TableElement table = new TableElement(driver.findElement(By.tagName("table")));
		
	}
	*/
	
	
	/**
	 * TableElement a helper class for Selenium test htmlunitdriver retrieving
	 * data from tables.
	 */
	private class TableElement {
		List<List<WebElement>> table;

		/**
		 * Constructor. This object will help user to get data from each cell of
		 * the table.
		 * 
		 * @param tableElement
		 *            The table WebElement.
		 */
		public TableElement(WebElement tableElement) {
			// TODO Auto-generated constructor stub
			table = new ArrayList<List<WebElement>>();
			List<WebElement> trCollection = tableElement.findElements(By.xpath("tbody/tr"));
			for (WebElement trElement : trCollection) {
				List<WebElement> tdCollection = trElement.findElements(By.xpath("td"));
				table.add(tdCollection);
			}

		}

		/**
		 * Get data from given row and column cell.
		 * 
		 * @param row
		 *            (start from 0)
		 * @param column(start
		 *            from 0)
		 * @return The WebElement in that given cell.
		 */
		public WebElement getTableCell(int row, int column) {
			return table.get(row).get(column);
		}

	}
	
	
}