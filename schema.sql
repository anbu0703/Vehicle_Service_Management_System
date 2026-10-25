-- Vehicle Service Management System : MySQL schema + sample data
-- Run:  mysql -u root -p < sql/schema.sql
CREATE DATABASE IF NOT EXISTS vsms CHARACTER SET utf8mb4;
USE vsms;

DROP TABLE IF EXISTS payments;
DROP TABLE IF EXISTS services;
DROP TABLE IF EXISTS vehicles;
DROP TABLE IF EXISTS customers;
DROP TABLE IF EXISTS users;

CREATE TABLE users (
  id INT AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(50) NOT NULL UNIQUE,
  password_hash CHAR(64) NOT NULL          -- SHA-256 hex
);

CREATE TABLE customers (
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(100) NOT NULL,
  phone VARCHAR(15) NOT NULL,
  email VARCHAR(100),
  address VARCHAR(200),
  reg_date VARCHAR(12)
);

CREATE TABLE vehicles (
  id INT AUTO_INCREMENT PRIMARY KEY,
  vehicle_no VARCHAR(20) NOT NULL UNIQUE,
  owner_name VARCHAR(100) NOT NULL,
  vehicle_type VARCHAR(20) NOT NULL,
  make_model VARCHAR(80) NOT NULL,
  fuel_type VARCHAR(20),
  year INT,
  chassis_no VARCHAR(30),
  engine_no VARCHAR(30),
  reg_date VARCHAR(12)
);

CREATE TABLE services (
  id INT AUTO_INCREMENT PRIMARY KEY,
  vehicle_no VARCHAR(20) NOT NULL,
  customer_name VARCHAR(100) NOT NULL,
  service_type VARCHAR(40) NOT NULL,
  service_date VARCHAR(12) NOT NULL,
  charges DECIMAL(10,2) NOT NULL,
  status VARCHAR(20),
  kms INT,
  technician VARCHAR(60),
  notes VARCHAR(255)
);

CREATE TABLE payments (
  id INT AUTO_INCREMENT PRIMARY KEY,
  invoice_no VARCHAR(20) NOT NULL UNIQUE,
  vehicle_no VARCHAR(20) NOT NULL,
  customer_name VARCHAR(100) NOT NULL,
  service_type VARCHAR(40) NOT NULL,
  amount DECIMAL(10,2) NOT NULL,
  paid_amount DECIMAL(10,2) DEFAULT 0,
  status VARCHAR(20),
  pay_mode VARCHAR(20),
  pay_date VARCHAR(12)
);

-- login: admin / admin123
INSERT INTO users(username, password_hash) VALUES ('admin', SHA2('admin123', 256));

INSERT INTO customers(name,phone,email,address,reg_date) VALUES
('Ramesh Kumar','9876543210','ramesh@gmail.com','15, Anna Nagar, Trichy - 620018','14-05-2024'),
('Suresh Babu','9123456780','suresh@gmail.com','12, Bharathi Street, Trichy - 620001','13-05-2024'),
('Vijay Kumar','9345678901','vijay@gmail.com','8, K.K. Nagar, Trichy - 620021','12-05-2024'),
('Arun Prakash','9865321470','arun@gmail.com','22, Woraiyur, Trichy - 620003','11-05-2024'),
('Manoj Kumar','9098765432','manoj@gmail.com','5, Thillai Nagar, Trichy - 620018','10-05-2024'),
('Karthik B','8833112233','karthikb@gmail.com','17, Thuvakudi, Trichy - 620015','09-05-2024'),
('Deepak Raj','7708996655','deepakraj@gmail.com','9, Melachinthamani, Trichy - 620002','08-05-2024'),
('Prakash M','9567891234','prakashm@gmail.com','31, Srirangam, Trichy - 620006','07-05-2024');

INSERT INTO vehicles(vehicle_no,owner_name,vehicle_type,make_model,fuel_type,year,chassis_no,engine_no,reg_date) VALUES
('TN01AB1234','Ramesh Kumar','Car','Maruti Suzuki Swift VDI','Diesel',2022,'MA3EN1ZE100123456','D13A123456789','14-05-2024'),
('TN09CD5678','Suresh Babu','Bike','Honda Activa 6G','Petrol',2021,'MB1KW2D3S01234567','JF50E1234567','13-05-2024'),
('TN02EF9012','Arun Prakash','Car','Hyundai i20 Asta','Petrol',2020,'MAT621GJ2N1234567','G4LA1234567','11-05-2024'),
('TN07GH3456','Vijay Kumar','Bike','Yamaha FZ-S V3','Petrol',2021,'ME4JC75D5M1234567','J3A1234567','12-05-2024'),
('TN11IJ7890','Manoj Kumar','Car','Tata Nexon XM','Diesel',2019,'MA1PC2EE0G1234567','R1234567','10-05-2024'),
('TN45KL2345','Deepak Raj','Bike','TVS Apache RTR 160','Petrol',2020,'MB1HA10EZJ1234567','HA1234567','08-05-2024'),
('TN38MN6789','Karthik B','Car','Kia Seltos HTK','Diesel',2021,'MZBCT45G0K1234567','D4FB1234567','09-05-2024'),
('TN59OP3456','Prakash M','Car','Maruti Suzuki Dzire VXI','Petrol',2022,'MA6CB2H3XL1234567','K12N1234567','07-05-2024');

INSERT INTO services(vehicle_no,customer_name,service_type,service_date,charges,status,kms,technician,notes) VALUES
('TN01AB1234','Ramesh Kumar','General Service','14-05-2024',2500,'Completed',25000,'Karthik','Engine oil changed, air filter cleaned'),
('TN09CD5678','Suresh Babu','Washing','13-05-2024',500,'Completed',12000,'Ravi','Full body wash'),
('TN02EF9012','Arun Prakash','Brake Service','13-05-2024',1800,'Pending',41000,'Karthik','Brake pads to be replaced'),
('TN07GH3456','Vijay Kumar','Oil Change','12-05-2024',1200,'Completed',18500,'Ravi','Engine oil replaced'),
('TN11IJ7890','Manoj Kumar','General Service','12-05-2024',2000,'Pending',56000,'Karthik','Inspection scheduled'),
('TN45KL2345','Deepak Raj','Engine Repair','11-05-2024',5500,'In Progress',33000,'Suresh','Engine tuning'),
('TN38MN6789','Karthik B','General Service','10-05-2024',2200,'Pending',29000,'Ravi','Awaiting parts'),
('TN59OP3456','Prakash M','Washing','09-05-2024',600,'Completed',8000,'Ravi','Interior and exterior wash');

INSERT INTO payments(invoice_no,vehicle_no,customer_name,service_type,amount,paid_amount,status,pay_mode,pay_date) VALUES
('INV0001','TN01AB1234','Ramesh Kumar','General Service',2500,2500,'Paid','Cash','14-05-2024'),
('INV0002','TN09CD5678','Suresh Babu','Washing',500,500,'Paid','UPI','13-05-2024'),
('INV0003','TN02EF9012','Arun Prakash','Brake Service',1800,1000,'Partial','Card','13-05-2024'),
('INV0004','TN07GH3456','Vijay Kumar','Oil Change',1200,1200,'Paid','Cash','12-05-2024'),
('INV0005','TN11IJ7890','Manoj Kumar','General Service',2000,1000,'Partial','UPI','12-05-2024'),
('INV0006','TN45KL2345','Deepak Raj','Engine Repair',5500,0,'Pending',NULL,'11-05-2024'),
('INV0007','TN38MN6789','Karthik B','General Service',2200,0,'Pending',NULL,'10-05-2024'),
('INV0008','TN59OP3456','Prakash M','Washing',600,600,'Paid','Cash','09-05-2024');
