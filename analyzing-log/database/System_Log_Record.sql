CREATE TABLE System_Log_Record
    (
      Id INT IDENTITY(1, 1)
             NOT NULL ,
      LogId INT NOT NULL,
      RecordTime DATETIME NULL ,
      System VARCHAR(20) NULL ,
      Host VARCHAR(20) NULL ,
      Status VARCHAR(10) NULL ,
      Interface VARCHAR(100) NULL ,
      ElapsedTime INT NULL ,
      RequestMethod VARCHAR(10) NULL ,
      ErrorCode VARCHAR(5) NULL ,
      ExceptionMsg VARCHAR(100) NULL ,
      LogAddress VARCHAR(100) NULL ,
    )