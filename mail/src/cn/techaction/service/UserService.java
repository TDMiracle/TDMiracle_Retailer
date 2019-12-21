package cn.techaction.service;

import cn.techaction.common.SverResponse;
import cn.techaction.pojo.User;
import cn.techaction.vo.ActionUserVo;

public interface UserService {

	/**
	 * �û���¼
	 * @param account
	 * @param password
	 * @return
	 */
	SverResponse<User> doLogin(String account, String password);
	
	/**
	 * �û�ע��
	 * @param user
	 * @return
	 */
	SverResponse<String> doRegister(User user);
	/**
	 *     ��ϢУ����֤
	 * @param str
	 * @param type
	 * @return
	 */
	public SverResponse<String> checkValidation(String str,String type);
	/**
	 * �����û�������û�����
	 * @param account
	 * @return
	 */
	SverResponse<User> findUserByAccount(String account);
	/**
	 * У���û������
	 * @param account
	 * @param question
	 * @param asw
	 * @return
	 */
	SverResponse<String> checkUserAnswer(String account, String question, String asw);
	/**
	 * �����û�����
	 * @param userId
	 * @param newpwd
	 * @return
	 */
	SverResponse<String> resetPassword(Integer userId, String newpwd);
	/**
	 * �����û���Ϣ
	 * @param userVo
	 * @return
	 */
	SverResponse<User> updateUserInfo(ActionUserVo userVo);
	/**
	 * �����û�����
	 * @param user
	 * @param newpwd
	 * @param oldpwd
	 * @return
	 */
	SverResponse updatePassword(User user, String newpwd, String oldpwd);
}
