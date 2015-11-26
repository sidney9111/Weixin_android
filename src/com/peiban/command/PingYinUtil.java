package com.peiban.command;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

public class PingYinUtil {
	/**
	 * 将字符串中的中文转化为拼音,其他字符不变
	 * 
	 * @param inputString
	 * @return
	 */
	public static String getPingYin(String inputString) {
		HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
		format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		format.setVCharType(HanyuPinyinVCharType.WITH_V);
		if (inputString == null)
		{
			return "@";
		}
		char[] input = inputString.trim().toCharArray();
		String output = "";
		if (input != null)
		{
			if((input[0] > 97 && input[0] < 122) || (input[0] > 65 && input[0] < 90)){
				output = String.valueOf(input[0]).toLowerCase();
			}
			else 
			{
				try
				{
					for (int i = 0; i < input.length; i++)
					{
						if (java.lang.Character.toString(input[i]).matches("[\\u4E00-\\u9FA5]+"))
						{
							String[] temp = PinyinHelper.toHanyuPinyinStringArray(input[i], format);
							output += temp[0];
						} else
							output += java.lang.Character.toString(input[i]);
					}
				} catch (Exception e)
				{
					e.printStackTrace();
				}
			}
			
		}
		return output;
	}
	

	/**
	 * 汉字转换位汉语拼音首字母，英文字符不变
	 * 
	 * @param chines汉字
	 * @return 拼音
	 */
	public static String converterToFirstSpell(String chines) {
		StringBuffer pinyinName = new StringBuffer();
		if (chines == null)
		{
			return "@";
		}
		char[] nameChar = chines.toCharArray();
		HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
		defaultFormat.setCaseType(HanyuPinyinCaseType.UPPERCASE);
		defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		for (int i = 0; i < nameChar.length; i++)
		{
			if (nameChar[i] > 128)
			{
				try
				{
					char name = PinyinHelper.toHanyuPinyinStringArray(nameChar[i], defaultFormat)[0].charAt(0);
					pinyinName.append(name);
				} catch (Exception e)
				{
					e.printStackTrace();
				}
			} else
			{
				pinyinName.append(nameChar[i]);
			}
		}
		return pinyinName.toString();
	}

}
