document.addEventListener('DOMContentLoaded', () => {
	const loginForm = document.getElementById('loginForm');
    const loginButton = document.getElementById('login');
    const uidInput = document.getElementById('uid');
    const upwInput = document.getElementById('upw');

    const handleKeyPress = (event) => {
        if (event.key === 'Enter') {
            event.preventDefault();
            loginButton.click();
        }
    };

    uidInput.addEventListener('keydown', handleKeyPress);
    upwInput.addEventListener('keydown', handleKeyPress);

    loginButton.addEventListener('click', (e) => {
		e.preventDefault();
		
        const uid = uidInput.value.trim();
        const upw = upwInput.value.trim();

        if (!uid) {
            alert("아이디를 입력해주세요.");
            uidInput.focus();
            return;
        }

        if (!upw) {
            alert("비밀번호를 입력해주세요.");
            upwInput.focus();
            return;
        }

		const formData = new FormData(loginForm);
        
		console.log("로그인 시도");
		fetch('/login', {
		            method: 'POST',
		            body: formData,
					credentials: 'include'
		        })
		        .then(res => {
					
		            if (res.status === 401) {
		                throw new Error("아이디 또는 비밀번호가 일치하지 않습니다.");
		            }

		            const token = res.headers.get('Authorization');
		            if (!token) {
		                throw new Error("토큰이 존재하지 않습니다.");
		            }

					alert("accessToken "+token);
					console.log("accessToken" +token);
					
		            // 로컬스토리지에 토큰 저장
		            localStorage.setItem('accessToken', token);

		            // 예: 메인 페이지 이동
		            window.location.href = '/';
		        })
		        .catch(err => {
					console.log("에러");
		            alert(err.message || '로그인 중 오류가 발생했습니다.');
		            console.error('Login Error:', err);
		        });
    });

    const setupSnsLogin = (elementId, apiEndpoint) => {
        const btn = document.getElementById(elementId);
        if (!btn) return;
		localStorage.setItem('accessToken', 'snstoken'); // 임시토큰, 소셜로그인에서는 토큰 발급을 하지않기 때문. 소셜로그인 jwt 구현해야됨
		
        let popupWindow = null;

        btn.addEventListener('click', () => {
            fetch(apiEndpoint, { method: 'POST' })
                .then(res => {
                    if (!res.ok) throw new Error(`서버 오류: ${res.status}`);
                    return res.text();
                })
                .then(url => {
                    if (!url) throw new Error('SNS 로그인 URL 없음');
					
					

                    const width = 771;
                    const height = 710;
                    const left = (window.innerWidth - width) / 2;
                    const top = (window.innerHeight - height) / 2;
                    const options = `width=${width},height=${height},left=${left},top=${top}`;

                    if (popupWindow && !popupWindow.closed) {
                        popupWindow.focus();
                        popupWindow.location.href = url;
                    } else {
                        popupWindow = window.open(url, '_blank', options);
                    }
                })
                .catch(err => {
                    console.error(`${elementId} 로그인 오류:`, err);
                });
        });
    };

    setupSnsLogin('kakao', '/kakaoapi');
    setupSnsLogin('naver', '/naverapi');
});
