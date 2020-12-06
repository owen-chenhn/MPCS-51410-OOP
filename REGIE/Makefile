CFLAGS1 = -d .
CFLAGS2 = -cp .;junit-4.13.jar;hamcrest-core-1.3.jar -d .
CC = javac

main_source = $(wildcard main/*.java)
test_source = $(wildcard test/*.java)

src: $(main_source)
	$(CC) $(CFLAGS1) $^

test: $(test_source)
	$(CC) $(CFLAGS2) $^



# compile test files: 
# javac -cp .`;junit-4.13.jar`;hamcrest-core-1.3.jar -d . .\test\*.java
# run a single test: 
# java -cp .`;junit-4.13.jar`;hamcrest-core-1.3.jar`;mysql-connector-java-5.1.44-bin.jar org.junit.runner.JUnitCore REGIE.test.TestXxx
	
