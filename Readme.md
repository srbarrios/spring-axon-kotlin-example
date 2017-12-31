**Video Store test**

- I check out the framework you proposed in the test, but after play a bit with it, I considered more interesting to implement this server using the Casumo Tech stack.
- I tried to apply as much as possible the design pattern of Command Query Responsibility Segregation, I guess is the best approach using Axon Framework + Spring Boot.
- There is some acceptance tests using axon fixtures and I attach bellow the postman collection I used to test it manually.
- There is also some tests for API Rest, where I'm checking the test case from the requirements.
- I added a TimeProvider, which should go to production, but it was easy to test it changing dates.

**Postman Collection**

If you want to easy test this API REST, import this link in your Postman app: https://www.getpostman.com/collections/9e9f2e66cb5bf4d39554 


**Author**

Oscar Barrios (oscar.barrios@gmail.com)# spring-axon-java-example
