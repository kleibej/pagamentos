# Projeto Spring Boot com Java 17

Este projeto foi desenvolvido utilizando Java 17 em conjunto com Spring Boot 3.3, o que facilitou significativamente a configuração inicial. Através de poucos cliques, foi possível adicionar as dependências essenciais e gerar o projeto Spring.

## Banco de Dados

Para otimizar o tempo de desenvolvimento, optei por utilizar o banco de dados relacional em memória H2. Esta escolha permite um desenvolvimento ágil, sem a necessidade de configurar um banco de dados externo.

## Arquitetura do Projeto

A arquitetura do projeto foi estruturada com uma separação entre os módulos principais, visando a manutenção e escalabilidade do código:

* Domain: Contém as classes de domínio, incluindo entities, services e repositories. Estas classes são fundamentais para a implementação das regras de negócio.
* Rest: Responsável pela comunicação com o cliente. Este módulo inclui os controllers, que recebem as requisições HTTP e retornam as respostas apropriadas, além dos DTOs (Data Transfer Objects), que encapsulam os dados relevantes para as requisições, garantindo segurança ao omitir atributos sensíveis.

![image](https://github.com/user-attachments/assets/5bdc241f-f110-41d1-bcfd-44019d43caa6)

## Implementação das Transações

Como a descrição do desafio não especificava regras para autorizar ou estornar transações, implementei a lógica de pagamento e estorno com dados definidos manualmente, utilizando o próprio JSON fornecido. Considerei o JSON imutável, pois na descrição não foi falado nada sobre.
