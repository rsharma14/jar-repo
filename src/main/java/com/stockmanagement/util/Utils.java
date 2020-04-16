package com.stockmanagement.util;


import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.util.UriComponentsBuilder;

public class Utils {

	public static String getUUID() {
		return UUID.randomUUID().toString();
	}

	public static String clearNull(Object obj) {
		return obj == null ? "" : obj.toString();
	}

	public static int getInt(Object obj) {
		try {
			return Integer.parseInt(obj+"");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public static double getDouble(Object obj) {
		try {
			return Double.parseDouble(obj+"");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0.0;
	}
	
	public static Map<String, Object> getRequestParams(HttpServletRequest request) {
		Enumeration<String> parameterNames = request.getParameterNames();
		Map<String, Object> objReq = new HashMap<>();
		while (parameterNames.hasMoreElements()) {
			String paramName = parameterNames.nextElement().trim();

			String[] paramValues = request.getParameterValues(paramName);
			objReq.put(paramName, request.getParameter(paramName).trim());

		}
		return objReq;
	}

	public static Object mapEntityToDTO(Object entityObject, Object dtoObject) {
		Class entityCls = entityObject.getClass();
		Class dtoCls = dtoObject.getClass();

		List<Method> dtoSetter = new ArrayList<Method>();
		List<Method> entityGetter = new ArrayList<Method>();
		
		Method[] dtoMethods = dtoCls.getDeclaredMethods();
		Method[] entityMethods = entityCls.getDeclaredMethods();

		for (Method method : dtoMethods) {
			if (isSetter(method)) {
				dtoSetter.add(method);
			}

		}
		
		for (Method method : entityMethods) {
			if (isGetter(method)) {
				entityGetter.add(method);
			}

		}


		Method m1=null;
		Method m2=null;
		
		for(int i=0;i<entityGetter.size();i++) {
			for(int j=0;j<dtoSetter.size();j++) {
				//System.out.println(entityGetter.get(i).getName().substring(3)+"="+dtoSetter.get(j).getName().substring(3));

				if(entityGetter.get(i).getName().substring(3).equals(dtoSetter.get(j).getName().substring(3))) {
					//System.out.println(entityGetter.get(i).getName().substring(3)+"=="+dtoSetter.get(j).getName().substring(3));

					m1 = entityGetter.get(i);
					m2=dtoSetter.get(j);
					try {
						m2.invoke(dtoObject, m1.invoke(entityObject));
					} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
						System.out.println(e.getMessage()+":"+m1.getName()+"==>"+m2.getName());
					}
					break;
				}
			}
		}

		return dtoObject;
	}

	public static Object mapDTOToEntity(Object dtoObject, Object entityObj) {
		Class entityCls = entityObj.getClass();
		Class dtoCls = dtoObject.getClass();

		List<Method> dtoGetter = new ArrayList<Method>();
		List<Method> entitySetter = new ArrayList<Method>();
		
		Method[] dtoMethods = dtoCls.getDeclaredMethods();
		Method[] entityMethods = entityCls.getDeclaredMethods();

		for (Method method : dtoMethods) {
			if (isGetter(method)) {
				dtoGetter.add(method);
			}

		}
		
		for (Method method : entityMethods) {
			if (isSetter(method)) {
				entitySetter.add(method);
			}

		}


		Method m1=null;
		Method m2=null;
		
		for(int i=0;i<dtoGetter.size();i++) {
			for(int j=0;j<entitySetter.size();j++) {
				//System.out.println(dtoGetter.get(i).getName().substring(3)+"="+entitySetter.get(j).getName().substring(3));

				if(dtoGetter.get(i).getName().substring(3).equals(entitySetter.get(j).getName().substring(3))) {
					//System.out.println(dtoGetter.get(i).getName().substring(3)+"=="+entitySetter.get(j).getName().substring(3));

					m1 = dtoGetter.get(i);
					m2=entitySetter.get(j);
					try {
						m2.invoke(entityObj, m1.invoke(dtoObject));
					} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
						System.out.println(e.getMessage());
					}
					break;
				}
			}
		}

		return entityObj;
	}

	public static Object mapFromObjToObj(Object fromObj, Object toObj) {
		Class toCls = toObj.getClass();
		Class fromCls = fromObj.getClass();

		List<Method> fromGetter = new ArrayList<Method>();
		List<Method> toSetter = new ArrayList<Method>();
		
		Method[] fromMethods = fromCls.getDeclaredMethods();
		Method[] entityMethods = toCls.getDeclaredMethods();

		for (Method method : fromMethods) {
			if (isGetter(method)) {
				fromGetter.add(method);
			}

		}
		
		for (Method method : entityMethods) {
			if (isSetter(method)) {
				toSetter.add(method);
			}

		}


		Method m1=null;
		Method m2=null;
		
		for(int i=0;i<fromGetter.size();i++) {
			for(int j=0;j<toSetter.size();j++) {
				//System.out.println(dtoGetter.get(i).getName().substring(3)+"="+entitySetter.get(j).getName().substring(3));

				if(fromGetter.get(i).getName().substring(3).equals(toSetter.get(j).getName().substring(3))) {
					//System.out.println(fromGetter.get(i).getName().substring(3)+"=="+toSetter.get(j).getName().substring(3));

					m1 = fromGetter.get(i);
					m2=toSetter.get(j);
					try {
						m2.invoke(toObj, m1.invoke(fromObj));
					} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
						System.out.println(e.getMessage());
					}
					break;
				}
			}
		}

		return toObj;
	}
	
	public static boolean isGetter(Method method) {
		if (Modifier.isPublic(method.getModifiers()) && method.getParameterTypes().length == 0) {
			if (method.getName().matches("^get[A-Z].*") && !method.getReturnType().equals(void.class))
				return true;
		}
		return false;
	}

	public static boolean isSetter(Method method) {
		return Modifier.isPublic(method.getModifiers()) && method.getReturnType().equals(void.class)
				&& method.getParameterTypes().length == 1 && method.getName().matches("^set[A-Z].*");
	}
	
	public static String getFormInput(String type,String name,String label,String placeholder,
			Object value,boolean required,Map<String,String> inputs) {
		if(inputs==null)
			inputs=new HashMap<>();
		
		WeakReference<String> labelClass= new WeakReference<String>(Utils.clearNull(inputs.get("labelClass")));
		WeakReference<String> inputClass= new WeakReference<String>(Utils.clearNull(inputs.get("inputClass")));
		WeakReference<String> inputDivClass= new WeakReference<String>(Utils.clearNull(inputs.get("inputDivClass")));

		String formInput="<div class='form-group'>" + 
				"			<label class='"+(!labelClass.get().isEmpty()?labelClass.get():"col-md-4 control-label")+"' for='"+name+"'>"+label+"</label>" + 
				"			<div class='"+(!inputDivClass.get().isEmpty()?inputDivClass.get():"col-md-8")+"'>" + 
				"				<input type='"+type+"' path='"+name+"' id='"+name+"' class='"+(inputClass.get().length()>0?inputClass:"form-control")+"' name='"+name + 
				"'					value='"+Utils.clearNull(value)+"'	placeholder='"+(placeholder!=null?placeholder:"Input")+"' required="+(required?required:false)+">" + 
				"<div class='has-error'>"+
                "<div id='error_"+name+"' class='error'>"+
				"</div></div></div></div>";
		return formInput;
	}
	
	public static String getFormRadio(String name,String label,boolean required,Map<String,String> inputs,Map<String,Object> radio) {
		if(inputs==null)
			inputs=new HashMap<>();
		
		WeakReference<String> labelClass= new WeakReference<String>(Utils.clearNull(inputs.get("labelClass")));
		WeakReference<String> inputClass= new WeakReference<String>(Utils.clearNull(inputs.get("inputClass")));
		WeakReference<String> inputDivClass= new WeakReference<String>(Utils.clearNull(inputs.get("inputDivClass")));
		
		String formInput="<div class='form-group'>" + 
				"<label class='"+(labelClass.get().length()>0?labelClass.get():"col-md-4 control-label")+"' for='"+name+"'>"+label+"</label>" + 
				"<div class='"+(!inputDivClass.get().isEmpty()?inputDivClass.get():"col-md-8")+"'>" ;
				for(String k:radio.keySet()) {
					formInput+="<input type='radio' path='"+name+"' value='"+radio.get(k)+"' class='form-control' />"+k; 
				}
				formInput+="<div class='has-error'>" + 
						"  <form:errors path='"+name+"' class='help-inline'/>" + 
						"   </div></div></div>";
		
		return formInput;
	}
	
	public static Map<String,String> getCombobox(ArrayList<Object> obj, String getValFunction,String delimit1, String getDisplayFunction, String delimit2,boolean encodeValue,boolean normalWithEncodeValue,boolean defaultOp)
	{
		Map<String,String> list=new LinkedHashMap<>();
		
		try
		{
			if(defaultOp) {
				list.put("", "Select");
			}
			for (Iterator<Object> iterator = obj.iterator(); iterator.hasNext();)
			{
				Object object = iterator.next();
				String[] methodCalls1 = getValFunction.split(",");
				String[] methodCalls2 = getDisplayFunction.split(",");
				String nameValue1 = "";
				String nameValue2 = "";
				
				for (int i = 0; i < methodCalls1.length; i++)
				{
					Method methodDisplay = object.getClass().getMethod(methodCalls1[i]);
					Object returnVal = methodDisplay.invoke(object, null);
					if (returnVal!= null && !returnVal.toString().trim().isEmpty())
						nameValue1 +=  clearNull(returnVal) + clearNull(delimit1);
				}
				nameValue1 = delimit1.isEmpty()?nameValue1:nameValue1.substring(0, nameValue1.length() - 1);

				for (int i = 0; i < methodCalls2.length; i++)
				{
					Method methodDisplay = object.getClass().getMethod(methodCalls2[i]);
					Object returnVal = methodDisplay.invoke(object, null);
					
					if (returnVal!= null && !returnVal.toString().trim().isEmpty())
						nameValue2 += clearNull(returnVal) + clearNull(delimit2);
				}
				nameValue2 = delimit2.isEmpty()?nameValue2:nameValue2.substring(0, nameValue2.length() - 1);

				if(encodeValue)
					nameValue1=DecryptionEncryptionUtil.encodingText(nameValue1);
				else if(normalWithEncodeValue &&  encodeValue)
					nameValue1=DecryptionEncryptionUtil.encodingText(nameValue1);
				
				list.put(nameValue1, nameValue2);
			}
		} catch (Exception e)
		{
			e.printStackTrace();
			list=new HashMap<>();
		}
		return list;
	}

	public static boolean isNullOrEmpty(Object object) {
		if(object ==null || object.toString().trim().isEmpty())
			return true;
		
		return false;
	}

	public static boolean isNullOrEmptyorNot(Object object, String extra) {
		if(object ==null || object.toString().trim().isEmpty() || object.toString().equals(extra))
			return true;
		
		return false;
	}

	public static String formatDateToString(Date date, String pattern) {

		try {
			SimpleDateFormat sdf = new SimpleDateFormat(pattern);
			if (date == null)
				date = new Date();
			return sdf.format(date);
		} catch (Exception e) {
			return e.getMessage();
		}
	}

	public static Date formatStringToDate(String dateString, String pattern) {

		try {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
			return simpleDateFormat.parse(dateString);
		} catch (Exception e) {
			return new Date();
		}
	}
	public static String getURI(String url, Map<String, Object> req1, Map<String, Object> req2) {
		UriComponentsBuilder uri = UriComponentsBuilder.fromHttpUrl(url);
		if (req1 != null) {
			for (Map.Entry<String, Object> req : req1.entrySet()) {
				uri.queryParam(req.getKey(), req.getValue());
			}
		}
		if (req2 != null) {

			for (Map.Entry<String, Object> req : req2.entrySet()) {
				uri.queryParam(req.getKey(), req.getValue());
			}
		}

		return uri.toUriString();
	}
}
