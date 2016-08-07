Requisites:

just need Java 8, and Chrome browser.
--------------------------------

Build:

To build a jar with maven, use in root folder : mvn clean compile assembly:single
-------------------------------

Test:

To test this application I used Chrome and Postman, I include a json postman to test api.
Also, there are tests validating api and access page management.
--------------------------------
Admin user for API

 "admin", "admin"

--------------------------------
User access to pages:

Page 1 : "edu", "12345"
Page 2 : "toni", "5678"
		 "juan", "7788"
		 
Page 3 : "juan", "7788"

---------------------------------

Comments:

I didn't use RxJava because I do not have enough knowledge to apply it, 
i have few ideas where to use it, for example, from api consuming an observable in service class. 

I had some problems trying to send authorization key in headers in each request, I tried adding cors with no success, this is why I needed to send authorization key as query string instead in headers.


