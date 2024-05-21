# In /etc/systemd/system create a file named oapen-memo-taskrunner.service with the following content:

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
Restart=always
RestartSec=5s

[Install] 
WantedBy=multi-user.target
