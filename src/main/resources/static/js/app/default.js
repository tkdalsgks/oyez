/**
 * 30일 이전 시간 변환
 *
 * @param value
 * @return value
 */
function timeForToday(value) {
	
	var sDateTime = String(value);
	var sYear = sDateTime.substring(0,4);
	var sMonth = sDateTime.substring(4,6);
	var sDate = sDateTime.substring(6,8);
	var sHour = sDateTime.substring(8,10);
	var sMin = sDateTime.substring(10,12);
	var sSec = sDateTime.substring(12,14);
	 
	const start = new Date(Number(sYear), Number(sMonth)-1, Number(sDate), Number(sHour), Number(sMin), Number(sSec));
	const end = new Date();
	
	const diff = (end - start) / 1000;
	const times = [
		{ name: '년', milliSeconds: 60 * 60 * 24 * 365 },
		{ name: '개월', milliSeconds: 60 * 60 * 24 * 30 },
		{ name: '일', milliSeconds: 60 * 60 * 24 },
		{ name: '시간', milliSeconds: 60 * 60 },
		{ name: '분', milliSeconds: 60 },
	];
	
	for (const value of times) {
		const betweenTime = Math.floor(diff / value.milliSeconds);
		
		if (betweenTime > 0) {
			return `${betweenTime}${value.name} 전`;
		}
	}
	return '방금 전';
}