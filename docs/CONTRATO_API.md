# Contrato da API — ColheitaSmart

Java 25 / Spring Boot 3.5.16 / HTTP + JSON. Contrato preparado antes da integração.
Base local: http://localhost:8080. URL pública ainda não existe.

## Endpoints
| Método | Rota | Sucesso |
|---|---|---|
| POST | /agricultores | 201 + Location |
| GET | /agricultores | 200, lista (pode ser vazia) |
| GET | /agricultores/{id} | 200 |
| PUT | /agricultores/{id} | 200 |
| DELETE | /agricultores/{id} | 204, sem corpo |
| POST | /culturas | 201 + Location |
| GET | /culturas | 200, lista (pode ser vazia) |
| GET | /culturas/{id} | 200 |
| PUT | /culturas/{id} | 200 |
| DELETE | /culturas/{id} | 204, sem corpo |
| POST | /lotes | 201 + Location |
| GET | /lotes | 200, lista (pode ser vazia) |
| GET | /lotes/{id} | 200 |
| PUT | /lotes/{id} | 200 |
| DELETE | /lotes/{id} | 204, sem corpo |
| GET | /agricultores/{id}/lotes | 200 |
| POST | /agricultores/{id}/lotes | 201 + Location |
| GET | /culturas/{id}/lotes | 200 |
| POST | /culturas/{id}/lotes | 201 + Location |
| GET | /status | 200, disponibilidade do processo (não verifica banco) |

Buscar/alterar/remover ID inexistente: 404.
Entrada inválida: 400. Duplicidade, exclusão com filhos ou regra violada: 409.
Método não permitido: 405 + Allow. Formato não suportado: 415.
Erro inesperado: 500 com mensagem genérica.

Os POSTs aninhados usam o mesmo JSON de /lotes; o ID do vínculo no JSON
deve coincidir com o da URL. Location aponta para /lotes/{id}.
Atualização e exclusão do lote usam PUT/DELETE /lotes/{id}.

## JSON Agricultor (POST e PUT)
```json
{"nmAgricultor":"Ana","dsEmail":"ana@example.com","nrTelefone":"11999999999","nmCidade":"Sao Paulo"}
```
Resposta: os mesmos campos e cdAgricultor (inteiro).
Nome/cidade: até 100 caracteres; e-mail válido até 150; telefone texto até 20.
Todos obrigatórios. E-mail único (sensível a maiúsculas no SQL proposto).

## JSON Cultura (POST e PUT)
```json
{"nmCultura":"Tomate","dsCategoria":"Hortalica"}
```
Resposta: mesmos campos e cdCultura (inteiro). Ambos os textos obrigatórios, até 100.

## JSON Lote (POST e PUT)
```json
{"cdAgricultor":1,"cdCultura":1,"qtPesoInicialKg":200,"qtPesoDisponivelKg":200,"dtColheita":"2026-11-01","dsArmazenamento":"Galpao ventilado","vlPrecoKg":4.50,"dsStatus":"DISPONIVEL"}
```
Resposta: mesmos campos e cdLote (inteiro).
IDs: inteiros positivos e existentes. Data: YYYY-MM-DD, hoje ou passado.
Pesos: números decimais, até 10 dígitos inteiros e 3 decimais.
Peso inicial maior que zero; disponível >= zero e <= inicial.
Preço: >= zero, até 10 dígitos inteiros e 2 decimais.
Armazenamento: obrigatório, até 200 caracteres.
Status: DISPONIVEL, ESGOTADO ou ENCERRADO (obrigatório).
Status é informado pelo cliente nesta versão, sem cálculo automático.
Todos os campos são obrigatórios; PUT substitui todos os campos editáveis.
Não envie o código do próprio registro na entrada. Campos desconhecidos retornam 400.

## Erro
```json
{"instante":"2026-11-11T00:00:00Z","status":400,"mensagem":"Confira os campos informados.","caminho":"/agricultores","campos":{"nmAgricultor":"must not be blank"}}
```
As mensagens individuais das constraints dependem do idioma da JVM.
Use status/campos para tratar o erro na interface. SQL e stack trace não são enviados.

## Front-end
Configure CORS_ALLOWED_ORIGINS na API com as origens exatas, separadas por vírgula.
Exemplo: http://localhost:5173,https://seu-front.vercel.app
Não há autenticação nesta entrega; não usar dados pessoais reais no ambiente demonstrativo.

```ts
const baseUrl = import.meta.env.VITE_API_URL;
const resposta = await fetch(baseUrl + "/lotes");
if (!resposta.ok) throw new Error("Não foi possível consultar os lotes.");
const lotes = await resposta.json();
```

A interface deve tratar carregamento, falha e lista vazia.
O endpoint de IA é independente e não está integrado.
Vendas, perdas e monitoramento estão fora deste CRUD prioritário.
