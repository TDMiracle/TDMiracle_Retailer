package cn.techaction.dao;

import java.util.List;

import cn.techaction.pojo.User;

public interface ActionUserDao {
	/**
	 * �����û��������û�
	 * @param account
	 * @return
	 */
	public int checkUserByAccount(String account);
	/**
	 * �����û�������������û�
	 * @param account
	 * @param password
	 * @return
	 */
	public User findUserByAccountAndPassword(String account,String password);
	/**
	 * ���������û�
	 * @return
	 */
	public List<User> findAllUsers();
	/**
	 * ����Id�����û�
	 * @param Id
	 * @return
	 */
	public User findUserById(Integer Id);
	/**
	 * �����û���Ϣ
	 * @param user
	 * @return
	 */
	public int updateUserInfo(User user);
}
