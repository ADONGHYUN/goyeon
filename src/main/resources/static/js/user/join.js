let chid='';
		let chidf='';
		let chpw='';
		let chpwf='';
		let chmail='';
		let chmailf='';
		
		function idCheck(){
			let uid = $('#uid').val();
			if(uid == ''){
				alert('아이디를 입력해주세요.');
			}else if(chidf==''){
				alert('올바른 아이디를 입력해주세요.');
				$('#uid').focus();
			}else{
				console.log(uid);
				
				$.ajax({
					url : 'join/getId',
					type : 'post',
					data : {
						uid : uid
					},
					success : function(data) {
						console.log('Received data:', data.uid);
						if(data == 'yes'){
							alert('중복된 아이디가 있습니다.');
							$('#uid').val('');
							$('#idmsg').text('');
							$('#uid').focus();
							
						}else{
							alert('사용 가능한 아이디입니다.');
							chid="ok";
						}
					},
					error : function(xhr, status, error) {
						console.error('AJAX request failed: ', status, error);
					}
				});
			}
		}
		
		function formReset(){
			document.getElementById('joinForm').reset();
			$('#idmsg').text('');
			$('#pwmsg').text('');
			$('#pwmsg2').text('');
		}
		 
		let debounceTimer;
		$('#uemail').on('input', function() {
		    clearTimeout(debounceTimer);  // 이전 타이머 취소
		    let uemail = $(this).val();
		    
		    debounceTimer = setTimeout(function() {
		        let msg = '';
		        chmail = '';
		        chmailf = '';
		        let emailtype = /^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*$/;
		        
		        if (uemail == '') {
		            msg = '';
		        } else if (!emailtype.test(uemail)) {
		            msg = "잘못된 이메일 형식입니다.";
		            $("#mailmsg").css("color", "red");
		        } else {
		            $.ajax({
		                url: '/user/join/getMail',
		                type: 'post',
		                data: { uemail: uemail },
		                success: function(data) {
		                    if (data == 'yes') {
		                        msg = "이미 가입된 이메일입니다.";
		                        $("#mailmsg").css("color", "red");
		                    } else if (data == 'no') {
		                        msg = "가입 가능한 이메일입니다.";
		                        $("#mailmsg").css("color", "green");
		                        chmail = 'ok';
		                    } else {
		                        alert('예상치 못한 에러.');
		                    }
		                    $('#mailmsg').text(msg);
		                },
		                error: function(xhr, status, error) {
		                    console.error('AJAX request failed: ', status, error);
		                }
		            });
		        }
		        $('#mailmsg').text(msg);
		    }, 1000);  // 1000ms 대기 후 요청 전송
		});

		
			$('#uid').on('input', function() {
		       	let uid = $(this).val();
		       	//console.log(uid);
		       	let msg = '';
		       	chid='';
				chidf='';
		       	if (uid == '') {
		       	    msg = '';
		       	}
		       	else if (/[가-힣]/.test(uid)) {
		       	    msg = "한글은 포함할 수 없습니다.";
		       	 	$("#idmsg").css("color", "red");
		       	}
		       	// 문자열 길이 체크 (6~12자리)
		       	else if (uid.length < 6 || uid.length > 12) {
		       	    msg = "6자 이상 입력해주세요.";
		       	 	$("#idmsg").css("color", "red");
		       	}
		       	// 영문을 1자리 이상 포함하는지 체크
		       	else if (!/(?=.*[A-Za-z])/.test(uid)) {
		       	    msg = "영문을 1자리 이상 입력해주세요.";
		       	 	$("#idmsg").css("color", "red");
		       	}
		       	// 영문과 숫자만 포함하는지 체크
		       	else if (!/^[A-Za-z0-9]+$/.test(uid)) {
		       	    msg = "영문과 숫자만 포함할 수 있습니다.";
		       	 	$("#idmsg").css("color", "red");
		       	} else {
		       	    msg = "올바른 형식입니다.";
		       	 	$("#idmsg").css("color", "green");
		       	 	chidf='ok';
		       	}
		        $('#idmsg').text(msg);
		    });
			
			$('#upw').on('input', function() {
		       	let upw = $(this).val();
		       	let upw2 = $('#upw2').val();
		       	//console.log(upw);
		       	
		       	let msg = '';
		       	let msg2 = '';
		       	chpwf='';

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
		        	chpwf='ok';
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
		       	//console.log(upw1);
		       	//console.log(upw2);
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
			
			$("#joinForm").submit(function(event) {
			    event.preventDefault();
			    console.log(chid);
			    console.log(chpw);
			    let upw2 = $('#upw2').val();
			    let uemail = $('#uemail').val();
			    let emailtype = /^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*$/;

			    if (chid == '') {
			        alert('아이디 중복체크를 해주세요.');
			    } else if (chpwf == '') {
			        alert('올바른 비밀번호를 입력해주세요.');
			        $('#upw').val('');
			        $('#upw').focus();
			    } else if (upw2 == '') {
			        alert('비밀번호 확인란을 입력해주세요.');
			        $('#upw2').focus();
			    } else if (chpw == '') {
			        alert('비밀번호 확인이 일치하지 않습니다.');
			        $('#upw2').val('');
			        $('#upw2').focus();
			    } else {
			        // 1초 지연 후 이메일 형식 검사 및 중복 체크
			        setTimeout(function() {
			            if (!emailtype.test(uemail)) {
			                alert('잘못된 이메일입니다.');
			                $('#uemail').focus();
			            } else if (chmail == '') {
			                alert('이미 가입된 이메일입니다.');
			                $('#uemail').val('');
			                $('#uemail').focus();
			            } else {
			                // 이메일이 유효하고 중복되지 않은 경우 AJAX 요청
			                let user = $("#joinForm").serializeArray();
			                let jsonData = {};
			                $.each(user, function() {
			                    jsonData[this.name] = this.value;
			                });
			                console.log(jsonData);

			                $.ajax({
			                    url: '/user/join',
			                    type: 'post',
			                    contentType: 'application/json',
			                    data: JSON.stringify(jsonData), // JSON 문자열로 변환
			                    success: function(data) {
			                        console.log('Received data:', data);
			                        if (data == 'yes') {
			                            alert('회원 가입이 완료되었습니다.');
			                            formReset();
			                            window.location.href = '/login';
			                        } else if (data == 'no') {
			                            alert('회원 가입에 실패하였습니다.');
			                        } else {
			                            alert('예외발생.');
			                        }
			                    },
			                    error: function(xhr, status, error) {
			                        console.error('AJAX request failed: ', status, error);
			                    }
			                });
			            }
			        }, 1000); // 1초 지연
			    }
			});