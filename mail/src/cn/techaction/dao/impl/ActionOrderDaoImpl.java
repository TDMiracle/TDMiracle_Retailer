package cn.techaction.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.springframework.stereotype.Repository;

import cn.techaction.dao.ActionOrderDao;
import cn.techaction.pojo.ActionOrder;

@Repository
public class ActionOrderDaoImpl implements ActionOrderDao {
	@Resource
	QueryRunner queryRunner;
	@Override
	public List<ActionOrder> findOrderByUid(Integer uid) {
		String sql="select * from action_orders where uid=?";
		try {
			return queryRunner.query(sql,new BeanListHandler<ActionOrder>(ActionOrder.class),uid); 
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
	}

}
