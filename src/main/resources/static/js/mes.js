const productLogTable = document.getElementById("product-log");
const tbody = productLogTable.querySelector("tbody");
const pageNumberSpan = document.getElementById("page-number");
const pageList = document.getElementById("page-list");
let currentPage = 0; // 현재 페이지
let totalPages = 0;
const itemsPerPage = 10;
let currentSearchQuery = "";

document.addEventListener("DOMContentLoaded", function () {
    currentSearchQuery = document.getElementById("search-input").value;
    fetch('/api/mes/totalProductLogCount')
        .then(response => response.json())
        .then(data => {
            const totalProductLogCount = data;
            totalPages = Math.ceil(totalProductLogCount / itemsPerPage);
            updatePagination();
        })
        .catch(error => {
            console.error('product Log count fetch error:', error);
        });
    fetchProductLog(currentPage);
    console.log("현재 페이지가 몇인데?", currentPage);
});

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

function calculateNumPagesToShow() {
    const minNumPagesToShow = Math.min(3, totalPages); // 동적으로 최소 페이지 수 계산
    const maxNumPagesToShow = 8;
    return Math.min(
        maxNumPagesToShow,
        Math.max(minNumPagesToShow, Math.ceil(totalPages / itemsPerPage))
    );
}


function getStartDate() {
    return document.getElementById("start-date").value || ""; // 값이 비어있을 경우 빈 문자열("")로 설정
}

function getEndDate() {
    return document.getElementById("end-date").value || ""; // 값이 비어있을 경우 빈 문자열("")로 설정
}

function getSearchQuery() {
    return document.getElementById("search-input").value || "";
}

function goToPage(page) {
    const startDate = getStartDate();
    const endDate = getEndDate();
    const searchQuery = getSearchQuery();
    fetchProductLogWithFilterAndSearch(startDate, endDate, searchQuery, page);
}

document.getElementById("filter-button").addEventListener("click", function () {
    const startDate = getStartDate();
    const endDate = getEndDate();
    const searchQuery = getSearchQuery();
    fetchProductLogWithFilterAndSearch(startDate, endDate, searchQuery, 0);
});


function fetchProductLogWithFilterAndSearch(startDate, endDate, searchQuery, page) {
    fetch(`/api/mes?search=${searchQuery}&page=${page}&startDate=${startDate}&endDate=${endDate}`)
        .then(response => response.json())
        .then(data => {
            tbody.innerHTML = ""; // 이전 목록을 지웁니다.
            updateProductLogList(data, page);
            totalPages = Math.ceil(data.page.totalElements / itemsPerPage); // totalElements로 totalPages 업데이트
            updatePagination();
        });
}

pageList.addEventListener("click", function (event) {
    if (event.target.classList.contains("page-list-item")) {
        const selectedPage = parseInt(event.target.textContent) - 1;
        goToPage(selectedPage);
    }
});

function formatDate(dateString) {
    const date = new Date(dateString);
    return date.toLocaleDateString("ko-KR");
}

function updateProductLogList(data, page) {
    tbody.innerHTML = "";

    data._embedded.productLogList.forEach(productLog => {
        const row = document.createElement('tr');
        const productId = document.createElement('td');
        productId.textContent = productLog.id;
        const workDate = document.createElement('td');
        workDate.textContent = formatDate(productLog.workDate);

        const productName = document.createElement('td');
        productName.textContent = productLog.productName;
        const productNumber = document.createElement('td');
        productNumber.textContent = productLog.productNumber;
        const workload = document.createElement('td');
        workload.textContent = productLog.workload;
        const workerName = document.createElement('td');
        workerName.textContent = productLog.workerName;
        row.appendChild(productId);
        row.appendChild(workDate);
        row.appendChild(productName);
        row.appendChild(productNumber);
        row.appendChild(workload);
        row.appendChild(workerName);
        tbody.appendChild(row);
    });
    currentPage = page;
}

function fetchProductLog(page) {
    fetch(`/api/mes?page=${page}`)
        .then(response => response.json())
        .then(data => {

            tbody.innerHTML = "";
            updateProductLogList(data, page);
            updatePagination();
            // 마지막 페이지인 경우에 대한 처리
            if (page === totalPages - 1) {
                document.getElementById("totalProductLogs").textContent = (page * itemsPerPage) + (data._embedded?.productLogList?.length || 0);
            } else {
                document.getElementById("totalProductLogs").textContent = Math.max(...data._embedded?.productLogList?.map(productLog => productLog.id) || [0]);
            }
        });
}


function openPopup() {
    const popupWindow = window.open("popUp", "Popup", "width=400,height=300");
}

// 팝업 창에서 제품 로그를 등록하는 이벤트 처리

// function submitProductLog(event) {
//     event.preventDefault();
//
//     // 폼 데이터 가져오기
//     const formData = new FormData(event.target);
//
// }
