SELECT row_number() over(order by RecordTime DESC) timeId, 
t1.ID, t1.LogId, RecordTime 
INTO #tmplog 
FROM Action_Log_Detail t1 
INNER JOIN Temp_Log_Id t2 ON t1.LogId=t2.LogId 
WHERE t1.Status = 'success' 

CREATE NONCLUSTERED INDEX #tmplog_Id ON #tmplog(Id, LogId) INCLUDE(timeId) 

SELECT TOP 50 t1.* FROM Action_Log_Detail t1 
INNER JOIN #tmplog t2 ON t1.id=t2.Id 
ORDER BY t2.timeId 

drop table #tmplog 