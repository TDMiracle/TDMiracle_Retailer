define(function(){
	var baseUrl="http://localhost:8080/mail/";
	//获取url中的参数
	function getParam(name){
		var reg = new RegExp("(^|&)" + name + "=([^&*])(&|$)");//构造一个含有目标参数的正则表达式对象
		var r = window.location.search.sustr(1).match(reg);//匹配目标参数
		if(r != null) return decodeURI(r[2]);
			return null;
	}
	//用户校验
	function userCalibration(){
		var url = window.location.href;
		//判断是否为登录页，如果是则不再请求登录信息
		if(url.indexOf("login.html")>=0){
			return;
		}
		//加载登录的用户信息
		$.ajax({
			url:baseUrl+"mgr/user/login.do",
			xhrFields:{withCredentials:true},
			crossDomain:true,
			success:function(user){
				if(user.status==0){
					//判断是否为管理员
					if(user.data.role==2){
						$("#user-info-container").html(user.data.account);
					}else{
						alert("无操作权限");
						$(window).attr("location","../dy2/index.html");
					}
				}else{
					//未登录直接跳转登录页
					$(window).attr("location","login.html");
				}
			}
		});
	}
	//模块化思想
	return {
		baseUrl:baseUrl,
		getParam:getParam,
		userCalibration:userCalibration
	};
});