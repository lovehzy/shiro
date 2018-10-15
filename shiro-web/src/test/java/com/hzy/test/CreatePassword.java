package com.hzy.test;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hzy.utils.HttpClientUtil;

public class CreatePassword {
	static List<String> tmpArr=new ArrayList<>();
	static List<String> pwds=new ArrayList<>();
	static BufferedOutputStream out=null;
	public static void main(String[] args) {
		try {
			out=new BufferedOutputStream(new FileOutputStream(new File("E:\\test\\password.txt"), true));
			String str="1,2,3,4,5,6,7,8,9";
			String[] s = str.split(",");
			repeatableArrangement(3, s);
			Map<String,String> param=new HashMap<>();
			for (String pwd : pwds) {
				param.put("username", "hzy");
				param.put("password",pwd);
				String info = HttpClientUtil.doPost("http://localhost:8080/login", param);
				System.out.println(pwd);
				if(info.equals("")) {
					System.out.println("密码是:"+pwd);
					break;
				}
				param.clear();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
     * 可重复排列
     * 类似自己和自己笛卡尔积，类似k层循环拼接的结果,元素个数[arr.len^3]
     * @param k 选取的元素个数（k层循环）
     * @param arr 数组
     */
    public static void repeatableArrangement(int k,String []arr){
        if(k==1){
            for(int i=0;i<arr.length;i++){
                tmpArr.add(arr[i]);
                String pwd = "";
                for(String str: tmpArr) {
                	pwd=pwd+str;
                }
                /*try {
                	pwd=pwd+",";
					out.write(pwd.getBytes());
					out.flush();
					out.close();
					System.out.println(pwd);
				} catch (IOException e) {
					e.printStackTrace();
				}*/
                pwds.add(pwd); 
                tmpArr.remove(tmpArr.size()-1); //移除尾部元素
            }
        }else if(k >1){
            for(int i=0;i<arr.length;i++){
                tmpArr.add(arr[i]);
                repeatableArrangement(k - 1, arr); //不去重
                tmpArr.remove(tmpArr.size()-1); //移除尾部元素,不能用remove(Object),因为它会移除头部出现的元素，我们这里需要移除的是尾部元素
            }
        }else{
            return;
        }
    }

   
}
