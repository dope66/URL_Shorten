const processNameSelect = document.getElementById('processName');

fetch('/enums/processTypes')
    .then(response => response.json())
    .then(processTypes => {
        processTypes.forEach(name => {
            const option = document.createElement('option');
            option.value = name;
            option.textContent = name;
            processNameSelect.appendChild(option);
        });
    })
    .catch(error => console.error('Error fetching process types:', error));

const workShiftSelect = document.getElementById('workShift');
// AJAX 요청으로 enum 값들을 가져와서 select 요소에 추가
fetch('/enums/WorkShiftEnum')
    .then(response => response.json())
    .then(workShiftEnums => {
        workShiftEnums.forEach(name => {
            const option = document.createElement('option');
            option.value = name;
            option.textContent = name;
            workShiftSelect.appendChild(option);
        });
    })
    .catch(error => console.error('Error fetching process types:', error));
