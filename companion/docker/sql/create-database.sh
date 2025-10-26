#!/bin/bash
sleep 30s

/opt/mssql-tools18/bin/sqlcmd -S localhost -U sa -P Password!1a -d master -i init.sql -C

touch /var/tmp/executed