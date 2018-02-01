#!/bin/sh
kubectl delete -f /home/centos/egar-people-api/scripts/kube/person-api-deployment.yaml
kubectl delete -f /home/centos/egar-people-api/scripts/kube/person-api-service.yaml
