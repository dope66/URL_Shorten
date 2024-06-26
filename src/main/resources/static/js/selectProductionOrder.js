const selectedProcessName = document.getElementById("search-processName");
const equipmentNameSelect = document.getElementById("search-equipmentName");
const workerNameSelect = document.getElementById("search-workerName");
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
fetchProcessNames();
fetchProcessWorkerList();
fetchProcessNameAndWorkerName();
fetchEquipmentName();

function fetchProcessWorkerList() {
    fetch(`/api/worker/list`)
        .then(response => response.json())
        .then(data => {
            originalData = data;
            createHandsontable(originalData);
        });
}

function createHandsontable(data) {
    const container = document.getElementById('employee-Table');
    hot = new Handsontable(container, {
        cells: function (row, col, prop) {
            var cellProperties = {};
            cellProperties.className = 'htCenter';
            return cellProperties;
        },
        className: "htCenter",
        colWidths: [260, 260, 270, 270, 270, 270, 270], // 각 열의 너비를 픽셀 단위로 설정
        rowHeights: 30, // 모든 행의 높이를 픽셀 단위로 설정
        licenseKey: 'non-commercial-and-evaluation',
        data: data,
        colHeaders: ['ID', '공정명', '호기', '성명', '국적', '직책', '주야간조'],
        columns: [
            {data: 'id', readOnly: true, className: "htCenter"},
            {data: 'processName', readOnly: true, className: "htCenter"},
            {data: 'equipmentName', readOnly: true, className: "htCenter"},
            {data: 'workerName', readOnly: true, className: "htCenter"},
            {data: 'nation', readOnly: true, className: "htCenter"},
            {data: 'position', readOnly: true, className: "htCenter"},
            {data: 'workShift', readOnly: true, className: "htCenter"},
        ],
        hiddenColumns: {
            columns: [0], // 'ID' 열을 숨깁니다.
            indicators: false // 숨겨진 열의 지시자 표시 여부
        },
        height: 250,
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
            updateWorkerContainer(rowData);
        }
    });
    console.log("hot이란 무엇인가",hot);
}

function updateWorkerContainer(rowData) {
    // Select 태그의 값 설정
    setSelectedValue('workerName', rowData.workerName);
    setSelectedValue('nation', rowData.nation);
    setSelectedValue('processName', rowData.processName);
    setSelectedValue('equipmentName', rowData.equipmentName);
    setSelectedValue('position', rowData.position);
    setSelectedValue('workShift', rowData.workShift);

}

document.getElementById('searchWorkerButton').addEventListener('click', workerSearch);

function setSelectedValue(selectId, value) {
    let selectElement = document.getElementById(selectId);
    let options = selectElement.options;
    selectElement.value = value;
}

const registerForm = document.getElementById("order-register");
document.getElementById('registerButton').addEventListener('click', function () {
    event.preventDefault();
    const processName = document.getElementById('processName').value;
    const equipmentName = document.getElementById('equipmentName').value;
    const processWorker = document.getElementById('workerName').value;
    const note = document.getElementById('note').value;
    const productionCount = document.getElementById('productionCount').value;
    const productionDate = document.getElementById('productionDate').value;

    // 사용자가 productionDate를 입력하지 않은 경우에만 현재 날짜와 시간을 할당
    const currentDate = new Date();
    const currentDateTime = currentDate.toISOString(); // ISO 형식으로 변환

    const data = {
        processName: processName,
        equipmentName: equipmentName,
        processWorker: processWorker,
        productionDate: productionDate ? productionDate : currentDateTime, // productionDate가 비어있을 때만 현재 날짜와 시간 추가
        note: note,
        productionCount: productionCount,
    };
    console.log("data", data);

    const confirmed = window.confirm('생산지시를 등록하시겠습니까?');
    if (confirmed) {
        fetch('/api/order/register', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data) // JSON 데이터 전송
        })
            .then(response => {
                if (response.ok) {
                    alert('생산지시 등록 완료');
                    window.location.reload();
                }
            })
            .catch(error => {
                console.error('fetch 오류 요청 ', error);
            });
    }

});

