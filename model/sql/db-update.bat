@echo off

echo Shutting down connections ...
psql -Upostgres -q -c "SELECT pg_terminate_backend(pid) FROM pg_stat_activity WHERE datname = 'bepo';" > NUL

echo Dropping database ...
psql -Upostgres -q -c "DROP DATABASE bepo;"

echo Dropping role ...
psql -Upostgres -q -c "DROP ROLE bepo;"

echo Creating role ...
psql -Upostgres -q -c "CREATE ROLE bepo WITH LOGIN SUPERUSER PASSWORD 'bepo';"
IF ERRORLEVEL 1 goto :EOF

echo Creating database ...
psql -Upostgres -q -c "CREATE DATABASE bepo WITH OWNER bepo ENCODING 'utf-8' TEMPLATE template1;"
IF ERRORLEVEL 1 goto :EOF

for %%a in (*.sql) do (
  echo Applying script: %%~na ...
  psql -q -Upostgres bepo < "%%~fa" > NUL 2>&1
  IF ERRORLEVEL 1 goto :EOF
  IF %1.==0. goto :skip
)

:skip

echo.
psql -Upostgres -q -c"TABLE \"-NGS-\".\"AppliedScripts\" ORDER BY 1" bepo
echo Expected: 
dir *.sql | findstr File(s)

echo.
pause
