package com.ljh.mhl.service;

import com.ljh.mhl.dao.EmployeeDao;
import com.ljh.mhl.pojo.Employee;

public class EmployeeService {
    private  EmployeeDao employeeDao = new EmployeeDao();
    //验证登录
    public Employee isLogin(String empid,String password){
        String sql = "select * from employee where empid = ? and password = md5(?) ";
        return employeeDao.querySingle(sql, Employee.class, empid, password);
    }
}
