/*$.validator.setDefaults({
		submitHandler: function() {
			alert("submitted!");
		}
	});*/
    $().ready(function() {
        // alert("Ready!");
        $("#registerForm").validate({
			rules: {
				name: {
					required: true,
					minlength: 2
				},
				country: {
					required: true,
					minlength: 2
				},
				email: {
					required: true,
					email: true
				},
				login: {
					required: true,
					minlength: 2
				},
				password: {
					required: true,
					minlength: 6
				},
				confirmPassword: {
					required: true,
					minlength: 6,
					equalTo: "#password"
				},
			},
			messages: {
			    name: {
					required: "Please enter your name",
					minlength: "Your name must be at least 2 characters long"
				},
				country: "Please enter your country",
				email: "Please enter a valid email address",
				login: {
					required: "Please enter your login",
					minlength: "Your login must be at least 2 characters long"
				},
				password: {
					required: "Please provide a password",
					minlength: "Your password must be at least 6 characters long"
				},
				confirmPassword: {
					required: "Please provide a password",
					minlength: "Your password must be at least 6 characters long",
					equalTo: "Please enter the same password as above"
				},
			}
		});
    });