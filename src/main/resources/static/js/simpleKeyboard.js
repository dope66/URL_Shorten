var currentInputTarget; // 현재 입력 대상을 저장하기 위한 변수

function openKeyboardForStandard(cell) {
    currentInputTarget = { type: 'standard', cell: cell };
    document.getElementById("modalInputValue").value = ""; // 모달 입력 필드 초기화
    openKeyboard(); // 모달 열기
}

function openKeyboardForCount(inputElement) {
    currentInputTarget = { type: 'count', inputElement: inputElement };
    document.getElementById("modalInputValue").value = inputElement.value; //기존 값으로 모달 입력 필드 초기화
    openKeyboard(); // 모달 열기
}

function pressKey(key) {
    var inputField = document.getElementById("modalInputValue");
    var currentValue = inputField.value; // 현재 모달 입력 필드의 값을 가져옵니다.

    if (key === "OK") {
        if(currentValue.endsWith(".")){
            currentValue+='0';
        }
        // 입력 대상에 따른 로직 분기
        if (currentInputTarget.type === 'standard') {
            var standardText = currentInputTarget.cell.parentNode.cells[2].textContent;
            var standardValue = parseFloat(standardText.match(/[\d\.]+/)[0]);
            var userInput = parseFloat(currentValue);

            if (!isNaN(userInput) && userInput === standardValue) {
                currentInputTarget.cell.textContent = 'OK';
                currentInputTarget.cell.style.backgroundColor = 'green';
                currentInputTarget.cell.style.color = 'white';
            } else {
                currentInputTarget.cell.textContent = 'NG';
                currentInputTarget.cell.style.backgroundColor = 'red';
                currentInputTarget.cell.style.color = 'white';
            }
        } else if (currentInputTarget.type === 'count') {
            // 생산량 또는 불량 수량 입력 처리
            currentInputTarget.inputElement.value = currentValue;
        }
        closeKeyboard(); // 모달 닫기
    } else if (key === "Cancel") {
        closeKeyboard(); // 모달 닫기
    } else if (key === "Backspace") {
        inputField.value = currentValue.slice(0, -1); // 마지막 문자 제거
    } else if (key === '.') {
        if (!currentValue) {
            // 현재 값이 비어있으면 "0."으로 시작
            inputField.value = '0.';
        } else if (!currentValue.includes('.')) {
            // 현재 값에 "."이 포함되어 있지 않으면 "." 추가
            inputField.value += '.';
        }
        // 이미 "."이 포함되어 있으면 아무 작업도 수행하지 않음
    }  else {
        inputField.value += key; // 숫자 추가
    }
}

function openKeyboard() {
    var modal = document.getElementById("keyboardModal");
    modal.style.display = "block";
}

function closeKeyboard() {
    var modal = document.getElementById("keyboardModal");
    modal.style.display = "none";
}
