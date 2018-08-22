package com.jackmar.jframelibray.http.exception;

/**
 * @Title 自定义异常
 * @Author luojiang
 * @Date 2016-05-23 16:13
 *
 */
public class BaseException extends RuntimeException
{

	private static final long serialVersionUID = 1L;

	public BaseException()
	{
		super();
	}

	public BaseException(String detailMessage, Throwable throwable)
	{
		super(detailMessage, throwable);
	}

	public BaseException(String detailMessage)
	{
		super(detailMessage);
	}

	public BaseException(Throwable throwable)
	{
		super(throwable);
	}

}
