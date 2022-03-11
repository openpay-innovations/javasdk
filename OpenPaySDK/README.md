# Plugint-sdk-java

Integration API
- API version: v1

## Requirements

Building the API client library requires:
1. Java 1.8+
2. Maven/Gradle
3. Google chrome version 80.x, if you have some other version like chrome v79 or v81 please install chromeDriver for the same and place in driver folder. Change the file name in merchantConfig.ini property name chromeDriverFileName
4. Also currently chromeDriver is available for windows and MAC. For any other OS please install the same and change the file name in merchantConfig.ini property name chromeDriverFileName

##System Requirements

1. Install maven 3.6.1 or above
2. Install JDK 1.8 or above
3. Set MAVEN_HOME to enviorment variables(set maven path)(For more information visit https://www.tutorialspoint.com/maven/maven_environment_setup.htm)
4. Set JAVA_HOME to enviorment variables(set java path)

## Installation

To install the API client library to your local Maven repository, simply execute:

1. Clone the project from specific git repo.
2. Use IDE for project setup like eclipse or similar
3. Import the project as import existing maven project in eclipse
4. Right click on project and click Run As --> Run Configuration --> Maven Build --> add goal - clean install -DskipTests=true
5. On success, project is successfully installed with all dependencies.

## Testing

###Perform individual Test Case-

1. Open Test Files one by one from src/main/test/java
2. Select each function and do right click --> run as Junit test, check results on console.
3. Go with above steps for all the functions one by one.
4. To update test data , check positive and negative data in folder /testData and update files accordingly.
5. Correct Order id in file testOrderPositive.txt will be written automatically on successful call of ordersPostTestPositive().
6. Redirection to URL will be automatically done on payment website and will automatically close after two minutes. We can configure this time in merchantConfig.ini file with property chromeDriverRedirectTimeout (add time in milisec)
7. We can change redirectURL value from mappingApiConfig.ini as well.

###Generate Test Report

To generate test report, right click on Project --> Run as --> Run configuration --> Maven Build --> add goal (test surefire-report:report) and run. All the test cases will run, redirection to URL will take place, complete the payment process. Once procedure is successful, refresh the project. Go to folder target --> Site --> surefire-report.html

###Logs

To check logs open folder /target/surefire-reports and can check output for each file ending with *output.txt
```
### Maven users

Add this dependency to your project's POM for SDK integration (for maven based projects)

```xml
<dependency>
  <groupId>com.plugint</groupId>
  <artifactId>com.plugint.sdk</artifactId>
  <version>1.0.0</version>
  <scope>compile</scope>
</dependency>
```

###Points to be Noted

1. Test report will be generated in /target/site/surefire-report.htm

2. Console Logs will be copied to file /target/surefire-reports/com.plugint.businessLayer.test.PlugintSDKTest-output.txt

3. Test data can be changed from files available in /testData. For each API we have one positive test case and one negative test case. For capture payment and get orders we will get order id from test order.

4. For positive test 'testOrderPositive.txt' file will be automatically written with order id during successful create new order api call.

5. Retry count for api is currently set to 3 in mappingApiConfig.ini with property as retryCount.

6. Download latest chromeDriver from link https://chromedriver.storage.googleapis.com/index.html?path=98.0.4758.102/ and add latest chromedriver to /driver folder and update config value chromeDriverFileName in file mappingConfig.ini
