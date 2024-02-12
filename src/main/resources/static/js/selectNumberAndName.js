
function toggleProductionType() {
    var productionTypeInput = document.getElementById("productionType");
    var productionTypeSelect = document.getElementById("productionTypeSelect");

    if (productionTypeInput.style.display !== "none") {
        // input 태그를 숨기고 select 태그를 보이게 함
        productionTypeInput.style.display = "none";
        productionTypeSelect.style.display = "block";
        // 변경된 "생산 유형"에 해당하는 데이터를 가져옴
        fetchProductionNumbersAndNames();
        document.getElementById("toggleProductionTypeButton").textContent = "취소";
    } else {
        productionTypeSelect.style.display = "none";
        productionTypeInput.style.display = "block";
        fetchProductionNumbersAndNamesInput();
        document.getElementById("toggleProductionTypeButton").textContent = "선택";
    }
}
/*
* select 일때.
* */
function fetchProductionNumbersAndNames() {
    const selectedProductionType = document.getElementById("productionTypeSelect").value; // 선택된 생산 유형을 가져옵니다.
    const productionNumberSelect = document.getElementById("productionNumber");
    const productionNameSelect = document.getElementById("productionName");



        fetch(`/api/mes/getProductionNumber?productionType=${selectedProductionType}`) // 선택된 생산 유형을 요청 URL에 포함시킵니다.
        .then(response => response.json())
        .then(productionNumbers => {
            productionNumberSelect.innerHTML = '';
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
    productionNumberSelect.addEventListener('change', () =>{
        fetch(`/api/mes/getProductionName?productionNumber=${productionNumberSelect}`)
            .then(response => response.json())
            .then(productionNames => {
                {
                    productionNameSelect.innerHTML = '';
                    console.log("production",productionNumberSelect)
                    productionNames.forEach(productionName => {
                        const option = document.createElement('option');
                        option.text = productionName;
                        option.value = productionName;
                        productionNameSelect.appendChild(option);
                    })
                }
            }).catch(error => {
            console.error('fetch 오류 요청 ', error);
        });
    });

}
/*
* input 일때
* */
function fetchProductionNumbersAndNamesInput(){
    const productionTypeSelect = document.getElementById('productionType');
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
        const selectedProductionNumber = productionNumberSelect.value;
        fetch(`/api/mes/getProductionName?productionNumber=${selectedProductionNumber}`)

            .then(response => response.json())
            .then(productionNames => {
                console.log("production",productionNumberSelect)
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


}

window.addEventListener('DOMContentLoaded', () => {
    fetchProductionNumbersAndNamesInput();
    // 초기에 생산 유형 목록을 가져와서 select 태그의 option에 추가
    const productionTypeSelect = document.getElementById('productionTypeSelect');
    const productionTypeInput =document.getElementById('productionType');
    fetch('/api/mes/getProductionType')
        .then(response => response.json())
        .then(data => {
            console.log("data : ", data);
            // 받은 데이터를 가지고 select 태그의 option을 추가합니다.
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
    productionTypeInput.dispatchEvent(new Event('change'));
    productionTypeSelect.addEventListener('change', () => {
        // input 태그일 때는 fetchProductionNumbersAndNamesInput 호출
        if (productionTypeSelect.style.display === "none") {
            fetchProductionNumbersAndNamesInput();
        } else {
            fetchProductionNumbersAndNames();
        }
    });
});
