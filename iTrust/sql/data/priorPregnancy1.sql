INSERT INTO priorPregnancies (
	priorPregnancyID, obstetricRecordID,
	yearOfConception, daysPregnant, hoursInLabor,
	weightGain, deliveryType, multiplicity
)
VALUES (
	1, 1, 2001, 200, 12.25, 5.6,
	'Vaginal Delivery Vacuum Assist',
	3
)
ON DUPLICATE KEY UPDATE priorPregnancyID = 1;

INSERT INTO priorPregnancies (
	priorPregnancyID, obstetricRecordID,
	yearOfConception, daysPregnant, hoursInLabor,
	weightGain, deliveryType, multiplicity
)
VALUES (
	2, 2, 2004, 210, 6.75, 3.2,
	'Vaginal Delivery', 1
)
ON DUPLICATE KEY UPDATE priorPregnancyID = 2;
