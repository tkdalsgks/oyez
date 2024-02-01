$(document).ready(function(){
	if(referer == null) {
		location.href = "/";
	}
});

/*
ClassicEditor
    .create( document.querySelector( '#content' ), {
        extraPlugins: [uploadAdapterPlugin],
        language: 'ko',
        ckfinder: { uploadUrl: '/api/v1/upload' }
    })
    .then( editor => {
    	window.ckeditor_write = editor;
    })
    .catch( error => {
        console.error( error );
    } );

function uploadAdapterPlugin(editor) {
	editor.plugins.get('FileRepository').createUploadAdapter = (loader) => {
		return uploadAdapter(loader);
	}
}
*/

// 게시글 삭제
function deleteBoard() {
    const writer = document.getElementById('writer').value;
	
	swal.fire({
		title: '게시글을 삭제할까요?',
		icon: 'warning',
		showCancelButton: true,
		confirmButtonColor: '#3085d6',
		confirmButtonText: '확인',
		cancelButtonColor: '#d33',
		cancelButtonText: '취소'
	}).then((result) => {
		if(result.isConfirmed) {
			if(writer != memberId) {
				toastr.warning('권한이 없는 게시글입니다.');
        	} else {
				
				var path = "/api/v1/" + id + "/delete";
				
				$.ajax({
					url: path,
					type: 'POST',
					success: function() {
						history.go(-1)
					},
					error: function() {
						toastr.options = {
							progressBar: true,
						 	showMethod: 'slideDown',
						 	timeOut: 1500
						};
						toastr.error('서버와의 통신 에러입니다.', '잠시 후 재시도 바랍니다.');
						return false;
					}
				});
        	}
		}
	});
}

// 게시글 리스트 페이지로 이동
function goListPage() {
    const queryString = new URLSearchParams(location.search);
    queryString.delete('id');
    location.href = '/community' + '?' + queryString.toString();
}

// 게시글 수정 페이지로 이동
function goChangePage() {
	const writer = document.getElementById('writer').value;
	const memberId = document.getElementById('memberId').value;
	
	if(writer != memberId) {
		toastr.warning('권한이 없는 게시글입니다.');
	} else {
        location.href = '/posts/' + id + '/modify' + location.search;
	}
	/* th:href="@{/community/write( id=${board.id} )}"  */
}

// 사용자 상세 페이지로 이동
function goMemberPage(id) {
    location.href = '/member/detail' + `?id=${id}`;
}

$(function() {
	printCommentList();
});

function openModal(id, writer, content) {
	
	$("#commentModal").modal("toggle");
	
	document.getElementById("modalWriter").value = writer;
	document.getElementById("modalContent").value = content;
	
	document.getElementById("btnCommentUpdate").setAttribute("onclick", "updateComment("+ id +")");
	document.getElementById("btnCommentDelete").setAttribute("onclick", "deleteComment("+ id +")");
}

// 댓글 작성
function insertComment(boardId) {
	var content = document.getElementById('content');
	
	var headers = { "Content-Type": "application/json", "X-HTTP-Method-Override": "POST" };
	
	if(boardSeq == 2) {
		var rating = document.getElementById('rating');
		var params = JSON.stringify({ 
			"boardId": boardId, 
			"content": content.value, 
			"writerId": memberId, 
			"memberNickname": writerName.value,
			"rating": rating.value 
		});
	} else {
		var params = JSON.stringify({ 
			"boardId": boardId, 
			"content": content.value, 
			"writerId": memberId, 
			"memberNickname": writerName.value
		});
	}
	
	if(content.value == "") {
		content.focus();
		toastr.warning('댓글을 작성하세요.');
		return false;
	} else {
		if(boardSeq == 2) {
			if(rating.value >= 1) {
				$.ajax({
					url: comment_uri,
					type: 'POST',
					headers: headers,
					dataType: 'json',
					data: params,
					success: function() {
						// 포인트 적립
						//savePoints();
						// 에디터 내용 초기화
						content.value = "";
						// 댓글 리스트 출력
						printCommentList();
					},
					error: function() {
						toastr.options = {
							progressBar: true,
						 	showMethod: 'slideDown',
						 	timeOut: 1500
						};
						toastr.error('서버와의 통신 에러입니다.', '잠시 후 재시도 바랍니다.');
						return false;
					}
				});
			} else {
				toastr.warning('별점을 입력하세요.');
				return false;			
			}
		} else {
			$.ajax({
				url: comment_uri,
				type: 'POST',
				contentType: 'application/json',
				dataType: 'json',
				data: params,
				success: function() {
					// 포인트 적립
					//savePoints();
					// 에디터 내용 초기화
					content.value = "";
					// 댓글 리스트 출력
					printCommentList();
				},
				error: function() {
					toastr.options = {
						progressBar: true,
					 	showMethod: 'slideDown',
					 	timeOut: 1500
					};
					toastr.error('서버와의 통신 에러입니다.', '잠시 후 재시도 바랍니다.');
					return false;
				}
			});
		}
	}
}

// 엔터키 댓글 작성
function enterkey() {
	if(event.keyCode == 13) {
		insertComment(boardId);
	}
};

// 댓글 수정
function updateComment(id, memberNickname) {
	
	var uri = comment_uri + "/" + id;
	var content = document.getElementById('contentEdit' + id);
	
	var headers = { "Content-Type": "application/json", "X-HTTP-Method-Override": "PATCH" };
	var params = JSON.stringify({ 
		"id": id, 
		"memberNickname": memberNickname, 
		"content": content.value
	});
	
	$.ajax({
		url: uri,
		type: 'PATCH',
		contentType: 'application/json',
		dataType: 'json',
		data: params,
		success: function(res) {
			if (res.result == false) {
				toastr.warning('댓글을 수정하지 못하였습니다.');
				return false;
			} else if(res.result == "isUnauthorized") {
				toastr.warning(res.message);
			} else {
				toastr.success(res.message);			
			}
			printCommentList();
			$("#commentModal").modal("hide");
		},
		error: function(response) {
			toastr.options = {
					progressBar: true,
				 	showMethod: 'slideDown',
				 	timeOut: 1500
				};
				toastr.error('서버와의 통신 에러입니다.', '잠시 후 재시도 바랍니다.');
			return false;
		}
	});
}

// 댓글 삭제
function deleteComment(id) {
	var uri = comment_uri + "/" + id;
	
	swal.fire({
		title: '댓글을 삭제할까요?',
		icon: 'warning',
		showCancelButton: true,
		confirmButtonColor: '#3085d6',
		confirmButtonText: '확인',
		cancelButtonColor: '#d33',
		cancelButtonText: '취소'
	}).then((result) => {
		if(result.isConfirmed) {
			var headers = {"Content-Type": "application/json", "X-HTTP-Method-Override": "DELETE"};

			$.ajax({
				url: uri,
				type: 'DELETE',
				headers: headers,
				dataType: 'json',
				success: function(response) {
					if (response.result == false) {
						toastr.warning('댓글을 삭제하지 못하였습니다.');
						return false;
					} else if(response.result == "isUnauthorized") {
						toastr.warning(response.message);
					} else {
						toastr.success(response.message);
					}
					printCommentList();
					$("#commentModal").modal("hide");
				},
				error: function(xhr, status, error) {
					toastr.options = {
						progressBar: true,
					 	showMethod: 'slideDown',
					 	timeOut: 1500
					};
					toastr.error('서버와의 통신 에러입니다.', '잠시 후 재시도 바랍니다.');
					return false;
				}
			});
		}
	});
}

// 댓글 수정(NEW)
function replyUpdate(id) {
	
	const reply = document.getElementById('reply' + id);
	const replyEdit = document.getElementById('replyEdit' + id);
	const replyUpdate = document.getElementById('replyUpdate' + id);
	const replyCancel = document.getElementById('replyCancel' + id);
	const replySave = document.getElementById('replySave' + id);
	
	reply.style.display = 'none';
	replyEdit.style.display = '';
	replyUpdate.style.display = 'none';
	replyCancel.style.display = '';
	replySave.style.display = '';
	
	/*
	ClassicEditor
        .create( document.querySelector( '#replyEditor' + id ), {
            extraPlugins: [uploadAdapterPlugin],
            language: 'ko',
            ckfinder: { uploadUrl: '/upload' }
        })
        .then( editor => {
        	window.ckeditor_change = editor;
        })
        .catch( error => {
            console.error( error );
    });
    */
}

// 댓글 수정 취소(NEW)
function replyCancel(id) {
	
	const reply = document.getElementById('reply' + id);
	const replyEdit = document.getElementById('replyEdit' + id);
	const replyUpdate = document.getElementById('replyUpdate' + id);
	const replyCancel = document.getElementById('replyCancel' + id);
	const replySave = document.getElementById('replySave' + id);
	
	reply.style.display = '';
	replyEdit.style.display = 'none';
	replyUpdate.style.display = '';
	replyCancel.style.display = 'none';
	replySave.style.display = 'none';
}


// 댓글 리스트 출력
function printCommentList() {
	$.get(printCommentList_uri, function(response) {
		if (isEmpty(response) == false) {
			var commentsHtml = "";
			
			if(boardSeq == 2) {
				$(response.commentList).each(function(id, comment) {
					commentsHtml += `
						<li class="comment-li">
							<div class="comment-img">
								<img src="${comment.profileImg}" style="border-radius: 999px;">
								<div class="comment-name">
									<span class="name"><a href="/${comment.memberId}/activity">${comment.memberNickname}</a></span>
									<div class="comment-edit" style="display: flex;">
										<span class="time comment-time">${timeForToday(comment.regDate)}
											<span>${comment.updtDate != null ? '·' : ''}</span>
											<span class="comment-udate">${comment.updtDate != null ? '수정됨' : ''}</span>
										</span>
									</div>
								</div>
								<div th:if="${boardSeq == '2'}" style="display: flex; flex-direction: column;">
									<div>
										<span class="reply-star">
											★★★★★
											<span style="width: ${comment.rating}0%">★★★★★</span>
										</span>
									</div>
									<div th:if="${memberId} == ${comment.memberId}" style="text-align: center;">
										${memberId == comment.memberId ? 
											`<span onclick="replyUpdate(${comment.id})" id="replyUpdate${comment.id}" style="align-items: center; font-size: 13px; cursor: pointer;">${comment.deleteYn != 'Y' ? '수정' : ''}</span>
											<span onclick="replyCancel(${comment.id})" id="replyCancel${comment.id}" style="display: none; align-items: center; font-size: 13px; cursor: pointer;">${comment.deleteYn != 'Y' ? '취소' : ''}</span>
											<span onclick="updateComment('${comment.id}', '${comment.memberNickname}')" id="replySave${comment.id}" style="display: none; align-items: center; font-size: 13px; cursor: pointer; background-color: skyblue;">${comment.deleteYn != 'Y' ? '저장' : ''}</span>
											<span onclick="deleteComment(${comment.id})" style="align-items: center; font-size: 13px; cursor: pointer;">${comment.deleteYn != 'Y' ? '삭제' : ''}</span>` :
											`` }
									</div>
								</div>
							</div>
							<div style="line-height: 22px;">
								<div id="reply${comment.id}" class="desc comment-content">
									${comment.deleteYn == 'N' ? comment.content : comment.adminDeleteYn == 'N' ? '[삭제된 댓글입니다.]' : '[관리자에 의해 삭제된 댓글입니다.]'}
								</div>
								<div id="replyEdit${comment.id}" style="display: none;">
									<textarea id="contentEdit${comment.id}" class="form-control comment-input textarea">${comment.content}</textarea>
								</div>
							</div>
						</li>
					`;
				});
			} else {
				$(response.commentList).each(function(id, comment) {
					
					commentsHtml += `
						<li class="comment-li">
							<div class="comment-img">
								<img src="${comment.profileImg}" style="border-radius: 999px;">
								<div class="comment-name">
									<span class="name"><a href="/${comment.memberId}/activity">${comment.memberNickname}</a></span>
									<div class="comment-edit" style="display: flex;">
										<span class="time comment-time">${timeForToday(comment.regDate)}
											<span>${comment.updtDate != null ? '·' : ''}</span>
											<span class="comment-udate">${comment.updtDate != null ? '수정됨' : ''}</span>
										</span>
									</div>
								</div>
								<div th:if="${boardSeq == '2'}" style="display: flex; flex-direction: column;">
									<div th:if="${memberId} == ${comment.memberId}" style="text-align: center;">
										${memberId == comment.memberId ? 
											`<span onclick="replyUpdate(${comment.id})" id="replyUpdate${comment.id}" style="align-items: center; font-size: 13px; cursor: pointer; color: var(--text1);">${comment.deleteYn != 'Y' ? '수정' : ''}</span>
											<span onclick="replyCancel(${comment.id})" id="replyCancel${comment.id}" style="display: none; align-items: center; font-size: 13px; cursor: pointer; color: var(--text1);">${comment.deleteYn != 'Y' ? '취소' : ''}</span>
											<span onclick="updateComment('${comment.id}', '${comment.memberNickname}')" id="replySave${comment.id}" style="display: none; align-items: center; font-size: 13px; cursor: pointer; background-color: skyblue; color: var(--text1);">${comment.deleteYn != 'Y' ? '저장' : ''}</span>
											<span onclick="deleteComment(${comment.id})" style="align-items: center; font-size: 13px; cursor: pointer; color: var(--text1);">${comment.deleteYn != 'Y' ? '삭제' : ''}</span>` :
											`` }
									</div>
								</div>
							</div>
							<div style="line-height: 22px;">
								<div id="reply${comment.id}" class="desc comment-content">
									${comment.deleteYn == 'N' ? comment.content : comment.adminDeleteYn == 'N' ? '[삭제된 댓글입니다.]' : '[관리자에 의해 삭제된 댓글입니다.]'}
								</div>
								<div id="replyEdit${comment.id}" style="display: none;">
									<textarea id="contentEdit${comment.id}" class="form-control comment-input textarea">${textarea(comment.content)}</textarea>
								</div>
							</div>
						</li>
					`;
				});
			}
			
			$(".notice-list").html(commentsHtml);
		}
	}, "json");
}

// 게시글 좋아요, 취소
function insertLikes(boardId) {
	var headers = { "Content-Type": "application/json", "X-HTTP-Method-Override": "POST" };
	var params = { "boardId": boardId, "memberId": memberId };
	
	$.ajax({
		url: insertLikes_uri,
		type: "POST",
		headers: headers,
		dataType: "json",
		data: JSON.stringify(params),
		success: function(response) {
			if(response.todayLikes >= 5) {
				toastr.warning(response.message);
				return false;
			} else if(response.result == false) {
				toastr.warning('다시 한 번 눌러주세요.');
				return false;
			} else {
				alert(1)
				//window.location.reload(true);
			}
		},
		error: function(xhr, status, error) {
			toastr.options = {
				progressBar: true,
			 	showMethod: 'slideDown',
			 	timeOut: 1500
			};
			toastr.error('서버와의 통신 에러입니다.', '잠시 후 재시도 바랍니다.');
			return false;
		}
	});
}

// 포인트 적립
function savePoints() {
	var pointsCd = "3";
	var points = "10";
	
	var headers = { "Content-Type": "application/json", "X-HTTP-Method-Override": "POST" };
	var params = JSON.stringify({ 
		"pointsCd": pointsCd, 
		"points": points, 
		"memberId": memberId 
	});
	
	$.ajax({
		url: "/api/v1/points",
		type: "POST",
		headers: headers,
		dataType: "JSON",
		data: params,
		success: function(response) {
			if(response.result == false) {
				toastr.warning('포인트 적립에 실패했습니다.');
				return false;
			} else {
				return true;
			}
		},
		error: function(xhr, status, error) {
			toastr.options = {
				progressBar: true,
			 	showMethod: 'slideDown',
			 	timeOut: 1500
			};
			toastr.error('서버와의 통신 에러입니다.', '잠시 후 재시도 바랍니다.');
			return false;
		}
	});
}

// 게시글 공개/비공개 전환
function publicOrPrivate() {
	var uri = "/api/v1/" + id + "/private";
	
	if(privateYn == 'Y') {
		privateYn = '공개';
	} else if(privateYn = 'N') {
		privateYn = '비공개';
	}
	
	swal.fire({
		title: privateYn + ' 게시글로 전환할까요?',
		icon: 'warning',
		showCancelButton: true,
		confirmButtonColor: '#3085d6',
		confirmButtonText: '확인',
		cancelButtonColor: '#d33',
		cancelButtonText: '취소'
	}).then((result) => {
		if(result.isConfirmed) {
			$.ajax({
				url: uri,
				type: "POST",
				dataType: "TEXT",
				success: function(response) {
					history.go(-1);
				},
				error: function(xhr, status, error) {
					toastr.options = {
						progressBar: true,
					 	showMethod: 'slideDown',
					 	timeOut: 1500
					};
					toastr.error('서버와의 통신 에러입니다.', '잠시 후 재시도 바랍니다.');
					return false;
				}
			});
		}
	});
}

function goSettings(memberId) {
    location.href = '/' + `${memberId}` + '/account';
}
