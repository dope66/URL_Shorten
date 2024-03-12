document.addEventListener('DOMContentLoaded', function() {

    console.log('DOMContentLoaded event fired!');
    // 기존 코드 유지
    const processEquipmentSelect = document.getElementById('processEquipmentSelect');
    const productionOrderLink = document.getElementById('productionOrderLink');
    let selectedProcessName = '';
    let selectedEquipmentName = '';

    fetch(`/api/worker/getProcessAndEquipment`)
        .then(response => response.json())
        .then(processAndEquipment => {
            processAndEquipment.forEach(item => {
                const value = item.replace(' ', '-');
                const option = new Option(item, value);
                processEquipmentSelect.add(option);
            });
        });

    processEquipmentSelect.addEventListener('change', function() {
        const selectedValue = this.value.split('-');
        selectedProcessName = encodeURIComponent(selectedValue[0]);
        selectedEquipmentName = encodeURIComponent(selectedValue[1] || '');
    });

    productionOrderLink.addEventListener('click', function(e) {
        e.preventDefault();
        if (!selectedProcessName || !selectedEquipmentName) {
            alert('공정과 설비를 선택해주세요.');
            return;
        }
        window.location.href = `/mes/productionOrder?processName=${selectedProcessName}&equipmentName=${selectedEquipmentName}`;
    });

    // 메뉴 토글 기능 개선
    document.querySelectorAll('.toggle-menu').forEach(function(menu) {
        menu.addEventListener('click', function(e) {
            e.preventDefault();
            let targetMenu = e.currentTarget.nextElementSibling; // 현재 클릭된 요소의 바로 다음 요소 선택

            // 모든 메뉴 숨김, 현재 메뉴는 제외
            document.querySelectorAll('.side-menu').forEach(function(menu) {
                if (menu !== targetMenu) {
                    menu.style.display = 'none';
                }
            });

            // 현재 메뉴 토글
            targetMenu.style.display = targetMenu.style.display === 'block' ? 'none' : 'block';
        });
    });

});