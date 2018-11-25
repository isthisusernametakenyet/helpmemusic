#!/bin/bash

javac -d www/WEB-INF/classes -cp javax.servlet-api-3.1.0.jar:www/WEB-INF/lib/org.json.jar:www/WEB-INF/src:. www/WEB-INF/src/servlet/UserServlet.java
