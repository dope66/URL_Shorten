function uploadFile(fileInputId, uploadType) {
    var fileInput = document.getElementById(fileInputId);
    var file = fileInput.files[0];

    if (!file || !file.name.match(/\.(xlsx|xls)$/)) {
        alert('지원하지 않는 파일 형식입니다. .xlsx 또는 .xls 파일만 업로드 가능합니다.');
        return;
    }

    var formData = new FormData();
    formData.append('file', file);
    formData.append('type', uploadType);

    var xhr = new XMLHttpRequest();
    xhr.open('POST', '/api/excel/upload', true);

    xhr.upload.onprogress = function(e) {
        if (e.lengthComputable) {
            var percentComplete = (e.loaded / e.total) * 100;
            document.getElementById('progressBar').style.width = percentComplete + '%';
            document.getElementById('statusText').innerText = '업로드 중... ' + Math.round(percentComplete) + '%';
        }
    };

    xhr.onload = function() {
        if (xhr.status === 200) {
            alert('파일 업로드 성공.');
            document.getElementById('statusText').innerText = '업로드 성공! 페이지를 새로 고침합니다.';
            window.location.reload();
        } else {
            alert('파일 업로드 실패: ' + xhr.statusText);
            document.getElementById('statusText').innerText = '업로드 실패.';
        }
    };

    xhr.onerror = function() {
        alert('업로드 중 네트워크 오류가 발생했습니다.');
        document.getElementById('statusText').innerText = '업로드 실패 - 네트워크 오류.';
    };

    xhr.send(formData);
}
