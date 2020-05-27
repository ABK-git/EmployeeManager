package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ConnectionDatabase.DatabaseConnect;
import beans.Department;

public class DepartmentDAO {
	//データベース接続パスの取得
	DatabaseConnect connect=new DatabaseConnect();

	//部署リスト取得
	private List<Department>selectDepartment(int id){
		//返すリストの宣言
		List<Department>list=new ArrayList<>();
		//コネクション宣言
		Connection con=null;
		try {
			Class.forName("org.h2.Driver");
			//コネクションの確立
			con=DriverManager.getConnection(connect.getJDBC_URL(),
					connect.getJDBC_USER(), connect.getJDBC_PASS());
			//sql文の設定
			String sql="SELECT * FROM DEPART";
			//編集の場合
			if(id!=0) {
				sql+=" WHERE ID='"+id+"'";
			}
			sql+=" ORDER BY ID ASC";
			//データベースからリストを取得する
			PreparedStatement ps=con.prepareStatement(sql);
			//結果の取得
			ResultSet rs=ps.executeQuery();
			while(rs.next()) {
				Department depart=new Department();
				depart.setId(rs.getInt("ID"));
				depart.setName(rs.getString("NAME"));
				//リストに格納する
				list.add(depart);
			}

		}catch(ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}finally {
			if(con!=null) {
				try {
					con.close();
				} catch (SQLException e) {
					// TODO 自動生成された catch ブロック
					e.printStackTrace();
				}
			}
		}
		return list;
	}
	//すべての部署を返すメソッド
	public List<Department>allDepartment(){
		return this.selectDepartment(0);
	}
	//idで指定した部署を渡すメソッド
	public Department getDepartment(int id) {
		List<Department>list=this.selectDepartment(id);
		return list.get(0);
	}
	//部署情報を新規登録、編集するメソッド
	public boolean addEdit(Department depart,boolean addOrEdit) {
		//返すboolean型
		boolean trueFalse=false;
		try {
			//H2データベースの指定
			Class.forName("org.h2.Driver");
		}catch(ClassNotFoundException e) {
			e.printStackTrace();
		}
		//SQL文の生成
		String sql=null;
		if(addOrEdit==true) {
			sql="INSERT INTO DEPART (NAME,ID) VALUES (?,?)";
		}else {
			sql="UPDATE DEPART SET NAME=? WHERE ID=?";
		}

		try {
			//コネクションを生成
			Connection conn=DriverManager.getConnection(connect.getJDBC_URL(),
					connect.getJDBC_USER(), connect.getJDBC_PASS());
			//preparedStatementにSQL文をセット
			PreparedStatement ps=conn.prepareStatement(sql);
			//?に名前を代入する
			ps.setString(1, depart.getName());
			//?にid代入する
			ps.setInt(2, depart.getId());
			//実行
			int trueOrFalse=ps.executeUpdate();
			if(trueOrFalse!=0) {
				trueFalse=true;
			}
		} catch (SQLException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		return trueFalse;
	}
	//部署情報を削除するメソッド
	public boolean delete(int id) {
		//返すboolean型
		boolean trueFalse=false;
		try {
			Class.forName("org.h2.Driver");
		} catch (ClassNotFoundException e1) {
			// TODO 自動生成された catch ブロック
			e1.printStackTrace();
		}
		//SQL文の生成
		String sql="DELETE FROM DEPART WHERE ID=?";
		try {
			//コネクション確立
			Connection conn=DriverManager.getConnection(connect.getJDBC_URL(),
					connect.getJDBC_USER(), connect.getJDBC_PASS());
			//sql文をセット
			PreparedStatement ps=conn.prepareStatement(sql);
			//?にidを代入する
			ps.setInt(1, id);
			//更新
			int trueOrFalse=ps.executeUpdate();
			if(trueOrFalse!=0) {
				trueFalse=true;
			}
		} catch (SQLException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		return trueFalse;
	}
}
