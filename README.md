# Desafio GoBots

O projeto faz parte do desafio técnico da GoBots.
O objetivo é criar uma API RESTful que receba webhooks de eventos de pedidos e armazene informações relevantes em um
banco de dados.

## Arquitetura

### Clean Architecture

![Arquitetura](clean-architecturewebp)

A clean architecture é uma abordagem de arquitetura de software que visa separar as responsabilidades de um sistema em
camadas
independentes, permitindo uma maior flexibilidade e facilidade de manutenção. Neste projeto, a clean architecture é
utilizada com as seguintes camadas:

- **Core**: Camada que contém as regras de negócio e a lógica de negócios do sistema. Livre de qualquer framework ou
  biblioteca externa.
- **DataProvider**: Camada que contém as implementações de acesso a dados externos como banco de dados e APIs externas
  utilizadas pelo sistema.

- ### marketplace-api

A marketplace-api é um módulo que contém a implementação da API no Spring Framework contendo beans, controllers e DTOs.
Ela é uma api multi-modulo, utilizando módulos **Core** e **DataProvider**. Ser multi-modulo garante a segurança na
arquitetura para ser seguido a risca sem misturar as camadas.

- ### receiver-api

A receiver-api foi feita com um único modulo por ser extremamente simples e exemplificar o uso sem multi-modulo, porém,
segue os mesmos princípios do marketplace-api.

- ### Vantagens

Essas camadas são independentes e podem ser desenvolvidas e testadas de forma isolada, permitindo uma maior
flexibilidade e facilidade de manutenção do sistema. Pode ser migrado de framework, banco de dados e bibliotecas com
uma facilidade extrema.
O código é modular facilitando a manutenção e evolução do sistema e baseado nos princípios do clean code(Uncle Bob) e
SOLID.
Objetos como DTOs podem conter diversas logicas de validação com Validations annotations e Jackson de forma separada não
poluindo outros objetos.
Entities podem ser feitas para JPA e MongoDB sem quebrar o fluxo do código.

- ### Desvantagens

O código é extremamente verboso e até repetitivo em alguns casos. Precisa de diversos conversores(mappers) para classes
em muitos casos praticamente idênticos.
Programadores inexperientes podem ter dificuldade em entender o código devido à complexidade e abstração.
Exige configurações extras para o projeto funcionar, também exige conhecimento do Framework além do básico.

- #### Observação

Pode surgir a dúvida sobre a perda de desempenho devido a diversos objetos, conversões e abstrações, mas JDK modernas
isso é irrelevante principalmente com o G1 GC, o problema é na verbosidade o que demora mais para ser desenvolvido.

## Tecnologias

Foi usado as tecnologias mais recentes para a data atual FEV 2026:

- Kotlin 2.3
- Spring Boot 4.0
- MongoDB
- Docker
- Spring Data MongoDB
- Virtual Threads
- Swagger

## Uso de IA

O uso de recurso de IA foi feito apenas para autocomplete da própria IDE e pesquisa de dúvidas gerais.

## Como executar

O projeto pode ser executado com Docker. Para isso, basta executar o comando:

```bash
  docker-compose up --build
```

# Utilização das APIs

## Endpoints marketplace-api:

A documentação pode ser visualizado no **Swagger**: http://localhost:8080/swagger-ui/index.html

### Fluxo e endpoints:

**Para criar um pedido:**

A criação de pedidos ocorre por meio do endpoint POST /orders e recebe um JSON com os dados do pedido.
O endpoint grava o pedido no banco de dados, retorna o id do pedido e enfileira para o envio do webhook caso haja
cadastro para o receiver-api.

**POST**: http://localhost:8080/orders

**Request Body**:

```json
{
  "storeId": "1"
}
```

**Resposta**:

#### STATUS CODE:

- **201** Sucesso, pedido criado.
- **400** Dados inválidos.

```json
{
  "id": "6990de32ec75487ed49195e8",
  "status": "order.created"
}
```

curl:

```bash
curl -X 'POST' \
  'http://localhost:8080/orders' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -d '{
  "storeId": "1"
}'
```

- Para cadastrar um webhook, basta enviar um POST para o endpoint /webhooks com os dados do webhook.
  Lembre-se que se for executado via docker a callbackUrl deve possuir o nome do container em vez de localhost.

  **POST**: http://localhost:8080/webhooks

**Request Body**:

```json
{
  "storeIDs": [
    "1",
    "2"
  ],
  "callbackUrl": "http://localhost:8081/order-receiver"
}
```

curl:

```bash
curl -X 'POST' \
  'http://localhost:8080/webhooks' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -d '{
  "storeIDs": [
    "1","2"
  ],
  "callbackUrl": "http://localhost:8081/order-receiver"
}'
```

**Resposta**:

#### STATUS CODE:

- **200** Sucesso.

```json
{
  "message": "webHook cadastrado com sucesso!"
}
```

- Para vizualizar os pedidos, apenas informar o id no endpoint: GET http://localhost:8080/orders/{id}

**Resposta**:

#### STATUS CODE:

- **200** Sucesso, dados retornados.
- **404** Nenhum dado encontrado.

```json
{
  "id": "string",
  "storeID": "string",
  "productsIDs": [
    "string"
  ],
  "clientID": "string",
  "priority": 0,
  "status": "string",
  "createdAt": 0
}
```

curl:

```bash
curl -X 'GET' \
  'http://localhost:8080/orders/1' \
  -H 'accept: */*'
```

**Resposta**:

#### STATUS CODE:

- **200** Sucesso, dados retornados.
- **404** Nenhum dado encontrado.

```json
{
  "id": "69914bb7d5e9eb47dc225082",
  "storeID": "1",
  "productsIDs": [
    "1",
    "2",
    "3"
  ],
  "clientID": "1",
  "priority": 0,
  "status": "order.created",
  "createdAt": 1771129783663
}
```

- Para buscar pedidos por status e orderID:

  **PATCH**: http://localhost:8080/orders/{orderId}/{status}

curl:

```bash
curl -X 'PATCH' \
  'http://localhost:8080/orders/69914bb7d5e9eb47dc225082/order.shipped' \
  -H 'accept: */*'
```

**Resposta**:

#### STATUS CODE:

- **202** Sucesso no update.
- **404** Nenhum dado encontrado.

```json
{
  "id": "69914bb7d5e9eb47dc225082",
  "status": "order.shipped"
}
```

- Listar todos os pedidos de loja especifica:

  **GET**: http://localhost:8080/orders?storeId=1

curl:

```bash
curl -X 'GET' \
  'http://localhost:8080/orders?storeId=1' \
  -H 'accept: */*'
```

**Resposta**:

#### STATUS CODE:

- **200** sucesso.
- **404** Nenhum dado encontrado.

```json
[
  {
    "id": "69914bb7d5e9eb47dc225082",
    "storeID": "1",
    "productsIDs": [
      "1",
      "2",
      "3"
    ],
    "clientID": "1",
    "priority": 0,
    "status": "order.shipped",
    "createdAt": 1771129783663
  },
  {
    "id": "69914bfbd5e9eb47dc225085",
    "storeID": "1",
    "productsIDs": [
      "1",
      "3"
    ],
    "clientID": "1",
    "priority": 0,
    "status": "order.created",
    "createdAt": 1771129851593
  }
]
```

## Endpoints receiver-api:

A documentação pode ser visualizado no **Swagger**: http://localhost:8081/swagger-ui/index.html

### Fluxo e endpoints:

- Recebe os pedidos enviados pelo webhook e grava no banco de dados.

  **POST**: http://localhost:8081/order-receiver

**Request Body**:

```json
{
  "eventID": "213",
  "event": "order.created",
  "orderID": "8802123",
  "storeID": "1",
  "timestamp": 1771086212
}
```

**curl**:

```curl
curl -X 'POST' \
  'http://localhost:8081/order-receiver' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -d '{
  "eventID": "213",
  "event": "order.created",
  "orderID": "8802123",
  "storeID": "1",
  "timestamp": 1771086212
}'
```

**Resposta**:

#### STATUS CODE:

- **202** webhook recebido e gravado.
- **200** ignora, pois já foi recebido anteriormente.
- **400** erro ao receber dados.

- Para **validar** se o webhook foi recebido e se foi processado, basta informar o orderID:

**GET**: http://localhost:8081/order-receiver/byID/{id}

```bash
curl -X 'GET' \
  'http://localhost:8081/order-receiver/order-receiver/byID/6991d7cb0c71bd71607642aa' \
  -H 'accept: */*'
```

**Resposta**:

#### STATUS CODE:

- **200** caso houver dados
- **404** se não houver dados

```json
[
  {
    "id": "6991d7cb0c71bd71607642aa",
    "event": "CREATED",
    "orderID": "6991d7c8467e0b90251b6ce4",
    "storeID": "1",
    "timestamp": 1771165640036
  }
]
```

- Para listar todos os pedidos recebidos:

  **GET**: http://localhost:8081/order-receiver/

curl:

```bash
curl -X 'GET' \
  'http://localhost:8081/order-receiver/order-receiver/' \
  -H 'accept: */*'
```

**Resposta**:

#### STATUS CODE:

- **200** caso houver dados
- **404** se não houver dados

``` json
[
  {
    "id": "6991d7cb0c71bd71607642aa",
    "event": "CREATED",
    "orderID": "6991d7c8467e0b90251b6ce4",
    "storeID": "1",
    "timestamp": 1771165640036
  }
]
```
