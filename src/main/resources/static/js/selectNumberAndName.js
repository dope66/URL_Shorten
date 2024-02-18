function fetchProductionNumbersAndNames() {
    const selectedProductionType = document.getElementById("productionTypeSelect").value; // 선택된 생산 유형을 가져옵니다.
    console.log("productionType Select : ", selectedProductionType);
    const productionNumberSelect = document.getElementById("productionNumber");
    const productionNameSelect = document.getElementById("productionName");

    // 생산 번호를 초기화합니다.
    productionNumberSelect.innerHTML = '';

    fetch(`/api/mes/getProductionNumber?productionType=${selectedProductionType}`) // 선택된 생산 유형을 요청 URL에 포함시킵니다.
        .then(response => response.json())
        .then(productionNumbers => {
            productionNumbers.forEach(productionNumber => {
                const option = document.createElement('option');
                option.text = productionNumber;
                option.value = productionNumber;
                productionNumberSelect.appendChild(option);
            });
        })
        .catch(error => {
            console.error('fetch 오류 요청 ', error);
        });

    productionNumberSelect.addEventListener('change', () => {
        fetch(`/api/mes/getProductionName?productionNumber=${productionNumberSelect.value}`)
            .then(response => response.json())
            .then(productionName => {
                productionNameSelect.value = productionName;
            }).catch(error => {
            console.error('fetch 오류 요청 ', error);
        });
    });
}

const productionTypeSelect = document.getElementById('productionTypeSelect');
const productionNameSelect = document.getElementById('productionName'); // productionNameSelect 가져오기

fetch('/api/mes/getProductionType')
    .then(response => response.json())
    .then(data => {
        data.forEach(productionType => {
            const option = document.createElement('option');
            option.text = productionType;
            option.value = productionType;
            productionTypeSelect.appendChild(option);
        });
    })
    .catch(error => {
        console.error('fetch 오류 요청 ', error);
    });

productionTypeSelect.addEventListener('change', () => {
    // productionType 변경 시 productionName 값 초기화
    productionNameSelect.value = '';
    fetchProductionNumbersAndNames();
});
