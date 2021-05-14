function setAttentionPercentage(userId, attentionPercentage) {
	var xhttp = null;

	xhttp = new XMLHttpRequest();
	xhttp.onreadystatechange = function() {
		if (this.readyState == 4 && this.status == 200) {
			location.reload();
		}
	};
	xhttp.open("GET", "StudentServlet?userIdNumber=" + userId + "&attentionPercentage=" + attentionPercentage, true);
	xhttp.send();
}

function setLessonId(activeLessonId) {
	var xhttp = null;

	xhttp = new XMLHttpRequest();
	xhttp.onreadystatechange = function() {
		if (this.readyState == 4 && this.status == 200) {
			location.reload();
		}
	};
	xhttp.open("GET", "StudentServlet?activeLessonId=" + activeLessonId, true);
	xhttp.send();
}