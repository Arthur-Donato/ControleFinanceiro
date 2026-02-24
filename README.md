# :rocket: Sistema para controle financeiro

![Badge de Status](https://img.shields.io/badge/status-em%20desenvolvimento-yellow)

## 🚧Atenção Projeto em Desenvolvimento 🚧
Este projeto ainda está em fase de desenvolvimento. Muitas funcionalidades podem apresentar alguns bugs, podem sofrer mudanças drásticas nas suas implementações. **Não é recomendado para uso em produção**

## 📝 Descrição
Essa é uma API RESTful para um sistema de controle financeiro pessoal, possibilitando os usuários criarem transações com o objetivo de terem um maior controle de suas vidas financeiras. Essa API RESTful se destina principalmente para as pessoas quem querem ter um maior controle de suas vidas financeiras, resolvendo o principal problema de controlar todos os gastos e ganhos.

## ✨ Principais funcionalidades
* [✅] Registro de usuários
* [✅] Registro de categorias
* [✅] Registro de transações
* [✅] Edição e exclusão de usuários, categorias e transações
* [✅] Testes de unidade
* [✅]  Interface gráfica para complementar a API
* [⏳ ] Dashboard com gráficos
* [ ] Relatórios mensais

## 🛠️ Tecnologias utilizadas

**Backend:** Java 21, Spring Boot, Spring Security, Spring Data JPA / Hibernate;\
**Banco de dados:** PostgreSQL;\
**Frontend:** 🚧Ainda em desenvolvimento 🚧;\
**Outros:** Lombok, Maven.\

## 🚀 Como executar o projeto
🚧 Lembrando que o projeto ainda está em desenvolvimento, mas caso queira executar o projeto fique a vontade 🚧

1. Clone o repositório:
   ```bash
    git clone [URL_DO_SEU_REPOSITÓRIO]
    ```
2. Acesse o diretório do projeto:
   ```Bash
   cd NOME_DO_PROJETO
   ```
3. **Configuração do backend:**
   * Certifique-se de que tenha instalado o Java 21;
   * Configure seu banco de dados no arquivo "application.properties" presente na pasta do projeto;
     
4. Executando o projeto (Via IDE):
   * Abra o projeto na sua IDE de preferência (Ex: IntelliJ IDEA, VS Code, Eclipse);
   * Aguarde a IDE baixar e configurar todas as dependencias;
   * Encontre a classe principal (RastreadorDeFinancasApplication.java)
   * Clique com o botão direito e selecione Run ou Debug

5. Testando a API:
   * O servidor vai estar rodando em http://localhost:8081;
   * Para testar todos os endpoints do projeto utilize a ferramente Postman ou alguma de sua preferencia.

## 🛣️ Próximos passos (Roadmap)

* [ ] Implementar uma interface gráfica.
* [ ] Criar uma funcionalidade para meta de gastos.
* [ ] Implementar módulo de autenticação.
