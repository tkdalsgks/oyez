window.addEventListener('load', () => {
	$.LoadingOverlay("show", {
		background       : "rgba(0, 0, 0, 0.3)",
		image            : "",
		maxSize          : 40,
		fontawesome      : "fa fa-spinner fa-pulse fa-fw",
		fontawesomeColor : "#FFFFFF",
	});
	
	setTimeout(() => {
		$.LoadingOverlay("hide");
	});
});