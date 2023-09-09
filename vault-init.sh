export VAULT_TOKEN="00000000-0000-0000-0000-000000000000"
export VAULT_ADDR="http://127.0.0.1:8200"
vault kv put secret/spring-mvc spring.kafka.bootstrap-servers=broker:29092 spring.application.name=spring-mvc-vault truststore.path=file:/services/vault/certs.jks truststore.password=12345
#vault kv put secret/spring-mvc spring.kafka.bootstrap-servers=broker:29092 spring.application.name=spring-mvc-vault
