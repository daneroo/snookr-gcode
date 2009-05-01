/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.scalr;

import com.google.gson.reflect.TypeToken;
import java.io.ByteArrayInputStream;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import java.io.InputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import org.apache.commons.fileupload.util.Streams;
import org.apache.commons.io.IOUtils;

/**
 *
 * @author daniel
 *   push this with something like:
 *     curl -m 30  -F "value=@filesystem.json.gz;type=application/octet-stream"  http://localhost:8080/upload
 */
public class CloudZipServlet extends HttpServlet {

    static final int MAXPOSTSIZE = 10 * 1024 * 1024;
    private static final Logger log =
            Logger.getLogger(CloudZipServlet.class.getName());

    /**
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            log.warning("Servlet CloudZipServlet GET response");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet CloudZipServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h3>ContextPath: " + request.getContextPath() + "</h3>");
            out.println("<h3>URI        :" + request.getRequestURI() + "</h3>");
            out.println("<p>Should only be used for POST</p>");
            out.println("</body>");
            out.println("</html>");
        } finally {
            out.close();
        }
    }

    /**
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            log.warning("Servlet CloudZipServlet POST");
            boolean fullEcho = false;
            if (fullEcho) {
                // OutputStream, so headers not sent to response
                dumpHeaders(request, null);
                echoRequest(request, response);
            } else {
                //Process upload params
                response.setContentType("text/plain");
                PrintWriter out = response.getWriter();

                //dumpHeaders(request, out);
                if (ServletFileUpload.isMultipartContent(request)) {
                    handleMultipart(out, request);
                } else {
                    String message = "IGNORING: Post is not multipart";
                    log.warning(message);
                    out.println(message);
                }
            }
        } catch (Exception ex) {
            log.warning("Upload Post threw exception: " + ex.getMessage());
            throw new ServletException(ex);
        }
    }

    /**
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Scalr upload with multipart handling";
    }// </editor-fold>

    /* dump headers to Logs, and writer, if write param not null*/
    private void dumpHeaders(HttpServletRequest request, PrintWriter writer) {
        String message = "POST request headers";
        for (Enumeration headers = request.getHeaderNames(); headers.hasMoreElements();) {
            String headerName = (String) headers.nextElement();
            message = message + "\n" + "  Header[" + headerName + "] = " + request.getHeader(headerName);
        }
        log.warning(message);
        if (writer != null) {
            writer.println(message);
        }

    }

    private void handleMultipart(PrintWriter out, HttpServletRequest request) throws IOException, FileUploadException {
        ServletFileUpload upload = new ServletFileUpload();
        upload.setFileSizeMax(MAXPOSTSIZE);
        out.println("Servlet CloudZip POST response (sum)");
        FileItemIterator iterator = upload.getItemIterator(request);
        while (iterator.hasNext()) {
            FileItemStream item = iterator.next();
            InputStream stream = item.openStream();
            if (item.isFormField()) {
                String value = Streams.asString(stream);
                String message = "Form field: " + item.getFieldName() + " length: " + value.length() + " value: " + value;
                out.println(message);
            } else {
                boolean inMem = true;
                Map<String, byte[]> zipMap = null;
                if (inMem) { // get the array first into memory
                    byte[] content = IOUtils.toByteArray(stream);
                    String md5sum = MD5.digest(content);
                    String message = "File field: " + item.getFieldName() + " name: " + item.getName() + " length: " + content.length + " md5: " + md5sum;
                    log.warning(message);
                    out.println(message);
                    InputStream is = new ByteArrayInputStream(content);
                    zipMap = expandZipStream(is);
                    is.close();
                } else {
                    String message = "File field: " + item.getFieldName() + " name: " + item.getName();
                    log.warning(message);
                    out.println(message);
                    zipMap = expandZipStream(stream);
                }
                for (Map.Entry<String, byte[]> e : zipMap.entrySet()) {
                    String name = e.getKey();
                    byte[] innercontent = e.getValue();
                    out.println("  -" + name + ": length: " + innercontent.length + " md5: " + MD5.digest(innercontent));
                }
                String jsonManifest = makeManifest(zipMap);
                out.println(jsonManifest);
                List<Map<String,String>> manifestList = decodeManifest(jsonManifest);
                for (Map<String,String> m: manifestList){
                    out.println("  +" + m.get("name") + ": length: " + m.get("length") + " md5: " + m.get("md5"));

                }
            }
        }
    }

    /* This assumes that getWriter has not/will not be called
     */
    private void echoRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        IOUtils.copy(request.getInputStream(), response.getOutputStream());
    }

    private Map<String, byte[]> expandZipStream(InputStream is) {
        // LinkedHashMap preserves insertion order in iteration
        Map<String, byte[]> map = new LinkedHashMap<String, byte[]>();
        ZipInputStream zipis = new ZipInputStream(is);
        while (true) {
            try {
                ZipEntry ze = zipis.getNextEntry();
                if (ze == null) {
                    break;
                }
                if (ze.isDirectory()) {
                    System.err.println("Ignoring directory: " + ze.getName());
                    continue;
                }
                //System.out.println("Reading next entry: " + ze.getName());
                String name = ze.getName();
                byte[] content = IOUtils.toByteArray(zipis);
                String md5sum = MD5.digest(content);
                System.out.println("Read: " + ze.getName() + " length: " + content.length + " md5: " + md5sum);
                map.put(name, content);
            } catch (IOException ex) {
                log.log(Level.SEVERE, null, ex);
                break;
            }
        }
        return map;
    }

    private String makeManifest(Map<String, byte[]> zipMap) {
        List<Map<String, String>> manifestList = new ArrayList<Map<String, String>>();
        //= new LinkedHashMap<String, String>();
        for (Map.Entry<String, byte[]> e : zipMap.entrySet()) {
            String name = e.getKey();
            byte[] content = e.getValue();
            Map<String, String> manifestEntry = new LinkedHashMap<String, String>();
            manifestEntry.put("name", name);
            manifestEntry.put("md5", MD5.digest(content));
            manifestEntry.put("length", "" + content.length);
            manifestList.add(manifestEntry);
        }
        return new JSON().encode(manifestList);
    }

    private List<Map<String, String>> decodeManifest(String jsonManifest) {
        List<Map<String, String>> manifestList = null;
        Type listType = new TypeToken<List<Map<String, String>>>() {
        }.getType();

        manifestList = new JSON().decode(jsonManifest, listType);
        return manifestList;
    }

    private String makeManifestAsMap(Map<String, byte[]> zipMap) {
        Map<String, String> manifestMap = new LinkedHashMap<String, String>();
        for (Map.Entry<String, byte[]> e : zipMap.entrySet()) {
            String name = e.getKey();
            byte[] content = e.getValue();
            manifestMap.put(name, MD5.digest(content));
        }
        return new JSON().encode(manifestMap);
    }
}
