http http://$(oc get route orders-rest-api -o jsonpath='{.status.ingress[0].host}')/add-stock item-id=abc quantity:=2