package edu.ncsu.csc.itrust.model.old.validate;

import java.util.regex.Pattern;

/**
 * Enum with all of the validation formats that fit into a regex.
 * 
 *  
 * 
 *
 */


public enum ValidationFormat {
	NAME("^(?=.*[a-zA-Z])[\\sa-zA-Z'-]{1,20}$", 				"Up to 20 Letters, space, ' and -"), 
	DATE("[\\d]{2}/[\\d]{2}/[\\d]{4}", 			"MM/DD/YYYY"),
	PHONE_NUMBER("[\\d]{3}-[\\d]{3}-[\\d]{4}", 	"xxx-xxx-xxxx"),
	MID("[\\d]{1,10}",							"Between 1 and 10 digits"),
	NPMID("[0-8][0-9]{0,9}", 					"1-10 digit number not beginning with 9"),
	DATETIME("[\\d]{4}-[\\d]{2}-[\\d]{2}[\\s]{1}[\\d]{2}:[\\d]{2}:[\\d]{2}.[\\d]{1}", "mm/dd/yyyy"),
	
	EMAIL(".+@.+\\..+",
					"Up to 30 alphanumeric characters and symbols . and _ @"), 
					
	QUESTION("[a-zA-Z0-9?\\-'.\\s]{1,50}", "Up to 50 alphanumeric characters and symbols ?-'."),
	ANSWER("[a-zA-Z0-9\\s]{1,30}", "Up to 30 alphanumeric characters"),
	
	ADDRESS("[a-zA-Z0-9.\\s]{1,30}", "Up to 30 alphanumeric characters, and ."),
	FULL_ADDRESS("[a-zA-Z0-9.,\\s]{1,100}", "Up to 100 alphanumeric characters, comma, and ."),
	APPT_COMMENT("[0-9a-zA-Z\\s'\"?!:;\\-._\n\t]{1,1000}", "Between 0 and 1000 alphanumerics with space, and other punctuation"),
	APPT_TYPE_NAME("[a-zA-Z ]{1,30}", "Between 1 and 30 alpha characters and space"),
	APPT_TYPE_DURATION("[0-9]{1,5}", "Between 1 and 5 numeric digits"),
	APPT_ID("[\\d]{1,10}", "Between 1 and 10 digits"),
	CITY("[a-zA-Z\\s]{1,15}", "Up to 15 characters"),
	STATE("[A-Z]{2}", "Two capital letters"),
	ZIPCODE("([0-9]{5})|([0-9]{5}-[0-9]{4})", "xxxxx or xxxxx-xxxx"), // ^[0-9]{5}(?:-[0-9]{4})?$
	
	BLOODTYPE("((O)|(A)|(B)|(AB))([+-]{1})", "Must be [O,A,B,AB]+/-"), // ^(?:O|A|B|AB)[+-]$
	
	NOTES("[a-zA-Z0-9\\s'\"?!:#;\\-.,_\n\t]{1,300}",
			"Up to 300 alphanumeric characters, with space, and other punctuation"),
	MESSAGES_BODY("[a-zA-Z0-9\\s'\"?!:;\\-.,_\n\t()\\\\/]{1,1000}",

			"Up to 1000 alphanumeric characters, with space, and other punctuation"),
	MESSAGES_SUBJECT("[a-zA-Z0-9\\s'\"?!:;\\-._\n\t()]{1,100}",
			"Up to 100 alphanumeric characters, with space, and other punctuation"),
	PASSWORD("[a-zA-Z0-9]{8,20}", "8-20 alphanumeric characters"),
	INSURANCE_ID("[\\s\\da-zA-Z'-]{1,20}", "Up to 20 letters, digits, space, ' and -"),
	HOSPITAL_ID("[\\d]{1,10}", "Between 1 and 10 digits"),
	VISIT_ID("[\\d]{1,10}", "Between 1 and 10 digits"),
	RECORD_ID("[\\d]{1,10}", "Between 1 and 10 digits"),
	LOCATION_ID("[\\d]{1,10}", "Between 1 and 10 digits"),
	HOSPITAL_NAME("[0-9a-zA-Z' .]{1,30}", "Between 1 and 30 alphanumerics, space, ', and ."),
	ND_CODE_DESCRIPTION("[a-zA-Z0-9\\s]{1,100}", "Up to 100 characters, letters, numbers, and a space"),
	DRUG_INT_COMMENTS("[a-zA-Z0-9.\\-',!;:()?\\s]{1,500}", "Up to 500 alphanumeric characters and .-',!;:()?"),
	EMAILS("[a-zA-Z0-9.\\-',!;:()?\\s\\\\/]{1,500}", 

		   "Up to 500 alphanumeric characters and .-',!;:()?"),
	ADVERSE_EVENT_COMMENTS("[a-zA-Z0-9.\\-',!;:()?\\s]{1,2000}", "Up to 2000 alphanumeric characters and .-',!;:()?"),
	ICD_CODE_DESCRIPTION("[a-zA-Z0-9\\s]{1,30}", "Up to 30 characters, letters, numbers, and a space"),
	CPT_CODE_DESCRIPTION("[a-zA-Z0-9\\s]{1,30}", "Up to 30 characters, letters, numbers, and a space"),
	ALLERGY_DESCRIPTION("[a-zA-Z0-9\\s]{1,30}", "Up to 30 characters, letters, numbers, and a space"),
	ICD9CM("([\\d]{1,3})|([\\d]{1,3}\\.[\\d]{0,2})", "xxx.xx"),
	CPT("[\\d]{1,4}[A-Za-z0-9]", "Up to four digit integer plus a letter or digit"),
	CROWN_RUMP_LENGTH("[\\d]{0,3}(\\.(\\d){0,1}){0,1}", "Up to 3-digit number + up to 1 decimal place"),
	BIPARIETAL_DIAMETER("[\\d]{0,3}(\\.(\\d){0,1}){0,1}", "Up to 3-digit number + up to 1 decimal place"),
	HEAD_CIRCUMFERENCE("[\\d]{0,3}(\\.(\\d){0,1}){0,1}", "Up to 3-digit number + up to 1 decimal place"),
	FEMUR_LENGTH("[\\d]{0,3}(\\.(\\d){0,1}){0,1}", "Up to 3-digit number + up to 1 decimal place"),
	OCCIPITOFRONTAL_DIAMETER("[\\d]{0,3}(\\.(\\d){0,1}){0,1}", "Up to 3-digit number + up to 1 decimal place"),
	ABDOMINAL_CIRCUMFERENCE("[\\d]{0,3}(\\.(\\d){0,1}){0,1}", "Up to 3-digit number + up to 1 decimal place"),
	HUMERUS_LENGTH("[\\d]{0,3}(\\.(\\d){0,1}){0,1}", "Up to 3-digit number + up to 1 decimal place"),
	ESTIMATED_FETAL_WEIGHT("[\\d]{0,3}(\\.(\\d){0,1}){0,1}", "Up to 3-digit number + up to 1 decimal place"),
	Height("[\\d]{0,3}(\\.(\\d){0,1}){0,1}", "Up to 3-digit number + up to 1 decimal place"),
	Weight("[\\d]{0,4}(\\.(\\d){0,1}){0,1}", "Up to 4-digit number + up to 1 decimal place"),
	YEAR("[\\d]{4}", "Must be 4 digits"), 
	GENDERCOD("(Male)|(Female)|(Not Specified)", "Only Male, Female, or All Patients"),
	LOINC("[\\d]{1,5}[-]{1}[\\d]{1}", "Must be 1-5 digits followed by a - then another digit"),
	ND("[\\d]{1,9}", "Up to nine digit integer"),
	LOINC_ITEM("[^\"]{1,100}", "Up to 100 characters, excluding quotation marks"),
	COMMENTS("[a-zA-Z0-9.\\s]{1,500}", "Up to 500 alphanumeric characters"),
	LAB_STATUS("(In Transit)|(Received)|(Testing)|(Pending)|(Completed)", "Only In Transit, Received, Testing, Pending, or Completed"),
	LAB_RIGHTS("(ALLOWED)|(RESTRICTED)", "Only ALLOWED, or RESTRICTED"),
	BLOOD_PRESSURE("^[0-9]{1,3}", "Between 1 and 3 digits"),
	SYSTOLIC_BLOOD_PRESSURE("^([4-9][0-9]|1[0-9][0-9]|2[0-3][0-9]|240)$", "Must be between 40 and 240"),
	DIASTOLIC_BLOOD_PRESSURE("^([4-9][0-9]|1[0-4][0-9]|150)$", "Must be between 40 and 150"),
	FETAL_HEART_RATE("^[0-9]{1,3}", "Between 1 and 3 digits"),
	GLUCOSE_LEVEL("^([0-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|250)$", "Must be between 0 and 250"),
	PEDOMETER_READING("^([0-9]{1,10})$", "Up to ten digit integer"),
	HEIGHT("^([0-9]{1,4}\\.[0-9])$", "Up to 4 digit number and 1 decimal place"),
	WEIGHT("^([0-9]{1,4}\\.[0-9])$", "Up to 4 digit number and 1 decimal place"),
	ORC("[\\d]{1,5}", "Up to five digit integer"),
	OR_CODE_DESCRIPTION("[a-zA-Z0-9\\s]{1,80}", "Up to 80 characters, letters, numbers, and a space"),

	PATIENT_INSTRUCTIONS_NAME("^[a-zA-Z0-9#;?\\-'.:,!/ \n]{1,100}$",
			"Up to 100 alphanumeric characters, with space, and other punctuation"),
	PATIENT_INSTRUCTIONS_COMMENTS("^[a-zA-Z0-9#;?\\-'.:,!/ \n]{1,500}$", 
			"Up to 500 alphanumeric characters, with space, and other punctuation"),
	PATIENT_INSTRUCTIONS_URL("^.{1,200}$",
			"Up to 200 characters, as a valid URL"),
	FILE_URL("^.{1,200}$",
			"Up to 200 characters, as a valid URL"),
	LABPROCEDURE_NUMRESULT_CONTENT("^[\\-+]?(?:\\d+(?:\\.\\d*)?|\\.\\d+)$",
			"A number containing an optional minus sign and an optional decimal point."),
	LABPROCEDURE_NUMRESULT_LENGTH("^.{1,20}$",
			"A number containing between 1 and 20 characters, including the optional minus sign and decimal point."),
	LABPROCEDURE_COMMENTS("^[^<>&]{1,500}$",
			"Up to 500 characters without newline, less than, greater than, or ampersand."),
			
	REFERRAL_NOTES("[a-zA-Z0-9\\s'\"?!:;\\-.,_\n\t()\\\\/]{1,500}",
			 "Up to 500 alphanumeric characters, with space, and other punctuation"),
	PRIORITY("[1-3]", "Priority must be between 1 and 3"),
	WEEKS_PREGNANT("^([0-9]|[1-3][0-9]|4[0-2])-[0-6]{1}$", "Weeks must be between 0 and 42, Days must be between 0 and 6"),
	HOURS_LABOR("[\\d]{0,3}.[\\d]{0,2}", "Hours in labor must between 0.0 and 999.99"),
	MULTIPLICITY("^([0-9]|[1-3][0-9]|4[0-2])$", "Must be between 0 and 42"),
	DOSAGE("^([0-9]|[1-3][0-9]|4[0-2])$", "Must be between 0 and 42"),
	FHR("^[0-9]\\d*", "Must be 0 or positive integer."),
	FHU("^[0-9]*.^[1-9][0-9]*|^[1-9][0-9]*.?[0-9]*$", "Must be a positive double."),
	WEEKS_PREGNANT_OV("^([0-9]|[1-3][0-9]|4[0-2])-[0-6]{1}$", "The patient chosen is not a current obstetrics patient"),
	MEALTYPE("^(?:Breakfast|Lunch|Snack|Dinner)$", "must be one of {Breakfast, Lunch, Snack, Dinner}"),
	EXERCISETYPE("^(?:Cardio|Weight Training)$", "must be one of {Cardio, Weight Training}"),
	SLEEPTYPE("^(?:Nightly|Nap)$", "must be one of {Nightly, Nap}"),
	HEIGHT_OV("^[0-9]{0,3}(?:\\.[0-9])?$", "Up to 3 digit number and up to 1 decimal place"),
	LENGTH_OV("^[0-9]{0,3}(?:\\.[0-9])?$", "Up to 3 digit number and up to 1 decimal place"),
	WEIGHT_OV("^[0-9]{0,3}(?:\\.[0-9])?$", "Up to 4 digit number and up to 1 decimal place"),
	HEAD_CIRCUMFERENCE_OV("^[0-9]{0,3}(?:\\.[0-9])?$", "Up to 3 digit number and up to 1 decimal place"),
	BLOOD_PRESSURE_OV("^[0-9]{1,3}\\/[0-9]{1,3}$", "Up to 3-digit number / Up to 3-digit number"),
	HDL_OV("^[0-8]?[0-9]$", "integer less than 90"),
	TRIGLYCERIDE_OV("^(?:[1-5][0-9]{2}|600)$", "integer between 100 and 600"),
	LDL_OV("^(?:[1-5]?[0-9]{1,2}|600)$", "integer between 0 and 600"),
	HSS_OV("^[1-3]$","1, 2, or 3, representing household smoking status"),
	PSS_OV("^[1-59]$","1-5 or 9, representing patient smoking status")
	;

	private Pattern regex;
	private String description;

	ValidationFormat(String regex, String errorMessage) {
		this.regex = Pattern.compile(regex);
		this.description = errorMessage;
	}

	public Pattern getRegex() {
		return regex;
	}

	public String getDescription() {
		return description;
	}
}
