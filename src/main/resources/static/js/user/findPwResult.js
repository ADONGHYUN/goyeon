function formReset(){
	document.getElementById('cngPwForm').reset();
	$('#idmsg').text('');
	$('#pwmsg').text('');
	$('#pwmsg2').text('');
}

		$('#upw').on('input', function() {
	       	let upw = $(this).val();
	       	let upw2 = $('#upw2').val();
	       	let msg = '';
	       	let msg2 = '';

	       	let type = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[@#^$!%*?&])[A-Za-z\d@#^$!%*?&]+$/;
	       	
	       	if(upw == ''){
	       		msg = '';
	       	 	$("#pwmsg").css("color", "black");
	       	}else if (upw.length < 8 || upw.length > 20) {
	            msg = "8자 이상을 입력해주세요.";
	            $("#pwmsg").css("color", "red");
	        }else if (!type.test(upw)) {
	            msg = "영문과 숫자, 특수문자를 하나 이상 포함해야 합니다.";
	            $("#pwmsg").css("color", "red");
	        }else{
	        	msg = "올바른 형식입니다.";
	        	$("#pwmsg").css("color", "green");
	        }
	        $('#pwmsg').text(msg);
	        
	        if(upw2 == ''){
	       		msg = '';
	       	}else if(upw != upw2){
	       		msg = '비밀번호가 일치하지 않습니다.';
	       		$("#pwmsg2").css("color", "red");
	       		chpw = '';
	       	}else{
	        	msg = "비밀번호가 일치합니다.";
	        	$("#pwmsg2").css("color", "green");
	        	chpw = 'yes';
	        }
	        $('#pwmsg2').text(msg);
	    });
		
		$('#upw2').on('input', function() {
			let upw1 = $('#upw').val();
	       	let upw2 = $(this).val();
	       	let msg = '';
	       		
	       	if(upw2 == ''){
	       		msg = '';
	       	}else if(upw1 != upw2){
	       		msg = '비밀번호가 일치하지 않습니다.';
	       		$("#pwmsg2").css("color", "red");
	       		chpw = '';
	       	}else{
	        	msg = "비밀번호가 일치합니다.";
	        	$("#pwmsg2").css("color", "green");
	        	chpw = 'yes';
	        }
	        $('#pwmsg2').text(msg);
	    });
		
		let chpw='';
		
		$("#cngPwForm").submit(function(event) {
	        event.preventDefault();
	        let uid = '${uid}';
	        let upw = $('#upw').val();
	        let upw2 = $('#upw2').val();
	        if(upw2 == ''){
	        	alert('비밀번호 확인란을 입력해주세요.');
	        	$('#upw2').focus();
	        }else if(chpw == ''){
	        	alert('비밀번호 확인이 일치하지 않습니다.');
	        	$('#upw2').val('');
	        	$('#upw2').focus();
	        }else{
	        	$.ajax({
					url : 'findPwChange',
					type : 'post',
					contentType : 'application/json',
					data : JSON.stringify({
						uid : uid,
						upw : upw
					}),
					success : function(data) {
						if(data == 'yes'){
							alert('비밀번호가 변경되었습니다.');
							window.location.href = '/login';
						}else if(data == 'no'){
							alert('비밀번호 변경에 실패하였습니다.');
						}else{
							alert('예외발생.');
						}
					},
					error : function(xhr, status, error) {
						console.error('AJAX request failed: ', status, error);
					}
				});
	        }
	 	});
		