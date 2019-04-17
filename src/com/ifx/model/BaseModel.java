package com.ifx.model;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Table;
import com.jfinal.plugin.activerecord.TableMapping;


/**
* <p>描述: 基础model类</p>
* <p>Copyright: Copyright (c) 2017 allen</p>
* @author allen
* @date Aug 2, 2017 3:04:54 PM
* @version 1.0
*/
public abstract class BaseModel<M extends Model<M>> extends Model<M>{

	/**
	* @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么)
	*/
	private static final long serialVersionUID = -1520130528291068609L;
	
	
	public Table getTable() {
		return TableMapping.me().getTable(getUsefulClass());
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Class<? extends Model> getUsefulClass() {
		Class c = getClass();
		return c.getName().indexOf("EnhancerByCGLIB") == -1 ? c : c.getSuperclass();	// com.demo.blog.Blog$$EnhancerByCGLIB$$69a17158
	}
}
