# RiffReview — API de avaliações musicais

Para o guia detalhado de alterações, arquitetura, fluxos, configuração, exemplos e endpoints, consulte [DOCUMENTACAO_PROJETO.md](DOCUMENTACAO_PROJETO.md). Para as decisões técnicas, consulte também [DOCUMENTACAO_TECNICA.md](DOCUMENTACAO_TECNICA.md).

Backend REST para pesquisar itens musicais, publicar avaliações e votar se uma avaliação é útil. A API permite consultas públicas; cadastro, publicação, alteração, exclusão e votos exigem autenticação.

## Tecnologias e requisitos

- Java 17 e Maven 3.6 ou superior;
- Spring Boot 3.3.5, Spring Web, Spring Data JPA, Spring Security e Bean Validation;
- Lombok para gerar os construtores de dependências do módulo de álbuns;
- PostgreSQL para execução normal; H2 para testes;
- JWT HS256 para autenticação e Springdoc OpenAPI para explorar a API.

## Banco de dados e execução

Crie um banco PostgreSQL vazio chamado `avaliacao_musical` e configure as variáveis de ambiente abaixo, se os valores locais não forem adequados:

| Variável | Padrão local | Uso |
| --- | --- | --- |
| `DB_URL` | `jdbc:postgresql://localhost:5432/avaliacao_musical` | URL JDBC |
| `DB_USERNAME` | `postgres` | Usuário do banco |
| `DB_PASSWORD` | `postgres` | Senha do banco |
| `JWT_SECRET` | segredo de desenvolvimento | Segredo HS256 com ao menos 32 bytes |
| `JWT_EXPIRATION_MS` | `86400000` | Validade do token em milissegundos |

Os padrões facilitam a execução local e não são adequados para produção. Defina um segredo aleatório próprio fora do código antes de publicar a aplicação. O Hibernate atualiza o esquema (`ddl-auto=update`); em produção, prefira migrações versionadas.

Na raiz do projeto, execute `mvn spring-boot:run`. A API ficará em `http://localhost:8080`. Para validar o código, execute `mvn test`.

## Arquitetura

O código está em `src/main/java/br/com/ifba` e segue o padrão de pacotes do projeto anterior:

- `album`, `artista`, `banda` e `musica`: cada domínio tem `IController`, controller, entity, repository, `IService` e service;
- `avaliacao`: `AvaliacaoIController`/`AvaliacaoController` e `AvaliacaoIService`/`AvaliacaoService`, além de DTOs, entidades e repositórios de avaliação/voto;
- `catalogo`: `CatalogoIController`/`CatalogoController` e `CatalogoIService`/`CatalogoService` para operações agregadas;
- `usuario`: `UsuarioIController`/`UsuarioController` e `UsuarioIService`/`UsuarioService` para cadastro e login;
- `infrastructure/{config,entity,repository}`: segurança compartilhada e entidade/repositório-base de item musical;
- `MusicalReviewsApplication.java`: classe principal no pacote raiz, para o Spring localizar os domínios e a infraestrutura.

Cada controller recebe HTTP e delega ao service do próprio domínio; services usam repositories para acessar entidades persistidas. Um controller não chama outro controller. As entidades musicais estendem `ItemMusical` e usam herança JPA `SINGLE_TABLE`; cada tipo tem seu repositório e serviço próprios.

`Usuario` tem várias avaliações e votos. Cada `Avaliacao` pertence a um único autor e referencia um `ItemMusical`. `Album`, `Artista`, `Banda` e `Musica` especializam essa entidade-base, identificadas pela coluna discriminadora `tipo`. `Voto` liga usuário e avaliação; uma restrição única no banco impede duplicidade.

## Endpoints

As respostas de erro de validação usam HTTP 400; recurso inexistente, 404; falta de autenticação, 401; tentativa de alterar recurso de outra pessoa ou voto duplicado, 403 ou 409, respectivamente. Datas são ISO-8601 em UTC.

| Método e rota | Autenticação | Descrição e respostas |
| --- | --- | --- |
| `POST /api/auth/cadastro` | Não | JSON `{ "nome", "email", "senha" }`; cria conta (201) e nunca retorna senha/hash. |
| `POST /api/auth/login` | Não | JSON `{ "email", "senha" }`; retorna `{ "token", "tipo":"Bearer" }` (200), credenciais inválidas (401). |
| `GET /api/itens?tipo=&q=` | Não | Pesquisa catálogo por tipo opcional (`ARTISTA`, `BANDA`, `ALBUM`, `MUSICA`) e trecho do nome (200). |
| `GET /api/bandas?q=` | Não | Pesquisa itens do tipo banda (200). |
| `GET /api/artistas?q=` | Não | Pesquisa itens do tipo artista (200). |
| `GET /api/albuns?q=` | Não | Pesquisa itens do tipo álbum (200). |
| `GET /api/musicas?q=` | Não | Pesquisa itens do tipo música (200). |
| `POST /api/itens` | Sim | Cria item para compor o catálogo; JSON `{ "tipo", "nome", "artista", "descricao" }` (201). |
| `GET /albums` ou `GET /albums/findall` | Não | Lista álbuns (200); `/api/albuns` permanece como alias legado. |
| `GET /albums/findbyid/{id}` | Não | Busca álbum por ID (200/404). |
| `GET /albums/findbyname/{nome}` | Não | Pesquisa álbuns pelo nome (200). |
| `POST /albums/save` | Sim | Cria álbum com nome, artista e descrição (201). |
| `PUT /albums/update/{id}` | Sim | Atualiza álbum (200/404). |
| `DELETE /albums/delete/{id}` | Sim | Exclui álbum (204/404). |
| `GET /api/artistas/findall` | Não | Lista ou filtra artistas (também mantém `GET /api/artistas?q=`). |
| `POST /api/artistas/save` | Sim | Cadastra artista (201). |
| `PUT /api/artistas/update/{id}` | Sim | Atualiza artista (200/404). |
| `DELETE /api/artistas/delete/{id}` | Sim | Exclui artista (204/404). |
| `GET /api/artistas/findbyid/{id}` | Não | Busca artista (200/404). |
| `GET /api/artistas/findbyname/{nome}` | Não | Pesquisa artista por nome (200). |
| `GET /api/bandas/findall` | Não | Lista ou filtra bandas (também mantém `GET /api/bandas?q=`). |
| `POST /api/bandas/save` | Sim | Cadastra banda (201). |
| `PUT /api/bandas/update/{id}` | Sim | Atualiza banda (200/404). |
| `DELETE /api/bandas/delete/{id}` | Sim | Exclui banda (204/404). |
| `GET /api/bandas/findbyid/{id}` | Não | Busca banda (200/404). |
| `GET /api/bandas/findbyname/{nome}` | Não | Pesquisa banda por nome (200). |
| `GET /api/musicas/findall` | Não | Lista ou filtra músicas (também mantém `GET /api/musicas?q=`). |
| `POST /api/musicas/save` | Sim | Cadastra música (201). |
| `PUT /api/musicas/update/{id}` | Sim | Atualiza música (200/404). |
| `DELETE /api/musicas/delete/{id}` | Sim | Exclui música (204/404). |
| `GET /api/musicas/findbyid/{id}` | Não | Busca música (200/404). |
| `GET /api/musicas/findbyname/{nome}` | Não | Pesquisa música por nome (200). |
| `GET /api/catalogos/findall?tipo=&q=` | Não | Consulta agregada do catálogo; alias legado `/api/itens`. |
| `POST /api/catalogos/save` | Sim | Cria item musical agregado; alias legado `/api/itens`. |
| `POST /api/auth/cadastro` | Não | Cadastra usuário (201). Rota mantida porque é a rota pública da configuração de segurança. |
| `POST /api/auth/login` | Não | Autentica usuário e retorna token (200). |
| `GET /api/avaliacoes?itemId=` | Não | Lista avaliações mais recentes; filtro `itemId` opcional (200). |
| `GET /api/avaliacoes/{id}` | Não | Busca uma avaliação (200/404). |
| `POST /api/avaliacoes` | Sim | JSON `{ "texto", "itemId" }`; publica usando a identidade do token (201). |
| `PUT /api/avaliacoes/{id}` | Sim, autor | JSON `{ "texto" }`; atualiza (200), 403 se não for o autor. |
| `DELETE /api/avaliacoes/{id}` | Sim, autor | Exclui avaliação e votos associados (204), 403 se não for o autor. |
| `POST /api/avaliacoes/{id}/votos` | Sim | JSON `{ "util": true }` ou `false`; registra voto (201), duplicidade (409). |
| `DELETE /api/avaliacoes/{id}/votos` | Sim | Remove o próprio voto (204/404). |

Rotas protegidas recebem `Authorization: Bearer <token>`. Avaliação de exemplo: `POST /api/avaliacoes` com `{"texto":"Um disco marcante.","itemId":1}`. A resposta inclui autor, item e totais `votosUteis` e `votosNaoUteis`.

## Testes e documentação interativa

Os testes de integração usam banco H2 isolado e MockMvc. Eles percorrem cadastro e login, consultas de catálogo, criação/consulta/edição/exclusão de avaliação, votos, dados inválidos e permissões. Importe `postman/RiffReview.postman_collection.json` no Postman; configure a conta e execute o login para preencher a variável de token.

Com a aplicação ativa, abra `http://localhost:8080/swagger-ui.html` para a documentação interativa ou `http://localhost:8080/v3/api-docs` para OpenAPI JSON.
