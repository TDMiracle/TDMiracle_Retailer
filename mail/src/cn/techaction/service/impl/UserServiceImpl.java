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
		// 判断用户是否存在
		int rs = actionUserDao.checkUserByAccount(account);
		if(rs == 0) {
			return SverResponse.createByErrorMessage("用户名不存在！");
		}
		// 根据用户名和密码查找用户
		String md5PwdString = MD5Util.MD5Encode(password, "utf-8", false);
		User user = actionUserDao.findUserByAccountAndPassword(account, md5PwdString);
		// 将密码置空，防止泄密
		if(user == null) {
			return SverResponse.createByErrorMessage("密码错误！");
		}
		user.setPassword(StringUtils.EMPTY);
		return SverResponse.createRespBySuccessMessage("登录成功！");
	}

	@Override
	public SverResponse<String> doRegister(User user) {
		// 检查用户名是否存在
		SverResponse<String> resp = checkValidation(user.getAccount(),ConstUtil.TYPE_ACCOUNT);
		if(!resp.isSuccess()) {
			return resp;
		}
		// 检查邮箱是否被注册
		resp = checkValidation(user.getEmail(),ConstUtil.TYPE_EMAIL);
		if(!resp.isSuccess()) {
			return resp;
		}
		// 指定用户角色，通过前端注册的用户都为客户
		user.setRole(ConstUtil.Role.ROLE_CUSTOMER);
		// 密码加密
		user.setPassword(MD5Util.MD5Encode(user.getPassword(), "utf-8", false));
		//执行注册
		Date curDataDate = new Date();
		user.setCreate_time(curDataDate);
		user.setUpdate_time(curDataDate);
		int rs = actionUserDao.insertUser(user);
		if(rs == 0) {
			return SverResponse.createByErrorMessage("注册失败！");
		}
		return SverResponse.createRespBySuccessMessage("注册成功！");
	}

	@Override
	public SverResponse<String> checkValidation(String str, String type) {
		// 判断字符串不为空type
		if(StringUtils.isNoneBlank(type)) {
			if(ConstUtil.TYPE_ACCOUNT.equals(type)) {
				int rs = actionUserDao.checkUserByAccount(str);
				if(rs > 0) {
					return SverResponse.createByErrorMessage("用户名已经存在！");
				}
			}
			if(StringUtils.isNotBlank(type)) {
				if(ConstUtil.TYPE_EMAIL.equals(type)) {
					int rs = actionUserDao.checkUserByEmail(str);
					if(rs > 0) {
						return SverResponse.createByErrorMessage("邮箱已经存在！");
					}
				}
			}
			if(StringUtils.isNotBlank(type)) {
				if(ConstUtil.TYPE_PHONE.equals(type)) {
					int rs = actionUserDao.checkUserByPhone(str);
					if(rs > 0) {
						return SverResponse.createByErrorMessage("电话号码已经存在！");
					}
				}
			}
		}else {
			return SverResponse.createByErrorMessage("信息验证类别失败");
		}
		return SverResponse.createRespBySuccessMessage("信息验证成功！");
	}

	@Override
	public SverResponse<User> findUserByAccount(String account) {
		// 通过用户名查找到用户
		User user = actionUserDao.findUserByAccount(account);
		if(user == null) {
			return SverResponse.createByErrorMessage("用户名错误！");
		}
		// 将密码置空,安全问题置空
		user.setPassword(StringUtils.EMPTY);
		user.setAsw(StringUtils.EMPTY);
		return SverResponse.createRespBySuccess(user);
	}

	@Override
	public SverResponse<String> checkUserAnswer(String account, String question, String asw) {
		// 1.。获取校验结果
		int rs = actionUserDao.checkUserAnswer(account,question,asw);
		if(rs > 0) {
			//答案正确,生成token
			String token = UUID.randomUUID().toString();
			//放入缓存
			TokenCache.setCacheData(TokenCache.PREFIX+account, token);
			return SverResponse.createRespBySuccessMessage(token);
		}
		return SverResponse.createByErrorMessage("问题答案错误！");
	}

	@Override
	public SverResponse<String> resetPassword(Integer userId, String newpwd) {
		// 1.密码加密
		String  pwd = MD5Util.MD5Encode(newpwd, "utf-8", false);
		// 2.获得user对象
		User user = actionUserDao.findUserById(userId);
		// 3.更新密码
		user.setPassword(pwd);
		user.setUpdate_time(new Date());
		int rs = actionUserDao.updateUserInfo(user);
		if(rs > 0) {
			return SverResponse.createRespBySuccessMessage("密码修改成功！");
		}
		return SverResponse.createByErrorMessage("密码修改失败！");
	}

	@Override
	public SverResponse<User> updateUserInfo(ActionUserVo userVo) {
		// 1.根据id获得user对象
		User updateUser = actionUserDao.findUserById(userVo.getId());
		// 2.修改数据
		updateUser.setAccount(userVo.getAccount());
		updateUser.setEmail(userVo.getEmail());
		updateUser.setPhone(userVo.getPhone());
		updateUser.setUpdate_time(new Date());
		updateUser.setAge(userVo.getAge());
		// 3.判断男女
		if(userVo.getSex().equals("男")) {
			updateUser.setSex(1);
		}else {
			updateUser.setSex(0);
		}
		updateUser.setName(userVo.getName());
		int rs = actionUserDao.updateUserInfo(updateUser);
		if(rs > 0) {
			return SverResponse.createRespBySuccess("用户信息修改成功！", updateUser);
		}
		return SverResponse.createByErrorMessage("用户信息修改失败!");
	}

	@Override
	public SverResponse updatePassword(User user, String newpwd, String oldpwd) {
		// 1.防止越权，检测旧密码是否正确
		oldpwd = MD5Util.MD5Encode(oldpwd, "utf-8", false);
		int rs = actionUserDao.checkPassword(user.getAccount(),oldpwd);
		if(rs == 0) {
			return SverResponse.createByErrorMessage("原始密码错误！");
		}
		// 2.将新密码插入数据库
		newpwd = MD5Util.MD5Encode(newpwd, "utf-8", false);
		user.setPassword(newpwd);
		user.setUpdate_time(new Date());
		rs = actionUserDao.updateUserInfo(user);
		if(rs > 0) {
			return SverResponse.createRespBySuccessMessage("密码修改成功！");
		}
		return SverResponse.createByErrorMessage("密码修改失败!");
	}

	
}
