function post(path, params, method) {
	method = method || "post"; 
	var form = document.createElement("form");
	form.setAttribute("method", method);
	form.setAttribute("action", path);
	
	for ( var key in params) {
		if (params.hasOwnProperty(key)) {
			var hiddenField = document.createElement("input");
			hiddenField.setAttribute("type", "hidden");
			hiddenField.setAttribute("name", key);
			hiddenField.setAttribute("value", params[key]);
			form.appendChild(hiddenField);
		}
	}

	document.body.appendChild(form);
	form.submit();
}



/*const checkboxes = document.getElementsByClassName("form-check-input");
	for (let i = 0; i < checkboxes.length; i++) {
		checkboxes[i].addEventListener("click", function() {
			for (let j = 0; j < checkboxes.length; j++) {
		      if (i !== j) {
		        checkboxes[j].checked = false;
		      }
		    }
	});
}*/


	
	    
