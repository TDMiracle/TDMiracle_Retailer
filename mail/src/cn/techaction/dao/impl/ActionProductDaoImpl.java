package cn.techaction.dao.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ColumnListHandler;
import org.springframework.stereotype.Repository;
import cn.techaction.dao.ActionProductDao;
import cn.techaction.pojo.ActionProduct;

@Repository
public class ActionProductDaoImpl implements ActionProductDao {
	@Override
	public List<ActionProduct> findProductsByInfo(Integer productId, Integer partsId, Integer startIndex,
			Integer pageSize) {
		String sql = "select id,name,product_id as productId,parts_id as partsId,icon_url as iconUrl,sub_images as subImage,detail,spec_param as specParam,price,stock,status,is_hot as isHot,created,updated " + 
				"from action_products where 1=1;";
		List<Object> params = new ArrayList<>();
		if(productId!=null) {
			sql += " and product_id=?";
			params.add(productId);
		}
		if(partsId!=null) {
			sql += " and parts_id=?";
			params.add(partsId);
		}
		sql += " limit ?,?";
		params.add(startIndex);
		params.add(pageSize);
		try {
			return queryRunner.query(sql, new BeanListHandler<ActionProduct>(ActionProduct.class),params.toArray());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	@Resource
	private QueryRunner queryRunner;
	@Override
	public Integer getTotalCount(Integer productId, Integer partsId) {
		String sql = "select count(*) as num from action_products where 1=1";
		List<Object> params = new ArrayList<>();
		if(productId!=null) {
			sql += " and product_id=?";
			params.add(productId);
		}
		if(partsId!=null) {
			sql += " and parts_id=?";
			params.add(partsId);
		}
		try {
			return queryRunner.query(sql, new ColumnListHandler<Long>("num"),params.toArray()).get(0).intValue();
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}
		
	}

}
