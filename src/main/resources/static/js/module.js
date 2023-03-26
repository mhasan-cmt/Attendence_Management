//debut chekbox
	  var checkboxes = document.querySelectorAll('input[name="moment"]');
	  for (var i = 0; i < checkboxes.length; i++) {
	    checkboxes[i].addEventListener('change', function () {
	      var checkbox = this;
	      if (checkbox.checked) {
	        for (var j = 0; j < checkboxes.length; j++) {
	          if (checkboxes[j] !== checkbox) {
	            checkboxes[j].checked = false;
	          }
	        }
	      }
	    });
	  }
	  
	  //debut date parsing
	  function formatDate(date) {
	        var d = new Date(date);
	            day = '' + d.getDate();
	            month = '' + (d.getMonth() + 1);
	            year = d.getFullYear();
	
	        if (month.length < 2) 
	            month = '0' + month;
	        if (day.length < 2) 
	            day = '0' + day;
	
	        return [day, month, year].join('/');
	    }
	    
		document.getElementById("date_debut").value = formatDate(new Date());
		document.getElementById("date_fin").value = formatDate(new Date());
		document.getElementById("date").value = formatDate(new Date());
		
		/**
		 * document.getElementById("date_debut").value = moment(document.getElementById("date_debut").value).format('DD/MM/YYYY');
  		document.getElementById("date_fin").value = moment(document.getElementById("date_fin").value).format('DD/MM/YYYY');
		 */