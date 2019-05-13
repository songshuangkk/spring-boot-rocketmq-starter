#Spring RocketMQ Starter

### Install

* maven:
    
    ```jshelllanguage
       <groupId>com.github.songshuangkk</groupId>
          <artifactId>spring-boot-rocketmq-starter</artifactId>
       <version>1.0-SNAPSHOT</version>
    ```
    
### Start
    
 * Consumer: Using Annotation.
 ```java
@RocketConsumer
```

* Name Server Config:
```yaml
rocket: 
    mq:
      name-server-addr: localhost: localhost:9876
```