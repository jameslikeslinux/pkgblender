$(document).ready(function() {	
	$("#notification a").click(function() {
		$.get("/blender/resendEmailVerification", function(data) {
			$("#notification span").text("Sent!");
		});
		
		return false;
	});
});