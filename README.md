# Challenge - Monitoramento e Priorização de Vegetação em Rodovias

## Objetivo do Projeto

O objetivo deste projeto é desenvolver um sistema em Java capaz de simular o monitoramento de vegetação em trechos de rodovias, permitindo registrar o crescimento da vegetação e identificar trechos críticos para manutenção.

Além disso, o sistema associa equipes de manutenção aos trechos críticos e gera um relatório automático de priorização de intervenções.

---

# Funcionalidades

- Cadastro de trechos de rodovia
- Registro de crescimento da vegetação
- Identificação de trechos críticos
- Associação de equipes de manutenção
- Geração de relatório automático de prioridade
- Validação de dados
- Encapsulamento com getters e setters
- Testes manuais de funcionamento

---

# Tecnologias Utilizadas

- Java 17
- Visual Studio Code
- GitHub

---

# Conceitos de POO

## Classes e Objetos

A classe `TrechoRodovia` representa um trecho monitorado da rodovia. A partir dela, são criados objetos como:

- BR-116 KM 10 ao 15
- BR-101 KM 20 ao 25

---

## Métodos e Comportamentos

Foi implementado o método:

```java
registrarCrescimento(double taxa)
```

Responsável por atualizar o nível da vegetação conforme a taxa informada.

---

## Encapsulamento

O atributo `nivelVegetacao` é privado, garantindo controle sobre seus valores. Também há validação para impedir valores negativos.

---

## Interface (Monitoramento IoT)

Foi criada a interface:

```java
MonitoravelViaIoT
```

Com o método:

```java
transmitirDadosSensor();
```

A classe `TrechoRodovia` implementa essa interface, permitindo simular atualização automática via sensores.

---

## Classe Abstrata

Foi criada a classe abstrata:

```java
IntervencaoOperacional
```

Ela representa uma intervenção genérica em um trecho de rodovia e define o método abstrato:

```java
executarServico();
```

---

## Herança

As classes abaixo herdam de `IntervencaoOperacional`:

- RocadaMecanizada
- Pulverizacao

---

## Polimorfismo

O sistema utiliza polimorfismo ao definir dinamicamente a intervenção adequada para cada trecho:

```java
IntervencaoOperacional intervencao =
        trecho.definirIntervencao();
```

---

# Estrutura do Projeto

```
├── Main.java
├── TrechoRodovia.java
├── EquipeManutencao.java
├── MonitoravelViaIoT.java
├── IntervencaoOperacional.java
├── RocadaMecanizada.java
├── Pulverizacao.java
└── SensorMock.java
```

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
