package com.lx.client.start;

import java.io.File;
import java.text.DecimalFormat;

/**
 * ClassName:StartGame <br/>
 * Function: TODO (启动游戏服务器). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-6-30 上午11:28:19 <br/>
 * 
 * @author lyh
 * @version
 * @see
 */
public class ClientStart {
	
	/**
	 * main:(). <br/>
	 * TODO().<br/>
	 * 
	 * @author lyh
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//File f = new File(System.getProperty("user.dir"));
		//ServerStart.main(new String[] { "61", f.getParent() + File.separator + "Engine", "clientlog" });
		// ServerInfoManage.loadContext(null);
		test();
	/*	DecimalFormat decimalFormat=new DecimalFormat(".0");//构造方法的字符格式这里如果小数不足2位,会以0补足.
		Double i = Double.parseDouble(decimalFormat.format(0.0));
		System.out.println(i);
		Double d=Double.parseDouble(decimalFormat.format(0.1));
        System.out.println("*******"+(d+i+d));*/
	}
	
	public static void test(){
		DecimalFormat decimalFormat=new DecimalFormat(".0");//构造方法的字符格式这里如果小数不足2位,会以0补足.
		//Double i = Double.parseDouble(decimalFormat.format(0.0));
		//Double j = Double.parseDouble(decimalFormat.format(0.0));
		//Double j2 =Double.parseDouble(decimalFormat.format(0.0));
		//Double k = Double.parseDouble(decimalFormat.format(0.0));
		Double d8 =Double.parseDouble(decimalFormat.format(8.0));
		Double d13 = Double.parseDouble(decimalFormat.format(13.0));
		Double d6 = Double.parseDouble(decimalFormat.format(6.0));

		Double num=0.0;
		Double d=Double.parseDouble(decimalFormat.format(0.1));
		for (Double i = Double.parseDouble(decimalFormat.format(0.0)); i < 20.0; i=i+d) {//第一个
			for ( Double j = Double.parseDouble(decimalFormat.format(0.0));j < 20.0; j=j+d) {//
				for ( Double j2 =Double.parseDouble(decimalFormat.format(0.0));j2 < 20.0; j2=j2+d) {
					for ( Double k = Double.parseDouble(decimalFormat.format(0.0));k < 20.0; k=k+d) {
						num++;
						if((i+j==d8.doubleValue())&&(j+k==d13.doubleValue())&&(i+j2==d13.doubleValue())&&(j2-k==d6.doubleValue())){
							System.out.println("i=="+i+"j=="+j+"j2=="+j2+"k=="+k);
							System.out.println(num);
						}else{
							System.out.println("  "+i+" "+j+" "+j2+" "+k);
						}
						System.out.println(num);
					}
				}
			}
		}
	}
	
}
