function visitPage(page,valor) {
    $.ajax({
        url: 'http://localhost:15000/'+page,
        dataType: 'json',
        data: {
			Authorization: valor
		},
        type: 'GET',
        success: function (data) {
		$('#init').after('Hello '+data.username+' in page '+data.pagename);
        },
      error:function(data){
        console.log(data);
		window.location.href = "./login.html";
      }
    });
}

function validateAuth(page){
var valor = localStorage.getItem('Authorization');
  if(valor == '' || valor == null){
    window.location.href = "./login.html";
  }else{
    visitPage(page,valor);
  }
}

function logout(){
  var valor = localStorage.getItem('Authorization');
  visitPage('logout',valor);
  localStorage.removeItem('Authorization');
   window.location.href = "./login.html";
}