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

    if (searchText !== undefined) {
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
    e.preventDefault();  
    var pageno = $(this).data('pageno');
    var searchType = $(this).data('searchtype');
    var searchText = $(this).data('searchtext');

    // searchText가 정의되지 않은 경우와 정의된 경우를 나누어 처리
    if (searchText !== undefined) {
        // 검색 조회일 때 ajax 요청
        var url = "/board/boardList/search/" + searchType + "/" + searchText + "/page/" + pageno;
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
        var url = "/board/boardList/page/" + pageno;
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
