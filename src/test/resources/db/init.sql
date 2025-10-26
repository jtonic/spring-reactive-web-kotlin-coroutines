-- ==========================================================================================
-- user by integration tests with test containers
-- ==========================================================================================
IF NOT EXISTS (SELECT name
               FROM sys.databases
               WHERE name = N'KkmkDB') CREATE DATABASE KkmkDB;
GO
