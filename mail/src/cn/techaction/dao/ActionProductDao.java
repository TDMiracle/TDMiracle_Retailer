package cn.techaction.dao;

import java.util.List;

import cn.techaction.pojo.ActionProduct;

public interface ActionProductDao {
	/**
	 *     �������������Ʒ��Ŀ
	 * @param productId
	 * @param partsId
	 * @return
	 */
	public Integer getTotalCount(Integer productId,Integer partsId);
	/**
	 * 
	 * @param productId
	 * @param partsId
	 * @param startIndex
	 * @param pageSize
	 * @return
	 */
	public List<ActionProduct> findProductsByInfo(Integer productId,Integer partsId,Integer startIndex,Integer pageSize);
}
