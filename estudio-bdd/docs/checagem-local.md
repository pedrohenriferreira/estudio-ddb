# Relatório de checagem técnica — 03/11 e 05/11/2026

## Verificação adicional em 05/11/2026

- `npm install --no-audit --no-fund`: concluiu e gerou `frontend/package-lock.json`.
- `npm run build`: concluiu; Vite 6.4.5 transformou 1580 módulos e gerou `frontend/dist`.
- `java` e `mvn` não foram encontrados no PATH; `mvn test` e a API não foram executados nesta sessão.
- Docker estava disponível, mas o banco e a aplicação não foram iniciados nesta sessão. Não há resultados HTTP observados.

## Checagens da geração original em 03/11/2026

Estas são **checagens efetivamente realizadas**, sem alegar que o aplicativo completo foi executado:

| Tipo | Resultado | Limitação |
|---|---|---|
| Sintaxe Java | 39 arquivos `.java` analisados sem erro de parsing | Não substitui compilação Maven |
| Tipagem Java aproximada | Código-fonte e testes compilados com JDK 21 e **stubs locais das APIs Spring/JPA/JUnit** | Stubs verificam assinaturas/Java, não a compatibilidade real com dependências Maven |
| Validador BDD | **12 casos isolados passaram**, executando `ValidadorCenario` original com classes de apoio simplificadas | Não usam Spring nem banco |
| Exportação Gherkin | `ExportacaoService.exportar()` executado com repositórios simulados; saída escrita em UTF-8 em `docs/exemplo-exportacao.feature` | Não é teste HTTP e não é arquivo exportado da aplicação com banco |
| Sintaxe React | `App.jsx` e `main.jsx` analisados pelo parser TypeScript em modo JSX; zero erros | Não substitui `npm run build` |
| Sintaxe JS | `api.js` e `vite.config.js` passaram por `node --check` | Não confirma dependências/npm |
| Sintaxe CSS | `styles.css` analisado pelo PostCSS, sem erro | Não comprova renderização visual em navegador |
| JSON | `frontend/package.json` lido com sucesso | Não confirma instalação de pacotes |

## Bloqueios do ambiente de geração original

- O executável `mvn` não está instalado.
- Não há conexão DNS com Maven Central e registro npm para baixar dependências.
- Não há serviço PostgreSQL/Docker neste ambiente.

**Para completar o marco de verificação:** instalar Java 21 e Maven, subir PostgreSQL, executar `mvn clean test`, iniciar API com perfil `demo`, executar requisições em `docs/roteiro-verificacao.md` e abrir a SPA com dados reais. Capturar as saídas reais e só então marcar/taguear a versão `api-completa`.
