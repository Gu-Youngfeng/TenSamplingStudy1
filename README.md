## TenSamplingStudy1


### 1. About the project

This project is cloned from the open-source implementation in paper "A Comparison of 10 Sampling Algorithms for Configurable Systems.", some Chinese comments will be added into the code. The original code can be download from

[http://www.dsc.ufcg.edu.br/~spg/sampling/](http://www.dsc.ufcg.edu.br/~spg/sampling/)

Please cite the paper if you want to reuse this project.

> Fl¨¢vio Medeiros, et al. "A Comparison of 10 Sampling Algorithms for Configurable Systems." IEEE/ACM International Conference on Software Engineering IEEE, 2016.


### 2. Inside the project

Brief introduction of project constructure.

1. Folder `bin/` saved the bytecode of Java code. 
2. Folder `bugs/` included the fault related files in 24 **C** projects, followed by each file there are some `.config` files which gave the different configurations to trigger the corresponding fault.
3. Folder `tables/` contained the covering array in T-wise algorithms.
4. Folder `src/` saved the relative Java code.   
5. File `bugs.xls` detailed the information of 135 faults.


### 3. Bugs Fixing

##### 1. We should remove the Line 120 in `checkers.BugChecker.java`, since it will mislead the calculation on Line 137.

```java
...
/** LINE 119 */ List<List<String>> samplings = algorithm.getSamples(new File(BugsChecker.SOURCE_LOCATION + project + "/" + path));
/** LINE 120 */ this.configurations += samplings.size();
...
/** LINE 136 */// Total number of configuration / total number of files in all projects.
/** LINE 137 */System.out.println("Configurations per file:" + ((double)configurations)/50078);
...
```

##### 2. We should replace the **Math.pow(directives.size(), 2)** with **Math.pow(2, directives.size())** on Line 28 and 47 in `core.algorithms.RandomSampling.java`, since $2^n$ should be written in **Math.pow(2,n)**.

```java
...
/** LINE 28 */ && NUMBER_CONFIGS < Math.pow(directives.size(), 2)) {
...
/** LINE 47 */ if (NUMBER_CONFIGS >= Math.pow(directives.size(), 2)) {
...
```
