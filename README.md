# oapenmemotaskrunner
    
## Python scripts running safely inside a Docker container

### 1. Create a Docker file

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
    
Save as dockerfile (or whatever name seems apt) 

### 2. Create an image from the Docker file

Now run the Docker file to create a local image:

    docker build . -t oapen/ubuntu2204python310 -f dockerfile
    
You can leave out `-f dockerfile` when that file is in the same directory and named 'dockerfile' (without extension)    

Call docker `image ls` to see the new image:

    REPOSITORY                  TAG       IMAGE ID       CREATED          SIZE 
    oapen/ubuntu2204python310   latest    8ff08ccecd71   2 minutes ago   570MB
    
#### 2.1 Docker version >= 24.0.7
    
NB. Due to a bug in older Docker versions (https://github.com/moby/moby/issues/45689) you must have Docker 24.0.7 (or higher) installed.   

This bug causes data written to `stdout` by Docker for reading by the Java application to be truncated. Output is closed prematurely by Docker.   
 
To check which version of Docker is currently installed: 

	docker version
	
Or a less verbose disclosure:

	docker --version 
	
Upgrading Docker:

	> sudo apt update
	> sudo apt upgrade docker 		    

After upgrading Docker, images must be re-fetched and Docker files rebuilt.

    
#### 2.2 Set user permissions

Assuming a group `docker` already was created exists (when Docker was installed, otherwise run `groupadd docker`) and the user is named `oapen`, add
it to the docker group:

    usermod -aG docker oapen
    
Log out and back in or restart your service for these changes to take effect.


### 3. Run Python script in a Docker container

Now go to the `pythonscripts` (or any other name of your liking) parent directory and run any Python script:

    docker run --rm --network=host -v ./pythonscripts:/root/scripts oapen/ubuntu2204python310 python3 /root/script/comic_books_rss.py

**explanation**

`--rm`  
Remove container when finished (you don’t want hundreds of disposed containers cluttering your system)

`--network=host`
Using --network="host" in your docker run command, then 127.0.0.1 in your Docker container will point to your docker host ([see this post on stack overflow](https://stackoverflow.com/questions/24319662/from-inside-of-a-docker-container-how-do-i-connect-to-the-localhost-of-the-mach)).

`-v ./pythonscipts:/root/scripts`   
'Project' the contents of the `pythonscripts` directory on the host system on a directory /root/scripts in the container. The directory can now be seen by the container as if it were local to the container.

`python3 /root/script/comic_books_rss.py`
Run the chosen script inside the container. 

Scripts must end with a print statement, feeding output to the shell, which can eventually be captured by the master Java process.

Running the docker run ... command can then be delegated to Apache Commons Exec to have all this initiated from within a Java application.

 
### 4. Coping with changes

When new modules must be installed:

- add them to dockerfile
- remove old image: `docker image remove <name>`
- rebuild new image from updated dockerfile


### 5. Copying existing image

Save the existing image to an archive:

    docker save -o <path for generated tar file> <image name>

To install: download, then run:

    docker load -i <path to image file>

