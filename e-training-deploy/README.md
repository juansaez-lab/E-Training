# Management of the etime-production-frontend-route route (etime.svc.elca.ch)

A certificate and DNS entries are configured so that the user friendlier domain
etime.svc.elca.ch is used in production.

Since the route contains a private key, it is not saved Git in `e-time-frontend/deploy/deploymentTemplate.yml`.
YAML without the key is kept in `e-time-frontend/deploy/etime-production-frontend-route.yaml`.

## Certificate renewal process

The certificate expires after one year. Project should follow this procedure yearly before expiration:

- Ask DevOps team to renew certificate (DRN or SYP). See instructions in
  https://bitbucket.svc.elca.ch/projects/ELCA-SSO/repos/ansible-okd/browse/certs/certs.md
- Manager page for the certificate: https://manager.svc.elca.ch/service/certificates/edit/etime.svc.elca.ch
- Update certificate in the route: `e-time-frontend/deploy/etime-production-frontend-route.yaml`
- Temporarily edit the YAML to add the private key. It can be found encrytped in
  https://bitbucket.svc.elca.ch/projects/ELCA-SSO/repos/ansible-okd/browse/certs/etime/cert-key.pem.vault
- Apply the route
```
oc apply -f e-time-frontend/deploy/etime-production-frontend-route.yaml
```
