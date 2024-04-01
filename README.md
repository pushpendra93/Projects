1. Set s3 bucket configuration in application.properties file 
aws.bucket.name=application-bucket
aws.bucket.region = ap-east-1
2. run command "mvn clean install"
3. run command "java -jar Log-Search\target\Log-Search-0.0.1-SNAPSHOT.jar"

4. Curl :: curl --location 'http://127.0.0.1:8080/log/search?keyword=Hello&from=1711937987&to=1711938019'
