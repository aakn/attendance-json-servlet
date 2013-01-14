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
public class pesitattendance extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		//  resp.getWriter().print("Hello from Java!\n");
		try{
			resp.setContentType("text/xml");
			PrintWriter out=resp.getWriter();
			String usn=req.getParameter("usn");
			Date d=new Date();
			System.out.println("Request for USN "+usn+" at time "+d);
			//String usn="1pe09cs059";
			//String cpass=req.getParameter("cpass");
			ipomo2 ip=new ipomo2();
			ip.getattn(usn);
			//out.println("TEST="+ip.subj[0]);
			//start tree build
			String root="student";
			DocumentBuilderFactory documentBuilderFactory = 
					DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = 
					documentBuilderFactory.newDocumentBuilder();
			Document document = documentBuilder.newDocument();
			Element rootElement = document.createElement(root);
			document.appendChild(rootElement);

			Element em = document.createElement("college");
			em.appendChild(document.createTextNode(ip.college));
			rootElement.appendChild(em);

			em = document.createElement("name");
			em.appendChild(document.createTextNode(ip.name));
			rootElement.appendChild(em);

			em = document.createElement("course");
			em.appendChild(document.createTextNode(ip.course));
			rootElement.appendChild(em);

			em = document.createElement("usn");
			em.appendChild(document.createTextNode(ip.rollno));
			rootElement.appendChild(em);

			em = document.createElement("semester");
			em.appendChild(document.createTextNode(ip.semester));
			rootElement.appendChild(em);

			em = document.createElement("section");
			em.appendChild(document.createTextNode(ip.section));
			rootElement.appendChild(em);


			for(int i=0;i<ip.ctr;i++)
			{
				//out.println(ip.subj[i]+"-"+ip.attn[i]+"-"+ip.total[i]+"-"+ip.percent[i]);
				//out.println("<br>");
				em = document.createElement(ip.subj[i]);
				//em.appendChild(document.createTextNode(ip.section));
				rootElement.appendChild(em);
				Element em2 = document.createElement("attended");
				em2.appendChild(document.createTextNode(ip.attn[i]));
				em.appendChild(em2);

				Element em3 = document.createElement("total");
				em3.appendChild(document.createTextNode(ip.total[i]));
				em.appendChild(em3);

				Element em4 = document.createElement("percentage");
				em4.appendChild(document.createTextNode(ip.percent[i]));
				em.appendChild(em4);
			}

			TransformerFactory transformerFactory = 
					TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(document);
			StreamResult result =  new StreamResult(out);
			transformer.transform(source, result);
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
class ipomo2
{
	static String subj[]=new String[20];
	static String attn[]=new String[20];
	static String total[]=new String[20];
	static String percent[]=new String[20];
	static String line;
	static String college;
	static String name;
	static String course;
	static String rollno;
	static String semester;
	static String section;

	static int ctr;
	/*
	public void getattn(String usn) {
		try {
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

			try{
				while ((line = rd.readLine()) != null) {
					// Process line...
					if(line.contains("temp =")) {
						System.out.println("INHERE");
						System.out.println("LINE="+line);
						wr.close();
						rd.close();
						//parse(line);
						awesomeParse(line);
						break;
						//return parse(line);
					}
					if(line.contains("var nicename"))
					{
						line=line.substring(line.indexOf('"')+1,line.lastIndexOf('"'));
						System.out.println("LINE="+line);
						college=line;

					}
					if(line.contains("var name"))
					{
						line=line.substring(line.indexOf('"')+1,line.lastIndexOf('"'));
						System.out.println("LINE="+line);
						name=line;
					}
					if(line.contains("var course"))
					{
						line=line.substring(line.indexOf('"')+1,line.lastIndexOf('"'));
						System.out.println("LINE="+line);
						course=line;
					}
					if(line.contains("var rollno"))
					{
						line=line.substring(line.indexOf('"')+1,line.lastIndexOf('"'));
						System.out.println("LINE="+line);
						rollno=line;
					}
					if(line.contains("var semester"))
					{
						line=line.substring(line.indexOf('"')+1,line.lastIndexOf('"'));
						System.out.println("LINE="+line);
						semester=line;
					}
					if(line.contains("var section"))
					{
						line=line.substring(line.indexOf('"')+1,line.lastIndexOf('"'));
						System.out.println("LINE="+line);
						section=line;
					} 
				}
			} catch (Exception e) {
				System.out.println("In here");
				e.printStackTrace();
			}
			//send stuff ends
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	*/
	public static void awesomeParse(String data) throws IOException {

		//storeResultInCache(data,name,semester,section);

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
				subj[i] = t[k++];
				total[i] = t[k++];
				attn[i] = t[k++];
				percent[i] = t[k++];
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

				if(line.contains("temp ="))
				{
					//System.out.println("INHERE");
					wr.close();
					rd.close();
					awesomeParse(line);
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
	

	
	public static void parse(String data) throws IOException
	{
		int firstbox=data.indexOf('[');
		data=data.substring(++firstbox);
		int lastbox=data.lastIndexOf(']');
		data=data.substring(0,lastbox);
		/*System.out.println(data);
		data=data.replace("[","");
		data=data.replace("]","");
		System.out.println(data); */
		StringTokenizer token=new StringTokenizer(data,"],[");
		//System.out.println(data);
		ctr=0;
		while(token.hasMoreTokens())
		{
			subj[ctr]=token.nextToken();
			subj[ctr]=subj[ctr].substring(1,subj[ctr].length()-1);
			total[ctr]=token.nextToken();
			total[ctr]=total[ctr].substring(1,total[ctr].length()-1);
			attn[ctr]=token.nextToken();
			attn[ctr]=attn[ctr].substring(1,attn[ctr].length()-1);
			percent[ctr]=token.nextToken();
			percent[ctr]=percent[ctr].substring(1,percent[ctr].length()-1);
			//System.out.println(percent[ctr]);
			ctr++;
		}
	}
	

	/*public static void main(String args[]) throws IOException
{
getattn(args[0]);
for(int i=0;i<ctr;i++)
{
System.out.println(subj[i]+"-"+attn[i]+"-"+total[i]+"-"+percent[i]);
}

//System.out.println(getattn("1pe09cs061"));
}
	 */

}
