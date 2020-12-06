# MPCS-51410-OOP
This repo is the final project of my course MPCS-51410 Object-Oriented Programming. It implemented REGIE: a simplified course registration system using Java and JDBC.   

Please configure the database first before running the tests.   
To compile the source files:   
cd REGIE  
make src  

To run test TestXxx.java:  
javac -cp .:junit-4.13.jar:hamcrest-core-1.3.jar -d . ./test/TestXxx.java  
java -cp .:junit-4.13.jar:hamcrest-core-1.3.jar:mysql-connector-java-5.1.44-bin.jar org.junit.runner.JUnitCore REGIE.test.TestXxx  
