gcloud auth login

docker tag cz.ivosahlik.ordering.system/order.service:$1 europe-west4-docker.pkg.dev/ordering-system-346904/ordering-system-repository/order.service:$1
docker tag cz.ivosahlik.ordering.system/payment.service:$1 europe-west4-docker.pkg.dev/ordering-system-346904/ordering-system-repository/payment.service:$1
docker tag cz.ivosahlik.ordering.system/restaurant.service:$1 europe-west4-docker.pkg.dev/ordering-system-346904/ordering-system-repository/restaurant.service:$1
docker tag cz.ivosahlik.ordering.system/customer.service:$1 europe-west4-docker.pkg.dev/ordering-system-346904/ordering-system-repository/customer.service:$1

docker push europe-west4-docker.pkg.dev/ordering-system-346904/ordering-system-repository/order.service:$1
docker push europe-west4-docker.pkg.dev/ordering-system-346904/ordering-system-repository/payment.service:$1
docker push europe-west4-docker.pkg.dev/ordering-system-346904/ordering-system-repository/restaurant.service:$1
docker push europe-west4-docker.pkg.dev/ordering-system-346904/ordering-system-repository/customer.service:$1

