# Warehouse example application with Dapr , Quarkus and Strimzi

This repository contains an example application for orders processing in a warehouse, with the purpose of trying and showing different technologies. This is a diagram showcasing in high level how the application works

![app diagram](/img/diagram.png)

## How it's implemented?

This example microservices application is implemented with [quarkus], [dapr] and [strimzi]. Quarkus is the Java framework used to code the microservices and it's combined with Dapr to do the connections and bindings with other services or kafka topics. Strimzi is a kubernetes operator for managing kafka deployments, used in this demo to deploy the kafka broker used for the queue and topic showed in the diagram above.

## How to run it?

First of all, a kubernetes or openshift cluster with strimzi and dapr operators installed.
There is a utility script to install dapr in `./scripts/install_dapr.sh` and the file I used to deploy strimzi operator under `kubefiles/operators` folder. Note better instructions to install this two operators can be found in their respective documentation.

### Deploy the infrastructure requirements
After installing the needed operators we can deploy the kafka broker and a redis instance used by the application (all through Dapr API's).
```bash
oc apply -f kubefiles/infra
```
### Apply Dapr configuration
Once the infrastructure is ready we can apply the Dapr components configuration which will be used by the application.
```bash
oc apply -f kubefiles/dapr
```
### Deploy the application
```bash
oc apply -f kubefiles/app
```
You can check the application is running successfully
```bash
oc get pod
```
### Quick demo after deploying

```bash
./scripts/add-stock.sh
./scripts/create-order.sh
./scripts/watch-demo-logs.sh
```

[quarkus]: <https://quarkus.io/>
[dapr]: <https://dapr.io/>
[strimzi]: <https://strimzi.io/>