# Gerenciador de Partidas

Aplicação em Spring Boot para o gerenciamento de partidas para a gincana do colégio CEDUP Abílo Paulo.

O objetivo deste projeto é fornecer um sistema de gerenciamento que facilite o controle de atividades esportivas relativas a gincana, bem como a disseminação de informações sobre a mesma,
como placares de jogos, datas e horários, pontuações das equipes e outros dados.

## Como rodar esta aplicação?

No momento a única forma de adquirir esta aplicação é clonando o repositório deste projeto, o que pode ser feito
executando o seguinte comando:

```shell
git clone https://github.com/bristotartur/gerenciador-de-partidas.git
```
Além do código do projeto, será também necessário possuir o banco de dados *PostgreSQL* em sua máquina.
Caso você não o tenha, você pode instalá-lo pelo [site oficial do PostgreSQL](https://www.postgresql.org/download/) 
ou, se preferir, baixar a [imagem oficial do PostgreSQL](https://hub.docker.com/_/postgres) disponível no Docker Hub com o seguinte comando:

```shell
docker pull postgres
```
Ou, caso preferir, você pode utilizar o seguinte `docker-compose.yml` para executar um container de PostgreSQL:

```yaml
version: '3.8'

services:
  postgres:
    image: postgres
    container_name: postgres
    environment:
      POSTGRES_DB: gerenciador_de_partidas
      POSTGRES_PASSWORD: senha-do-banco
    ports:
      - "5432:5432"
    volumes:
      - ./postgres-data:/var/lib/postgresql/data
```

Repare que o `docker-compose.yml` já define um banco chamado `gerenciador_de_partidas`, processo que deve ser feito manualmente 
caso opte por rodar diretamente a imagem ou utilizar o PostgreSQL instalado localmente. 

Certifique-se de substituir `senha-do-banco` pelo valor que você desejar, e também garanta que haja uma pasta chamada `postgres-data` 
no mesmo diretório onde o arquivo `docker-compose.yml` for salvo para persistir os dados do container. 
Além disso, ajuste o arquivo `application.yml` dentro do diretório `src/main/resources/` para as suas configurações do banco.

```yaml
    platform: postgres
    driver-class-name: org.postgresql.Driver
    url: jdbc:postgresql://localhost:5432/gerenciador_de_partidas
    username: postgres
    password: senha-do-banco
```

Por fim, você pode executar o `docker-compose.yml`:

```shell
docker-compose up -d
```

Após todas as configurações do banco, você pode finalmente rodar o programa!

Caso deseje rodar a aplicação pela linha de comando, basta abrir o terminal de sua preferência no diretório do projeto e rodar o comando:

```shell
./mvnw spring-boot:run
```

ou

```shell
./mvnw.cmd spring-boot:run
```

Você também pode rodar direto de sua IDE, mas caso o faça, precisará antes configurá-la para suportar o *Lombok*, que é 
uma das dependências utilizadas no projeto. Em algumas IDEs, basta instalar o plugin do Lombok, que está disponível 
nos principais editores de texto, como Intellij e Vscode. Para alguns editores, será necessário realizar uma configuração 
mais específica para o suporte da dependência, o que pode ser conferido no [site oficial do Lombok](https://projectlombok.org/).

Caso a execução do programa ocorra corretamente, você verá os seguintes *logs* no terminal:

```log
2024-02-16T18:57:15.199-03:00  INFO 3468 --- [  restartedMain] o.s.b.d.a.OptionalLiveReloadServer       : LiveReload server is running on port 35729
2024-02-16T18:57:15.291-03:00  INFO 3468 --- [  restartedMain] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port 8080 (http) with context path ''
2024-02-16T18:57:15.308-03:00  INFO 3468 --- [  restartedMain] c.b.g.GerenciadorDePartidasApplication   : Started GerenciadorDePartidasApplication in 21.721 seconds (process running for 25.912)
```

Com a aplicação rodando, você pode agora começar a utilizar o programa!
