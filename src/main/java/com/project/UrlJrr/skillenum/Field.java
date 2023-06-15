package com.project.UrlJrr.skillenum;

public enum Field {
    검색엔진(2), 네트워크(2), 데이터라벨링(2), 데이터마이닝(2), 데이터시각화(2), 딥러닝(2), 머신러닝(2), 메타버스(2),
    모델링(2), 모의해킹(2), 미들웨어(2), 반응형웹(2), 방화벽(2), 블록체인(2), 빅데이터(2), 빌링(2), 솔루션(2), 스크립트(2), 신경망(2), 아키텍쳐(2),
    악성코드(2), 알고리즘(2), 영상처리(2), 웹표준_웹접근성(2), 음성인식(2), 이미지프로세싱(2), 인터페이스(2), 인프라(2), 임베디드(2),
    자율주행(2), 정보통신(2), 챗봇(2), 취약점진단(2), 컴퓨터비전(2), 크로스브라우징(2), 크롤링(2), 클라우드(2),
    텍스트마이닝(2), 트러블슈팅(2), 펌웨어(2), 플러그인(2), 핀테크(2), 헬스케어(2),AI_인공지능(2,"AI(인공지능)"), API(2), APM(2),AR_증강현실(2,"AI(증강현실)"),
    DBMS(2), DevOps(2), DLP(2), DW(2), ERP(2), ETL(2), FPGA(2), GIS(2), H_W(2), IDC(2), IIS(2),
    IoT(2), ISMS(2), MCU(2), Nginx(2),NLP_자연어처리(2,"NLP(자연어처리)"), NLU_자연어이해(2,"NLP(자연어이해)"), OCR(2), OLAP(2), RDBMS(2), RPA(2), RTOS(2),
    S_W(2), SAP(2), SDK(2), SOA(2), STT(2), TTS(2), UTM(2), VDI(2), VMware(2), VoIP(2), VPN(2), VR_가상현실(2,"VR(가상현실)"), WCF(2), Windows(2);

    private final int score;
    private final String skill;

    Field(int score) {
        this(score, null);
    }

    Field(int score, String skill) {
        this.score = score;
        this.skill = skill;
    }

    public int getScore() {
        return score;
    }

    public String getSkillName() {
        if (skill == null) {
            return name(); // 기존의 이름을 반환합니다.
        }
        return skill;
    }
}
