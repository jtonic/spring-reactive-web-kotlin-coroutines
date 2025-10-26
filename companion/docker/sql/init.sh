#!/bin/bash
sudo chmod +x create-database.sh
./create-database.sh & /opt/mssql/bin/sqlservr
