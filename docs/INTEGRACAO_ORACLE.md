# Integração com Oracle da FIAP

O script da GS está em sql/ColheitaSmart_Oracle.sql. Os DAOs usam TB_AGRICULTOR, TB_CULTURA e TB_LOTE e suas sequences SQ_TB_*. A API também respeita as chaves estrangeiras das tabelas adicionais: um lote que tenha registros vinculados não pode ser excluído e a API responde 409.

1. No SQL Developer, conecte ao esquema em que o grupo já criou as tabelas. Execute sql/02_verificar_oracle.sql. Espere três tabelas, três sequences e as respectivas contagens. Se o banco já foi montado, preserve os registros existentes e use apenas a checagem.
2. No IntelliJ, abra Run > Edit Configurations e selecione ColheitaSmartApplication. Em Environment variables, configure SPRING_PROFILES_ACTIVE=oracle, DB_URL, DB_USERNAME e DB_PASSWORD. Use host, porta e nome do serviço que aparecem na conexão do SQL Developer. Para uma conexão com nome de serviço, o formato é jdbc:oracle:thin:@//HOST:PORTA/SERVICO. Mantenha a senha na configuração local.
3. Execute ColheitaSmartApplication. Faça GET http://localhost:8080/culturas e GET http://localhost:8080/lotes. Um HTTP 200 com os registros do Oracle confirma leitura pelo Java. A rota /status só confirma que o processo iniciou.
4. Abra docs/requisicoes.http para testar gravação. Use registros fictícios novos, anote os IDs retornados e remova apenas os registros criados no teste, excluindo primeiro o lote.

Os testes automatizados usam exclusivamente H2 e o arquivo src/test/resources/estrutura.sql. O script completo da GS foi feito para execução em esquema vazio no SQL Developer. Aplicação e testes não o executam automaticamente.
