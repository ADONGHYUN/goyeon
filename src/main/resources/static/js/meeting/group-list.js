document.addEventListener("DOMContentLoaded", function () {
	const buttons = document.querySelectorAll(".join-btn");
	const token = localStorage.getItem('accessToken');
	let gender = null;
	let selectedGroupId = null;

	const modal = document.getElementById("groupModal");
	const closeBtn = document.querySelector(".close");
	const groupList = document.getElementById("myGroupList");
	const confirmBtn = document.getElementById("confirmJoinBtn");

	if (token) {
		const payload = parseJwt(token);
		if (payload) {
			console.log(payload.gender);
			gender = payload.gender || null;
		}
	}

	buttons.forEach(button => {
		button.addEventListener("click", function () {
			const groupId = this.getAttribute("data-id");
			const leaderGender = this.getAttribute("data-gender");

			if (!gender) {
				alert("로그인이 필요합니다.");
				return;
			}

			if (gender === leaderGender) {
				alert("같은 성별끼리는 신청할 수 없습니다.");
				return;
			}

			selectedGroupId = groupId;
			openModalWithGroups();
		});
	});

	closeBtn.onclick = () => modal.style.display = "none";

	confirmBtn.addEventListener("click", () => {
		if (selectedGroupId) {
			window.location.href = `/meeting/group/${selectedGroupId}`;
		}
	});

	function openModalWithGroups() {
		groupList.innerHTML = ''; // 초기화

		fetch('/meeting/my-groups', {
			method: 'GET',
			headers: {
				'Authorization': 'Bearer ' + token
			}
		})
			.then(res => res.json())
			.then(data => {
				if (data.length === 0) {
					groupList.innerHTML = "<li>가입된 그룹이 없습니다.</li>";
				} else {
					data.forEach(group => {
						const card = document.createElement('div');
						card.classList.add('group-card');
						card.innerHTML = `
							<h3>그룹 이름: ${group.groupName}</h3>
							<p>그룹장: ${group.leaderUid}</p>
							<button class="send-request" data-group-id="${group.groupId}">신청</button>
						`;
						groupList.appendChild(card);
					});
				}
				modal.style.display = "flex";
			})
			.catch(err => {
				console.error('그룹 목록 불러오기 실패', err);
				groupList.innerHTML = "<li>오류가 발생했습니다.</li>";
				modal.style.display = "flex";
			});
	}
	
	groupList.addEventListener('click', function (e) {
		if (e.target.classList.contains('send-request')) {
			const fromGroupId = e.target.getAttribute('data-group-id');
			const toGroupId = selectedGroupId; // 상대 그룹 ID
			const token = localStorage.getItem('accessToken');

			fetch('/meeting/meeing-request', {
				method: 'POST',
				headers: {
					'Content-Type': 'application/json',
					'Authorization': 'Bearer ' + token
				},
				body: JSON.stringify({
					fromGroup: fromGroupId,
					toGroup: toGroupId
				})
			})
			.then(res => res.json())
			.then(data => {
				alert(data.message);
			})
			.catch(err => {
				console.error("요청 실패", err);
				alert("요청 중 오류 발생");
			});
		}
	});
});



function parseJwt(token) {
	try {
		const base64Url = token.split('.')[1];
		const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
		const jsonPayload = decodeURIComponent(
			atob(base64)
				.split('')
				.map(c => '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2))
				.join('')
		);
		return JSON.parse(jsonPayload);
	} catch (e) {
		console.error("JWT 디코딩 실패:", e);
		return null;
	}
}
