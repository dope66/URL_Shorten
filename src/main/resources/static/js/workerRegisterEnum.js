const processNameSelect = document.getElementById('processName');
const positionSelect = document.getElementById('position');

function fetchProcessNameDetail(ProcessName){
    fetch('/enums/processNameEnum')
        .then(response => response.json())
        .then(processNameEnums => {
            const processNameSelect = document.getElementById('processName');
            // 수정, 디테일 select 태그 추가에도 option을 넣어줘야함.
            processNameSelect.innerHTML = '<option value="" disabled selected>공정 선택</option>';
            processNameEnums.forEach(name => {
                const option = document.createElement('option');
                option.value = name;
                option.textContent = name;
                processNameSelect.appendChild(option);
            });
            if(ProcessName){
                processNameSelect.value = ProcessName;
            }
        })
        .catch(error => console.error('Error fetching process types:', error));

}


// AJAX 요청으로 enum 값들을 가져와서 select 요소에 추가
function fetchWorkShiftEnum(SelectWorkShift) {
    fetch('/enums/WorkShiftEnum')
        .then(response => response.json())
        .then(workShiftEnums => {


            const workShiftSelect = document.getElementById('workShift');
            workShiftSelect.innerHTML = '';
            workShiftEnums.forEach(name => {
                const option = document.createElement('option');
                option.value = name;
                option.textContent = name;
                workShiftSelect.appendChild(option);
            });
            if (SelectWorkShift) {
                workShiftSelect.value = SelectWorkShift;
            }
        })
        .catch(error => console.error('Error fetching process types:', error));
}





function fetchPositionEnum(SelectPosition) {
    fetch('/enums/positionEnum')
        .then(response => response.json())
        .then(positionEnums => {
            const positionSelect = document.getElementById('position');
            positionSelect.innerHTML = '';
            positionEnums.forEach(name => {
                const option = document.createElement('option');
                option.value = name;
                option.textContent = name;
                positionSelect.appendChild(option);
            });
            if(SelectPosition){
                positionSelect.value = SelectPosition;
            }
        })
        .catch(error => console.error('Error fetching process types:', error));
}
fetch(`/enums/positionEnum`)
    .then(response => response.json())
    .then(positionEnums => {
        positionEnums.forEach(name => {
            const option = document.createElement('option');
            option.value = name;
            option.textContent = name;
            positionSelect.appendChild(option);
        });
    })
    .catch(error => console.error('Error fetching process types:', error));

const position =document.getElementById('position');
