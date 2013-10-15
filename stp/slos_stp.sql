DROP SEQUENCE SEQ_APP_NUMBER;

CREATE SEQUENCE SEQ_APP_NUMBER
START WITH 0
MAXVALUE 999999999999999999999999999
MINVALUE 0
NOCYCLE
NOCACHE
NOORDER;


CREATE OR REPLACE PACKAGE SLOS AS

  FUNCTION getApplicationNumber(segmentCode IN VARCHAR2) RETURN VARCHAR2;
  procedure resetSeq(p_seq_name in varchar2);

END slos;
/

CREATE OR REPLACE PACKAGE BODY SLOS AS

  FUNCTION getApplicationNumber(segmentCode IN VARCHAR2) RETURN VARCHAR2 IS
    appNumber VARCHAR2(5);
  BEGIN
    select TO_CHAR(SEQ_APP_NUMBER.nextval,'0000') into appNumber from dual;
    RETURN segmentCode || TO_CHAR(sysdate, 'yyyymmdd') || trim(appNumber);
  END;

  procedure resetSeq(p_seq_name in varchar2)
    is
        l_val number;
    begin
        execute immediate 'select ' || p_seq_name || '.nextval from dual' INTO l_val;
        execute immediate 'alter sequence ' || p_seq_name || ' increment by -' || l_val || ' minvalue 0';
        execute immediate 'select ' || p_seq_name || '.nextval from dual' INTO l_val;
        execute immediate 'alter sequence ' || p_seq_name || ' increment by 1 minvalue 0';
    end;

END slos;
/
