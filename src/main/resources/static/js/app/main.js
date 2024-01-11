

$(document).ready(function(){
	savePoints();
});

// 포인트 적립
function savePoints() {
	var pointsCd = "1";
	
	var headers = { "Content-Type": "application/json", "X-HTTP-Method-Override": "POST" };
	var params = { "pointsCd": pointsCd, "memberId": memberId };
	
	//console.log("params : " + JSON.stringify(params));
	/*
	if(role != 'GUEST') {
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
				} else if(response.result == true) {
					location.href = "/points/box";
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
	*/
}
