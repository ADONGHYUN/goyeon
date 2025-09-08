window.onload = function() {
    tinymce.init({
        selector: '#content',
        plugins: 'image code',
        toolbar: 'undo redo | link image | code',
        images_upload_url: '/board/uploadImage',
        automatic_uploads: true,
        file_picker_types: 'image',
        images_reuse_filename: true,
        images_upload_handler: function (blobInfo, success, failure) {
            const xhr = new XMLHttpRequest();
            const formData = new FormData();
            xhr.withCredentials = false;
            xhr.open('POST', '/board/uploadImage');
            
            xhr.onload = function() {
                if (xhr.status !== 200) {
                    failure('HTTP Error: ' + xhr.status);
                    return;
                }

                const json = JSON.parse(xhr.responseText);

                if (!json || typeof json.location !== 'string') {
                    failure('Invalid JSON: ' + xhr.responseText);
                    return;
                }

                success(json.location);
            };

            formData.append('file', blobInfo.blob(), blobInfo.filename());
            xhr.send(formData);
        }
    });
};