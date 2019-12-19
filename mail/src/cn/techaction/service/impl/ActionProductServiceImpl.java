package cn.techaction.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.techaction.common.SverResponse;
import cn.techaction.dao.ActionProductDao;
import cn.techaction.pojo.ActionProduct;
import cn.techaction.service.ActionProductService;
import cn.techaction.utils.PageBean;

@Service
public class ActionProductServiceImpl implements ActionProductService {
	@Autowired
	private ActionProductDao actionProductDao;
	@Override
	public SverResponse<PageBean<ActionProduct>> findProduct(Integer productId, Integer partsId, Integer pageNum,
			Integer pageSize) {
		//1.�ȸ�����������dao���ò�ѯ��Ʒ��������
		//2.����dao���÷�ҳ��ѯ����Ʒ��Ϣ
		int totalCount = actionProductDao.getTotalCount(productId, partsId);
		PageBean<ActionProduct> pageBean = new PageBean<>(pageNum, pageSize, totalCount);
		pageBean.setData(actionProductDao.findProductsByInfo(productId, partsId, pageNum, pageSize));
		return SverResponse.createRespBySuccess(pageBean);
	}

}
