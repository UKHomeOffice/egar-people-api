#!/bin/sh
echo Starting Submission-API version: $PEOPLE_API_VER
rm -rf /home/centos/egar-people-api/scripts/kube/person-api-deployment.yaml; envsubst < "/home/centos/egar-people-api/scripts/kube/person-api-deployment-template.yaml" > "/home/centos/egar-people-api/scripts/kube/person-api-deployment.yaml"
kubectl create -f /home/centos/egar-people-api/scripts/kube/person-api-deployment.yaml
kubectl create -f /home/centos/egar-people-api/scripts/kube/person-api-service.yaml
