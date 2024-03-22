document.addEventListener('DOMContentLoaded', function() {
    restoreMenuStates();
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
    document.querySelectorAll('.toggle-menu').forEach(function(menu) {
        menu.addEventListener('click', function(e) {
            e.preventDefault();
            let targetMenuId = e.currentTarget.getAttribute('data-target'); // 현재 클릭된 메뉴의 대상 ID를 가져옴
            let targetMenu = document.getElementById(targetMenuId);

            // 메뉴 표시 상태 토글
            targetMenu.style.display = targetMenu.style.display === 'block' ? 'none' : 'block';

            // 변경된 메뉴 상태 저장
            saveMenuState(targetMenuId, targetMenu.style.display === 'block');
        });
    });
    // 메뉴 상태를 localStorage에 저장하는 함수

});
function saveMenuState(menuId, isOpen) {
    sessionStorage.setItem(menuId + '_state', isOpen ? 'open' : 'closed');
}

function restoreMenuStates() {
    document.querySelectorAll('.side-menu').forEach(function(menu) {
        let menuId = menu.getAttribute('id');
        let state = sessionStorage.getItem(menuId + '_state');

        // 저장된 상태가 없는 경우 기본적으로 'open'으로 간주
        if (state === null || state === 'open') {
            menu.style.display = 'block';
        } else if (state === 'closed') {
            menu.style.display = 'none';
        }
    });
}