 package cn.bao.book.adminservlet;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.ImageIcon;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadBase;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import cn.bao.book.domain.Book;
import cn.bao.book.service.BookService;
import cn.bao.category.domain.Category;
import cn.bao.category.service.CategoryService;
import cn.itcast.commons.CommonUtils;

@WebServlet("/AdminAddBookServlet")
public class AdminAddBookServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private BookService bookService = new BookService();
	private CategoryService categoryService = new CategoryService();
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		/*
		 * 1. �ѱ����ݷ�װ��Book������
		 *   * �ϴ�����
		 */
		// ��������
		DiskFileItemFactory factory = new DiskFileItemFactory(15 * 1024, new File("E:\\Temp"));
		// �õ�������
		ServletFileUpload sfu = new ServletFileUpload(factory);
		// ���õ����ļ���СΪ15KB
		sfu.setFileSizeMax(100 * 1024 * 1024);
		// ʹ�ý�����ȥ����request���󣬵õ�List<FileItem>
		try {
			
			List<FileItem> fileItemList = sfu.parseRequest(request);
			/*
			 * * ��fileItemList�е����ݷ�װ��Book������
			 *   > �����е���ͨ���ֶ������ȷ�װ��Map��
			 *   > �ٰ�map�е����ݷ�װ��Book������
			 */
			Map<String,String> map = new HashMap<String,String>();
			
			for(FileItem fileItem : fileItemList) {
				if(fileItem.isFormField()) {
					map.put(fileItem.getFieldName(), fileItem.getString("UTF-8"));
				}
			}
			
			Book book = CommonUtils.toBean(map, Book.class);
			// Ϊbookָ��bid
			book.setBid(CommonUtils.uuid());
			/*
			 * ��Ҫ��Map�е�cid��װ��Category�����У��ٰ�Category����Book
			 */
			Category category = CommonUtils.toBean(map, Category.class);
			//category.setCid(categoryService.findCidByCname(category.getCname()));
			book.setCategory(category);
			/*
			 * 2. �����ϴ����ļ�
			 *   * �����Ŀ¼
			 *   * ������ļ�����
			 */
			// �õ������Ŀ¼
			String savepath = this.getServletContext().getRealPath("/book_img");
			// �õ��ļ����ƣ���ԭ���ļ��������uuidǰ׺�������ļ�����ͻ
			String filename = CommonUtils.uuid() + "_" + fileItemList.get(1).getName();
			
			
			/*
			 * У���ļ�����չ��
			 */
			if(!filename.toLowerCase().endsWith("jpg")) {
				request.setAttribute("msg", "���ϴ���ͼƬ����JPG��չ����");
				request.setAttribute("categoryList", categoryService.findAll());
				request.getRequestDispatcher("/adminjsps/admin/book/add.jsp")
						.forward(request, response);
				return;
			}
			
			// ʹ��Ŀ¼���ļ����ƴ���Ŀ���ļ�
			File destFile = new File(savepath, filename);
			// �����ϴ��ļ���Ŀ���ļ�λ��
			fileItemList.get(1).write(destFile);
			
			/*
			 * 3. ����Book�����image������ͼƬ��·�����ø�Book��image
			 */
			book.setImage("book_img/" + filename);
			
			/*
			 * У��ͼƬ�ĳߴ�
			 */
			Image image = new ImageIcon(destFile.getAbsolutePath()).getImage();
			if(image.getWidth(null) > 200 || image.getHeight(null) > 200) {
				destFile.delete();//ɾ������ļ���
				request.setAttribute("msg", "���ϴ���ͼƬ�ߴ糬����200 * 200��");
				request.setAttribute("categoryList", categoryService.findAll());
				request.getRequestDispatcher("/adminjsps/admin/book/add.jsp")
						.forward(request, response);
				return;
			}
			
			/*
			 * 4. ʹ��BookService��ɱ���
			 */
			bookService.add(book);			
			/*
			 * 5. ���ص�ͼ���б�
			 */
			request.getRequestDispatcher("/AdminBookServlet?method=findAll")
					.forward(request, response);
		} catch (Exception e) { 
			if(e instanceof FileUploadBase.FileSizeLimitExceededException) {
				request.setAttribute("msg", "���ϴ����ļ�������100M");
				request.setAttribute("categoryList", categoryService.findAll());
				request.getRequestDispatcher("/adminjsps/admin/book/add.jsp")
						.forward(request, response);
			}
		}
	}
}
