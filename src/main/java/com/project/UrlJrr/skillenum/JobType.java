package com.project.UrlJrr.skillenum;

public enum JobType {
    게임개발(1), 기술지원(1), 데이터분석가(1), 데이터엔지니어(1), 백엔드_서버개발(1),
    보안관제(1), 보안컨설팅(1), 앱개발(1), 웹개발(1), 웹마스터(1),
    유지보수(1), 정보보안(1), 퍼블리셔(1), 프론트엔드(1), CISO(1), CPO(1),
    DBA(1), FAE(1), GM_게임운영(1), ICT컨설팅(1), IT컨설팅(1), QA_테스터(1),
    SE_시스템엔지니어(1), SI개발(1), SQA(1);

    private final int score;

    JobType(int score) {
        this.score = score;
    }

    public int getScore() {
        return score;
    }
}
