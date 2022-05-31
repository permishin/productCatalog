package com.example.productcatalog.ftp;

import java.io.*;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FTPConnectAndLogin {

private static void showServerReply(FTPClient ftpClient) {
        String[] replies = ftpClient.getReplyStrings();
        if (replies != null && replies.length > 0) {
        for (String aReply : replies) {
        System.out.println("SERVER: " + aReply);
        }
        }
        }

        private static File convertMultiPartToFile(MultipartFile file)  {
                File convFile = new File(file.getOriginalFilename());

                FileOutputStream fos = null;
                try {
                        fos = new FileOutputStream(convFile);
                        fos.write(file.getBytes());
                        fos.close();
                } catch (FileNotFoundException e) {
                        e.printStackTrace();
                } catch (IOException e) {
                        e.printStackTrace();
                }
                return convFile;
        }

public synchronized static void downloadToServer(MultipartFile localFileNameDestination, String remoteFileNameDestination, String fileToTransfer) {

        File file = convertMultiPartToFile(localFileNameDestination);

        String server = "ftp.covi626648.nichost.ru";
        int port = 21;
        String user = "covi626648_ftp";
        String pass = "EM2MthHbIm";

        FTPClient ftpClient = new FTPClient();
        try {
        ftpClient.connect(server, port);
        ftpClient.login(user, pass);
        ftpClient.enterLocalPassiveMode();

        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

        String secondRemoteFile = remoteFileNameDestination;
        InputStream inputStream = new FileInputStream(file);

        System.out.println("Start uploading " + fileToTransfer + " file");
        OutputStream outputStream = ftpClient.storeFileStream(secondRemoteFile);
        byte[] bytesIn = new byte[4096];
        int read = 0;

        while ((read = inputStream.read(bytesIn)) != -1) {
        outputStream.write(bytesIn, 0, read);
        }
        inputStream.close();
        outputStream.close();

        boolean completed = ftpClient.completePendingCommand();
        if (completed) {
        System.out.println("The file is uploaded successfully.");
        }

        } catch (IOException ex) {
        System.out.println("Error: " + ex.getMessage());
        ex.printStackTrace();
        } finally {
        try {
        if (ftpClient.isConnected()) {
        ftpClient.logout();
        ftpClient.disconnect();
        }
        } catch (IOException ex) {
        ex.printStackTrace();
        }
        }
        }

public synchronized void deleteFromServer(String remoteDestination,
        String fileNameToDelete) {
//        String server = System.getenv("FtpServer");
//        int port = 21;
//        String user = System.getenv("FtpLogin");
//        String pass = System.getenv("FtpPass");

        String server = "ftp.covi626648.nichost.ru";
        int port = 21;
        String user = "covi626648_ftp";
        String pass = "EM2MthHbIm";

        FTPClient ftpClient = new FTPClient();
        try {

        ftpClient.connect(server, port);

        int replyCode = ftpClient.getReplyCode();
        if (!FTPReply.isPositiveCompletion(replyCode)) {
        System.out.println("Connect failed");
        return;
        }

        boolean success = ftpClient.login(user, pass);

        if (!success) {
        System.out.println("Could not login to the server");
        return;
        }
        String fileToDelete = "gemotest-laboratory.com/docs/" + remoteDestination + "/" + fileNameToDelete;

        boolean deleted = ftpClient.deleteFile(fileToDelete);
        if (deleted) {
        System.out.println("The file was deleted successfully.");
        } else {
        System.out.println("Could not delete the  file, it may not exist.");
        }

        } catch (IOException ex) {
        System.out.println("Oh no, there was an error: " + ex.getMessage());
        ex.printStackTrace();
        } finally {
        // logs out and disconnects from server
        try {
        if (ftpClient.isConnected()) {
        ftpClient.logout();
        ftpClient.disconnect();
        }
        } catch (IOException ex) {
        ex.printStackTrace();
        }
        }
        }
        }
