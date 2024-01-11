/**
 * 자료형에 상관없이 값이 비어있는지 확인
 * 
 * @param value - 타겟 밸류
 * @returns true or false
 */
function isEmpty(value) {
	if (value == null || value == "" || value == undefined || (value != null && typeof value == "object" && !Object.keys(value).length)) {
		return true;
	}

	return false;
}

/**
 * 문자열의 마지막 문자의 종성을 반환
 * 
 * @param str - 타겟 문자열
 * @returns 문자열의 마지막 문자의 종성
 */
function charToUnicode(str) {
	return (str.charCodeAt(str.length - 1) - 0xAC00) % 28;
}

/**
 * 필드1, 필드2의 값이 다르면 해당 필드2로 focus한 뒤에 메시지 출력
 * 
 * @param field1 - 타겟 필드1
 * @param field2 - 타겟 필드2
 * @param fieldName - 필드 이름
 * @returns 메시지
 */
function equals(field1, field2, fieldName) {
	if (field1.value === field2.value) {
		return true;
	} else {
		/* alert 메시지 */
		var message = "";
		/* 종성으로 을 / 를 구분 */
		if (charToUnicode(fieldName) > 0) {
			message = fieldName + "이 일치하지 않습니다.";
		} else {
			message = fieldName + "가 일치하지 않습니다.";
		}
		field2.focus();
		toastr.warning(message);
		return false;
	}
}

/**
 * field의 값이 올바른 형식인지 체크 (정규표현식 사용)
 * 
 * @param field - 타겟 필드
 * @param fieldName - 필드 이름 (null 허용)
 * @param focusField - 포커스할 필드 (null 허용)
 * @returns 메시지
 */
function isValid(field, fieldName, focusField) {

	if (isEmpty(field.value) == true) {
		/* 종성으로 조사(을 또는 를) 구분 */
		var message = (charToUnicode(fieldName) > 0) ? fieldName + "을 입력하세요." : fieldName + "를 입력하세요.";
		toastr.warning(message);
		throw new Error( '빈칸을 확인해주세요.' );
	} else {
		return true;
	}
}

/**
 * 둘 중 비어있지 않은 value를 반환
 * 
 * @param value1 - 타겟 밸류1
 * @param value2 - 타겟 밸류2
 * @returns 비어잇지 않은 vlaue
 */
function nvl(value1, value2) {
	return (isEmpty(value1) == false ? value1 : value2);
}

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

/*
window.onresize = function() {
	const width = window.innerWidth;
	
	if(width <= 640) {
		$("#board-title").each(function() {
			const length = 16;
			$(this).each(function() {
				if ($(this).text().length >= length) {
					$(this).text($(this).text().substr(0, length) + "...")
				}
			});
		});
	} else {
		$("#board-title").each(function() {
			const length = 35;
			$(this).each(function() {
				if ($(this).text().length >= length) {
					$(this).text($(this).text().substr(0, length) + "...")
				}
			});
		});
	}
}
*/

/**
 * 리뷰 별점 시스템
 **/
const drawStar = (target) => {
	document.querySelector(`.star span`).style.width = `${target.value * 10}%`;
}

/**
 * 리뷰 별점 수정 시스템
 **/
const replyDrawStar = (target) => {
	document.querySelector(`.reply-star-u span`).style.width = `${target.value * 10}%`;
}

/**
 * textarea 자동 높이 조절
 **/
const DEFAULT_HEIGHT = 20;
const $textarea = document.querySelector('.textarea');

$textarea.oninput = (event) => {
	const $target = event.target;
	
	$target.style.height = 0;
	$target.style.height = DEFAULT_HEIGHT + $target.scrollHeight + 'px';
};



