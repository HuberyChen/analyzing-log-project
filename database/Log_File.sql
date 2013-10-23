CREATE TABLE Log_File (
	Id INT IDENTITY(1,1) NOT NULL ,
	LogName VARCHAR(100) NOT NULL ,
	ProjectId INT NOT NULL ,
	ServerId INT NOT NULL ,
	LogType VARCHAR(10) NULL ,
	AbsolutePath VARCHAR(200) NULL ,
	IsDecomposed CHAR(1) NOT NULL ,
	IsAnalyzed CHAR(1) NOT NULL ,
	Sequence INT NULL ,
)