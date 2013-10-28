CREATE OR REPLACE PACKAGE SELOS_WF AS

  Procedure SP_CASE_CREATE_STATUS(
    appNumber IN HIS_CASE_CREATION.APP_NUMBER%TYPE,
    caNumber IN WRK_CASE_PRESCREEN.CA_NUMBER%TYPE,
    createStatus IN HIS_CASE_CREATION.CREATE_STATUS%TYPE,
    userId IN WRK_CASE_PRESCREEN.CREATE_BY%TYPE,
    createDateTime IN WRK_CASE_PRESCREEN.CREATE_DATE%TYPE,
    wobNumber IN WRK_CASE_PRESCREEN.WOB_NUMBER%TYPE,
    status OUT MST_STATUS.DESCRIPTION%TYPE,
    statusCode OUT MST_STATUS.ID%TYPE
  );

  PROCEDURE SP_GET_BDM_PROFILE(
    p_userName IN MST_USER.USERNAME%TYPE,
    zoneId OUT MST_USER.ZONE_ID%TYPE,
    divisionId OUT MST_USER.DIVISION_ID%TYPE,
    departmentId OUT MST_USER.DEPARTMENT_ID%TYPE,
    regionId OUT MST_USER.REGION_ID%TYPE
  );

  PROCEDURE SP_GET_NEXT_STATUS(
    stepCode IN REL_STEP_ACTION.STEP_ID%TYPE,
    previousStatus IN REL_STEP_ACTION.CURRENT_STATUS_ID%TYPE,
    action IN MST_ACTION.DESCRIPTION%TYPE,
    appNumber IN WRK_CASE_PRESCREEN.APP_NUMBER%TYPE,
    newStatusCode OUT REL_STEP_ACTION.NEXT_STATUS_ID%TYPE,
    newStatus OUT MST_STATUS.DESCRIPTION%TYPE
  );


  PROCEDURE SP_UPDATE_CASE_STATUS_HISTORY(
    stepCode IN REL_STEP_ACTION.STEP_ID%TYPE,
    stepName IN HIS_CASE_HISTORY.STEP_NAME%TYPE,
    previousStatus IN REL_STEP_ACTION.CURRENT_STATUS_ID%TYPE,
    action IN MST_ACTION.DESCRIPTION%TYPE,
    appNumber IN WRK_CASE_PRESCREEN.APP_NUMBER%TYPE,
    userId IN HIS_CASE_HISTORY.USER_ID%TYPE,
    dateTime IN HIS_CASE_HISTORY.CREATE_DATE%TYPE,
    p_caNumber IN HIS_CASE_HISTORY.CA_NUMBER%TYPE,
    newStatusCode OUT REL_STEP_ACTION.NEXT_STATUS_ID%TYPE,
    newStatus OUT MST_STATUS.DESCRIPTION%TYPE
  );

END SELOS_WF;

COMMIT;

CREATE OR REPLACE PACKAGE BODY SELOS_WF AS

  Procedure SP_CASE_CREATE_STATUS(
    appNumber IN HIS_CASE_CREATION.APP_NUMBER%TYPE,
    caNumber IN WRK_CASE_PRESCREEN.CA_NUMBER%TYPE,
    createStatus IN HIS_CASE_CREATION.CREATE_STATUS%TYPE,
    userId IN WRK_CASE_PRESCREEN.CREATE_BY%TYPE,
    createDateTime IN WRK_CASE_PRESCREEN.CREATE_DATE%TYPE,
    wobNumber IN WRK_CASE_PRESCREEN.WOB_NUMBER%TYPE,
    status OUT MST_STATUS.DESCRIPTION%TYPE,
    statusCode OUT MST_STATUS.ID%TYPE
  ) IS

    BEGIN

      UPDATE HIS_CASE_CREATION SET CREATE_STATUS = createStatus where APP_NUMBER = appNumber;

      INSERT INTO WRK_CASE_PRESCREEN (ID,CREATE_DATE,APP_NUMBER,CA_NUMBER,WOB_NUMBER,CREATE_BY,STATUS_ID,CASE_LOCK) VALUES (SEQ_WRK_CASE_PRESCREEN_ID.nextval,createDateTime,appNumber,caNumber,wobNumber,userId,10001,0);

      SELECT
        NVL(DESCRIPTION,''),
        NVL(ID,0)
      INTO status,statusCode FROM MST_STATUS WHERE ID = 10001;

      INSERT INTO HIS_CASE_HISTORY (ID,STEP_NAME,USER_ID,ACTION,CREATE_DATE,APP_NUMBER,CA_NUMBER) VALUES (SEQ_HIS_CASE_ID.nextval,'CASE_CREATION','SYSTEM','CREATE CASE',createDateTime,appNumber,caNumber);

      COMMIT;

    END SP_CASE_CREATE_STATUS;

  PROCEDURE SP_GET_BDM_PROFILE(
    p_username IN MST_USER.USERNAME%TYPE,
    zoneId OUT MST_USER.ZONE_ID%TYPE,
    divisionId OUT MST_USER.DIVISION_ID%TYPE,
    departmentId OUT MST_USER.DEPARTMENT_ID%TYPE,
    regionId OUT MST_USER.REGION_ID%TYPE
  ) IS


    BEGIN


      SELECT
        NVL(ZONE_ID,0),
        NVL(DIVISION_ID,0),
        NVL(DEPARTMENT_ID,0),
        NVL(REGION_ID,0)
      into zoneId, divisionId, departmentId, regionId FROM MST_USER
      WHERE ID = p_username;

    END SP_GET_BDM_PROFILE;

  PROCEDURE SP_GET_NEXT_STATUS(
    stepCode IN REL_STEP_ACTION.STEP_ID%TYPE,
    previousStatus IN REL_STEP_ACTION.CURRENT_STATUS_ID%TYPE,
    action IN MST_ACTION.DESCRIPTION%TYPE,
    appNumber IN WRK_CASE_PRESCREEN.APP_NUMBER%TYPE,
    newStatusCode OUT REL_STEP_ACTION.NEXT_STATUS_ID%TYPE,
    newStatus OUT MST_STATUS.DESCRIPTION%TYPE
  ) IS
    actionId NUMBER;
    BEGIN

      SELECT NVL(ACTIONID,0) INTO actionId from MST_ACTION WHERE Name = ACTION;

      SELECT NVL(NEXT_STATUS_ID,0) into newStatusCode FROM REL_STEP_ACTION WHERE STEP_ID = stepCode AND CURRENT_STATUS_ID = previousStatus AND ACTION_ID = actionId;

      SELECT NVL(DESCRIPTION,'') INTO newStatus FROM MST_STATUS WHERE ID = newStatusCode;

      UPDATE WRK_CASE_PRESCREEN SET STATUS_ID = newStatusCode WHERE APP_NUMBER = appNumber;

    END SP_GET_NEXT_STATUS;

  PROCEDURE SP_UPDATE_CASE_STATUS_HISTORY(
    stepCode IN REL_STEP_ACTION.STEP_ID%TYPE,
    stepName IN HIS_CASE_HISTORY.STEP_NAME%TYPE,
    previousStatus IN REL_STEP_ACTION.CURRENT_STATUS_ID%TYPE,
    action IN MST_ACTION.DESCRIPTION%TYPE,
    appNumber IN WRK_CASE_PRESCREEN.APP_NUMBER%TYPE,
    userId IN HIS_CASE_HISTORY.USER_ID%TYPE,
    dateTime IN HIS_CASE_HISTORY.CREATE_DATE%TYPE,
    p_caNumber IN HIS_CASE_HISTORY.CA_NUMBER%TYPE,
    newStatusCode OUT REL_STEP_ACTION.NEXT_STATUS_ID%TYPE,
    newStatus OUT MST_STATUS.DESCRIPTION%TYPE
  ) IS

    p_actionId NUMBER;

    BEGIN

      SELECT NVL(ID,0) INTO p_actionId from MST_ACTION WHERE DESCRIPTION = action;

      SELECT NVL(NEXT_STATUS_ID,0) into newStatusCode FROM REL_STEP_ACTION WHERE STEP_ID = stepCode AND CURRENT_STATUS_ID = previousStatus AND ACTION_ID = p_actionId;

      SELECT NVL(DESCRIPTION,'') INTO newStatus FROM MST_STATUS WHERE ID = newStatusCode;

      UPDATE WRK_CASE_PRESCREEN SET STATUS_ID = newStatusCode WHERE APP_NUMBER = appNumber;

      INSERT INTO HIS_CASE_HISTORY (ID,STEP_ID,STEP_NAME,USER_ID,ACTION,CREATE_DATE,APP_NUMBER,CA_NUMBER) VALUES (SEQ_HIS_CASE_ID.nextval,stepCode,stepName,userId,action,dateTime,appNumber,p_caNumber);

    END SP_UPDATE_CASE_STATUS_HISTORY;

END SELOS_WF;

COMMIT;