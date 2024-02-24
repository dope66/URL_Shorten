let currentPage = 0;
let originalData = []; // 원본 데이터 배열
let hot;
const imageInput = document.getElementById('imageInput'); // 이미지 입력란
const previewImage = document.getElementById('preview-image'); // 미리보기 이미지
const registerForm = document.getElementById("worker-register-form");
const selectedProcessName = document.getElementById("search-processName");
const equipmentNameSelect = document.getElementById("search-equipmentName");
// 페이지 로드 시 실행되는 코드
document.addEventListener("DOMContentLoaded", function () {
    // 페이지 로드 시 실행되는 코드
    fetchProcessWorkerList(currentPage); // 초기 페이지 데이터 로딩
    fetchProcessNames(); // 공정명 목록 가져오기
    fetchProcessNameAndWorkerName();
    fetchWorkShiftEnum(); // 근무조 목록 가져오기
    fetchPositionEnum();
    fetchProcessNameDetail();
});
// 초기 공정원 불러오기 및 페이지네이션
function fetchProcessWorkerList(page) {
    fetch(`/api/worker/list?page=${page}`)
        .then(response => response.json())
        .then(data => {
            originalData = data;
            createHandsontable(originalData);
            console.log("originalDataPagination", originalData);

        });
}
// 근무조 목록 가져오기
function createHandsontable(data) {
    const container = document.getElementById('employee-Table');
    hot = new Handsontable(container, {
        cells: function (row, col, prop) {
            var cellProperties = {};
            cellProperties.className = 'htCenter';
            return cellProperties;
        },
        className: "htCenter",
        colWidths: [100, 100, 100, 100, 100, 100, 150], // 각 열의 너비를 픽셀 단위로 설정
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
        height: 300,
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

    console.log("hot : ", hot);
}
// 공정원 검색 기능
function workerSearch() {
    const processNameSelected = document.getElementById('search-processName').value;
    const equipmentNameSelected = document.getElementById('search-equipmentName').value;
    const workerNameQuery = document.getElementById('search-input').value.trim().toLowerCase(); // 대소문자 구분 없이 검색하기 위해 소문자로 변환

    const filteredData = originalData.filter(item => {
        const matchesProcessName = processNameSelected ? item.processName === processNameSelected : true;
        const matchesEquipmentName = equipmentNameSelected ? item.equipmentName === equipmentNameSelected : true;
        const matchesWorkerName = workerNameQuery ? item.workerName.toLowerCase().includes(workerNameQuery) : true;
        return matchesProcessName && matchesEquipmentName && matchesWorkerName;
    });

    if (filteredData.length > 0) {
        // 검색 결과가 있는 경우, 검색된 데이터만 로드
        hot.loadData(filteredData);
        console.log('검색된 데이터를 표시합니다.');
    } else {
        // 검색 결과가 없는 경우
        hot.loadData([]);
        console.log('검색 결과가 없습니다.');
    }
}
// 전체 공정원 리스트 불러오기
function wholeWorker() {
    // 원본 데이터를 Handsontable에 다시 로드합니다.
    hot.loadData(originalData);
    console.log("전체 리스트를 불러옵니다.");

    // 검색 입력란과 선택란을 초기화합니다.
    document.getElementById('search-input').value = '';
    document.getElementById('search-processName').selectedIndex = 0; // 첫 번째 옵션(공정명 선택)으로 리셋
    document.getElementById('search-equipmentName').selectedIndex = 0; // 첫 번째 옵션(호기 선택)으로 리셋


}
fetch('/api/worker/getAllWorkerName')
    .then(response => response.json()
        .then(data => {
            const workerNameSelect = document.getElementById('search-input');
            workerNameSelect.innerHTML = '<option value="" disabled selected>성명 선택</option>';
            data.forEach(workerName => {
                const option = document.createElement('option');
                option.text = workerName;
                option.value = workerName;
                workerNameSelect.appendChild(option);
            });
        }));
// 이미지 미리 보기 기능
imageInput.addEventListener('change', function () {
    const file = this.files[0];
    if (file) {
        const reader = new FileReader();
        reader.onload = function (event) {
            previewImage.src = event.target.result;
            previewImage.style.display = 'block'; // 이미지가 선택되면 보이게 함
        }
        reader.readAsDataURL(file);
    } else {
        previewImage.src = '#'; // 파일 선택이 취소되면 빈 이미지로 설정
        previewImage.style.display = 'none'; // 이미지를 숨김
    }
});
// 홈 버튼
document.getElementById('homeButton').addEventListener('click', function (event) {
    event.preventDefault();
    window.location.href = "/mes/home";
});
// 공정원 등록
registerForm.addEventListener('submit', (event) => {
    event.preventDefault();

    // 이미지 파일 가져오기
    const imageFile = imageInput.files[0];
    const processName = document.getElementById('processName').value;
    const equipmentName = document.getElementById('equipmentName').value;
    const workerName = document.getElementById('workerName').value;
    const nation = document.getElementById('nation').value;
    const position = document.getElementById('position').value;
    const workShift = document.getElementById('workShift').value;

    // FormData 객체 생성
    const formData = new FormData();
    formData.append('image', imageFile);
    formData.append('processName', processName);
    formData.append('equipmentName', equipmentName);
    formData.append('workerName', workerName);
    formData.append('nation', nation);
    formData.append('position', position);
    formData.append('workShift', workShift);

    const confirmed = window.confirm('공정원 등록하시겠습니까?');
    if (confirmed) {
        fetch('/api/worker/register', {
            method: 'POST',
            body: formData // FormData 객체 전송
        })
            .then(response => {
                if (response.ok) {
                    alert('회원 등록완료');
                    window.location.reload();
                }
            })
            .catch(error => {
                console.error('fetch 오류 요청 ', error);
            });
    }
});

// 해당하는 공정명 검색기능으로 가져오기
function fetchProcessNames(selectedProcessName) {
    fetch('/api/worker/getProcessName')
        .then(response => response.json())
        .then(data => {
            const processNameSelect = document.getElementById('search-processName');
            // 기존의 옵션을 초기화
            processNameSelect.innerHTML = '<option value="" disabled selected>공정 선택</option>';
            // 가져온 데이터로 옵션 생성
            data.forEach(processName => {
                const option = document.createElement('option');
                option.text = processName;
                option.value = processName;
                processNameSelect.appendChild(option);
            });
            // 선택된 공정명이 있을 경우 해당 옵션을 선택 상태로 설정
            if (selectedProcessName) {
                processNameSelect.value = selectedProcessName;
            }
        })
        .catch(error => {
            console.error('fetch 오류 요청: ', error);
        });
}
// 공정명 변경시 호기 목록 가져오기
selectedProcessName.addEventListener('change', () => {
    // productionType 변경 시 productionName 값 초기화
    equipmentNameSelect.value = '';
    fetchProcessNameAndWorkerName();
});
// 공정명에 따른 호기 목록 가져오기
function fetchProcessNameAndWorkerName() {
    // 호기 선택란 초기화
    equipmentNameSelect.innerHTML = '<option value="" disabled selected>호기 선택</option>';

    if (selectedProcessName.value) {
        // 공정명이 선택된 경우, 해당하는 호기만 가져오기
        fetch(`/api/worker/getEquipmentName?processName=${selectedProcessName.value}`)
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
    } else {
        // 공정명이 선택되지 않은 경우, 모든 호기 가져오기
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
    }
}

