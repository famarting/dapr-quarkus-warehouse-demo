oc logs $(oc get pod -l=app=$1 -o jsonpath='{.items[0].metadata.name}') -c $1 -f