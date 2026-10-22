DECLARE
    v_qtd    NUMBER;
    v_inicio NUMBER;
BEGIN
    -- 1) Remove o IDENTITY (só das tabelas que ainda têm)
    FOR t IN (SELECT table_name
                FROM user_tab_columns
               WHERE column_name = 'ID'
                 AND identity_column = 'YES'
                 AND table_name IN ('EQUIPE_MANUTENCAO', 'TRECHO_RODOVIA',
                                    'INTERVENCAO_OPERACIONAL', 'RELATORIO_PRIORIDADE')) LOOP
        EXECUTE IMMEDIATE 'ALTER TABLE ' || t.table_name || ' MODIFY ID DROP IDENTITY';
    END LOOP;

    -- 1b) Cria as sequences que ainda não existem, começando após o maior ID
    FOR s IN (SELECT 'EQUIPE_MANUTENCAO' AS tabela FROM dual
              UNION ALL SELECT 'TRECHO_RODOVIA' FROM dual
              UNION ALL SELECT 'INTERVENCAO_OPERACIONAL' FROM dual
              UNION ALL SELECT 'RELATORIO_PRIORIDADE' FROM dual) LOOP
        SELECT COUNT(*) INTO v_qtd FROM user_sequences WHERE sequence_name = 'SEQ_' || s.tabela;
        IF v_qtd = 0 THEN
            EXECUTE IMMEDIATE 'SELECT NVL(MAX(ID), 0) + 1 FROM ' || s.tabela INTO v_inicio;
            EXECUTE IMMEDIATE 'CREATE SEQUENCE SEQ_' || s.tabela
                           || ' START WITH ' || v_inicio || ' INCREMENT BY 1 NOCACHE';
        END IF;
    END LOOP;

    -- 2) Novas colunas em INTERVENCAO_OPERACIONAL
    SELECT COUNT(*) INTO v_qtd FROM user_tab_columns
     WHERE table_name = 'INTERVENCAO_OPERACIONAL' AND column_name = 'ID_EQUIPE';
    IF v_qtd = 0 THEN
        EXECUTE IMMEDIATE 'ALTER TABLE INTERVENCAO_OPERACIONAL ADD (ID_EQUIPE NUMBER)';
    END IF;

    SELECT COUNT(*) INTO v_qtd FROM user_tab_columns
     WHERE table_name = 'INTERVENCAO_OPERACIONAL' AND column_name = 'URGENTE';
    IF v_qtd = 0 THEN
        EXECUTE IMMEDIATE 'ALTER TABLE INTERVENCAO_OPERACIONAL ADD (URGENTE NUMBER(1) DEFAULT 0 NOT NULL)';
    END IF;

    -- 3) Padroniza os tipos gravados pela Sprint 3 (texto livre) para os códigos do JPA
    EXECUTE IMMEDIATE q'[UPDATE INTERVENCAO_OPERACIONAL SET URGENTE = 1
                          WHERE UPPER(TIPO_INTERVENCAO) LIKE '%URGENTE%']';
    EXECUTE IMMEDIATE q'[UPDATE INTERVENCAO_OPERACIONAL SET TIPO_INTERVENCAO = 'ROCADA_MECANIZADA'
                          WHERE UPPER(TIPO_INTERVENCAO) LIKE 'RO%']';
    EXECUTE IMMEDIATE q'[UPDATE INTERVENCAO_OPERACIONAL SET TIPO_INTERVENCAO = 'PULVERIZACAO'
                          WHERE UPPER(TIPO_INTERVENCAO) LIKE 'PULV%']';
    EXECUTE IMMEDIATE q'[UPDATE INTERVENCAO_OPERACIONAL SET TIPO_INTERVENCAO = 'SEM_INTERVENCAO'
                          WHERE TIPO_INTERVENCAO NOT IN ('ROCADA_MECANIZADA', 'PULVERIZACAO')]';

    -- Intervenções antigas não tinham equipe: atribui a primeira equipe cadastrada
    EXECUTE IMMEDIATE q'[UPDATE INTERVENCAO_OPERACIONAL
                            SET ID_EQUIPE = (SELECT MIN(ID) FROM EQUIPE_MANUTENCAO)
                          WHERE ID_EQUIPE IS NULL]';

    -- 4) Restrições (só cria as que ainda não existem)
    SELECT COUNT(*) INTO v_qtd FROM user_tab_columns
     WHERE table_name = 'INTERVENCAO_OPERACIONAL' AND column_name = 'ID_EQUIPE' AND nullable = 'Y';
    IF v_qtd = 1 THEN
        EXECUTE IMMEDIATE 'ALTER TABLE INTERVENCAO_OPERACIONAL MODIFY ID_EQUIPE NOT NULL';
    END IF;

    SELECT COUNT(*) INTO v_qtd FROM user_constraints WHERE constraint_name = 'FK_INTERVENCAO_EQUIPE';
    IF v_qtd = 0 THEN
        EXECUTE IMMEDIATE 'ALTER TABLE INTERVENCAO_OPERACIONAL ADD CONSTRAINT FK_INTERVENCAO_EQUIPE '
                       || 'FOREIGN KEY (ID_EQUIPE) REFERENCES EQUIPE_MANUTENCAO (ID)';
    END IF;

    SELECT COUNT(*) INTO v_qtd FROM user_constraints WHERE constraint_name = 'CK_TIPO_INTERVENCAO';
    IF v_qtd = 0 THEN
        EXECUTE IMMEDIATE q'[ALTER TABLE INTERVENCAO_OPERACIONAL ADD CONSTRAINT CK_TIPO_INTERVENCAO
                             CHECK (TIPO_INTERVENCAO IN ('SEM_INTERVENCAO', 'PULVERIZACAO', 'ROCADA_MECANIZADA'))]';
    END IF;

    SELECT COUNT(*) INTO v_qtd FROM user_constraints WHERE constraint_name = 'CK_MONITORADO_IOT';
    IF v_qtd = 0 THEN
        EXECUTE IMMEDIATE 'ALTER TABLE TRECHO_RODOVIA ADD CONSTRAINT CK_MONITORADO_IOT '
                       || 'CHECK (MONITORADO_IOT IN (0, 1))';
    END IF;

    COMMIT;
END;