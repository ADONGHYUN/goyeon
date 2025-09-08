window.onload = function() {
    tinymce.init({
        selector: '#content',
        plugins: 'image code', // 이미지 업로드 및 삽입, code는 코드확인용
        toolbar: 'undo redo | link image | code', // 각종 기능들 적용
        images_upload_url: '/board/uploadImage', // 이미지 업로드할 서버 URL
        automatic_uploads: true, // true면 에디터에 삽입시 자동 업로드
        file_picker_types: 'image', // 이미지 파일만 선택 가능
        images_reuse_filename: true, // true로 설정하면 업로드된 이미지 파일의 원래 이름이 유지됨
        images_upload_handler: function (blobInfo, success, failure) {
            const xhr = new XMLHttpRequest(); // XMLHttpRequest 객체 생성
            const formData = new FormData(); // FormData 객체 생성
            xhr.withCredentials = false; // 인증 없이 요청
            xhr.open('POST', '/board/uploadImage'); // POST 요청 설정

            xhr.onload = function() {
                if (xhr.status !== 200) {
                    failure('HTTP Error: ' + xhr.status); // 실패 시 에러 메시지
                    return;
                }

                try {
                    const json = JSON.parse(xhr.responseText); // 서버 응답을 JSON으로 파싱
                    console.log("json::", json); // 응답 확인을 위한 로그 출력

                    // JSON 응답 확인
                    if (!json || typeof json.location !== 'string') {
                        failure('Invalid JSON: ' + xhr.responseText);
                        return;
                    }

                    // 성공 시 이미지 URL을 에디터에 삽입
                    success(json.location);
                    console.log("Image URL inserted: ", json.location); // 삽입된 이미지 URL 확인
                } catch (e) {
                    failure('Invalid JSON: ' + xhr.responseText); // JSON 파싱 에러 처리
                }
            };

            formData.append('file', blobInfo.blob(), blobInfo.filename()); // 폼 데이터에 파일 추가
            xhr.send(formData); // 서버로 폼 데이터 전송
        }
    });
};
