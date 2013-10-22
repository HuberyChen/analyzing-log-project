CREATE TABLE Log_List(
	Id INT IDENTITY(1,1) NOT NULL ,
	LogName VARCHAR(100) NOT NULL ,
	APIName VARCHAR(30) NOT NULL ,
	HostName VARCHAR(30) NOT NULL ,
	LogType VARCHAR(10) NULL ,
	AbsolutePath VARCHAR(200) NULL ,
)