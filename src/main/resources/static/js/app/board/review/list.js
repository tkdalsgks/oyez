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
    /*
    notice.forEach(row => {
        html += `
        	<tr>
                <td class="list-important">
	                	<div>
	                		<div class="list-img">
		                		<a href="/${row.writerId}/activity">
			                		<span class="list-writer" style="font-weight: 800;">관리자</span>
				                </a>
		                		<span>&nbsp;·&nbsp;</span>
			                	<span class="list-time">${timeForToday(moment(row.idate).format('YYYY/MM/DD HH:mm'))}</span>
			                	<!-- <span>${row.udate != null ? '&nbsp;·&nbsp;' : ''}</span> -->
			                	<!-- <span class="list-udate">${row.udate != null ? '수정됨' : ''}</span> -->
		                	</div>
		        			${row.privateYn == 'N' ? 
		                		`<a href="javascript:void(0);" onclick="goViewPage(` + row.id + `);"><div class="list-title"><span>` + row.title + `</span></div></a>` : 
		                		`<div class="list-title" style="cursor: pointer;" onclick="privatePageNotice(` + row.id + `)"><span><img style="margin-right: 5px;" src="/img/app/board/lock.png">` + row.title + `</span></div>` }
		                	<div class="list-info">
			                	<span class="list-filter1">${row.noticeYn === false ? '일상' : '공지사항'}</span>
			                	<span class="list-hashtag">${row.hashtag}</span>
			                	<div>
			                		<svg class="list-view-svg" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" aria-hidden="true" class="h-5 w-5"><path stroke-linecap="round" stroke-linejoin="round" d="M2.036 12.322a1.012 1.012 0 010-.639C3.423 7.51 7.36 4.5 12 4.5c4.638 0 8.573 3.007 9.963 7.178.07.207.07.431 0 .639C20.577 16.49 16.64 19.5 12 19.5c-4.638 0-8.573-3.007-9.963-7.178z"></path><path stroke-linecap="round" stroke-linejoin="round" d="M15 12a3 3 0 11-6 0 3 3 0 016 0z"></path></svg>
				                	<span class="list-view">${row.viewCnt}</span>
			                		<svg class="list-comment-svg" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" aria-hidden="true" class="h-5 w-5"><path stroke-linecap="round" stroke-linejoin="round" d="M8.625 9.75a.375.375 0 11-.75 0 .375.375 0 01.75 0zm0 0H8.25m4.125 0a.375.375 0 11-.75 0 .375.375 0 01.75 0zm0 0H12m4.125 0a.375.375 0 11-.75 0 .375.375 0 01.75 0zm0 0h-.375m-13.5 3.01c0 1.6 1.123 2.994 2.707 3.227 1.087.16 2.185.283 3.293.369V21l4.184-4.183a1.14 1.14 0 01.778-.332 48.294 48.294 0 005.83-.498c1.585-.233 2.708-1.626 2.708-3.228V6.741c0-1.602-1.123-2.995-2.707-3.228A48.394 48.394 0 0012 3c-2.392 0-4.744.175-7.043.513C3.373 3.746 2.25 5.14 2.25 6.741v6.018z"></path></svg>
				                	<span class="list-comment">${row.commentCnt}</span>
			                		<svg class="list-likes-svg" style="color: red" xmlns="http://www.w3.org/2000/svg" width="13" height="13" fill="currentColor" class="bi bi-suit-heart-fill" viewBox="0 0 16 16"> <path d="M4 1c2.21 0 4 1.755 4 3.92C8 2.755 9.79 1 12 1s4 1.755 4 3.92c0 3.263-3.234 4.414-7.608 9.608a.513.513 0 0 1-.784 0C3.234 9.334 0 8.183 0 4.92 0 2.755 1.79 1 4 1z" fill="red"></path> </svg>
				                	<span class="list-likes">${row.likesCnt}</span>
			                	</div>
		                	</div>
	                	</div>
                </td>
            </tr>
        `;
    })
    
    likes.forEach(row => {
        html += `
        	<tr>
                <td class="list-important">
	                	<div>
	                		<div class="list-img">
		                		<a href="/${row.writerId}/activity">
		                			${row.profileImg != null ? 
		                				`<img src="${row.profileImg}">` :
		                				`<span>${row.subWriter}<span>` }
			                		<span class="list-writer">${row.writer}</span>
				                </a>
		                		<span>&nbsp;·&nbsp;</span>
			                	<span class="list-time">${timeForToday(moment(row.idate).format('YYYY/MM/DD HH:mm'))}</span>
			                	<span>${row.udate != null ? '&nbsp;·&nbsp;' : ''}</span>
			                	<span class="list-udate">${row.udate != null ? '수정됨' : ''}</span>
		                	</div>
		                	${row.privateYn == 'N' ? 
		                		`<a href="javascript:void(0);" onclick="goViewPage(` + row.id + `);"><div class="list-title"><span>` + row.title + `</span></div></a>` : 
		                		`<div class="list-title" style="cursor: pointer;" onclick="privatePageLikes(` + row.id + `)"><span><img style="margin-right: 5px;" src="/img/app/board/lock.png">` + row.title + `</span></div>` }
		                	<div class="list-info">
			                	<span class="list-filter1">${row.likesCnt >= 1 ? '좋아요' : row.id}</span>
			                	<span class="list-hashtag">${row.hashtag}</span>
			                	<div>
			                		<svg class="list-view-svg" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" aria-hidden="true" class="h-5 w-5"><path stroke-linecap="round" stroke-linejoin="round" d="M2.036 12.322a1.012 1.012 0 010-.639C3.423 7.51 7.36 4.5 12 4.5c4.638 0 8.573 3.007 9.963 7.178.07.207.07.431 0 .639C20.577 16.49 16.64 19.5 12 19.5c-4.638 0-8.573-3.007-9.963-7.178z"></path><path stroke-linecap="round" stroke-linejoin="round" d="M15 12a3 3 0 11-6 0 3 3 0 016 0z"></path></svg>
				                	<span class="list-view">${row.viewCnt}</span>
			                		<svg class="list-comment-svg" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" aria-hidden="true" class="h-5 w-5"><path stroke-linecap="round" stroke-linejoin="round" d="M8.625 9.75a.375.375 0 11-.75 0 .375.375 0 01.75 0zm0 0H8.25m4.125 0a.375.375 0 11-.75 0 .375.375 0 01.75 0zm0 0H12m4.125 0a.375.375 0 11-.75 0 .375.375 0 01.75 0zm0 0h-.375m-13.5 3.01c0 1.6 1.123 2.994 2.707 3.227 1.087.16 2.185.283 3.293.369V21l4.184-4.183a1.14 1.14 0 01.778-.332 48.294 48.294 0 005.83-.498c1.585-.233 2.708-1.626 2.708-3.228V6.741c0-1.602-1.123-2.995-2.707-3.228A48.394 48.394 0 0012 3c-2.392 0-4.744.175-7.043.513C3.373 3.746 2.25 5.14 2.25 6.741v6.018z"></path></svg>
				                	<span class="list-comment">${row.commentCnt}</span>
				                	<svg class="list-likes-svg" style="color: red" xmlns="http://www.w3.org/2000/svg" width="13" height="13" fill="currentColor" class="bi bi-suit-heart-fill" viewBox="0 0 16 16"> <path d="M4 1c2.21 0 4 1.755 4 3.92C8 2.755 9.79 1 12 1s4 1.755 4 3.92c0 3.263-3.234 4.414-7.608 9.608a.513.513 0 0 1-.784 0C3.234 9.334 0 8.183 0 4.92 0 2.755 1.79 1 4 1z" fill="red"></path> </svg>
				                	<span class="list-likes">${row.likesCnt}</span>
			                	</div>
		                	</div>
	                	</div>
                </td>
            </tr>
        `;
    })
    */
    res.review.content.forEach(row => {
		html += `
			<li class="postGrid__block">
				${row.profileImg != null ? 
					`<a href="javascript:goViewPage(` + row.id + `);">
						<div>
							<img src="/img/app/common/image.gif" class="pb__contentImg">
						</div>
					</a>` : ``
				}
				<div class="postCard__content">
					<a href="javascript:goViewPage(` + row.id + `);">
						<div class="pc__title">
							${row.title}
						</div>
						<div class="pc__content">
							${row.content}
						</div>
						<div class="pc__footer">
							<span>${timeForToday(row.regDate)}</span>
							<span>·</span>
							<span>${row.viewCnt}개의 댓글</span>
						</div>
					</a>
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
		/*
        html += `
       		 <tr>
                <td class="list-general">
	                	<div>
	                		<div class="list-img">
		                		<a href="/${row.writerId}/activity">
		                			${row.profileImg != null ? 
		                				`<img src="${row.profileImg}">` :
		                				`<span class="list-img-span">${row.subWriter}</span>` }
			                		<span class="list-writer">${row.writer}</span>
			                	</a>
		                		<span>&nbsp;·&nbsp;</span>
			                	<span class="list-time">${timeForToday(moment(row.idate).format('YYYY/MM/DD HH:mm'))}</span>
			                	<span>${row.udate != null ? '&nbsp;·&nbsp;' : ''}</span>
			                	<span class="list-udate">${row.udate != null ? '수정됨' : ''}</span>
		                	</div>
		                	${row.privateYn == 'N' ? 
		                		`<a href="javascript:void(0);" onclick="goViewPage(` + row.id + `);"><div class="list-title"><span>` + row.title + `</span></div></a>` : 
		                		`<div class="list-title" style="cursor: pointer;" onclick="privatePageList(` + row.id + `)"><span><img style="margin-right: 5px;" src="/img/app/board/lock.png">` + row.title + `</span></div>` }
		                	<div class="list-info">
			                	<span class="list-filter2">${row.noticeYn === false ? '일상' : '공지'}</span>
			                	<span class="list-hashtag">${row.hashtag}</span>
			                	<div>
			                		<svg class="list-view-svg" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" aria-hidden="true" class="h-5 w-5"><path stroke-linecap="round" stroke-linejoin="round" d="M2.036 12.322a1.012 1.012 0 010-.639C3.423 7.51 7.36 4.5 12 4.5c4.638 0 8.573 3.007 9.963 7.178.07.207.07.431 0 .639C20.577 16.49 16.64 19.5 12 19.5c-4.638 0-8.573-3.007-9.963-7.178z"></path><path stroke-linecap="round" stroke-linejoin="round" d="M15 12a3 3 0 11-6 0 3 3 0 016 0z"></path></svg>
				                	<span class="list-view">${row.viewCnt}</span>
			                		<svg class="list-comment-svg" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" aria-hidden="true" class="h-5 w-5"><path stroke-linecap="round" stroke-linejoin="round" d="M8.625 9.75a.375.375 0 11-.75 0 .375.375 0 01.75 0zm0 0H8.25m4.125 0a.375.375 0 11-.75 0 .375.375 0 01.75 0zm0 0H12m4.125 0a.375.375 0 11-.75 0 .375.375 0 01.75 0zm0 0h-.375m-13.5 3.01c0 1.6 1.123 2.994 2.707 3.227 1.087.16 2.185.283 3.293.369V21l4.184-4.183a1.14 1.14 0 01.778-.332 48.294 48.294 0 005.83-.498c1.585-.233 2.708-1.626 2.708-3.228V6.741c0-1.602-1.123-2.995-2.707-3.228A48.394 48.394 0 0012 3c-2.392 0-4.744.175-7.043.513C3.373 3.746 2.25 5.14 2.25 6.741v6.018z"></path></svg>
				                	<span class="list-comment">${row.commentCnt}</span>
				                	<svg class="list-likes-svg" style="color: red" xmlns="http://www.w3.org/2000/svg" width="13" height="13" fill="currentColor" class="bi bi-suit-heart-fill" viewBox="0 0 16 16"> <path d="M4 1c2.21 0 4 1.755 4 3.92C8 2.755 9.79 1 12 1s4 1.755 4 3.92c0 3.263-3.234 4.414-7.608 9.608a.513.513 0 0 1-.784 0C3.234 9.334 0 8.183 0 4.92 0 2.755 1.79 1 4 1z" fill="red"></path> </svg>
				                	<span class="list-likes">${row.likesCnt}</span>
			                	</div>
		                	</div>
	                	</div>
                </td>
            </tr>
        `;
        */
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

// 비공개 게시글 접근 : 리스트
function privatePageList(id) {
	list.forEach(function(value) {
		if(value.id == id) {
			if(userId == value.writerId) {
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
			if(userId == value.writerId) {
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
			if(userId == value.writerId) {
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




