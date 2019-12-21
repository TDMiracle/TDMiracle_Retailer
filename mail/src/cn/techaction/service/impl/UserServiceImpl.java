package cn.techaction.service.impl;

import java.util.Date;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.techaction.common.SverResponse;
import cn.techaction.dao.ActionUserDao;
import cn.techaction.pojo.User;
import cn.techaction.service.UserService;
import cn.techaction.utils.ConstUtil;
import cn.techaction.utils.MD5Util;
import cn.techaction.utils.TokenCache;
import cn.techaction.vo.ActionUserVo;

@Service
public class UserServiceImpl implements UserService {
	@Autowired
	private ActionUserDao actionUserDao;

	@Override
	public SverResponse<User> doLogin(String account, String password) {
		// �ж��û��Ƿ����
		int rs = actionUserDao.checkUserByAccount(account);
		if(rs == 0) {
			return SverResponse.createByErrorMessage("�û��������ڣ�");
		}
		// �����û�������������û�
		String md5PwdString = MD5Util.MD5Encode(password, "utf-8", false);
		User user = actionUserDao.findUserByAccountAndPassword(account, md5PwdString);
		// �������ÿգ���ֹй��
		if(user == null) {
			return SverResponse.createByErrorMessage("�������");
		}
		user.setPassword(StringUtils.EMPTY);
		return SverResponse.createRespBySuccessMessage("��¼�ɹ���");
	}

	@Override
	public SverResponse<String> doRegister(User user) {
		// ����û����Ƿ����
		SverResponse<String> resp = checkValidation(user.getAccount(),ConstUtil.TYPE_ACCOUNT);
		if(!resp.isSuccess()) {
			return resp;
		}
		// ��������Ƿ�ע��
		resp = checkValidation(user.getEmail(),ConstUtil.TYPE_EMAIL);
		if(!resp.isSuccess()) {
			return resp;
		}
		// ָ���û���ɫ��ͨ��ǰ��ע����û���Ϊ�ͻ�
		user.setRole(ConstUtil.Role.ROLE_CUSTOMER);
		// �������
		user.setPassword(MD5Util.MD5Encode(user.getPassword(), "utf-8", false));
		//ִ��ע��
		Date curDataDate = new Date();
		user.setCreate_time(curDataDate);
		user.setUpdate_time(curDataDate);
		int rs = actionUserDao.insertUser(user);
		if(rs == 0) {
			return SverResponse.createByErrorMessage("ע��ʧ�ܣ�");
		}
		return SverResponse.createRespBySuccessMessage("ע��ɹ���");
	}

	@Override
	public SverResponse<String> checkValidation(String str, String type) {
		// �ж��ַ�����Ϊ��type
		if(StringUtils.isNoneBlank(type)) {
			if(ConstUtil.TYPE_ACCOUNT.equals(type)) {
				int rs = actionUserDao.checkUserByAccount(str);
				if(rs > 0) {
					return SverResponse.createByErrorMessage("�û����Ѿ����ڣ�");
				}
			}
			if(StringUtils.isNotBlank(type)) {
				if(ConstUtil.TYPE_EMAIL.equals(type)) {
					int rs = actionUserDao.checkUserByEmail(str);
					if(rs > 0) {
						return SverResponse.createByErrorMessage("�����Ѿ����ڣ�");
					}
				}
			}
			if(StringUtils.isNotBlank(type)) {
				if(ConstUtil.TYPE_PHONE.equals(type)) {
					int rs = actionUserDao.checkUserByPhone(str);
					if(rs > 0) {
						return SverResponse.createByErrorMessage("�绰�����Ѿ����ڣ�");
					}
				}
			}
		}else {
			return SverResponse.createByErrorMessage("��Ϣ��֤���ʧ��");
		}
		return SverResponse.createRespBySuccessMessage("��Ϣ��֤�ɹ���");
	}

	@Override
	public SverResponse<User> findUserByAccount(String account) {
		// ͨ���û������ҵ��û�
		User user = actionUserDao.findUserByAccount(account);
		if(user == null) {
			return SverResponse.createByErrorMessage("�û�������");
		}
		// �������ÿ�,��ȫ�����ÿ�
		user.setPassword(StringUtils.EMPTY);
		user.setAsw(StringUtils.EMPTY);
		return SverResponse.createRespBySuccess(user);
	}

	@Override
	public SverResponse<String> checkUserAnswer(String account, String question, String asw) {
		// 1.����ȡУ����
		int rs = actionUserDao.checkUserAnswer(account,question,asw);
		if(rs > 0) {
			//����ȷ,����token
			String token = UUID.randomUUID().toString();
			//���뻺��
			TokenCache.setCacheData(TokenCache.PREFIX+account, token);
			return SverResponse.createRespBySuccessMessage(token);
		}
		return SverResponse.createByErrorMessage("����𰸴���");
	}

	@Override
	public SverResponse<String> resetPassword(Integer userId, String newpwd) {
		// 1.�������
		String  pwd = MD5Util.MD5Encode(newpwd, "utf-8", false);
		// 2.���user����
		User user = actionUserDao.findUserById(userId);
		// 3.��������
		user.setPassword(pwd);
		user.setUpdate_time(new Date());
		int rs = actionUserDao.updateUserInfo(user);
		if(rs > 0) {
			return SverResponse.createRespBySuccessMessage("�����޸ĳɹ���");
		}
		return SverResponse.createByErrorMessage("�����޸�ʧ�ܣ�");
	}

	@Override
	public SverResponse<User> updateUserInfo(ActionUserVo userVo) {
		// 1.����id���user����
		User updateUser = actionUserDao.findUserById(userVo.getId());
		// 2.�޸�����
		updateUser.setAccount(userVo.getAccount());
		updateUser.setEmail(userVo.getEmail());
		updateUser.setPhone(userVo.getPhone());
		updateUser.setUpdate_time(new Date());
		updateUser.setAge(userVo.getAge());
		// 3.�ж���Ů
		if(userVo.getSex().equals("��")) {
			updateUser.setSex(1);
		}else {
			updateUser.setSex(0);
		}
		updateUser.setName(userVo.getName());
		int rs = actionUserDao.updateUserInfo(updateUser);
		if(rs > 0) {
			return SverResponse.createRespBySuccess("�û���Ϣ�޸ĳɹ���", updateUser);
		}
		return SverResponse.createByErrorMessage("�û���Ϣ�޸�ʧ��!");
	}

	@Override
	public SverResponse updatePassword(User user, String newpwd, String oldpwd) {
		// 1.��ֹԽȨ�����������Ƿ���ȷ
		oldpwd = MD5Util.MD5Encode(oldpwd, "utf-8", false);
		int rs = actionUserDao.checkPassword(user.getAccount(),oldpwd);
		if(rs == 0) {
			return SverResponse.createByErrorMessage("ԭʼ�������");
		}
		// 2.��������������ݿ�
		newpwd = MD5Util.MD5Encode(newpwd, "utf-8", false);
		user.setPassword(newpwd);
		user.setUpdate_time(new Date());
		rs = actionUserDao.updateUserInfo(user);
		if(rs > 0) {
			return SverResponse.createRespBySuccessMessage("�����޸ĳɹ���");
		}
		return SverResponse.createByErrorMessage("�����޸�ʧ��!");
	}

	
}
