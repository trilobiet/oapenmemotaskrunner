In /etc/systemd/system create a file named oapen-memo-taskrunner.service with the following content:

[Unit]
Description=OAPEN MEMO Taskrunner
After=syslog.target

[Service]
User=oapen
ExecStart=/home/oapen/oapenmemo/taskrunner.jar SuccessExitStatus=143

[Install] 
WantedBy=multi-user.target
