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
		//1.根据id获得User对象
		//2.把userVo修改的属性值赋值给User对象
		//3.调用dao层方法
		User user = actionUserDao.findUserById(userVo.getId());
		user.setAccount(userVo.getAccount());
		user.setAge(userVo.getAge());
		user.setEmail(userVo.getEmail());
		user.setName(userVo.getName());
		user.setPhone(userVo.getPhone());
		if(userVo.getSex().equals("男")) {
			user.setSex(1);
		}else {
			user.setSex(0);
		}
		user.setUpdate_time(new Date());
		int rs = actionUserDao.updateUserInfo(user);
		if(rs > 0) {
			return SverResponse.createRespBySuccess("用户信息修改成功！",user);
		}
		return SverResponse.createByErrorMessage("用户信息修改失败！");
	}
	
	@Override
	public SverResponse<ActionUserVo> findUser(Integer Id) {
		//1.调用dao层方法
		User user = actionUserDao.findUserById(Id);
		//2.将user转换 成actionUserVo对象
		ActionUserVo userVo = setNormalProperty(user);
		return SverResponse.createRespBySuccess(userVo);
	}
	@Override
	public SverResponse<User> doLogin(String account, String password) {
		//1.判断账号、用户名是否存在
		int rs = actionUserDao.checkUserByAccount(account);
		//2.存在，根据用户名密码 查找用户
			//2.1md5加密后对比
		String md5pwd = MD5Util.MD5Encode(password, "utf-8",false);
		if(rs > 0) {
			User user = actionUserDao.findUserByAccountAndPassword(account, md5pwd);
			//3.判断是否找到
			if(user==null) {
				return SverResponse.createByErrorMessage("密码错误");
			}
			else {
				//用户存在,制空密码，写回session
				user.setPassword(StringUtils.EMPTY);
				return SverResponse.createRespBySuccess("登陆成功！", user);
			}
		}
		else {
			return SverResponse.createByErrorMessage("用户不存在！");
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
		//1.调用dao层类方法
		List<User> users = actionUserDao.findAllUsers();
		List<ActionUserVo> usersVo = Lists.newArrayList();
		//2.处理：user对象转换成actionUserVo方法()
		for(User u:users) {
			usersVo.add(setNormalProperty(u));
		}
		return SverResponse.createRespBySuccess(usersVo);
	}
	/**
	 * 将user转换成actionUserVo对象
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
			vo.setSex("男");
		}
		else {
			vo.setSex("女");
		}
		return vo;
	}

	@Override
	public SverResponse<String> delUser(Integer id) {
		//1.判断用户是否有订单，如果没有订单则可以删除
		if(actionOrderDao.findOrderByUid(id).size()>0) {
			return SverResponse.createByErrorMessage("用户存在关联的订单无法删除");
		}
		//2.删除用户实际是修改用户del状态字段
		User user = actionUserDao.findUserById(id);
		user.setDel(1);
		user.setUpdate_time(new Date());
		int rs = actionUserDao.updateUserInfo(user);
		if(rs > 0) {
			return SverResponse.createByErrorMessage("用户删除成功！");
		}
		return SverResponse.createByErrorMessage("用户删除失败!");
	}
	
}
