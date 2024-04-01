1. Set s3 bucket configuration in application.properties file 
aws.bucket.name=application-bucket
aws.bucket.region = ap-east-1
2. export AWS_ACCESS_KEY_ID="your access key"
   export AWS_SECRET_ACCESS_KEY="your secret key"
3. run command "mvn clean install"
4. run command "java -jar Log-Search\target\Log-Search-0.0.1-SNAPSHOT.jar"

5. Curl :: curl --location 'http://127.0.0.1:8080/log/search?keyword=Hello&from=1711937987&to=1711938019'
