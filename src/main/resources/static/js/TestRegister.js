// // 초중물 검사 등록 기능
// const productionTestForm = document.getElementById("production-test-form");
function registTest() {
    const processName = document.getElementById('processName').value;
    const equipmentName = document.getElementById("equipmentName").value;
    const processWorker = document.getElementById('processWorker').value;
    const note = document.getElementById('note').value;
    const company = document.getElementById('company').value;
    const productionCount = document.getElementById('productionCount').value;
    const defectCount = document.getElementById('defectCount').value;

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
document.addEventListener('DOMContentLoaded', function() {
    var rows = document.querySelectorAll('#test-note tbody tr');

    rows.forEach(function(row) {
        var lastTd = row.querySelector('td:last-child');

        lastTd.addEventListener('click', function() {
            if (this.textContent === 'OK') {
                this.textContent = 'NG';
                this.style.color = 'red';
            } else {
                this.textContent = 'OK';
                this.style.color = 'green';
            }
            this.style.fontWeight = 'bold';
            this.style.textAlign = 'center';
        });
    });
});
