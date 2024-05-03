document.addEventListener('DOMContentLoaded', function() {
    var keyboardzone = document.getElementById("keyboardzone");
    var currentInput;

    // 포커스 이벤트 리스너를 등록하는 함수
    function addFocusListener(inputId) {
        var inputElement = document.getElementById(inputId);
        inputElement.addEventListener("focus", function() {
            currentInput = this; // 현재 포커스된 input 태그를 저장
            keyboardzone.style.display = "block"; // 키보드를 보이게 합니다
            keyboard.setInput(this); // 현재 포커스된 input 태그를 커스텀 키보드의 입력 대상으로 설정
        });
    }

    // "company"와 "note" 입력 필드에 대한 포커스 리스너 등록
    addFocusListener("company");
    addFocusListener("note");

    // 키보드 닫기 로직 (예: ESC 키 누름, 키보드 외부 클릭)
    document.addEventListener("keydown", function(e) {
        if(e.key === "Escape") {
            keyboardzone.style.display = "none"; // 키보드 숨김
        }
    });

    // document.addEventListener("click", function(e) {
    //     if (!keyboardzone.contains(e.target) && !currentInput.contains(e.target)) {
    //         keyboardzone.style.display = "none"; // 키보드 숨김
    //     }
    // });
    document.addEventListener("click", function(e) {
        // 모달이나 현재 포커스된 입력 필드가 아닌 곳을 클릭했을 경우에만 모달을 숨김
        if (!keyboardzone.contains(e.target) && currentInput && !currentInput.contains(e.target)) {
            keyboardzone.style.display = "none";
        }
    });

    // 필요에 따라 커스텀 키보드를 숨기는 코드 추가
});
function customKeyboard(zone, input, onClick, onESC, onEnter, form) {
    /*
        zone : 생성될 위치
        input : 입력할 변수
        onClick : 키보드가 눌렸을때 동작
        onESC : 뒤로 눌렸을때 동작
        form : 키보드의 모습
    */
    function hideModal() {
        keyboardzone.style.display = "none"; // 'keyboardzone'은 화상 키보드 모달의 ID입니다.
    }

    var nowlang = "koNormal";
    this.setClick = function(newclick) {
        onClick = newclick;
    }
    this.setEnter = function(Enterfun) {
        onEnter = Enterfun;
    }
    this.setZone = function(newZone) {
        zone = newZone;
    };
    var charlist = [];
    this.setInput = function(inputtag) {
        input = inputtag;
        var sub = Hangul.disassemble("" + input.value);
        charlist = sub;
    }
    function getText() {
        return Hangul.assemble(charlist);
    }
    if(form == null) {
        form = {
            koNormal : [
                ['뒤로','1','2','3','4','5','6','7','8','9','0', 'backspace'],
                ['ㅂ', 'ㅈ', 'ㄷ', 'ㄱ', 'ㅅ', 'ㅛ', 'ㅕ', 'ㅑ', 'ㅐ', 'ㅔ'],
                ['ㅁ','ㄴ','ㅇ','ㄹ','ㅎ','ㅗ','ㅓ','ㅏ','ㅣ', 'enter'],
                ['shift','ㅋ','ㅌ','ㅊ','ㅍ','ㅠ','ㅜ','ㅡ','한/영'],
                ["space"]
            ],
            koShift : [
                ['뒤로','!','@','#','$','%','^','&','*','(',')', 'backspace'],
                ['ㅃ', 'ㅉ', 'ㄸ', 'ㄲ', 'ㅆ', 'ㅛ', 'ㅕ', 'ㅑ', 'ㅒ', 'ㅖ'],
                ['ㅁ','ㄴ','ㅇ','ㄹ','ㅎ','ㅗ','ㅓ','ㅏ','ㅣ', 'enter'],
                ['shift','ㅋ','ㅌ','ㅊ','ㅍ','ㅠ','ㅜ','ㅡ','한/영'],
                ["space"]
            ],
            enNormal : [
                ['뒤로','1','2','3','4','5','6','7','8','9','0', 'backspace'],
                ['q','w','e','r','t','y','u','i','o','p'],
                ['a','s','d','f','g','h','j','k','l','enter'],
                ['shift','z','x','c','v','b','n','m','한/영'],
                ["space"]
            ],
            enShift : [
                ['뒤로','!','@','#','$','%','^','&','*','(',')', 'backspace'],
                ['Q','W','E','R','T','Y','U','I','O','P'],
                ['A','S','D','F','G','H','J','K','L','enter'],
                ['shift','Z','X','C','V','B','N','M','한/영'],
                ["space"]
            ]
        }
    }
    var keydiv = {};
    for (let index = 0; index < Object.keys(form).length; index++) {
        keydiv[Object.keys(form)[index]] = document.createElement("div")
        keydiv[Object.keys(form)[index]].style.cssText = `
            position: absolute;
            width : 100%;
            height: 100%;
            align : center;
            visibility: hidden;
            font-size: 25px;
        `
        for (let i = 0; i < form[Object.keys(form)[index]].length; i++) {
            var keyline = document.createElement("table");
            keyline.style.cssText = `
                width : 100%;
                height: calc(100% / ` + form[Object.keys(form)[index]].length + `);
            `
            for (let j = 0; j < form[Object.keys(form)[index]][i].length; j++) {
                var key = document.createElement("th")
                key.style.cssText = `
                    padding-top : calc((100% / ` + form[Object.keys(form)[index]].length + `) / 5);
                `
                key.innerText = form[Object.keys(form)[index]][i][j];
                key.addEventListener("click", keyfun)
                keyline.appendChild(key);
            }
            keydiv[Object.keys(form)[index]].appendChild(keyline);
        }
        zone.appendChild(keydiv[Object.keys(form)[index]])
    }
    keydiv[nowlang].style.visibility = "visible";
    function keyfun() {
        if(this.innerText == '뒤로') {
            onESC();
            hideModal(); // '뒤로' 버튼을 눌렀을 때 모달 숨김
            return;
        } else if(this.innerText == 'enter') {
            onEnter(getText());
            hideModal(); // '뒤로' 버튼을 눌렀을 때 모달 숨김
            return
        } else if(this.innerText == '한/영') {
            keydiv[nowlang].style.visibility = "hidden";
            if(nowlang == "koNormal") {
                nowlang = "enNormal"
            }
            else if(nowlang == "enNormal") {
                nowlang = "koNormal"
            }
            else if(nowlang == "koShift") {
                nowlang = "enShift"
            }
            else if(nowlang == "enShift") {
                nowlang = "koShift"
            }
            keydiv[nowlang].style.visibility = "visible";
            return
        }
        else if(this.innerText == 'shift') {
            keydiv[nowlang].style.visibility = "hidden";
            if(nowlang == "koNormal") {
                nowlang = "koShift"
            }
            else if(nowlang == "enNormal") {
                nowlang = "enShift"
            }
            else if(nowlang == "koShift") {
                nowlang = "koNormal"
            }
            else if(nowlang == "enShift") {
                nowlang = "enNormal"
            }
            keydiv[nowlang].style.visibility = "visible";
            return
        }
        else if(this.innerText == 'backspace') {
            charlist.splice(charlist.length - 1, 1);
        }
        else if(this.innerText == 'space') {
            charlist.push(" ");
        }
        else {
            charlist.push(this.innerText);
        }

        text = Hangul.assemble(charlist)
        input.value = text;
        if(onClick != null) {
            onClick(getText());
        }
    }


}

var keyboardzone = document.getElementById("keyboardzone");
var input = document.getElementById("myinput");

var keyboard = new customKeyboard(
    keyboardzone/*생성위치 태그*/,
    input/*input을 받을 태그*/,
    function()/*클릭 했을때*/ {
        console.log("click : ", text);
    },
    function()/*esc 눌렀을때*/ {
        console.log("esc");
    },
    function(e)/*앤터 눌렀을때*/ {
        console.log("앤터 : ", text);
    },
    null/*키패드를 모양 값*/
    // {
    //     koNormal : [
    //         ['뒤로','1','2','3','4','5','6','7','8','9','0', 'backspace'],
    //         ['ㅂ', 'ㅈ', 'ㄷ', 'ㄱ', 'ㅅ', 'ㅛ', 'ㅕ', 'ㅑ', 'ㅐ', 'ㅔ'],
    //         ['ㅁ','ㄴ','ㅇ','ㄹ','ㅎ','ㅗ','ㅓ','ㅏ','ㅣ', 'enter'],
    //         ['shift','ㅋ','ㅌ','ㅊ','ㅍ','ㅠ','ㅜ','ㅡ','한/영'],
    //         ["space"]
    //     ],
    //     koShift : [
    //         ['뒤로','!','@','#','$','%','^','&','*','(',')', 'backspace'],
    //         ['ㅃ', 'ㅉ', 'ㄸ', 'ㄲ', 'ㅆ', 'ㅛ', 'ㅕ', 'ㅑ', 'ㅒ', 'ㅖ'],
    //         ['ㅁ','ㄴ','ㅇ','ㄹ','ㅎ','ㅗ','ㅓ','ㅏ','ㅣ', 'enter'],
    //         ['shift','ㅋ','ㅌ','ㅊ','ㅍ','ㅠ','ㅜ','ㅡ','한/영'],
    //         ["space"]
    //     ],
    //     enNormal : [
    //         ['뒤로','1','2','3','4','5','6','7','8','9','0', 'backspace'],
    //         ['q','w','e','r','t','y','u','i','o','p'],
    //         ['a','s','d','f','g','h','j','k','l','enter'],
    //         ['shift','z','x','c','v','b','n','m','한/영'],
    //         ["space"]
    //     ],
    //     enShift : [
    //         ['뒤로','!','@','#','$','%','^','&','*','(',')', 'backspace'],
    //         ['Q','W','E','R','T','Y','U','I','O','P'],
    //         ['A','S','D','F','G','H','J','K','L','enter'],
    //         ['shift','Z','X','C','V','B','N','M','한/영'],
    //         ["space"]
    //     ]
    // }
);

input.addEventListener("click", function() {
    //input 태그를 자신으로 설정
    keyboard.setInput(this);
    //키패드 클릭 이벤트 설정
    keyboard.setClick(function(text) {
        console.log("input을 click한 후 : ", text);
    })
    //앤터 이벤트 설정
    keyboard.setEnter(function(e) {
        menu3searching(e)
    });
})