document.addEventListener('DOMContentLoaded', function() {
    const npwInput = document.getElementById('npw');
    const npw2Input = document.getElementById('npw2');
    const npwmsg = document.getElementById('npwmsg');
    const pwmsg2 = document.getElementById('pwmsg2');

    function validatePasswords() {
        const npw = npwInput ? npwInput.value : '';
        const npw2 = npw2Input ? npw2Input.value : '';

        const type = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,20}$/;

        if (npw === '') {
            if (npwmsg) npwmsg.textContent = '';
            if (pwmsg2) pwmsg2.textContent = '';
        } else if (npw.length < 8) {
            if (npwmsg) {
                npwmsg.textContent = "8자 이상을 입력해주세요.";
                npwmsg.style.color = 'red';
            }
            if (pwmsg2) pwmsg2.textContent = '';
        } else if (!type.test(npw)) {
            if (npwmsg) {
                npwmsg.textContent = "영문과 숫자, 특수문자를 하나 이상 포함해야 합니다.";
                npwmsg.style.color = 'red';
            }
            if (pwmsg2) pwmsg2.textContent = '';
        } else {
            if (npwmsg) {
                npwmsg.textContent = "올바른 형식입니다.";
                npwmsg.style.color = 'green';
            }

            if (npw2 === '') {
                if (pwmsg2) pwmsg2.textContent = '';
            } else if (npw !== npw2) {
                if (pwmsg2) {
                    pwmsg2.textContent = '비밀번호가 일치하지 않습니다.';
                    pwmsg2.style.color = 'red';
                }
            } else {
                if (pwmsg2) {
                    pwmsg2.textContent = '비밀번호가 일치합니다.';
                    pwmsg2.style.color = 'green';
                }
            }
        }
    }

    if (npwInput) npwInput.addEventListener('input', validatePasswords);
    if (npw2Input) npw2Input.addEventListener('input', validatePasswords);

    const submitButton = document.getElementById('submitChange');
    if (submitButton) {
        submitButton.addEventListener('click', function() {
            const npw = npwInput ? npwInput.value : '';
            const npw2 = npw2Input ? npw2Input.value : '';
            const uid = document.getElementById('uid') ? document.getElementById('uid').value : '';

            if (npw === '' || npw2 === '' || npw !== npw2) {
                alert('새 비밀번호와 새 비밀번호 확인이 일치하지 않거나 비밀번호 형식이 올바르지 않습니다.');
                return;
            }

            const formData = new FormData();
            formData.append('uid', uid);
            formData.append('upw', document.getElementById('upw') ? document.getElementById('upw').value : '');
            formData.append('npw', npw);

            fetch('/user/pwUpdate', {
                method: 'POST',
                body: formData
            })
            .then(response => response.text()) 
            .then(result => {
                if (result === '1') {
                    alert('비밀번호가 변경되었습니다.');
                    window.close();
                } else {
                    alert('현재 비밀번호가 일치하지 않습니다.');
                }
            })
            .catch(error => {
                alert('비밀번호 변경 중 오류가 발생했습니다.');
                console.error('Error:', error);
            });
        });
    } else {
        console.error('Submit button not found!');
    }
});
