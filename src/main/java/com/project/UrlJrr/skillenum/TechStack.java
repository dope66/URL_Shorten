package com.project.UrlJrr.skillenum;

public enum TechStack {
    그누보드(3), 라즈베리파이(3), 쉘스크립트(3), 스마트컨트랙트(3), 아두이노(3), 액션스크립트(3),
    어셈블리(3), 와이어샤크(3), 임베디드리눅스(3), 파워빌더(3), 풀스택(3), NET(3),
    ABAP(3), AIX(3), Ajax(3), Android(3), Angular(3), Apache(3), ArcGIS(3),
    ASP(3), ASP_NET(3), AWS(3), Azure(3), Bootstrap(3), C_Sharp(3,"C#"), C_Plus_Plus(3,"C++"),
    CentOS(3), COBOL(3), CSS(3), CSS3(3), C언어(3), Delphi(3), Directx(3),
    Django(3), Docker(3), Eclipse(3), ECMAScript(3), ElasticStack(3), Flask(3),
    FLEX(3), Flutter(3), GCP(3), Git(3), GoLang(3), GraphQL(3), Groovy(3), Gulp(3),
    Hadoop(3), HBase(3), HTML(3), HTML5(3), IaaS(3), iBATIS(3), Ionic(3),
    iOS(3), Java(3), Javascript(3), Jenkins(3), JPA(3), jQuery(3), JSP(3),
    Kafka(3), Keras(3), Kotlin(3), Kubernetes(3), LabVIEW(3), Linux(3), Logstash(3),
    Lucene(3), MacOS(3), MariaDB(3), Matlab(3), Maven(3), MFC(3), MongoDB(3),
    MSSQL(3), MyBatis(3), MySQL(3), Node_js(3,"Node.js"), NoSQL(3), Objective_C(3,"Objective-C"), OpenCV(3),
    OpenGL(3), OracleDB(3), OSS(3), PaaS(3), Pandas(3), Perl(3), PHP(3), PL_SQL(3,"PL/SQL"),
    PostgreSQL(3),Pro_C(3,"Pro-C"), Python(3), Pytorch(3), QGIS(3), Qt(3), R(3),
    React(3), React_Native(3,"React-Native"), ReactJS(3), Redis(3), Redux(3), RestAPI(3), Ruby(3), SaaS(3),
    SAS(3), Scala(3), Servlet(3), Solaris(3), Solidity(3), Spark(3), Splunk(3), Spring(3),
    SpringBoot(3), SQL(3), SQLite(3), Storm(3), Struts(3), SVN(3), Swift(3), Sybase(3),
    Tensorflow(3), Tomcat(3), TypeScript(3), Ubuntu(3), Unity(3), Unix(3),
    Unreal(3), VB_NET(3,"VB.NET"), Verilog(3),Vert_x(3,"Vert.x"), VisualBasic(3), VisualC_C_plus_plus(3,"VisualC·C++"),Vue_js(3,"Vue.js"), WAS(3),
    WebGL(3), Webpack(3), WebRTC(3), WPF(3), XML(3);

    private final int score;
    private final String skill;

    TechStack(int score) {
        this(score, null);
    }

    TechStack(int score, String skill) {
        this.score = score;
        this.skill = skill;
    }

    public int getScore() {
        return score;
    }

    public String getSkillName() {
        if (skill == null) {
            return name();
        }
        return skill;
    }
}

