package com.robot.og.base.dto;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *
 * </p>
 *
 * @author tank
 * @date 2020/7/16
 */
public class Convert {

	/**
	 * 抽取Th
	 * @param tr 包含th的行
	 * @return
	 */
	public static final List<String> parseThs(Element tr) {
		Elements tds = tr.getElementsByTag("th");
		List<String> ths = new ArrayList<String>(tds.size());
		for (int i = 0; i < tds.size(); i++) {
			ths.add(tds.get(i).text());
		}
		return ths;
	}

	/**
	 * 抽取td
	 * @param ths 抽取好的th
	 * @param tr 包含td的行
	 * @return
	 */
	public static final Map<String,String> parseTds(List<String> ths, Element tr) {
		Elements tds = tr.getElementsByTag("td");
		HashMap<String, String> column = new HashMap<String, String>();
		for (int i = 0; i < tds.size(); i++) {
			column.put(ths.get(i), tds.get(i).text());
		}
		return column;
	}

	/**
	 * 将trs抽取成map列表
	 * @param ths
	 * @param trs
	 * @return
	 */
	public static final List<Map<String,String>> parseListMap(List<String> ths,Elements trs) {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>(trs.size());
		for (Element tr : trs) {
			Map<String, String> map = Convert.parseTds(ths, tr);
			list.add(map);
		}
		return list;
	}
}
