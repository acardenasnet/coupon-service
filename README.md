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

In order to use KV consul in our application we should create the KV into consul, you can either use the UI or use the following command:

```shell script
curl -X PUT http://127.0.0.1:8500/v1/kv/config/application/coupon.code -d '20DISCCOUNT'
``` 

The command above create the key=`coupon.code` and assign the value=`20DISCCOUNT`.

Now we can modify our code to use the annotation `@Value` to read the KV configuration, like this:

```java
  @Value("${coupon.code}")
  private String couponCode;
```

Then we can compare the coupon received against `couponCode`,

```java
coupon.equals(couponCode)
```

At this point the application should works, but if the KV change the value, it'll be not refreshed automatically, you can test changing the value with the same command above to create the KV, just with a different value ( if the key exits will update if not will created )

To solve the issue we need annotate the class with the annotation `@RefreshScope`, this allow update the value of the value injection, that means that will been updated the value.

```java
@Slf4j
@RefreshScope
@RestController
public class CouponController {
  ...
}
```

With this change you can change the value without restart your service and the changes should be reflected on the fly.

You can see the value of your KV stored in consul using the following command:

```shell script
curl http://localhost:8500/v1/kv/config/application/coupon.code
``` 

And will see a result like:

```json
[
    {
        "LockIndex": 0,
        "Key": "config/application/coupon.code",
        "Flags": 0,
        "Value": "MjBESVNDQ09VTlQ=",
        "CreateIndex": 4488,
        "ModifyIndex": 4535
    }
]
```

You con see the the value is not plain text, that is good, but we still can read it, using:

```shell script
echo 'MjBESVNDQ09VTlQ=' |base64 --decode
```

And yes the answer is, yes the value is encoding on base64.