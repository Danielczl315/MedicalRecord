INSERT INTO ultrasoundRecords (
	recordID,
	obstetricsOfficeVisitID,
	crownRumpLength,
	biparietalDiameter,
	headCircumference,
	femurLength,
	occipitofrontalDiameter,
	abdominalCircumference,
	humerusLength,
	estimatedFetalWeight,
	fileURL
)
VALUES (
	1,
	1,
	3.4,
	2.6,
	3.1,
	5.4,
	1.7,
	4.9,
	4.5,
	6.8,
	'nonexistent'
)
ON DUPLICATE KEY UPDATE 
obstetricsOfficeVisitID = 1;

INSERT INTO ultrasoundRecords (
	recordID,
	obstetricsOfficeVisitID,
	crownRumpLength,
	biparietalDiameter,
	headCircumference,
	femurLength,
	occipitofrontalDiameter,
	abdominalCircumference,
	humerusLength,
	estimatedFetalWeight,
	fileURL
)
VALUES (
	2,
	2,
	3.7,
	3.0,
	3.5,
	5.2,
	2.0,
	5.3,
	4.9,
	10.4,
	'nonexistent'
)
ON DUPLICATE KEY UPDATE 
obstetricsOfficeVisitID = 2;