- This project demonstrate how to create a reactive web application that connects mysql/postgres sql db with reactive
  r2dbc driver
- This project was tested with mysql 8 and postgres 13.1

## How to Run

- For mysql change db config in application.yml
- For postgres comment/uncomment following in build.gradle

```
    runtimeOnly("io.r2dbc:r2dbc-postgresql")
    runtimeOnly("org.postgresql:postgresql")

//    runtimeOnly("dev.miku:r2dbc-mysql")
//    runtimeOnly("mysql:mysql-connector-java")
```

- For postgres comment/uncomment following in application.yml and update db config

```
spring:
  r2dbc:
    url: "r2dbc:postgresql://localhost:5432/db_srcrud"
    username: postgres
    password: 123456
    properties:
      schema: schema_srcrud
#spring:
#  r2dbc:
#    url: "r2dbc:pool:mysql://localhost:3306/demo"
#    username: demo
#    password: 123456

```

- Create table customer with two field 

```
id primary key auto increment
name text
```

- Run application with following command

`./gradlew bootRun`





### Sample log output for making nio db call

```
2021-01-03 15:58:34.142 DEBUG 5096 --- [ctor-http-nio-2] o.s.w.r.r.m.a.ResponseBodyResultHandler  : [866d438c-3] 0..1 [com.lynas.demopostgress.Customer]
2021-01-03 15:58:34.142 DEBUG 5096 --- [ctor-http-nio-2] io.r2dbc.pool.ConnectionPool             : Obtaining new connection from the driver
2021-01-03 15:58:34.143 DEBUG 5096 --- [ctor-http-nio-2] o.s.r2dbc.core.DefaultDatabaseClient     : Executing SQL statement [select c.* from customer c where c.name=?]
2021-01-03 15:58:34.143 DEBUG 5096 --- [ctor-http-nio-2] dev.miku.r2dbc.mysql.MySqlConnection     : Create a parametrized statement provided by text query
2021-01-03 15:58:34.143 DEBUG 5096 --- [ctor-http-nio-2] d.m.r.mysql.client.ReactorNettyClient    : Request: TextQueryMessage{sqlParts=REDACTED, values=REDACTED}
2021-01-03 15:58:34.144 DEBUG 5096 --- [actor-tcp-nio-2] d.m.r.mysql.client.MessageDuplexCodec    : Decode context change to DecodeContext-Result
2021-01-03 15:58:34.144 DEBUG 5096 --- [actor-tcp-nio-2] d.m.r.m.m.server.MetadataDecodeContext   : Respond a metadata bundle by filled-up
2021-01-03 15:58:34.144 DEBUG 5096 --- [actor-tcp-nio-2] d.m.r.mysql.client.ReactorNettyClient    : Response: SyntheticMetadataMessage{completed=false, messages=[DefinitionMetadataMessage{database='rincewind', table='c' (origin:'customer'), column='id' (origin:'id'), collationId=63, size=20, type=8, definitions=4203, decimals=0}, DefinitionMetadataMessage{database='rincewind', table='c' (origin:'customer'), column='name' (origin:'name'), collationId=45, size=262140, type=252, definitions=10, decimals=0}], eof=null}
2021-01-03 15:58:34.144 DEBUG 5096 --- [actor-tcp-nio-2] d.m.r.mysql.client.ReactorNettyClient    : Response: RowMessage(encoded)
```

### CURL sample request response

```
$ curl http://localhost:8080/customer/cname/sazzad
  % Total    % Received % Xferd  Average Speed   Time    Time     Time  Current
                                 Dload  Upload   Total   Spent    Left  Speed
100    24  100    24    0     0   2400      0 --:--:-- --:--:-- --:--:--  2400{"id":3,"name":"sazzad"}

```