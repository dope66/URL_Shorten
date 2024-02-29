
function openKeyboardForCount(inputId) {
    currentInputField = document.getElementById(inputId);

    if (inputId === 'productionCount' || inputId === 'defectCount') {
        openKeyboard();
    }
}

var currentInputField;

function pressKey(key) {
    var currentValue = currentInputField.value; // 현재 입력 필드의 값을 가져옵니다.

    if (key === "OK" || key === "Cancel") {
        closeKeyboard();
    } else if (key === "Backspace") {
        currentInputField.value = currentValue.slice(0, -1);
    } else if (key === '.') {
        if (currentValue === '') {
            currentInputField.value = '0.';
        } else if (!currentValue.includes('.')) {
            currentInputField.value += '.';
        }
    } else {
        currentInputField.value += key;
    }
}


function openKeyboard() {
    var modal = document.getElementById("keyboardModal");
    modal.style.display = "block";
}

// JavaScript 코드에 closeKeyboard 함수 추가
function closeKeyboard() {
    var modal = document.getElementById("keyboardModal");
    modal.style.display = "none";
}
