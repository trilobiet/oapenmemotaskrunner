
server.port=8085

spring.datasource.url=jdbc:mysql://12.34.56.123:3306/oapen_memo?reconnect=true&rewriteBatchedStatements=true
spring.datasource.username=trilobiet
spring.datasource.password=******

logging.file.name=${user.home}/[...]/oapenmemo/logs/oapen_memo-taskrunner.log
logging.level.root=INFO
logging.level.oapen.memoproject.taskrunner=INFO

docker.image.python=oapen/ubuntu2204python310
path.temp.pythonscripts=${user.home}/[...]/oapenmemo/tmp_python/
path.exports=${user.home}/oapen/oapenmemo/exports/

spring.jpa.show-sql=false
spring.jpa.properties.hibernate.format_sql=false

# second, minute, hour, day of month, month, and day of week
taskrunner.cronschedule=0 0 2 * * *

# period during which no client calls will be processed 
# this should start at the same time as cronschedule and extend for enough hours
# to reasonably allow the taskrunner to finish all tasks 
taskRunner.busy.starttime=02:00
taskRunner.busy.hours=4

# set to false for testing
path.temp.pythonscripts.purge=true