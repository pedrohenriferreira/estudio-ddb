# Endpoints REST — Estúdio BDD

Base URL: `http://localhost:8080/api`. Requisições/respostas JSON, exceto a exportação `.feature` (texto plano). Rotas mutáveis de histórias/cenários/passos/sugestões recebem `X-Usuario-Id: <id>` correspondente a usuário cadastrado. **Cabeçalho não autentica**: identidade simulada de sala de aula.

## Tabela de endpoints

| Método | Caminho | Descrição | Autoria/cabeçalho |
|---|---|---|---|
| `GET` | `/usuarios` | Lista usuários | Não |
| `POST` | `/usuarios` | Cadastra usuário | Não |
| `GET` | `/projetos` | Lista projetos | Não |
| `POST` | `/projetos` | Cria projeto | Não |
| `GET` | `/projetos/{id}` | Detalha projeto | Não |
| `PUT` | `/projetos/{id}` | Atualiza projeto | Não |
| `DELETE` | `/projetos/{id}` | Exclui projeto vazio; se houver histórias, retorna 409 | Não |
| `GET` | `/projetos/{id}/historias?prioridade=ALTA` | Histórias + contagens (filtro opcional) | Não |
| `POST` | `/projetos/{id}/historias` | Cria história | X-Usuario-Id |
| `GET` | `/historias/{id}` | Detalha história | Não |
| `PUT` | `/historias/{id}` | Atualiza história | Autor |
| `DELETE` | `/historias/{id}` | Exclui história | Autor |
| `GET` | `/historias/{id}/cenarios` | Cenários com passos ordenados | Não |
| `POST` | `/historias/{id}/cenarios` | Cria cenário com passos | Autor |
| `GET` | `/cenarios/{id}` | Detalha cenário | Não |
| `PUT` | `/cenarios/{id}` | Edita cenário e substitui os passos | Autor |
| `DELETE` | `/cenarios/{id}` | Exclui cenário e passos | Autor |
| `GET` | `/cenarios/{id}/passos` | Lista passos ordenados | Não |
| `POST` | `/cenarios/{id}/passos` | Adiciona passo validando cenário resultante | Autor |
| `PUT` | `/passos/{id}` | Edita passo validando cenário resultante | Autor |
| `DELETE` | `/passos/{id}` | Remove passo se cenário continuar válido | Autor |
| `GET` | `/historias/{id}/sugestoes?decisao=PENDENTE` | Sugestões, filtro opcional | Não |
| `POST` | `/historias/{id}/sugestoes` | Registra sugestão da IA, inicialmente PENDENTE | X-Usuario-Id |
| `POST` | `/sugestoes/{id}/decisao` | Aceita/rejeita sugestão uma única vez | X-Usuario-Id |
| `GET` | `/historias/{id}/exportacao` | Download `.feature` se autorizado | Não |
| `GET` | `/projetos/{id}/resumo` | Contagens por origem/decisão/motivo | Não |

## Formatos dos corpos de entrada

**POST `/usuarios`**
```json
{ "nome": "Ana Analista", "email": "ana@example.com" }
```

**POST/PUT `/projetos`**
```json
{ "nome": "Biblioteca digital", "descricao": "Empréstimos e reservas", "contextoDominio": "Leitor: pessoa que consulta livros; exemplar: cópia física" }
```

**POST/PUT `/historias`**
```json
{
  "titulo": "Reservar livro disponível",
  "papel": "leitor autenticado",
  "objetivo": "reservar um livro do catálogo",
  "beneficio": "retirá-lo posteriormente",
  "prioridade": "ALTA"
}
```

**POST/PUT `/cenarios`**
```json
{
  "titulo": "Reserva confirmada",
  "posicao": 1,
  "origem": "HUMANO",
  "passos": [
    { "tipo": "DADO", "texto": "que o livro está disponível", "posicao": 1 },
    { "tipo": "QUANDO", "texto": "o leitor solicita a reserva", "posicao": 2 },
    { "tipo": "ENTAO", "texto": "o sistema confirma a reserva", "posicao": 3 }
  ]
}
```

Em `PUT /cenarios/{id}` o array de passos é **substituído integralmente**; IDs antigos não são preservados. Os endpoints individuais de passo recebem `{ "tipo": "E", "texto": "a confirmação é enviada", "posicao": 4 }`. Em qualquer operação, não é permitido deixar cenário temporariamente inválido. Posições de passos devem ser distintas e positivas; a ordem é baseada no número informado.

**POST `/historias/{id}/sugestoes`**
```json
{
  "ferramenta": "ChatGPT",
  "prompt": "Proponha um cenário alternativo para a reserva de livro",
  "conteudo": "Cenário: livro indisponível ..."
}
```

**POST `/sugestoes/{id}/decisao`**
```json
{ "decisao": "REJEITADA", "motivoRejeicao": "GENERICA", "justificativa": "Não considera as regras do acervo." }
```
Ou `{"decisao":"ACEITA"}`. Enums para `motivoRejeicao`: `INCORRETA`, `GENERICA`, `DUPLICADA`, `FORA_DO_ESCOPO`, `OUTRO`. Para `OUTRO` é exigida justificativa. Aceitar sugestão **não cria cenário automaticamente**.

## Exemplo de saída do resumo

`GET /api/projetos/1/resumo`:
```json
{
  "projetoId": 1,
  "quantidadeHistorias": 2,
  "cenariosPorOrigem": { "HUMANO": 1, "SUGESTAO_IA": 0 },
  "sugestoesPorDecisao": { "PENDENTE": 1, "ACEITA": 0, "REJEITADA": 0 },
  "motivosRejeicao": {
    "INCORRETA": 0,
    "GENERICA": 0,
    "DUPLICADA": 0,
    "FORA_DO_ESCOPO": 0,
    "OUTRO": 0
  }
}
```
Os valores acima ilustram o estado inicial **esperado** com `demo` em banco vazio; não são um retorno observado por execução nesta sessão.

## Códigos HTTP e erros

- `200 OK`: consulta e alteração bem-sucedida; `201 Created`: criação; `204 No Content`: exclusão.
- `400 Bad Request`: DTO inválido, JSON malformado ou cabeçalho/enum inválido.
- `403 Forbidden`: somente o autor pode alterar/excluir história, cenário e passos.
- `404 Not Found`: ID inexistente.
- `409 Conflict`: nome/título duplicado, posição de cenário repetida ou sugestão já decidida.
- `422 Unprocessable Entity`: cenário inválido, rejeição sem motivo ou exportação proibida.

Resposta de erro segue RFC 9457 via `ProblemDetail` (campos `type`, `title`, `status`, `detail`), com mensagens legíveis em português.
