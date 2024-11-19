package fileupload;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

public class FileUtil {

    // 파일 업로드 기능
    public static String uploadFile(HttpServletRequest req,
    		String sDirectory) 
    				throws ServletException, IOException {
        Part part = req.getPart("o_file");
        
        // 파일 유효성 검사
        if (part == null || part.getSize() == 0) {
            System.out.println("파일이 업로드되지 않았거나 빈 파일입니다.");
            return ""; // 빈 파일 처리
        }

        String partHeader = part.getHeader("content-disposition");
        System.out.println("partHeader=" + partHeader);

        String[] phArr = partHeader.split("filename=");
        if (phArr.length < 2) {
            return ""; // 파일명이 없으면 빈 문자열 반환
        }

        String originalFileName = phArr[1].trim().replace("\"", "");
        if (originalFileName.isEmpty()) {
            return ""; // 파일명이 비어 있으면 빈 문자열 반환
        }

        // 파일 저장
        String filePath = sDirectory + File.separator + originalFileName;
        part.write(filePath);
        System.out.println("파일 저장 완료: " + filePath);

        return originalFileName;
    }

    // 파일명 변경 기능 (중복 방지)
    public static String renameFile(String sDirectory, String fileName) {
        String ext = fileName.substring(fileName.lastIndexOf(".")); // 확장자 추출
        String newFileName;
        File newFile;

        do {
            // 중복 방지를 위한 새로운 파일명 생성
            String now = new SimpleDateFormat("yyyyMMdd_HmsS").format(new Date());
            newFileName = now + ext;
            newFile = new File(sDirectory + File.separator + newFileName);
        } while (newFile.exists()); // 동일한 이름의 파일이 존재하면 반복

        File oldFile = new File(sDirectory + File.separator + fileName);
        if (oldFile.renameTo(newFile)) {
            System.out.println("파일명 변경 완료: " + newFileName);
        } else {
            System.out.println("파일명 변경 실패.");
        }

        return newFileName; // 변경된 파일명 반환
    }

    // 다중 파일 업로드 처리
    public static ArrayList<String> multipleFile(HttpServletRequest req,
    		String sDirectory) 
    				throws ServletException, IOException {
    	
        ArrayList<String> listFileName = new ArrayList<>();
        Collection<Part> parts = req.getParts();

        for (Part part : parts) {
            if (!"o_file".equals(part.getName()) || part.getSize() == 0) {
                continue; // 파일이 아니거나 빈 파일이면 건너뜀
            }

            String partHeader = part.getHeader("content-disposition");
            System.out.println("partHeader=" + partHeader);

            String[] phArr = partHeader.split("filename=");
            if (phArr.length < 2) continue;

            String originalFileName = phArr[1].trim().replace("\"", "");
            if (!originalFileName.isEmpty()) {
                String filePath = sDirectory + File.separator + originalFileName;
                part.write(filePath);
                System.out.println("파일 저장 완료: " + filePath);

                listFileName.add(originalFileName);
            }
        }
        return listFileName;
    }

    // 파일 다운로드 기능
    public static void download(HttpServletRequest req, HttpServletResponse resp,
                                 String directory, String sfileName, String ofileName, String board_id, String board_type) {
        String sDirectory = req.getServletContext().getRealPath(directory);
        File file = new File(sDirectory, sfileName);

        if (!file.exists()) {
            System.out.println("다운로드 요청된 파일이 존재하지 않습니다: " + sfileName);
            try {
                resp.getWriter().println("<script>alert('파일이 존재하지 않습니다.'); history.back();</script>");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }

        try (InputStream iStream = new FileInputStream(file);
             OutputStream oStream = resp.getOutputStream()) {

            // 한글 파일명 처리
            String client = req.getHeader("User-Agent");
            if (client.indexOf("WOW64") == -1) {
                ofileName = new String(ofileName.getBytes("UTF-8"), "ISO-8859-1");
            } else {
                ofileName = new String(ofileName.getBytes("KSC5601"), "ISO-8859-1");
            }

            resp.reset();
            resp.setContentType("application/octet-stream");
            resp.setHeader("Content-Disposition", "attachment; filename=\"" + ofileName + "\"");
            resp.setHeader("Content-Length", String.valueOf(file.length()));

            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = iStream.read(buffer)) != -1) {
                oStream.write(buffer, 0, bytesRead);
            }

            System.out.println("파일 다운로드 완료: " + sfileName);

        } catch (IOException e) {
            System.out.println("파일 다운로드 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // 파일 삭제 기능
    public static void deleteFile(HttpServletRequest req, String directory, String filename) {
        String sDirectory = req.getServletContext().getRealPath(directory);
        File file = new File(sDirectory + File.separator + filename);

        if (file.exists()) {
            if (file.delete()) {
                System.out.println("파일 삭제 완료: " + filename);
            } else {
                System.out.println("파일 삭제 실패: " + filename);
            }
        } else {
            System.out.println("삭제할 파일이 존재하지 않습니다: " + filename);
        }
    }
}
