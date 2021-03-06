#Annotators

Some text annotators supporting fungal enzyme discovery.

##mycoMINE


A NLP pipeline that supports the extraction of relevant mentions from literature related to fungal enzymes. 

- requires GATE 7.2 and Java 1.7 or higher
- the application has been exported with its resources, but mycoMINE/application-resources/proteins/ac2sq.zip must be unzipped before running the pipeline.
- usage:

  -- in GATE GUI: ```Applications > Restore Application from File... > application.xgapp```
 
  -- in java projects: do not forget to set System property ```gate.home``` to your gate installation directory, and to make ```gate.jar``` and all libraries under ```gate/lib``` directory available on your classpath environment variable. (integration examples to be found [here](http://gate.ac.uk/wiki/code-repository/))

Please consult the lib .jar files for their respective licenses.



##substrateMINE


A GATE-based pipeline for substrate name tagging.

- requires GATE 7.2 and Java 1.7
- the application has been exported with its resources
- usage:

  -- in GATE GUI: ```Applications > Restore Application from File... > application.xgapp```
 
  -- in java projects: do not forget to set System property ```gate.home``` to your gate installation directory, and to make ```gate.jar``` and all libraries under ```gate/lib``` directory available on your classpath environment variable. (integration examples to be found [here](http://gate.ac.uk/wiki/code-repository/))

Please consult the lib .jar files for their respective licenses.

