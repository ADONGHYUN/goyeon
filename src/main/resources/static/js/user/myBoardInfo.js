$(document).ready(function() {
     $.ajax({
         url: '/board/get-myboard',
         type: 'GET',
         success: function(response) {
             var infoBoardLinks = $('#infoBoardLinks');
             
             // 게시판 링크 생성
             response.getMyboardCategory.forEach(function(boardName) {
                 // a 태그 생성
                 var newLink = $('<a>')
                     .attr('href', '/board/boardList/' + encodeURIComponent(boardName))
                     .text(boardName)
                     .addClass('list-group-item list-group-item-action')
             	    .attr('data-category', boardName); // data-category 속성 추가
                
                 // 드롭다운 메뉴 생성
                 var dropdownMenu = $('<ul>').addClass('dropdown-menu')
                 	 .append($('<li>').append($('<div>').addClass('dropdown-item').text(boardName+' 게시판')))
                     .append($('<li>').append($('<a>').addClass('dropdown-item').attr('href', '/board/update/' + boardName).text('수정')))
                     .append($('<li>').append($('<button>').addClass('dropdown-item').text('삭제').attr('onclick', 'deleteBoard("' + boardName + '")')));
                
                 // a 태그와 드롭다운을 묶는 div
                 var dropdown = $('<div>').addClass('dropdown').append(newLink).append(dropdownMenu);
                 infoBoardLinks.append(dropdown);
             });
         },
         error: function(error) {
             alert('나의 게시판 불러오기 에러');
         }
     });
 });

// 게시물 삭제 함수
/* function deleteBoard(boardName) {
	console.log("boardName:"+boardName);
	var deleteConfirm = confirm(boardName + " 게시판을 정말 삭제하시겠습니까?");
	
	if(deleteConfirm){
	$.ajax({
        url: '/board/deleteBoard/'+boardName,
        type: 'DELETE',
        success: function(response) {
        	alert('게시판이 삭제되었습니다.');
        	window.location.reload();
        },
        error: function(error) {
            alert('게시판 삭제 에러');
        }
    });
	} else{
		alert(boardName +' 게시판 삭제가 취소되었습니다.');
	}
} */

function deleteBoard(boardName) {
	console.log("boardName :"+boardName);
    // 모달 메시지 설정
    $('#deleteMessage').html(`${boardName} 게시판을 정말 삭제하시겠습니까?<br>작성하신 ${boardName} 게시판의 모든 글은 삭제됩니다.`);


    
    // 모달 표시
    $('#deleteModal').modal('show');

    // 확인 버튼 클릭 이벤트
    $('#confirmDelete').off('click').on('click', function() {
        console.log("boardName: " + boardName);
        $.ajax({
            url: '/board/deleteBoard/' + encodeURIComponent(boardName),
            type: 'DELETE',
            success: function(response) {
                alert('게시판이 삭제되었습니다.');
                window.location.reload(); // 페이지 새로고침
            },
            error: function(error) {
                alert('게시판 삭제 에러');
            }
        });
        $('#deleteModal').modal('hide'); // 모달 숨기기
    });

    // 취소 버튼 클릭 이벤트
    $('#cancelDelete').off('click').on('click', function() {
        alert(boardName + ' 게시판 삭제가 취소되었습니다.');
        $('#deleteModal').modal('hide'); // 모달 숨기기
    });
}