## Remote System Scripts Controller (RSSC)<br />
### Client-server application developed using javaFX and SQlite.<br />
#### Set up & execution of regular bash commands from the remote client.<br />    
-------
* How do i use this?
The tool is purposed for custom enviromantal script schedule kind of [crond](https://en.wikipedia.org/wiki/Cron) (no support for Windows yet).
Server side holds its own database with scheduled commands, which can be executed once, or repetitively.

#### Deployment instructions
    + clone&build
        * git clone https://github.com/DovahZul/RSSC.git
        * cd RSSC
        * mvn -f server_pom.xlm (for server side)
        * mvn -f client_pom.xml (for client side)
        * java -jar target/RSSC...
##### Dependencies
+ Server
    * SQLite DJBC 3.31.1

+ Client
    * javafx 11
##### Configuration
 - 
#### Database configuration
 - 
#### How to run tests
 - 


