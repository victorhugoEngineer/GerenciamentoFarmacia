# 💊 Sistema de Gerenciamento de Farmácia (GerenciamentoFarmacia)

[![Java](https://img.shields.io/badge/Java-11%2B-orange.svg)](https://www.oracle.com/java/)
[![JavaFX](https://img.shields.io/badge/JavaFX-17-blue.svg)](https://openjfx.io/)
[![Build Tool](https://img.shields.io/badge/Maven-3.x-red.svg)](https://maven.apache.org/)
[![License](https://img.shields.io/badge/License-MIT--Style-green.svg)](#licença)

Uma aplicação desktop robusta desenvolvida em **Java** e **JavaFX**, destinada à gestão automatizada de estoque, controle de medicamentos e integração de dados de fornecedores no setor farmacêutico.

---

## 📌 Visão Geral

O **GerenciamentoFarmacia** oferece uma solução integrada para simplificar os processos operacionais de controle de estoque em drogarias e farmácias. A aplicação combina uma interface gráfica intuitiva com recursos avançados de auditoria e relatórios em tempo real, auxiliando na redução de perdas por validade e no controle rigoroso de medicamentos sujeitos a regulamentação especial.

---

## ✨ Funcionalidades Principais

- **Gestão de Medicamentos (CRUD Complete):**
  - Cadastro, exclusão, consulta detalhada e listagem de produtos.
  - Registro de atributos essenciais: código, nome comercial, descrição, princípio ativo, preço unitário e tipo (controlado/não controlado).
  
- **Gestão Integrada de Fornecedores:**
  - Associação direta entre o medicamento e a razão social, CNPJ (com formatação automática) e dados de contato do fornecedor.

- **Módulo de Análise e Relatórios (Java Streams API):**
  - **Próximos ao Vencimento:** Monitoramento preventivo de lote com alerta para produtos que vencem nos próximos 30 dias.
  - **Estoque Baixo:** Alerta automático para itens com estoque abaixo do limite de segurança (menos de 5 unidades).
  - **Valoração de Estoque por Fornecedor:** Cálculo dinâmico do valor monetário total armazenado.
  - **Segregação por Regulamentação:** Filtro analítico entre medicamentos de controle especial e de venda livre.

- **Persistência Segura e Tolerante a Falhas:**
  - Armazenamento em arquivo plano CSV com suporte a escrita atômica via arquivo temporário (evitando corrupção de dados em eventuais interrupções de execução).

---

## 🛠️ Tecnologias e Dependências

- **Linguagem:** Java 11+
- **Interface Gráfica (GUI):** JavaFX 17 (estruturado em FXML / CSS)
- **Gerenciador de Dependências e Build:** Apache Maven
- **Testes Unitários:** JUnit 5
- **Controle de Versão:** Git & GitHub

---

## 🏗️ Arquitetura do Sistema

A aplicação foi estruturada seguindo boas práticas de Orientação a Objetos e o padrão arquitetural **MVC (Model-View-Controller)** com camada de serviço desacoplada:

## 📋 Pré-requisitos

Antes de iniciar, certifique-se de ter os seguintes componentes configurados no seu ambiente:

- **Java Development Kit (JDK):** Versão 11 ou superior configurada na variável `JAVA_HOME`.
- **Git:** Para clonar o repositório.
- *(Opcional)* **Apache Maven:** Não obrigatório, pois o projeto inclui o **Maven Wrapper (`mvnw`)**.

---

## 🚀 Instruções de Execução

### 1. Clonar o Repositório
```bash
git clone [https://github.com/victorhugoEngineer/GerenciamentoFarmacia.git](https://github.com/victorhugoEngineer/GerenciamentoFarmacia.git)
cd GerenciamentoFarmacia
