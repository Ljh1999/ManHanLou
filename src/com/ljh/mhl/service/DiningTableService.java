package com.ljh.mhl.service;

import com.ljh.mhl.dao.DiningTableDao;
import com.ljh.mhl.pojo.DiningTable;

import java.util.List;

public class DiningTableService {
    private DiningTableDao diningTableDao = new DiningTableDao();

    //显示餐桌
    public List<DiningTable> showDiningTable() {
        String sql = "select  id,state from diningTable ";
        return diningTableDao.queryMultiply(sql, DiningTable.class);
    }

    //通过id，查找餐桌编号是否存在并且状态能不能预定
    public DiningTable getDiningTableById(int id){
        String sql = "select * from diningTable where id = ?";
        return diningTableDao.querySingle(sql,DiningTable.class,id);
    }
    //更新预定餐桌信息
    public int updateDiningTable(int id,String ordername,String orderTel){
        String sql = "update diningTable set state = ?,orderName = ?,orderTel = ? where id = ? ";
        return diningTableDao.dmlUpdate(sql,"已预定",ordername,orderTel,id);
    }

    //更新餐桌状态
    public boolean updateDiningTableState(int diningid,String state){
        String sql = "update diningTable set state = ? where id = ?";
        int i = diningTableDao.dmlUpdate(sql, state, diningid);
        return i > 0;
    }
    //结账后餐桌清空
    public boolean updateDiningTableclear(int diningid,String state){
        String sql = "update diningTable set state = ?,orderName = '',orderTel = '' where id = ? ";
        int i = diningTableDao.dmlUpdate(sql, state, diningid);
        return i > 0;
    }

}
