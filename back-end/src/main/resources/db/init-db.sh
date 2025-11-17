#!/bin/bash
set -e

echo "Checking if database exists..."
if ! mysql -h localhost -u root -p"$MYSQL_ROOT_PASSWORD" -e "USE simple_todo_list" >/dev/null 2>&1; then
  echo "Database does not exist. Creating..."
  mysql -h localhost -u root -p"$MYSQL_ROOT_PASSWORD" < /docker-entrypoint-initdb.d/init.sql
  echo "Database initialized."
else
  echo "Database already exists. Skipping initialization."
fi