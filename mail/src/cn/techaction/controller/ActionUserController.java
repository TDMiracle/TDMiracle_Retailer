package cn.techaction.controller;

import java.util.List;


import javax.servlet.http.HttpSession;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import cn.techaction.common.SverResponse;
import cn.techaction.pojo.User;
import cn.techaction.service.ActionUserService;
import cn.techaction.utils.ConstUtil;
import cn.techaction.vo.ActionUserVo;

@Controller
@RequestMapping("/mgr/user")
public class ActionUserController {
	@Autowired
	private ActionUserService actionUserService;
	@RequestMapping("/login.do")
	@ResponseBody//转换成response中body中，json格式数据
	public SverResponse<User> doLogin(HttpSession session,String account,String password) {
		//1.调用Service层方法：登录
		SverResponse<User> response = actionUserService.doLogin(account, password);
		//2.判断是否能登录
		if(response.isSuccess()) {
			//3.能登录则判断是否是管理员，是管理员存放在session,否则提示错误信息
			User user = response.getData();
			if(user.getRole()==ConstUtil.Role.ROLE_ADMIN) {
				session.setAttribute(ConstUtil.CUR_USER, user);
				return response;
			}
			return SverResponse.createByErrorMessage("不是管理员，无法登录！");
		}
		return response;
	}
	@RequestMapping("/finduserlist.do")
	@ResponseBody
	public SverResponse<List<ActionUserVo>> getUserDetail(HttpSession session) {
		//1.判断用户是否登录
		User user = (User)session.getAttribute(ConstUtil.CUR_USER);
		if(user == null) {
			return SverResponse.createByErrorCodeMessage(405,"请登录后再进行操作");
		}
		//2.判断登录的用户是否是管理员
		SverResponse<String> response = actionUserService.isAdmin(user);
		if(response.isSuccess()) {
			//3.调用service层方法获得所有用户
			return actionUserService.findUserList();
		}
		return SverResponse.createByErrorMessage("您无操作权限！");
	}
	
	@RequestMapping("/finduser.do")
	@ResponseBody
	public SverResponse<ActionUserVo> findUser(HttpSession session,Integer id){
		//1.判断用户是否登录
		User user = (User)session.getAttribute(ConstUtil.CUR_USER);
		if(user == null) {
			return SverResponse.createByErrorCodeMessage(405,"请登录后再进行操作");
		}
		//2.判断登录的用户是否是管理员
		SverResponse<String> response = actionUserService.isAdmin(user);
		if(response.isSuccess()) {
		//3.调用service层方法获得Id对应用户
			return actionUserService.findUser(id);
		}
		return SverResponse.createByErrorMessage("您无操作权限！");
	}
	@RequestMapping("/updateuser.do")
	@ResponseBody
	public SverResponse<User> updateUser(HttpSession session,ActionUserVo userVo){
		//1.判断用户是否登录
		User user = (User)session.getAttribute(ConstUtil.CUR_USER);
		if(user == null) {
			return SverResponse.createByErrorCodeMessage(405,"请登录后再进行操作");
		}
		//2.判断登录的用户是否是管理员
		SverResponse<String> response = actionUserService.isAdmin(user);
		if(response.isSuccess()) {	
		//3.调用service层方法更新用户
			return actionUserService.updateUserInfo(userVo);
		}
		return SverResponse.createByErrorMessage("您无操作权限！");
	}
	/**
	 * 删除用户
	 * @param session
	 * @param id
	 * @return
	 */
	@RequestMapping("/deleteusers.do")
	@ResponseBody
	public SverResponse<String> delUsers(HttpSession session,Integer id){
		//1.判断用户是否登录
		User user = (User)session.getAttribute(ConstUtil.CUR_USER);
		if(user == null) {
			return SverResponse.createByErrorCodeMessage(405,"请登录后再进行操作");
		}
		//2.判断登录的用户是否是管理员
		SverResponse<String> response = actionUserService.isAdmin(user);
		if(response.isSuccess()) {	
			//3.调用service的删除方法
			return actionUserService.delUser(id);
		}
		return SverResponse.createByErrorMessage("您无操作权限!");
	}
}
