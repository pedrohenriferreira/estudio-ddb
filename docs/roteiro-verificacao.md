# Roteiro de verificação — Semanas 1–3

**Situação:** código e testes preparados; `npm install` e `npm run build` concluídos em 05/11/2026. Maven/Java e a API não foram executados nesta sessão. **Resultado observado** fica pendente para os testes de API e SPA com dados reais. A coluna 'Esperado' não significa teste aprovado.

## Verificação automatizada

```bash
cd backend
mvn clean test
```

Testes incluídos:
- `ValidadorCenarioTest`: sem ENTÃO, começa com E, ordem inválida, sequência válida fora de ordem na entrada.
- `SugestaoServiceTest`: rejeição sem motivo, `OUTRO` sem justificativa, aceite válido.
- `ExportacaoServiceTest`: sugestão pendente, história sem cenário, cenário válido.
- `ArquiteturaTest`: controllers **não dependem** de repositories (ArchUnit).

**Resultado observado:** `[a preencher: data, ambiente, contagem de testes, OK/falhas]`.

## Verificação por cliente HTTP + SPA

| # | Entrada / ação | Resultado esperado | Resultado observado |
|---|---|---|---|
| 1 | Criar projeto e história com título único e narrativa completa | HTTP 201, IDs e campos persistidos | **Pendente** |
| 2 | Repetir título da história no mesmo projeto | HTTP 409 com `detail` de unicidade | **Pendente** |
| 3 | Criar história sem benefício ou com campo vazio | HTTP 400 de validação | **Pendente** |
| 4 | Criar cenário com DADO, QUANDO, ENTAO em posições 1–3 | HTTP 201; três passos ordenados | **Pendente** |
| 5 | Cenário sem QUANDO ou sem ENTAO | HTTP 422 com motivo | **Pendente** |
| 6 | Cenário inicia por E; ou DADO após ENTAO | HTTP 422 | **Pendente** |
| 7 | Editar um passo válido alterando texto, e consultar cenário | Ordem preservada e texto atualizado | **Pendente** |
| 8 | Alterar posição do passo criando posição duplicada | HTTP 422 | **Pendente** |
| 9 | Criar com usuário 1, editar/excluir história com usuário 2 | HTTP 403 | **Pendente** |
| 10 | Criar com usuário 1, editar/excluir cenário e passo com usuário 2 | HTTP 403 | **Pendente** |
| 11 | Registrar sugestão | HTTP 201, `decisao=PENDENTE` | **Pendente** |
| 12 | Rejeitar sugestão sem `motivoRejeicao` | HTTP 422 | **Pendente** |
| 13 | Rejeitar com motivo ou aceitar sem motivo | HTTP 200 e decisão armazenada | **Pendente** |
| 14 | Decidir novamente uma sugestão já decidida | HTTP 409, estado anterior preservado | **Pendente** |
| 15 | Exportar história sem cenários | HTTP 422 | **Pendente** |
| 16 | Exportar história com sugestão pendente | HTTP 422 | **Pendente** |
| 17 | Após decisão, exportar história com cenário | HTTP 200, `.feature`, primeira linha `# language: pt` | **Pendente** |
| 18 | Filtrar histórias por prioridade | Somente itens com prioridade informada | **Pendente** |
| 19 | Consultar `/projetos/{id}/resumo` antes/depois da decisão | Quantidades coerentes por origem, decisão, motivos | **Pendente** |
| 20 | Desligar e religar API/PostgreSQL mantendo volume | Dados ainda presentes | **Pendente** |
| 21 | SPA: carregar usuários, projetos e histórias | Dados reais da API exibidos | **Pendente** |
| 22 | SPA: trocar usuário e criar história | `autorId` corresponde ao usuário selecionado | **Pendente** |
| 23 | SPA: mudar filtro e pesquisar | Lista responde ao filtro e busca sem misturar projetos | **Pendente** |
| 24 | SPA sem API / API retorna erro | Mensagem clara, opção de tentar novamente | **Pendente** |
| 25 | `mvn test`, `npm run build` | Finalizam sem erros | **Frontend:** `npm run build` passou em 05/11/2026; **backend:** pendente |

## Marco da Semana 1 (27/10)

- [x] Modelo de classes documentado.
- [x] Projeto Spring Boot, entidades e associações codificadas.
- [x] `AGENTS.md`, README e atividade BDD em arquivo textual.
- [ ] Aplicação realmente iniciou com PostgreSQL e criou tabelas (verificar).
- [ ] Três cenários transcritos **à mão**.
- [ ] Primeiro commit no repositório do grupo.

## Marco da Semana 2 (03/11)

- [x] CRUD e regras de autoria/cenários/erros **codificados**.
- [ ] Requisições válidas e inválidas executadas e observadas via cliente HTTP.

## Marco da Semana 3 (10/11)

- [x] Sugestões, exportação, consulta/resumo, testes e ArchUnit **codificados**.
- [x] SPA inicial com usuários, projetos e histórias **codificada**.
- [x] Endpoints documentados.
- [ ] `mvn test` executado e aprovado.
- [ ] SPA renderiza dados reais da API em execução.
- [ ] Marcador `api-completa` criado no Git **após verificação**.
- [ ] Registro de interações com IA revisado com evidências reais.

## Evidências para Moodle

Anexe capturas da API respondendo, testes executados, SPA carregada, `.feature` **efetivamente exportado** e repositório/tag. Não use os arquivos de exemplo como evidências de teste executado.
