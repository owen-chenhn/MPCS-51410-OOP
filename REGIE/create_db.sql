create database if not exists REGIE;
use REGIE;

drop table if exists User;
create table User(id varchar(12) primary key,
                    name varchar(20),
                    password varchar(20),
                    department varchar(30),
                    role varchar(10));

drop table if exists Course;
create table Course(courseId varchar(10) primary key,
                    course_name varchar(20),
                    instructorId varchar(12),
                    department varchar(30),
                    course_time varchar(20),
                    room varchar(20),
                    maxNum int,
                    regNum int,
                    course_description varchar(50));

drop table if exists RegistrationList;
create table RegistrationList(studentId varchar(12),
                    courseId varchar(10));

drop table if exists GradeList;
create table GradeList(studentId varchar(12),
                    courseId varchar(10),
                    grade char(1),
                    primary key (studentId, courseId));

drop table if exists PrerequisiteList;
create table PrerequisiteList(courseId varchar(10),
                    prereq_course varchar(10));

drop table if exists RequestList;
create table RequestList(studentId varchar(12),
                    courseId varchar(10));



insert into User values 
("133224", "Jack Marthon", "abc12345", "math", "student"), 
("133225", "Mary Stony", "cccd5523", "computer science", "student"),
("133226", "Anny Ye", "aaad5523", "computer science", "student"),
("133227", "Pierror William", "bbbd5523", "computer science", "student"),
("133228", "Miller Matheww", "gggd5523", "computer science", "student"),
("133229", "Bucher Christina", "bucher523", "math", "student"),
("133230", "Prado Aledran", "aasc5523", "math", "student"),
("133231", "Banks Justine", "sdfa5523", "math", "student"),
("i244653", "Noah Oliver", "noahpsd1123", "math", "instructor"),
("i912234", "Avery Sofia", "sofia8244", "computer science", "instructor"),
("i235667", "Donny Clark", "don118244", "math", "instructor");

insert into Course values 
("MATH28553", "History of Math", "i244653", "math", "10:00-12:00 wed", "math263", 40, 22, "Introducing history of math."),
("MATH28554", "Discrete Math", "i244653", "math", "14:00-16:00 tue", "math211", 40, 30, "This course is all about discrete mathematics."),
("MATH28555", "Intro to Math", "i244653", "math", "14:00-16:00 mon", "math263", 40, 36, "An introduction course to math."),
("MATH28560", "Financial Math", "i235667", "math", "12:00-13:50 fri", "math263", 40, 36, "An introduction course to financial math."),
("MATH28570", "Application of Math", "i235667", "math", "18:00-20:00 mon", "math263", 40, 36, "Introducing the application of math."),
("CMSC62455", "Intro to programming", "i912234", "computer science", "16:30-18:30 fri", "cs012", 30, 21, "An introduction course to programming languages."),
("CMSC62456", "Algorithms", "i912234", "computer science", "8:30-10:30 wed", "cs011", 30, 21, "Intermediate course of physics."),
("CMSC63457", "Advanced Java", "i912234", "computer science", "8:00-10:00 tue", "cs351", 20, 16, "Advanced course of programming language Java.");

insert into RegistrationList values 
("133224", "MATH28570"),
("133224", "CMSC62456"),
("133224", "MATH28555"),
("133225", "CMSC62456"),
("133229", "MATH28553"),
("133229", "CMSC62455");

insert into GradeList values 
("133224", "MATH28553", 'A'),
("133224", "MATH28554", 'B'),
("133224", "MATH28555", 'F'),
("133225", "MATH28555", 'A'),
("133229", "CMSC62455", 'B'),
("133230", "CMSC62455", 'A'),
("133231", "CMSC62455", 'A');

insert into PrerequisiteList values 
("MATH28555", "MATH28554"),
("MATH28555", "MATH28553"),
("CMSC62456", "CMSC62455"),
("MATH28560", "MATH28555"),
("CMSC63457", "CMSC62455");

insert into RequestList values 
("133224", "MATH28560"),
("133225", "CMSC63457"),
("133227", "CMSC62456"),
("133228", "CMSC62456"),
("133225", "MATH28554"),
("133229", "MATH28555");