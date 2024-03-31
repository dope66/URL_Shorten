
document.addEventListener('DOMContentLoaded', function () {
    fetch(`/api/order/AllListToday`)
        .then(response => response.json())
        .then(data => {
            originalData = data;
            createHandsontable(originalData);
        });
});
function createHandsontable(data) {
    const container = document.getElementById('productionOrder-list');
    hot = new Handsontable(container, {
        cells: function (row, col, prop) {
            var cellProperties = {};
            cellProperties.className = 'htCenter';
            return cellProperties;
        },
        className: "htCenter",
        colWidths: [170,180,180,180, 180, 180, 180, 180, 180, 180, 180], // 각 열의 너비를 픽셀 단위로 설정
        rowHeights: 30, // 모든 행의 높이를 픽셀 단위로 설정
        licenseKey: 'non-commercial-and-evaluation',
        data: data,
        colHeaders: ['차종','품번','품명','공정명', '생산날짜', '호기', '작업자', '목표량','비고'],
        columns: [
            {data: 'productType', readOnly: true, className: "htCenter"},
            {data: 'productionNumber', readOnly: true, className: "htCenter"},
            {data: 'productionName', readOnly: true, className: "htCenter"},
            {data: 'processName', readOnly: true, className: "htCenter"},
            {data: 'productionDate', readOnly: true, renderer: dateRenderer, className: "htCenter"},
            {data: 'equipmentName', readOnly: true, className: "htCenter"},
            {data: 'processWorker', readOnly: true, className: "htCenter"},
            {data: 'productionCount', readOnly: true, className: "htCenter"},
            {data: 'note', readOnly: true, className: "htCenter"},
        ],
        height: 700,
        viewportRowRenderingOffset: 5, // 표시할 행의 오프셋
        columnSorting: false, // 정렬 활성화
        contextMenu: false, // 우클릭 메뉴 활성화
        manualRowMove: true, // 행 이동 활성화
        manualColumnMove: true, // 열 이동 활성화
        filters: true, // 필터링 활성화
        dropdownMenu: false, // 드롭다운 메뉴 활성화
        rowHeaders: true, // 행 번호 표시
        search: true, // 검색 기능 활성화
    });
}
function dateRenderer(instance, td, row, col, prop, value, cellProperties) {
    const dateFormatter = new Intl.DateTimeFormat('ko-KR', {
        year: 'numeric', // '2-digit'에서 'numeric'으로 변경하여 연도를 4자리로 표시
        month: '2-digit',
        day: '2-digit'
    });

    // value가 유효한 날짜인지 확인
    const dateValue = value instanceof Date ? value : new Date(value);
    if (!isNaN(dateValue.getTime())) {
        // 날짜 형식으로 변환
        td.textContent = dateFormatter.format(dateValue).replace(/\./g, '-').slice(0, -1);
    } else {
        // 유효하지 않은 날짜인 경우 기본 텍스트 설정
        td.textContent = '';
    }
    td.className += ' htCenter';
    return td;
}