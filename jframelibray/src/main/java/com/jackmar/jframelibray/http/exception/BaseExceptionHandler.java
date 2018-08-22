package com.jackmar.jframelibray.http.exception;

import java.lang.Thread.UncaughtExceptionHandler;
import java.text.DateFormat;

/**
 *
 * @Title 系统异常处理基类
 * @Author luojiang
 * @Date 2016-05-23 16:13
 * 
 */
public abstract class BaseExceptionHandler implements UncaughtExceptionHandler
{
	public static final String TAG = "CrashHandler";
	
	protected static final DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.FULL);

	/**
	 * 未捕获异常跳转
	 * 
	 */
	@Override
	public void uncaughtException(Thread thread, final Throwable ex)
	{
		// 如果正确处理了未捕获异常
		if (handleException(ex))
		{
			try
			{
				// 如果处理了，让程序继续运行3秒再退出，保证错误日志的保存
				Thread.sleep(3000);
			} catch (InterruptedException e)
			{
				e.printStackTrace();
			}

			// 退出程序
			android.os.Process.killProcess(android.os.Process.myPid());
			System.exit(1);
		}
	}

	/**
	 * 
	 * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成. 开发者可以根据自己的情况来自定义异常处理逻辑
	 * 
	 * @param ex
	 * @return
	 * 
	 */
	public abstract boolean handleException(Throwable ex);

}
