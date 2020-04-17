TOPDIR=$(dir $(lastword $(MAKEFILE_LIST)))
include $(TOPDIR)/Makefile.env.mk

SERVICES = orders-rest-api orders-service stocks-service demo-service

container: $(SERVICES) 

$(SERVICES): 
	$(MAKE) -C $@ $(MAKECMDGOALS)

login_container_registry:
	$(CONTAINER_CTL) login $(CONTAINER_REGISTRY)

.PHONY: login_container_registry container $(SERVICES)
