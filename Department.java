package beans;

import java.io.Serializable;

public class Department implements Serializable{
	//部署ID
	private int id;
	//部署名
	private String name;

	//新規登録用コンストラクタ
	public Department(String name) {
		this.name=name;
	}
	public Department(int id,String name) {
		this.id=id;
		this.name=name;
	}
	//引数なしのコンストラクタ
	public Department() {
		// TODO 自動生成されたコンストラクター・スタブ
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

}
