package cn.techaction.service;

import cn.techaction.common.SverResponse;
import cn.techaction.pojo.ActionProduct;
import cn.techaction.utils.PageBean;

public interface ActionProductService {
	/**
	 * ��ѯָ����Ʒ�б�
	 * @param productId
	 * @param partsId
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	public SverResponse<PageBean<ActionProduct>> findProduct(Integer productId,Integer partsId,
			Integer pageNum,Integer pageSize);
}
