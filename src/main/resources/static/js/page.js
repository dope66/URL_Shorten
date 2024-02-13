
function calculateNumPagesToShow() {
    const minNumPagesToShow = Math.min(3, totalPages); // 동적으로 최소 페이지 수 계산
    const maxNumPagesToShow = 8;
    return Math.min(
        maxNumPagesToShow,
        Math.max(minNumPagesToShow, Math.ceil(totalPages / itemsPerPage))
    );
}
function updatePagination() {
    pageNumberSpan.textContent = currentPage;
    pageList.innerHTML = ""; // 페이지 목록 초기화

    const numPagesToShow = calculateNumPagesToShow(); // 동적으로 페이지 수 계산
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
pageList.addEventListener("click", function (event) {
    if (event.target.classList.contains("page-list-item")) {
        const selectedPage = parseInt(event.target.textContent) - 1;
        goToPage(selectedPage);
    }
});
