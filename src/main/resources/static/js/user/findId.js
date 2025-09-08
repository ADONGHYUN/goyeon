let conf = '';
		let cemail = '';
		$("#mailForm").submit(function(event) {
			event.preventDefault();
			let uemail = $('#uemail').val();
			if (uemail == '') {
				alert('이메일 주소를 입력해주세요.');
			} else {

				$.ajax({
					url : 'findGetMail',
					type : 'post',
					data : {
						uemail : uemail
					},
					success : function(data) {

						if (data == 'api') {
							alert('소셜 가입 회원입니다. 소셜 로그인으로 로그인 해주세요.');
							location.href = '/login';
						} else if (data == 'yes') {
							alert('인증번호가 발송되었습니다. 인증번호의 유효시간은 10분입니다.');
							$('#code').focus();
							conf = '1';
							cemail = uemail;
						} else if (data == 'yesb') {
							//alert('인증번호 발송 but 인증번호 저장 실패.');
							alert('인증번호 발급 과정에서 오류가 발생했습니다. 다시 발급해주세요.');
							conf = '';
							cemail = '';
						} else if (data == 'no') {
							alert('가입되지 않은 이메일입니다.');
							$('#uemail').val('');
							conf = '';
							cemail = '';
						} else {
							alert('예상치 못한 에러 발생.');
							conf = '';
							cemail = '';
						}
					},
					error : function(xhr, status, error) {
						console.error('AJAX request failed: ', status, error);
					}
				});
			}
		});

		$("#codeForm").submit(function(event) {
			event.preventDefault();
			let mailcode = $('#code').val();
			let uemail = cemail;

			if (conf == '') {
				alert('인증번호를 발급받아주세요.');
			} else if (mailcode == '') {
				alert('인증번호를 입력해주세요.');
			} else {
				$.ajax({
					url : 'findId',
					type : 'post',
					contentType : 'application/json',
					data : JSON.stringify({
						uemail : uemail,
						mailcode : mailcode
					}),
					success : function(data) {

						if (data == 'yes') {
							alert('인증이 완료되었습니다.');
							$('#result').val(uemail);

							$('#resultForm').submit();

						} else if (data == 'no') {
							alert('인증번호가 일치하지 않습니다.');
						} else if (data == 'exp') {
							alert('인증번호가 만료되었습니다. 다시 발급해주세요.');
							$('#code').val('');
						} else {
							alert('예상치 못한 에러.');
						}
					},
					error : function(xhr, status, error) {
						console.error('AJAX request failed: ', status, error);
					}
				});
			}
		});