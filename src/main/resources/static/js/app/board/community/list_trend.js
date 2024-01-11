function getList(size) {
	var data = { size : size };
	
	$.ajax({
		url: '/api/v1/community/trend',
		type: 'get',
		contentType: 'application/json',
		data: data,
		dataType: 'json',
		async: false,
		success: function(res) {
			if(res.commu.content.length <= res.commu.total) {
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
			if(size <= commuCnt) {
				size += 10;
				getList(size);
			}
		}
	});
});

// 게시글 리스트 조회
function findAllBoard(res) {
	if ( !res.commu.content.length ) {
		document.getElementById('no-list').innerHTML = '<td colspan="6"><div className="no_data_msg" style="font-weight: 600; text-align: center;">검색된 결과가 없습니다.</div></td>';
	}
	
	drawList(res);
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
    
    res.commu.content.forEach(row => {
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
    })
    
    document.getElementById('list').innerHTML = html;
}