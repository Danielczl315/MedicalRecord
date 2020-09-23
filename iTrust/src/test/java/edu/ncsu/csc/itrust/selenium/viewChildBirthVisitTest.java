package edu.ncsu.csc.itrust.selenium;


import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import edu.ncsu.csc.itrust.model.old.enums.TransactionType;


import java.util.ArrayList;
import java.util.List;

public class viewChildBirthVisitTest extends iTrustSeleniumTest {
	private HtmlUnitDriver driver;
	private StringBuffer verificationErrors = new StringBuffer();

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		gen.clearAllTables();
		gen.standardData();
	}
	@Test
	public void testViewChildBirthVisitList() throws Exception {

		driver = (HtmlUnitDriver) login("9000000000", "pw");
		assertEquals("iTrust - HCP Home", driver.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 9000000000L, 0L, "");
		driver.findElement(By.cssSelector("div.panel-heading")).click();
		driver.findElement(By.linkText("Document Childbirth")).click();
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
	public void testCheckEditChildBirthVisit() throws Exception {

		driver = (HtmlUnitDriver) login("9000000012", "pw");
		 							 
		assertEquals("iTrust - HCP Home", driver.getTitle());
		//assertLogged(TransactionType.HOME_VIEW, 9000000000L, 0L, "");
		driver.findElement(By.cssSelector("div.panel-heading")).click();
		driver.findElement(By.linkText("Document Childbirth")).click();
		driver.findElement(By.name("UID_PATIENTID")).sendKeys("1");

		// the button to click should have the text of the MID
		driver.findElement(By.cssSelector("input[value='1']")).submit();
		//assertFalse(driver.findElement(By.linkText("ADD")).click());
		try {
			//driver.findElement(By.className("list-group-item")).click();
			
			driver.findElement(By.name("03/20/2004")).click();
			driver.findElement(By.name("edit_button")).click();
			
		}catch (Error e) {
			verificationErrors.append(e.toString());
			fail();
	}
		
	}
	
	@Test
	public void testCheckAddChildBirthVisit() throws Exception {

		driver = (HtmlUnitDriver) login("9000000012", "pw");
		 							 
		assertEquals("iTrust - HCP Home", driver.getTitle());
		//assertLogged(TransactionType.HOME_VIEW, 9000000000L, 0L, "");
		driver.findElement(By.cssSelector("div.panel-heading")).click();
		driver.findElement(By.linkText("Document Childbirth")).click();
		driver.findElement(By.name("UID_PATIENTID")).sendKeys("1");

		// the button to click should have the text of the MID
		driver.findElement(By.cssSelector("input[value='1']")).submit();
		//assertFalse(driver.findElement(By.linkText("ADD")).click());
		try {
			//driver.findElement(By.className("list-group-item")).click();
			
			driver.findElement(By.name("02/14/2018")).click();
			driver.findElement(By.name("add_button")).click();
			
		}catch (Error e) {
			verificationErrors.append(e.toString());
			fail();
	}
		
	}
	/**
	@Test
	public void testCheckChildBirthVisitEligible() throws Exception {

		driver = (HtmlUnitDriver) login("9000000000", "pw");
		 							 
		assertEquals("iTrust - HCP Home", driver.getTitle());
		//assertLogged(TransactionType.HOME_VIEW, 9000000000L, 0L, "");
		driver.findElement(By.cssSelector("div.panel-heading")).click();
		driver.findElement(By.linkText("Document Childbirth")).click();
		driver.findElement(By.name("UID_PATIENTID")).sendKeys("1");

		// the button to click should have the text of the MID
		driver.findElement(By.cssSelector("input[value='1']")).submit();
		//assertFalse(driver.findElement(By.linkText("ADD")).click());
		
			//driver.findElement(By.className("list-group-item")).click();
			
		driver.findElement(By.name("02/14/2018")).click();
		driver.findElement(By.name("add_button")).click();
			

		if(driver.getPageSource().contains("Only an OB/GYN can add records")) {
			assertTrue(true);
		}
		else {
				
			fail();
		}
		
	}
	*/
}
