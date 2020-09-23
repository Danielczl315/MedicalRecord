package edu.ncsu.csc.itrust.selenium;


import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import edu.ncsu.csc.itrust.model.old.enums.TransactionType;


import java.util.ArrayList;
import java.util.List;



public class editBabyInitializationTest extends iTrustSeleniumTest {

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

		driver = (HtmlUnitDriver) login("9000000012", "pw");
		assertEquals("iTrust - HCP Home", driver.getTitle());
		driver.findElement(By.cssSelector("div.panel-heading")).click();
		driver.findElement(By.linkText("Baby Initialization")).click();
		driver.findElement(By.name("UID_PATIENTID")).sendKeys("1");

		// the button to click should have the text of the MID
		driver.findElement(By.cssSelector("input[value='1']")).submit();
		
		try {
			//driver.findElement(By.className("list-group-item"));
			//driver.findElement(By.name("submit")).click();
			driver.findElement(By.id("iTrustContent"));
		}catch (Error e) {
				verificationErrors.append(e.toString());
				fail();
		}
	}
}
