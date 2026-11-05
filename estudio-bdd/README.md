# Estúdio BDD — Programação de Sistemas II · 2026/2

Projeto acadêmico para registrar histórias de usuário, cenários BDD e sugestões de IA. Este primeiro marco contém a modelagem e a fundação Spring Boot.

## Semana 1 — fundação

- Modelo JPA de usuários, projetos, histórias, cenários, passos e sugestões, com associações e restrições de unicidade.
- PostgreSQL para desenvolvimento em `docker-compose.yml` e configuração em `backend/src/main/resources/application.properties`.
- Dados fictícios no perfil `demo` para popular um banco vazio.
- Diagrama em `docs/diagrama-classes.md`, modelo textual de história e três cenários em `docs/historia-3-cenarios.feature`, e orientações de IA em `docs/registro-ia.md`.

A atividade BDD do enunciado pede que os cenários sejam escritos à mão. O arquivo textual serve como modelo e não comprova essa etapa. O registro de IA contém propostas a validar pelo grupo.

## Execução planejada

Pré-requisitos: Java 21, Maven, PostgreSQL 16 ou Docker Compose. Na raiz do projeto:

```bash
docker compose up -d db
cd backend
mvn spring-boot:run -Dspring-boot.run.profiles=demo
```

Configure `DB_URL`, `DB_USER` e `DB_PASSWORD` para um banco local, se necessário. A inicialização com PostgreSQL e a criação das tabelas ainda precisam de verificação observada. Preencha os integrantes em `docs/integrantes.md`.

## Próximos marcos

Semana 2: CRUD REST de projetos, histórias, cenários e passos, com validação e autoria. Semana 3: sugestões, exportação, testes, documentação de endpoints e início da SPA. Entrega única em 17/11/2026.