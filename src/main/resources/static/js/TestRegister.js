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
        alert('모든 내용을 채워주세요.ex)생산설비,호기,공정원');
        console.log("processName : ",processName);
        console.log("equipmentName : ",equipmentName);
        console.log("processWorker : ",processWorker);
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

        lastTd.style.fontWeight='bold';
        lastTd.style.textAlign='center';


        lastTd.addEventListener('click', function () {
            if (checkItemTd.textContent === "기준홀") {
                openKeyboardForStandard(this);
            } else {
                // "기준홀"이 아닌 경우 클릭으로 상태 토글
                toggleResultState(this);
            }
        });
    });

    // 생산량, 불량 수량 입력 필드에 대한 처리
    var countInputs = ['productionCount', 'defectCount'];
    countInputs.forEach(function(inputId) {
        var inputElement = document.getElementById(inputId);
        inputElement.addEventListener('focus', function() {
            openKeyboardForCount(inputElement);
        });
    });
});

// 상태 토글 로직을 함수로 분리
function toggleResultState(cell) {
    if (cell.textContent.trim() === 'OK') {
        cell.textContent = 'NG';
        cell.style.backgroundColor = 'red';
        cell.style.color = 'white';
    } else {
        cell.textContent = 'OK';
        cell.style.backgroundColor = 'green';
        cell.style.color = 'white';
    }
    cell.style.fontWeight = 'bold';
    cell.style.textAlign = 'center';
}
