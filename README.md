I'll updating this repo with almost all the information, this is my twitter, please follow me to updates questions or suggestions.
[@acardenasnet](https://twitter.com/acardenasnet)

To start you need execute the below commands ( If you already did it with `order-service`, then skip this step:

```shell script
docker run -d --name=dev-consul -e CONSUL_BIND_INTERFACE=eth0 -p 8500:8500 consul
docker run -d -e CONSUL_BIND_INTERFACE=eth0 consul agent -dev -join=172.17.0.2
docker run -d -e CONSUL_BIND_INTERFACE=eth0 consul agent -dev -join=172.17.0.2
```

After this you can start this service executing:

````shell script
mvn sprong-boot:run
````

Remember set the name to your project:
```properties
spring.application.name=coupon-service
```

The name that you use is the name that will use to register in consul, in this branch you only can run one instance of `coupon-service`.
`coupon-service` is configured to star in a dynamic port, that means that every time could use a different port, with this we can see that `order-service` could call `coupon-service` without knows the real ip and port, due to `consul` will handle that and provide the instances availables registered.

The following property said to spring that can use a dynamically port.
```properties
server.port=0
```

To allow register many instances of this services into `consul` we need create a different name to each instance, I'm showing the basic method, but if you will use in a real environment it's good idea use a complex name,

```properties
spring.cloud.consul.discovery.instance-id=${spring.application.name}:${random.value}
spring.cloud.consul.discovery.tags=javaDevDayMx2019
spring.cloud.consul.discovery.prefer-ip-address=true
spring.cloud.consul.host=localhost
spring.cloud.consul.port=8500
```

In the above snippet also added `tags`, and shows the properties used to connect to consul `host` and `port`
