<h2>JourneyMate</h2>
<h2>Brief desciption</h2>
 <p>This project is application for finding routes and destionations and rate it.</p>
 <p>The backend service is with monolith architecture. It is a REST API and its using JWT tokens to authorize users.</p>
 
 <h2>Tech Stack:</h2>
<ul>
 <li>Java</li>
 <li>Spring Boot</li>
 <li>PostgresSQL</li>
 <li>Docker</li>
 <li>Cloudinary (for storing images)</li>
 <li>Swagger-UI (GUI for REST API documentation)</li>
</ul>

<h2>ER diagram:</h2>
<img src="./readme-images/journeymate_er.png" alt="Journeymate ER diagram">

<h2>API:</h2>
<img src="./readme-images/AuthController.png" alt="Journeymate API">
<img src="./readme-images/UserController.png" alt="Journeymate API">
<img src="./readme-images/RouteController.png" alt="Journeymate API">
<img src="./readme-images/CommentController.png" alt="Journeymate API">
<img src="./readme-images/ReactionController.png" alt="Journeymate API">

## Local setup:
```
1. In the folder ./docker create .env file with values from .env.example.
1.1 Run docker contrainer in folder ./docker  `docker-compose up -d`
2. Test by Postman for example or something else
```
