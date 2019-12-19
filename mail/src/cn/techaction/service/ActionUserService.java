package cn.techaction.service;

import java.util.List;

import cn.techaction.common.SverResponse;
import cn.techaction.pojo.User;
import cn.techaction.vo.ActionUserVo;

public interface ActionUserService {
	/**
	 * �û���¼
	 * @param account
	 * @param password
	 * @return
	 */
	public SverResponse<User> doLogin(String account,String password);
	/**
	 * �ж��û��Ƿ��ǹ���Ա
	 * @param user
	 * @return
	 */
	public SverResponse<String> isAdmin(User user);
	/**
	 * ������õ��û���Ϣ
	 * @return
	 */
	public SverResponse<List<ActionUserVo>> findUserList();
	/**
	 * ����Id�����û�����
	 * @param Id
	 * @return
	 */
	public SverResponse<ActionUserVo> findUser(Integer Id);
	/**
	 * �����û���Ϣ
	 * @param userVo
	 * @return
	 */
	public SverResponse<User> updateUserInfo(ActionUserVo userVo);
	/**
	 * ɾ���û�
	 * @param id
	 * @return
	 */
	public SverResponse<String> delUser(Integer id);
}
