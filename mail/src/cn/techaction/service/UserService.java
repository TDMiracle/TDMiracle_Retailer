package cn.techaction.service;

import cn.techaction.common.SverResponse;
import cn.techaction.pojo.User;
import cn.techaction.vo.ActionUserVo;

public interface UserService {

	/**
	 * 用户登录
	 * @param account
	 * @param password
	 * @return
	 */
	SverResponse<User> doLogin(String account, String password);
	
	/**
	 * 用户注册
	 * @param user
	 * @return
	 */
	SverResponse<String> doRegister(User user);
	/**
	 *     信息校验验证
	 * @param str
	 * @param type
	 * @return
	 */
	public SverResponse<String> checkValidation(String str,String type);
	/**
	 * 根据用户名获得用户对象
	 * @param account
	 * @return
	 */
	SverResponse<User> findUserByAccount(String account);
	/**
	 * 校验用户问题答案
	 * @param account
	 * @param question
	 * @param asw
	 * @return
	 */
	SverResponse<String> checkUserAnswer(String account, String question, String asw);
	/**
	 * 重置用户密码
	 * @param userId
	 * @param newpwd
	 * @return
	 */
	SverResponse<String> resetPassword(Integer userId, String newpwd);
	/**
	 * 更新用户信息
	 * @param userVo
	 * @return
	 */
	SverResponse<User> updateUserInfo(ActionUserVo userVo);
	/**
	 * 更新用户密码
	 * @param user
	 * @param newpwd
	 * @param oldpwd
	 * @return
	 */
	SverResponse updatePassword(User user, String newpwd, String oldpwd);
}
