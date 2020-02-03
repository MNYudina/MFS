## 1. Purpose
The project allow to count  all directed subgraphs statistically.
See for details [here](https://mnyudina.github.io/publications/)

## 2. Sources
To use the code, you should download the repository and install the libraries based on the provided pom.xml file. The libraries used are common-collection, colt, jung, junit, common-cli. The program is written in java 8, respectively, you must use> = jdk 8

## 3. Launch
If the JRE is installed, then the program can be launched using the command line:

* java -jar target/RFS-0.1.jar

### The following parameters are taken into account

#### -g
It indicates used network graph file. 
 
#### -op
It indicates using operation

 * scfe - get number of 3-size directed subgraphs by full enumeration algorithm
 * 3scs - get number of 3-size directed subgraphs by sampling algorithm
 * 4scfe - get number of 4-size directed subgraphs by full enumeration algorithm
 * 4scs - get number of 4-size directed subgraphs by sampling algorithm separated by comma.


#### -r
This parameter sets number of runs used by sampling algorithms only 
 
## 4. Examples of use

Examples are presented in run.bat file