INSERT INTO babyDeliveryInfo (
	recordID, childBirthVisitID, patientMID, gender,
	dateOfBirth, deliveryMethod, isEstimated
)
VALUES (
	1, 1, 996, 'Male',
	'2001-11-03', 'Vaginal Delivery Vacuum Assist', 'False'
)
ON DUPLICATE KEY UPDATE recordID = 1;

INSERT INTO babyDeliveryInfo (
	recordID, childBirthVisitID, patientMID, gender,
	dateOfBirth, deliveryMethod, isEstimated
)
VALUES (
	2, 1, 997, 'Male',
	'2001-11-03', 'Vaginal Delivery Vacuum Assist', 'False'
)
ON DUPLICATE KEY UPDATE recordID = 2;

INSERT INTO babyDeliveryInfo (
	recordID, childBirthVisitID, patientMID, gender,
	dateOfBirth, deliveryMethod, isEstimated
)
VALUES (
	3, 1, 998, 'Female',
	'2001-11-03', 'Vaginal Delivery Vacuum Assist', 'False'
)
ON DUPLICATE KEY UPDATE recordID = 3;

INSERT INTO babyDeliveryInfo (
	recordID, childBirthVisitID, patientMID, gender,
	dateOfBirth, deliveryMethod, isEstimated
)
VALUES (
	4, 2, 999, 'Female',
	'2004-12-10', 'Vaginal Delivery', 'True'
)
ON DUPLICATE KEY UPDATE recordID = 4;