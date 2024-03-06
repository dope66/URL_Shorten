document.addEventListener('DOMContentLoaded', function () {
    const urlParams = new URLSearchParams(window.location.search);
    const processName = urlParams.get('processName');
    const equipmentName = urlParams.get('equipmentName');

    fetch(`/api/order/list?processName=${encodeURIComponent(processName)}&equipmentName=${encodeURIComponent(equipmentName)}`)
        .then(response => response.json())
        .then(data => {
            originalData = data;
            createHandsontable(originalData);
        });
    document.getElementById('movePageButton').addEventListener('click', function() {
        if (selectedRow !== null) {
            moveTestPage(selectedRow);
        } else {
            alert('행을 선택해주세요.');
        }
    });
});

function moveTestPage(rowData) {
    localStorage.setItem('selectedRowData', JSON.stringify({
        processName: rowData.processName,
        equipmentName: rowData.equipmentName,
        processWorker: rowData.processWorker // 이 부분 추가
    }));
    window.location.href = '/mes/firstProduction';
}
// 전역 변수로 선택된 행 데이터를 저장
let selectedRow = null;
function createHandsontable(data) {
    const container = document.getElementById('productionOrder-list');
    hot = new Handsontable(container, {
        cells: function (row, col, prop) {
            var cellProperties = {};
            cellProperties.className = 'htCenter';
            return cellProperties;
        },
        className: "htCenter",
        colWidths: [100, 150, 100, 100, 100, 100, 150, 200, 100], // 각 열의 너비를 픽셀 단위로 설정
        rowHeights: 30, // 모든 행의 높이를 픽셀 단위로 설정
        licenseKey: 'non-commercial-and-evaluation',
        data: data,
        colHeaders: ['순', '공정명', '생산날짜', '호기', '작업자', '목표량', '비고'],
        columns: [
            {data: 'id', readOnly: true, className: "htCenter"},
            {data: 'processName', readOnly: true, className: "htCenter"},
            {data: 'productionDate', readOnly: true, renderer: dateRenderer, className: "htCenter"},
            {data: 'equipmentName', readOnly: true, className: "htCenter"},
            {data: 'processWorker', readOnly: true, className: "htCenter"},
            {data: 'productionCount', readOnly: true, className: "htCenter"},
            {data: 'note', readOnly: true, className: "htCenter"},
        ],
        hiddenColumns: {
            columns: [0], // 'ID' 열을 숨깁니다.
            indicators: false // 숨겨진 열의 지시자 표시 여부
        },
        height: 300,
        viewportRowRenderingOffset: 5, // 표시할 행의 오프셋
        columnSorting: false, // 정렬 활성화
        contextMenu: false, // 우클릭 메뉴 활성화
        manualRowMove: true, // 행 이동 활성화
        manualColumnMove: true, // 열 이동 활성화
        filters: true, // 필터링 활성화
        dropdownMenu: false, // 드롭다운 메뉴 활성화
        rowHeaders: true, // 행 번호 표시
        search: true, // 검색 기능 활성화
        afterSelection: function(r, c, r2, c2) {
            // 이전에 선택된 행의 배경색을 초기화합니다.
            this.render(); // Handsontable 인스턴스를 다시 렌더링하여 스타일을 초기화합니다.

            selectedRow = this.getSourceDataAtRow(r);
            console.log("selectedRow",selectedRow);
            this.updateSettings({
                cells: function(row, col) {
                    var cellProperties = {};
                    if(row === r) {
                        cellProperties.renderer = function(instance, td, row, col, prop, value, cellProperties) {
                            Handsontable.renderers.TextRenderer.apply(this, arguments);
                            td.style.backgroundColor = '#9acee3'; // 선택한 행의 배경색 변경
                        };
                    }
                    return cellProperties;
                }
            });
        },

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

function startButtonRenderer(instance, td, row, col, prop, value, cellProperties) {
    // 기존의 셀 내용을 초기화
    Handsontable.dom.empty(td);

    // 버튼 엘리먼트 생성
    const button = document.createElement('button');
    button.textContent = '시작';
    button.className = 'start-button'; // CSS 클래스 설정 (스타일링을 위해)
    button.onclick = function() {
        const rowData = instance.getSourceDataAtRow(row);
        moveTestPage(rowData); // 버튼 클릭 시 실행할 함수
    };

    // 버튼을 td에 추가
    td.appendChild(button);

    return td;
}
