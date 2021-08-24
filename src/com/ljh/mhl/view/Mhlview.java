package com.ljh.mhl.view;

import com.ljh.mhl.pojo.*;
import com.ljh.mhl.service.BillService;
import com.ljh.mhl.service.DiningTableService;
import com.ljh.mhl.service.EmployeeService;
import com.ljh.mhl.service.MenuService;

import java.util.List;
import java.util.Scanner;

/**
 * 这是主页面
 */
public class Mhlview {
    public static void main(String[] args) {
        new Mhlview().showMenu();
    }

    private EmployeeService employeeService = new EmployeeService();
    private DiningTableService diningTableService = new DiningTableService();
    private MenuService menuService = new MenuService();
    private BillService billService = new BillService();
    private boolean loop = true;
    String key = "";
    Scanner scanner = new Scanner(System.in);

    //显示餐桌状态列表
    public void showDiningList() {
        List<DiningTable> diningTables = diningTableService.showDiningTable();
        System.out.println("餐桌编号 \t 餐桌状态");
        for (DiningTable diningTable : diningTables) {
            System.out.println("\t" + diningTable.getId() + "\t\t" + diningTable.getState());
        }
        System.out.println("==========显示完毕==========");
    }

    //完成订座
    public void orderDiningTable() {
        System.out.println("==========预定餐桌==========");
        System.out.println("请输入要预定餐桌的编号（-1退出）：");
        int id = scanner.nextInt();
        if (id == -1) {
            System.out.println("您取消了预定！");
            return;
        }
        System.out.print("确认是否预定（y/n）：");
        char c = scanner.next().charAt(0);
        if (c == 'y') {
            DiningTable diningTable = diningTableService.getDiningTableById(id);
            if (diningTable == null) {
                System.out.println("您要预定的餐桌编号不存在！");
                return;
            } else if (!diningTable.getState().equals("空")) {
                System.out.println("餐桌已被使用，不能预定");
                return;
            }
            System.out.print("预订人名字：");
            String orderName = scanner.next();
            System.out.print("预定人电话：");
            String orderTel = scanner.next();
            int i = diningTableService.updateDiningTable(id, orderName, orderTel);
            System.out.println(i > 0 ? "预定成功" : "预定失败");
        } else {
            System.out.println("您取消了预定！");
        }
    }

    //显示菜谱
    public void showMenuList() {
        System.out.println("显示所有菜品");
        List<Menu> menuList = menuService.getMenuList();
        System.out.println("菜品编号\t菜品名\t\t类别\t\t价格");
        for (Menu menu : menuList) {
            System.out.println(menu);
        }
    }

    //点餐服务
    public void order() {
        System.out.println("点餐服务");
        System.out.print("请选择点餐的桌号（-1退出）：");
        int diningid = scanner.nextInt();
        if (diningid == -1) {
            System.out.println("您取消了点餐~");
            return;
        }
        System.out.print("请选择要点的菜品编号（-1退出）：");
        int menuId = scanner.nextInt();
        if (menuId == -1) {
            System.out.println("您取消了点餐~");
            return;
        }
        System.out.print("请选择要点的菜品数量（-1退出）：");
        int menuNum = scanner.nextInt();
        if (menuNum == -1) {
            System.out.println("您取消了点餐~");
            return;
        }
        //验证餐桌号是否存在
        DiningTable diningTable = diningTableService.getDiningTableById(diningid);
        if (diningTable == null) {
            System.out.println("该餐桌不存在");
            return;
        }
        //验证菜品是否存在
        Menu menu = menuService.getMenuById(menuId);
        if (menu == null) {
            System.out.println("该菜品不存在~");
            return;
        }

        System.out.print("确定是否点这个菜（y/n）：");
        char c = scanner.next().charAt(0);
        if (c == 'y') {
            if (billService.generateBill(menuId, menuNum, diningid)) {
                System.out.println("点餐成功");
            } else {
                System.out.println("点餐失败");
            }
        } else {
            System.out.println("您取消了点菜~");
            return;
        }
    }

    //显示账单
    public void showOrder() {
        System.out.println("查看账单");
        System.out.println("编号\t\t菜品号\t\t菜品量\t\t金额\t\t桌号\t\t日期\t\t\t\t\t\t\t状态\t\t\t菜品名\t\t\t金额");
        List<MultiTableBean> multiTableBeans = billService.orderListAndMenu();
        for (MultiTableBean multiTableBean : multiTableBeans) {
            System.out.println(multiTableBean);
        }
    }

    //结账
    public void checkOut() {
        System.out.println("结账");
        System.out.print("请选择要结账的餐桌编号（-1退出）：");
        int diningId = scanner.nextInt();
        if (diningId == -1) {
            System.out.println("您取消了结账~");
            return;
        }
        //验证餐桌号是否存在
        DiningTable diningTable = diningTableService.getDiningTableById(diningId);
        if (diningTable == null) {
            System.out.println("该餐桌不存在");
            return;
        }
        if (!billService.hasCheckOut(diningId)) {
            System.out.println("该餐位没有未结账的账单~");
            return;
        }
        System.out.print("结账的方式（现金/支付宝/微信）回车表示退出：");
        String state = scanner.next();
        if ("".equals(state)) {
            System.out.println("您取消了结账~");
            return;
        }
        System.out.print("确认是否结账：（y/n）");
        char c = scanner.next().charAt(0);
        if (c == 'y') {
            if (billService.updateOrderState(diningId, state)) {
                System.out.println("结账成功");
            } else {
                System.out.println("结账失败");
            }
        } else {
            System.out.println("取消结账~");
            return;
        }
    }

    //显示主菜单
    public void showMenu() {

        while (loop) {
            System.out.println("==========满汉楼==========");
            System.out.println("\t\t1 登录满汉楼");
            System.out.println("\t\t2 退出满汉楼");
            System.out.print("请输入你的选择：");
            key = scanner.next();
            switch (key) {
                case "1":
                    System.out.print("请输入员工号：");
                    String empId = scanner.next();
                    System.out.print("请输入密 码：");
                    String password = scanner.next();
                    //到数据库去判断
                    Employee employee = employeeService.isLogin(empId, password);
                    if (employee != null) {
                        System.out.println("==========登录成功[" + employee.getName() + "]==========");
                        while (loop) {
                            System.out.println("==========满汉楼二级菜单==========");
                            System.out.println("\t\t1 显示餐桌状态");
                            System.out.println("\t\t2 预定餐桌");
                            System.out.println("\t\t3 显示所有菜品");
                            System.out.println("\t\t4 点餐服务");
                            System.out.println("\t\t5 查看账单");
                            System.out.println("\t\t6 结账");
                            System.out.println("\t\t9 退出满汉楼");
                            System.out.print("请输入你的选择：");
                            key = scanner.next();
                            switch (key) {
                                case "1":
                                    showDiningList(); //显示餐桌状态
                                    break;
                                case "2":
                                    orderDiningTable();//订座
                                    break;
                                case "3":
                                    showMenuList(); //显示所有菜品
                                    break;
                                case "4":
                                    order(); //点餐服务
                                    break;
                                case "5":
                                    showOrder();//显示账单
                                    break;
                                case "6":
                                    checkOut(); //结账
                                    break;
                                case "9":
                                    loop = false;
                                    break;
                                default:
                                    System.out.println("你的输入有误，请重新输入~");
                                    break;
                            }
                        }
                    } else {
                        System.out.println("==========登录失败==========");
                    }
                    break;
                case "2":
                    loop = false;
                    break;
                default:
                    System.out.println("你的输入有误！请重新输入");
            }
        }
        System.out.println("退出满汉楼系统~");
    }
}
