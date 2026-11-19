# ColheitaSmart API — etapa independente

Backend Spring Boot 3.5.16, Java 25, JDBC e Oracle.
CRUD de Agricultor, Cultura e Lote. Sem JPA/Hibernate/Spring Data.

## Abrir
No IntelliJ, abra o pom.xml como projeto Maven e escolha JDK 25.
Configure também Maven Runner e Importer para usar o JDK do projeto.
Não habilite recursos preview.

## Testar sem banco do grupo
Com Maven instalado: mvn test
No IntelliJ, janela Maven > Lifecycle > test (nao precisa de Maven no PATH).

Os testes usam H2 em memória (apenas teste), reiniciado a cada cenário.
O modo test não deve ser usado no Render e não é demonstração da persistência Oracle.
Não há perfil que finja dados Oracle em produção.

## Executar com Oracle (após alinhar com André)
1. Use sql/ColheitaSmart_Oracle.sql como referência oficial e siga docs/INTEGRACAO_ORACLE.md.
2. Se o Oracle já estiver preparado, execute somente sql/02_verificar_oracle.sql.
3. Configurar DB_URL, DB_USERNAME, DB_PASSWORD e CORS_ALLOWED_ORIGINS no ambiente
   da Run Configuration do IntelliJ. SPRING_PROFILES_ACTIVE=oracle.
4. Rodar ColheitaSmartApplication ou mvn spring-boot:run.
5. Consultar GET http://localhost:8080/status e GET /culturas.

.env.example é um exemplo: Spring Boot não importa .env automaticamente.
Nunca inserir credenciais em application.properties.

## Integração
docs/CONTRATO_API.md: campos, tipos, exemplos e status.
docs/requisicoes.http: sequência manual para IntelliJ HTTP Client ou adaptação ao Insomnia.
docs/DOCUMENTACAO_TECNICA.md: base da documentação final e diagramas.

## Publicação
Dockerfile multi-stage e render.yaml preparados.
No Render, definir as variáveis do .env.example e confirmar conexão ao Oracle.
CORS_ALLOWED_ORIGINS recebe origens reais separadas por vírgula (sem barra final).
Não foi efetuada publicação nem criado link de API.

## O que está incluído
- Models, DTOs de entrada/saída, Bean Validation e ModelMapper.
- DAOs JDBC com DataSource no construtor, PreparedStatement e fechamento de recursos.
- BOs, CRUD completo e GET/POST aninhados dos dois relacionamentos 1:N.
- Respostas 201/Location, 200, 204, 400, 404, 405, 409 e 500; 415 para mídia inválida.
- Tratamento global, regra de peso e CORS configurável.
- SQL proposto, Docker, testes e documentação preliminar.

## Dependências externas pendentes
- André: confirmar schema, tamanhos, constraints e sequences; testar Oracle FIAP.
- Front: consumir GET/POST reais e informar origem para CORS.
- Grupo: nomes, RM/turma, URL pública e vídeo integrado.
- Validação da imagem Docker e deploy Render exigem Docker/conta/variáveis.
- PDF final da documentação deve incorporar links e identificação após integração.
Vendas/perdas/monitoramentos e integração de IA não estão neste escopo prioritário.

## Uso de IA
ChatGPT/Codex apoiou implementação e revisão. Revisar e compreender antes da entrega.
