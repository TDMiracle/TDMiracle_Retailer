package cn.techaction.controller.portal;

import javax.servlet.http.HttpSession;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.techaction.common.SverResponse;
import cn.techaction.pojo.User;
import cn.techaction.service.UserService;
import cn.techaction.utils.ConstUtil;
import cn.techaction.vo.ActionUserVo;

@Controller
@RequestMapping("/user")
public class ActionUserPortalController {
	@Autowired
	private UserService userService;
	
	@RequestMapping("/do_login.do")
	@ResponseBody
	public SverResponse<User> doLogin(HttpSession session,String account,String password){
		SverResponse<User> response = userService.doLogin(account,password);
		if(response.isSuccess()) {
			//登录成功，将用户信息存入session
			session.setAttribute(ConstUtil.CUR_USER, response.getData());
		}
		return response;
	}
	
	@RequestMapping("/do_register.do")
	@ResponseBody
	public SverResponse<String> RegisterUser(User user){
		return userService.doRegister(user);
	}
	
	@RequestMapping("/getuserbyaccount.do")
	@ResponseBody
	public SverResponse<User> getUserByAccount(String account){
		return userService.findUserByAccount(account);
	}
	
	@RequestMapping("/checkuserasw.do")
	@ResponseBody
	public SverResponse<String> checkUserAnswer(String account,String question,String asw){
		return userService.checkUserAnswer(account,question,asw);
	}
	
	@RequestMapping("/resetpassword.do")
	@ResponseBody
	public SverResponse<String> resetPassword(Integer userId,String newpwd){
		return userService.resetPassword(userId,newpwd);
	}
	
	@RequestMapping("/updateuserinfo.do")
	@ResponseBody
	public SverResponse<User> updateUserInfo(HttpSession session,ActionUserVo userVo){
		User curUser = (User)session.getAttribute(ConstUtil.CUR_USER);
		if(curUser == null) {
			return SverResponse.createByErrorMessage("用户尚未登录！");
		}
		userVo.setId(curUser.getId());
		userVo.setAccount(curUser.getAccount());
		SverResponse<User> rsp = userService.updateUserInfo(userVo);
		if(rsp.isSuccess()) {
			//重写session(用于多个页面)
			session.setAttribute(ConstUtil.CUR_USER, rsp.getData());
		}
		return rsp;
	}
	
	@RequestMapping("/updatepassword.do")
	@ResponseBody
	public SverResponse<String> updatePassword(HttpSession session,String newpwd,String oldpwd){
		// session取出
		User user = (User)session.getAttribute(ConstUtil.CUR_USER);
		if(user == null) {
			return SverResponse.createByErrorMessage("请登录后再修改密码！");
		}
		SverResponse result = userService.updatePassword(user,newpwd,oldpwd);
		//修改成功后，将session清空准备重新登录
		if(result.isSuccess()) {
			session.removeAttribute(ConstUtil.CUR_USER);
		}
		return result;
	}
}
