package cn.techaction.dao;

import java.util.List;

import cn.techaction.pojo.ActionOrder;

public interface ActionOrderDao {
	/**
	 * �����û�id��ö�����Ϣ
	 * @param uid
	 * @return
	 */
	public List<ActionOrder> findOrderByUid(Integer uid);
}
