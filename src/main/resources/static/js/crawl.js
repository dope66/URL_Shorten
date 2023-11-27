
    const scrapTable = document.getElementById("zero_config");
    const tbody = scrapTable.querySelector("tbody");
    const pageNumberSpan = document.getElementById("page-number");
    const pageList = document.getElementById("page-list");
    let currentPage = 0; // 현재 페이지 번호
    let totalPages = 0; // Scrap의 총 페이지 수
    const itemsPerPage = 10; // 페이지당 아이템 수


    document.addEventListener("DOMContentLoaded", function () {
    // Scrap의 총 갯수를 가져오는 API 호출
    fetch('/api/scrap/totalScrapCount')
        .then(response => response.json())
        .then(data => {
            const totalScrapCount = data; // Scrap의 총 갯수
            totalPages = Math.ceil(totalScrapCount / itemsPerPage); // 페이지 수를 동적으로 설정
            // 나머지 코드와 함께 페이지 번호 업데이트
            updatePagination();
        })
        .catch(error => {
            console.error('Scrap count fetch error:', error);
        });

    // 초기 페이지 로딩
    fetchScraps(currentPage);
});
    function goToPage(page) {
    fetchScraps(page);
    currentPage = page;
    updatePagination();
}

    function updatePagination() {
    pageNumberSpan.textContent = currentPage + 1;
    pageList.innerHTML = ""; // 페이지 목록 초기화

    // 페이지 목록을 생성하고 현재 페이지 주변의 페이지 번호를 추가
    const numPagesToShow = 10; // 한 번에 표시할 페이지 수
    const startPage = Math.max(0, currentPage - Math.floor(numPagesToShow / 2));
    const endPage = Math.min(totalPages - 1, startPage + numPagesToShow - 1);

    for (let i = startPage; i <= endPage; i++) {
    const pageItem = document.createElement("span");
    pageItem.textContent = i + 1;
    pageItem.classList.add("page-list-item"); // 클래스 추가
    if (i === currentPage) {
    pageItem.classList.add("current-page");
}
    pageItem.addEventListener("click", () => {
    goToPage(i);
});
    pageList.appendChild(pageItem);
}

}
    // 게시물 목록 불러오기
    function fetchScraps(page) {
    fetch(`/api/scrap?page=${page}`)
        .then(response => response.json())
        .then(data => {
            tbody.innerHTML = ""; // 이전 목록을 지웁니다.

            data._embedded.scrapList.forEach(scrap => {
                const row = document.createElement('tr');

                const scrapId = document.createElement('td');
                scrapId.textContent = scrap.id;

                const scrapTitle = document.createElement('td');
                // scrapTitle.textContent = scrap.articleText;
                const maxTitleLength = 25; // 표시할 최대 길이
                scrapTitle.textContent = scrap.articleText.length > maxTitleLength
                    ? scrap.articleText.slice(0, maxTitleLength) + "..." // 긴 제목은 자르고 "..." 추가
                    : scrap.articleText; // 짧은 제목은 그대로 표시

                const scrapMatching = document.createElement('td');
                const link = document.createElement('a');
                link.href = `/matching/${scrap.id}`;
                link.textContent = "바로가기";
                link.target = "_blank";
                scrapMatching.appendChild(link);

                const scrapCompany = document.createElement('td');
                scrapCompany.textContent = scrap.company;

                const scrapSent = document.createElement('td');
                scrapSent.textContent = scrap.sent ? "발송완료" : "발송대기";


                const scrapSite = document.createElement('td');
                scrapSite.textContent = scrap.sourceSite;


                const scrapDeadLine = document.createElement('td');
                scrapDeadLine.textContent = scrap.deadline;



                row.appendChild(scrapId);
                row.appendChild(scrapTitle);
                row.appendChild(scrapMatching);
                row.appendChild(scrapCompany);
                row.appendChild(scrapSent);
                row.appendChild(scrapSite);
                row.appendChild(scrapDeadLine);
                tbody.appendChild(row);
            });

            currentPage = page;
        });
}
