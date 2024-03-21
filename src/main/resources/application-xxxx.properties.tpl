
server.port=8085

spring.datasource.url=jdbc:mysql://12.34.56.123:3306/oapen_memo?reconnect=true&rewriteBatchedStatements=true
spring.datasource.username=trilobiet
spring.datasource.password=******

logging.level.root=INFO
logging.level.oapen.memoproject.taskrunner=INFO

spring.jpa.show-sql=false
spring.jpa.properties.hibernate.format_sql=false

# set to false for testing
path.temp.pythonscripts.purge=true