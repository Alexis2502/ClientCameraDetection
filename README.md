# ClientCameraDetection

Client app that uses REST API.
All the dependencies have been added to pom.xml file, the main ones being Lombok, Jackson- Data Binding, Apache HttpClient5, slf4j-simple, Webcam-Capture, Guava.

For uploading a new photo is responsible endpoint REST POST /api/v1/photos. A query multipart/form-data is send to the endpoint and photo that is placed under key imageToAnalyse. Endpoint requires login and password, for that was used header Authorization: Basic. 

Stopwatch is responsible for turning the camera on every minute and is reseted after after each minute.
I used a Webcam for turning the camera on and taking a picture. I used function Webcam.getDefault(), because I am using an integrated webcam.
