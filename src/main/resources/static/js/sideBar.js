document.addEventListener('DOMContentLoaded', function() {
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
            const targetMenuId = e.target.getAttribute('data-target') || e.target.parentElement.getAttribute('data-target'); // 대상 메뉴 ID를 얻음
            const targetMenu = document.getElementById(targetMenuId); // 해당 ID로 요소를 찾음

            // 모든 메뉴 숨김
            document.querySelectorAll('.side-menu').forEach(function(menu) {
                if(menu.id !== targetMenuId) {
                    menu.style.display = "none";
                }
            });

            // 선택된 메뉴 토글
            if(targetMenu && targetMenu.style.display === "block") {
                targetMenu.style.display = "none";
            } else if(targetMenu) {
                targetMenu.style.display = "block";
            }
        });
    });
});