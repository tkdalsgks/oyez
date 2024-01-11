ClassicEditor
    .create( document.querySelector( '#content' ), {
        extraPlugins: [uploadAdapterPlugin],
        language: 'ko',
        ckfinder: { uploadUrl: '/upload' }
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
function initIDate() {
	document.getElementById('IDate').value = moment().format('YYYY/MM/DD');
}

// 게시글 상세정보 렌더링
function renderBoardInfo() {
	if ( !board ) {
		initIDate();
		return false;
	}
	
	const form = document.getElementById('saveForm');
	const fields = ['id', 'title', 'content', 'writer', 'noticeYn'];
	form.isNotice.checked = board.noticeYn;
	form.IDate.value = moment(board.IDate).format('YYYY/MM/DD HH:mm');
	
	fields.forEach(field => {
		form[field].value = board[field];
	});
}

// 게시글 저장(수정)
function saveBoard() {
	
	const form = document.getElementById('saveForm');
	const filter = $("#filter option:selected").val();
	const hashtag = document.getElementById('hashtag');
	
	const fields = [form.title, form.writer];
	const fieldNames = ['제목', '이름'];
	
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
		} else if(hashtag == null) {
			toastr.warning('해시태그를 최소 1개 입력하세요.');
			return false;
		} else {
			if(board == null) {
				savePoints();
			}
			document.getElementById('saveBtn').disabled = true;
			form.noticeYn.value = form.isNotice.checked;
			form.filterId.value = filter;
			form.hashtag.value = hashtag.value;
			form.action = saveBoard_formAction;
			form.submit();
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
	var params = { "pointsCd": pointsCd, "points": points, "userId": userId };
	
	//console.log("params : " + JSON.stringify(params));
	
	$.ajax({
		url: "/points",
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
