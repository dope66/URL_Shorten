const processNameSelect = document.getElementById('processName');
const positionSelect = document.getElementById('position');


fetch('/enums/processNameEnum')
    .then(response => response.json())
    .then(processNameEnums => {
        processNameEnums.forEach(name => {
            const option = document.createElement('option');
            option.value = name;
            option.textContent = name;
            processNameSelect.appendChild(option);
        });

    })
    .catch(error => console.error('Error fetching process types:', error));

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
