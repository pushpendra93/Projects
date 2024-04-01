1. cd Projects\File-Search
2. Set s3 bucket configuration in application.properties file 
aws.bucket.name=application-bucket
aws.bucket.region = ap-east-1
3. export AWS_ACCESS_KEY_ID="your access key"
   export AWS_SECRET_ACCESS_KEY="your secret key"
4. run command "mvn clean install"
5. run command "java -jar Log-Search\target\Log-Search-0.0.1-SNAPSHOT.jar"

6. Curl :: curl --location 'http://127.0.0.1:8080/log/search?keyword=Hello&from=1711937987&to=1711938019'
