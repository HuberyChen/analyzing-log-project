CREATE TABLE [dbo].[SchedulerJobTracking](
	[TrackingId] [int] IDENTITY(1,1) NOT NULL,
	[JobName] [varchar](100) NOT NULL,
	[CreatedTime] [datetime] NOT NULL,
	[Message] [varchar](100) NULL,
	[Status] [varchar](30) NOT NULL,
 CONSTRAINT [PK__SchedulerJobTracking] PRIMARY KEY CLUSTERED 
(
	[TrackingId] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO