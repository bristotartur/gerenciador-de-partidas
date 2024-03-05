# Gerenciador de Partidas

Aplicação em Spring Boot para o gerenciamento de partidas para a gincana do colégio CEDUP Abílo Paulo.

O objetivo deste projeto é fornecer um sistema de gerenciamento que facilite o controle de atividades esportivas relativas a gincana, bem como a disseminação de informações sobre a mesma,
como placares de jogos, datas e horários, pontuações das equipes e outros dados.

## Considerações sobre o sistema

O projeto se encontra em fase de desenvolvimento e muitas das funcionalidades previstas a ele ainda não foram implementadas.
Portanto, é necessário estar ciente de que o uso prévio desta aplicação apresenta poucos recursos e de que este não é o produto
final.

## Como rodar esta aplicação?

No momento a única forma de adquirir esta aplicação é clonando o repositório deste projeto, o que pode ser feito
executando o seguinte comando:

```shell
git clone https://github.com/bristotartur/gerenciador-de-partidas.git
```
O código é completamente escrito em *Java*, então certifique-se de possuir a JDK mais recente ou da versão 17 para frente.
Você pode adquirir a JDK através do [site oficial da Oracle](https://www.oracle.com/br/java/technologies/downloads/). 
Caso você tenha o Java instalado mas não sabe a versão, você pode consultá-la executando o seguinte comando em seu termianl:

```shell
java --version
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

## Utilizando a API

Para utilizar a API do *Gerenciador de Partidas* é recomendado o uso de softwares especializados em testes de APIs, como
o [Postman](https://www.postman.com/) ou o [Insomnia](https://insomnia.rest/). Você pode também utilizar o comando *curl*, que é uma ferramenta de linha de comando 
focada no consumo de APIs e que vem pré-instalado junto de alguns sistemas operacionais. Independente de qual opção escolher,
será pressuposto que você tenha conhecimento e saiba usar alguma destas ferramentas para consumir e testar esta API.

### Iniciando a gincana:

A primeira coisa que deve ser feita no sistema é a criação de uma *edição*. As *edições* são entidades que representam as
gincanas, sendo assim a entidade principal de todo o sistema, pois todas as demais entidades estão relacionadas de forma direta ou
indireta a elas. A URL geral para interações com *edições* é a seguinte:

```plaintext
http://localhost:8080/gerenciador-de-partidas/api/editions
```
Para criar uma nova *edição*, basta utilizar a URL acima com o método `POST` e inserir um corpo de requisição definindo a data
de início (*opening*) e encerramento (*closure*) da gincana:

```json
{
  "opening": "2024-04-21",
  "closure": "2024-05-04"
}
```
Você terá o seguinte corpo de resposta:

```json
{
  "editionId": 1,
  "atomica": 0,
  "mestres": 0,
  "papaLeguas": 0,
  "twister": 0,
  "unicontti": 0,
  "editionStatus": "SCHEDULED",
  "opening": "2024-04-21",
  "closure": "2024-05-04",
  "_links": {
    "editions": {
      "href": "http://localhost:8080/gerenciador-de-partidas/api/editions"
    },
    "sportEvents": {
      "href": "http://localhost:8080/gerenciador-de-partidas/api/sport-events/from?edition=1"
    }
  }
}
```
Repare que muitos dados presentes no corpo de resposta não estão presentes no corpo de requisição. Os campos referentes
as equipes da gincana informam sua pontuação, que por padrão é 0, e não podem ser alteradas diretamente. No momento, não
há nenhuma forma de atualizar estes campos, mas futuramente a pontuação das equipes será calculada automaticamente após o 
término de *eventos esportivos* e *tarefas*.

Os únicos campos que podem ser alterados manualmente em um *evento* são `editionStatus`, `opening` e `closure`. Por via de 
regra, o corpo de requisição utilizado na criação de novas entidades será sempre o mesmo utilizado para atualizá-las. Portanto
caso deseje alterar a data de início ou encerramento de uma edição, basta inserir o mesmo corpo apresentado anteriormente, 
modificando apenas os campos necessários:

```json
{
  "opening": "2024-04-11", 
  "closure": "2024-05-04"
}
```
A URL também é a mesma, adicionando apenas o ID da entidade ao final e trocando o método `POST` por `PUT`:

```plaintext
http://localhost:8080/gerenciador-de-partidas/api/editions/1
```
Repare que desta forma conseguimos alterar `opening` e `closure`, mas não `editionsStatus`. Ao utilizar esta API é necessário 
entender que certos dados podem ser alterados de forma direta e indireta. A alteração indireta é aquela realizada ao modificar
outras entidades que, ao serem manipuladas, causam impactos em entidades relacionadas. Já a alteração direta é aquela em que
a modificação é feita diretamente na entidade, e existem duas formas de realizá-la. A primeira é a manual, onde você altera 
a entidade por meio de um corpo de requisição, como mostrado acima. Já a segunda é realizada por meio de uma URL, que é o 
caso de `editionStatus`. Para atualizá-lo usaremos método `PUT` novamente e a URL será a mesma, porém informando ao final qual o novo
status da *edição*:

```plaintext
http://localhost:8080/gerenciador-de-partidas/api/editions/1/update?status=in-progress
```

Além das *edições*, as entidades relacionadas a *eventos esportivos*, *tarefas* e *partidas* também possuem um campo de *status*.
Os valores possíveis deste campo são `scheduled` (agendado), `in-progress` (em andamento), `ended` (encerrado) e `open-for-edits` 
(aberto a edições). O *status* padrão de todas essas entidades é "*agendado*" e, com exceção das edições, só é possível 
alterá-las diretamente neste status. Caso tente alterar diretamente uma entidade que não está mais agendada, você receberá 
uma exceção deste tipo:

```json
{
    "title": "UnprocessableEntityException.",
    "status": 422,
    "details": "Atributos de eventos esportivos só podem ser atualizados com o status 'SCHEDULED'.",
    "developerMessage": "com.bristotartur.gerenciadordepartidas.exceptions.UnprocessableEntityException",
    "timestamp": "2024-02-24T16:36:10.84352"
}
```
O status `in-progress` permite que algumas alterações indiretas, como gols em uma partida, possam ser realizadas e, em algumas
circunstâncias, apenas uma entidade de determinado tipo pode estar em andamento. Um exemplo disso são as entidades relacionadas 
a *partidas*, pois na maioria dos eventos apenas uma partida por vez pode estar em andamento. O status `ended` faz com que as entidades
não possam mais ser alteradas tanto de forma direta ou indireta. Por fim, o status `open-for-edits` tem como objetivo 
permitir a alteração de *edições*, *partidas* e *eventos* que já foram encerrados, mas no momento esta funcionalidade não está
disponível.

### Criando eventos:

Com nossa *edição* já definida, podemos começar a adicionar *eventos* a ela. Os *eventos* são as entidades responsáveis por
movimentar a gincana e eles podem ser dividos em dois grupos: os eventos *esportivos*, que são compostos por entidades relacionadas
a *partidas*, e as tarefas, que são divididas em tarefas *normais*, *culturais* e tarefas *cumpridas*. No momento apenas
os eventos esportivos estão disponíveis no sistema, então apenas eles serão abordados.

Para criar um novo *evento esportivo*, utilizaremos o seguinte corpo de requisição:

```json
{
    "type": "FUTSAL",
    "modality": "MASCULINE",
    "totalMatches": 6,
    "editionId": 1
}
```
A URL geral para interações com eventos esportivos é semelhante a de edições, substituindo apenas `/editions` por `/sport-events`.
Para inserir o corpo de requisição, utilize a URL com o método `POST`:

```url
http://localhost:8080/gerenciador-de-partidas/api/sport-events
```
O corpo de resposta será algo semelhante a isso:

```json
{
    "sportEventId": 1,
    "type": "FUTSAL",
    "modality": "MASCULINE",
    "firstPlace": "NONE",
    "secondPlace": "NONE",
    "thirdPlace": "NONE",
    "fourthPlace": "NONE",
    "fifthPlace": "NONE",
    "totalMatches": 6,
    "eventStatus": "SCHEDULED",
    "_links": {
        "sportEventList": {
            "href": "http://localhost:8080/gerenciador-de-partidas/api/sport-events"
        },
        "edition": {
            "href": "http://localhost:8080/gerenciador-de-partidas/api/editions/1"
        }
    }
}
```
Antes de prosseguirmos, vamos analizar a estrutura do evento. O campo `type` faz referência ao tipo de esporte do evento. 
As modalidades esportivas disponíveis no sistema são `futsal`, `handball`, `basketball`, `volleyball`, `table-tennis` e 
`chess`. Após o tipo, temos `modality`, que diz respeito a modalidade/categoria do evento esportivo, ou seja, se ele é masculino,
feminino ou misto, sendo que estas opções são representadas como `masculine`, `feminine` e `mixed`. Estes dois campos são 
cruciais, pois em uma edição só pode haver um evento esportivo para cada combinação destes dois atributos, isto é, só pode
haver um evento de futsal feminino, um evento de handebol masculino, um evento de vôlei misto, e assim por diante. Caso
haja a tentativa de criar dois eventos com o mesmo tipo e modalidade, a seguinte exceção será lançada:

```json
{
    "title": "ConflictException.",
    "status": 409,
    "details": "Evento esportivo de tipo 'FUTSAL' e modalidade 'MASCULINE' já existe na edição '1'.",
    "developerMessage": "com.bristotartur.gerenciadordepartidas.exceptions.ConflictException",
    "timestamp": "2024-02-24T18:38:25.8560947"
}
```
É importante ressaltar que apenas os eventos de futsal e handebol possuem suporte no momento. As demais modalidades receberão 
funcionalidades em breve.

Os próximos campos dizem respeito a colocação das equipes no evento, que por padrão possuem o valor `none`. Ainda não há um 
mecanismo para o cálculo da posição das equipes no evento, mas isto será adicionado em breve e o cálculo será realizado de forma
automática. Prosseguindo, temos o campo `totalMatches`, que diz respeito ao total de partidas permitidas em um evento. Geralmente os eventos
esportivos da gincana possuem por volta de 14 partidas, mas neste exemplo seram utilizada apenas 6 para facilitar. Por fim,
temos o campo `eventStatus`, que funciona da mesma forma que o *status* das edições, e para atualizá-lo, basta usar a seguinte 
URL com o método `PUT`:

```plaintext
http://localhost:8080/gerenciador-de-partidas/api/sport-events/1/update?status=in-progress
```
Um detalhe importante sobre os eventos é que eles só podem ser alterados enquanto sua *edição* estiver sob o status `scheduled`
ou `in-progress`, sendo que o status dos eventos podem ser alterados **apenas** enquanto sua *edição* estiver em andamento. Caso
tente-se operar sobre um evento que não pode ter seus campos alterados, você receberá uma exceção como essa:

```json
{
    "title": "UnprocessableEntityException.",
    "status": 422,
    "details": "Operações não podem ser realizadas em edições já encerradas.",
    "developerMessage": "com.bristotartur.gerenciadordepartidas.exceptions.UnprocessableEntityException",
    "timestamp": "2024-02-24T19:44:26.9374694"
}
```
ou, caso tente atualizar o status de um *evento* cuja edição não está em andamento:

```json
{
    "title": "UnprocessableEntityException.",
    "status": 422,
    "details": "Eventos só podem ter seu status atualizado caso sua edição estaja com o status 'IN_PROGRESS'.",
    "developerMessage": "com.bristotartur.gerenciadordepartidas.exceptions.UnprocessableEntityException",
    "timestamp": "2024-02-24T19:40:33.1745262"
}
```
Você deve ter reparado que tanto o corpo de resposta da *edição* quanto do evento possuem um campo chamado `_links` e que,
embora no corpo de requisição dos eventos seja especificado o ID de uma determinada *edição*, não existe um campo no corpo
de resposta contendo ela. Isso ocorre porque a API do sistema é feita sob as especificações da arquitetura REST. Uma das 
restrições desta arquitetura se chama HATEOAS, que é uma abreviatura para *Hypermedia as the Engine of Application State*, 
cujo objetivo é ajudar os clientes a consumirem o serviço sem a necessidade de conhecimento prévio profundo da API. Repare que, 
mesmo o corpo de resposta do evento não possuindo um campo para sua edição, ele possui um link para ela e vice-versa, o
que facilita a navegação pelos recursos da API. Isto é especialmente útil para entidades que possuem muitos relacionamentos,
como é o caso das *partidas*, que serão abordadas mais adiante.

### Adicionando participantes:

Antes de criar as *partidas* é necessário adicionar os *participantes* da gincana. Utilize a URL abaixo com o método `POST`
junto do corpo de requisição mostrado em seguida:

```plaintext
http://localhost:8080/gerenciador-de-partidas/api/participants
```
```json
{
  "name": "Carlos Eduardo",
  "classNumber": "3-53",
  "team": "PAPA_LEGUAS",
  "editionId": 1
}
```
Este será o corpo de resposta:

```json
{
  "participantId": 1,
  "name": "Carlos Eduardo",
  "classNumber": "3-53",
  "team": "PAPA_LEGUAS",
  "_links": {
    "participants": {
      "href": "http://localhost:8080/gerenciador-de-partidas/api/participants"
    },
    "teamMembers": {
      "href": "http://localhost:8080/gerenciador-de-partidas/api/participants/from?team=papa-leguas"
    },
    "edition": {
      "href": "http://localhost:8080/gerenciador-de-partidas/api/editions/1"
    },
    "matches": {
      "href": "http://localhost:8080/gerenciador-de-partidas/api/participants/1/matches"
    }
  }
}
```
Todo participante deve obrigatoriamente pertencer a uma equipe, sendo que cada equipe representa um ou mais cursos disponíveis 
no CEDUP. Segue abaixo uma tabela sobre as equipes existentes:

| Nome da equipe   | Representação no sistema                          | Cursos                                 | Mascote         |
|------------------|---------------------------------------------------|----------------------------------------|-----------------|
| Atômica          | `ATOMICA`, `atomica`                              | Alimentos, Química e Análises Clínicas | Formiga Atômica |
| Mestres de Obras | `MESTRES_DE_OBRAS`, `mestres-de-obras`, `mestres` | Edificações                            | Super Mario     |
| Papa-Léguas      | `PAPA_LEGUAS`, `papa-leguas`, `papa`              | Informática e Ciência de Dados         | Papa-Léguas     |
| Twister          | `TWISTER`, `twister`                              | Administração                          | Taz             |
| Unicontti        | `UNICONTTI`, `unicontti`                          | Comércio e Marketing                   | Tio Patinhas    |

Para prosseguir, escolha duas equipes e adicione 5 participantes em cada para começar a criar partidas. 
