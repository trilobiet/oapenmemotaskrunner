# In /etc/systemd/system create a file named oapen-memo-taskrunner.service with the following content:
# https://www.baeldung.com/linux/run-java-application-as-service

[Unit]
Description=OAPEN MEMO Taskrunner
After=syslog.target network.target

[Service]
#Environment=JAVA_OPTS="JAVA_OPTIONS=-Xmx4G"
SuccessExitStatus=143
User=oapen
Group=oapen

Type=simple

ExecStart=java -Xmx1G -jar /home/oapen/oapenmemo/taskrunner.jar
ExecStop=/bin/kill -15 $MAINPID

Restart=always
RestartSec=5s

[Install] 
WantedBy=multi-user.target
