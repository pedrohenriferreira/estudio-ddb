# Registro de IA — preparação para a documentação obrigatória

O enunciado exige **ao menos seis interações relevantes**, três da API e três da SPA, incluindo problema, ferramenta e prompt, proposta da IA, decisão do grupo, mudanças e verificação. Esta entrega foi estruturada com assistência de IA, mas **não temos registro de seis sessões independentes nem aprovação dos demais integrantes**. Os itens abaixo são **roteiros e propostas de registro**, já alinhados com o código disponível, a serem executados, revisados e completados pelo grupo. Não apresentá-los como interações/testes realmente verificados sem esse trabalho.

## Primeira ampliação da história inicial (Semana 1)

**História escrita sem IA (modelo para transcrição manual):** `docs/historia-3-cenarios.feature` — o analista rejeita sugestões inadequadas, com os cenários: rejeição válida, ausência de motivo e impossibilidade de modificar decisão já tomada.

**Prompt proposto para IA:** "Leia esta história de usuário e os três cenários em Gherkin. Sugira dois cenários adicionais, distintos e concretos, para decisões de sugestão de IA. Não altere as regras: rejeitar exige motivo e uma sugestão decidida não pode ser alterada."

**Possíveis respostas de ampliação para avaliação pelo grupo:**
- *Útil:* "Dado que uma sugestão está pendente, quando o analista a aceita, então a sugestão fica ACEITA e nenhum cenário muda automaticamente." Acrescenta a regra de não aplicar sugestão automaticamente.
- *Genérica:* "Dado que o sistema está disponível, quando o usuário usa IA, então o sistema funciona corretamente." Não contém um comportamento verificável e deve ser rejeitada como `GENERICA`.

**Decisão proposta:** aproveitar o primeiro cenário como exemplo de regra relevante; rejeitar o segundo por generalidade. **Status:** a ser validado pelo grupo e registrado pelo sistema quando houver execução.

## Três interações da API — roteiros de revisão

### API-1 — Validação de sequência BDD
- **Problema:** impedir `ENTAO` antes de `QUANDO`, `E` inicial, falta de `ENTAO` e posições repetidas.
- **Ferramenta sugerida:** ChatGPT; **prompt:** "Revise a função ValidadorCenario Java 21 para verificar a ordem DADO→QUANDO→ENTAO, a continuação E/MAS e posições únicas; identifique casos de borda."
- **Proposta a avaliar:** estado de sequência, continuidade e testes para cada caminho inválido.
- **Decisão do grupo:** *pendente de validação*; código-base presente em `service/ValidadorCenario.java`.
- **Mudanças:** ajustar função se testes apontarem falhas.
- **Verificação necessária:** `mvn test` e chamadas para cenário inválido (422).

### API-2 — Curadoria de sugestões
- **Problema:** não permitir rejeição sem motivo, redecisão ou aplicação automática.
- **Ferramenta sugerida:** ChatGPT; **prompt:** "Revise regras de decisão de SugestaoService: PENDENTE/ACEITA/REJEITADA, motivo obrigatório e bloqueio de nova decisão, sem mudar cenários automaticamente."
- **Proposta a avaliar:** validação antes da alteração e persistência do decisor/data.
- **Decisão do grupo:** *pendente de validação*; implementação inicial presente.
- **Mudanças:** corrigir se algum caminho violar regra.
- **Verificação necessária:** testes unitários + requisições POST decisão.

### API-3 — Camadas e DTOs
- **Problema:** controllers não devem depender de repositories e erros precisam de formato claro.
- **Ferramenta sugerida:** ChatGPT; **prompt:** "Analise controllers, services e repositories do projeto. Proponha regra ArchUnit para impedir controller→repository e padronização de erros com ProblemDetail."
- **Proposta a avaliar:** regra `noClasses()` e exceções de negócio com HTTP status coerente.
- **Decisão do grupo:** *pendente de validação*; estrutura e teste adicionados.
- **Mudanças:** realizar ajustes identificados por `mvn test`.
- **Verificação necessária:** ArchUnit em `mvn test`, inspecionar retorno JSON 403/422.

## Três interações da SPA — roteiros de revisão

### SPA-1 — Busca de dados reais
- **Problema:** lista de projetos deve consumir a API, não dados mockados.
- **Ferramenta sugerida:** ChatGPT; **prompt:** "Crie no React um cliente fetch para GET /api/usuarios e /api/projetos com tratamento de ProblemDetail e estados de erro e carregamento."
- **Proposta a avaliar:** `src/api.js`, carregamento em `App.jsx` e possibilidade de tentar novamente.
- **Decisão do grupo:** *pendente de validação*; código-base presente.
- **Mudanças:** revisar comportamento de falha/timeout após executar localmente.
- **Verificação necessária:** abrir SPA com API ligada/desligada.

### SPA-2 — Filtro de prioridade
- **Problema:** filtro deve usar `GET /api/projetos/{id}/historias?prioridade=...`.
- **Ferramenta sugerida:** ChatGPT; **prompt:** "Implemente filtro em React que consulte o backend por prioridade, abortando requisições obsoletas para evitar mistura de resultados."
- **Proposta a avaliar:** `AbortController` e lista renderizada após alteração do filtro.
- **Decisão do grupo:** *pendente de validação*; código-base presente.
- **Mudanças:** revisar navegação rápida e estados vazios.
- **Verificação necessária:** alternar prioridades e comparar retorno da API.

### SPA-3 — Identidade simulada e criação
- **Problema:** autor deve ser o usuário selecionado e passado via cabeçalho.
- **Ferramenta sugerida:** ChatGPT; **prompt:** "Construa formulário React para criar história de usuário usando X-Usuario-Id do usuário de demonstração selecionado; mostre erros HTTP ao usuário."
- **Proposta a avaliar:** formulário `NovaHistoria` e `api()` com cabeçalho.
- **Decisão do grupo:** *pendente de validação*; código-base presente.
- **Mudanças:** revisar campos, mensagem 409 e autoria.
- **Verificação necessária:** criar história, consultar autor, editar via API com outro ID e obter 403.

## Registro real — completar a cada interação

Copie para cada uso real: **data / integrante / problema / ferramenta / prompt integral / trecho ou resumo da proposta / decisão aceita ou rejeitada + motivo / arquivos alterados / teste executado + saída observada / commit**.
