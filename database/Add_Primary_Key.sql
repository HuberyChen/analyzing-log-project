IF NOT EXISTS( SELECT  1
			   FROM    dbo.sysindexes
               WHERE   name = N'PX_Log_File_Id'
                       AND id = OBJECT_ID(N'[dbo].[Log_File]'))
BEGIN
ALTER TABLE dbo.Log_File ADD CONSTRAINT PX_Log_File_Id PRIMARY KEY(Id)
END;
GO

IF NOT EXISTS( SELECT  1
			   FROM    dbo.sysindexes
               WHERE   name = N'PX_Action_Log_Detail_Id'
                       AND id = OBJECT_ID(N'[dbo].[Action_Log_Detail]'))
BEGIN
ALTER TABLE dbo.Action_Log_Detail ADD CONSTRAINT PX_Action_Log_Detail_Id PRIMARY KEY(Id)
END;
GO

IF NOT EXISTS( SELECT  1
			   FROM    dbo.sysindexes
               WHERE   name = N'PX_Project_Id'
                       AND id = OBJECT_ID(N'[dbo].[Project]'))
BEGIN
ALTER TABLE dbo.Project ADD CONSTRAINT PX_Project_Id PRIMARY KEY(Id)
END;
GO

IF NOT EXISTS( SELECT  1
			   FROM    dbo.sysindexes
               WHERE   name = N'PX_Project_Instance_Id'
                       AND id = OBJECT_ID(N'[dbo].[Project_Instance]'))
BEGIN
ALTER TABLE dbo.Project_Instance ADD CONSTRAINT PX_Project_Instance_Id PRIMARY KEY(Id)
END;
GO

IF NOT EXISTS( SELECT  1
			   FROM    dbo.sysindexes
               WHERE   name = N'PX_Schedule_Id'
                       AND id = OBJECT_ID(N'[dbo].[Schedule]'))
BEGIN
ALTER TABLE dbo.Schedule ADD CONSTRAINT PX_Schedule_Id PRIMARY KEY(Id)
END;
GO