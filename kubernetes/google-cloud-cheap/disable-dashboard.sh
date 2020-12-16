# https://cloud.google.com/kubernetes-engine/docs/how-to/small-cluster-tuning#kubernetes_dashboard
gcloud container clusters update cluster-rpl-1 --update-addons=KubernetesDashboard=DISABLED