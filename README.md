# Tenda Atacado Cupom Service

API REST para gerenciamento de cupons de desconto do Tenda Atacado.

## 📋 Pré-requisitos

- **Docker** e **Docker Compose** instalados
- **Maven 3.6+** (apenas para execução local sem Docker)
- **Java 8** (apenas para execução local sem Docker)

## 🚀 Como Subir o Projeto com Docker

### 1. Build e Start dos Serviços

Execute o comando abaixo na raiz do projeto para construir a imagem Docker da aplicação e subir todos os serviços (aplicação + SonarQube):

```bash
docker-compose up -d --build
```

Este comando irá:
- Construir a imagem Docker da aplicação
- Subir o container da aplicação na porta **8084**
- Subir o container do SonarQube na porta **9000**

### 2. Verificar se os Serviços Estão Rodando

```bash
docker-compose ps
```

Você deve ver dois containers rodando:
- `tenda-atacado-cupom-service` (porta 8084)
- `tenda-atacado-cupom-sonarqube` (porta 9000)

### 3. Acessar a Aplicação

- **API**: http://localhost:8084
- **Swagger UI**: http://localhost:8084/swagger-ui.html
- **Health Check**: http://localhost:8084/actuator/health
- **SonarQube**: http://localhost:9000

### 4. Verificar se Tudo Está Funcionando

Após subir os containers, verifique se a aplicação está respondendo:

```bash
# Verificar health check
curl http://localhost:8084/actuator/health

# Verificar se o Swagger está acessível
curl http://localhost:8084/swagger-ui.html
```

Você deve receber respostas HTTP 200 em ambos os casos.

### 5. Parar os Serviços

```bash
docker-compose down
```

Para remover também os volumes (dados do SonarQube):

```bash
docker-compose down -v
```

## 🧪 Como Rodar os Testes com SonarQube

### 1. Subir o SonarQube (se ainda não estiver rodando)

```bash
docker-compose up -d sonarqube
```

Aguarde alguns segundos para o SonarQube inicializar completamente (pode levar até 1-2 minutos na primeira execução).

### 2. Obter o Token do SonarQube

#### Passo a passo:

1. Acesse o SonarQube: http://localhost:9000
2. Faça login com as credenciais padrão:
   - **Usuário**: `admin`
   - **Senha**: `admin`
3. Na primeira vez, você será solicitado a alterar a senha. Altere para uma senha de sua escolha.
4. Após o login, clique no seu **perfil** (canto superior direito) → **My Account**
5. Vá na aba **Security**
6. No campo **Generate Token**, digite um nome para o token (ex: `maven-sonar-token`)
7. Clique em **Generate**
8. **Copie o token gerado** (você só verá ele uma vez!)

### 3. Executar os Testes e Enviar para o SonarQube

Execute o comando abaixo substituindo `<SEU_TOKEN>` pelo token copiado no passo anterior:

```bash
mvn clean verify sonar:sonar -Psonar-local -Dsonar.login=<SEU_TOKEN>
```

**Exemplo:**
```bash
mvn clean verify sonar:sonar -Psonar-local -Dsonar.login=e14be5fdeb663a120501de7707b
```

Este comando irá:
- Limpar o projeto (`clean`)
- Compilar e executar os testes (`verify`)
- Gerar o relatório de cobertura (JaCoCo)
- Enviar os resultados para o SonarQube (`sonar:sonar`)

### 4. Visualizar os Resultados no SonarQube

Após a execução, acesse: http://localhost:9000

O projeto **Tenda Atacado Cupom** estará disponível na lista de projetos com:
- Cobertura de código
- Code smells
- Bugs
- Vulnerabilidades
- Duplicações

## 📚 Documentação da API (Swagger)

O Swagger está configurado e acessível em:

**http://localhost:8084/swagger-ui.html**

A documentação interativa permite:
- Visualizar todos os endpoints disponíveis
- Testar as requisições diretamente pelo navegador
- Ver os modelos de dados (DTOs)
- Ver exemplos de requisições e respostas

### Testando o Swagger após subir com Docker

1. Aguarde a aplicação inicializar completamente (verifique os logs: `docker-compose logs -f app`)
2. Acesse: http://localhost:8084/swagger-ui.html
3. Você deve ver a interface do Swagger com todos os endpoints disponíveis
4. Os endpoints estão **públicos** (sem necessidade de autenticação), então você pode testar diretamente

### Endpoints Disponíveis

- `POST /v1/coupons` - Criar um novo cupom
- `GET /v1/coupons/{id}` - Buscar cupom por ID (UUID)
- `DELETE /v1/coupons/{code}` - Deletar cupom (soft delete) pelo código

### Exemplo de Requisição para Criar Cupom

```json
{
  "code": "ABC123",
  "description": "Cupom de desconto de teste",
  "discountValue": 10.50,
  "expirationDate": "2026-12-31T00:00:00.000Z",
  "published": false
}
```
