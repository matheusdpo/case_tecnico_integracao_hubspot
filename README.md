# Integração com HubSpot - Meetime Case Técnico

> Projeto de backend Java para integração com a API do HubSpot utilizando OAuth 2.0 (Authorization Code Flow), com
> endpoints para autenticação, criação de contatos e recebimento de webhooks.

---

## Índice

- [Sobre o Projeto](#sobre-o-projeto)
- [Tecnologias e Bibliotecas](#tecnologias-e-bibliotecas)
- [Como Executar o Projeto](#como-executar-o-projeto)
- [Endpoints Disponíveis](#endpoints-disponíveis)
- [Documentação da API - Swagger UI](#documentação-da-api---swagger-ui)
- [Postman Collection](#postman-collection)
- [Decisões Técnicas](#decisões-técnicas)
- [Melhorias Futuras](#melhorias-futuras)

---

## Sobre o Projeto

Este projeto foi desenvolvido como parte de um **case técnico para a Meetime**, com o objetivo de integrar uma API Java
com o CRM do HubSpot, utilizando OAuth 2.0 para autenticação e consumo de endpoints protegidos.

**Funcionalidades:**

- Geração da URL de autorização OAuth
- Autenticação via OAuth 2.0 para troca de Token
- Criação de contatos no HubSpot via API
- Execucao e processamento de eventos do tipo `contact.creation`

---

## Tecnologias e Bibliotecas

| Tecnologia   | Finalidade                               |
|--------------|------------------------------------------|
| Java 17+     | Linguagem principal                      |
| Spring Boot  | Framework para desenvolvimento da API    |
| Spring Web   | Criação dos endpoints REST               |
| Kong Unirest | Para chamadas HTTP ao HubSpot            |
| Jackson      | Serialização e desserialização JSON      |
| Thymeleaf    | Renderização de templates HTML           |
| Swagger      | Documentação da API                      |
| SLF4J        | Armazenamento de logs (logs/hubspot.log) |
| Bucket4j  | Limitação de requisições                 |

---

## Como Executar o Projeto

### Pré-requisitos

- Java 17+
- Maven
- Conta de desenvolvedor no HubSpot

### Clone o repositório

```bash
git clone https://github.com/matheusdpo/case_tecnico_integracao_hubspot.git
cd case_tecnico_integracao_hubspot
```

### Configure as variáveis no `application.properties`

```properties
hubspot.key.clientId=SEU_CLIENT_ID
hubspot.key.clientSecret=SEU_CLIENT_SECRET
hubspot.key.apiKey=SUA_API_KEY
hubspot.key.appId=SEU_APP_ID
```

### ▶️ Execute a aplicação

```bash
mvn spring-boot:run
```

---

## Endpoints Disponíveis

### 1. Gerar URL de Autorização OAuth

- **GET** `/api/v1/hubspot/auth-url`
- Retorna a URL para o usuário autenticar no HubSpot e capturar o `code`

---

### 2. Callback OAuth

- **GET** `/api/v1/hubspot/token-access`
- Recebe o `code` do HubSpot e troca por tokens de acesso

---

### 3. Criar Contato no HubSpot

- **POST** `/api/v1/hubspot/create-account`
- Cria contatos no HubSpot com os dados fornecidos no corpo da requisição

---

### 4. Processa Webhook de Criação de Contato

- **POST** `/api/v1/hubspot/process-webhook`
- Endpoint para receber eventos `contact.creation` do HubSpot e processá-los

---

## Documentação da API - Swagger UI

Esta aplicação possui integração com o **Swagger UI**, permitindo a visualização e testes interativos dos endpoints da API.


### URL da documentação:

 [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

---

Para acessar o Swagger, certifique-se de que a aplicação esteja rodando localmente na porta `8080`.

Caso tenha modificado a porta, atualize a URL acima conforme necessário.

---

## Postman Collection

1. Baixe o arquivo da collection: [PostmanCollection.json](./HubSpot.postman_collection.json)
2. Abra o Postman
3. Vá em "Import" e selecione o arquivo baixado
4. Pronto! Agora você pode testar todos os endpoints

---

## Decisões Técnicas

- Spring Framework para facilitar a criação de APIs RESTful
- Kong Unirest para chamadas HTTP ao HubSpot
- Jackson para serialização e desserialização de JSON
- Thymeleaf para renderização de templates HTML e exibição do code no navegador
- OpenAPI/Swagger para documentação da API
- Adicionado /v1/ no path para versionamento da API
- Logs (SLF4J) nativo do Spring evitar dependências extras para monitoramento e depuração
- Bucket4j para limitar o número de requisições, por nao necessitar uso de infra extra (Docker e Redis) – perfeito para rate limiting simples em aplicações Java.

---

## Melhorias Futuras

- **Testes unitários e de integração**
- **Redis integrado com Bucket4j para garantiria controle centralizado e limitação efetiva entre múltiplas instâncias.**
- **Novos endpoints para manipulação de contatos, como atualizacao, exclusao e consulta**
- **Criacao de usuarios e permissões para acesso aos endpoints**
- **Integração com outras entidades do HubSpot (ex: Companies, Deals)**

---

## Contato

Feito por **Matheus de Paulo Oliveira**  
💼 Software Engineer | Java | Spring Framework | APIs RESTful | RabbitMQ | AWS | RPA | Linux   
📫 matheusoliveira1991@hotmail.com

