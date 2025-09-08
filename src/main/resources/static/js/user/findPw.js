let conf = '';
let cid = '';
let chid = '';
let chidf = '';

$('#uid').on('input', function() {
	let uid = $(this).val();
	let msg = '';
	chid = '';
	chidf = '';
	if (uid == '') {
		msg = '';
	}
	else if (/[가-힣]/.test(uid)) {

	}
	// 문자열 길이 체크 (6~12자리)
	else if (uid.length < 6 || uid.length > 12) {

	}
	// 영문을 1자리 이상 포함하는지 체크
	else if (!/(?=.*[A-Za-z])/.test(uid)) {

	}
	// 영문과 숫자만 포함하는지 체크
	else if (!/^[A-Za-z0-9]+$/.test(uid)) {

	} else {

		chidf = 'ok';
	}
});

$("#idForm").submit(function(event) {
	event.preventDefault();
	let uid = $('#uid').val();
	let uemail = $('#uemail').val();
	if (uid == '') {
		alert('아이디를 입력해주세요.');
	} else if (chidf == '') {
		alert('잘못된 아이디입니다.');
		$('#uid').focus();
	} else if (uemail == '') {
		alert('이메일 주소를 입력해주세요.');
		$('#uemail').focus();
	} else {

		$.ajax({
			url: 'findGetFid',
			type: 'post',
			data: {
				uid: uid,
				uemail, uemail
			},
			success: function(data) {
				if (data == 'yes') {
					alert('인증번호가 발송되었습니다. 인증번호의 유효시간은 10분입니다.');
					$('#code').focus();
					conf = '1';
					cid = uid;
				} else if (data == 'yesb') {
					//alert('인증번호 발송 but 인증번호 저장 실패.');
					alert('인증번호 발급 과정에서 오류가 발생했습니다. 다시 발급해주세요.');
					conf = '';
				} else if (data == 'no') {
					alert('가입되지 않은 회원입니다.');
					$('#uid').val('');
					conf = '';
				} else if (data == 'nmail') {
					//alert('인증번호 발송 but 인증번호 저장 실패.');
					alert('메일이 일치하지 않습니다.');
					conf = '';
				} else {
					alert('예상치 못한 에러 발생.');
					conf = '';
				}
			},
			error: function(xhr, status, error) {
				console.error('AJAX request failed: ', status, error);
			}
		});
	}
});

$("#codeForm").submit(function(event) {
	event.preventDefault();
	let mailcode = $('#code').val();
	let uid = cid;
	if (conf == '') {
		alert('인증번호를 발급받아주세요.');
	} else if (mailcode == '') {
		alert('인증번호를 입력해주세요.');
	} else {
		$.ajax({
			url: 'findPw',
			type: 'post',
			contentType: 'application/json',
			data: JSON.stringify({
				uid: uid,
				mailcode: mailcode
			}),
			success: function(data) {
				if (data == 'yes') {
					alert('인증 성공.');

					$('#result').val(uid);

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
			error: function(xhr, status, error) {
				console.error('AJAX request failed: ', status, error);
			}
		});
	}
});