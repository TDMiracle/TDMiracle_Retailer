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
	@ResponseBody//ת����response��body�У�json��ʽ����
	public SverResponse<User> doLogin(HttpSession session,String account,String password) {
		//1.����Service�㷽������¼
		SverResponse<User> response = actionUserService.doLogin(account, password);
		//2.�ж��Ƿ��ܵ�¼
		if(response.isSuccess()) {
			//3.�ܵ�¼���ж��Ƿ��ǹ���Ա���ǹ���Ա�����session,������ʾ������Ϣ
			User user = response.getData();
			if(user.getRole()==ConstUtil.Role.ROLE_ADMIN) {
				session.setAttribute(ConstUtil.CUR_USER, user);
				return response;
			}
			return SverResponse.createByErrorMessage("���ǹ���Ա���޷���¼��");
		}
		return response;
	}
	@RequestMapping("/finduserlist.do")
	@ResponseBody
	public SverResponse<List<ActionUserVo>> getUserDetail(HttpSession session) {
		//1.�ж��û��Ƿ��¼
		User user = (User)session.getAttribute(ConstUtil.CUR_USER);
		if(user == null) {
			return SverResponse.createByErrorCodeMessage(405,"���¼���ٽ��в���");
		}
		//2.�жϵ�¼���û��Ƿ��ǹ���Ա
		SverResponse<String> response = actionUserService.isAdmin(user);
		if(response.isSuccess()) {
			//3.����service�㷽����������û�
			return actionUserService.findUserList();
		}
		return SverResponse.createByErrorMessage("���޲���Ȩ�ޣ�");
	}
	
	@RequestMapping("/finduser.do")
	@ResponseBody
	public SverResponse<ActionUserVo> findUser(HttpSession session,Integer id){
		//1.�ж��û��Ƿ��¼
		User user = (User)session.getAttribute(ConstUtil.CUR_USER);
		if(user == null) {
			return SverResponse.createByErrorCodeMessage(405,"���¼���ٽ��в���");
		}
		//2.�жϵ�¼���û��Ƿ��ǹ���Ա
		SverResponse<String> response = actionUserService.isAdmin(user);
		if(response.isSuccess()) {
		//3.����service�㷽�����Id��Ӧ�û�
			return actionUserService.findUser(id);
		}
		return SverResponse.createByErrorMessage("���޲���Ȩ�ޣ�");
	}
	@RequestMapping("/updateuser.do")
	@ResponseBody
	public SverResponse<User> updateUser(HttpSession session,ActionUserVo userVo){
		//1.�ж��û��Ƿ��¼
		User user = (User)session.getAttribute(ConstUtil.CUR_USER);
		if(user == null) {
			return SverResponse.createByErrorCodeMessage(405,"���¼���ٽ��в���");
		}
		//2.�жϵ�¼���û��Ƿ��ǹ���Ա
		SverResponse<String> response = actionUserService.isAdmin(user);
		if(response.isSuccess()) {	
		//3.����service�㷽�������û�
			return actionUserService.updateUserInfo(userVo);
		}
		return SverResponse.createByErrorMessage("���޲���Ȩ�ޣ�");
	}
	/**
	 * ɾ���û�
	 * @param session
	 * @param id
	 * @return
	 */
	@RequestMapping("/deleteusers.do")
	@ResponseBody
	public SverResponse<String> delUsers(HttpSession session,Integer id){
		//1.�ж��û��Ƿ��¼
		User user = (User)session.getAttribute(ConstUtil.CUR_USER);
		if(user == null) {
			return SverResponse.createByErrorCodeMessage(405,"���¼���ٽ��в���");
		}
		//2.�жϵ�¼���û��Ƿ��ǹ���Ա
		SverResponse<String> response = actionUserService.isAdmin(user);
		if(response.isSuccess()) {	
			//3.����service��ɾ������
			return actionUserService.delUser(id);
		}
		return SverResponse.createByErrorMessage("���޲���Ȩ��!");
	}
}
