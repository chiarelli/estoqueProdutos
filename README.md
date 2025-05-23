# 🛒 Projeto Estoque de Produtos API

Este é um projeto desenvolvido como parte do curso **Web Developer Java**, com o objetivo de implementar um sistema completo (backend e frontend) de cadastro e gerenciamento de produtos de supermercado, praticando os seguintes conceitos:

- Desenvolvimento de API REST com Spring Boot  
- Persistência de dados com Spring Data JPA  
- Integração com PostgreSQL  
- Validações com Bean Validation  
- Boas práticas de modelagem, versionamento de API e regras de negócio  

---

### 🧠 Regras de Negócio

- Não é permitido cadastrar produtos com o **mesmo nome**
- Não é permitido excluir produtos com **estoque maior que 0**
- O **preço do produto não pode ser negativo**
- A **categoria é obrigatória** no cadastro e edição de produtos

---

### 🛠 Tecnologias utilizadas

- Java 21  
- Spring Boot 3.4.x  
- Spring Data JPA  
- PostgreSQL (via Docker)  
- Maven  
- SpringDoc OpenAPI (Swagger)  
- Bean Validation (Jakarta Validation)  
- Docker + Docker Compose  

---

### 📦 Entidades principais

#### 🧺 Produto

- `id`: UUID  
- `nome`: String (único)  
- `preco`: BigDecimal (não negativo)  
- `quantidade`: Integer (não negativo) 
- `categoria`: Categoria associada (obrigatória)

#### 🗂️ Categoria

- `id`: UUID  
- `nome`: String (único)  

---

### 🔄 Endpoints principais (Swagger)

A API possui documentação interativa disponível via Swagger:

> 🔗 Acesse: [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

#### 📋 Produtos

- `POST /api/v1/produtos` – Cadastrar novo produto  
- `GET /api/v1/produtos` – Listar todos os produtos  
- `GET /api/v1/produtos/{id}` – Buscar produto por ID  
- `PUT /api/v1/produtos/{id}` – Atualizar um produto  
- `DELETE /api/v1/produtos/{id}` – Excluir produto (estoque deve ser 0)

#### 🗃️ Categorias

- `GET /api/v1/categorias` – Listar categorias  
- `GET /api/v1/categorias/{id}` – Buscar categoria por ID  

---

### ▶️ Como executar o projeto

#### 1. Subir o PostgreSQL com Docker

```bash
docker-compose -f docker-compose.dev.yml up -d
```

#### 2. Executar a aplicação
No terminal (ou pela IDE):

```bash
./mvnw clean package
java -jar ./target/supermercado-api-0.0.1-SNAPSHOT.jar
```

### ✅ Status atual
- [x] Projeto inicial com Spring Boot configurado
- [ ] Entidade Produto implementada com validações
- [ ] Regras de negócio aplicadas
- [ ] CRUD completo de Produtos e Categorias
- [ ] Documentação Swagger
- [ ] Tratamento de exceções e mensagens amigáveis
- [ ] Testes automatizados (unitários e integração)