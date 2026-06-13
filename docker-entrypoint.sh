#!/bin/sh
set -eu

# Some platforms supply postgresql://user:password@host:port/database.
# Spring's PostgreSQL driver expects jdbc:postgresql://host:port/database.
if [ -n "${DATABASE_URL:-}" ] && [ -z "${SPRING_DATASOURCE_URL:-}" ]; then
  database_address="${DATABASE_URL#*://}"
  database_address="${database_address#*@}"
  export SPRING_DATASOURCE_URL="jdbc:postgresql://${database_address}"
fi

exec java -jar /app/app.jar
