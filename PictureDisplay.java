package servlet;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import beans.Picture;
import dao.PictureDAO;

/**
 * Servlet implementation class PictureDisplay
 */
@WebServlet("/PictureDisplay")
public class PictureDisplay extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public PictureDisplay() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String id=request.getParameter("id");
		int photoID=Integer.parseInt(id);
		//daoの生成
		PictureDAO pd=new PictureDAO();
		//写真データを取得する
	    Picture picture=pd.editPicture(photoID);
		//コンテントタイプの設定
		response.setContentType(picture.getContextType());
		//バイトアレイインプットストリーム生成
		ByteArrayInputStream bais=new ByteArrayInputStream(picture.getPhoto());
		//サーブレットアウトプットストリームの取得
		ServletOutputStream sos=response.getOutputStream();
		try {
			byte[]buf=new byte[1024];
			int len;
			while((len=bais.read(buf))>0) {
				sos.write(buf,0,len);
			}
		}catch(IOException e) {
			e.printStackTrace();
		}finally {
			bais.close();
			sos.close();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
