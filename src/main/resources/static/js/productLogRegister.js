const registryForm = document.getElementById("product-log-form");

// 생산일지 등록 폼 제출 시
registryForm.addEventListener('submit', (event) => {
    event.preventDefault();
    const workDate = document.getElementById('workDate').value;
    const productionTypeSelect = document.getElementById("productionTypeSelect").value;
    let productionType;

    productionType = productionTypeSelect;

    // 여기서 선택된 생산 유형을 사용하여 필요한 작업을 수행합니다.
    console.log("선택된 생산 유형:", productionType);

    const productionName = document.getElementById('productionName').value;
    const productionNumber = document.getElementById('productionNumber').value;
    const production = document.getElementById('production').value;
    const workerName = document.getElementById('workerName').value;

    const newProductLog = {workDate, productionType, productionName, productionNumber, production, workerName};
    console.log(newProductLog);

    const confirmed = window.confirm('정말로 등록하시겠습니까?');
    if (confirmed) {
        // 확인을 선택한 경우에만 등록 진행
        fetch('/api/mes/register', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(newProductLog)
        })
            .then(response => {
                if (response.ok) {
                    alert('등록이 완료되었습니다.');
                    window.close();
                }
            })
            .catch(error => {
                console.error('fetch 오류 요청 ', error);
            });
    }
});
