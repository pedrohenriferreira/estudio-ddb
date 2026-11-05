# AGENTS.md — regras do Estúdio BDD

## Escopo
Projeto acadêmico PS2 2026/2. Implementar o enunciado e manter as cinco camadas. Nesta entrega implementar até a Semana 3; não adicionar autenticação real, fluxo bônus ou integração automática com IA sem autorização.

## Arquitetura
- SPA (`frontend`) só executa apresentação e consome HTTP JSON, sem duplicar regras de negócio.
- Controllers recebem `@Valid` DTOs e delegam a serviços, sem dependência de repository.
- Services executam regras e transações; repositories apenas consultas JPA; model contém entidades e enums.
- `DtoMapper` deve ser chamado com sessão JPA ativa. Associações `@ManyToOne` são `LAZY`; coleções têm cascata apenas de pai para filho.
- Dependências Spring sempre por construtor (exceto injeção de valor de configuração).

## Regras funcionais
1. Usuário.email, Projeto.nome únicos; HistoriaUsuario.titulo único por projeto; Cenario.titulo único por história.
2. História exige título, papel, objetivo, benefício e prioridade.
3. Cenário com QUANDO + ENTAO, ordem DADO → QUANDO → ENTAO (E e MAS continuam o anterior e não começam); posições distintas.
4. Só autor edita/exclui história, cenário ou passo; `X-Usuario-Id` é identidade simulada, não autenticação.
5. Sugestão nasce PENDENTE; rejeição exige motivo; decisão é final; aceitar não altera cenários.
6. Exportação exige cenário e zero sugestões pendentes; `# language: pt`, `Funcionalidade`, `Cenário`, `Dado`, `Quando`, `Então`, `E`, `Mas`.
7. Resumo por origem de cenário, decisão de sugestão e motivos de rejeição.

## Comandos
```
docker compose up -d db
cd backend && mvn test && mvn spring-boot:run -Dspring-boot.run.profiles=demo
cd frontend && npm install && npm run dev
cd frontend && npm run build
```

## Fluxo de trabalho com IA
Propor mudança pequena → revisar modelo, camadas e regras → executar `mvn test` e `npm run build` → validar manualmente os endpoints afetados → registrar prompt, proposta, decisão e verificação em `docs/registro-ia.md`. Não marcar sucesso que não foi observado. Nunca enviar credenciais.

## Convenções
Nomes de domínio/DTOs em português; rotas plurais `/api/...`; Java 21, classes de serviço pequenas por agregado; exceções de regra com mensagens claras e ProblemDetail; retornos 201 criação, 204 exclusão, 400 validação de DTO, 403 autoria, 404 não encontrado, 409 unicidade/decisão já tomada, 422 quebra de regra. Não alterar endpoint sem atualizar documentação e testes.
