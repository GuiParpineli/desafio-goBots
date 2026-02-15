# Desafio GoBots

O projeto faz parte do desafio técnico da GoBots.
O objetivo é criar uma API RESTful que receba webhooks de eventos de pedidos e armazene informações relevantes em um
banco de dados.

O projeto possui as seguintes funcionalidades:

marketplace-api:

- Criar pedidos
- Cadastrar webhooks
- Enviar pedidos para o webhook
- Scheduler para enviar pedidos para o webhook com retry em caso de erro.
- Listar pedidos salvos
- Listar eventos recebidos por id do evento
- Listar pedidos salvos por loja
- Atualizar status de pedidos com validações de transição

receiver-api:

- Receber webhooks de eventos de pedidos, enriquecer e salvar no banco de dados com idempotência.
- Scheduler para verificar se o webhook foi recebido e processado com retry em caso de erro.
- Listar eventos recebidos por id do evento
- Listar pedidos salvos

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

O uso de recurso de IA foi feito para autocomplete da própria IDE e pesquisa de dúvidas gerais.
Os testes foram gerados pelo agente Junie e corrigidos erros manualmente.

Prompt para teste receiver-api _OrderEventController_:

```markdown
Escreva testes unitários para a classe OrderEventControllerImpl
do módulo receiver-api.

Stack: Kotlin, JUnit 5, Mock e Spring MockMvc.

Cenários a cobrir:

1. POST /order-receiver com evento novo → deve retornar 202
2. POST /order-receiver com evento duplicado → deve retornar 200
3. POST /order-receiver com body inválido → deve retornar 400
4. GET /order-receiver/ → deve retornar lista de eventos
5. GET /order-receiver/byID/{id} → deve retornar eventos do ID

Regras:

- Mockar o OrderEventService
- Seguir o padrão AAA (Arrange, Act, Assert)
- Nomear testes de forma descritiva
- Colocar os testes no diretório de testes correspondente ao pacote da classe
```

Prompt para teste marketplace-api _OrderController_:

```markdown
Escreva testes unitários para a classe OrderControllerImpl
do módulo marketplace-api.

Stack: Kotlin, JUnit 5, MockK e Spring MockMvc.

Cenários a cobrir:

1. POST /orders cria novo pedido → deve retornar 201
2. PATCH /orders//{orderId}/{status} se o eventStatus for valido deve retornar 202, caso EventStaus invalido 400, caso n
   encontrar a orderID 404
3. GET /orders/{orderId} com body inválido deve retornar 400; caso n encontre o orderID retorna 404
4. GET /orders→ deve retornar lista de eventos ou 404 caso banco esteja vazio
5. GET /orders/{storeId} → deve retornar eventos do ID ou 404 caso n encontre

Regras:

- Mockar o OrderEventService
- Seguir o padrão AAA (Arrange, Act, Assert)
- Nomear testes de forma descritiva
- Colocar os testes no diretório de testes correspondente ao pacote da classe
```

## Como executar

O projeto pode ser executado com Docker. Para isso, basta executar o comando:

```bash
  docker-compose up --build
```

# Utilização das APIs

## Endpoints marketplace-api:

A documentação pode ser visualizado no **Swagger**: http://localhost:8080/swagger-ui/index.html

### Fluxo e endpoints:

**Como criar pedido**

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

**Como cadastrar um webhook:**

- Para cadastrar um webhook, basta enviar um POST para o endpoint /webhooks com os dados do webhook.
  Salva o evento no banco com status PENDING. Se sucesso no envio altera para DELIVERED. A um scheduler tenta processar
  de tempos em tempos.
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

**Resposta**:

#### STATUS CODE:

- **200** Sucesso.

```json
{
  "message": "webHook cadastrado com sucesso!"
}
```

**Como validar que o Receiver salvou**

Para validar se o Receiver salvou o pedido, basta enviar um GET para o endpoint /order-receiver/ ou
/order-receiver/byID/{id} e verificar se o pedido foi salvo.
Processado informa se conseguir enriquecer o Json buscando no endpoint do marketplace-api. Caso processed:false, o
scheduler tenta processar de tempos em tempos.

**GET**: http://localhost:8081/order-receiver/

**GET**: http://localhost:8081/order-receiver/byID/{id}

Resposta:

```json
[
  {
    "id": "6991d7cb0c71bd71607642aa",
    "event": "CREATED",
    "orderID": "6991d7c8467e0b90251b6ce4",
    "storeID": "1",
    "timestamp": 1771165640036,
    "processed": true
  },
  {
    "id": "6992033f1da516c2836cf8d4",
    "event": "PAID",
    "orderID": "6991d7c8467e0b90251b6ce4",
    "storeID": "1",
    "timestamp": 1771176765416,
    "processed": true
  },
  {
    "id": "699205e7df2675ea42f5d569",
    "event": "SHIPPED",
    "orderID": "6991d7c8467e0b90251b6ce4",
    "storeID": "1",
    "timestamp": 1771177445494,
    "processed": true
  }
]
```

Caso deseje ver o snapshot juntamente, consultar o endpoint:

**GET**: http://localhost:8081/order-receiver/complete

**Resposta**:
```json
[
  {
    "id": "6991d7cb0c71bd71607642aa",
    "event": "CREATED",
    "orderID": "6991d7c8467e0b90251b6ce4",
    "storeID": "1",
    "timestamp": 1771165640036,
    "orderSnapshot": {
      "productsIDs": [
        "1",
        "2",
        "3"
      ],
      "clientID": "1",
      "priority": 0,
      "status": "order.created",
      "createdAt": 1771165640000
    },
    "processed": true
  },
  {
    "id": "6992033f1da516c2836cf8d4",
    "event": "PAID",
    "orderID": "6991d7c8467e0b90251b6ce4",
    "storeID": "1",
    "timestamp": 1771176765416,
    "orderSnapshot": {
      "productsIDs": [
        "1",
        "2",
        "3"
      ],
      "clientID": "1",
      "priority": 0,
      "status": "order.paid",
      "createdAt": 1771165640000
    },
    "processed": true
  },
  {
    "id": "699205e7df2675ea42f5d569",
    "event": "SHIPPED",
    "orderID": "6991d7c8467e0b90251b6ce4",
    "storeID": "1",
    "timestamp": 1771177445494,
    "orderSnapshot": {
      "productsIDs": [
        "1",
        "2",
        "3"
      ],
      "clientID": "1",
      "priority": 0,
      "status": "order.shipped",
      "createdAt": 1771165640000
    },
    "processed": true
  }
]
```