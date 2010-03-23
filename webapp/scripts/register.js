jQuery.validator.addMethod("unixusername", function(value, element) {
	return this.optional(element) || /^[a-z][a-z0-9_-]{1,6}[a-z0-9]$/.test(value);
}, "Please enter a Unix-style username.");

jQuery.validator.addMethod("minlowercase", function(value, element, params) {
	var matches = value.match(/[a-z]/g);
	if (matches == null)
		return false;
	return this.optional(element) || matches.length >= params;
}, jQuery.validator.format("Please enter at least {0} lower-case letter"));

jQuery.validator.addMethod("minuppercase", function(value, element, params) {
	var matches = value.match(/[A-Z]/g);
	if (matches == null)
		return false;
	return this.optional(element) || matches.length >= params;
}, jQuery.validator.format("Please enter at least {0} upper-case letter"));

jQuery.validator.addMethod("minnumbers", function(value, element, params) {
	var matches = value.match(/[0-9]/g);
	if (matches == null)
		return false;
	return this.optional(element) || matches.length >= params;
}, jQuery.validator.format("Please enter at least {0} numbers"));

$(document).ready(function() {		
	$("#registrationForm").validate({
		rules: {
			username: {
				required: true,
				unixusername: true,
				remote: "/blender/register/checkUsernameAvailable"
			},
			
			password: {
				required: true,
				minlength: 8,
				minlowercase: 1,
				minuppercase: 1,
				minnumbers: 1
			},
			
			passwordConfirmation: {
				required: true,
				equalTo: "#password"
			},
			
			name: "required",
			
			email: {
				required: true,
				email: true,
				remote: "/blender/register/checkEmailNotRegistered"
			}
		},
		
		messages: {
			username: {
				required: msg.fieldRequired,
				unixusername: msg.invalidUsername,
				remote: msg.usernameTaken
			},
			
			password: {
				required: msg.fieldRequired,
				minlength: msg.invalidPassword,
				minlowercase: msg.invalidPassword,
				minuppercase: msg.invalidPassword,
				minnumbers: msg.invalidPassword
			},
			
			passwordConfirmation: {
				required: msg.fieldRequired,
				equalTo: msg.passwordsNomatch
			},
			
			name: msg.fieldRequired,
			
			email: {
				required: msg.fieldRequired,
				email: msg.invalidEmail,
				remote: msg.emailTaken
			}
		},
		
		errorPlacement: function(error, element) {
			element.parent().next().html('<img src="/blender/images/invalid.png" />');
			error.appendTo(element.parent().next());
		},
		
		success: function(label) {
			label.parent().html('<img src="/blender/images/ok.png" />');
		}
	});
	
	/*
	 * Check the ReCaptcha form before submission.
	 */
	if (typeof(Recaptcha) != "undefined") {
		$("#registrationForm").submit(function() {
			var challenge = Recaptcha.get_challenge();
			var response = Recaptcha.get_response();
			
			if (response == "") {
				$("#recaptcha-status").html('<img src="/blender/images/invalid.png" />' + msg.fieldRequired);
				return false;
			}
				
			var valid = $.ajax({
				async: false,
				url: "/blender/register/checkReCaptchaValid",
				data: "challenge=" + challenge + "&response=" + response
			}).responseText;
			
			if (valid == "true") {
				$("#recaptcha-html").text(msg.captchaCorrect);
				$("#recaptcha-status").text(" ");
				return true;
			}
			
			Recaptcha.reload();
			$("#recaptcha-status").html('<img src="/blender/images/invalid.png" />' + msg.captchaIncorrect);
			return false;
		});
	}
});