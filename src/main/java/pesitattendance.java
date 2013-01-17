import java.io.*;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import javax.servlet.*;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.*;
import java.util.*;
import java.net.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import org.w3c.dom.*;
import org.json.*;
import net.sf.json.JSON;
import net.sf.json.xml.XMLSerializer;

public class pesitattendance extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		//  resp.getWriter().print("Hello from Java!\n");
		try{
			resp.setContentType("text/json");
			PrintWriter out=resp.getWriter();
			String usn=req.getParameter("usn");
			Date d=new Date();
			System.out.println("Request for USN "+usn+" at time "+d);
			//String usn="1pe09cs059";
			//String cpass=req.getParameter("cpass");
			Ipomo ip=new Ipomo();
			ip.getattn(usn);
			//out.println("TEST="+ip.subj[0]);
			//start tree build
			String root="student";

			JSONObject jsonObj = new JSONObject();
			jsonObj.put("college",ip.college);
			jsonObj.put("name",ip.name);
			jsonObj.put("course",ip.course);
			jsonObj.put("usn",ip.rollno);
			jsonObj.put("semester",ip.semester);
			jsonObj.put("section",ip.section);
			
			JSONObject attendanceJSON = new JSONObject();
			for (int i=0;i<ip.subjList.size();i++) {
				JSONObject subjJSON = new JSONObject();
				
				subjJSON.put("attended",ip.attnList.get(i));
				subjJSON.put("total",ip.totalList.get(i));
				subjJSON.put("percent",ip.percentList.get(i));

				attendanceJSON.put(ip.subjList.get(i),subjJSON);
			}
			jsonObj.put("attendance",attendanceJSON);
			out.println(jsonObj.toString(4));
			

		}catch(Exception e)
		{
			System.out.println("some gudbad happened.. "+e);
		}

	}

	public static void main(String[] args) throws Exception{
		Server server = new Server(Integer.valueOf(System.getenv("PORT")));
		ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
		context.setContextPath("/");
		server.setHandler(context);
		context.addServlet(new ServletHolder(new pesitattendance()),"/*");
		server.start();
		server.join();   
	}
}
class Ipomo
{
	static String subj[]=new String[20];
	static String attn[]=new String[20];
	static String total[]=new String[20];
	static String percent[]=new String[20];
	static ArrayList<String> subjList = new ArrayList<String>();
	static ArrayList<String> attnList = new ArrayList<String>();
	static ArrayList<String> totalList = new ArrayList<String>();
	static ArrayList<String> percentList = new ArrayList<String>();
	static String line;
	static String college;
	static String name;
	static String course;
	static String rollno;
	static String semester;
	static String section;

	static int ctr;
	
	public static void awesomeParseList(String data) throws IOException {

		try {
			int firstIndex, lastIndex;
			firstIndex = data.indexOf('[');
			lastIndex = data.lastIndexOf(']');
			data = data.substring(++firstIndex,lastIndex);
			firstIndex = data.indexOf('[');
			lastIndex = data.lastIndexOf(']');
			data = data.substring(++firstIndex,lastIndex);
			System.out.println("Ali's data : " + data);
			String a[] = data.split("\\],\\[");
			int i;
			for( i = 0; i<a.length;i++) {
				a[i] = a[i].replaceAll("\"", "");
				System.out.println(a[i]);
				String t[] = a[i].split(",");
				int k = 0;
				subjList.add(t[k++]);
				totalList.add(t[k++]);
				attnList.add(t[k++]);
				percentList.add(t[k++]);
			}
			ctr = i;
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	
	public static void getattn(String usn)
	{
		try {
			CookieHandler.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL));
			// Construct data
			String data = URLEncoder.encode("userid", "UTF-8") + "=" + URLEncoder.encode(usn, "UTF-8");
			data += "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode("003", "UTF-8");
			// Send data
			URL url = new URL("http://ipomo.in/IpomoStudentLoginServer/IpomoStudentServlet");
			URLConnection conn = url.openConnection();
			conn.setDoOutput(true);
			OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
			wr.write(data);
			wr.flush();
			// Get the response
			BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			//System.out.println("*******************************");
			//send stuff
			url = new URL("http://ipomo.in/IpomoStudentLoginServer/studentInfo.jsp");
			conn = url.openConnection();
			conn.setDoOutput(true);
			rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));

			while ((line = rd.readLine()) != null) {
				// Process line...
				//out.println(line);
				if(line.contains("temp ="))
				{
					//System.out.println("INHERE");
					wr.close();
					rd.close();
					//JSONParse(line,out);
					awesomeParseList(line);
					//return parse(line);
				}
				if(line.contains("var nicename"))
				{
					line=line.substring(line.indexOf('"')+1,line.lastIndexOf('"'));
					//System.out.println("LINE="+line);
					college=line;
				}
				if(line.contains("var name"))
				{
					line=line.substring(line.indexOf('"')+1,line.lastIndexOf('"'));
					//System.out.println("LINE="+line);
					name=line;
				}
				if(line.contains("var course"))
				{
					line=line.substring(line.indexOf('"')+1,line.lastIndexOf('"'));
					//System.out.println("LINE="+line);
					course=line;
				}
				if(line.contains("var rollno"))
				{
					line=line.substring(line.indexOf('"')+1,line.lastIndexOf('"'));
					//System.out.println("LINE="+line);
					rollno=line;
				}
				if(line.contains("var semester"))
				{
					line=line.substring(line.indexOf('"')+1,line.lastIndexOf('"'));
					//System.out.println("LINE="+line);
					semester=line;
				}
				if(line.contains("var section"))
				{
					line=line.substring(line.indexOf('"')+1,line.lastIndexOf('"'));
					//System.out.println("LINE="+line);
					section=line;
				} 
			}
			//send stuff ends

		} catch (Exception e) {
		}

	}

}
