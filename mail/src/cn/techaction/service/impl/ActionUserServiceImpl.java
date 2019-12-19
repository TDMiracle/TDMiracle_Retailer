package cn.techaction.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

import cn.techaction.common.SverResponse;
import cn.techaction.dao.ActionOrderDao;
import cn.techaction.dao.ActionUserDao;
import cn.techaction.pojo.User;
import cn.techaction.service.ActionUserService;
import cn.techaction.utils.ConstUtil;
import cn.techaction.utils.MD5Util;
import cn.techaction.vo.ActionUserVo;

@Service
public class ActionUserServiceImpl implements ActionUserService {
	@Autowired
	private ActionUserDao actionUserDao;
	@Autowired
	private ActionOrderDao actionOrderDao;
	@Override
	public SverResponse<User> updateUserInfo(ActionUserVo userVo) {
		//1.����id���User����
		//2.��userVo�޸ĵ�����ֵ��ֵ��User����
		//3.����dao�㷽��
		User user = actionUserDao.findUserById(userVo.getId());
		user.setAccount(userVo.getAccount());
		user.setAge(userVo.getAge());
		user.setEmail(userVo.getEmail());
		user.setName(userVo.getName());
		user.setPhone(userVo.getPhone());
		if(userVo.getSex().equals("��")) {
			user.setSex(1);
		}else {
			user.setSex(0);
		}
		user.setUpdate_time(new Date());
		int rs = actionUserDao.updateUserInfo(user);
		if(rs > 0) {
			return SverResponse.createRespBySuccess("�û���Ϣ�޸ĳɹ���",user);
		}
		return SverResponse.createByErrorMessage("�û���Ϣ�޸�ʧ�ܣ�");
	}
	
	@Override
	public SverResponse<ActionUserVo> findUser(Integer Id) {
		//1.����dao�㷽��
		User user = actionUserDao.findUserById(Id);
		//2.��userת�� ��actionUserVo����
		ActionUserVo userVo = setNormalProperty(user);
		return SverResponse.createRespBySuccess(userVo);
	}
	@Override
	public SverResponse<User> doLogin(String account, String password) {
		//1.�ж��˺š��û����Ƿ����
		int rs = actionUserDao.checkUserByAccount(account);
		//2.���ڣ������û������� �����û�
			//2.1md5���ܺ�Ա�
		String md5pwd = MD5Util.MD5Encode(password, "utf-8",false);
		if(rs > 0) {
			User user = actionUserDao.findUserByAccountAndPassword(account, md5pwd);
			//3.�ж��Ƿ��ҵ�
			if(user==null) {
				return SverResponse.createByErrorMessage("�������");
			}
			else {
				//�û�����,�ƿ����룬д��session
				user.setPassword(StringUtils.EMPTY);
				return SverResponse.createRespBySuccess("��½�ɹ���", user);
			}
		}
		else {
			return SverResponse.createByErrorMessage("�û������ڣ�");
		}
	}
	@Override
	public SverResponse<String> isAdmin(User user) {
		if(user.getRole()==ConstUtil.Role.ROLE_ADMIN) {
			return SverResponse.createRespBySuccess();
		}
		return SverResponse.createRespByError();
	}
	@Override
	public SverResponse<List<ActionUserVo>> findUserList() {
		//1.����dao���෽��
		List<User> users = actionUserDao.findAllUsers();
		List<ActionUserVo> usersVo = Lists.newArrayList();
		//2.����user����ת����actionUserVo����()
		for(User u:users) {
			usersVo.add(setNormalProperty(u));
		}
		return SverResponse.createRespBySuccess(usersVo);
	}
	/**
	 * ��userת����actionUserVo����
	 * @param user
	 * @return
	 */
	private ActionUserVo setNormalProperty(User user) {
		ActionUserVo vo = new ActionUserVo();
		vo.setAccount(user.getAccount());
		vo.setAge(user.getAge());
		vo.setEmail(user.getEmail());
		vo.setId(user.getId());
		vo.setName(user.getName());
		vo.setPhone(user.getPhone());
		if(user.getSex()==1) {
			vo.setSex("��");
		}
		else {
			vo.setSex("Ů");
		}
		return vo;
	}

	@Override
	public SverResponse<String> delUser(Integer id) {
		//1.�ж��û��Ƿ��ж��������û�ж��������ɾ��
		if(actionOrderDao.findOrderByUid(id).size()>0) {
			return SverResponse.createByErrorMessage("�û����ڹ����Ķ����޷�ɾ��");
		}
		//2.ɾ���û�ʵ�����޸��û�del״̬�ֶ�
		User user = actionUserDao.findUserById(id);
		user.setDel(1);
		user.setUpdate_time(new Date());
		int rs = actionUserDao.updateUserInfo(user);
		if(rs > 0) {
			return SverResponse.createByErrorMessage("�û�ɾ���ɹ���");
		}
		return SverResponse.createByErrorMessage("�û�ɾ��ʧ��!");
	}
	
}
