
-- ColheitaSmart
-- Migracao 03: preparar autenticacao dos agricultores
-- Executar uma unica vez no esquema Oracle da API.

ALTER TABLE TB_AGRICULTOR
ADD (
    DS_SENHA_HASH VARCHAR2(100 CHAR)
);

COMMENT ON COLUMN TB_AGRICULTOR.DS_SENHA_HASH
IS 'Hash BCrypt da senha do agricultor. Nunca armazenar senha em texto puro.';

-- Conferir a nova coluna
SELECT
    COLUMN_NAME,
    DATA_TYPE,
    DATA_LENGTH,
    NULLABLE
FROM USER_TAB_COLUMNS
WHERE TABLE_NAME = 'TB_AGRICULTOR'
  AND COLUMN_NAME = 'DS_SENHA_HASH';
