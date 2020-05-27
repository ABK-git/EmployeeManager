package ConnectionDatabase;

public class DatabaseConnect {
	//データベース接続用のパスワード
	private final String JDBC_URL="jdbc:h2:c:\\Users\\Abiko\\test";
	private final String JDBC_USER="sa";
	private final String JDBC_PASS="";

	//コンストラクタ
	public DatabaseConnect() {

	}

	public String getJDBC_URL() {
		return JDBC_URL;
	}

	public String getJDBC_USER() {
		return JDBC_USER;
	}

	public String getJDBC_PASS() {
		return JDBC_PASS;
	}

}
