use sampledb;
GO

DROP TABLE IF EXISTS allcol_tbl;
GO

CREATE TABLE allcol_tbl (
    col1 int NOT NULL PRIMARY KEY,
    col2 bit,
    col3 decimal,
    col4 money,
    col5 numeric,
    col6 smallint,
    col7 tinyint,
    col8 float,
    col9 real,
    col10 date,
    col11 datetime2,
    col12 datetime,
    col13 datetimeoffset,
    col14 smalldatetime,
    col15 time,
    col16 char,
    col17 text,
    col18 varchar,
    col19 nchar,
    col20 nvarchar
);
GO
