IF NOT EXISTS (SELECT name FROM sys.databases WHERE name = 'reservationsdb')
    CREATE DATABASE reservationsdb;
GO
IF NOT EXISTS (SELECT name FROM sys.databases WHERE name = 'authdb')
    CREATE DATABASE authdb;
GO
IF NOT EXISTS (SELECT name FROM sys.databases WHERE name = 'emaildb')
    CREATE DATABASE emaildb;
GO
