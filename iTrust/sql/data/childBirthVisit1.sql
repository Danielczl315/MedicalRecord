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
	1,
	1,
	8,
	'Vaginal Delivery Vacuum Assist',
	'True',
	10,
	10,
	5,
	8,
	5
)
ON DUPLICATE KEY UPDATE visitID = 1;

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
	2,
	2,
	8,
	'Vaginal Delivery',
	'True',
	5,
	5,
	2,
	6,
	3
)
ON DUPLICATE KEY UPDATE visitID = 2;