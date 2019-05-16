# Data Cleaning project purpose

This is a small little project that does some "normalizations" of fields in a CSV file in `stdin` and outputs it to `stdout`.
These normalizations are described in `PROBLEM_README.md`.

# Implementations, or, "Why is there a Java and Python version?"
XXXXX

# Usage

## Python
### Setup
* Install Python 3.6+ must be installed
* If using pip package manager, run `pip install pandas`
* If using Anaconda package manager, run `conda install pandas`

## Java
### Setup
* Install Java JDK 1.8+ from https://www.oracle.com/technetwork/java/javase/downloads/index.html
* Install Maven 3.0.x from https://maven.apache.org
* This was tested with Oracle Java, but OpenJDK should work too.

### Building
The Java version is built using Maven.
It can be built with `mvn clean package`. This will generate a Java jar file with all the libraries needed in 
`target\data_cleaning_proj-0.0.1-SNAPSHOT-jar-with-dependencies.jar`.
It will also run all unit tests found in `test/com/edgriebel/dataCleaning` as part of building.
The build will fail immediately if there are errors in the unit tests.

### Running
This will execute the app: `mvn exec:java < filein.csv > fileout.csv`. 
After it's packaged, it can also be run by typing 
`java -jar target/data_cleaning_proj-0.0.1-SNAPSHOT-jar-with-dependencies.jar < filein.csv > fileout.csv`

### Notes
A nice summary of the project dependencies, test resuts, and test coverage can be seen by 
running `mvn site:run` and then going to `http://localhost:8080`

### Areas for improvement
While the conversion methods are broken out into separate methods and tested, the main method in Cleaner.java is monolithic.
This should be refactored into separate, testable methods.
