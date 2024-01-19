var size = 10;

window.onload = () => {
	setQueryStringParams();
	
	getList(size);
}

function enterkey() {
	if(event.keyCode == 13) {
		movePage(1);
	}
};

// 쿼리 스트링 파라미터 셋팅
function setQueryStringParams() {
	if ( !location.search ) {
		return false;
	}
	
	const form = document.getElementById('searchForm');
	
	new URLSearchParams(location.search).forEach((value, key) => {
		if (form[key]) {
			form[key].value = value;
		}
	})
}


// 페이지 HTML draw
/*
function drawPage(pagination, params) {
	if ( !pagination || !params ) {
		document.querySelector('.paging').innerHTML = '';
		throw new Error('Missing required parameters...');
	}
	
	let html = '';
	
	// 첫 페이지, 이전 페이지
	if (pagination.existPrevPage) {
		html += `
				<a href="javascript:void(0);" onclick="movePage(1)" class="page_bt first">&lt;&lt;</a>
				<a href="javascript:void(0);" onclick="movePage(${pagination.startPage - 1})" class="page_bt prev">&lt;</a>
				`;
	}
	
	// 페이지 번호
	html += '<p>';
	for (let i = pagination.startPage; i <= pagination.endPage; i++) {
		html += (i !== params.page)
		? `<a href="javascript:void(0);" onclick="movePage(${i});">${i}</a>`
		: `<span class="on">${i}</span>`
	}
	html += '</p>';
	
	// 다음 페이지, 마지막 페이지
	if (pagination.existNextPage) {
		html += `
				<a href="javascript:void(0);" onclick="movePage(${pagination.endPage + 1});" class="page_bt next">&gt;</a>
				<a href="javascript:void(0);" onclick="movePage(${pagination.totalPageCount});" class="page_bt last">&gt;&gt;</a>
				`;
	}
	
	document.querySelector('.paging').innerHTML = html;
}
*/

// 페이지 이동
function movePage(page) {
    const form = document.getElementById('searchForm');
    const queryParams = {
		keyword: form.keyword.value
		, searchType: form.searchType.value
        , page: (page) ? page : 1
    }
    
    location.href = location.pathname + '?' + new URLSearchParams(queryParams).toString();
}

// 게시글 상세 페이지로 이동
function goViewPage(id) {
    location.href = '/posts/' + id;
}

// 비공개 게시글 접근 : 리스트
function privatePageList(id) {
	list.forEach(function(value) {
		if(value.id == id) {
			if(memberId == value.memberId) {
				toastr.options = {
					progressBar: true,
				 	showMethod: 'slideDown',
				 	timeOut: 1500
				};
				toastr.options.onHidden = function() { goViewPage(id); };
				toastr.warning('본인이 등록한 게시글로 상세페이지로 이동합니다.', '비공개 게시글입니다.');
			} else {
				toastr.options = {
					progressBar: true,
				 	showMethod: 'slideDown',
				 	timeOut: 1500
				};
				toastr.error('다른 사용자가 등록한 게시물은 접근이 불가합니다.', '비공개 게시글입니다.');
			}
		} 
	});
}

// 비공개 게시글 접근 : 좋아요
function privatePageLikes(id) {
	likes.forEach(function(value) {
		if(value.id == id) {
			if(memberId == value.memberId) {
				toastr.options = {
					progressBar: true,
				 	showMethod: 'slideDown',
				 	timeOut: 1500
				};
				toastr.options.onHidden = function() { goViewPage(id); };
				toastr.warning('본인이 등록한 게시글로 상세페이지로 이동합니다.', '비공개 게시글입니다.');
			} else {
				toastr.options = {
					progressBar: true,
				 	showMethod: 'slideDown',
				 	timeOut: 1500
				};
				toastr.error('다른 사용자가 등록한 게시물은 접근이 불가합니다.', '비공개 게시글입니다.');
			}
		} 
	});
}

// 비공개 게시글 접근 : 공지
function privatePageNotice(id) {
	notice.forEach(function(value) {
		if(value.id == id) {
			if(memberId == value.memberId) {
				toastr.options = {
					progressBar: true,
				 	showMethod: 'slideDown',
				 	timeOut: 1500
				};
				toastr.options.onHidden = function() { goViewPage(id); };
				toastr.warning('본인이 등록한 게시글로 상세페이지로 이동합니다.', '비공개 게시글입니다.');
			} else {
				toastr.options = {
					progressBar: true,
				 	showMethod: 'slideDown',
				 	timeOut: 1500
				};
				toastr.error('다른 사용자가 등록한 게시물은 접근이 불가합니다.', '비공개 게시글입니다.');
			}
		} 
	});
}

// 사용자 상세 페이지로 이동
function goUserPage(id) {
    const queryString = (location.search) ? location.search + `&id=${id}` : `?id=${id}`;
    location.href = '/member/detail' + queryString;
}

// 게시글 리프레쉬
function refreshBoard() {
	drawList();
}

// 게시글 작성하기
function writeBoard() {
	if(certified == null) {
		toastr.warning('계정 인증이 필요한 기능입니다.');
	} else {
		location.href = '/community/write';
	}
}

// 검색 박스 생성
const search = document.getElementById('searchBox');
search.style.display = 'none';

function searchBox() {
	if(search.style.display == 'none') {
		search.style.display = '';
	} else {
		search.style.display = 'none';
	}
}




