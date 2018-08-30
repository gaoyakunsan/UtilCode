package com.util;

import java.io.StringReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.util.TablesNamesFinder;

/**
 * Created by wangkun on 2016/1/7.
 */
public class testRunner {

	public static List<String> getTableNameBySql(String sql) throws JSQLParserException {
		CCJSqlParserManager parser = new CCJSqlParserManager();
		StringReader reader = new StringReader(sql);
		List<String> list = new ArrayList<String>();
		Statement stmt = parser.parse(new StringReader(sql));
		if (stmt instanceof Select) {
			Select selectStatement = (Select) stmt;
			TablesNamesFinder tablesNamesFinder = new TablesNamesFinder();
			List tableList = tablesNamesFinder.getTableList(selectStatement);
			for (Iterator iter = tableList.iterator(); iter.hasNext();) {
				String tableName = iter.next().toString();
				list.add(tableName);
			}
		}
		return list;
	}

	public static void main(String[] args) throws JSQLParserException {
		
		String sql = "select \r\n" + 
				"youe_time AS 日期\r\n" + 
				",youe_cnt AS 给额用户数\r\n" + 
				",num\r\n" + 
				",case when num>=1 then day0 else 0 end 1天\r\n" + 
				",case when num>=2 then day0+day1 else 0 end 2天\r\n" + 
				",case when num>=3 then day0+day1+day2 else 0 end 3天 \r\n" + 
				",case when num>=4 then day0+day1+day2+day3 else 0 end 4天\r\n" + 
				",case when num>=5 then day0+day1+day2+day3+day4 else 0 end 5天\r\n" + 
				",case when num>=6 then day0+day1+day2+day3+day4+day5 else 0 end 6天\r\n" + 
				",case when num>=7 then day0+day1+day2+day3+day4+day5+day6 else 0 end 7天\r\n" + 
				",case when num>=8 then day0+day1+day2+day3+day4+day5+day6+day7 else 0 end 8天\r\n" + 
				",case when num>=9 then day0+day1+day2+day3+day4+day5+day6+day7+day8 else 0 end 9天\r\n" + 
				",case when num>=10 then day0+day1+day2+day3+day4+day5+day6+day7+day8+day9 else 0 end 10天\r\n" + 
				",case when num>=11 then day0+day1+day2+day3+day4+day5+day6+day7+day8+day9+day10 else 0 end 11天\r\n" + 
				",case when num>=12 then day0+day1+day2+day3+day4+day5+day6+day7+day8+day9+day10+day11 else 0 end 12天\r\n" + 
				",case when num>=13 then day0+day1+day2+day3+day4+day5+day6+day7+day8+day9+day10+day11+day12 else 0 end 13天\r\n" + 
				",case when num>=14 then day0+day1+day2+day3+day4+day5+day6+day7+day8+day9+day10+day11+day12+day13 else 0 end 14天\r\n" + 
				",case when num>=15 then day0+day1+day2+day3+day4+day5+day6+day7+day8+day9+day10+day11+day12+day13+day14 else 0 end 15天\r\n" + 
				",case when num>=16 then day0+day1+day2+day3+day4+day5+day6+day7+day8+day9+day10+day11+day12+day13+day14+day15 else 0 end 16天\r\n" + 
				",case when num>=17 then day0+day1+day2+day3+day4+day5+day6+day7+day8+day9+day10+day11+day12+day13+day14+day15+day16 else 0 end 17天\r\n" + 
				",case when num>=18 then day0+day1+day2+day3+day4+day5+day6+day7+day8+day9+day10+day11+day12+day13+day14+day15+day16+day17 else 0 end 18天\r\n" + 
				",case when num>=19 then day0+day1+day2+day3+day4+day5+day6+day7+day8+day9+day10+day11+day12+day13+day14+day15+day16+day17+day18 else 0 end 19天\r\n" + 
				",case when num>=20 then day0+day1+day2+day3+day4+day5+day6+day7+day8+day9+day10+day11+day12+day13+day14+day15+day16+day17+day18+day19 else 0 end 20天\r\n" + 
				",case when num>=21 then day0+day1+day2+day3+day4+day5+day6+day7+day8+day9+day10+day11+day12+day13+day14+day15+day16+day17+day18+day19+day20 else 0 end 21天\r\n" + 
				",case when num>=22 then day0+day1+day2+day3+day4+day5+day6+day7+day8+day9+day10+day11+day12+day13+day14+day15+day16+day17+day18+day19+day20+day21 else 0 end 22天\r\n" + 
				",case when num>=23 then day0+day1+day2+day3+day4+day5+day6+day7+day8+day9+day10+day11+day12+day13+day14+day15+day16+day17+day18+day19+day20+day21+day22 else 0 end 23天\r\n" + 
				",case when num>=24 then day0+day1+day2+day3+day4+day5+day6+day7+day8+day9+day10+day11+day12+day13+day14+day15+day16+day17+day18+day19+day20+day21+day22+day23 else 0 end 24天\r\n" + 
				",case when num>=25 then day0+day1+day2+day3+day4+day5+day6+day7+day8+day9+day10+day11+day12+day13+day14+day15+day16+day17+day18+day19+day20+day21+day22+day23+day24 else 0 end 25天\r\n" + 
				",case when num>=26 then day0+day1+day2+day3+day4+day5+day6+day7+day8+day9+day10+day11+day12+day13+day14+day15+day16+day17+day18+day19+day20+day21+day22+day23+day24+day25 else 0 end 26天\r\n" + 
				",case when num>=27 then day0+day1+day2+day3+day4+day5+day6+day7+day8+day9+day10+day11+day12+day13+day14+day15+day16+day17+day18+day19+day20+day21+day22+day23+day24+day25+day26 else 0 end 27天\r\n" + 
				",case when num>=28 then day0+day1+day2+day3+day4+day5+day6+day7+day8+day9+day10+day11+day12+day13+day14+day15+day16+day17+day18+day19+day20+day21+day22+day23+day24+day25+day26+day27 else 0 end 28天\r\n" + 
				",case when num>=29 then day0+day1+day2+day3+day4+day5+day6+day7+day8+day9+day10+day11+day12+day13+day14+day15+day16+day17+day18+day19+day20+day21+day22+day23+day24+day25+day26+day27+day28 else 0 end 29天\r\n" + 
				",case when num>=30 then day0+day1+day2+day3+day4+day5+day6+day7+day8+day9+day10+day11+day12+day13+day14+day15+day16+day17+day18+day19+day20+day21+day22+day23+day24+day25+day26+day27+day28+day29 else 0 end 30天\r\n" + 
				"--,case when num>=31 then day0+day1+day2+day3+day4+day5+day6+day7+day8+day9+day10+day11+day12+day13+day14+day15+day16+day17+day18+day19+day20+day21+day22+day23+day24+day25+day26+day27+day28+day29+day30 else 0 end 1天\r\n" + 
				"FROM rpt.yqqcsd_bi_temp";
		//String sql = "/*===========================登录用户===============================*/drop table if exists RPT.xzl20180709_denglu";
		String sql1 = "";
		if(sql.indexOf("--") != -1){
			if(sql.indexOf("--") != -1){
                if(sql.toLowerCase().indexOf("select") != -1){
                    sql1 = sql.substring(sql.indexOf("select"), sql.length());
                }else if(sql.toLowerCase().indexOf("drop") != -1){
                    sql1 = sql.substring(sql.indexOf("drop"), sql.length());
                }else if(sql.toLowerCase().indexOf("create") != -1){
                    sql1 = sql.substring(sql.indexOf("create"), sql.length());
                }else if(sql.toLowerCase().indexOf("update") != -1){
                    sql1 = sql.substring(sql.indexOf("update"), sql.length());
                }else if(sql.toLowerCase().indexOf("update") != -1){
                    sql1 = sql.substring(sql.indexOf("update"), sql.length());
                }
            }
        }else if(sql.indexOf("/*") != -1){
        	sql1 = sql.substring(sql.indexOf("*/") + 2, sql.length());
        }
//		System.out.println(sql1);
		/*List<String> tables = getTableNameBySql(sql);
		for(String table: tables) {
			System.out.println(table);
		}*/
		
		
		String str = "iview.dashboard_dataset";
		String strArr [] = str.split("\\.");
		System.out.println(strArr[0]);
	}

		
}
