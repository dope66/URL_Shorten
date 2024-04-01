let originalData = []; // 원본 데이터 배열
const selectedProcessName = document.getElementById("search-processName");
const equipmentNameSelect = document.getElementById("search-equipmentName");
const workerNameSelect = document.getElementById("search-workerName");
selectedProcessName.innerHTML = '<option value="" disabled selected>공정명 선택</option>';
selectedProcessName.addEventListener('change', () => {
    // productionType 변경 시 productionName 값 초기화
    equipmentNameSelect.value = '';
    workerNameSelect.value = '';
    fetchProcessNameAndWorkerName();
});

equipmentNameSelect.addEventListener('change', function () {
    // 공정명 선택이 이미 되어있는 경우에만 작업자 이름을 가져옵니다.
    if (selectedProcessName.value) {
        fetchProcessNameAndEquipmentName();
    }
});


// 전체 테스트 리스트 불러오기
function fetchTestList() {
    fetch(`/api/worker/testList`)
        .then(response => response.json())
        .then(data => {
            originalData = data;
            createHandsontable(originalData);

            const aggregatedData = aggregateDataByMonth(originalData);
            createDevExpressChart(aggregatedData);
        })
        .catch(error => {
            console.error('Data fetch error: ', error);
        });
}
fetchTestList();
fetchProcessNames();
fetchEquipmentName();
// handsonTable 생성
function createHandsontable(data) {
    const container = document.getElementById('test-list');
    hot = new Handsontable(container, {
        cells: function (row, col, prop) {
            var cellProperties = {};
            cellProperties.className = 'htCenter';
            return cellProperties;
        },
        className: "htCenter",
        colWidths: [100, 179, 179, 179, 179, 179, 179, 179, 179, 179], // 각 열의 너비를 픽셀 단위로 설정
        rowHeights: 30, // 모든 행의 높이를 픽셀 단위로 설정
        licenseKey: 'non-commercial-and-evaluation',
        data: data,
        colHeaders: ['ID', '생산날짜', '생산설비', '호기', '작업자', '고객사', '비고', '등록시간', '생산량', '불량수량'],
        columns: [
            {data: 'id', readOnly: true, className: "htCenter"},
            {data: 'productionDate', readOnly: true, renderer: dateRenderer, className: "htCenter"},
            {data: 'processName', readOnly: true, className: "htCenter"},
            {data: 'equipmentName', readOnly: true, className: "htCenter"},
            {data: 'processWorker', readOnly: true, className: "htCenter"},
            {data: 'company', readOnly: true, className: "htCenter"},
            {data: 'note', readOnly: true, className: "htCenter"},
            {data: 'registerDate', readOnly: true, className: "htCenter", renderer: timeRenderer},
            {data: 'productionCount', readOnly: true, className: "htCenter"},
            {data: 'defectCount', readOnly: true, className: "htCenter"},
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
        afterOnCellMouseDown: function (event, coords) {
            const rowData = hot.getSourceDataAtRow(coords.row);
            // applyDataToForm(rowData);
            console.log("rowData", rowData);
            updateTestContainer(rowData);
        }
    });
}

// 날짜 데이터 yy-mm-dd로 변환
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
// 시간 데이터를 변환
// 시간 데이터를 HH:mm:ss로 변환하는 renderer 함수
function timeRenderer(instance, td, row, col, prop, value, cellProperties) {
    const timeFormatter = new Intl.DateTimeFormat('ko-KR', {
        hour: '2-digit',
        minute: '2-digit',
        second: '2-digit',
        hour12: false // 24시간 표시
    });

    // value가 유효한 날짜인지 확인
    const timeValue = value instanceof Date ? value : new Date(value);
    if (!isNaN(timeValue.getTime())) {
        // 시간 형식으로 변환
        td.textContent = timeFormatter.format(timeValue);
    } else {
        // 유효하지 않은 날짜인 경우 기본 텍스트 설정
        td.textContent = '';
    }
    td.className += ' htCenter';
    return td;
}


document.getElementById('searchButton').addEventListener('click', performSearchForTest);

function updateTestContainer(rowData) {

    setSelectedValue('searched-processName', rowData.processName);
    setSelectedValue('searched-equipmentName', rowData.equipmentName);
    setSelectedValue('searched-workerName', rowData.processWorker);
    setSelectedValue('searched-production',rowData.productionCount);
    setSelectedValue('searched-defeatCount',rowData.defectCount);
}
function setSelectedValue(selectId, value) {
    let selectElement = document.getElementById(selectId);
    let options = selectElement.options;
    selectElement.value = value;
}
document.getElementById('total-search').addEventListener('click', function (event) {
    event.preventDefault();
    // 원본 데이터를 Handsontable에 다시 로드합니다.
    hot.loadData(originalData);
    // 검색 입력란과 선택란을 초기화합니다.
    // document.getElementById('search-workerName').value = '';
    document.getElementById('search-processName').selectedIndex = 0; // 첫 번째 옵션(공정명 선택)으로 리셋
    // document.getElementById('search-equipmentName').selectedIndex = 0; // 첫 번째 옵션(호기 선택)으로 리셋
    document.getElementById('startDate').value = ''; // startDate 입력 필드 초기화
    document.getElementById('endDate').value = ''; // endDate 입력 필드 초기화

    document.getElementById('searched-processName').value='';
    document.getElementById('searched-equipmentName').value='';
    document.getElementById('searched-workerName').value='';
    document.getElementById('searched-production').value='';
    document.getElementById('searched-defeatCount').value='';


// 전체 데이터를 기반으로 차트 데이터를 집계하고 차트를 다시 생성합니다.
    const aggregatedData = aggregateDataByMonth(originalData);
    createDevExpressChart(aggregatedData);


    // 공정원 데이터를 가져오는 API 호출
    fetch('/api/worker/getAllWorkerName')
        .then(response => response.json())
        .then(data => {
            const workerNameSelect = document.getElementById('search-workerName');
            workerNameSelect.innerHTML = '<option value="" disabled selected>공정원 선택</option>'; // 공정원 선택란 초기화
            data.forEach(workerName => {
                const option = document.createElement('option');
                option.text = workerName; // 옵션 텍스트에 공정원 이름 설정
                option.value = workerName; // 옵션 값에 공정원 이름 설정
                workerNameSelect.appendChild(option); // 공정원 선택란에 옵션 추가
            });
        })
        .catch(error => console.error('공정원 이름을 불러오는 중 오류 발생:', error));
    const equipmentNameSelect = document.getElementById('search-equipmentName');
    equipmentNameSelect.innerHTML = '<option value="" disabled selected>호기 선택</option>';

    fetch(`/api/worker/getAllEquipmentName`)
        .then(response => response.json())
        .then(equipmentNames => {
            equipmentNames.forEach(equipmentName => {
                const option = document.createElement('option');
                option.text = equipmentName;
                option.value = equipmentName;
                equipmentNameSelect.appendChild(option);
            });
        })
        .catch(error => {
            console.error('fetch 오류 요청 ', error);
        });
});
