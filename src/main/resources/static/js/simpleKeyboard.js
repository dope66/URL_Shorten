let keyboard; // 가상 키보드 객체를 전역 변수로 선언

function toggleVirtualKeyboard() {
    let Keyboard = window.SimpleKeyboard.default;

    // 가상 키보드가 이미 생성되었는지 확인하고, 생성되지 않았다면 생성
    if (!keyboard) {
        keyboard = new Keyboard({
            onChange: input => onChange(input),
            onKeyPress: button => onKeyPress(button)
        });

        // Update simple-keyboard when input is changed directly
        document.querySelector(".input").addEventListener("input", event => {
            keyboard.setInput(event.target.value);
        });
    }

    // 가상 키보드를 보이도록 설정
    document.querySelector(".simple-keyboard").classList.remove("keyboard-closed");
}

function closeKeyboard() {
    // 가상 키보드를 화면에서 숨기도록 설정
    document.querySelector(".simple-keyboard").classList.add("keyboard-closed");
}

function onChange(input) {
    document.querySelector(".input").value = input;
    console.log("Input changed", input);
}

function onKeyPress(button) {
    console.log("Button pressed", button);

    if (button === "{shift}" || button === "{lock}") handleShift();
}

function handleShift() {
    let currentLayout = keyboard.options.layoutName;
    let shiftToggle = currentLayout === "default" ? "shift" : "default";

    keyboard.setOptions({
        layoutName: shiftToggle
    });
}