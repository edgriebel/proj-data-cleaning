# Data Cleaning project purpose

This is a small little project that does some "normalizations" of fields in a CSV file in `stdin` and outputs it to `stdout`.
These normalizations are described in `PROBLEM_README.md`.

# Implementations, or, "Why is there a Java and Python version?"
I did some basic research on the problem in a Python notebook (`Experiments.ipynb`) 
to get a sense for the data, test out some conversions, and explore character set 
issues and invalid UTF-8 characters with the files.

Based on this, I started creating a verison in Java. 
I ran into some issues with UTF-8 character conversions not appearing to work properly.
I also had two problems with the library I used to read and write CSV files, 
[OpenCSV](http://opencsv.sourceforge.net) because
Another thing that was a problem 

Because of these issues, I pivoted to using my Python noodling as to a solution. I started by
converting the notebook to a Python file with `jupyter nbconvert --python Experiments.ipynb`
and renaming it (to `convert.py`).
Once I had that created I had to make minimal tweaks to the code to run as a standalone program.
I was able to quickly complete this version because of the functionality that 
[Pandas](https://pandas.pydata.org) provides for reading and writing data, 
and the rich data conversions available in the standard Python library.

Since the Python version went so quickly, I went back and figured out the problems with the Java version of
the program.

# Usage

## Python Version
### Setup
* Python 3.6+ must be installed
* Install Pandas library
  * If using pip package manager, run `pip install pandas`
  * If using Anaconda package manager, run `conda install pandas`

### Running
Run `./cleaner.py < infile.csv > outfile.csv`. Status checkpoints are printed to stderr.

### Libraries Used
* [Pandas](https://pandas.pydata.org) to read and write CSV files. I have used this library
in the past for machine learning and big data work and find it saves writing a lot of "plumbing code".

### Known Bugs
* The value of fractional seconds (milliseconds) differs from the Java version.

### Areas for Improvement
* Pandas stores numbers as double-precision. These numbers are output with more precision than necessary.
* Create some unit tests with Python `unittest` library


## Java Version
### Setup
* Install Java JDK 1.8+ from https://www.oracle.com/technetwork/java/javase/downloads/index.html
* Install Maven 3.0.x from https://maven.apache.org
* This was tested with Oracle Java, but OpenJDK should work too.

### Quick-Start
The script `run_java.sh` will build and run the application with a single command. 
Assuming you wanted to normalize file `datafile.csv` into `datafile.norm.csv`, you would run
`./run_java.sh < datafile.csv > datafile.norm.csv`

### Building
The Java version is built using Maven.
It can be built with `mvn clean package`. This will generate a Java jar file with all the libraries needed in 
`target\data_cleaning_proj-0.0.1-SNAPSHOT-jar-with-dependencies.jar`.

All unit tests found in `test/com/edgriebel/dataCleaning` are executed as part of build.
The build will fail immediately if there are errors in the unit tests.

Code coverage metrics are created by [JaCoCo](https://www.eclemma.org/jacoco) based on unit tests run.
These are easiest viewed with the "Site Report" described under _Notes_

### Running
The easiest way to run the application is with `run_java.sh` as described above in _Setup_.
To execute the app in test mode, run: `mvn exec:java < filein.csv`. This will print the normalized 
CSV file to stdout, but it will also output Maven build information to stdout.

After it's packaged, it can be run manually by typing
`java -jar target/data_cleaning_proj-0.0.1-SNAPSHOT-jar-with-dependencies.jar < filein.csv > fileout.csv`. 
This is what `run_java.sh` does after building it.

### Notes
A nice summary of the project dependencies, test resuts, and test coverage can be seen by 
running `mvn site:run` and then going to `http://localhost:8080`.
You can view the test results by clicking on _Project Reports_

### Libraries Used
This uses the OpenCSV library (http://opencsv.sourceforge.net). It is a pretty capable library, but some things
required more work than seemed necessary, like creating a new class just to control the column order on output. 
Also it is clunky to round-trip a CSV file, a lot of the problems I had were because input and output
formats were different but formatting is controlled per CSV record class.

### Known Bugs
* The header for Java isn't in exactly the same format as the original file, the words should be capitalized.
There is caused by how the header is generated and sorted.

### Areas for improvement
* While the conversion methods are broken out into separate methods and tested, the main method in Cleaner.java is monolithic.
This should be refactored into separate, testable methods.
* Error handling for the conversion methods involves returning `null` or `"0"` and not giving much feedback, this should be more robust for a production system.
