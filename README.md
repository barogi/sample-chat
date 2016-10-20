#### Configure

##### web.xml
Default database type is **hsql**
```xml
    <!--
        Active database environment supported values are: mysql, hsql
    -->
    <context-param>
        <param-name>db.env</param-name>
        <param-value>hsql</param-value>
    </context-param>
```

By default spring environment profile **dev** use liquibase to recreate database schema when application starts. It activated by default only for demo. **Liquibase will drop all data and recreate schema any time when application starts.**
```xml
    <!--
        Active spring profile
    -->
    <context-param>
       <param-name>spring.profiles.default</param-name>
       <param-value>dev</param-value>
    </context-param>
```
##### JDBC settings
* **mysql** connection settings [db-mysql.properties](src/main/resources/db-mysql.properties)
* **hsql** connection settings [db-hsql.properties](src/main/resources/db-hsql.properties)

### Build
Build without tests
>     mvn  clean package -DskipTests=true

Build and run intergration test for **hsql**
>     mvn clean package -Ddb.env=mysql -Dspring.profiles.active=dev

Build and run intergration test for **mysql**
>     mvn clean package -Ddb.env=mysql -Dspring.profiles.active=dev


### Run 
Run Tomcat6 on port **8082** with **hsql** :
>     mvn tomcat6:run -Ddb.env=hsql -Dspring.profiles.active=dev -Dmaven.tomcat.port=8282

Run Tomcat6 on port **8082** with **mysql** :
>     mvn tomcat6:run -Ddb.env=mysql -Dspring.profiles.active=dev -Dmaven.tomcat.port=8282
