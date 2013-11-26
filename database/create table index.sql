IF NOT EXISTS( SELECT  1
			   FROM    dbo.sysindexes
               WHERE   name = N'IX_Log_File_Name_Project_Instance'
                       AND id = OBJECT_ID(N'[dbo].[Log_File]'))
BEGIN
CREATE INDEX [IX_Log_File_Name_Project_Instance] ON dbo.Log_File (LogName,Project,Instance)
END;
GO

IF NOT EXISTS( SELECT  1
			   FROM    dbo.sysindexes
               WHERE   name = N'IX_Detail_Status_Interface_LogId_ErrorCode'
                       AND id = OBJECT_ID(N'[dbo].[Action_Log_Detail]'))
BEGIN
CREATE INDEX [IX_Detail_Status_Interface_LogId_ErrorCode] ON dbo.Action_Log_Detail ([Status],Interface,LogId,ErrorCode)
END;
GO

IF NOT EXISTS( SELECT  1
			   FROM    dbo.sysindexes
               WHERE   name = N'IX_Detail_Status'
                       AND id = OBJECT_ID(N'[dbo].[Action_Log_Detail]'))
BEGIN
CREATE INDEX [IX_Detail_Status] ON dbo.Action_Log_Detail ([Status])
END;
GO

IF NOT EXISTS( SELECT  1
			   FROM    dbo.sysindexes
               WHERE   name = N'IX_Detail_Interface'
                       AND id = OBJECT_ID(N'[dbo].[Action_Log_Detail]'))
BEGIN
CREATE INDEX [IX_Detail_Interface] ON dbo.Action_Log_Detail (Interface)
END;
GO

IF NOT EXISTS( SELECT  1
			   FROM    dbo.sysindexes
               WHERE   name = N'IX_Detail_LogId'
                       AND id = OBJECT_ID(N'[dbo].[Action_Log_Detail]'))
BEGIN
CREATE INDEX [IX_Detail_LogId] ON dbo.Action_Log_Detail (LogId)
END;
GO

IF NOT EXISTS( SELECT  1
			   FROM    dbo.sysindexes
               WHERE   name = N'IX_Detail_ErrorCode'
                       AND id = OBJECT_ID(N'[dbo].[Action_Log_Detail]'))
BEGIN
CREATE INDEX [IX_Detail_ErrorCode] ON dbo.Action_Log_Detail (ErrorCode)
END;
GO

IF NOT EXISTS( SELECT  1
			   FROM    dbo.sysindexes
               WHERE   name = N'IX_Action_Log_Detail_LodId'
                       AND id = OBJECT_ID(N'[dbo].[Action_Log_Detail]'))
BEGIN
CREATE INDEX [IX_Action_Log_Detail_LodId] ON dbo.Action_Log_Detail (LogId) INCLUDE (Status)
END;
GO

IF NOT EXISTS( SELECT  1
			   FROM    dbo.sysindexes
               WHERE   name = N'IX_Detail_RecordTime'
                       AND id = OBJECT_ID(N'[dbo].[Action_Log_Detail]'))
BEGIN
CREATE INDEX [IX_Detail_RecordTime] ON dbo.Action_Log_Detail (RecordTime DESC) 
END;
GO

IF NOT EXISTS( SELECT  1
			   FROM    dbo.sysindexes
               WHERE   name = N'IX_Action_Log_Detail_LodId_Status_RecordTime'
                       AND id = OBJECT_ID(N'[dbo].[Action_Log_Detail]'))
BEGIN
CREATE INDEX [IX_Action_Log_Detail_LodId_Status_RecordTime] ON dbo.Action_Log_Detail (LogId) INCLUDE (Status,RecordTime)
END;
GO