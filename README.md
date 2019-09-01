# RPL-2.0
RPL Final Assignment FIUBA

This service has three components, the producer backend, the consumer backend and the message broker.

## Endpoints

### `/submit`
When you make a POST to `/submit` with a body with the following structure:
```json
{
  "quantity": 100
}
```
The producer send a `"quantity"` ammount of messages to the broker, which are then received by the consumer.


## Bootstrapping

### Local
#### Configuration
- Download and install [Docker](https://docs.docker.com/install/)
- If using IntelliJ: copy `./RplApplication_Consumer.xml` and `./RplApplication_Producer.xml` to your `./.idea/runConfigurations/` folder. This will create two running configuration, one for running the producer and other for running the consumer.

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
- Download and install [kubectle](https://kubernetes.io/docs/tasks/tools/install-kubectl/)

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
