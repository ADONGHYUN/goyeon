$('#searchBtn').on('click', function() {
	const nickname = $('#searchNickname').val().trim();

	if (!nickname) {
		alert('닉네임을 입력해주세요.');
		return;
	}

	const encoded = encodeURIComponent(nickname);

	$.ajax({
		url: `/user/searchNickname?nickname=${encoded}`,
		method: 'GET',
		dataType: 'json',
		success: function(user) {
			const resultsDiv = $('#searchResults');
			const teamList = $('#teamList');
			console.log("성공" + user);
			console.log("성공" + user.uid);
			
			if (!user || !user.uid) {
				resultsDiv.html('<span style="color: red;">사용자를 찾을 수 없습니다.</span>');
				setTimeout(() => resultsDiv.empty(), 3000);
				return;
			}

			// 중복 확인 (uid 기준)
			if (teamList.find(`[data-uid="${user.uid}"]`).length > 0) {
				alert('이미 초대한 사용자입니다.');
				return;
			}
			const userDiv = $(`
			            <div class="badge bg-primary text-white p-2 m-1 d-inline-flex align-items-center"
			                 data-uid="${user.uid}" data-nickname="${user.unickname}">
			                <span class="me-2">${user.unickname}</span>
			                <button type="button" class="btn-close btn-close-white btn-sm" aria-label="Remove"></button>
			            </div>
			        `);

			userDiv.find('button').on('click', function() {
				userDiv.remove();
			});

			teamList.append(userDiv);
		},
		error: function(xhr, status, error) {
			alert('사용자 검색에 실패했습니다.');
			console.error(error);
		}
	});
});

// 그룹 만들기 버튼 클릭
$('form').on('submit', function(e) {
	e.preventDefault(); // 기본 제출 막음

	const teamList = $('#teamList');
	const uids = [];

	teamList.find('[data-uid]').each(function() {
		uids.push($(this).data('uid'));
	});

	if (uids.length === 0) {
		alert('최소 한 명 이상 초대해주세요.');
		return;
	}

	console.log("uids :" + uids);

	const memberCount = $('select[name="memberCount"]').val();

	// JSON 전송
	$.ajax({
		url: '/meeting/create',
		method: 'POST',
		contentType: 'application/json',
		data: JSON.stringify({
			memberCount: memberCount,
			teamMembers: uids
		}),
		success: function(res) {
			if (res.success) {
			      alert(res.message); // "그룹 생성 성공"
			    } else {
			      alert("실패: " + res.message);
			    }
			window.location.href = '/';
		},
		error: function(xhr) {
			alert('그룹 생성 실패');
			console.error(xhr.responseText);
		}
	});
});

