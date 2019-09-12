# RPL-2.0
RPL Final Assignment FIUBA

This service has three components, the producer backend, the consumer backend and the message broker.
You can also add a MySQL server to persist data. If not, an H2 in-memory database is used (so as soon 
as you close the JVM, all data is lost) 

## Endpoints

A `swagger.json` is generated every time functional tests are run.
To see them in a nice UI you can copy the file to [Swagger Editor](https://editor.swagger.io/).

You can also parse the `swagger.json` to a `swagger.yaml` by using [api-spec-converter](https://github.com/LucyBot-Inc/api-spec-converter)
```shell script
npm install -g api-spec-converter
api-spec-converter --from=swagger_2 --to=swagger_2 --syntax=yaml swagger.json > swagger.yaml  
```
 

### `/health`
When you make a GET to `/health`, the service responds with a `"plain/text"` "pong".

---------
### `/submit`
When you make a POST to `/submit` with a body with the following structure:
```json
{
  "quantity": 100
}
```
The producer send a `"quantity"` ammount of messages to the broker, which are then received by the consumer.

----------
## Bootstrapping

### Local
#### Configuration
- Download and install [Docker](https://docs.docker.com/install/)
- If using IntelliJ: copy `./RplApplication_Consumer.xml` and `./RplApplication_Producer.xml` to your `./.idea/runConfigurations/` folder. This will create two running configuration, one for running the producer and other for running the consumer.

#### Unit and Functional Tests
```shell script
./gradlew clean && ./gradlew check
```


#### Running the service
- Start rabbitmq with:
```shell script
docker run -p 9999:15672 -p 5672:5672 deadtrickster/rabbitmq_prometheus:latest
```
- Run Gradle build.
- Start producer with the `RplApplication Producer` configuration.
- Start consumer with the `RplApplication Consumer` configuration.
- Optional: you can view the data of the broker in `http://localhost:9999/`.

### Local with minikube
#### Configuration
- Download and install [Minikube](https://kubernetes.io/docs/tasks/tools/install-minikube/)
- Download and install [kubectl](https://kubernetes.io/docs/tasks/tools/install-kubectl/)

Both the consumer and the producer use the `alepox/rpl-2.0:latest` Docker image (see `kubernetes/deployment/consumer.yaml` and `kubernetes/deployment/producer.yaml`). If the image was not pushed yet you should use the following commands:

```shell script
docker commit -m "First commit" -a "alepox" <CONTAINER_ID>
docker tag rpl-backend alepox/rpl-2.0:latest
docker push alepox/rpl-2.0:latest
```

With the following credentials:

```
user: alepox
password: ?_@vn2w!@CZjVZh
```

#### Running the service
- Start a kubernetes cluster in Minikube with:
```shell script
minikube start \
  --memory 8096 \
  --extra-config=controller-manager.horizontal-pod-autoscaler-upscale-delay=1m \
  --extra-config=controller-manager.horizontal-pod-autoscaler-downscale-delay=2m \
  --extra-config=controller-manager.horizontal-pod-autoscaler-sync-period=10s
```

- Create the message broker deployment and service:
```shell script
kubectl create -f ./kubernetes/deployment/queue.yaml
kubectl create -f ./kubernetes/service/queue.yaml
```
- Create the producer deployment and service:
```shell script
kubectl create -f ./kubernetes/deployment/producer.yaml
kubectl create -f ./kubernetes/service/producer.yaml
```
- Create the consumer deployment and service:
```shell script
kubectl create -f ./kubernetes/deployment/consumer.yaml
kubectl create -f ./kubernetes/service/consumer.yaml
```
- Get the ip address of the cluster:
```shell script
minikube ip
```
- Get ports of each service:
```shell script
kubectl get services
```
> Output:
```
NAME         TYPE        CLUSTER-IP      EXTERNAL-IP   PORT(S)                                                         AGE
consumer     NodePort    10.99.192.154   <none>        8888:31000/TCP                                                  6s
kubernetes   ClusterIP   10.96.0.1       <none>        443/TCP                                                         27m
producer     NodePort    10.101.10.198   <none>        80:32000/TCP                                                    29s
queue        NodePort    10.105.128.2    <none>        15671:30000/TCP,15672:31157/TCP,5671:31353/TCP,5672:32440/TCP   27m
```
- Optional: you can view the data of the broker in `http://<cluster_ip>:31157/`.



## Optional 

run the MySQL server as a POD

[TUTORIAL](https://kubernetes.io/docs/tasks/run-application/run-single-instance-stateful-application/)

Note: the db will have 10GB of storage and in case you destroy your `minikube` cluster **you will
 also destroy the DB with all its data**. 

- [Only for production tests] Create the mysql server deployment and service
```shell script
kubectl create -f ./kubernetes/mysql/persistent_volume_deployment.yaml.yaml
kubectl create -f ./kubernetes/mysql/mysql_deployment.yaml.yaml
```

- [Only for production tests] Create the database rplStage (replase POD mysql-***-*** with YOUR pod)
```shell script
kubectl exec -it mysql-7d7fdd478f-hxcx9 -- /usr/bin/mysql -ppassword -e "create database rplStage"
```

- [Only for production tests] Run migrations getting the host and port (31111) of the mysql server 
service in the kubernetes cluster
```shell script
minikube ip --> 192.168.99.100
bash flyway/migrate.sh jdbc:mysql://192.168.99.100:31111/rplStage root password
```

- [Only for production tests] Change `SPRING_PROFILES_ACTIVE` env variable in `kubernetes/deployment/producer.yaml` to `hello-world,producer,prod`

- [Only for production tests] If you already deployed the producer, run `kubectl apply -f kubernetes/service/producer.yaml`

- [Only for production tests] This will create a stateful application where one POD will contain the DB. 
Otherwise, an H2 DB will be created and provisioned when the springboot app starts  


# Bibliography

- [SpringBoot Kubernetes](https://learnk8s.io/blog/scaling-spring-boot-microservices/)
- [MySQL in Kubernetes](https://kubernetes.io/docs/tasks/run-application/run-single-instance-stateful-application/)
- [Push a docker image](https://karlcode.owtelse.com/blog/2017/01/25/push-a-docker-image-to-personal-repository/)
- [CI/CD](https://sivalabs.in/2018/01/ci-cd-springboot-applications-using-travis-ci/)
- [Kubernetes](https://kubernetes.io/docs/tasks/run-application/run-stateless-application-deployment/)
- [Connect to DigitalOcean Kubernetes Cluster](https://www.digitalocean.com/docs/kubernetes/how-to/connect-to-cluster/)
- 