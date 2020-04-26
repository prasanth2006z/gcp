# Building spring-boot application with  CloudBuild and deploying into Container registry

COMMAND: gcloud builds submit

IAM:

Below section shows the errors and resolution while executing the cloudbuild.yaml

ERROR: (gcloud.compute.instance-groups.managed.rolling-action.restart) Could not fetch resource:
Step #2:  - Required 'compute.instanceGroupManagers.get' permission for 'projects/<PROJECT-NAME>/zones/us-central1-a/instanceGroupManagers/instance-group-2'


Resolution: I have added Compute Instance Admin (v1) ROLE to <PROJECT-ID>@cloudbuild.gserviceaccount.com


ERROR: (gcloud.compute.instance-groups.managed.rolling-action.restart) Could not fetch resource:
Step #2:  - The user does not have access to service account '<PROJECT-ID>-compute@developer.gserviceaccount.com'.  User: '<PROJECT-ID>@cloudbuild.gserviceaccount
.com'.  Ask a project owner to grant you the iam.serviceAccountUser role on the service account

Resolution: I have added Service Account User ROLE to <PROJECT-ID>@cloudbuild.gserviceaccount.com 
