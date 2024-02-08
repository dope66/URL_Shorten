window.addEventListener('DOMContentLoaded', () => {
    // productionType select 요소
    const productionTypeSelect = document.getElementById('productionType');

    // productName select 요소
    const productionNumberSelect = document.getElementById('productionNumber');
    const productionNameSelect = document.getElementById('productionName');

    // productionType 값이 변경될 때 productName을 가져와서 옵션으로 설정
    productionTypeSelect.addEventListener('change', () => {
        const selectedProductionType = productionTypeSelect.value;

        // 서버에서 해당 productionType에 해당하는 productName 가져오기
        fetch(`/api/mes/getProductionNumber?productionType=${selectedProductionType}`)
            .then(response => response.json())
            .then(productionNumbers => {
                // 이전에 추가된 옵션 제거
                productionNumberSelect.innerHTML = '';

                // 가져온 productName들을 옵션으로 추가
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
    });

    productionNumberSelect.addEventListener('change', () => {
        const selectedProductionType = productionTypeSelect.value;
        fetch(`/api/mes/getProductionName?productionType=${selectedProductionType}`)
            .then(response => response.json())
            .then(productionNames => {
                productionNameSelect.innerHTML = '';

                productionNames.forEach(productionName => {
                    const option = document.createElement('option');
                    option.text = productionName;
                    option.value = productionName;
                    productionNameSelect.appendChild(option);
                });
            })
            .catch(error => {
                console.error('fetch 오류 요청 ', error);
            });

    });

});