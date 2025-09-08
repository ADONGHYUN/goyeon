	document.addEventListener('DOMContentLoaded', function() {
	    const upw = document.getElementById('upw');
	    const pwmsg2 = document.getElementById('pwmsg');
	    const agreeTerms = document.getElementById('agreeTerms');
	    const deleteA = document.getElementById('delete');
	    let dccon;
	    
	    window.addEventListener('message', function(event) {
	    	dccon = event.data;
	        if (dccon === true) {
	        	document.querySelector('label[for="upw"]').classList.add('d-none');
	            upw.classList.add('d-none');
	        }
	    });
	    
	    deleteA.addEventListener('click', function() {
	        if (agreeTerms.checked) {
	            if (!dccon && (upw.value === '')) {
	                pwmsg2.textContent = '비밀번호를 입력해주세요.';
	            } else {           
	                const formData = new FormData(document.getElementById('deleteForm'));
					if(dccon){
						formData.set("upw", "0000");
					}
	                fetch('/user/delete', {
	                    method: 'POST',
	                    body: formData
	                })
	                .then(response => response.text())
	                .then(result => {
	                	console.log("result:" +result);
	                    if (result === '1') {
	                        alert('탈퇴가 완료되었습니다.\n다음에 또 찾아주시면 더 좋은 서비스로 보답하겠습니다.');
	                        if (window.opener) {
	                            window.opener.location.href = '/';
	                        }
	                        window.close();
	                    } else if(result === '0'){
	                        alert('비밀번호가 일치하지 않습니다.');
	                    }else if(result === '2'){
	                    	alert('탈퇴 중 오류가 발생했습니다. 관리자에게 문의해주세요.');
	                    }
	                })
	                .catch(error => {
	                    alert('탈퇴 중 오류가 발생했습니다.');
	                    console.error('Error:', error);
	                });
	            }
	        } else {
	            alert("탈퇴 약관에 동의하지 않으면 탈퇴하실 수 없습니다.");
	        }
	    });
	});

