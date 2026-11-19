# Estado de validação
- Revisão estática: campos do plano, DTOs, rotas, SQL e camadas conferidos.
- Compilação parcial: 20 classes principais compilaram com javac do JDK 25 e bibliotecas locais. Não substitui o build Maven completo; BOs/controllers de domínio/ModelMapper ainda precisam desse build.
- JDK configurado: Java 25 no pom e nas imagens Docker.
- Testes automatizados escritos: 12 cenários de integração com MockMvc e H2.
- Execução dos testes: BLOQUEADA neste ambiente. Maven não conseguiu resolver
  repo.maven.apache.org (DNS). Não existe resultado de aprovação.
- Não foram testados: CRUD no Oracle FIAP, chaves geradas com ojdbc11,
  integração React, build Docker e publicação no Render.
- Não foi criado link público nem gravado vídeo.
- Para validar localmente: importar Maven no IntelliJ com internet,
  configurar JDK 25 e executar Lifecycle > test na janela Maven.
- Depois executar Lifecycle > package.
- Confirmar com André o SQL antes de configurar Oracle.
- Este pacote é uma implementação para revisão e validação, não entrega final homologada.
