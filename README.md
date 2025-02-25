# ms-ambev-order

O **ms-ambev-order** é uma aplicação Spring Boot que gerencia pedidos de produtos. A aplicação permite receber pedidos via API REST, processá-los e armazená-los em um banco de dados MongoDB. Além disso, utiliza Apache Kafka para desacoplar a recepção de pedidos do seu processamento, garantindo robustez e escalabilidade.

## Funcionalidades

- Receber pedidos via API REST.
- Processar pedidos e calcular o valor total.
- Armazenar pedidos em um banco de dados MongoDB.
- Usar Kafka para enviar e consumir mensagens de pedidos.
- Verificação de duplicação de pedidos.
- Criação de índices no MongoDB para otimizar consultas.

## Tecnologias Utilizadas

- **Java 17**: Linguagem de programação.
- **Spring Boot**: Framework para desenvolvimento de aplicações Java.
- **MongoDB**: Banco de dados NoSQL para armazenamento de dados.
- **Apache Kafka**: Sistema de mensagens para integrar a recepção e o processamento de pedidos.
- **Swagger**: Para documentação da API.


## Documentação da API
- A documentação da API é gerada automaticamente pelo Swagger. Você pode acessá-la através do seguinte URL:

- Swagger UI: http://localhost:8080/swagger-ui/index.html
- API Docs: http://localhost:8080/v3/api-docs

## Pré-requisitos

Antes de executar a aplicação, você precisará ter instalado:

- **Java 17**
- **Maven**
- **MongoDB**
- **Apache Kafka** (e Zookeeper)

### Executando o MongoDB e Kafka

Se você estiver usando Docker, pode iniciar o MongoDB e Kafka com os seguintes comandos:

```bash
docker run -d --name zookeeper -p 2181:2181 wurstmeister/zookeeper
docker run -d --name kafka --link zookeeper -p 9092:9092 wurstmeister/kafka
docker run -d --name mongodb -p 27017:27017 mongo


