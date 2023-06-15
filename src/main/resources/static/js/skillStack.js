$(document).ready(function () {
    // SkillStack 목록
    var jobTypes = [
        "게임개발", "기술지원", "데이터분석가", "데이터엔지니어", "백엔드/서버개발", "보안관제", "보안컨설팅", "앱개발", "웹개발",
        "웹마스터", "유지보수", "정보보안", "퍼블리셔", "프론트엔드", "CISO", "CPO", "DBA", "FAE", "GM(게임운영)",
        "ICT컨설팅", "IT컨설팅", "QA/테스터", "SE(시스템엔지니어)", "SI개발", "SQA"
    ];

    var fields = [
        "검색엔진", "네트워크", "데이터라벨링", "데이터마이닝", "데이터시각화", "딥러닝", "머신러닝", "메타버스", "모델링", "모의해킹", "미들웨어",
        "반응형웹", "방화벽", "블록체인", "빅데이터", "빌링", "솔루션", "스크립트", "신경망", "아키텍쳐", "악성코드", "알고리즘", "영상처리", "웹표준·웹접근성",
        "음성인식", "이미지프로세싱", "인터페이스", "인프라", "임베디드", "자율주행", "정보통신", "챗봇", "취약점진단", "컴퓨터비전", "크로스브라우징", "크롤링",
        "클라우드", "텍스트마이닝", "트러블슈팅", "펌웨어", "플러그인", "핀테크", "헬스케어", "AI(인공지능)", "API", "APM", "AR(증강현실)", "DBMS", "DevOps",
        "DLP", "DW", "ERP", "ETL", "FPGA", "GIS", "H/W", "IDC", "IIS", "IoT", "ISMS", "MCU", "Nginx", "NLP(자연어처리)", "NLU(자연어이해)",
        "OCR", "OLAP", "RDBMS", "RPA", "RTOS", "S/W", "SAP", "SDK", "SOA", "STT", "TTS", "UTM", "VDI", "VMware",
        "VoIP", "VPN", "VR(가상현실)", "WCF", "Windows"
    ];

    var techStack = [
        "그누보드", "라즈베리파이", "쉘스크립트", "스마트컨트랙트", "아두이노", "액션스크립트", "어셈블리", "와이어샤크", "임베디드리눅스", "파워빌더",
        "풀스택", ".NET", "ABAP", "AIX", "Ajax", "Android", "Angular", "Apache", "ArcGIS", "ASP", "ASP.NET", "AWS",
        "Azure", "Bootstrap", "C#", "C++", "CentOS", "COBOL", "CSS", "CSS3", "C언어", "Delphi", "Directx", "Django", "Docker", "Eclipse", "ECMAScript",
        "ElasticStack", "Flask", "FLEX", "Flutter", "GCP", "Git", "GoLang", "GraphQL", "Groovy", "Gulp", "Hadoop", "HBase",
        "HTML", "HTML5", "IaaS", "iBATIS", "Ionic", "iOS", "Java", "Javascript", "Jenkins", "JPA", "jQuery", "JSP", "Kafka", "Keras", "Kotlin", "Kubernetes", "LabVIEW",
        "Linux", "Logstash", "Lucene", "MacOS", "MariaDB", "Matlab", "Maven", "MFC", "MongoDB", "MSSQL", "MyBatis", "MySQL", "Node.js", "NoSQL", "Objective-C", "OpenCV", "OpenGL",
        "OracleDB", "OSS", "PaaS", "Pandas", "Perl", "PHP", "PL/SQL", "PostgreSQL", "Pro-C", "Python", "Pytorch", "QGIS",
        "Qt", "R", "React", "React-Native", "ReactJS", "Redis", "Redux", "RestAPI", "Ruby", "SaaS", "SAS", "Scala", "Servlet",
        "Solaris", "Solidity", "Spark", "Splunk", "Spring", "SpringBoot", "SQL", "SQLite", "Storm", "Struts", "SVN", "Swift", "Sybase", "Tensorflow",
        "Tomcat", "TypeScript", "Ubuntu", "Unity", "Unix", "Unreal", "VB.NET", "Verilog", "Vert.x", "VisualBasic", "VisualC·C++", "Vue.js", "WAS", "WebGL", "Webpack", "WebRTC", "WPF",
        "XML"
    ];

    var skillStackList = {
        JobType: jobTypes,
        Field: fields,
        TechStack: techStack
    };


    // 자동완성 옵션 생성
    var skillOptions = '';
    Object.values(skillStackList).forEach(function (skills) {
        skills.forEach(function (skill) {
            skillOptions += '<option value="' + skill + '">';
        });
    });
    $('#skill-list-datalist').html(skillOptions);


    // 기술 스택 추가 기능
    $('#add-skill-btn').click(function () {
        var skill = $('#skill-input').val();
        if (skill !== '') {
            $('#selected-skills-list').append('<li>' + skill + '</li>');
            $('#skill-input').val('');

            // 선택된 기술 스택을 UserDto 객체에 설정
            var skillStack = [];
            $('#selected-skills-list li').each(function () {
                skillStack.push($(this).text());
            });
            $('#selected-skills-input').val(skillStack.join(',')); // 쉼표로 구분하여 저장
        }
    });
    // SkillStack 제거 버튼 클릭 시
    $(document).on('click', '#selected-skills li', function () {
        $(this).remove();
    });
});