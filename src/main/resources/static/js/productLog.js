fetchProductLog(); // 초기 페이지 데이터 로딩

function openPopup() {
    const popupWindow = window.open("popUp", "Popup", "width=700,height=900");
}

function fetchProductLog() {
    fetch(`/api/mes/productLogList`)
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(data => {
            originalData = data;
            console.log("data? ",data);
            // 데이터가 비어있지 않은지 확인
            if (originalData && originalData.length > 0) {
                createHandsontable(originalData);
            } else {
                console.error('No data received');
            }
        })
        .catch(error => {
            console.error('There has been a problem with your fetch operation:', error);
        });
}
function formatDate(dateString) {
    const date = new Date(dateString);
    return date.toLocaleDateString("ko-KR");
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
function createHandsontable(data) {
    console.log("handsontable 실행?")
    console.log("handsontable 실행? 그러면 data? ",data);
    const container = document.getElementById('product-log');
    hot  =new Handsontable(container, {
        cells: function(row, col, prop) {
            var cellProperties = {};
            cellProperties.className = 'htCenter';
            return cellProperties;
        },
        className: "htCenter",
        colWidths: [270, 270, 270, 270, 270, 265], // 각 열의 너비를 픽셀 단위로 설정
        rowHeights: 30, // 모든 행의 높이를 픽셀 단위로 설정
        licenseKey: 'non-commercial-and-evaluation',
        data:data,
        colHeaders: [ '차종', '작업날짜', '품번', '품명', '생산량', '생산자'],
        columns: [
            {data: 'productionType', readOnly: true, className: "htCenter"},
            {data: 'workDate', readOnly: true ,renderer: dateRenderer, className: "htCenter"},
            {data: 'productionNumber', readOnly: true, className: "htCenter"},
            {data: 'productionName', readOnly: true, className: "htCenter"},
            {data: 'production', readOnly: true, className: "htCenter"},
            {data: 'workerName', readOnly: true, className: "htCenter"}
        ],
        height: 700,
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
    const startDateElement = document.getElementById("start-date");
    const endDateElement = document.getElementById("end-date");
    const query = document.getElementById('search-input').value.trim().toLowerCase();

    const filteredData = originalData.filter(item => {
        // 날짜 필터링
        const itemDate = new Date(item.workDate);
        const startDate = startDateElement.value ? new Date(startDateElement.value) : null;
        if (startDate) {
            startDate.setHours(0, 0, 0, 0); // 시작 날짜를 그 날의 자정으로 설정
        }
        const endDate = endDateElement.value ? new Date(endDateElement.value) : null;
        if (endDate) {
            endDate.setHours(23, 59, 59, 999); // 종료 날짜를 그 날의 마지막 시각으로 설정
        }
        const matchesDate = (!startDate || itemDate >= startDate) && (!endDate || itemDate <= endDate);

        // 검색어 필터링
        const matchesQuery = !query || Object.values(item).some(value =>
            String(value).toLowerCase().includes(query)
        );

        return matchesDate && matchesQuery;
    });

    if (filteredData.length > 0) {
        hot.loadData(filteredData);
        console.log('검색된 데이터를 표시합니다.');
    } else {
        hot.loadData([]);
        console.log('검색 결과가 없습니다.');
    }
}
