# Android App, Web Service and Dashboard

This is a distributed systems application which takes the input from the user, calls the servlet which calls the API, sends back to mobile app and displays the picture on the app. Below are the details of the implementation:

## 1.	Native Android application
My application uses TextView, EditText, Button, and ImageView. See content_main.xml for details of how they are incorporated.
 

b.	Requires input from user
User types in a word
 

c.	Makes an HTTP Request
My application does a HTTP Get request in CatPicture.java to the servlet on codespaces. The request is:

"https://m-asadshoaib-ubiquitous-funicular-pv7qvvg7wjwf747r-8080.preview.app.github.dev/cat-servlet?name=" + searchTerm;

d.	Receives and parses a JSON from the web service
An example:
{"pictureURL":"https://cataas.com/cat/zCOGzLIUYUL7U6dz/says/hey"}

e.	Displays new info to the user
 
f.	Is repeatable
Allows user to repeatedly input without restarting the app.


## 2.	Implements a web application deployed to codespaces
The URL of my service deployed to codespaces is:

https://m-asadshoaib-ubiquitous-funicular-pv7qvvg7wjwf747r-8080.preview.app.github.dev

a.	Using a HTTP Servlet to implement a simple API
In my program deployed, the following are implemented:

Controller: CatServlet

Model: WebCatGetModel

View: dashboard.jsp

Additionally, I am storing things in a POJO titled MongoP4T2.

b.	Recieves a HTTP request from the android application 
My CatServlet receives the HTTP GET request with the argument search term under “name” and then passes this on to model.

c.	Executes business logic appropriate to application
WebCatGetModel makes the appropriate call to https://cataas.com/cat/ . It then parses the JSON response and extracts parts needed to send to the android application. 

This also computes the statistics to be shown in the operational analytics section of the dashboard

d.	Replies to android with JSON
The below is sent to the android application. This tag and JSON has been made by myself.
 

## 4.	Log useful information
Date and Time: to keep a track of when the requests were made
Search Term: to check what is input by the user for searching
API Response: to see what API is returning
Device: to see where the requests are being made from
Time Taken: to understand latency per request
Sent to Phone: to see what is being sent to the phone

## 5.	Store the log information in mongoDB
All the data is being stored in “Dashboard” database under collection “Results”. My connection string is:
mongodb://mshoaib:H87NyDqXcAlTIOde@ac-41xfnxe-shard-00-02.fbowljg.mongodb.net:27017,ac-41xfnxe-shard-00-01.fbowljg.mongodb.net:27017,ac-41xfnxe-shard-00-00.fbowljg.mongodb.net:27017/test?w=majority&retryWrites=true&tls=true&authMechanism=SCRAM-SHA-1

## 6.	Display operational analytics and full web based dashboard

The URL is: https://m-asadshoaib-ubiquitous-funicular-pv7qvvg7wjwf747r-8080.preview.app.github.dev/dashboard

 
