package cn.techaction.dao;

import java.util.List;

import cn.techaction.pojo.User;

public interface ActionUserDao {
	/**
	 * 根据用户名查找用户
	 * @param account
	 * @return
	 */
	public int checkUserByAccount(String account);
	/**
	 * 根据账号
	 * @param account
	 * @param password
	 * @return
	 */
	public User findUserByAccountAndPassword(String account,String password);
	/**
	 * 查找所有用户
	 * @return
	 */
	public List<User> findAllUsers();
	/**
	 * 根据Id查找用户
	 * @param Id
	 * @return
	 */
	public User findUserById(Integer Id);
	/**
	 * 更新用户信息
	 * @param user
	 * @return
	 */
	public int updateUserInfo(User user);
	/**
	 * 验证邮箱是否被注册
	 * @param str
	 * @return
	 */
	public int checkUserByEmail(String email);
	/**
	 * 验证电话号码是否被注册
	 * @param str
	 * @return
	 */
	public int checkUserByPhone(String phone);
	/**
	 * 新增用户
	 * @param user
	 * @return
	 */
	public int insertUser(User user);
	/**
	 * 根据account查找用户
	 * @param account
	 * @return
	 */
	public User findUserByAccount(String account);
	/**
	 * 检查用户密码问题的答案
	 * @param account
	 * @param question
	 * @param asw
	 * @return
	 */
	public int checkUserAnswer(String account, String question, String asw);
	/**
	 * 验证用户密码是否已经存在
	 * @param account
	 * @param oldpwd
	 * @return
	 */
	public int checkPassword(String account, String oldpwd);
}
