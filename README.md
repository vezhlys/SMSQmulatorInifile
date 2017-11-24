# Inifile

Dependency for [SMSQmulator](https://github.com/vezhlys/SMSQmulator "SMSQmulator"). Holds map of program options which can be overridden by reading/writing values from/to SMSQmulator.ini. In theory this library can be used for other projects though it probably partially overlaps with java own Properties.

More info from original author can be found by generating javadoc.

## Build
mvn clean install

## Usage with SMSQmulator
Copy SMSQmulatorInifile-1.0.jar to /lib folder (relatively to SMSQmulator jar file). In case of original [SMSQmulator](http://www.wlenerz.com/SMSQmulator/) rename it Inifile.jar. 