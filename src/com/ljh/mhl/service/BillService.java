package com.ljh.mhl.service;

import com.ljh.mhl.dao.BillDao;
import com.ljh.mhl.dao.MultiTableBeanDao;
import com.ljh.mhl.pojo.Bill;
import com.ljh.mhl.pojo.Menu;
import com.ljh.mhl.pojo.MultiTableBean;

import java.util.List;
import java.util.UUID;

public class BillService {
    private BillDao billDao = new BillDao();
    private MenuService menuService = new MenuService();
    private DiningTableService diningTableService = new DiningTableService();
    private MultiTableBeanDao multiTableBeanDao = new MultiTableBeanDao();

    //生成账单信息
    public boolean generateBill(int menuId, int num, int diningTableid) {
        //通过uuid生成一个随机账单号
        String billId = UUID.randomUUID().toString();
        String sql = "insert into bill values(null,?,?,?,?,?,now(),'未结账')";
        Menu menu = menuService.getMenuById(menuId);
        int i = billDao.dmlUpdate(sql, billId, menuId, num, menu.getPrice() * num, diningTableid);
        if (i <= 0) {
            return false;
        }
        //更改餐桌状态为“用餐中”
        return diningTableService.updateDiningTableState(diningTableid, "就餐中");
    }

    //显示账单信息
    public List<Bill> orderList() {
        String sql = "select *from bill";
        return billDao.queryMultiply(sql, Bill.class);
    }

    //显示账单信息和菜品名称，价格
    public List<MultiTableBean> orderListAndMenu() {
        String sql = "select bill.*,name,price from bill,menu where bill.menuId = menu.id";
        return multiTableBeanDao.queryMultiply(sql, MultiTableBean.class);
    }

    //验证餐桌是否有要结账的账单
    public boolean hasCheckOut(int diningid) {
        String sql = "select * from bill where diningTableId = ? and state = '未结账' limit 0,1";
        Bill bill = billDao.querySingle(sql, Bill.class, diningid);
        return bill != null;
    }

    //更改账单状态
    public boolean updateOrderState(int diningid, String state) {
        //更新账单表
        String sql = "update bill set state = ? where diningTableId = ? and state = '未结账'";
        int i = billDao.dmlUpdate(sql, state, diningid);
        if (i <= 0) {
            return false;
        }
        //清空DiningTable表的餐桌状态和信息
        if (!diningTableService.updateDiningTableclear(diningid, "空")) {
            return false;
        }
        return true;
    }
}
