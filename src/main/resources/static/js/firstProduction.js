
document.addEventListener('DOMContentLoaded', async function() {
    await initializeSelectionsAndLoadWorkers();
});

async function initializeSelectionsAndLoadWorkers() {
    const selectedRowData = JSON.parse(localStorage.getItem('selectedRowData') || '{}');
    await initializeProcessNameSelect(selectedRowData.processName);
    await initializeEquipmentNameSelect(selectedRowData.processName, selectedRowData.equipmentName);
    await loadProcessWorkers(selectedRowData.processName, selectedRowData.equipmentName, selectedRowData.processWorker);
}

async function initializeProcessNameSelect(selectedProcessName) {
    const processNameSelect = document.getElementById("processName");
    try {
        const response = await fetch('/api/worker/getProcessName');
        const processNames = await response.json();
        processNames.forEach(processName => {
            const option = new Option(processName, processName);
            processNameSelect.appendChild(option);
        });
        if (selectedProcessName) {
            processNameSelect.value = selectedProcessName;
        }
    } catch (error) {
        console.error('Error loading process names:', error);
    }
}


async function initializeEquipmentNameSelect(processName, selectedEquipmentName) {
    const equipmentNameSelect = document.getElementById("equipmentName");
    equipmentNameSelect.innerHTML = '<option value="" disabled selected>호기 선택</option>';
    if (processName) {
        try {
            const response = await fetch(`/api/worker/getEquipmentName?processName=${processName}`);
            const equipmentNames = await response.json();
            equipmentNames.forEach(equipmentName => {
                const option = new Option(equipmentName, equipmentName);
                equipmentNameSelect.appendChild(option);
            });
            if (selectedEquipmentName) {
                equipmentNameSelect.value = selectedEquipmentName;
            }
        } catch (error) {
            console.error('Error loading equipment names:', error);
        }
    }
}


async function loadProcessWorkers(processName, equipmentName, selectedWorkerName) {
    const workerSelect = document.getElementById("processWorker");
    workerSelect.innerHTML = '<option value="" disabled selected>공정원 선택</option>';
    if (processName && equipmentName) {
        try {
            const response = await fetch(`/api/worker/getWorkerName?processName=${processName}&equipmentName=${equipmentName}`);
            const workers = await response.json();
            workers.forEach(workerName => {
                const option = new Option(workerName, workerName);
                workerSelect.add(option);
            });

            if(selectedWorkerName) {
                workerSelect.value = selectedWorkerName;
                fetchWorkerId(selectedWorkerName); // 이미지 로드 로직 호출
            }

        } catch (error) {
            console.error('Error loading process workers:', error);
        }
    }
}
function fetchWorkerId(workerName) {
    const processName = document.getElementById("processName").value;
    const equipmentName = document.getElementById("equipmentName").value;

    if (processName && equipmentName && workerName) {
        fetch(`/api/worker/getWorkerId?processName=${processName}&equipmentName=${equipmentName}&workerName=${workerName}`)
            .then(response => response.json())
            .then(workerId => {
                console.log("workerId", workerId);
                loadWorkerImage(workerId); // 이미지 로드 함수 호출
            })
            .catch(error => {
                console.error('아이디를 가져오는 도중 오류 발생: ', error);
            });
    }
}

function loadWorkerImage(workerId) {
    const imageUrl = `/api/worker/getBase64Image?id=${workerId}`;
    fetch(imageUrl)
        .then(response => {
            if (!response.ok) throw new Error('Image not found');
            return response.text();
        })
        .then(data => {
            const imageElement = document.querySelector("#workerImage");
            if (imageElement) {
                imageElement.src = data;
            }
        })
        .catch(error => {
            console.error('Error:', error);
            const imageElement = document.querySelector("#workerImage");
            if (imageElement) {
                imageElement.src = "/static/image/defaultWorker.png";
            }
        });
}

document.getElementById("processName").addEventListener('change', async function() {
    const processName = this.value;
    await initializeEquipmentNameSelect(processName);
    await loadProcessWorkers(processName, document.getElementById("equipmentName").value);
});

document.getElementById("equipmentName").addEventListener('change', async function() {
    const equipmentName = this.value;
    const processName = document.getElementById("processName").value;
    await loadProcessWorkers(processName, equipmentName);
});

const selectedProcessName = document.getElementById("processName");
const equipmentNameSelect = document.getElementById("equipmentName");
const workerNameSelect = document.getElementById("processWorker");
const worekerIdselect = document.getElementById("workerId");
var imageElement = document.querySelector("#workerImage");

workerNameSelect.innerHTML = '<option value="" disabled selected>공정원 선택</option>';
fetchProcessNameAndWorkerName();
fetchEquipmentName();
/*
* 공정명을 이용한 함수
* */
// 공정명과 호기 이름을 가져오는 함수
function fetchProcessNameAndWorkerName() {
    // equipmentNameSelect 초기화
    equipmentNameSelect.innerHTML = '<option value="" disabled selected>호기 선택</option>';
    //공정명값으로 호기 가져오기
    if (selectedProcessName.value) {
        console.log("selectedProcessName: ", selectedProcessName.value);
        fetch(`/api/worker/getEquipmentName?processName=${selectedProcessName.value}`)
            .then(response => response.json())
            .then(equipmentNames => {
                console.log("equipmentName : ", equipmentNames);
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
        //공정 명으로 공정원 가져외기
        fetch(`/api/worker/getWorkerNameWithProcessName?processName=${selectedProcessName.value}`)
            .then(response => response.json())
            .then(workerNames => {
                const workerNameSelect = document.getElementById('processWorker');
                workerNameSelect.innerHTML = '<option value="" disabled selected>공정원 선택</option>';
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
        equipmentNameSelect.addEventListener('change', () => {
            console.log("공정명 값이 있고 호기 변경이 있을 경우 ")
            workerNameSelect.innerHTML = '<option value="" disabled selected>공정원 선택</option>';
            imageElement.src = "/static/image/defaultWorker.png";
            fetchProcessNameAndEquipmentName();
        });
    }
}

selectedProcessName.addEventListener('change', () => {
    // productionType 변경 시 productionName 값 초기화
    equipmentNameSelect.value = '';
    workerNameSelect.value = '';
    imageElement.src = "/static/image/defaultWorker.png";
    fetchProcessNameAndWorkerName();
});

// 공정명이 존재한 상태에서 호기 변경시 공정원 이름 변경
function fetchProcessNameAndEquipmentName() {
    const processName = selectedProcessName.value;
    const equipmentName = equipmentNameSelect.value;

    // workerName을 가져오는 fetch 요청을 select option이 변경되었을 때 실행
    fetch(`/api/worker/getWorkerName?processName=${processName}&equipmentName=${equipmentName}`)
        .then(response => response.json())
        .then(workerInfos => {
            workerNameSelect.innerHTML = '<option value="" disabled selected>공정원 선택</option>';
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

}

// 모든 호기 가져오기
fetch(`/api/worker/getAllEquipmentName`)
    .then(response => response.json())
    .then(equipmentNames => {
        console.log("equipmentNames: ", equipmentNames);
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

function fetchEquipmentName() {
    console.log("공정명이 선택되지 않은 경우");
    equipmentNameSelect.innerHTML = '<option value="" disabled selected>호기 선택</option>';
    workerNameSelect.innerHTML = '<option value="" disabled selected>공정원 선택</option>';
}
// workerName을 선택한 후에 Id를 가져오는 함수
function fetchWorkerId() {
    const processName = selectedProcessName.value;
    const equipmentName = equipmentNameSelect.value;
    const workerName = workerNameSelect.value;

    if (processName && equipmentName && workerName) {
        fetch(`/api/worker/getWorkerId?processName=${processName}&equipmentName=${equipmentName}&workerName=${workerName}`)
            .then(response => response.json())
            .then(workerId => {
                console.log("workerId", workerId);
                worekerIdselect.value = workerId;
                var imageUrl = "/api/worker/getBase64Image?id=" + workerId;
                fetch(imageUrl)
                    .then(function (response) {
                        // 이미지가 성공적으로 로드되었는지 확인
                        if (!response.ok) throw new Error('Image not found');
                        return response.text();
                    })
                    .then(function (data) {
                        // 성공적으로 이미지 데이터를 받아온 경우
                        var imageElement = document.querySelector("#workerImage");
                        if (imageElement) {
                            imageElement.src = data;
                        }
                    })
                    .catch(function (error) {
                        // 이미지 로드에 실패한 경우 기본 이미지로 대체
                        console.error('Error:', error);
                        var imageElement = document.querySelector("#workerImage");
                        if (imageElement) {
                            imageElement.src = "/static/image/defaultWorker.png"; // 기본 이미지 경로 설정
                        }
                    });
            })
            .catch(error => {
                console.error('아이디를 가져오는 도중 오류 발생: ', error);
            });
    } else if (processName && workerName) {
        fetch(`/api/worker/getWorkerIdWithProcessNameAndWorkerName?processName=${processName}&workerName=${workerName}`)
            .then(response => response.json())
            .then(workerId => {
                console.log("공정명과 공정원 이름으로 id 가져오기 workerId : ", workerId);
                worekerIdselect.value = workerId;
                var imageUrl = "/api/worker/getBase64Image?id=" + workerId;
                fetch(imageUrl)
                    .then(function (response) {
                        // 이미지가 성공적으로 로드되었는지 확인
                        if (!response.ok) throw new Error('Image not found');
                        return response.text();
                    })
                    .then(function (data) {
                        // 성공적으로 이미지 데이터를 받아온 경우
                        var imageElement = document.querySelector("#workerImage");
                        if (imageElement) {
                            imageElement.src = data;
                        }
                    })
                    .catch(function (error) {
                        // 이미지 로드에 실패한 경우 기본 이미지로 대체
                        console.error('Error:', error);
                        var imageElement = document.querySelector("#workerImage");
                        if (imageElement) {
                            imageElement.src = "/static/image/defaultWorker.png"; // 기본 이미지 경로 설정
                        }
                    });
            })
            .catch(error => {
                console.error('아이디를 가져오는 도중 오류 발생: ', error);
            });

    }
}

// workerName select option이 변경될 때마다 fetchWorkerId 함수 실행
workerNameSelect.addEventListener('change', fetchWorkerId);
