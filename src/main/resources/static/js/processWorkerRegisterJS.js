let currentPage = 0;
let originalData = []; // 원본 데이터 배열
let hot;
const imageInput = document.getElementById('imageInput'); // 이미지 입력란
const previewImage = document.getElementById('preview-image'); // 미리보기 이미지
const registerForm = document.getElementById("worker-register-form");
const selectedProcessName = document.getElementById("search-processName");
const equipmentNameSelect = document.getElementById("search-equipmentName");
let currentWorkerId = null;

// 페이지 로드 시 실행되는 코드
document.addEventListener("DOMContentLoaded", function () {
    // 페이지 로드 시 실행되는 코드
    fetchInitialData();
});
function fetchInitialData() {
    fetchProcessWorkerList(currentPage);
    fetchProcessNames();
    fetchProcessNameAndWorkerName();
    fetchWorkShiftEnum();
    fetchPositionEnum();
    fetchProcessNameDetail();
}

// 초기 공정원 불러오기 및 페이지네이션
function fetchProcessWorkerList(page) {
    fetch(`/api/worker/list?page=${page}`)
        .then(response => response.json())
        .then(data => {
            originalData = data;
            createHandsontable(originalData);
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
}

// 공정원 검색 기능
function workerSearch() {
    const processNameSelected = selectedProcessName.value;
    const equipmentNameSelected = equipmentNameSelect.value;
    const workerNameQuery = document.getElementById('search-workerName').value.trim().toLowerCase();

    const filteredData = originalData.filter(item => {
        const matchesProcessName = !processNameSelected || item.processName === processNameSelected;
        const matchesEquipmentName = !equipmentNameSelected || item.equipmentName === equipmentNameSelected;
        const matchesWorkerName = !workerNameQuery || item.workerName.toLowerCase().includes(workerNameQuery);
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
    document.getElementById('search-workerName').value = '';
    document.getElementById('search-processName').selectedIndex = 0; // 첫 번째 옵션(공정명 선택)으로 리셋
    document.getElementById('search-equipmentName').selectedIndex = 0; // 첫 번째 옵션(호기 선택)으로 리셋

    AllWorkerName();

}

//전체 공정원 이름 리스트 불러오기
function AllWorkerName() {
    fetch('/api/worker/getAllWorkerName')
        .then(response => response.json()
            .then(data => {
                const workerNameSelect = document.getElementById('search-workerName');
                workerNameSelect.innerHTML = '<option value="" disabled selected>성명 선택</option>';
                data.forEach(workerName => {
                    const option = document.createElement('option');
                    option.text = workerName;
                    option.value = workerName;
                    workerNameSelect.appendChild(option);
                });
            }));
}

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

// 공정명 가져오기
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
        fetch(`/api/worker/getWorkerNameWithProcessName?processName=${selectedProcessName.value}`)
            .then(response => response.json())
            .then(workerNames => {
                const workerNameSelect = document.getElementById('search-workerName');
                workerNameSelect.innerHTML = '<option value="" disabled selected>성명 선택</option>';
                workerNames.forEach(workerName => {
                    const option = document.createElement('option');
                    option.text = workerName;
                    option.value = workerName;
                    workerNameSelect.appendChild(option);
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
        AllWorkerName();


    }

}

//권한 부여 기능
document.addEventListener('DOMContentLoaded', function () {
    // 페이지 로드 시 권한 상태 확인 및 적용
    const hasPermission = sessionStorage.getItem('hasEditPermission') === 'true';
    enableEditAndDeleteFeatures(hasPermission, false)
    const positionSelect = document.getElementById('position');
    positionSelect.addEventListener('change', function () {
        const selectedPosition = positionSelect.value;
        const formContainer = document.getElementById('form-container');

        removeLoginFields(); // 이전 필드 제거
        if (selectedPosition === '조장') {
            addLoginFields(formContainer); // 조장 선택 시 로그인 필드 추가
        }
    });

    function addLoginFields(parentElement) {
        const fieldContainer = document.createElement('div');
        fieldContainer.style.display = 'flex'; // Flexbox 레이아웃 적용
        fieldContainer.style.alignItems = 'center'; // 세로 중앙 정렬
        fieldContainer.style.marginTop = '5px'; // 상단 여백 추가
        fieldContainer.className = 'field-container'; // 클래스 이름 추가

        // 비밀번호 입력 필드 생성 및 스타일 설정
        const passwordInput = createInputElement('password', 'adminPassword', '관리자 비밀번호');
        passwordInput.style.marginRight = '5px'; // 버튼과의 간격
        passwordInput.style.width = '190px'; // 너비 설정

        // 권한 부여 버튼 생성 및 스타일 설정
        const authButton = document.createElement('button');
        authButton.textContent = '권한 부여';
        authButton.id = 'authButton';
        authButton.style.backgroundColor = '#e8b839';
        authButton.style.color = 'white';
        authButton.style.border = 'none';
        authButton.style.cursor = 'pointer';
        authButton.style.width = '100px';

        // fieldContainer에 비밀번호 입력 필드와 권한 부여 버튼 추가
        fieldContainer.appendChild(passwordInput);
        fieldContainer.appendChild(authButton);

        // position 선택 요소의 label 태그의 다음 형제로 fieldContainer 삽입
        const nextSibling = positionSelect.nextElementSibling;
        const formContainer = positionSelect.parentNode;
        formContainer.insertBefore(fieldContainer, nextSibling);

        authButton.addEventListener('click', function (event) {
            event.preventDefault(); // 폼의 기본 제출을 방지
            const password = passwordInput.value;
            if (password === 'admin') {
                alert('권한이 부여되었습니다.');
                sessionStorage.setItem('hasEditPermission', 'true'); // sessionStorage를 사용하여 권한 부여 상태 저장
                enableEditAndDeleteFeatures(true);
                // 권한 부여 후 필드 및 버튼 숨기기
                fieldContainer.style.display = 'none';
            } else {
                alert('비밀번호가 잘못되었습니다.');
                enableEditAndDeleteFeatures(false);
            }
        });
    }

    function createInputElement(type, id, placeholder) {
        const input = document.createElement('input');
        input.type = type;
        input.id = id;
        input.placeholder = placeholder;
        return input;
    }

    function removeLoginFields() {
        const existingFieldContainer = document.querySelector('.field-container');
        if (existingFieldContainer) existingFieldContainer.remove();
    }

    if (hasPermission) {
        createDeleteButton();
    }

});

function enableEditAndDeleteFeatures(hasPermission, isEditMode = true) {
    const modifyButton = document.getElementById('registerButton');
    const deleteButton = document.getElementById('deleteButton'); // 삭제 버튼 선택
    // modifyButton.style.display = (isEditMode && hasPermission) || !isEditMode ? 'inline-block' : 'none';

    if (modifyButton) {
        if (hasPermission && isEditMode) {
            modifyButton.style.display = 'inline-block';
            // deleteButton이 존재할 때만 스타일을 변경
            if (deleteButton) deleteButton.style.display = 'inline-block';
        } else if (!isEditMode) {
            modifyButton.style.display = 'inline-block';
            // 등록 모드에서는 deleteButton을 숨김 (존재하는 경우)
            if (modifyButton.value === "등록") deleteButton.style.display = 'none';
        } else {
            // 권한이 없을 경우 modifyButton을 숨김
            modifyButton.style.display = 'none';
            // 권한이 없을 경우 deleteButton도 숨김 (존재하는 경우)
            if (deleteButton) deleteButton.style.display = 'none';
        }
    }


}
// 수정 버튼 변경
function updateWorkerContainer(rowData) {

    const isEditMode = !!rowData && !!rowData.id;
    let deleteButton = document.getElementById('deleteButton');
    currentWorkerId = rowData ? rowData.id : null; // 현재 worker ID 업데이트
    if (!deleteButton) {
        createDeleteButton();
    } else {
        deleteButton.style.display = rowData && rowData.id ? 'inline-block' : 'none';
    }


    document.getElementById('workerName').value = rowData ? rowData.workerName : '';
    document.getElementById('nation').value = rowData ? rowData.nation : '';
    // Select 태그의 값 설정
    setSelectedValue('processName', rowData.processName);
    setSelectedValue('equipmentName', rowData.equipmentName);
    setSelectedValue('position', rowData.position);
    setSelectedValue('workShift', rowData.workShift);

    // 이미지 업데이트
    fetchImageAndUpdatePreview(rowData.id);

    // "등록" 버튼을 "수정" 버튼으로 변환
    const registerButton = document.getElementById('registerButton');
    registerButton.textContent = rowData.id ? '수정' : '등록';
    const hasPermission = sessionStorage.getItem('hasEditPermission') === 'true'; // sessionStorage를 사용하여 권한 상태 확인
    registerButton.style.display = hasPermission ? 'inline-block' : 'none'; // 권한이 있으면 버튼을 보여주고, 없으면 숨김
    enableEditAndDeleteFeatures(hasPermission, isEditMode); // isEditMode를 기반으로 버튼 표시 여부 결정

    registerButton.onclick = function () {
        event.preventDefault(); // 폼의 기본 제출을 방지
        if (rowData.id) {
            modifyWorker(rowData.id); // 수정 함수 호출, 필요에 따라 rowData.id 전달
        }

    };
}
// 삭제 기능
function deleteWorker(workerId) {
    event.preventDefault();
    const confirmed = window.confirm('정말 삭제 하시겠습니까?');
    if (confirmed) {
        fetch('/api/worker/' + workerId, {
            method: 'DELETE',
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('삭제 실패');
                }
                alert('삭제 완료되었습니다.');
                window.location.reload();
            })
            .catch(error => {
                console.error('Error:', error);
                alert('삭제에 실패했습니다. 네트워크 오류가 발생했습니다.');
            });
    }
}
const deleteButton = document.createElement('button');
// 삭제 버튼 생성
function createDeleteButton() {
    // 삭제 버튼 생성

    deleteButton.id = 'deleteButton';
    deleteButton.textContent = '삭제';
    deleteButton.style.backgroundColor = '#f61e1e'; // 일단 숨김
    deleteButton.style.borderColor = '#f61e1e';
    deleteButton.style.color = 'white';
    deleteButton.style.height= '30px';
    deleteButton.style.width= '100px';
    deleteButton.style.display = 'none'; // 처음에는 숨김
    // navigation-container 요소 선택
    const navigationContainer = document.getElementById('navigation-container');

    // 삭제 버튼을 navigation-container에 추가
    navigationContainer.appendChild(deleteButton);

    // 삭제 버튼에 클릭 이벤트 리스너 추가
    deleteButton.onclick = function () {

        event.preventDefault();
        if (currentWorkerId) {
            deleteWorker(currentWorkerId);
        }
    };
}

function modifyWorker(workerId) {
    // FormData 객체 생성
    const formData = new FormData();
    const imageFile = document.getElementById('imageInput').files[0];
    if (imageFile) {
        formData.append('image', imageFile);
    }

    formData.append('workerName', document.getElementById('workerName').value);
    formData.append('nation', document.getElementById('nation').value);
    formData.append('equipmentName', document.getElementById('equipmentName').value);
    formData.append('processName', document.getElementById('processName').value);
    formData.append('position', document.getElementById('position').value);
    formData.append('workShift', document.getElementById('workShift').value);

    // 사용자에게 수정을 확인
    const confirmed = window.confirm('정말 수정 하시겠습니까?');
    if (confirmed) {
        fetch('/api/worker/modify/' + workerId, {
            method: 'POST',
            body: formData,
        })
            .then(response => {
                if (response.ok) {
                    alert('수정이 완료되었습니다.');
                    window.location.reload();
                } else {
                    alert('수정에 실패했습니다. 상태 코드: ' + response.status);
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('수정에 실패했습니다. 네트워크 오류가 발생했습니다.');
            });
    }


    console.log('Sending modified data to server:', formData);
}


// Select 태그에 선택된 값을 설정하는 함수
function setSelectedValue(selectId, value) {
    let selectElement = document.getElementById(selectId);
    let options = selectElement.options;
    for (let option of options) {
        if (option.value == value) {
            selectElement.value = value;
            break;
        }
    }
}
/// 이미지 불러오기
function fetchImageAndUpdatePreview(workerId) {
    fetch(`/api/worker/getBase64Image?id=${workerId}`)
        .then(response => response.text()) // 응답을 텍스트로 처리
        .then(data => {
            const previewImage = document.getElementById('preview-image');
            previewImage.src = data; // Base64 인코딩된 이미지 데이터를 직접 src에 할당
            console.log('imagePathInput', previewImage.src);

            // 이미지 미리보기 업데이트 후 `required` 속성 조건적 제거 로직
            const defaultImagePath = '/static/image/defaultWorker.png'; // 기본 이미지 경로 설정
            const imageInput = document.getElementById('imageInput');
            if (previewImage.src !== window.location.origin + defaultImagePath) {
                imageInput.removeAttribute('required');
            } else {
                // 기본 이미지일 경우 required 속성을 다시 추가
                imageInput.setAttribute('required', '');
            }
        })
        .catch(error => {
            console.error('이미지를 불러오는 중 오류 발생:', error);
            const previewImage = document.getElementById('preview-image');
            previewImage.src = '/static/image/defaultWorker.png'; // 에러 발생 시 기본 이미지로 대체

            // 에러 발생 시 required 속성을 다시 추가
            const imageInput = document.getElementById('imageInput');
            imageInput.setAttribute('required', '');
        });
}


