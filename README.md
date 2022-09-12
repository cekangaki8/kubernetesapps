# Helm deployments of ephemeral applications

## Purpose of POC

In this lab we will be creating a simple application that will be ran on a kubernetes cluster.

This lab will be incremental using versions for each milestone.

### MILESTONE 1

####GOAL:
Create an application with no arguments, that can be started from kubernetes using helm charts.

VERSION: 1.0.0

####STEPS:
1. Create a simple helloworld spring batch application that will run print hello world!
2. Create an image and save to artifactory
3. Run the job on kubernetes

####FOLLOW ALONG
1. Build application, create and push to dockerhub
```
mvn clean install
docker build . -t cekangaki8/simplebatchjob:1.0.0
docker push cekangaki8/simplebatchjob:1.0.0
```
2. Create kubernetes job and run on openshift cluster  

Using kubernetes job manifest
```yaml
apiVersion: batch/v1
kind: Job
metadata:
  name: helloworld
spec:
  template:
    metadata:
      name: helloworld
    spec:
      containers:
        - name: helloworldbatchjob
          image: cekangaki8/simplebatchjob:1.0.0
      restartPolicy: Never
```
Run the job on the cluster
```
Candys-MacBook-Pro:simplebatchjob cekangaki$ kubectl apply -f ./k8s/helloWorldJob.yaml 
job.batch/helloworld created

//Check the job
Candys-MacBook-Pro:simplebatchjob cekangaki$ kubectl get jobs
NAME         COMPLETIONS   DURATION   AGE
helloworld   1/1           7s         6m42s

```

Notice the restart policy prevents the job from restarting on failure. 
Also in the above example once the job has successfully been completed, we 
cannot restart the same job. The job must first be cleaned up from the 
cluster in order to be restarted.

Add ttlSecondsAfterFinished in the manifest to have the job delete after 60 seconds.
``` yaml
apiVersion: batch/v1
kind: Job
metadata:
  name: helloworld
spec:
  ttlSecondsAfterFinished: 60
```

3. Create a helm chart and run on openshift cluster

TAGGED VERSION - 1.0.1

Run the helm command with no wait for the job to start
```
Candys-MacBook-Pro:simplebatchjob cekangaki$ helm upgrade --install batchjobchart ./batchjobchart
Release "batchjobchart" has been upgraded. Happy Helming!
NAME: batchjobchart
LAST DEPLOYED: Sun Sep 11 23:40:13 2022
NAMESPACE: mysql
STATUS: deployed
REVISION: 3
TEST SUITE: None
NOTES:
Job deployed.

Candys-MacBook-Pro:simplebatchjob cekangaki$ kubectl get jobs
NAME             COMPLETIONS   DURATION   AGE
simplebatchjob   0/1           31s        31s
Candys-MacBook-Pro:simplebatchjob cekangaki$ 

```

Run the helm command with a wait for job completion

(NOT WORKING - Despite the documentation :-( ... Figure out the job wait )
```
Candys-MacBook-Pro:simplebatchjob cekangaki$ helm uninstall batchjobchart
release "batchjobchart" uninstalled

Candys-MacBook-Pro:simplebatchjob cekangaki$ helm install --timeout 3m --wait-for-jobs batchjobchart ./batchjobchart
NAME: batchjobchart
LAST DEPLOYED: Sun Sep 11 23:51:10 2022
NAMESPACE: mysql
STATUS: deployed
REVISION: 1
TEST SUITE: None
NOTES:
Job deployed.

```

##Useful Links:
* [Creating a Batch Service](https://spring.io/guides/gs/batch-processing/)
* [Spring Batch reference](https://docs.spring.io/spring-batch/docs/current/reference/html/)
* [Kubernetes Job](https://kubernetes.io/docs/concepts/workloads/controllers/job/)
* [Helm commands](https://helm.sh/docs/helm/helm_install/)
* [Helm job chart](https://github.com/cetic/helm-job)