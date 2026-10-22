# Sistema MOTIVA — Monitoramento de Vegetação em Rodovias

API REST em **Spring Boot + Spring Data JPA** sobre o banco **Oracle FIAP**, evolução do protótipo de console das Sprints 1–3.

| Sprint | O que foi construído |
|---|---|
| 1 | Domínio base (`TrechoRodovia`), encapsulamento, protótipo console |
| 2 | Motor de regras: classe abstrata `IntervencaoOperacional`, interface `MonitoravelViaIoT`, polimorfismo |
| 3 | Persistência com JDBC puro, padrão DAO, CRUD no Oracle, histórico de relatórios |
| **4** | **Spring Boot 4.1 + JPA + API REST, DTOs, Bean Validation, tratamento global de erros e testes** |

---

## 1. O que mudou da Sprint 3 para a Sprint 4

| Sprint 3 (JDBC puro) | Sprint 4 (Spring Boot) |
|---|---|
| Classes de `model/` + mapeamento manual no DAO | `@Entity` em `model/` (mapeamento via JPA) |
| 4 DAOs com `PreparedStatement` | 4 interfaces `JpaRepository` com *derived queries* — **zero SQL manual** |
| `ConexaoBD` (Singleton) com senha no código | `application.properties` + variáveis de ambiente + pool HikariCP |
| `GeradorRelatorio` / `IntervencaoService` | `@Service` (regras de negócio continuam aqui) |
| `Main` com `System.out.println` | `@RestController` devolvendo JSON |
| `GENERATED ALWAYS AS IDENTITY` | `@SequenceGenerator` ligado às sequences `SEQ_*` |

A herança da Sprint 2 foi preservada no JPA com `@Inheritance(SINGLE_TABLE)`:

- `TrechoRodovia` / `TrechoMonitoradoIoT` → tabela `TRECHO_RODOVIA`, discriminador `MONITORADO_IOT` (0/1). Só o `TrechoMonitoradoIoT` implementa `MonitoravelViaIoT`.
- `SemIntervencao` / `Pulverizacao` / `RocadaMecanizada` → tabela `INTERVENCAO_OPERACIONAL`, discriminador `TIPO_INTERVENCAO`. Ao ler do banco, o Hibernate instancia a subclasse certa, e `getDescricao()` / `descreverServico()` continuam polimórficos.

## 2. Resposta ao feedback da Sprint 3

| Pendência | Como foi resolvida |
|---|---|
| Senha real do Oracle fixa em `ConexaoBD.java` | `ConexaoBD` foi removida. Credenciais vêm de `ORACLE_USER` / `ORACLE_PASSWORD` (variáveis de ambiente). `.gitignore` bloqueia `.env` e `application-local.properties`. A senha foi trocada no portal. |
| `SQLException` sem tratamento / sem mensagem orientativa | `GlobalExceptionHandler` (`@RestControllerAdvice`): todo erro vira um JSON padronizado com o status HTTP certo (400, 404, 409, 503...) e uma mensagem dizendo o que fazer. Falha de banco (o antigo `SQLException`) vira **503** com mensagem amigável. |
| `EQUIPE_MANUTENCAO` isolada | Nova FK `INTERVENCAO_OPERACIONAL.ID_EQUIPE → EQUIPE_MANUTENCAO.ID` (`@ManyToOne`). Consultas de alocação: `GET /api/equipes/{id}/intervencoes` e `GET /api/trechos/{id}/intervencoes`. Scripts SQL ajustados. |

## 3. Tecnologias

Java 17 · Spring Boot 4.1.1 · Spring Web MVC · Spring Data JPA (Hibernate) · Bean Validation · Oracle (driver `ojdbc17` via Maven) · JUnit 5 + MockMvc + H2 (testes) · Maven

## 4. Estrutura

```
src/main/java/br/com/motiva/
├── MotivaApplication.java     # @SpringBootApplication
├── model/                     # @Entity: TrechoRodovia, TrechoMonitoradoIoT, EquipeManutencao,
│                              # IntervencaoOperacional (+ 3 subclasses), RelatorioPrioridade,
│                              # Constantes, NivelPrioridade, MonitoravelViaIoT
├── repository/                # interfaces JpaRepository (sem SQL)
├── service/                   # regras de negócio: IntervencaoService (motor), GeradorRelatorio,
│                              # TrechoRodoviaService, EquipeManutencaoService, IntervencaoOperacionalService
├── controller/                # @RestController
├── dto/                       # records de entrada (Request) e saída (Response)
└── exception/                 # exceções de negócio + GlobalExceptionHandler
src/main/resources/application.properties
src/test/java/.../MotivaApiTest.java      # @SpringBootTest + MockMvc
sql/  migracao-sprint4.sql · script-criacao.sql · script-dados.sql
evidencias/                               # prints das requisições
```

Fluxo das camadas: **Controller → Service → Repository → Banco**. O Controller não tem regra de negócio; o Service não conhece HTTP.

## 5. Como executar

### 5.1 Banco
- **Já tenho as tabelas da Sprint 3:** rode `sql/migracao-sprint4.sql` uma vez (no DBeaver use *Executar script*, Alt+X).
- **Banco vazio:** rode `sql/script-criacao.sql` e depois `sql/script-dados.sql`.

### 5.2 Credenciais (variáveis de ambiente)
Linux/Mac:
```bash
export ORACLE_USER=RM000000
export ORACLE_PASSWORD='sua_senha'
```
Windows (PowerShell):
```powershell
$env:ORACLE_USER="RM000000"
$env:ORACLE_PASSWORD="sua_senha"
```

### 5.3 Subir a API
```bash
mvn spring-boot:run
```
A API fica em `http://localhost:8080`.

### 5.4 Testes automatizados
```bash
mvn test
```
Os testes usam H2 em memória (perfil `test`), então não precisam do Oracle nem das variáveis de ambiente.

## 6. Endpoints

### Trechos — `/api/trechos`
| Método | Rota | Ação | Sucesso | Erros |
|---|---|---|---|---|
| GET | `/api/trechos` | Lista todos (filtros opcionais: `nivelMinimo`, `tipoTerreno`, `rodovia`) | 200 | 400 |
| GET | `/api/trechos/{id}` | Busca por ID | 200 | 404 |
| POST | `/api/trechos` | Cria (`monitoradoIot: true` cria um trecho IoT) | 201 + `Location` | 400 |
| PUT | `/api/trechos/{id}` | Atualiza | 200 | 400 / 404 |
| DELETE | `/api/trechos/{id}` | Remove | 204 | 404 / 409 (tem intervenções) |
| POST | `/api/trechos/{id}/leitura-sensor` | Simula leitura do sensor IoT (aplica crescimento) | 200 | 400 (não é IoT) / 404 |
| GET | `/api/trechos/{id}/intervencoes` | Intervenções do trecho | 200 | 404 |

### Equipes — `/api/equipes`
| Método | Rota | Ação | Sucesso | Erros |
|---|---|---|---|---|
| GET | `/api/equipes` | Lista (filtros opcionais: `nome`, `responsavel`) | 200 | — |
| GET | `/api/equipes/{id}` | Busca por ID | 200 | 404 |
| POST | `/api/equipes` | Cria | 201 | 400 |
| PUT | `/api/equipes/{id}` | Atualiza | 200 | 400 / 404 |
| DELETE | `/api/equipes/{id}` | Remove | 204 | 404 / 409 (alocada) |
| GET | `/api/equipes/{id}/intervencoes` | Alocação da equipe | 200 | 404 |

### Intervenções — `/api/intervencoes`
| Método | Rota | Ação | Sucesso | Erros |
|---|---|---|---|---|
| GET | `/api/intervencoes` | Lista (filtro opcional `tipo`: `SEM_INTERVENCAO`, `PULVERIZACAO`, `ROCADA_MECANIZADA`) | 200 | 400 |
| GET | `/api/intervencoes/{id}` | Busca por ID | 200 | 404 |
| POST | `/api/intervencoes` | Registra; **o tipo é decidido pelo motor de regras** conforme o nível do trecho | 201 | 400 / 404 |
| PUT | `/api/intervencoes/{id}` | Atualiza equipe, data e observação | 200 | 400 / 404 |
| DELETE | `/api/intervencoes/{id}` | Remove | 204 | 404 |

### Relatórios — `/api/relatorios`
| Método | Rota | Ação | Sucesso | Erros |
|---|---|---|---|---|
| POST | `/api/relatorios` | Gera relatório dos trechos atuais e salva no histórico | 201 | 400 (sem trechos) |
| GET | `/api/relatorios` | Histórico (mais recente primeiro) | 200 | — |
| GET | `/api/relatorios/{id}` | Busca um relatório | 200 | 404 |
| GET | `/api/relatorios/periodo?inicio=AAAA-MM-DD&fim=AAAA-MM-DD` | Histórico por período (inclusivo) | 200 | 400 |

### Regras de classificação (motor — `IntervencaoService`)
| Nível de vegetação | Classificação | Intervenção |
|---|---|---|
| ≥ 45 cm | URGENTE | Roçada Mecanizada (Urgente) |
| ≥ 30 cm | CRÍTICO | Roçada Mecanizada |
| ≥ 15 cm | ATENÇÃO | Pulverização |
| < 15 cm | NORMAL | Sem Intervenção |

### Formato de erro
```json
{
  "timestamp": "2026-10-21T14:30:00",
  "status": 400,
  "erro": "Bad Request",
  "mensagem": "Dados inválidos. Corrija os campos indicados.",
  "caminho": "/api/trechos",
  "campos": { "nivelVegetacao": "O nível de vegetação não pode ser negativo." }
}
```

## 7. Exemplos cURL

```bash
# 1) Criar trecho monitorado via IoT -> 201
curl -i -X POST http://localhost:8080/api/trechos \
  -H "Content-Type: application/json" \
  -d '{"nomeRodovia":"BR-116","quilometroInicial":10,"quilometroFinal":15,"nivelVegetacao":48,"tipoTerreno":"UMIDO","monitoradoIot":true}'

# 2) Listar trechos com vegetação >= 30 cm -> 200
curl -i "http://localhost:8080/api/trechos?nivelMinimo=30"

# 3) Buscar trecho inexistente -> 404
curl -i http://localhost:8080/api/trechos/9999

# 4) Validação: nível negativo -> 400
curl -i -X POST http://localhost:8080/api/trechos \
  -H "Content-Type: application/json" \
  -d '{"nomeRodovia":"BR-101","quilometroInicial":1,"quilometroFinal":2,"nivelVegetacao":-5,"tipoTerreno":"SECO"}'

# 5) Atualizar trecho -> 200
curl -i -X PUT http://localhost:8080/api/trechos/1 \
  -H "Content-Type: application/json" \
  -d '{"nomeRodovia":"BR-116","quilometroInicial":10,"quilometroFinal":15,"nivelVegetacao":20,"tipoTerreno":"SECO"}'

# 6) Criar equipe -> 201
curl -i -X POST http://localhost:8080/api/equipes \
  -H "Content-Type: application/json" \
  -d '{"nomeEquipe":"Equipe Leste","responsavel":"João Lima"}'

# 7) Registrar intervenção (motor decide o tipo) -> 201
curl -i -X POST http://localhost:8080/api/intervencoes \
  -H "Content-Type: application/json" \
  -d '{"trechoId":1,"equipeId":1,"observacao":"Vistoria de rotina"}'

# 8) Alocação da equipe -> 200
curl -i http://localhost:8080/api/equipes/1/intervencoes

# 9) Leitura de sensor IoT -> 200 (ou 400 se o trecho não for IoT)
curl -i -X POST http://localhost:8080/api/trechos/1/leitura-sensor

# 10) Gerar relatório -> 201
curl -i -X POST http://localhost:8080/api/relatorios

# 11) Histórico -> 200
curl -i http://localhost:8080/api/relatorios

# 12) Histórico por período -> 200
curl -i "http://localhost:8080/api/relatorios/periodo?inicio=2026-10-01&fim=2026-10-31"

# 13) Excluir equipe alocada -> 409 ; excluir intervenção -> 204
curl -i -X DELETE http://localhost:8080/api/equipes/1
curl -i -X DELETE http://localhost:8080/api/intervencoes/1
```

## 8. Evidências

Prints das requisições em [`evidencias/`](evidencias/):

| # | Requisição | Status |
|---|---|---|
| 1 | `POST /api/trechos` | 201 |
| 2 | `GET /api/trechos/9999` | 404 |
| 3 | `POST /api/trechos` com nível negativo | 400 |
| 4 | `POST /api/intervencoes` | 201 |
| 5 | `POST /api/relatorios` | 201 |
| 6 | `GET /api/relatorios/periodo` | 200 |
| 7 | `DELETE /api/equipes/{id}` alocada | 409 |
| 8 | `mvn test` | testes passando |

## 9. Perguntas de reflexão

**1. Por que o Repository é uma interface e não uma classe? Quem escreve a implementação e quando?**
Porque nós só declaramos *o que* queremos (salvar, buscar por ID, buscar por tipo...) e não *como* fazer. Quem escreve a implementação é o Spring Data JPA, em tempo de execução: na inicialização da aplicação ele cria um *proxy* (uma classe gerada dinamicamente) que implementa a interface, traduz cada método em JPQL/SQL e o registra como bean. Por ser interface, o Service depende só do contrato, e a implementação pode ser trocada (ex.: H2 nos testes, Oracle em produção) sem mudar o código.

**2. O pattern DAO da Sprint 3 "morreu" na migração ou só mudou de forma?**
Só mudou de forma. A ideia do DAO — isolar o acesso a dados do resto do sistema — continua igual: o Service ainda não sabe nada de SQL nem de conexão. A diferença é que, na Sprint 3, nós escrevíamos o DAO à mão (`PreparedStatement`, `ResultSet`, fechar recursos, ~375 linhas no `EquipeManutencaoDAO`); agora declaramos uma interface de poucas linhas e o framework gera o DAO por nós. O Repository *é* um DAO, só que gerado.

**3. Por que a validação de `nivelVegetacao >= 0` deve ficar no Service e não no Controller?**
Porque é uma regra de negócio, não uma regra de HTTP. O Controller é só uma porta de entrada; amanhã os dados podem chegar por outra porta (uma fila de mensagens do sensor IoT, um job agendado, outro Service). Se a regra estiver no Controller, essas outras entradas a ignoram e gravam dados inválidos. No Service ela vale sempre. No projeto há duas camadas: o `@PositiveOrZero` no DTO barra o erro de formato logo na entrada da API, e o `TrechoRodoviaService.validar()` garante a regra independentemente de quem chamou.

**4. Onde está o SQL do `findByTipo()`? Quem o gerou?**
Não está em lugar nenhum do nosso código. O Spring Data lê o nome do método na inicialização, quebra em partes (`find` + `By` + `Tipo`), identifica o atributo `tipo` da entidade e monta uma consulta JPQL equivalente a `SELECT i FROM IntervencaoOperacional i WHERE i.tipo = ?1`. Na hora da execução, o Hibernate traduz essa JPQL para o SQL do Oracle (`SELECT ... FROM INTERVENCAO_OPERACIONAL WHERE TIPO_INTERVENCAO = ?`). Dá para ver o SQL gerado no console porque `spring.jpa.show-sql=true`.

## 10. Boas práticas de Git
- Commits incrementais por camada (entidades → repositories → services → controllers → tratamento de erros → testes → documentação).
- `.gitignore` ignora `target/`, arquivos de IDE e arquivos de credenciais.
- Nenhuma senha no repositório: credenciais só por variáveis de ambiente.
