package com.util.dbUtil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class JdbcUtil {

	private static String url = "jdbc:mysql://masterdb.oms.idc.pplive.cn/media_asset?useUnicode=true&characterEncoding=utf8"; 
    private static String user = "pp_meizi";  
    private static String password = "a2bcbfffe05f83764";  
      
    private JdbcUtil() { }  
      
    static {  
        try {  
            Class.forName("oracle.jdbc.driver.OracleDriver");  
        } catch (ClassNotFoundException e) {  
            throw new ExceptionInInitializerError(e);  
        }  
    }  
  
    public static Connection getConnection() throws SQLException{  
        return DriverManager.getConnection(url, user, password);  
    }  
      
    public static void free(ResultSet rs, Statement st, Connection conn) {  
        try {  
            if (rs != null) {  
                rs.close();  
            }  
        } catch (SQLException e) {  
            e.printStackTrace();  
        } finally {  
            try {  
                if (st != null) {  
                    st.close();  
                }  
            } catch (SQLException e) {  
                e.printStackTrace();  
            } finally {  
                if (conn != null) {  
                    try {  
                        conn.close();  
                    } catch (SQLException e) {  
                        e.printStackTrace();  
                    }  
                }  
            }  
        }  
    }  
}
