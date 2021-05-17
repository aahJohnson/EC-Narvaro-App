function setAttentionPercentage(userId, attentionPercentage) {
	var xhttp = null;

	xhttp = new XMLHttpRequest();
	xhttp.onreadystatechange = function() {
		if (this.readyState == 4 && this.status == 200) {
			location.reload();
		}
	};
	xhttp.open("GET", "StudentServlet?userIdNumber=" + userId + "&attendingPercentage=" + attentionPercentage, true);
	xhttp.send();
}

function date(date){
	var xhttp = null;

	xhttp = new XMLHttpRequest();
	xhttp.onreadystatechange = function() {
		if (this.readyState == 4 && this.status == 200) {
			location.reload();
		}
	};
	xhttp.open("GET", "StudentServlet?date=" + date, true);
	xhttp.send();
}