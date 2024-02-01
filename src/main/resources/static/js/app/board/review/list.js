var size = 10;

window.onload = () => {
	setQueryStringParams();
	
	getList(size);
}

function getList(size) {
	var data = { size : size };
	
	$.ajax({
		url: '/api/v1/review',
		type: 'get',
		contentType: 'application/json',
		data: data,
		dataType: 'json',
		async: false,
		success: function(res) {
			if(res.review.content.length <= res.review.total) {
				findAllBoard(res);
			}
		},
		error: function(res) {
			toastr.options = {
				progressBar: true,
			 	showMethod: 'slideDown',
			 	timeOut: 1500
			};
			toastr.error('서버와의 통신 에러입니다.', '잠시 후 재시도 바랍니다.');
			return false;
		}
	});
	
};

$(function() {
	$(window).scroll(function() {
		if(Math.ceil(window.innerHeight + window.scrollY) >= document.body.offsetHeight) {
			if(size <= reviewCnt) {
				size += 10;
				getList(size);
			}
		}
	});
});

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

// 게시글 리스트 조회
function findAllBoard(res) {
	if ( !res.review.content.length ) {
		document.getElementById('no-list').innerHTML = '<td colspan="6"><div className="no_data_msg" style="font-weight: 600; text-align: center;">검색된 결과가 없습니다.</div></td>';
		drawPage();
	}
	
	//let num = pagination.totalRecordCount - ((params.page - 1) * params.recordSize);
	drawList(res);
	//drawPage(pagination, params);
}

function drawPlus() {
	let html = '';
	
    
    
    const list = document.getElementById('list');
    const append = document.createElement("tr");
    append.innerHTML = 'append';
    list.append(append);
}

// 리스트 HTML draw
function drawList(res) {

    let html = '';
    
    res.review.content.forEach(row => {
		html += `
			<li class="postGrid__block">
				${row.privateYn == 'N' ? 
					`<a href="javascript:goViewPage(` + row.id + `);">
						<div>
							${row.titleImg != '' && row.titleImg != null ? `<img src="${row.titleImg}" class="pb__contentImg">` : `` }
						</div>
					</a>` : 
					`<div style="text-align: center;">
						${row.titleImg != '' && row.titleImg != null ? `` : `` }
					</div>`
				}
				<div class="postCard__content">
					${row.privateYn == 'N' ?
						`<a href="javascript:goViewPage(` + row.id + `);">
							<div class="pc__title">
								${row.title}
							</div>
							<div class="pc__content">
								<div>${row.content}</div>
							</div>
						</a>` :
						`${row.writerId == memberId ? 
						`<div onclick="unlockBoard(` + row.id + `)" class="pc__title" style="align-items: center; display: flex;">
							<img style="margin-right: 5px;" src="/img/app/board/lock.png">${row.title}
						</div>` : 
						`<div onclick="lockedBoard()" class="pc__title" style="align-items: center; display: flex;">
							<img style="margin-right: 5px;" src="/img/app/board/lock.png">${row.title}
						</div>`}`
					}
					<div class="pc__footer">
						<span>${timeForToday(row.regDate)}</span>
						<span>·</span>
						<span>${row.commentCnt}개의 댓글</span>
					</div>
				</div>
				<div class="postCard__footer">
					<a href="javascript:goViewPage(` + row.id + `);">
						${row.profileImg != null ? `<img src="${row.profileImg}" class="pf__profileImg">` : `${row.writer}` }
						<span>${row.writer}</span>
					</a>
					<div class="pf__likes">
						<svg viewBox="0 0 24 24"><path fill="currentColor" d="m18 1-6 4-6-4-6 5v7l12 10 12-10V6z"></path></svg>
						<span>${row.likesCnt}</span>
					</div>
				</div>
			</li>
		`;
    })
    
    document.getElementById('list').innerHTML = html;
}

// 페이지 HTML draw
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

// 비공개 게시글 접근
function unlockBoard(id) {
	toastr.options = {
		progressBar: true,
	 	showMethod: 'slideDown',
	 	timeOut: 1500
	};
	toastr.options.onHidden = function() { goViewPage(id); };
	toastr.warning('본인이 등록한 게시글로 상세페이지로 이동합니다.', '비공개 처리된 게시글입니다.');
}

function lockedBoard() {
	toastr.options = {
		progressBar: true,
	 	showMethod: 'slideDown',
	 	timeOut: 2000
	};
	toastr.error('다른 사용자가 등록한 게시물은 접근이 불가합니다.', '비공개 처리된 게시글입니다.');
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
		location.href = '/review/write';
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




