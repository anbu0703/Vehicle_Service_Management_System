# Vehicle Service Management System (VSMS)

Java Swing + JDBC + MySQL desktop application (Summer Internship project, Samcore Solutions, Trichy).

## Features
Login (SHA-256 hashed passwords) | Dashboard | Customers | Vehicles | Service Entry | Payments | Reports
Each data screen supports Add, Update, Delete (with confirmation), Search and input validation.

## Folder structure
```
src/vsms/    Java source (App, LoginPanel, MainPanel, CrudPanel, JdbcRepo, MemoryRepo ...)
sql/         schema.sql  (tables + sample data)
config/      db.properties (MySQL user/password)
lib/         put mysql-connector-j-8.x.jar here
tools/       Shots.java  (creates the output screenshots)
screenshots/ sample output of every screen
build.sh / build.bat, run.sh / run.bat, pom.xml
```

## Quick start (no MySQL): demo mode
1. Install JDK 11 or newer.
2. `./build.sh`  (Windows: `build.bat`)
3. `./run.sh --demo`  (Windows: `run.bat --demo`)
4. Login: **admin / admin123**

## With MySQL
1. Install MySQL and create the database: `mysql -u root -p < sql/schema.sql`
2. Download **mysql-connector-j** (https://dev.mysql.com/downloads/connector/j/) and copy the .jar into `lib/`.
3. Edit `config/db.properties` (db.user, db.password).
4. `./build.sh` then `./run.sh`  (Windows: `build.bat`, `run.bat`)
5. Login: **admin / admin123**

If the database cannot be reached, the app offers to open in demo mode.

## Eclipse / NetBeans / IntelliJ
Import as a Maven project (pom.xml) or create a Java project with `src` as the source folder and add the MySQL connector jar to the build path. Main class: `vsms.App`.

## How it maps to the report
- 7.1 Login algorithm -> `LoginPanel`, `JdbcRepo.login`
- 7.2 / 7.3 Registration -> `CrudPanel.onAdd` (validation) + `JdbcRepo.insert`
- 7.4 Search -> `CrudPanel` search box + `JdbcRepo.list`
- 7.5 Update -> `CrudPanel.onUpdate` + `JdbcRepo.update`
- 7.6 Delete (confirm) -> `CrudPanel.onDelete` + `JdbcRepo.delete`
- 7.7 Database connectivity -> `JdbcRepo.connect` (PreparedStatement, try-with-resources closes connection)
