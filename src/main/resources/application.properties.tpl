
spring.profiles.active=@activatedProperties@

logging.file.name=${user.home}/oapen/oapenmemo/logs/oapen_memo-taskrunner.log

docker.image.python=oapen/ubuntu2204python310
path.temp.pythonscripts=${user.home}/oapen/oapenmemo/tmp_python/

# set to false for testing
path.temp.pythonscripts.purge=false