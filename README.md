## How to start the demo application

### Use class `DemoApplication`

Execute method `main` in class `DemoApplication`. This will start the embedded Tomcat server and deploy the demo application. The demo application
is available at `http://localhost:8080/demo/api`.

### Use Docker

### For manual testing

Use file `src/Dockerfile` as an example how to create a Docker image for the demo application. You first need to build
the WAR file of the demo application using `mvn package`. Then execute `$> docker build .` (don't forget the point after `build`) to
create an image. Finally, execute `$> docker run -p 8080:8080 --rm <IMAGE_ID>` to start the container.

### For integration testing

Call `mvn verify` to start the integration tests.  
Then the integration tests will be executed on a random and available port. All integration tests must be located in 
the `src/test/java` directory and must end with `Tests`. See class `DemoApplicationTests` for an example.
