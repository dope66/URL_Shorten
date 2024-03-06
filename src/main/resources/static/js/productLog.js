function openPopup() {
    const popupWindow = window.open("popUp", "Popup", "width=700,height=900");
}
let currentPage = 0;
let originalData = []; // 원본 데이터 배열
let hot;
document.addEventListener("DOMContentLoaded", function () {
    // 페이지 로드 시 실행되는 코드
    fetchProductLog(currentPage); // 초기 페이지 데이터 로딩
});
function fetchProductLog(page) {
    fetch(`/api/mes?page=${page}`)
        .then(response => response.json())
        .then(data => {
            originalData = data._embedded.productLogList; // 원본 데이터 업데이트
            createHandsontable(originalData); // Handsontable 인스턴스 생성 및 데이터 적용
            console.log("originalDataPagination", originalData);
        });
}

function fetchTestList() {
    fetch(`/api/worker/testList`)
        .then(response => response.json())
        .then(data => {
            originalData = data;
            createHandsontable(originalData);
        });
}
function formatDate(dateString) {
    const date = new Date(dateString);
    return date.toLocaleDateString("ko-KR");
}
function createHandsontable(data) {
    const container = document.getElementById('product-log');
    hot  =new Handsontable(container, {
        cells: function(row, col, prop) {
            var cellProperties = {};
            cellProperties.className = 'htCenter';
            return cellProperties;
        },
        className: "htCenter",
        colWidths: [100, 100, 180, 200, 100, 80], // 각 열의 너비를 픽셀 단위로 설정
        rowHeights: 30, // 모든 행의 높이를 픽셀 단위로 설정
        licenseKey: 'non-commercial-and-evaluation',
        data: data.map(productLog => [
            productLog.productionType,
            formatDate(productLog.workDate),
            productLog.productionNumber,
            productLog.productionName,
            productLog.production,
            productLog.workerName

        ]),
        colHeaders: [ '차종', '작업날짜', '품번', '품명', '생산량', '생산자'],
        columns: [
            {data: 0, readOnly: true, className: "htCenter"},
            {data: 1, readOnly: true , className: "htCenter"},
            {data: 2, readOnly: true, className: "htCenter"},
            {data: 3, readOnly: true, className: "htCenter"},
            {data: 4, readOnly: true, className: "htCenter"},
            {data: 5, readOnly: true, className: "htCenter"}
        ],
        columnSorting: true, // 정렬 활성화
        contextMenu: false, // 우클릭 메뉴 활성화
        manualRowMove: true, // 행 이동 활성화
        manualColumnMove: true, // 열 이동 활성화
        filters: true, // 필터링 활성화
        dropdownMenu: false, // 드롭다운 메뉴 활성화
        rowHeaders: true, // 행 번호 표시
        search: true, // 검색 기능 활성화
        // 기타 필요한 Handsontable 옵션 추가
    });

    console.log("hot : ", hot);
}
function performSearch() {
    if (!hot || !hot.getPlugin('search')) {
        console.error('Handsontable 인스턴스가 초기화되지 않았거나, 검색 플러그인을 사용할 수 없습니다.');
        return;
    }

    const query = document.getElementById('search-input').value.trim();
    const startDate = document.getElementById('start-date').value;
    const endDate = document.getElementById('end-date').value;

    // 원본 데이터에서 날짜 조건에 맞는 데이터를 필터링
    let dateFilteredData = originalData.filter(item => {
        const itemDate = new Date(item.workDate);
        // 시작 날짜 조건
        const startCondition = startDate ? new Date(startDate) <= itemDate : true;

        // 종료 날짜 조건 - 당일 포함 (endDate의 23:59:59까지 포함)
        let endCondition = true;
        if (endDate) {
            let endDateObj = new Date(endDate);
            endDateObj.setHours(23, 59, 59); // 종료 날짜를 당일의 마지막 시각으로 설정
            endCondition = itemDate <= endDateObj;
        }

        return startCondition && endCondition;
    });

    let filteredData = [];
    if (query) {
        // 임시로 날짜 필터링된 데이터를 Handsontable에 로드하여 검색 수행
        hot.loadData(dateFilteredData.map(item => [
            item.productionType,
            formatDate(item.workDate),
            item.productionNumber,
            item.productionName,
            item.production,
            item.workerName
        ]));

        // 검색 플러그인으로 검색 수행
        const searchPlugin = hot.getPlugin('search');
        const searchResult = searchPlugin.query(query);
        let resultRowsIndexes = Array.from(new Set(searchResult.map(result => result.row)));
        filteredData = resultRowsIndexes.map(index => dateFilteredData[index]);
    } else {
        // 검색어가 없는 경우, 날짜 필터링 결과를 그대로 사용
        filteredData = dateFilteredData;
    }

    // 필터링된 데이터 로드
    if (filteredData.length > 0) {
        hot.loadData(filteredData.map(item => [
            item.productionType,
            formatDate(item.workDate),
            item.productionNumber,
            item.productionName,
            item.production,
            item.workerName
        ]));
        console.log('검색 및 날짜 필터링된 데이터를 표시합니다.');
    } else {
        console.log('조건에 맞는 데이터가 없습니다.');
        hot.loadData([]);
    }
}
