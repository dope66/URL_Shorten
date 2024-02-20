
function openKeyboardForCount(inputId) {
    console.log("openKeyboardForCount 나오는게 맞아?");
    currentInputField = document.getElementById(inputId);
    if (inputId === 'production') {
        openKeyboard();
    }
}
var currentInputField;

function pressKey(key) {
    if (key === "OK") {
         closeKeyboard();
    } else if (key === "Cancel") {
         currentInputField.value = "";
        closeKeyboard();
    } else {
         currentInputField.value += key;
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

