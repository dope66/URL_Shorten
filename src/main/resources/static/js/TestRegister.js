// // 초중물 검사 등록 기능
// const productionTestForm = document.getElementById("production-test-form");
function registTest() {
    const processName = document.getElementById('processName').value;
    const equipmentName = document.getElementById("equipmentName").value;
    const processWorker = document.getElementById('processWorker').value;
    let note = document.getElementById('note').value;
    const company = document.getElementById('company').value;
    const productionCount = document.getElementById('productionCount').value;
    const defectCount = document.getElementById('defectCount').value;
    note = note.trim() ? note : "없음";
    if (!processName || !equipmentName || !processWorker) {
        alert('모든 필드를 채워주세요.');
        return; // 필수 필드가 비어 있으면 함수 실행을 중단
    }

    const newTest = {processName, equipmentName, processWorker, note, company, productionCount, defectCount};
    console.log("new Test ;", newTest);
    const confirmed = window.confirm('정말로 등록하시겠습니까?');
    if (confirmed) {
        // 확인을 선택한 경우에만 등록 진행
        fetch('/api/Test/productionTest', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(newTest)
        })
            .then(response => {
                if (response.ok) {
                    alert('등록이 완료되었습니다.');
                    window.location.reload();
                }
            })
            .catch(error => {
                console.error('fetch 오류 요청 ', error);
            });
    }

}
document.addEventListener('DOMContentLoaded', function () {
    var rows = document.querySelectorAll('#test-note tbody tr');

    rows.forEach(function (row) {
        var lastTd = row.querySelector('td:last-child');
        var checkItemTd = row.cells[1]; // 검사항목이 있는 td

        lastTd.addEventListener('click', function () {
            // "기준홀" 검사항목인 경우
            if (checkItemTd.textContent === "기준홀") {
                var standardTd = row.cells[2]; // 검사규격이 있는 td
                var standardText = standardTd.textContent; // 예: "&phi; 2.0에 준함"
                var standardValue = parseFloat(standardText.match(/[\d\.]+/)[0]); // 규격에서 숫자 추출

                var userInput = prompt("검사 결과를 입력하세요. (예: 2.0)", "");
                if (userInput !== null) {
                    var userValue = parseFloat(userInput);
                    if (!isNaN(userValue) && userValue === standardValue) {
                        this.textContent = 'OK';
                        this.style.backgroundColor = 'green';
                        this.style.color = 'white';
                    } else {
                        this.textContent = 'NG';
                        this.style.backgroundColor = 'red';
                        this.style.color = 'white';
                    }
                }
            } else {
                // "기준홀"이 아닌 경우 클릭으로 상태 토글
                if (this.textContent.trim() === 'OK') {
                    this.textContent = 'NG';
                    this.style.backgroundColor = 'red';
                    this.style.color = 'white';
                } else {
                    this.textContent = 'OK';
                    this.style.backgroundColor = 'green';
                    this.style.color = 'white';
                }
            }
            // 폰트 스타일과 텍스트 정렬 설정
            this.style.fontWeight = 'bold';
            this.style.textAlign = 'center';
        });
    });
});



