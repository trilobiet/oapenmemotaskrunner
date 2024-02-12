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