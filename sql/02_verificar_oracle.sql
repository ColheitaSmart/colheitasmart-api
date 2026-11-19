SELECT USER AS esquema FROM dual;
SELECT table_name FROM user_tables
WHERE table_name IN ('TB_AGRICULTOR', 'TB_CULTURA', 'TB_LOTE')
ORDER BY table_name;
SELECT sequence_name FROM user_sequences
WHERE sequence_name IN ('SQ_TB_AGRICULTOR', 'SQ_TB_CULTURA', 'SQ_TB_LOTE')
ORDER BY sequence_name;
SELECT 'TB_AGRICULTOR' AS tabela, COUNT(*) AS registros FROM tb_agricultor
UNION ALL SELECT 'TB_CULTURA', COUNT(*) FROM tb_cultura
UNION ALL SELECT 'TB_LOTE', COUNT(*) FROM tb_lote;
