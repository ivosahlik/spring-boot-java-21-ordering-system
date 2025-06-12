1) docker-compose -f common.yml -f zookeeper.yml up

# https://zookeeper.apache.org/doc/r3.6.0/zookeeperCLI.html
##https://zookeeper.apache.org/doc/current/zookeeperCLI.html
## KAFKA_OPTS: "-Dzookeeper.4lw.commands.whitelist=ruok"
echo ruok | nc localhost 2181

2) docker-compose -f common.yml -f kafka_cluster.yml up
3) docker-compose -f common.yml -f init_kafka.yml up

4) localhost:9000
5) create cluster
   cluster-name: ordering-system-cluster
   cluster-zookeeper-host: zookeeper:2181
   save
6) removed
docker-compose -f common.yml -f zookeeper.yml down
docker-compose -f common.yml -f kafka_cluster.yml down
docker-compose -f common.yml -f init_kafka.yml down

7) run docker-compose.yaml up -> run postgredb
   docker-compose -f docker-compose-postgres.yaml up
8) docker-compose -f docker-compose-postgres.yaml down
8) install https://formulae.brew.sh/formula/kcat, https://macappstore.org/kafkacat/
   kcat -C -b localhost:19092 -t payment-request
old: kafkacat -C -b localhost:19092 -t payment-request




docker-compose -f common.yml -f zookeeper.yml up
docker-compose -f common.yml -f kafka_cluster.yml up 
docker-compose -f common.yml -f init_kafka.yml up
docker-compose -f common.yml -f docker-compose-postgres.yaml up


docker-compose -f common.yml -f kafka_cluster.yml up
