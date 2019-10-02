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
```yaml
spring:
    application:
        name: order-service
```

The name that you use is the name that will use to register in consul, in this branch you only can run one instance of `order-service` at less locally due to by default the port to run the app is `8080`, in the branch `final`,  you will see the configuration complete and you will be able to run many instances of this.

Into the controller can see the following snippet:

```java
  @Autowired
  private DiscoveryClient discoveryClient;
  
  public Optional<URI> serviceUrl() {
    return discoveryClient.getInstances("coupon-service")
        .stream()
        .map(serviceInstance -> serviceInstance.getUri())
        .findFirst();
  }
```

The method above returns all the instances registered in Consul, healthy and unhealthy, in the branch `load_balanced` I will explain how avoid to bring all the instances and only retrieve the healthies



