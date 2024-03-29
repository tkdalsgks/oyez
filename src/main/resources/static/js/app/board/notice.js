window.onload = () => {
	getNotice();
}

function getNotice() {
	
	$.ajax({
		url: '/api/v1/notice',
		type: 'get',
		async: false,
		success: function(res) {
			drawNotice(res);
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
	})
}

function drawNotice(res) {
	
	let html = '';
	
	res.notice.forEach(row => {
		html += `
			<li>
				<a href="/posts/${row.id}" style="display: flex; align-items: center;">
					<div style="display: flex; align-items: center;">
						<img style="width: 25px; margin-right: 5px;" src="/img/upload/warning.svg">
						${row.title}
					</div>
				</a>
			</li>
		`;
	})
	
	document.getElementById('notice').innerHTML = html;
}