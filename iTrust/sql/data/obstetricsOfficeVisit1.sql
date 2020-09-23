INSERT INTO obstetricsOfficeVisit (
	visitID,
	obstetricsInitRecordID,
	locationID,
	patientMID,
	hcpMID,
	apptID,
	visitDate,
	weight,
	bloodPressure,
	fetalHeartRate,
	lowLyingPlacentaObserved,
	numberOfBabies
)
VALUES (
	1,
	1,
	4,
	1,
	9000000012,
	7,
	'2001-2-15',
	6.2,
	1,
	40,
	'False',
	1
)
ON DUPLICATE KEY UPDATE 
patientMID = 1;

INSERT INTO obstetricsOfficeVisit (
	visitID,
	obstetricsInitRecordID,
	locationID,
	patientMID,
	hcpMID,
	apptID,
	visitDate,
	weight,
	bloodPressure,
	fetalHeartRate,
	lowLyingPlacentaObserved,
	numberOfBabies
)
VALUES (
	2,
	2,
	4,
	1,
	9000000012,
	7,
	'2004-4-20',
	10.6,
	2,
	50,
	'True',
	2
)
ON DUPLICATE KEY UPDATE 
patientMID = 2;

INSERT INTO obstetricsOfficeVisit (
	visitID,
	obstetricsInitRecordID,
	locationID,
	patientMID,
	hcpMID,
	apptID,
	visitDate,
	weight,
	bloodPressure,
	fetalHeartRate,
	lowLyingPlacentaObserved,
	numberOfBabies
)
VALUES (
	3,
	3,
	4,
	1,
	9000000012,
	7,
	'2018-3-01',
	10.6,
	2,
	50,
	'True',
	2
)
ON DUPLICATE KEY UPDATE 
patientMID = 3;