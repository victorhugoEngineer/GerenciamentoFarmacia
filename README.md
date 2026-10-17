# 💊 GerenciamentoFarmacia — Sistema de Gerenciamento de Estoque de Medicamentos

![Java](https://img.shields.io/badge/Java-21%2B-orange?style=flat&logo=openjdk)
![JavaFX](https://img.shields.io/badge/JavaFX-17-blue?style=flat&logo=java)
![Maven](https://img.shields.io/badge/Maven-Build-red?style=flat&logo=apachemaven)
![License](https://img.shields.io/badge/License-MIT-green?style=flat)

Aplicação desktop desenvolvida em **Java** e **JavaFX** para auxiliar no gerenciamento do estoque de medicamentos de uma farmácia. O sistema permite cadastrar produtos, controlar quantidades em estoque, registrar fornecedores, acompanhar datas de validade e gerar relatórios estratégicos para tomada de decisões.

---

## 📸 Interface do Sistema

<p align="center">
 <img width="1145" height="705" alt="Captura de tela 2026-10-16 185248" src="https://github.com/user-attachments/assets/4770300a-ce66-45dc-8539-7159981c28b8" />
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
- [Regras de Validação e Consistência](#-regras-de-validação-consistência)
- [Possíveis Melhorias](#-possíveis-melhorias)
- [Autor](#-autor)

---

## 🎯 Sobre o Projeto

O **GerenciamentoFarmacia** visa solucionar desafios comuns no controle de inventários farmacêuticos, oferecendo uma interface centralizada e amigável. Com ele, o operador pode manter o cadastro de medicamentos sincronizado com as informações completas dos fornecedores, identificando rapidamente itens com baixo estoque ou com validade próxima do vencimento.

---

## ✨ Funcionalidades

### 💊 Gestão de Medicamentos
- **Cadastro Completo:** Registro de código alfanumérico (3 a 10 caracteres), nome, descrição, princípio ativo, data de validade, quantidade, preço unitário e indicação de medicamento controlado.
- **Consulta por Código:** Pesquisa rápida e insensível a maiúsculas/minúsculas de medicamentos cadastrados.
- **Listagem Dinâmica:** Exibição em tabela (`TableView`) com ordenação automática por nome e atualização instantânea das informações.
- **Exclusão Sincronizada:** Remoção de registros selecionados na interface com reflexo imediato no arquivo de persistência.
- **Validação de Formulário:** Validação rigorosa de campos obrigatórios, datas de validade futuras, valores positivos e formatação correta de preços.

### 🏢 Gestão de Fornecedores
- Associação direta de cada medicamento ao seu fornecedor responsável.
- Armazenamento de CNPJ (com verificação dos dígitos verificadores via módulo 11), Razão Social, Telefone, E-mail, Cidade e Estado (sigla da UF com exatamente 2 letras).

---

## 📊 Módulo de Relatórios Gerenciais

- **Próximos ao Vencimento:** Filtra e lista medicamentos com data de vencimento prevista para os próximos 30 dias a partir da data atual.
- **Estoque Baixo:** Identifica e exibe alertas para produtos com quantidade em estoque abaixo do limite mínimo (ex.: menos de 5 unidades).
- **Valor por Fornecedor:** Agrupa e calcula o valor financeiro total investido no estoque segregado por fornecedor ($\text{Preço Unitário} \times \text{Quantidade}$).
- **Filtro de Controlados:** Isola e apresenta medicamentos sujeitos a controle especial para auditoria e prestação de contas.

---

## 🛠️ Tecnologias Utilizadas

| Tecnologia | Finalidade |
| :--- | :--- |
| **Java 21+** | Linguagem principal para regras de negócio e orientação a objetos. |
| **JavaFX 17** | Construção da interface gráfica do usuário (GUI). |
| **Apache Maven** | Gerenciamento de dependências e automação do build. |
| **CSV** | Persistência de dados leve e portátil sem dependência de banco externo. |
| **Git & GitHub** | Controle de versão e hospedagem de código. |

---

## 🏗️ Arquitetura e Organização do Código

O projeto adota o padrão de arquitetura em camadas (**MVC / Layered Architecture**), promovendo alta coesão e baixo acoplamento:

```text
com.example.gerenciamentofarmacia
├── controller/          # Controladores JavaFX (FarmaciaController, DialogoController)
├── model/               # Modelos do domínio (Medicamento, Fornecedor)
├── repository/          # Camada de acesso a dados e arquivo CSV (MedicamentoRepository)
├── service/             # Regras de negócio e relatórios (MedicamentoService, RelatorioService)
├── validation/          # Validações de domínio e exceções (Validador, ValidacaoException)
├── util/                # Utilitários de interface e caixas de diálogo (Dialogos)
├── FarmaciaApplication  # Classe principal de inicialização do JavaFX
└── module-info.java     # Configuração de módulos Java
```

### . Compilar e construir o projeto
Certifique-se de ter o Maven instalado e execute:
```bash
mvn clean compile
```

### . 🚀 Como Executar

Siga os passos abaixo para clonar, compilar e rodar o projeto em sua máquina local:

### . Clonar o repositório
Abra o seu terminal e execute os comandos:
```bash
git clone https://github.com/victorhugoEngineer/GerenciamentoFarmacia.git
cd GerenciamentoFarmacia
```

> 💡 **Alternativa:** Se preferir, você pode executar a classe `FarmaciaApplication.java` diretamente por meio da sua IDE de preferência (IntelliJ, Eclipse, VS Code, etc.).

---

## 💾 Persistência e Regras de Integridade de Dados

O sistema gerencia o armazenamento de informações de forma local e simplificada, seguindo diretrizes rígidas de padronização e persistência:

### ⚙️ Características do Arquivo
* **Armazenamento:** Os dados são persistidos localmente em um arquivo CSV estruturado no caminho `dados/medicamentos.csv`.
* **Formatação:** Utiliza-se o ponto e vírgula (`;`) como delimitador de campos, assegurando a compatibilidade com caracteres acentuados da língua portuguesa e a formatação correta de valores decimais.
* **Concorrência e Tolerância a Falhas:** As operações de escrita são sincronizadas (`synchronized`), contando com um mecanismo de *rollback* em memória para mitigar inconsistências em caso de falhas de I/O.
* **Sanitização de Strings:** O sistema realiza a higienização automática dos campos de texto para impedir quebras de linha acidentais ou conflitos com o caractere delimitador.

### 🛡️ Regras de Validação e Consistência (Campos Obrigatórios)
Para garantir a integridade do banco de dados em formato CSV, o sistema aplica as seguintes restrições no momento da inserção:

1. **Unicidade do Identificador:** O código do produto funciona como chave primária, sendo obrigatório que cada registro possua um código único e exclusivo.
2. **Preenchimento Manual do Validador:** O campo validador deve ser inserido estritamente de forma manual pelo usuário para evitar falhas de processamento e inconsistências lógicas no sistema.
3. **Formatação de CNPJ:** O cadastro exige a inserção do CNPJ seguindo estritamente a máscara padrão de formatação (ex: `00.000.000/0000-00`).
4. **Restrição de Localidade (UF):** O campo correspondente à Unidade Federativa (UF) aceita exclusivamente a sigla do estado composta por exatamente duas letras maiúsculas (ex: `SP`, `RJ`, `GO`).

---

## 🔮 Possíveis Melhorias

Futuras implementações e evoluções planejadas para o sistema:

- [ ] Implementação de autenticação e controle de acesso por perfis de usuário (Ex: Operador / Administrador).
- [ ] Integração com banco de dados relacional (ex.: PostgreSQL, MySQL ou SQLite via JPA/Hibernate).
- [ ] Geração e exportação de relatórios em formato PDF e Excel.
- [ ] Leitura de código de barras para dar baixa/entrada automatizada no estoque.

---

## 👨‍💻 Autor

Desenvolvido por **Victor Hugo Alves Vaz**.
