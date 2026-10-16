# 💊 GerenciamentoFarmacia — Sistema de Gerenciamento de Estoque de Medicamentos

![Java](https://img.shields.io/badge/Java-21%2B-orange?style=flat&logo=openjdk)
![JavaFX](https://img.shields.io/badge/JavaFX-17-blue?style=flat&logo=java)
![Maven](https://img.shields.io/badge/Maven-Build-red?style=flat&logo=apachemaven)
![License](https://img.shields.io/badge/License-MIT-green?style=flat)

Aplicação desktop desenvolvida em **Java** e **JavaFX** para auxiliar no gerenciamento do estoque de medicamentos de uma farmácia. O sistema permite cadastrar produtos, controlar quantidades em estoque, registrar fornecedores, acompanhar datas de validade e gerar relatórios estratégicos para tomada de decisões.

---

## 📸 Interface do Sistema

<p center>
  <img src="docs/![](../../../Pictures/Screenshots/Captura de tela 2026-10-16 185248.png)interface.png" alt="Tela Principal do Sistema de Gerenciamento de Farmácia" width="850"/>
</p>

---

## 📌 Sumário
- [Sobre o projeto](#-sobre-o-projeto)
- [Funcionalidades](#-funcionalidades)
- [Módulo de Relatórios](#-módulo-de-relatórios)
- [Tecnologias Utilizadas](#-tecnologias-utilizadas)
- [Arquitetura e Organização do Código](#-arquitetura-e-organização-do-código)
- [Pré-requisitos](#-pré-requisitos)
- [Como Executar](#-como-executar)
- [Persistência de Dados](#-persistência-de-dados)
- [Possíveis Melhorias](#-possíveis-melhorias)
- [Autor](#-autor)

---

## 🎯 Sobre o Projeto

O **GerenciamentoFarmacia** visa solucionar desafios comuns no controle de inventários farmacêuticos, oferecendo uma interface centralizada e amigável. Com ele, o operador pode manter o cadastro de medicamentos sincronizado com as informações completas dos fornecedores, identificando rapidamente itens com baixo estoque ou com validade próxima do vencimento.

---

## ✨ Funcionalidades

### 💊 Gestão de Medicamentos
- **Cadastro Completo:** Registro de código alfanumérico, nome, descrição, princípio ativo, data de validade, quantidade, preço unitário e indicação de medicamento controlado.
- **Consulta por Código:** Pesquisa rápida de medicamentos cadastrados.
- **Listagem Dinâmica:** Exibição em tabela com atualização instantânea das informações.
- **Exclusão com Confirmação:** Remoção de registros selecionados sincronizada diretamente no armazenamento.

### 🏢 Gestão de Fornecedores
- Associação direta de cada medicamento ao seu fornecedor responsável.
- Armazenamento de CNPJ (com verificação dos dígitos verificadores), Razão Social, Telefone, E-mail, Cidade e Estado (UF com 2 letras).

### 📊 Módulo de Relatórios Gerenciais
- **Próximos ao Vencimento:** Filtra medicamentos com vencimento previsto para os próximos 30 dias.
- **Estoque Baixo:** Alerta para produtos com menos de 5 unidades em estoque.
- **Valor por Fornecedor:** Calcula o valor financeiro investido por fornecedor (Preço × Quantidade).
- **Filtro de Controlados:** Separação e análise de medicamentos sob controle especial.

---

## 🛠️ Tecnologias Utilizadas

| Tecnologia | Finalidade |
| :--- | :--- |
| **Java 11+** | Linguagem principal para regras de negócio e orientação a objetos. |
| **JavaFX 17** | Construção da interface gráfica (GUI). |
| **Apache Maven** | Gerenciamento de dependências e automação do build. |
| **CSV** | Persistência de dados leve e portátil sem dependência de banco externo. |
| **Git & GitHub** | Controle de versão e hospedagem de código. |

---

## 🏗️ Arquitetura e Organização do Código

O projeto adota o padrão de arquitetura em camadas (**MVC / Layered Architecture**), separando responsabilidades de forma clara:
