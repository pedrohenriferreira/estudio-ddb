# Estúdio BDD — Programação de Sistemas II · 2026/2

> **Escopo desta versão:** Semanas 1, 2 e 3 do cronograma (até **10/11/2026**). API REST completa no código-fonte, testes unitários e de arquitetura, documentação, e início da SPA (seleção de usuário, projetos e histórias). O restante da interface fica para a Semana 4.

Projeto acadêmico de documentação de requisitos com histórias de usuário, cenários Gherkin (`Dado/Quando/Então`), curadoria humana de sugestões de IA e exportação de arquivos `.feature`.

**Integrantes:**

| Integrante | Nome completo | RA |
| --- | --- | --- |
| 1 | Arthur Garcia | 10735807 |
| 2 | Pedro Ferreira | 10740749 |
| 3 | Rapahel Squinzari | 10737008 |

## 1. Tecnologias e requisitos

| Parte | Stack |
| --- | --- |
| Backend | Java 21, Spring Boot 3.5.7, Spring Web, Spring Data JPA, Bean Validation, PostgreSQL |
| Testes | JUnit 5, Spring Boot Test, ArchUnit 1.4.1 |
| Frontend | React 19, Vite 6, CSS e lucide-react |
| Banco | PostgreSQL 16 via Docker Compose (ou PostgreSQL local) |

**Pré-requisitos:** JDK 21, Maven 3.9+, Node.js 20.19+ ou 22+, npm, PostgreSQL 16+ ou Docker com Compose. Na primeira execução será necessário acesso à internet para instalar dependências Maven/npm. Não inclua senhas reais no Git. Os exemplos usam o banco de desenvolvimento descrito em `docker-compose.yml`.

## 2. Executar localmente

Após clonar o repositório, entre na pasta `estudio-bdd/`, que contém `docker-compose.yml`, `backend/` e `frontend/`. Abra **três terminais nessa pasta** e execute cada bloco abaixo em seu respectivo terminal.

**Terminal A — banco:**

```bash
docker compose up -d db
```

O Compose cria o banco `estudio_bdd` e o usuário de desenvolvimento `estudio` (senha de exemplo `estudio123`, apenas para ambiente local). Se estiver usando banco local, crie estes objetos ou defina `DB_URL`, `DB_USER` e `DB_PASSWORD` no ambiente.

**Terminal B — API:**

```bash
cd backend
mvn clean test
mvn spring-boot:run -Dspring-boot.run.profiles=demo
```

API em `http://localhost:8080`. O perfil `demo` cria dois usuários, um projeto, duas histórias, um cenário com passos válidos e uma sugestão pendente **apenas quando o banco está vazio**. O modo normal também funciona sem o perfil, mas então os dados precisam ser cadastrados via API.

**Terminal C — SPA:**

```bash
cd frontend
npm ci
npm run dev
```

Acesse `http://localhost:5173`. O Vite redireciona chamadas de `/api` para o Spring Boot. Se desejar acessar a API diretamente por outra origem, configure `FRONTEND_ORIGIN` no backend; o CORS é restrito a essa origem.

Para compilar o frontend:

```bash
cd frontend
npm run build
```

**Estado da verificação:** `npm install` e `npm run build` foram executados em 05/11/2026. Maven/Java não estavam disponíveis nessa sessão; por isso `mvn test`, a inicialização da API, a persistência no PostgreSQL e as respostas HTTP ainda precisam de verificação real. Veja [checagens realizadas](docs/checagem-local.md) e o [roteiro com resultados esperados e observados](docs/roteiro-verificacao.md). O [exemplo de `.feature`](docs/exemplo-exportacao.feature) foi gerado em teste isolado do serviço, não por uma requisição à aplicação completa.

## 3. Organização e cinco camadas

```text
estudio-bdd/
├── backend/
│   ├── pom.xml
│   └── src/
│       ├── main/java/br/edu/mackenzie/estudiobdd/
│       │   ├── config/       CORS, dados de demonstração, ProblemDetail
│       │   ├── controller/   Endpoints e métodos HTTP
│       │   ├── dto/          DTOs de entrada e saída / mapeamento
│       │   ├── service/      Casos de uso, transações, regras de negócio
│       │   ├── repository/   Interfaces Spring Data JPA
│       │   └── model/        Entidades e enums JPA
│       └── test/java/...    Testes de regras e ArchUnit
├── frontend/                React + Vite; SPA inicial da Semana 3
├── docs/                    Diagramas, cenários, endpoints, IA e verificação
├── AGENTS.md                Instruções para colaboradores e assistentes de IA
└── docker-compose.yml       PostgreSQL local
```

Fluxo lógico: **SPA → HTTP/JSON → Controller → Service → Repository → PostgreSQL**. O domínio (`model`) define as entidades e é usado pelos serviços e repositórios. Controllers não importam repositórios; a regra é assegurada por teste ArchUnit. `DtoMapper` converte entidades em DTOs de saída **dentro das transações dos serviços**, evitando falhas de acesso a associações `LAZY`; os controllers recebem/validam DTOs e delegam a execução.

**Associações:** `Projeto (1:N) HistoriaUsuario`, `HistoriaUsuario (1:N) Cenario`, `Cenario (1:N) Passo`, `HistoriaUsuario (1:N) Sugestao`; `Usuario` é autor da história e responsável por registrar/decidir sugestões. Coleções usam `@OneToMany`, `cascade=ALL`, `orphanRemoval=true`; relacionamentos de muitos-para-um têm `LAZY`. Cenários e passos usam `posicao` para ordenação consistente. Ver [diagrama de classes](docs/diagrama-classes.md).

### Exemplo de caminho: cadastrar um cenário

1. A SPA (na Semana 4) envia `POST /api/historias/1/cenarios` com JSON e `X-Usuario-Id`.
2. `CenarioController` valida o DTO (`@Valid`) e encaminha para `CenarioService`.
3. `CenarioService` verifica se a história existe e pertence ao autor, testa unicidade de título e posição, valida os passos (`ValidadorCenario`) e inicia a transação.
4. `CenarioRepository` persiste `Cenario` e `Passo` por cascata JPA.
5. A resposta é mapeada em `CenarioView`; erros são devolvidos como `ProblemDetail`.

## 4. Funcionalidades até a Semana 3

| Funcionalidade | API | SPA |
| --- | --- | --- |
| Usuários de demonstração | Listar, criar | Selecionar usuário |
| Projetos | CRUD | Listar e criar |
| Histórias | CRUD, filtro prioridade, indicadores | Listar, filtrar, criar e visualizar narrativa |
| Cenários + passos | CRUD, ordem e autoria | **Semana 4** |
| Sugestões de IA | Registrar, listar, aceitar ou rejeitar | **Semana 4** |
| Exportação `.feature` | Com bloqueios e Gherkin pt | **Semana 4** |
| Resumo do projeto | Contagens por categoria | **Semana 4** |
| Testes | 3 regras mínimas + adicionais + ArchUnit, escritos | — |

**Identidade simulada:** operações protegidas por autoria recebem `X-Usuario-Id` informando o usuário existente. **Não é autenticação**: qualquer cliente pode enviar outro ID. Esse mecanismo é exclusivamente acadêmico, conforme enunciado. A API valida autoria em alterações e exclusões de histórias, cenários e passos. Sugestões podem ser registradas e decididas por qualquer usuário de demonstração existente.

**Regras centrais:** para não contornar a autoria, projetos com histórias não podem ser excluídos diretamente: primeiro o respectivo autor remove suas histórias. E-mails, nomes de projeto, títulos de história dentro de projeto e títulos de cenário dentro de história não podem se repetir; narrativa da história é obrigatória; cenário exige `QUANDO` e `ENTAO`, ordem DADO→QUANDO→ENTAO e nunca inicia por `E`/`MAS`; sugestões nascem pendentes, rejeição exige motivo, e sugestões decididas não podem ser modificadas. Aceitar não altera automaticamente os cenários. Exportação exige ao menos um cenário e zero sugestões pendentes.

## 5. Rotas REST e exemplos

Documentação completa dos endpoints, corpos e respostas: **[docs/endpoints.md](docs/endpoints.md)**.

Criar história de usuário:

```bash
curl -i -X POST http://localhost:8080/api/projetos/1/historias \
  -H 'Content-Type: application/json' -H 'X-Usuario-Id: 1' \
  -d '{"titulo":"Devolver exemplar","papel":"leitor","objetivo":"registrar devolução","beneficio":"encerrar o empréstimo","prioridade":"MEDIA"}'
```

Criar cenário válido da história `1`:

```bash
curl -i -X POST http://localhost:8080/api/historias/1/cenarios \
  -H 'Content-Type: application/json' -H 'X-Usuario-Id: 1' \
  -d '{"titulo":"Devolução efetuada","posicao":2,"origem":"HUMANO","passos":[{"tipo":"DADO","texto":"que existe empréstimo ativo","posicao":1},{"tipo":"QUANDO","texto":"o leitor devolve o livro","posicao":2},{"tipo":"ENTAO","texto":"o empréstimo é encerrado","posicao":3}]}'
```

Exportar (somente após decidir todas as sugestões):

```bash
curl -i http://localhost:8080/api/historias/1/exportacao
```

Sem cenários ou com sugestões pendentes, a exportação devolve erro `422` com mensagem. A saída permitida é texto puro com cabeçalho `# language: pt` e `Content-Disposition: attachment` para download da extensão `.feature`.

## 6. Conteúdos da atividade inicial (Semana 1)

- Diagrama de classes: `docs/diagrama-classes.md` (Mermaid, pronto para renderização).
- Uma história BDD e três cenários: `docs/historia-3-cenarios.feature` (modelo textual). **É necessário transcrevê-los à mão** e levar o registro à aula, conforme o enunciado; o arquivo não prova a etapa manual.
- Primeira ampliação sugerida por IA e análise de pertinência: `docs/registro-ia.md`.
- Caminho da operação pelas camadas: seção 3 deste README.
- `AGENTS.md`: convenções, regras e comandos.

## 7. Testes, marcos e próximos passos

Os testes estão em `backend/src/test` e podem ser executados com:

```bash
cd backend
mvn test
```

Os testes incluem a rejeição de cenário sem `ENTAO`, de sugestão rejeitada sem motivo, o bloqueio de exportação com sugestão pendente e uma regra ArchUnit que impede acesso direto de controllers a repositories. O [roteiro de verificação](docs/roteiro-verificacao.md) contém entradas, resultados esperados e campos para resultados observados. Os testes Java ainda precisam ser executados com Maven; os exemplos de respostas HTTP também não foram observados em uma execução completa.

O histórico já contém os commits das semanas 1, 2 e 3. Depois que `mvn test`, os endpoints e a SPA com dados reais estiverem verificados, marque o commit validado:

```bash
git tag api-completa
```

Para concluir a **Semana 4 (até 16/11)**: construir os editores de história, cenários e passos, interface de registro/decisão de sugestões ao lado da história, download `.feature` e tela do resumo; executar roteiro completo; concluir o registro de seis interações revisadas pelo grupo; preparar a entrega. A atividade de fluxo de aprovação configurável é opcional e está **fora deste escopo**.

### Pendências para a entrega única em 17/11/2026

- Executar `mvn test`, verificar a API com PostgreSQL e registrar entradas, resultados esperados e observados no [roteiro](docs/roteiro-verificacao.md).
- Completar a SPA da Semana 4 e demonstrar o fluxo de história, cenários, sugestões, exportação e resumo.
- Exportar de fato um `.feature` pela API, guardar o arquivo no pacote de entrega e conferir seu Gherkin em português; o [exemplo isolado](docs/exemplo-exportacao.feature) ainda não satisfaz essa evidência.
- Registrar e revisar com o grupo ao menos seis interações reais com IA (três da API e três da SPA) em [docs/registro-ia.md](docs/registro-ia.md); o conteúdo atual é um roteiro de propostas.
- Reunir as evidências, criar a tag `entrega-final`, preparar o ZIP dessa versão e enviar o link do repositório e o ZIP no Moodle. Não incluir `node_modules`, pastas de compilação nem credenciais.

## 8. Limitações e decisões de projeto

- Banco com `ddl-auto=update` exclusivamente para desenvolvimento acadêmico, para criar tabelas facilmente. Para produção, usar migrations (Flyway/Liquibase) e políticas de segurança reais.
- A identidade por cabeçalho não prova que o solicitante seja aquele usuário.
- As alterações de um cenário completo substituem sua lista de passos em uma transação; para editar um passo conservando ID, use os endpoints de passos.
- Não há integração automática com LLM: o usuário copia prompt e resposta da ferramenta de sua escolha; decisão é humana.
- O front usa Google Fonts (requer internet para a fonte, mas possui fallback local).
- Até a Semana 3, a SPA é propositalmente parcial; não há simulação falsa de telas ainda não implementadas.
