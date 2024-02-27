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
        // 공정명이 선택된 경우 해당하는 성명만 가져오기
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