# MEMO Task runner

A service that on a daily schedule queries the MEMO database to select and run MEMO tasks. 

Tasks consist of SQL queries and Python scripts that are stored in the database and when selected for execution are run in an instantly created Docker container. The Task runner then captures the Python output and saves it as a file for the associated client to be downloaded.

Selection of tasks is based on a task's start date and run frequency (see below, Annex: dates and frequencies).

On each run any existing older version of the output file is overwritten.

## Python + SQL run environment

Python scripts and SQL queries are created and edited by MEMO Manager users and saved as text strings in the database.

### Script restrictions (Code Guard)

Python scripts are inspected by a Code Guard in the Task runner before being executed. Though they can not do much harm as they are run inside a Docker container, certain commands are forbidden.

Forbidden instructions are:
* `exec`
* `eval`
* `open`
* `os.`
* `subprocess.`

Invoked Python libraries are *not* inspected, so when any of these instructions occur in an installed library, that could compromise security. Then again, Python libraries can only be installed by a system administrator.

When Task Runner encounters illegal instructions the script will not run and an error is thrown and logged.

    Illegal instructions found: {trilobiet:file_xml=[open()]} 


### SQL restrictions

The Task runner poses no restrictions on what SQL queries can do, and thus, for safety, restrictions must be defined in the database. In order to avoid unwanted deletes or updates, database access should be allowed only to a user with `read` but not `write` permissions.


### Installing of extra Python packages

When extra Python library packages are needed, a new Docker image must be created, containing the extra package (see below).


## Configuration

* `server.port`   
   Choose a free port (e.g. 8085) to access the Task runner by HTTP, internally by the Task Manager, or externally for testing purposes or
   when the Task manager runs on a different machine. In that case map your external web server (NGINX) to the designated port.
* `spring.datasource.url`   
   Database url (`jdbc:mysql://12.34.56.123:3306/oapen_memo?reconnect=true&rewriteBatchedStatements=true`)
* `spring.datasource.username`   
* `spring.datasource.password`   
   Login credentials 
* `docker.image.python`   
   Name of Docker image to create containers from (`oapen/ubuntu2204python310`)
   See below, Annex: A Docker image to run Python scripts
* `path.temp.pythonscripts`   
   MEMO Taskrunner constructs a temporary file tree for its Python scripts and included queries and libraries.
   This will be the root of that directory (`${user.home}/oapenmemo/tmp_python/`). You can choose, for debugging purposes,
   to not remove the generated Python files after a task has finished running (setting `path.temp.pythonscripts.purge`).    
   See below (Annex: Temporary Python script file tree) for detailed information about the temporary file tree.
* `path.temp.pythonscripts.purge`   
   Remove temporary Python file tree for tasks that have finished running (set to `true`)  
* `path.exports`   
   Root directory for the generated export files (`${user.home}/oapenmemo/exports/`). OAPEN Client web gets its files from here.
   See below for a detailed explanation. 
* `taskrunner.cronschedule`   
   MEMO Task Runner runs continuously as a service, but we periodically need to select and run the defined tasks. To set a scheme
   for this purpose a cron-style syntax is used (suggested value `0 0 2 * * *`, 2.00 AM every day).   
   (`* * * * * *` = second, minute, hour, day of month, month, and day of week)
* `taskRunner.busy.starttime`   
* `taskRunner.busy.hours`   
   These two values denote a period during which no client calls (run or dry run requests through HTTP) will be processed.
   This period should start at the same time as `cronschedule` and must last long enough to allow the task runner to complete all its selected tasks.    
   (suggested values `starttime=02:00` and `hours=4`)

### Export directory structure

The export directory at `path.exports` has a sub-directory for each client containing the files generated by the task runner.
Also a directory `tmp` is available containing the results of the dry runs.

	exports
	  |-- abc_corp
	  |-- a_client
	  |-- another_client
	  .
	  .
	  |-- tmp
	  .
	  .
	  |-- zzz_corp
	  
These directories contain at most just as many files as there are tasks, because older versions of the files will be overwritten every time a task runs.	  


## Installation 

This package runs as a service. It must be listening continuously for run-requests from the Task Manager. Therefore running as a cron job is not advised.

- Copy `taskrunner-x.y.z.jar` to the user's (`oapen`) home directory;
- Create a symlink `ln -s taskrunner-x.y.z.jar taskrunner.jar`;
- In `/etc/systemd/system` create a file named `oapen-memo-taskrunner.service` with the content
  copied from [Readme-memotaskrunner-service.txt](./Readme-memotaskrunner-service.txt);
- Furthermore, Docker must be installed (version >= 24.0.7) along with the Python image (see instructions below).  


## Running tasks manually

Tasks can be run or dry-run directly through a web browser or a tool like `curl` 

* Run a task (`runtask`):

	http://localhost:8085/api/runtask/4f443c3a-6118-11ee-8c99-0242ac120002

* Dry run a task (`dryruntask`):

	http://localhost:8085/api/dryruntask/4f443c3a-6118-11ee-8c99-0242ac120002
	
The last part of the paths being the Task's `id`.	
	
Exactly these calls are made by MEMO Manager when associated buttons are pressed in the GUI. When MEMO Taskrunner and MEMO Manager are installed on the same server we can do with `localhost:[port]`. Otherwise a webserver mapping is needed to call the task runner and precautions must be taken to prevent unauthorized run requests.


### Response format
	
	{
	  "idTask": "95e4fbbd-30ff-408f-b6e1-c6694d1003a5",
	  "message": "OK",
	  "dateTime": "2024-11-28T10:02:32.325731",
	  "output": null,
	  "success": true
	}	
	
Output will always be suppressed (`null`), because we do not want to flood our browser screen with actual export contents.	


## Annex: dates and frequencies

Task frequencies are defined as `DAY`, `WEEK`, `MONTH` or `YEAR`. 

When a Task's frequency is defined as `MONTH` or `YEAR` then that Task will only run when the day of month defined in the Task's
`startDate` value exists in the current month.

> A Task with `startDate = 2024-02-29` and frequency `YEAR` will start in February 2024 and then run only every 4 years, so in February 2028, 2032 etc.

> A Task with `startDate = 2024-01-31` and frequency `MONTH` will run in January, March, May, July, August, October and December, but not in February, April, June, September and November.
    
## Annex: A Docker image to run Python scripts

Assuming Docker (>= 24.0.7) is installed the following steps describe installing the Docker Python image:

### 1. Create a Docker file

Holding the following content:

    FROM ubuntu:22.04
    
    RUN apt update
    RUN apt install -y python3.10
    RUN apt install -y python3-pip
    
    # Install module: Python MySQL connector
    RUN pip3 install mysql-connector-python
    
    # Install module: Python HTML Builder https://github.com/niklasf/python-tinyhtml
    RUN pip3 install tinyhtml
    
    # Install module: Python RSS module https://github.com/svpino/rfeed
    # (no pip available)
    RUN apt install -y wget
    RUN wget https://raw.githubusercontent.com/svpino/rfeed/master/rfeed.py
    RUN mv ./rfeed.py /usr/local/lib/python3.10/dist-packages
    
Save as `dockerfile` (or whatever name seems apt) 

### 2. Create an image from the Docker file

Now run the Docker file to create a local image:

    docker build . -t oapen/ubuntu2204python310 -f dockerfile
    
You can leave out `-f dockerfile` when that file is in the same directory and named 'dockerfile' (without extension)    

Call docker `docker image ls` to check whether the image is created indeed:

    REPOSITORY                  TAG       IMAGE ID       CREATED          SIZE 
    oapen/ubuntu2204python310   latest    8ff08ccecd71   2 minutes ago   570MB
    
#### 2.1 Docker version must be >= 24.0.7
    
> NB. Due to a bug in older Docker versions (https://github.com/moby/moby/issues/45689) you must have Docker 24.0.7 (or higher) installed.**   

This bug causes data written to `stdout` by Docker for reading by the Task Manager application to be truncated. Output sometimes appears to be closed prematurely by Docker.   
 
To check which version of Docker is currently installed: 

	docker version
	
Or a less verbose disclosure:

	docker --version 
	
Upgrading Docker:

	> sudo apt update
	> sudo apt upgrade docker 		    

After upgrading Docker, images must be re-fetched and Docker files rebuilt.

    
#### 2.2 Set user permissions

Assuming a group `docker` already was exists (when Docker was installed, otherwise run `groupadd docker`) and the user running MEMO Task Manager is named `oapen`, add this user to the `docker` group:

    usermod -aG docker oapen
    
Log out and back in or restart your service for these changes to take effect.


### 3. Run Python script in a Docker container

To test, create a directory `pythonscripts` (or any other name of your liking) and put a Python file (e.g. `test.py`) in it.

Now go to the `pythonscripts`  parent directory and run the `test.py` script in a Docker container:

    docker run --rm --network=host -v ./pythonscripts:/root/scripts oapen/ubuntu2204python310 python3 /root/scripts/test.py

**explanation**

`--rm`  
Remove container when finished (you don’t want hundreds of disposed containers cluttering your system)

`--network=host`
Using --network="host" in your docker run command, then 127.0.0.1 in your Docker container will point to your docker host ([see this post on stack overflow](https://stackoverflow.com/questions/24319662/from-inside-of-a-docker-container-how-do-i-connect-to-the-localhost-of-the-mach)).

`-v ./pythonscipts:/root/scripts`   
'Project' the contents of the `pythonscripts` directory on the host system on a directory `/root/scripts` in the container. This directory will be seen by the container as if it were local to the container.

`python3 /root/script/test.py`
Run the chosen script inside the container. 

Scripts must end with a print statement, feeding output to the shell, which will eventually be captured by the master Java process.

Running the docker `run`... command can then be delegated to Apache Commons Exec to have all this initiated from within a Java application.

 
### 4. Coping with Python requirement changes

When new modules must be installed:

- add them to dockerfile
- remove old image: `docker image remove <name>`
- rebuild new image from updated dockerfile


### 5. Copying existing image

Save the existing image to an archive:

    docker save -o <path for generated tar file> <image name>

To install: download, then run:

    docker load -i <path to image file>
    
    
## Annex: Temporary Python script file tree

Each time a task is run, MEMO Task Manager creates a temporary Python script directory under `path.temp.pythonscripts` (see Configuration above).

The root of this temporary Python script directory is given a random name.

Consider a Task named `mytask.xml` for client `ABC Corporation` (user name `abccorp`):

    67392584
       |
       |-- queries
       |      |
       |      |-- abccorp
       |      |      |
       |      |      |-- mytask_xml.py
       |      |
       |      |-- full_text_search.py
       |
       |-- sniplets
       |      |
       |      |-- mysql_connect.py
       |      |-- marc21.py
       |
       |-- main.py
       
This corresponds with the following Python file structure in `main.py`:
        
	from queries.abccorp import mytask_xml as mainquery
	from queries import full_text_search
	
	from sniplets import mysql_connect
	from sniplets import marc21

Both `queries` (without sub path) and `sniplets` refer to Library items.

Queries are saved as SQL, but are converted to a Python string named `query`. So in the example above, in `main.py` the SQL strings are available as `mainquery.query` resp. `full_text_search.query`
        
After running the task the temporary directory is removed, unless setting `path.temp.pythonscripts.purge` is `FALSE`.  




        