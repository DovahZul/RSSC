## Remote System Scripts Controller (RSSC)<br />
### Client-server application developed using javaFX and SQlite.<br />
#### Set up & execution of regular bash commands from the remote client.<br />    
-------
#### How do i use this?  
The tool is purposed for custom enviromantal script schedule kind of [crond](https://en.wikipedia.org/wiki/Cron) (no support for Windows yet).
Server side holds its own database with scheduled commands, which will be executed repetitively.

#### Deployment instructions
* clone&build:  

        git clone https://github.com/DovahZul/RSSC.git
        cd RSSC

* for server side:  

        mvn -f server_pom.xml clean package
        java -jar target/Server-RSSC...

* for client side:  

        mvn -f client_pom.xml clean package
        java -jar target/Client-RSSC...

##### Dependencies
+ Server
    * SQLite DJBC 3.31.1

+ Client
    * javafx 11
#### Configuration
 - in progress
#### Database configuration
 - in progress
#### How to run tests
 - in progress
#### User guide
![Client main view](/screenshots/screenshot_client_main.png)

Server can execute single-line commands directly from inner schedule.
command has such properties as:
* active - enables or disables command;
* timestamp properties - time values when command will be executed. 
    + Possible time properties: 
    * second (1-60 or -1 as ignore property)
    * minute (1-60 or -1 as ignore property)
    * hour (1-60 or -1 as ignore property)
    * day (1-31 or -1 as ignore property) - day of current month according to local system date
    * month (1-12 or -1 as ignore property) 

For example, "notify-send hello bash!" will be executed every 10, 20, 30, 40, 50, 60 second, i.e. every 10 seconds infinitely while server runs.




