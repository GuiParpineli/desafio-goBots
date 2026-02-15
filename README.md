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

A marketplace-api é uma camada que contém a implementação da API no Spring Framework contendo beans, controllers e DTOs.
Ela é uma api multi-modulo, utilizando módulos **Core** e **DataProvider**. Ser multi-modulo garante a segurança na
arquitetura para ser seguido a risca sem misturar as camadas.

- ### receiver-api

A receiver-api foi feita com um único modulo por ser extremamente simples e exemplificar o uso sem multi-modulo, porém,
segue os mesmos princípios do marketplace-api.

- ### Vantagens

Essas camadas são independentes e podem ser desenvolvidas e testadas de forma isolada, permitindo uma maior
flexibilidade e facilidade de manutenção do sistema. Pode ser migrado de framework, banco de dados e bibliotecas com
uma facilidade extrema.
O código é modular e fácil de entender, facilitando a manutenção e evolução do sistema e baseado nos princípios do clean
code(Uncle Bob) e SOLID.
Objetos como DTOs podem conter diversas logicas de validação com Validations annotations e Jackson de forma separada não
poluindo outros objetos.
Entities podem ser feitas para JPA e MongoDB sem quebrar o fluxo do código.

- ### Desvantagens

O código é extremamente verboso e até repetitivo em alguns casos. Precisa de diversos conversores(mappers) para classes
em muitos casos praticamente idênticos.
Programadores inexperientes podem ter dificuldade em entender o código devido à complexidade e abstração.
Exige configurações extras para o projeto funcionar.
Exige conhecimento do Framework além do básico.

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

## Utilização das APIs

### marketplace-api

A documentação pode ser visualizado no Swagger: http://localhost:8080/swagger-ui/index.html

Fluxo e endpoints:

Para criar um pedido:

A criação de pedidos ocorre por meio do endpoint POST /orders e recebe um JSON com os dados do pedido.

**Exemplo**:

_**URL**_: http://localhost:8080/orders

**Request Body**:
```json
{
  "storeId": "1"
}
```

**Resposta**:
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