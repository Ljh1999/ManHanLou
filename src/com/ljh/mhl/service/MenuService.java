package com.ljh.mhl.service;

import com.ljh.mhl.dao.MenuDao;
import com.ljh.mhl.pojo.Menu;

import java.util.List;


public class MenuService {
    private MenuDao menuDao = new MenuDao();

    //显示所有菜品
    public List<Menu> getMenuList() {
        String sql = "select * from menu";
        return menuDao.queryMultiply(sql, Menu.class);
    }

    //通过菜品id查询菜品是否存在
    public Menu getMenuById(int menuId) {
        String sql = "select * from menu where id = ?";
        return menuDao.querySingle(sql, Menu.class, menuId);
    }

}
