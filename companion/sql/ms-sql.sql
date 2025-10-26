-- ==========================================================================================
-- Currently is not used
-- ==========================================================================================

-- Create new database
IF NOT EXISTS (SELECT name
               FROM sys.databases
               WHERE name = N'KkmkDB') CREATE DATABASE KkmkDB;

-- Create login for SQL Server authentication
-- CREATE LOGIN my_user
--     WITH PASSWORD = 'StrongP@ssw0rd123';

-- Switch to the new database
-- USE KkmkDB;

-- Create database user and map it to the login
-- CREATE USER my_user FOR LOGIN my_user;

-- Grant necessary permissions to the user
-- ALTER ROLE db_datareader ADD MEMBER my_user;
-- ALTER ROLE db_datawriter ADD MEMBER my_user;
-- create a new user

-- add user to database

-- Create a new schema
-- CREATE SCHEMA MySchema;

-- Grant permissions on the schema
-- GRANT CREATE TABLE TO my_user;
-- GRANT ALTER ON SCHEMA::MySchema TO my_user;

-- Create Person table
-- IF OBJECT_ID('Person', 'U') IS NOT NULL
-- CREATE TABLE Person
-- (
--     PersonId  INT IDENTITY (1,1) PRIMARY KEY,
--     FirstName NVARCHAR(100),
--     LastName  NVARCHAR(100),
--     Age       INT
-- );

-- Grant permissions on the Person table to my_user
-- GRANT SELECT, INSERT, UPDATE, DELETE ON MySchema.Person TO my_user;
