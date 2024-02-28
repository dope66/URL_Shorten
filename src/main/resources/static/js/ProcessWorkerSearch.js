
// 공정명 변경 시 호기 불러오기 및 호기 선택시 공정원 불러오기
function fetchProcessNameAndWorkerName() {
    console.log("공정원 이름 불러오는 시스템 ")
    // 호기 선택란 초기화
    equipmentNameSelect.innerHTML = '<option value="" disabled selected>호기 선택</option>';
    // 공정명이 있을경우
    if (selectedProcessName.value) {
        console.log("공정명이 선택된 경우");
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
        console.log("공정명이 선택된 경우 processName:",selectedProcessName.value);
        // 공정명으로 공정원 가져오기
        fetch(`/api/worker/getWorkerNameWithProcessName?processName=${selectedProcessName.value}`)
            .then(response => response.json())
            .then(workerNames => {
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
        equipmentNameSelect.addEventListener('change',()=>{
            console.log("공정명 값이 있고 호기 변경이 있을 경우 ")
            workerNameSelect.innerHTML = '<option value="" disabled selected>성명 선택</option>';
            fetchProcessNameAndEquipmentName();
        });
    }
}
// 공정명과 호기가 둘다 있을 경우 공정원 불러오는 함수
function fetchProcessNameAndEquipmentName(){
    // 공정명과 호기 가 둘다 있을 경우
    if(selectedProcessName.value && equipmentNameSelect.value){
        console.log("공정명, 호기가 선택된 경우");
        // workerName을 가져오는 fetch 요청을 select option이 변경되었을 때 실행
        fetch(`/api/worker/getWorkerName?processName=${selectedProcessName.value}&equipmentName=${equipmentNameSelect.value}`)
            .then(response => response.json())
            .then(workerInfos => {
                workerNameSelect.innerHTML = '<option value="" disabled selected>선택하세요</option>';
                workerInfos.forEach(workerInfo => {
                    const option = document.createElement('option');
                    option.text = workerInfo;
                    option.value = workerInfo;
                    workerNameSelect.appendChild(option);
                });
            })
            .catch(error => {
                console.error('fetch 오류 요청 ', error);
            });
        console.log("공정명,호기가 선택된 경우",selectedProcessName.value,equipmentNameSelect.value);

    }
}
// 공정명이 선택되지 않은 경우 호기와 전체 공정원(AllWorkerName) 불러오는 함수
function fetchEquipmentName(){
    console.log("공정명이 선택되지 않은 경우");
    equipmentNameSelect.innerHTML = '<option value="" disabled selected>호기 선택</option>';
    workerNameSelect.innerHTML = '<option value="" disabled selected>성명 선택</option>';
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
// 전체 공정원을 불러오는 함수
function AllWorkerName() {
    fetch('/api/worker/getAllWorkerName')
        .then(response => response.json()
            .then(data => {
                workerNameSelect.innerHTML = '<option value="" disabled selected>성명 선택</option>';
                data.forEach(workerName => {
                    const option = document.createElement('option');
                    option.text = workerName;
                    option.value = workerName;
                    workerNameSelect.appendChild(option);
                });
            }));
}
// 검색 하는 함수
function workerSearch() {
    const processNameSelected = selectedProcessName.value;
    const equipmentNameSelected = equipmentNameSelect.value;
    // const workerNameQuery = document.getElementById('search-workerName').value.trim().toLowerCase();
    const workerNameElement = document.getElementById('search-workerName');
    const workerNameQuery = workerNameElement && workerNameElement.value ? workerNameElement.value.trim().toLowerCase() : '';


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
// 전체 공정명(생산설비)을 불러오는 함수
function fetchProcessNames(selectedProcessName) {
    console.log("전체 공정명 가져오기");
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

