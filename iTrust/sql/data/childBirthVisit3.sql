INSERT INTO childBirthVisit (
	visitID,
	obstetricsInitRecordID,
	apptID,
	preferredDeliveryMethod,
	hasDelivered,
	pitocinDosage,
	nitrousOxideDosage,
	epiduralAnaesthesiaDosage,
	magnesiumSulfateDosage,
	rhImmuneGlobulinDosage
)
VALUES (
	3,
	3,
	8,
	'Vaginal Delivery',
	'True',
	5,
	5,
	2,
	6,
	3
)
ON DUPLICATE KEY UPDATE visitID = 3;


INSERT INTO babyDeliveryInfo (
	recordID, childBirthVisitID, patientMID, gender,
	dateOfBirth, deliveryMethod, isEstimated
)
VALUES (
	5, 1, 998, 'Female',
	'2018-11-03', 'Vaginal Delivery Vacuum Assist', 'False'
)
ON DUPLICATE KEY UPDATE recordID = 5;

INSERT INTO babyDeliveryInfo (
	recordID, childBirthVisitID, patientMID, gender,
	dateOfBirth, deliveryMethod, isEstimated
)
VALUES (
	6, 2, 999, 'Female',
	'2018-11-03', 'Vaginal Delivery', 'True'
)
ON DUPLICATE KEY UPDATE recordID = 6;