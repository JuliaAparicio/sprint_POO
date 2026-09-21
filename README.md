# Sprint 3 — Sistema MOTIVA (persistência com Oracle + JDBC puro)

Este projeto evolui o sistema MOTIVA (Sprints 1 e 2) adicionando persistência
de dados em Oracle via JDBC puro, além de corrigir os pontos arquiteturais
apontados no feedback da Sprint 2.

## O que mudou em relação à Sprint 2

- **Correção arquitetural (IoT):** `MonitoravelViaIoT` não é mais implementada
  pela classe base `TrechoRodovia`. Foi criada a subclasse
  `TrechoMonitoradoIoT`, que é a única com capacidade de transmitir dados via
  sensor.
- **3 níveis de intervenção:** `SemIntervencao`, `Pulverizacao` e
  `RocadaMecanizada` (esta com variante "urgente").
- **Magic numbers extraídos:** todos os limiares e taxas de crescimento estão
  em `model/Constantes.java`.
- **Lógica de decisão separada:** a decisão de qual intervenção aplicar saiu
  de `TrechoRodovia` e foi para `service/IntervencaoService.java`.
- **Persistência (novo, Sprint 3):** pacotes `db/`, `dao/` e `service/` com
  `ConexaoBD`, quatro DAOs (padrão inserir/buscarPorId/listarTodas/atualizar/
  deletar) e `GeradorRelatorio`, que agora salva o histórico de relatórios no
  banco.

## Estrutura de pacotes

```
src/
├── model/     # TrechoRodovia, TrechoMonitoradoIoT, EquipeManutencao,
│              # IntervencaoOperacional e subclasses, Constantes
├── db/        # ConexaoBD (Singleton)
├── dao/       # EquipeManutencaoDAO, TrechoRodoviaDAO,
│              # IntervencaoOperacionalDAO, RelatorioPrioridadeDAO
├── service/   # IntervencaoService, GeradorRelatorio
└── main/      # Main (classe de demonstração)
sql/
├── script-criacao.sql
└── script-dados.sql
```

## Como rodar

1. **Configurar credenciais:** edite `src/db/ConexaoBD.java` e ajuste `URL`,
   `USUARIO` e `SENHA` para o Oracle do laboratório (host, porta e serviço).
2. **Criar o banco:** execute `sql/script-criacao.sql` e depois
   `sql/script-dados.sql` no Oracle (SQL*Plus, SQL Developer, etc.).
3. **Driver JDBC:** coloque `ojdbc17.jar` em `lib/` (não incluso neste pacote —
   baixe da Oracle ou copie do laboratório).
4. **Compilar:**
   ```
   javac -cp lib/ojdbc17.jar -d out $(find src -name "*.java")
   ```
5. **Executar:**
   ```
   java -cp out:lib/ojdbc17.jar main.Main      # Linux/Mac
   java -cp out;lib/ojdbc17.jar main.Main       # Windows
   ```

## Observações

- As tabelas usam `GENERATED ALWAYS AS IDENTITY` (Oracle 12c+) — os DAOs leem
  o ID gerado via `getGeneratedKeys()`.
- Os DAOs usam `try-with-resources` para `PreparedStatement`/`ResultSet`
  (fecha os recursos automaticamente, mesmo em caso de exceção) e todas as
  queries usam `PreparedStatement` com parâmetros (`?`), nunca concatenação de
  string.
- Se a conexão com o Oracle falhar, `GeradorRelatorio` continua imprimindo o
  relatório no console normalmente e apenas avisa no `stderr` que não
  conseguiu salvar no banco — a aplicação não quebra.
- Não foi possível compilar o projeto neste ambiente (sem acesso ao Oracle
  nem a um JDK completo), então revise a compilação no seu ambiente antes da
  entrega.
=======
---

# Sprint 1

## Objetivo

Implementar a estrutura inicial do sistema utilizando conceitos básicos de POO.

## Funcionalidades

- Cadastro de trechos
- Registro de crescimento da vegetação
- Identificação de trechos críticos
- Associação de equipes de manutenção
- Encapsulamento
- Validação de dados

---

# Sprint 2 – Motor de Regras

## Objetivo

Adicionar inteligência ao sistema com:

- Classes abstratas
- Interfaces
- Herança
- Polimorfismo

---

## Crescimento Diferenciado

- Terreno SECO: +5 cm
- Terreno ÚMIDO: +10 cm

---

## Relatório Automático

O sistema percorre um array de trechos e gera um relatório com:

- Rodovia
- KM inicial
- KM final
- Vegetação
- Tipo de intervenção

---

# Testes Realizados

- Crescimento da vegetação
- Validação de valores negativos
- Identificação de trechos críticos
- Execução de sensores IoT
- Teste de interface (Mock)
- Teste de classe abstrata (conceitual)
- Geração de relatório automático

---

# Exemplo de Saída

```
===== TRECHO =====
Rodovia: BR-116
Vegetação: 17.0 cm
Status: Normal

===== TRECHO =====
Rodovia: BR-101
Vegetação: 38.0 cm
Status: CRÍTICO

===== EQUIPE ASSOCIADA =====
Equipe: Equipe Sul
Responsável: Carlos Silva

===== RELATÓRIO DE PRIORIDADE =====
Rodovia: BR-116
Intervenção: Pulverização

Rodovia: BR-101
Intervenção: Roçada Mecanizada

===== TESTE MOCK =====
Mock enviando dados do sensor.
```

---

# Reflexões

## Por que não faz sentido executar apenas uma Intervenção Operacional genérica?

Porque `IntervencaoOperacional` é uma abstração. Na prática, é necessário executar ações específicas como Roçada Mecanizada ou Pulverização.

---

## Diferença entre classe abstrata e interface

- Classe abstrata: permite compartilhar estrutura e comportamento.
- Interface: define apenas um contrato de comportamento, sem implementação obrigatória.

---

# Como Executar

```bash
javac *.java
java Main
```

---

# Resultado Esperado

O sistema deve:

- Simular crescimento da vegetação
- Identificar trechos críticos
- Associar equipes
- Gerar relatório automático
- Executar testes de interface e abstração
