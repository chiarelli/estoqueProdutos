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

#### 1. Clonar o repositório
```bash
git clone https://github.com/chiarelli/estoqueProdutos.git
cd estoqueProdutos
```

#### 2. Subir o PostgreSQL com Docker

```bash
docker-compose -f docker-compose.dev.yml up -d
```

#### 3. Executar os testes (opcional)
Lembrando que o container precisa estar `ready` para realizar os testes.
```bash
./mvnw test
```

#### 4. Executar a aplicação
No terminal (ou pela IDE):

```bash
./mvnw spring-boot:run
```

### ✅ Status atual
- [x] Projeto inicial com Spring Boot configurado
- [x] Endpoints listagem da entidade Categoria finalizado 
- [x] Entidade Produto implementada com validações
- [x] Regras de negócio aplicadas
- [x] CRUD completo de Produtos
- [x] Documentação Swagger
- [x] Tratamento de exceções e mensagens amigáveis
- [x] Testes automatizados (API)

---
###### 👨‍💻 Autor
> Feito por Raphael Mathias Chiarelli Gomes durante o curso de Spring Boot Web Developer na [COTI](https://www.cotiinformatica.com.br/curso/java).