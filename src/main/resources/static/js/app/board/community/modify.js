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
	const content = document.getElementsByClassName('ck-content');
	
	const fields = [form.title, form.writer];
	const fieldNames = ['제목', '이름'];
	
	for (let i = 0, len = fields.length; i < len; i++) {
		isValid(fields[i], fieldNames[i]);
	}
	
	if(ckeditor.getData() == '' || ckeditor.getData() == 0) {
		window.ckeditor.focus();
		toastr.warning('내용을 입력하세요.');
		return false;
	} else {
		document.getElementById('saveBtn').disabled = true;
		form.noticeYn.value = form.isNotice.checked;
		form.action = saveBoard_formAction;
		form.submit();
	}
}
