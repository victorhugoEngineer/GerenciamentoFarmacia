💊 Gerenciamento de Farmácia

Sistema desenvolvido em Java para auxiliar no gerenciamento do estoque de medicamentos de uma farmácia, com foco na organização e no controle dos produtos.

📋 Sobre o projeto

O GerenciamentoFarmacia é uma aplicação desktop desenvolvida com JavaFX, voltada para o gerenciamento de medicamentos em um ambiente farmacêutico.

O projeto utiliza Maven para gerenciar dependências e automatizar tarefas de compilação, além de JavaFX para o desenvolvimento da interface gráfica.

🎯 Objetivos
Facilitar a organização do estoque de medicamentos.
Apoiar o gerenciamento de produtos farmacêuticos.
Desenvolver uma aplicação desktop utilizando Java.
Aplicar conceitos de programação orientada a objetos e desenvolvimento de interfaces gráficas.
🛠️ Tecnologias utilizadas
Java 11 — linguagem de programação.
JavaFX 17 — desenvolvimento da interface gráfica.
FXML — estruturação de interfaces.
Apache Maven — gerenciamento de dependências e compilação.
JUnit 5 — suporte à criação de testes automatizados.
Git e GitHub — versionamento e hospedagem do código-fonte.
⚙️ Pré-requisitos

Antes de executar o projeto, certifique-se de ter instalado:

JDK 11 ou compatível
Apache Maven (opcional, caso utilize o Maven Wrapper incluído no projeto).
Git
🚀 Como executar o projeto
1. Clone o repositório
git clone https://github.com/victorhugoEngineer/GerenciamentoFarmacia.git

2. Acesse a pasta do projeto
cd GerenciamentoFarmacia

3. Execute a aplicação

No Linux ou macOS:

./mvnw clean javafx:run


No Windows:

mvnw.cmd clean javafx:run


Caso tenha o Maven instalado globalmente, você também pode executar:

mvn clean javafx:run


O Maven irá compilar o projeto, resolver as dependências e iniciar a aplicação JavaFX.

📁 Estrutura do projeto
GerenciamentoFarmacia/
├── .mvn/
│   └── wrapper/
├── src/
│   └── main/
├── pom.xml
├── mvnw
├── mvnw.cmd
└── README.md

src/main/ — código-fonte principal da aplicação.
.mvn/wrapper/ — arquivos de configuração do Maven Wrapper.
pom.xml — dependências e configurações de compilação.
mvnw — script para execução do Maven em Linux e macOS.
mvnw.cmd — script para execução do Maven no Windows.
🧪 Testes

O projeto possui dependências do JUnit 5 para a implementação de testes automatizados.

Para executar os testes configurados:

./mvnw test


No Windows:

mvnw.cmd test

🔮 Possíveis melhorias futuras
Implementação de cadastro, edição e exclusão de medicamentos.
Controle de quantidades disponíveis em estoque.
Alertas para medicamentos próximos do vencimento.
Pesquisa e filtragem de produtos.
Relatórios de estoque.
Persistência de dados em banco de dados.
Aprimoramento da interface gráfica.

As funcionalidades acima representam sugestões de evolução e não necessariamente estão implementadas na versão atual.

👨‍💻 Autor

Victor Hugo Engineer

GitHub: @victorhugoEngineer
Repositório: GerenciamentoFarmacia
📄 Licença

Este projeto está disponível para fins de estudo e desenvolvimento. Consulte as informações de licenciamento do repositório antes de utilizá-lo ou redistribuí-lo.

⭐ Se este projeto foi útil para você, considere deixar uma estrela no repositório!
