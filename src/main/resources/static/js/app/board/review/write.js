ClassicEditor
    .create( document.querySelector( '#content' ), {
        extraPlugins: [uploadAdapterPlugin],
        language: 'ko',
        ckfinder: { uploadUrl: '/api/v1/upload' }
    })
    .then( editor => {
    	window.ckeditor = editor;
    })
    .catch( error => {
        console.error( error );
    } );

function uploadAdapterPlugin(editor) {
	editor.plugins.get('FileRepository').createUploadAdapter = (loader) => {
		return uploadAdapter(loader);
	}
}

window.onload = () => {
	renderBoardInfo();
}

// 등록일 초기화
//function initIDate() {
	//document.getElementById('regDate').value = moment().format('YYYY/MM/DD');
//}

// 게시글 상세정보 렌더링
function renderBoardInfo() {
	if ( !board ) {
		//initIDate();
		return false;
	}
	
	const form = document.getElementById('saveForm');
	const fields = ['id', 'title', 'content', 'writer', 'noticeYn', 'privateYn'];
	if(board.noticeYn == 'Y') {
		form.isNotice.checked = board.noticeYn;		
	}
	if(board.privateYn == 'Y') {
		form.isPrivate.checked = board.privateYn;		
	}
	
	console.log("1 " + board.noticeYn)
	console.log("2 " + board.privateYn)
	
	//form.regDate.value = moment(board.regDate).format('YYYY/MM/DD HH:mm');
	
	fields.forEach(field => {
		form[field].value = board[field];
	});
}

// 게시글 저장(수정)
function saveBoard() {
	
	const form = document.getElementById('saveForm');
	const notice = document.getElementById('isNotice');
	const noticeYn = document.getElementById('noticeYn');
	const private = document.getElementById('isPrivate');
	const privateYn = document.getElementById('privateYn');
	const filter = $("#filter option:selected").val();
	const title = document.getElementById('title');
	
	console.log(title.value)
	console.log(ckeditor.getData())
	
	const fields = [form.title, form.hashtag];
	const fieldNames = ['제목', '해시태그'];
	
	for (let i = 0, len = fields.length; i < len; i++) {
		isValid(fields[i], fieldNames[i]);
	}
	
	if(ckeditor.getData() == '' || ckeditor.getData() == 0) {
		toastr.warning('내용을 입력하세요.');
		ckeditor.editing.view.focus();
		return false;
	} else {
		if(filter == 'NONE') {
			toastr.warning('필터를 선택하세요.');
			return false;
		} else {
			if(board == null) {
				//savePoints();
			}
			
			var path = saveBoard_formAction;
			
			// 공지글 여부
			if(notice.checked == true) {
				noticeYn.value = 'Y'
			} else {
				noticeYn.value = 'N'
			}
			
			// 비공개 여부
			if(private.checked == true) {
				privateYn.value = 'Y'
			} else {
				privateYn.value = 'N'
			}
			
			// 타이틀 이미지 여부
			var editorData = ckeditor.getData();
			var s = editorData.indexOf("https://oyez-webservice.s3.ap-northeast-2.amazonaws.com/")
			var e = editorData.indexOf("\"></figure>")
			var e2 = editorData.indexOf("\">")
			
			var result = editorData.substring(s, e)
			var result2 = editorData.substring(s, e2)
			
			var titleImg = null;
			if(result.search("https://oyez-webservice.s3.ap-northeast-2.amazonaws.com/")) {
				titleImg = result2;
			} else if(result2.search("https://oyez-webservice.s3.ap-northeast-2.amazonaws.com/")) {
				titleImg = result;
			}
			
			if(titleImg != null) {
				var data = JSON.stringify({ 
					"title": title.value, 
					"content": ckeditor.getData(),
					"titleImg": titleImg,
					"noticeYn": noticeYn.value,
					"privateYn": privateYn.value,
					"memberId": memberId,
					"filter": filter,
					"hashtag": hashtag.value 
				});
			} else {
				var data = JSON.stringify({ 
					"title": title.value, 
					"content": ckeditor.getData(),
					"noticeYn": noticeYn.value,
					"privateYn": privateYn.value,
					"memberId": memberId,
					"filter": filter,
					"hashtag": hashtag.value 
				});
			}
			
			$.ajax({
				url: path,
				type: 'POST',
				contentType: 'application/json',
				data: data,
				success: function() {
					swal.fire({
						title: '게시글이 작성되었습니다.',
						icon: 'warning',
						confirmButtonColor: '#3085d6',
						confirmButtonText: '확인',
					}).then((result) => {
						if(result.isConfirmed) {
							location.href = "javascript:history.go(-1)";
						}
					});
				},
				error: function() {
				}
			})
		}
	}
}

// 게시글 작성 취소
function cancelBoard() {
	swal.fire({
		title: '게시글 작성을 취소할까요?',
		text: '현재 작성중인 내용은 저장되지 않습니다.',
		icon: 'warning',
		showCancelButton: true,
		confirmButtonColor: '#3085d6',
		cancelButtonColor: '#d33',
		confirmButtonText: '확인',
		cancelButtonText: '취소'
	}).then((result) => {
		if(result.isConfirmed) {
			location.href = "javascript:history.go(-1)";
		}
	});
}

// 포인트 적립
function savePoints() {
	var pointsCd = "2";
	var points = "100";
	
	var headers = { "Content-Type": "application/json", "X-HTTP-Method-Override": "POST" };
	var params = { "pointsCd": pointsCd, "points": points, "memberId": memberId };
	
	//console.log("params : " + JSON.stringify(params));
	
	$.ajax({
		url: "/api/v1/points",
		type: "POST",
		headers: headers,
		dataType: "JSON",
		data: JSON.stringify(params),
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

// 해시태그
const hashtagInput = document.getElementById('hashtags');
const hashtagOutput = document.getElementById('hashtag-output');
const hiddenHashtagInput = document.getElementById('hashtag');
var cnt = 0;

let hashtags = [];

function addHashtag(tag) {
	tag = "#" + hashtagInput.value;
	const span = document.createElement("span");
	span.innerText = tag + " ";
	span.classList.add("hashtag");
	span.classList.add("tagify__tag");
	
	const removeButton = document.createElement("button");
	removeButton.classList.add("tagify__tag__removeBtn");
	removeButton.classList.add("remove-button");
	removeButton.addEventListener("click", () => {
		hashtagOutput.removeChild(span);
		hashtags = hashtags.filter((hashtag) => hashtag !== tag);
		hiddenHashtagInput.value = hashtags.join(" ");
		cnt--;
	});
	
	span.appendChild(removeButton);
	hashtagOutput.appendChild(span);
	hashtags.push("#" + hashtagInput.value);
	hiddenHashtagInput.value = hashtags.join(" ");
	cnt++;
}

hashtagInput.addEventListener("keydown", (event) => {
	if(event.keyCode === 13) {
		event.preventDefault();
		const tag = hashtagInput.value.trim();
		if(cnt >= 3) {
			toastr.warning('작성 갯수를 초과했습니다.');
			hashtagInput.value = null;
		} else {
			if(tag) {
				addHashtag();
				hashtagInput.value = null;
			}
		}
	}
});
