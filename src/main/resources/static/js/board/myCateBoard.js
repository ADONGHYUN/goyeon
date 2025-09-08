function searchAction(event) {
    event.preventDefault();
    var searchType = $('#searchType').val().trim();
    var searchText = $('#searchTextInput').val().trim();

    console.log("searchType,searchText :"+searchType + searchText);
    
    $.ajax({
    	type:"GET",
    	url:"/board/boardList/"+"${bcategory}"+"/search/" +searchType+ "/"+searchText+"/page/1",
    	success: function(response) {
    		console.log("ajax성공");
    		console.log("aa"+response.postList);
    		updateTable(response.postList);
    		updatePaging(response.paging, searchType, searchText);
        },
        error: function(xhr, status, error) {
           
        }
    });
}


function updateTable(postListData) {
    console.log("updateTable 호출");
    console.log("postListData: ", postListData);
    
    var postList = $("#postList");
    postList.empty(); // 기존 내용을 모두 비웁니다.

    // postListData의 각 게시물을 순회하며 HTML 구조를 동적으로 생성합니다.
    postListData.forEach(function(post) {
        // 게시물에 이미지가 있는지 확인
        var imgTag = post.bimage ? `<img src="/resources/Thumbnail/\${post.bimage}" alt="\${post.title}" >` : '';
        console.log("imgTag: "+ imgTag);


        var cardHtml = `
            <div class="col">
                <div id="space-card" class="space-card">
                <div class="dropdown" id="cardEditButton" >               
                <button class="btn btn-link dropdown-toggle" type="button" data-bs-toggle="dropdown" aria-expanded="false">
                  &#x22EE; 
                </button>
               
                <ul class="dropdown-menu" aria-labelledby="dropdownMenuButton">
                  <li><a class="dropdown-item" href="/board/update/\${post.bnum}">수정</a></li>
                  <li><button class="dropdown-item" onclick="deletePost('\${post.bnum}', this)">삭제</button></li>

                </ul>
              </div>
                <a href="/board/boardDetail/bnum/\${encodeURIComponent(post.bnum)}">
                        \${imgTag}
                        <div class="card-body">
                            <h5 class="card-title">\${post.title}</h5>
                            <h5 class="card-title">\${post.subtitle}</h5>
                            <div class="card-text">
                                \${post.uid}                                
                                <div class="favorite-icon" data-pname="\${post.title}" id="likeHeart" onclick="toggleFavorite('\${post.title}', this)"> &#9829;</div>
                            </div>                               
                        </div>
                    </a>
                </div>                                             
            </div>
        `;

        // 생성한 HTML을 #postList에 추가
        postList.append(cardHtml);
    });
}


// 날짜 포맷팅 함수
function formatDate(dateString) {
    var date = new Date(dateString);
    var year = date.getFullYear();
    var month = ('0' + (date.getMonth() + 1)).slice(-2);
    var day = ('0' + date.getDate()).slice(-2);
    return year + '-' + month + '-' + day;
}


function updatePaging(ppage, searchType, searchText) {
    console.log("updatePaging");
    var paginationDiv = $(".pagination");
    paginationDiv.empty(); // 기존 페이징 요소 비우기

    if(searchText !== undefined){
    	if (ppage.startPage != 1) {
            var prevPage = ppage.startPage - 5;
            paginationDiv.append('<li class="page-item"><a href="#" class="page-link" data-pageno="' + prevPage + '" data-searchType="' + searchType + '" data-searchText="' + searchText + '">이전</a></li>');
        }
        for (var pageNo = ppage.startPage; pageNo <= ppage.endPage; pageNo++) {
            var activeClass = (pageNo == ppage.currentPage) ? ' active' : '';
            paginationDiv.append('<li class="page-item' + activeClass + '"><a href="#" class="page-link" data-pageno="' + pageNo + '" data-searchType="' + searchType + '" data-searchText="' + searchText + '">' + pageNo + '</a></li>');
        }
        if (ppage.lastStartPage != ppage.startPage) {
            var nextPage = ppage.startPage + 5;
            paginationDiv.append('<li class="page-item"><a href="#" class="page-link" data-pageno="' + nextPage + '" data-searchType="' + searchType + '" data-searchText="' + searchText + '">다음</a></li>');
        }  	        
    } else {
    	if (ppage.startPage != 1) {
            var prevPage = ppage.startPage - 5;
            paginationDiv.append('<li class="page-item"><a href="#" class="page-link" data-pageno="' + prevPage + '">이전</a></li>'); // 수정된 부분
        }
        for (var pageNo = ppage.startPage; pageNo <= ppage.endPage; pageNo++) {
            var activeClass = (pageNo == ppage.currentPage) ? ' active' : '';
            paginationDiv.append('<li class="page-item' + activeClass + '"><a href="#" class="page-link" data-pageno="' + pageNo + '">' + pageNo + '</a></li>');
        }
        if (ppage.lastStartPage != ppage.startPage) {
            var nextPage = ppage.startPage + 5;
            paginationDiv.append('<li class="page-item"><a href="#" class="page-link" data-pageno="' + nextPage + '">다음</a></li>'); // 수정된 부분
        }
        
    }
}


$(document).on('click', '.page-link', function(e) {
	console.log("aaa");
    e.preventDefault();  
    var pageno = $(this).data('pageno');
    var searchType = $(this).data('searchtype');
    var searchText = $(this).data('searchtext');
    console.log("page는 : "+pageno);
	
    // searchText가 정의되지 않은 경우와 정의된 경우를 나누어 처리
    if (searchText !== undefined) {
        // 검색 조회일 때 ajax 요청
        var url = "/board/boardList/"+ "${bcategory}"+ "/search/" + searchType + "/" + searchText + "/page/" + pageno;
        $.ajax({
            url: url,
            type: 'GET',
            success: function(response) {
                updateTable(response.postList);
                updatePaging(response.paging, searchType, searchText);
            },
            error: function(error) {
                console.error("Ajax Error " + error);
            }
        });
    } else {
        // 검색 조회가 아닐 때 ajax 요청
        var url = "/board/boardList/" + "${bcategory}"+ "/page/" + pageno;
        console.log("pageno :: " + pageno);
        console.log("url :: " + url);
	
        $.ajax({
            url: url,
            type: 'GET',
            success: function(response) {
                updateTable(response.postList);
                updatePaging(response.paging, searchType, searchText);
            },
            error: function(error) {
                console.error("Ajax Error " + error);
            }
        });
    }
});


function deletePost(bnum, element) {
    // 확인 메시지를 띄운 후 삭제 요청을 진행합니다.
    if (!confirm("정말 삭제하시겠습니까?")) {
        return;
    }

    $.ajax({
        url: '/board/delete/bnum/'+bnum,
        type: 'DELETE', // HTTP 메소드 DELETE 사용
        success: function(response) {
            console.log("삭제 성공");
            
            // 게시글이 포함된 최상위 카드 요소를 찾아서 삭제합니다.
            $(element).closest('.col').remove();
        },
        error: function(xhr, status, error) {
            console.error("삭제 실패:", error);
            alert("삭제에 실패했습니다. 다시 시도해주세요.");
        }
    });
}