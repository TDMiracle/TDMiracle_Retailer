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
	 * �����˺�
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
	/**
	 * ��֤�����Ƿ�ע��
	 * @param str
	 * @return
	 */
	public int checkUserByEmail(String email);
	/**
	 * ��֤�绰�����Ƿ�ע��
	 * @param str
	 * @return
	 */
	public int checkUserByPhone(String phone);
	/**
	 * �����û�
	 * @param user
	 * @return
	 */
	public int insertUser(User user);
	/**
	 * ����account�����û�
	 * @param account
	 * @return
	 */
	public User findUserByAccount(String account);
	/**
	 * ����û���������Ĵ�
	 * @param account
	 * @param question
	 * @param asw
	 * @return
	 */
	public int checkUserAnswer(String account, String question, String asw);
	/**
	 * ��֤�û������Ƿ��Ѿ�����
	 * @param account
	 * @param oldpwd
	 * @return
	 */
	public int checkPassword(String account, String oldpwd);
}
