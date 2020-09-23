package edu.ncsu.csc.itrust.selenium;


import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import edu.ncsu.csc.itrust.model.old.enums.TransactionType;


import java.util.ArrayList;
import java.util.List;

public class viewDeliveryLaborReportTest extends iTrustSeleniumTest {
	private HtmlUnitDriver driver;
	private StringBuffer verificationErrors = new StringBuffer();

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		gen.clearAllTables();
		gen.standardData();
	}
	
	@Test
	public void testViewDeliveryLaborReport() throws Exception {

		driver = (HtmlUnitDriver) login("9000000000", "pw");
		assertEquals("iTrust - HCP Home", driver.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 9000000000L, 0L, "");
		driver.findElement(By.cssSelector("div.panel-heading")).click();
		driver.findElement(By.linkText("Labor and Delivery Report")).click();
		driver.findElement(By.name("UID_PATIENTID")).sendKeys("1");

		// the button to click should have the text of the MID
		driver.findElement(By.cssSelector("input[value='1']")).submit();
		
		try {
			driver.findElement(By.id("iTrustContent"));
		}catch (Error e) {
				verificationErrors.append(e.toString());
				fail();
		}
	}
	
}
	