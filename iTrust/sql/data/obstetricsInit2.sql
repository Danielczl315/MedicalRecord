INSERT INTO obstetricsInitRecords (
	recordID, patientMID, initDate, lastMenstrualPeriod
)
VALUES (
	1, 2, '2001-01-30', '2000-12-25'
)
ON DUPLICATE KEY UPDATE recordID = 1;

INSERT INTO obstetricsInitRecords (
	recordID, patientMID, initDate, lastMenstrualPeriod
)
VALUES (
	2, 2, '2004-03-20', '2004-02-22'
)
ON DUPLICATE KEY UPDATE recordID = 2;

INSERT INTO obstetricsInitRecords (
	recordID, patientMID, initDate, lastMenstrualPeriod
)
VALUES (
	3, 2, '2018-02-14', '2018-1-13'
)
ON DUPLICATE KEY UPDATE recordID = 3;