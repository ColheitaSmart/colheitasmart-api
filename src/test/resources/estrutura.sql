-- Proposta da API Java: validar com Andre antes de executar no Oracle do grupo.
-- Nao executar sobre tabelas existentes. Nao inclui DROP nem dados pessoais.
CREATE TABLE tb_agricultor (
 cd_agricultor NUMBER(19) PRIMARY KEY,
 nm_agricultor VARCHAR2(100 CHAR) NOT NULL,
 ds_email VARCHAR2(150 CHAR) NOT NULL UNIQUE,
 nr_telefone VARCHAR2(20 CHAR) NOT NULL,
 nm_cidade VARCHAR2(100 CHAR) NOT NULL
);
CREATE SEQUENCE sq_tb_agricultor START WITH 1 INCREMENT BY 1;
COMMENT ON TABLE tb_agricultor IS 'Pequeno agricultor responsavel pelos lotes';

CREATE TABLE tb_cultura (
 cd_cultura NUMBER(19) PRIMARY KEY,
 nm_cultura VARCHAR2(100 CHAR) NOT NULL,
 ds_categoria VARCHAR2(100 CHAR) NOT NULL
);
CREATE SEQUENCE sq_tb_cultura START WITH 1 INCREMENT BY 1;
COMMENT ON TABLE tb_cultura IS 'Cultura agricola de cada lote';

CREATE TABLE tb_lote (
 cd_lote NUMBER(19) PRIMARY KEY,
 cd_agricultor NUMBER(19) NOT NULL,
 cd_cultura NUMBER(19) NOT NULL,
 qt_peso_inicial_kg NUMBER(13,3) NOT NULL,
 qt_peso_disponivel_kg NUMBER(13,3) NOT NULL,
 dt_colheita DATE NOT NULL,
 ds_armazenamento VARCHAR2(200 CHAR) NOT NULL,
 vl_preco_kg NUMBER(12,2) NOT NULL,
 ds_status VARCHAR2(20 CHAR) NOT NULL,
 CONSTRAINT fk_lote_agricultor FOREIGN KEY(cd_agricultor) REFERENCES tb_agricultor(cd_agricultor),
 CONSTRAINT fk_lote_cultura FOREIGN KEY(cd_cultura) REFERENCES tb_cultura(cd_cultura),
 CONSTRAINT ck_lote_inicial CHECK(qt_peso_inicial_kg > 0),
 CONSTRAINT ck_lote_disponivel CHECK(qt_peso_disponivel_kg >= 0 AND qt_peso_disponivel_kg <= qt_peso_inicial_kg),
 CONSTRAINT ck_lote_preco CHECK(vl_preco_kg >= 0),
 CONSTRAINT ck_lote_status CHECK(ds_status IN ('DISPONIVEL', 'ESGOTADO', 'ENCERRADO'))
);
CREATE SEQUENCE sq_tb_lote START WITH 1 INCREMENT BY 1;
CREATE INDEX ix_lote_agricultor ON tb_lote(cd_agricultor);
CREATE INDEX ix_lote_cultura ON tb_lote(cd_cultura);
COMMENT ON TABLE tb_lote IS 'Lote colhido acompanhado pelo ColheitaSmart';
