# Challenge - Monitoramento e Priorização de Vegetação em Rodovias

## Objetivo do Projeto

O objetivo deste projeto é desenvolver um sistema em Java capaz de simular o monitoramento de vegetação em trechos de rodovias, permitindo registrar o crescimento da vegetação e identificar trechos críticos para manutenção.

Além disso, o sistema associa equipes de manutenção aos trechos considerados críticos, simulando um cenário real de priorização de roçada em rodovias.

---

# Funcionalidades

- Cadastro de trechos de rodovia
- Registro de crescimento da vegetação
- Identificação de trechos críticos
- Associação de equipes de manutenção
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

Foi criada a classe `TrechoRodovia`, responsável por representar um trecho monitorado da rodovia.

Também foram criados objetos específicos representando trechos reais, como:
- BR-116 KM 10 ao 15
- BR-101 KM 20 ao 25

---

## Métodos e Comportamentos

Foi implementado o método:

```java
registrarCrescimento(double taxa)
```

Esse método permite atualizar o nível da vegetação de acordo com a taxa de crescimento informada.

---

## Encapsulamento

O atributo `nivelVegetacao` foi definido como privado, garantindo maior segurança e controle dos dados.

Também foi implementada uma validação para impedir valores negativos, garantindo a integridade do sistema.

---

# Estrutura das Classes

## Classe TrechoRodovia

Responsável por:
- armazenar os dados do trecho
- controlar o crescimento da vegetação
- identificar trechos críticos
- exibir informações

### Principais atributos

- nomeRodovia
- quilometroInicial
- quilometroFinal
- nivelVegetacao

### Principais métodos

- registrarCrescimento()
- trechoCritico()
- exibirInformacoes()

---

## Classe EquipeManutencao

Responsável por:
- representar equipes de manutenção
- associar equipes aos trechos críticos

---

# Como Executar o Projeto

## Compilar

```bash
javac src/*.java
```

## Executar

```bash
java -cp src Main
```

---

# Exemplo de Saída

```bash
===== TRECHO =====
Rodovia: BR-116
KM Inicial: 10.0
KM Final: 15.0
Vegetação: 17.0 cm
Status: Normal

===== TRECHO =====
Rodovia: BR-101
KM Inicial: 20.0
KM Final: 25.0
Vegetação: 38.0 cm
Status: CRÍTICO

===== EQUIPE ASSOCIADA =====
Equipe: Equipe Sul
Responsável: Carlos Silva
Trecho crítico: BR-101
```

---

# Testes Realizados

## Teste 1 - Crescimento da Vegetação

### Objetivo

Validar se o método `registrarCrescimento()` adiciona corretamente o crescimento ao nível atual da vegetação.

### Cenário

- Vegetação inicial: 10 cm
- Crescimento registrado: 5 cm

### Resultado Esperado

```bash
15 cm
```

### Resultado Obtido

```bash
15.0 cm
```

---

## Teste 2 - Validação de Valor Negativo

### Objetivo

Garantir que o sistema não permita valores negativos para o nível da vegetação.

### Cenário

Valor informado:
```bash
-5
```

### Resultado Esperado

```bash
0 cm
```

### Resultado Obtido

```bash
0.0 cm
```

---

# Evidência de Execução

<img width="893" height="503" alt="terminal_POO" src="https://github.com/user-attachments/assets/d43249c8-04ad-4887-9d36-2e1f9f2cb01c" />


---

# Perguntas de Reflexão

## Por que TrechoRodovia é uma classe e “BR-116 KM 10 ao 15” é um objeto?

A classe representa um modelo genérico contendo características e comportamentos comuns aos trechos de rodovia. Já “BR-116 KM 10 ao 15” representa uma instância específica criada a partir dessa classe, ou seja, um objeto.

---

## Como um método difere de uma função solta em programação estruturada?

O método pertence a um objeto e atua diretamente sobre seus atributos e comportamentos. Já uma função em programação estruturada normalmente é independente e não pertence a nenhum objeto específico.

---

## Se o nivelVegetacao fosse público, que tipo de problema poderia ocorrer?

Valores inválidos poderiam ser atribuídos diretamente ao atributo, como números negativos, comprometendo a lógica do sistema e causando falhas na identificação correta dos trechos críticos.

---

# Clean Code Aplicado

Durante o desenvolvimento do projeto foram aplicadas boas práticas de Clean Code, como:

- nomes claros para classes e métodos
- separação de responsabilidades
- organização do código
- métodos objetivos
- validação de dados
- código legível e de fácil manutenção
